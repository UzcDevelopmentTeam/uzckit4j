package uzc.kit.entity.response;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.http.AccountResponse;
import uzc.kit.service.impl.grpc.BrsApi;
import org.bouncycastle.util.encoders.Hex;

public class Account {
    private final UzcAddress id;
    private final UzcValue balance;
    private final UzcValue forgedBalance;
    private final UzcValue unconfirmedBalance;
    private final byte[] publicKey;
    private final String description;
    private final String name;

    public Account(UzcAddress id, UzcValue balance, UzcValue forgedBalance, UzcValue unconfirmedBalance, byte[] publicKey, String description, String name) {
        this.id = id;
        this.balance = balance;
        this.forgedBalance = forgedBalance;
        this.unconfirmedBalance = unconfirmedBalance;
        this.publicKey = publicKey;
        this.description = description;
        this.name = name;
    }

    public Account(AccountResponse accountResponse) {
        this.id = UzcAddress.fromEither(accountResponse.getAccount());
        this.balance = UzcValue.fromPlanck(accountResponse.getBalanceNQT());
        this.forgedBalance = UzcValue.fromPlanck(accountResponse.getForgedBalanceNQT());
        this.unconfirmedBalance = UzcValue.fromPlanck(accountResponse.getUnconfirmedBalanceNQT());
        this.publicKey = accountResponse.getPublicKey() == null ? new byte[32] : Hex.decode(accountResponse.getPublicKey());
        this.description = accountResponse.getDescription();
        this.name = accountResponse.getName();
    }

    public Account(BrsApi.Account account) {
        this.id = UzcAddress.fromId(account.getId());
        this.balance = UzcValue.fromPlanck(account.getBalance());
        this.forgedBalance = UzcValue.fromUzc(account.getForgedBalance());
        this.unconfirmedBalance = UzcValue.fromUzc(account.getUnconfirmedBalance());
        this.publicKey = account.getPublicKey().toByteArray();
        this.description = account.getDescription();
        this.name = account.getName();
    }

    public UzcAddress getId() {
        return id;
    }

    public UzcValue getBalance() {
        return balance;
    }

    public UzcValue getForgedBalance() {
        return forgedBalance;
    }

    public UzcValue getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
