package uzc.kit.entity.response.attachment;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.service.impl.grpc.BrsApi;

public class MultiOutSameAttachment extends TransactionAttachment {
    private UzcAddress[] recipients;

    public MultiOutSameAttachment(int version, UzcAddress[] recipients) {
        super(version);
        this.recipients = recipients;
    }

    public MultiOutSameAttachment(BrsApi.MultiOutSameAttachment multiOutSameAttachment) {
        super(multiOutSameAttachment.getVersion());
        this.recipients = multiOutSameAttachment.getRecipientsList()
                .stream()
                .map(UzcAddress::fromId)
                .toArray(UzcAddress[]::new);
    }

    public UzcAddress[] getRecipients() {
        return recipients;
    }
}
