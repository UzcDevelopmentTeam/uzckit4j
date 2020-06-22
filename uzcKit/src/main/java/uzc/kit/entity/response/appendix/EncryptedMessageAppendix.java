package uzc.kit.entity.response.appendix;

import uzc.kit.entity.UzcEncryptedMessage;
import uzc.kit.entity.response.TransactionAppendix;
import uzc.kit.service.impl.grpc.BrsApi;

public abstract class EncryptedMessageAppendix extends TransactionAppendix {
    private final UzcEncryptedMessage encryptedMessage;

    private EncryptedMessageAppendix(int version, UzcEncryptedMessage encryptedMessage) {
        super(version);
        this.encryptedMessage = encryptedMessage;
    }

    private static UzcEncryptedMessage encryptedMessageFromProtobuf(BrsApi.EncryptedData encryptedData, boolean isText) {
        return new UzcEncryptedMessage(encryptedData.getData().toByteArray(), encryptedData.getNonce().toByteArray(), isText);
    }

    public static EncryptedMessageAppendix fromProtobuf(BrsApi.EncryptedMessageAppendix encryptedMessageAppendix) {
        switch(encryptedMessageAppendix.getType()) {
            case TO_RECIPIENT:
                return new ToRecipient(encryptedMessageAppendix.getVersion(), encryptedMessageFromProtobuf(encryptedMessageAppendix.getEncryptedData(), encryptedMessageAppendix.getIsText()));
            case TO_SELF:
                return new ToRecipient(encryptedMessageAppendix.getVersion(), encryptedMessageFromProtobuf(encryptedMessageAppendix.getEncryptedData(), encryptedMessageAppendix.getIsText()));
            default:
                throw new IllegalArgumentException("Invalid type");
        }
    }

    public UzcEncryptedMessage getEncryptedMessage() {
        return encryptedMessage;
    }

    public static class ToRecipient extends EncryptedMessageAppendix {
        public ToRecipient(int version, UzcEncryptedMessage encryptedMessage) {
            super(version, encryptedMessage);
        }
    }

    public static class ToSelf extends EncryptedMessageAppendix {
        public ToSelf(int version, UzcEncryptedMessage encryptedMessage) {
            super(version, encryptedMessage);
        }
    }
}
