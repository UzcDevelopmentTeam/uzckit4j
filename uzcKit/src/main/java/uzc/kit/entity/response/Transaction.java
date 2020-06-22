package uzc.kit.entity.response;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcTimestamp;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.attachment.OrdinaryPaymentAttachment;
import uzc.kit.entity.response.http.TransactionResponse;
import uzc.kit.entity.response.http.attachment.TransactionAppendixResponse;
import uzc.kit.service.impl.grpc.BrsApi;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

public class Transaction {
    private final UzcAddress recipient;
    private final UzcAddress sender;
    private final UzcID blockId;
    private final UzcID ecBlockId;
    private final UzcID id;
    private final UzcTimestamp blockTimestamp;
    private final UzcTimestamp timestamp;
    private final UzcValue amount;
    private final UzcValue fee;
    private final byte[] fullHash;
    private final byte[] referencedTransactionFullHash;
    private final byte[] senderPublicKey;
    private final byte[] signature;
    private final byte[] signatureHash;
    private final int blockHeight;
    private final int confirmations;
    private final int ecBlockHeight;
    private final int subtype;
    private final int type;
    private final int version;
    private final TransactionAttachment attachment;
    private final TransactionAppendix[] appendages;
    private final short deadline;

    public Transaction(UzcAddress recipient, UzcAddress sender, UzcID blockId, UzcID ecBlockId, UzcID id, UzcTimestamp blockTimestamp, UzcTimestamp timestamp, UzcValue amount, UzcValue fee, byte[] fullHash, byte[] referencedTransactionFullHash, byte[] senderPublicKey, byte[] signature, byte[] signatureHash, int blockHeight, int confirmations, int ecBlockHeight, int subtype, int type, int version, TransactionAttachment attachment, TransactionAppendix[] appendages, short deadline) {
        this.recipient = recipient;
        this.sender = sender;
        this.blockId = blockId;
        this.ecBlockId = ecBlockId;
        this.id = id;
        this.blockTimestamp = blockTimestamp;
        this.timestamp = timestamp;
        this.amount = amount;
        this.fee = fee;
        this.fullHash = fullHash;
        this.referencedTransactionFullHash = referencedTransactionFullHash;
        this.senderPublicKey = senderPublicKey;
        this.signature = signature;
        this.signatureHash = signatureHash;
        this.blockHeight = blockHeight;
        this.confirmations = confirmations;
        this.ecBlockHeight = ecBlockHeight;
        this.subtype = subtype;
        this.type = type;
        this.version = version;
        this.attachment = attachment;
        this.appendages = appendages;
        this.deadline = deadline;
    }

    public Transaction(TransactionResponse transactionResponse) {
        this.recipient = UzcAddress.fromEither(transactionResponse.getRecipient());
        this.sender = UzcAddress.fromEither(transactionResponse.getSender());
        this.blockId = UzcID.fromLong(transactionResponse.getBlock());
        this.ecBlockId = UzcID.fromLong(transactionResponse.getEcBlockId());
        this.id = UzcID.fromLong(transactionResponse.getTransaction());
        this.blockTimestamp = new UzcTimestamp(transactionResponse.getBlockTimestamp());
        this.timestamp = new UzcTimestamp(transactionResponse.getTimestamp());
        this.amount = UzcValue.fromPlanck(transactionResponse.getAmountNQT());
        this.fee = UzcValue.fromPlanck(transactionResponse.getFeeNQT());
        this.fullHash = Hex.decode(transactionResponse.getFullHash());
        this.referencedTransactionFullHash = transactionResponse.getReferencedTransactionFullHash() == null ? null : Hex.decode(transactionResponse.getReferencedTransactionFullHash());
        this.senderPublicKey = Hex.decode(transactionResponse.getSenderPublicKey());
        this.signature = Hex.decode(transactionResponse.getSignature());
        this.signatureHash = Hex.decode(transactionResponse.getSignatureHash());
        this.blockHeight = transactionResponse.getHeight();
        this.confirmations = transactionResponse.getConfirmations();
        this.ecBlockHeight = transactionResponse.getEcBlockHeight();
        this.subtype = transactionResponse.getSubtype();
        this.type = transactionResponse.getType();
        this.version = transactionResponse.getVersion();
        this.attachment = transactionResponse.getAttachment() == null ? new OrdinaryPaymentAttachment(transactionResponse.getVersion()) : transactionResponse.getAttachment().getAttachment().toAttachment();
        this.appendages = transactionResponse.getAttachment() == null ? new TransactionAppendix[0] : Arrays.stream(transactionResponse.getAttachment().getAppendages())
                .map(TransactionAppendixResponse::toAppendix)
                .toArray(TransactionAppendix[]::new);
        this.deadline = transactionResponse.getDeadline();
    }

    public Transaction(BrsApi.Transaction transaction) {
        UzcCrypto uzcCrypto = UzcCrypto.getInstance();
        BrsApi.BasicTransaction basicTransaction = transaction.getTransaction();
        this.recipient = UzcAddress.fromId(transaction.getId());
        this.sender = UzcAddress.fromId(basicTransaction.getSenderId());
        this.blockId = UzcID.fromLong(transaction.getBlock());
        this.ecBlockId = UzcID.fromLong(basicTransaction.getEcBlockId());
        this.id = UzcID.fromLong(transaction.getId());
        this.blockTimestamp = new UzcTimestamp(transaction.getBlockTimestamp());
        this.timestamp = new UzcTimestamp(basicTransaction.getTimestamp());
        this.amount = UzcValue.fromPlanck(basicTransaction.getAmount());
        this.fee = UzcValue.fromPlanck(basicTransaction.getFee());
        this.fullHash = transaction.getFullHash().toByteArray();
        this.referencedTransactionFullHash = basicTransaction.getReferencedTransactionFullHash().toByteArray();
        this.senderPublicKey = basicTransaction.getSenderPublicKey().toByteArray();
        this.signature = basicTransaction.getSignature().toByteArray();
        this.signatureHash = uzcCrypto.getSha256().digest(basicTransaction.getSignature().toByteArray()); // TODO check this is correct
        this.blockHeight = transaction.getBlockHeight();
        this.confirmations = transaction.getConfirmations();
        this.ecBlockHeight = basicTransaction.getEcBlockHeight();
        this.subtype = basicTransaction.getSubtype();
        this.type = basicTransaction.getType();
        this.version = basicTransaction.getVersion();
        this.attachment = TransactionAttachment.fromProtobuf(basicTransaction.getAttachment(), basicTransaction.getVersion());
        this.appendages = basicTransaction.getAppendagesList()
                .stream()
                .map(TransactionAppendix::fromProtobuf)
                .toArray(TransactionAppendix[]::new);
        this.deadline = (short) basicTransaction.getDeadline();
    }

    public UzcAddress getRecipient() {
        return recipient;
    }

    public UzcAddress getSender() {
        return sender;
    }

    public UzcID getBlockId() {
        return blockId;
    }

    public UzcID getEcBlockId() {
        return ecBlockId;
    }

    public UzcID getId() {
        return id;
    }

    public UzcTimestamp getBlockTimestamp() {
        return blockTimestamp;
    }

    public UzcTimestamp getTimestamp() {
        return timestamp;
    }

    public UzcValue getAmount() {
        return amount;
    }

    public UzcValue getFee() {
        return fee;
    }

    public byte[] getFullHash() {
        return fullHash;
    }

    public byte[] getReferencedTransactionFullHash() {
        return referencedTransactionFullHash;
    }

    public byte[] getSenderPublicKey() {
        return senderPublicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getSignatureHash() {
        return signatureHash;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public int getEcBlockHeight() {
        return ecBlockHeight;
    }

    public int getSubtype() {
        return subtype;
    }

    public int getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }

    public TransactionAttachment getAttachment() {
        return attachment;
    }

    public TransactionAppendix[] getAppendages() {
        return appendages;
    }

    public short getDeadline() {
        return deadline;
    }
}
