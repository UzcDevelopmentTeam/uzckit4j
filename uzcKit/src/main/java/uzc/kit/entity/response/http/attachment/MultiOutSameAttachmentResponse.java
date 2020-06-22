package uzc.kit.entity.response.http.attachment;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.entity.response.attachment.MultiOutSameAttachment;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public final class MultiOutSameAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.MultiSameOutCreation")
    private final int version;
    private final String[] recipients;

    public MultiOutSameAttachmentResponse(int version, String[] recipients) {
        this.version = version;
        this.recipients = recipients;
    }

    public int getVersion() {
        return version;
    }

    public String[] getRecipients() {
        return recipients;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new MultiOutSameAttachment(version, Arrays.stream(recipients)
                .map(UzcAddress::fromId)
                .toArray(UzcAddress[]::new));
    }
}
