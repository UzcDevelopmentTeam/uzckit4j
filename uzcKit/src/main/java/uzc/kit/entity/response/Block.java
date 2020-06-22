package uzc.kit.entity.response;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcTimestamp;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.http.BlockResponse;
import uzc.kit.service.impl.grpc.BrsApi;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Block {
    private final BigInteger nonce;
    private final UzcAddress generator;
    private final UzcID id;
    private final UzcID nextBlock;
    private final UzcID previousBlock;
    private final UzcID[] transactions;
    private final UzcTimestamp timestamp;
    private final UzcValue blockReward;
    private final UzcValue totalAmount;
    private final UzcValue totalFee;
    private final byte[] generationSignature;
    private final byte[] generatorPublicKey;
    private final byte[] payloadHash;
    private final byte[] previousBlockHash;
    private final byte[] signature;
    private final int height;
    private final int payloadLength;
    private final int scoopNum;
    private final int version;
    private final long baseTarget;

    public Block(BigInteger nonce, UzcAddress generator, UzcID id, UzcID nextBlock, UzcID previousBlock, UzcID[] transactions, UzcTimestamp timestamp, UzcValue blockReward, UzcValue totalAmount, UzcValue totalFee, byte[] generationSignature, byte[] generatorPublicKey, byte[] payloadHash, byte[] previousBlockHash, byte[] signature, int height, int payloadLength, int scoopNum, int version, long baseTarget) {
        this.nonce = nonce;
        this.generator = generator;
        this.id = id;
        this.nextBlock = nextBlock;
        this.previousBlock = previousBlock;
        this.transactions = transactions;
        this.timestamp = timestamp;
        this.blockReward = blockReward;
        this.totalAmount = totalAmount;
        this.totalFee = totalFee;
        this.generationSignature = generationSignature;
        this.generatorPublicKey = generatorPublicKey;
        this.payloadHash = payloadHash;
        this.previousBlockHash = previousBlockHash;
        this.signature = signature;
        this.height = height;
        this.payloadLength = payloadLength;
        this.scoopNum = scoopNum;
        this.version = version;
        this.baseTarget = baseTarget;
    }

    public Block(BlockResponse blockResponse) {
        this.nonce = new BigInteger(blockResponse.getNonce());
        this.generator = UzcAddress.fromEither(blockResponse.getGenerator());
        this.id = UzcID.fromLong(blockResponse.getBlock());
        this.nextBlock = UzcID.fromLong(blockResponse.getNextBlock());
        this.previousBlock = UzcID.fromLong(blockResponse.getPreviousBlock());
        this.transactions = Arrays.stream(blockResponse.getTransactions())
                .map(UzcID::fromLong)
                .toArray(UzcID[]::new);
        this.timestamp = new UzcTimestamp(blockResponse.getTimestamp());
        this.blockReward = UzcValue.fromUzc(blockResponse.getBlockReward());
        this.totalAmount = UzcValue.fromPlanck(blockResponse.getTotalAmountNQT());
        this.totalFee = UzcValue.fromPlanck(blockResponse.getTotalFeeNQT());
        this.generationSignature = Hex.decode(blockResponse.getGenerationSignature());
        this.generatorPublicKey = Hex.decode(blockResponse.getGeneratorPublicKey());
        this.payloadHash = Hex.decode(blockResponse.getPayloadHash());
        this.previousBlockHash = Hex.decode(blockResponse.getPreviousBlockHash());
        this.signature = Hex.decode(blockResponse.getBlockSignature());
        this.height = blockResponse.getHeight();
        this.payloadLength = blockResponse.getPayloadLength();
        this.scoopNum = blockResponse.getScoopNum();
        this.version = blockResponse.getVersion();
        this.baseTarget = blockResponse.getBaseTarget();
    }

    public Block(BrsApi.Block block) {
        UzcCrypto uzcCrypto = UzcCrypto.getInstance();
        this.nonce = new BigInteger(Long.toUnsignedString(block.getNonce()));
        this.generator = uzcCrypto.getUzcAddressFromPublic(block.getGeneratorPublicKey().toByteArray());
        this.id = UzcID.fromLong(block.getId());
        this.nextBlock = UzcID.fromLong(block.getNextBlockId());
        this.previousBlock = block.getPreviousBlockHash().size() == 0 ? UzcID.fromLong(0) : uzcCrypto.hashToId(block.getPreviousBlockHash().toByteArray());
        this.transactions = block.getTransactionIdsList()
                .stream()
                .map(UzcID::fromLong)
                .toArray(UzcID[]::new);
        this.timestamp = new UzcTimestamp(block.getTimestamp());
        this.blockReward = UzcValue.fromPlanck(block.getBlockReward());
        this.totalAmount = UzcValue.fromPlanck(block.getTotalAmount());
        this.totalFee = UzcValue.fromPlanck(block.getTotalFee());
        this.generationSignature = block.getGenerationSignature().toByteArray();
        this.generatorPublicKey = block.getGeneratorPublicKey().toByteArray();
        this.payloadHash = block.getPayloadHash().toByteArray();
        this.previousBlockHash = block.getPreviousBlockHash().toByteArray();
        this.signature = block.getBlockSignature().toByteArray();
        this.height = block.getHeight();
        this.payloadLength = block.getPayloadLength();
        this.scoopNum = block.getScoop();
        this.version = block.getVersion();
        this.baseTarget = block.getBaseTarget();
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public UzcAddress getGenerator() {
        return generator;
    }

    public UzcID getId() {
        return id;
    }

    public UzcID getNextBlock() {
        return nextBlock;
    }

    public UzcID getPreviousBlock() {
        return previousBlock;
    }

    public UzcID[] getTransactions() {
        return transactions;
    }

    public UzcTimestamp getTimestamp() {
        return timestamp;
    }

    public UzcValue getBlockReward() {
        return blockReward;
    }

    public UzcValue getTotalAmount() {
        return totalAmount;
    }

    public UzcValue getTotalFee() {
        return totalFee;
    }

    public byte[] getGenerationSignature() {
        return generationSignature;
    }

    public byte[] getGeneratorPublicKey() {
        return generatorPublicKey;
    }

    public byte[] getPayloadHash() {
        return payloadHash;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public int getHeight() {
        return height;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public int getScoopNum() {
        return scoopNum;
    }

    public int getVersion() {
        return version;
    }

    public long getBaseTarget() {
        return baseTarget;
    }
}
