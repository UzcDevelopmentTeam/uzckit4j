package uzc.kit.entity.response.attachment;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.service.impl.grpc.BrsApi;

import java.util.Map;
import java.util.stream.Collectors;

public class MultiOutAttachment extends TransactionAttachment {
    private final Map<UzcAddress, UzcValue> outputs;

    public MultiOutAttachment(int version, Map<UzcAddress, UzcValue> outputs) {
        super(version);
        this.outputs = outputs;
    }

    public MultiOutAttachment(BrsApi.MultiOutAttachment multiOutAttachment) {
        super(multiOutAttachment.getVersion());
        this.outputs = multiOutAttachment.getRecipientsList()
                .stream()
                .collect(Collectors.toMap(recipient -> UzcAddress.fromId(recipient.getRecipient()), recipient -> UzcValue.fromPlanck(recipient.getAmount())));
    }

    public Map<UzcAddress, UzcValue> getOutputs() {
        return outputs;
    }
}
