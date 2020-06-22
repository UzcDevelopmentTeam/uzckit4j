package uzc.kit.entity.response.attachment;

import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.service.impl.grpc.BrsApi;

// TODO this is currently the default for unsupported types
public class OrdinaryPaymentAttachment extends TransactionAttachment {
    public OrdinaryPaymentAttachment(int version) {
        super(version);
    }
}
