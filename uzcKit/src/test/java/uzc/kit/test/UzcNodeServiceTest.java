package uzc.kit.test;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.*;
import uzc.kit.entity.response.attachment.ATCreationAttachment;
import uzc.kit.entity.response.attachment.MultiOutAttachment;
import uzc.kit.entity.response.attachment.MultiOutSameAttachment;
import uzc.kit.service.UzcNodeService;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class UzcNodeServiceTest {

    private final UzcNodeService uzcNodeService = getUzcNodeService();
    private final UzcCrypto uzcCrypto = UzcCrypto.getInstance();

    protected abstract UzcNodeService getUzcNodeService();

    @Test
    public void testUzcServiceGetBlock() {
        Block blockIDResponse = RxTestUtils.testSingle(uzcNodeService.getBlock(TestVariables.EXAMPLE_BLOCK_ID));
        Block blockHeightResponse = RxTestUtils.testSingle(uzcNodeService.getBlock(TestVariables.EXAMPLE_BLOCK_HEIGHT));
        Block blockTimestampResponse = RxTestUtils.testSingle(uzcNodeService.getBlock(TestVariables.EXAMPLE_TIMESTAMP));
    }

    @Test
    public void testUzcServiceGetBlockID() {
        UzcID blockIDResponse = RxTestUtils.testSingle(uzcNodeService.getBlockId(TestVariables.EXAMPLE_BLOCK_HEIGHT));
    }

    @Test
    public void testUzcServiceGetBlocks() {
        Block[] blocksResponse = RxTestUtils.testSingle(uzcNodeService.getBlocks(0, 99)); // BRS caps this call at 99 blocks.
        //assertEquals(100, blocksResponse.getBlocks().length);
    }

    @Test
    public void testUzcServiceGetConstants() {
        Constants constantsResponse = RxTestUtils.testSingle(uzcNodeService.getConstants());
    }

    @Test
    public void testUzcServiceGetAccount() {
        Account accountResponse = RxTestUtils.testSingle(uzcNodeService.getAccount(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountATs() {
        AT[] accountATsResponse = RxTestUtils.testSingle(uzcNodeService.getAccountATs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountBlockIDs() {
        UzcID[] accountBlockIDsResponse = RxTestUtils.testSingle(uzcNodeService.getAccountBlockIDs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountBlocks() {
        Block[] accountBlocksResponse = RxTestUtils.testSingle(uzcNodeService.getAccountBlocks(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountTransactionIDs() {
        UzcID[] accountTransactionIDsResponse = RxTestUtils.testSingle(uzcNodeService.getAccountTransactionIDs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountTransactions() {
        Transaction[] accountTransactionsResponse = RxTestUtils.testSingle(uzcNodeService.getAccountTransactions(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAccountWithRewardRecipient() {
        UzcAddress[] accountsWithRewardRecipientResponse = RxTestUtils.testSingle(uzcNodeService.getAccountsWithRewardRecipient(TestVariables.EXAMPLE_POOL_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceGetAT() {
        AT accountATsResponse = RxTestUtils.testSingle(uzcNodeService.getAt(TestVariables.EXAMPLE_AT_ID));
    }

    @Test
    public void testUzcServiceGetAtIDs() {
        UzcAddress[] atIDsResponse = RxTestUtils.testSingle(uzcNodeService.getAtIds());
    }

    @Test
    public void testUzcServiceGetTransaction() {
        Transaction transactionIdTransactionResponse = RxTestUtils.testSingle(uzcNodeService.getTransaction(TestVariables.EXAMPLE_TRANSACTION_ID));
        Transaction fullHashTransactionResponse = RxTestUtils.testSingle(uzcNodeService.getTransaction(TestVariables.EXAMPLE_TRANSACTION_FULL_HASH));

        Transaction multiOutTransactionResponse = RxTestUtils.testSingle(uzcNodeService.getTransaction(TestVariables.EXAMPLE_MULTI_OUT_TRANSACTION_ID));
        assertEquals(MultiOutAttachment.class, multiOutTransactionResponse.getAttachment().getClass());
        assertEquals(22, ((MultiOutAttachment) multiOutTransactionResponse.getAttachment()).getOutputs().size());

        Transaction multiOutSameTransactionResponse = RxTestUtils.testSingle(uzcNodeService.getTransaction(TestVariables.EXAMPLE_MULTI_OUT_SAME_TRANSACTION_ID));
        assertEquals(MultiOutSameAttachment.class, multiOutSameTransactionResponse.getAttachment().getClass());
        assertEquals(128, ((MultiOutSameAttachment) multiOutSameTransactionResponse.getAttachment()).getRecipients().length);

        Transaction atCreationTransactionResponse = RxTestUtils.testSingle(uzcNodeService.getTransaction(TestVariables.EXAMPLE_AT_CREATION_TRANSACTION_ID));
        assertEquals(ATCreationAttachment.class, atCreationTransactionResponse.getAttachment().getClass());
    }

    @Test
    public void testUzcServiceGetTransactionBytes() {
        byte[] transactionBytesResponse = RxTestUtils.testSingle(uzcNodeService.getTransactionBytes(TestVariables.EXAMPLE_TRANSACTION_ID));
    }

    @Test
    public void testUzcServiceGenerateTransaction() {
        // TODO test with zero amounts
        byte[] withoutMessageAmount = RxTestUtils.testSingle(uzcNodeService.generateTransaction(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromUzc(1), UzcValue.fromUzc(1), 1440));
        byte[] withStringMessage = RxTestUtils.testSingle(uzcNodeService.generateTransactionWithMessage(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromUzc(1), UzcValue.fromUzc(1), 1440, "Test Transaction"));
        byte[] withBytesMessage = RxTestUtils.testSingle(uzcNodeService.generateTransactionWithMessage(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromUzc(1), UzcValue.fromUzc(1), 1440, TestVariables.EXAMPLE_ACCOUNT_PUBKEY));
    }

    @Test
    public void testUzcServiceSuggestFee() {
        FeeSuggestion suggestFeeResponse = RxTestUtils.testSingle(uzcNodeService.suggestFee());
        assertTrue(suggestFeeResponse.getPriorityFee().compareTo(suggestFeeResponse.getStandardFee()) >= 0);
        assertTrue(suggestFeeResponse.getStandardFee().compareTo(suggestFeeResponse.getCheapFee()) >= 0);
    }

    @Test
    public void testUzcServiceGetMiningInfo() {
        MiningInfo miningInfoResponse = RxTestUtils.testObservable(uzcNodeService.getMiningInfo(), 1).get(0);
    }

    @Test
    public void testUzcServiceGetRewardRecipient() {
        UzcAddress rewardRecipientResponse = RxTestUtils.testSingle(uzcNodeService.getRewardRecipient(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testUzcServiceSubmitNonce() {
        Long submitNonceResponse = RxTestUtils.testSingle(uzcNodeService.submitNonce("example", "0", null));
    }

    @Test
    public void testUzcServiceGenerateMultiOut() {
        Map<UzcAddress, UzcValue> recipients = new HashMap<>();
        recipients.put(uzcCrypto.getUzcAddressFromPassphrase("example1"), UzcValue.fromUzc(1));
        recipients.put(uzcCrypto.getUzcAddressFromPassphrase("example2"), UzcValue.fromUzc(2));
        byte[] multiOutResponse = RxTestUtils.testSingle(uzcNodeService.generateMultiOutTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromPlanck(753000), 1440, recipients));
    }

    @Test
    public void testUzcServiceGenerateMultiOutSame() {
        Set<UzcAddress> recipients = new HashSet<>();
        recipients.add(uzcCrypto.getUzcAddressFromPassphrase("example1"));
        recipients.add(uzcCrypto.getUzcAddressFromPassphrase("example2"));
        byte[] multiOutSameResponse = RxTestUtils.testSingle(uzcNodeService.generateMultiOutSameTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromUzc(1), UzcValue.fromPlanck(753000), 1440, recipients));
    }

    @Test
    public void testUzcServiceGenerateCreateATTransaction() {
        byte[] lotteryAtCode = Hex.decode("1e000000003901090000006400000000000000351400000000000201000000000000000104000000803a0900000000000601000000040000003615000200000000000000260200000036160003000000020000001f030000000100000072361b0008000000020000002308000000090000000f1af3000000361c0004000000020000001e0400000035361700040000000200000026040000007f2004000000050000001e02050000000400000036180006000000020000000200000000030000001a39000000352000070000001b07000000181b0500000012332100060000001a310100000200000000030000001a1a0000003618000a0000000200000020080000000900000023070800000009000000341f00080000000a0000001a78000000341f00080000000a0000001ab800000002000000000400000003050000001a1a000000");
        byte[] lotteryAtCreationBytes = UzcCrypto.getInstance().getATCreationBytes((short) 1, lotteryAtCode, new byte[0], 1, 1, 1, UzcValue.fromUzc(2));
        assertEquals("01000000020001000100010000c2eb0b0000000044011e000000003901090000006400000000000000351400000000000201000000000000000104000000803a0900000000000601000000040000003615000200000000000000260200000036160003000000020000001f030000000100000072361b0008000000020000002308000000090000000f1af3000000361c0004000000020000001e0400000035361700040000000200000026040000007f2004000000050000001e02050000000400000036180006000000020000000200000000030000001a39000000352000070000001b07000000181b0500000012332100060000001a310100000200000000030000001a1a0000003618000a0000000200000020080000000900000023070800000009000000341f00080000000a0000001a78000000341f00080000000a0000001ab800000002000000000400000003050000001a1a00000000", Hex.toHexString(lotteryAtCreationBytes));
        byte[] createATResponse = RxTestUtils.testSingle(uzcNodeService.generateCreateATTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY, UzcValue.fromUzc(5), 1440, "TestAT", "An AT For Testing", lotteryAtCreationBytes));
    }
}
