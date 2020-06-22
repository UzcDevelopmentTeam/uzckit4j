package uzc.kit.entity.response.http;

import uzc.kit.entity.UzcEncryptedMessage;
import org.bouncycastle.util.encoders.Hex;

public class EncryptedMessageResponse {
    private final String data;
    private final String nonce;
    private final boolean isText;

    public EncryptedMessageResponse(String data, String nonce, boolean isText) {
        this.data = data;
        this.nonce = nonce;
        this.isText = isText;
    }

    public UzcEncryptedMessage toEncryptedMessage() {
        return new UzcEncryptedMessage(Hex.decode(data), Hex.decode(nonce), isText);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return nonce;
    }

    public boolean isText() {
        return isText;
    }
}
