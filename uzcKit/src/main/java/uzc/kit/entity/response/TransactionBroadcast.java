package uzc.kit.entity.response;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.response.http.BroadcastTransactionResponse;
import uzc.kit.service.impl.grpc.BrsApi;
import org.bouncycastle.util.encoders.Hex;

public class TransactionBroadcast {
    private final byte[] fullHash;
    private final UzcID transactionId;
    private final int numberPeersSentTo;

    public TransactionBroadcast(byte[] fullHash, UzcID transactionId, int numberPeersSentTo) {
        this.fullHash = fullHash;
        this.transactionId = transactionId;
        this.numberPeersSentTo = numberPeersSentTo;
    }

    public TransactionBroadcast(BroadcastTransactionResponse response) {
        this.fullHash = Hex.decode(response.getFullHash());
        this.transactionId = UzcID.fromLong(response.getTransactionID());
        this.numberPeersSentTo = response.getNumberPeersSentTo();
    }

    public TransactionBroadcast(BrsApi.TransactionBroadcastResult transactionBroadcastResult, byte[] transactionBytes) {
        UzcCrypto uzcCrypto = UzcCrypto.getInstance();
        this.fullHash = uzcCrypto.getSha256().digest(transactionBytes);
        this.transactionId = uzcCrypto.hashToId(this.fullHash);
        this.numberPeersSentTo = transactionBroadcastResult.getNumberOfPeersSentTo();
    }

    public byte[] getFullHash() {
        return fullHash;
    }

    public UzcID getTransactionId() {
        return transactionId;
    }

    public int getNumberPeersSentTo() {
        return numberPeersSentTo;
    }
}
