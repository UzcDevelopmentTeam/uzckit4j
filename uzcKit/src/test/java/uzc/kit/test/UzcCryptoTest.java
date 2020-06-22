package uzc.kit.test;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.entity.UzcEncryptedMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class UzcCryptoTest { // TODO more unit tests
    @Test
    public void TestEncryptTextMessage() {
        String message = "Test message";

        byte[] myPrivateKey = UzcCrypto.getInstance().getPrivateKey("example1");
        byte[] myPublicKey = UzcCrypto.getInstance().getPublicKey(myPrivateKey);
        byte[] theirPrivateKey = UzcCrypto.getInstance().getPrivateKey("example2");
        byte[] theirPublicKey = UzcCrypto.getInstance().getPublicKey(theirPrivateKey);

        UzcEncryptedMessage uzcEncryptedMessage = UzcCrypto.getInstance().encryptTextMessage(message, myPrivateKey, theirPublicKey);

        String result1 = new String(UzcCrypto.getInstance().decryptMessage(uzcEncryptedMessage, myPrivateKey, theirPublicKey));
        String result2 = new String(UzcCrypto.getInstance().decryptMessage(uzcEncryptedMessage, theirPrivateKey, myPublicKey));

        Assert.assertEquals(message, result1);
        Assert.assertEquals(message, result2);
    }

    @Test
    public void TestSignAndVerify() {
        byte[] myMessage = "A Message".getBytes(StandardCharsets.UTF_8);
        byte[] myPrivateKey = UzcCrypto.getInstance().getPrivateKey("example1");
        byte[] myPublic = UzcCrypto.getInstance().getPublicKey(myPrivateKey);
        byte[] signature = UzcCrypto.getInstance().sign(myMessage, myPrivateKey);
        Assert.assertTrue(UzcCrypto.getInstance().verify(signature, myMessage, myPublic, true));
    }
}
