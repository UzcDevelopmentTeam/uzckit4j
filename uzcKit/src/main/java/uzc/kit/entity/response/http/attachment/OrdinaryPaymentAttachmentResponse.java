package uzc.kit.entity.response.http.attachment;

import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.entity.response.attachment.OrdinaryPaymentAttachment;

@SuppressWarnings("WeakerAccess")
public final class OrdinaryPaymentAttachmentResponse extends TransactionAttachmentResponse {
    OrdinaryPaymentAttachmentResponse() {}

    @Override
    public TransactionAttachment toAttachment() {
        return new OrdinaryPaymentAttachment(1);
    }
}
