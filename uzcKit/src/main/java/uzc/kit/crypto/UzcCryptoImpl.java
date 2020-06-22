package uzc.kit.crypto;

import uzc.kit.crypto.ec.Curve25519;
import uzc.kit.crypto.ec.Curve25519Impl;
import uzc.kit.crypto.hash.UzcHashProvider;
import uzc.kit.crypto.hash.shabal.Shabal256;
import uzc.kit.crypto.plot.PlotCalculator;
import uzc.kit.crypto.plot.impl.PlotCalculatorImpl;
import uzc.kit.crypto.rs.ReedSolomon;
import uzc.kit.crypto.rs.ReedSolomonImpl;
import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcEncryptedMessage;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcValue;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class UzcCryptoImpl extends AbstractUzcCrypto {

    static final UzcCryptoImpl INSTANCE = new UzcCryptoImpl();

    private final ThreadLocal<SecureRandom> secureRandom = ThreadLocal.withInitial(SecureRandom::new);
    private final Curve25519 curve25519;
    private final ReedSolomon reedSolomon;
    private final PlotCalculator plotCalculator;
    private final long epochBeginning;

    private UzcCryptoImpl() {
        this.curve25519 = new Curve25519Impl(this::getSha256);
        this.reedSolomon = new ReedSolomonImpl();
        this.plotCalculator = new PlotCalculatorImpl(this::getShabal256);
        this.epochBeginning = calculateEpochBeginning();
        UzcHashProvider.init();
    }

    private long calculateEpochBeginning() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public MessageDigest getSha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return new SHA256.Digest(); // Fallback to Bouncy Castle's implementation
        }
    }

    @Override
    public MessageDigest getShabal256() {
        try {
            return MessageDigest.getInstance("Shabal-256");
        } catch (NoSuchAlgorithmException e) {
            return new Shabal256();
        }
    }

    @Override
    public MessageDigest getRipeMD160() {
        try {
            return MessageDigest.getInstance("RIPEMD-160");
        } catch (NoSuchAlgorithmException e) {
            return new RIPEMD160.Digest(); // Fallback to Bouncy Castle's implementation
        }
    }

    @Override
    public byte[] getPrivateKey(String passphrase) {
        byte[] privateKey = getSha256().digest(stringToBytes(passphrase));
        curve25519.clampPrivateKey(privateKey);
        return privateKey;
    }

    @Override
    public byte[] getPublicKey(byte[] privateKey) {
        return curve25519.getPublicKey(privateKey);
    }

    @Override
    public UzcAddress getUzcAddressFromPublic(byte[] publicKey) {
        return UzcAddress.fromId(hashToId(getSha256().digest(publicKey)));
    }

    @Override
    public UzcID hashToId(byte[] hash) throws IllegalArgumentException {
        if (hash == null || hash.length < 8) {
            throw new IllegalArgumentException("Invalid hash: " + Arrays.toString(hash));
        }
        return UzcID.fromLong(bytesToLong(hash));
    }

    @Override
    public byte[] getSharedSecret(byte[] myPrivateKey, byte[] theirPublicKey) {
        return curve25519.getSharedSecret(myPrivateKey, theirPublicKey);
    }

    @Override
    public byte[] sign(byte[] message, byte[] privateKey) {
        return curve25519.sign(message, privateKey);
    }

    @Override
    public byte[] signTransaction(byte[] privateKey, byte[] unsignedTransaction) {
        byte[] signature = sign(unsignedTransaction, privateKey);
        byte[] signedTransaction = new byte[unsignedTransaction.length];
        System.arraycopy(unsignedTransaction, 0, signedTransaction, 0, unsignedTransaction.length); // Duplicate the transaction
        System.arraycopy(signature, 0, signedTransaction, 96, 64); // Insert the signature
        return signedTransaction;
    }

    @Override
    public boolean verify(byte[] signature, byte[] message, byte[] publicKey, boolean enforceCanonical) {
        return curve25519.verify(message, signature, publicKey, enforceCanonical);
    }

    @Override
    public byte[] aesEncrypt(byte[] plaintext, byte[] signingKey, byte[] nonce) throws IllegalArgumentException {
        if (signingKey.length != 32) {
            throw new IllegalArgumentException("Key length must be 32 bytes");
        }
        try {
            for (int i = 0; i < 32; i++) {
                signingKey[i] ^= nonce[i];
            }
            byte[] key = getSha256().digest(signingKey);
            byte[] iv = new byte[16];
            secureRandom.get().nextBytes(iv);
            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(true, ivAndKey);
            byte[] output = new byte[aes.getOutputSize(plaintext.length)];
            int ciphertextLength = aes.processBytes(plaintext, 0, plaintext.length, output, 0);
            ciphertextLength += aes.doFinal(output, ciphertextLength);
            byte[] result = new byte[iv.length + ciphertextLength];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(output, 0, result, iv.length, ciphertextLength);
            return result;
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] aesDecrypt(byte[] encrypted, byte[] signingKey, byte[] nonce) throws IllegalArgumentException {
        if (signingKey.length != 32) {
            throw new IllegalArgumentException("Key length must be 32 bytes");
        }
        try {
            if (encrypted.length < 16 || encrypted.length % 16 != 0) {
                throw new InvalidCipherTextException("invalid ciphertext"); // TODO
            }
            byte[] iv = Arrays.copyOfRange(encrypted, 0, 16);
            byte[] ciphertext = Arrays.copyOfRange(encrypted, 16, encrypted.length);
            for (int i = 0; i < 32; i++) {
                signingKey[i] ^= nonce[i];
            }
            byte[] key = getSha256().digest(signingKey);
            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(false, ivAndKey);
            byte[] output = new byte[aes.getOutputSize(ciphertext.length)];
            int plaintextLength = aes.processBytes(ciphertext, 0, ciphertext.length, output, 0);
            plaintextLength += aes.doFinal(output, plaintextLength);
            byte[] result = new byte[plaintextLength];
            System.arraycopy(output, 0, result, 0, result.length);
            return result;
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public UzcEncryptedMessage encryptBytesMessage(byte[] message, byte[] myPrivateKey, byte[] theirPublicKey) {
        return encryptPlainMessage(message, false, myPrivateKey, theirPublicKey);
    }

    @Override
    public UzcEncryptedMessage encryptTextMessage(String message, byte[] myPrivateKey, byte[] theirPublicKey) {
        return encryptPlainMessage(stringToBytes(message), true, myPrivateKey, theirPublicKey);
    }

    private UzcEncryptedMessage encryptPlainMessage(byte[] message, boolean isText, byte[] myPrivateKey, byte[] theirPublicKey) {
        if (message.length == 0) {
            return new UzcEncryptedMessage(new byte[0], new byte[0], isText);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(message);
            gzip.flush();
            gzip.close();
            byte[] compressedPlaintext = bos.toByteArray();
            byte[] nonce = new byte[32];
            secureRandom.get().nextBytes(nonce);
            byte[] data = aesSharedEncrypt(compressedPlaintext, myPrivateKey, theirPublicKey, nonce);
            return new UzcEncryptedMessage(data, nonce, isText);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] decryptMessage(UzcEncryptedMessage message, byte[] myPrivateKey, byte[] theirPublicKey) {
        if (message.getData().length == 0) {
            return message.getData();
        }
        byte[] compressedPlaintext = aesSharedDecrypt(message.getData(), myPrivateKey, theirPublicKey, message.getNonce());
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedPlaintext); GZIPInputStream gzip = new GZIPInputStream(bis); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int nRead;
            while((nRead = gzip.read(buffer, 0, buffer.length)) > 0) {
                bos.write(buffer, 0, nRead);
            }
            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String rsEncode(UzcID uzcID) {
        return reedSolomon.encode(uzcID.getSignedLongId());
    }

    @Override
    public UzcID rsDecode(String rs) throws IllegalArgumentException {
        rs = rs.toUpperCase();
        long rsValue;
        try {
            rsValue = reedSolomon.decode(rs);
        } catch (ReedSolomon.DecodeException e) {
            throw new IllegalArgumentException("Reed-Solomon decode failed", e);
        }
        return UzcID.fromLong(rsValue);
    }

    @Override
    public Date fromEpochTime(long epochTime) {
        return new Date(epochBeginning + epochTime * 1000L);
    }

    @Override
    public long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0, length = Math.min(8, bytes.length)-1; i <= length; i++) {
            result <<= 8;
            result |= (bytes[length-i] & 0xFF);
        }
        return result;
    }

    @Override
    public int bytesToInt(byte[] bytes) {
        int result = 0;
        for (int i = 0, length = Math.min(4, bytes.length)-1; i <= length; i++) {
            result <<= 8;
            result |= (bytes[length-i] & 0xFF);
        }
        return result;
    }

    @Override
    public byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    @Override
    public byte[] intToBytes(int i) {
        byte[] result = new byte[8];
        for (int index = 3; index >= 0; index--) {
            result[index] = (byte)(i & 0xFF);
            i >>= 8;
        }
        return result;
    }

    @Override
    public String toHexString(byte[] bytes) {
        return Hex.toHexString(bytes);
    }

    @Override
    public byte[] parseHexString(String string) {
        return Hex.decode(string);
    }

    @Override
    public byte[] calculateGenerationSignature(byte[] lastGenSig, long lastGenId) {
        return plotCalculator.calculateGenerationSignature(lastGenSig, lastGenId);
    }

    @Override
    public int calculateScoop(byte[] genSig, long height) {
        return plotCalculator.calculateScoop(genSig, height);
    }

    @Override
    public BigInteger calculateHit(long accountId, long nonce, byte[] genSig, int scoop, int pocVersion) {
        return plotCalculator.calculateHit(accountId, nonce, genSig, scoop, pocVersion);
    }

    @Override
    public BigInteger calculateHit(long accountId, long nonce, byte[] genSig, byte[] scoopData) {
        return plotCalculator.calculateHit(accountId, nonce, genSig, scoopData);
    }

    @Override
    public BigInteger calculateDeadline(long accountId, long nonce, byte[] genSig, int scoop, long baseTarget, int pocVersion) {
        return plotCalculator.calculateDeadline(accountId, nonce, genSig, scoop, baseTarget, pocVersion);
    }

    private void putLength(int nPages, int length, ByteBuffer buffer) {
        if (nPages * 256 <= 256) {
            buffer.put((byte) length);
        } else if (nPages * 256 <= 32767) {
            buffer.putShort((short) length);
        } else {
            buffer.putInt(length);
        }
    }

    @Override
    public byte[] getATCreationBytes(short atVersion, byte[] code, byte[] data, int dPages, int csPages, int usPages, UzcValue minActivationAmount) {
        int cPages = (code.length / 256) + ((code.length % 256) != 0 ? 1 : 0);

        if (dPages < 0 || csPages < 0 || usPages < 0) {
            throw new IllegalArgumentException();
        }

        long minActivationAmountPlanck = minActivationAmount.toPlanck().longValueExact();

        int creationLength = 4; // version + reserved
        creationLength += 8; // pages
        creationLength += 8; // minActivationAmount
        creationLength += cPages * 256 <= 256 ? 1 : (cPages * 256 <= 32767 ? 2 : 4); // code size
        creationLength += code.length;
        creationLength += dPages * 256 <= 256 ? 1 : (dPages * 256 <= 32767 ? 2 : 4); // data size
        creationLength += data.length;

        ByteBuffer creation = ByteBuffer.allocate(creationLength);
        creation.order(ByteOrder.LITTLE_ENDIAN);
        creation.putShort(atVersion);
        creation.putShort((short) 0);
        creation.putShort((short) cPages);
        creation.putShort((short) dPages);
        creation.putShort((short) csPages);
        creation.putShort((short) usPages);
        creation.putLong(minActivationAmountPlanck);
        putLength(cPages, code.length, creation);
        creation.put(code);
        putLength(dPages, data.length, creation);
        creation.put(data);
        return creation.array();
    }
}
