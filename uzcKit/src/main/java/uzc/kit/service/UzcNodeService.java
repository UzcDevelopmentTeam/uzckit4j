package uzc.kit.service;

import uzc.kit.entity.*;
import uzc.kit.entity.response.*;
import uzc.kit.service.impl.CompositeUzcNodeService;
import uzc.kit.service.impl.DefaultSchedulerAssigner;
import uzc.kit.service.impl.GrpcUzcNodeService;
import uzc.kit.service.impl.HttpUzcNodeService;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public interface UzcNodeService {
    /**
     * Get a block via a block ID
     * @param block The block ID of the requested block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(UzcID block);

    /**
     * Get the block at a specific height
     * @param height The height of the block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(int height);

    /**
     * Get the block at the specified timestamp
     * @param timestamp The timestamp of the block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(UzcTimestamp timestamp);

    /**
     * Get the block ID at a specified height
     * @param height The height of the block
     * @return The Block ID response, wrapped in a single
     * @deprecated Just use getBlock and then getId instead
     */
    @Deprecated
    Single<UzcID> getBlockId(int height);

    /**
     * Gets all the blocks between the first index and last index.
     * @param firstIndex The index from the most recent blocks (0 would be the most recent block)
     * @param lastIndex The end index from the most recent blocks
     * @return The blocks, wrapped in a single
     */
    Single<Block[]> getBlocks(int firstIndex, int lastIndex); // TODO includeTransactions?

    /**
     * Get the Constants in use by the node
     * @return The constants, wrapped in a single
     */
    Single<Constants> getConstants();

    /**
     * Get the account details of the specified account
     * @param accountId The address of the account
     * @return The block details, wrapped in a single
     */
    Single<Account> getAccount(UzcAddress accountId);

    /**
     * Get the ATs created by the account
     * @param accountId The address of the account
     * @return A list of the ATs, wrapped in a single
     */
    Single<AT[]> getAccountATs(UzcAddress accountId);

    /**
     * Get the IDs of the blocks forged by an account
     * @param accountId The address of the account
     * @return The block IDs, wrapped in a single
     * @deprecated Just use getAccountBlocks and then getId instead
     */
    @Deprecated
    Single<UzcID[]> getAccountBlockIDs(UzcAddress accountId); // TODO timestamp, firstIndex, lastIndex

    /**
     * Get the blocks forged by an account
     * @param accountId The address of the account
     * @return The blocks, wrapped in a single
     */
    Single<Block[]> getAccountBlocks(UzcAddress accountId); // TODO timestamp, firstIndex, lastIndex, includeTransactions

    /**
     * Get the transaction IDs of an account
     * @param accountId The address of the account
     * @return The account's transaction IDs, wrapped in a single
     */
    Single<UzcID[]> getAccountTransactionIDs(UzcAddress accountId); // TODO filtering

    /**
     * Get the transactions of an account
     * @param accountId The address of the account
     * @return The account's transactions, wrapped in a single
     */
    Single<Transaction[]> getAccountTransactions(UzcAddress accountId); // TODO filtering

    /**
     * Get the list of accounts which have their reward recipient set to the specified account
     * @param accountId The address of the account
     * @return The list of account IDs with reward recipients set to the account, wrapped in a single
     */
    Single<UzcAddress[]> getAccountsWithRewardRecipient(UzcAddress accountId); // TODO finish

    /**
     * Get the details of an AT
     * @param at The address of the AT
     * @return The details of the AT, wrapped in a single
     */
    Single<AT> getAt(UzcAddress at);

    /**
     * Get the list of addresses of all ATs
     * @return The list of AT addresses, wrapped in a single
     */
    Single<UzcAddress[]> getAtIds();

    /**
     * Get the details of a transaction
     * @param transactionId The ID of the transaction
     * @return The transaction details, wrapped in a single
     */
    Single<Transaction> getTransaction(UzcID transactionId);

    /**
     * Get the details of a transaction
     * @param fullHash The full hash of the transaction
     * @return The transaction details, wrapped in a single
     */
    Single<Transaction> getTransaction(byte[] fullHash);

    /**
     * Get the transaction bytes
     * @param transactionId The ID of the transaction
     * @return The transaction bytes, wrapped in a single
     */
    Single<byte[]> getTransactionBytes(UzcID transactionId);

    /**
     * Generate a simple transaction (only sending money)
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransaction(UzcAddress recipient, byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline);

    /**
     * Generate a transaction with a plaintext message
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(UzcAddress recipient, byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline, String message);

    /**
     * Generate a transaction with a plaintext message
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(UzcAddress recipient, byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline, byte[] message);

    /**
     * Generate a transaction with an encrypted message (can be read by sender and recipient)
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessage(UzcAddress recipient, byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline, UzcEncryptedMessage message);

    /**
     * Generate a transaction with an encrypted-to-self message (can be read by only sender)
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction (Use sender public key and sender private key to encrypt)
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessageToSelf(UzcAddress recipient, byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline, UzcEncryptedMessage message);

    /**
     * Get the currently suggested transaction fees, which are calculated based on current network congestion -
     * @return Suggested transaction fees - Priority, standard and cheap in descending speed and cost, wrapped in a single
     */
    Single<FeeSuggestion> suggestFee();

    /**
     * Get the current mining info
     * @return An observable that returns the current mining info when it changes.
     */
    Observable<MiningInfo> getMiningInfo();

    /**
     * Broadcast a transaction on the network
     * @param transactionBytes The signed transaction bytes
     * @return The number of peers this transaction was broadcast to, wrapped in a single
     */
    Single<TransactionBroadcast> broadcastTransaction(byte[] transactionBytes);

    /**
     * Get the reward recipient of the account
     * @param account The account
     * @return The reward recipient, wrapped in a single
     */
    Single<UzcAddress> getRewardRecipient(UzcAddress account);

    /**
     * Submit a nonce for mining
     * @param passphrase The passphrase of the miner (if solo mining) or null if pool mining
     * @param nonce The nonce that results in the deadline you want to submit
     * @param accountId The account ID of the miner
     * @return The calculated deadline, wrapped in a single
     */
    Single<Long> submitNonce(String passphrase, String nonce, UzcID accountId);

    /**
     * Generate a multi-out transaction
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param recipients A map of recipients and how much they get. Length must be 2-64 inclusive
     * @return The unsigned transaction bytes, wrapped in a single
     * @throws IllegalArgumentException If the number of recipients is not in the range of 2-64 inclusive
     */
    Single<byte[]> generateMultiOutTransaction(byte[] senderPublicKey, UzcValue fee, int deadline, Map<UzcAddress, UzcValue> recipients) throws IllegalArgumentException;

    /**
     * Generate a multi-out transaction
     * @param senderPublicKey The public key of the sender
     * @param amount The amount each recipient gets
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param recipients A list of recipients. Each will get the amount specified. Length must be 2-128 inclusive
     * @return The unsigned transaction bytes, wrapped in a single
     * @throws IllegalArgumentException If the number of recipients is not in the range of 2-128 inclusive
     */
    Single<byte[]> generateMultiOutSameTransaction(byte[] senderPublicKey, UzcValue amount, UzcValue fee, int deadline, Set<UzcAddress> recipients) throws IllegalArgumentException;

    /**
     * Generate the transaction for creating an AT
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param name The name of the AT
     * @param description The description of the AT
     * @param creationBytes The creation bytes of the AT (if pre-calculated and not using the following fields)
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateCreateATTransaction(byte[] senderPublicKey, UzcValue fee, int deadline, String name, String description, byte[] creationBytes);

    static UzcNodeService getInstance(String nodeAddress) {
        return getInstance(nodeAddress, null);
    }

    static UzcNodeService getInstance(String nodeAddress, String httpUserAgent) {
        if (nodeAddress.startsWith("grpc://")) {
            return new GrpcUzcNodeService(nodeAddress, new DefaultSchedulerAssigner());
        } else {
            return new HttpUzcNodeService(nodeAddress, httpUserAgent, new DefaultSchedulerAssigner());
        }
    }

    static UzcNodeService getCompositeInstance(String... nodeAddresses) {
        return getCompositeInstanceWithUserAgent(null, nodeAddresses);
    }

    static UzcNodeService getCompositeInstanceWithUserAgent(String httpUserAgent, String... nodeAddresses) {
        return new CompositeUzcNodeService(Arrays.stream(nodeAddresses)
                .map(nodeAddress -> getInstance(nodeAddress, httpUserAgent))
                .toArray(UzcNodeService[]::new));
    }
}
