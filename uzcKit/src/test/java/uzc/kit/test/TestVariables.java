package uzc.kit.test;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcTimestamp;
import org.bouncycastle.util.encoders.Hex;

public class TestVariables {
    public static final int EXAMPLE_BLOCK_HEIGHT = 470000;
    public static final UzcTimestamp EXAMPLE_TIMESTAMP = new UzcTimestamp(126144000); // 4 years
    public static final UzcID EXAMPLE_BLOCK_ID = UzcID.fromLong("9466704733664017405");
    public static final UzcAddress EXAMPLE_ACCOUNT_ID = UzcAddress.fromId(UzcID.fromLong("7009665667967103287"));
    public static final UzcAddress EXAMPLE_POOL_ACCOUNT_ID = UzcAddress.fromId(UzcID.fromLong("888561138747819634"));
    public static final byte[] EXAMPLE_ACCOUNT_PUBKEY = Hex.decode("34d010e80c0d6dc409f8d7a99d0815bfbff9387909a9fca4c65253ec44fad360");
    public static final String EXAMPLE_ACCOUNT_RS = "UZC-W5YR-ZZQC-KUBJ-G78KB";

    public static final UzcID EXAMPLE_TRANSACTION_ID = UzcID.fromLong("10489995701880641892");
    public static final UzcID EXAMPLE_AT_CREATION_TRANSACTION_ID = UzcID.fromLong("3474457271106823767");
    public static final UzcID EXAMPLE_MULTI_OUT_TRANSACTION_ID = UzcID.fromLong("3631659512270044993");
    public static final UzcID EXAMPLE_MULTI_OUT_SAME_TRANSACTION_ID = UzcID.fromLong("5032020914938737522");
    public static final byte[] EXAMPLE_TRANSACTION_FULL_HASH = Hex.decode("e475946429c220d33f414a9d6106452547abe23ee1379fc38f571cac1c037c6f");

    public static final UzcAddress EXAMPLE_AT_ID = UzcAddress.fromId("3474457271106823767");

    public static final String EXAMPLE_AT_LONG_HEX2LONG = "9900958322455989675";
}
