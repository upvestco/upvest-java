package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDBalance {

    private @NotNull String id;
    private @NotNull String address;
    private @NotNull String contract;
    private @NotNull String transactionHash;
    private @NotNull String transactionIndex;
    private @NotNull String blockHash;
    private @NotNull int blockNumber;
    private @NotNull String timestamp;
    private @NotNull boolean isMainChain;

    public HDBalance(@NotNull String id, @NotNull String address, @Nullable String contract, @NotNull String transactionHash,
                     @NotNull String transactionIndex, @NotNull String blockHash, @NotNull int blockNumber, @NotNull String timestamp,
                     @NotNull boolean isMainChain) {
        this.id = id;
        this.address = address;
        this.contract = contract;
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.timestamp = timestamp;
        this.isMainChain = isMainChain;
    }

    public @NotNull String getID() {
        return id;
    }

    public @NotNull String getAddress() {
        return address;
    }

    public @Nullable String getContract() { return contract; }

    public @NotNull String getTransactionHash() {
        return transactionHash;
    }

    public @NotNull String getTransactionIndex() {
        return transactionIndex;
    }

    public @NotNull String getBlockHash() {
        return blockHash;
    }

    public @NotNull int getBlockNumber() {
        return blockNumber;
    }

    public @NotNull String getTimestamp() {
        return timestamp;
    }

    public @NotNull boolean getIsMainChain() {
        return isMainChain;
    }
}
