package uzc.kit.entity.response.http.attachment;

import uzc.kit.entity.UzcEncryptedMessage;
import uzc.kit.entity.response.TransactionAppendix;
import uzc.kit.entity.response.appendix.EncryptedMessageAppendix;
import uzc.kit.entity.response.http.EncryptedMessageResponse;
import com.google.gson.annotations.SerializedName;

public class EncryptedMessageAttachmentResponse extends TransactionAppendixResponse {
    private final EncryptedMessageResponse encryptedMessage;
    @SerializedName("version.EncryptedMessage")
    private final int version;

    public EncryptedMessageAttachmentResponse(EncryptedMessageResponse encryptedMessage, int version) {
        this.encryptedMessage = encryptedMessage;
        this.version = version;
    }

    public EncryptedMessageResponse getEncryptedMessage() {
        return encryptedMessage;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public TransactionAppendix toAppendix() {
        return new EncryptedMessageAppendix.ToRecipient(version, encryptedMessage.toEncryptedMessage());
    }
}
