package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDBlock {

    private @NotNull int number;
    private @NotNull String hash;
    private @NotNull String parentHash;
    private @NotNull String nonce;
    private @NotNull String sha3Uncles;
    private @NotNull String logsBloom;
    private @NotNull String transactionsRoot;
    private @NotNull String stateRoot;
    private @NotNull String receiptsRoot;
    private @NotNull String miner;
    private @NotNull String difficulty;
    private @NotNull String totalDifficulty;
    private @NotNull String extraData;
    private @NotNull String size;
    private @NotNull String gasLimit;
    private @NotNull String gasUsed;
    private @NotNull String[] transactions;
    private @NotNull String timestamp;
    private @NotNull String[] uncles;

    public HDBlock(@NotNull int number, @NotNull String hash, @NotNull String parentHash, @NotNull String nonce, @NotNull String sha3Uncles,
                   @NotNull String logsBloom, @NotNull String transactionsRoot, @NotNull String stateRoot, @NotNull String receiptsRoot,
                   @NotNull String miner, @NotNull String difficulty, @NotNull String totalDifficulty, @NotNull String extraData,
                   @NotNull String size, @NotNull String gasLimit, @NotNull String gasUsed, @NotNull String[] transactions,
                   @NotNull String timestamp, @NotNull String[] uncles) {
        this.number = number;
        this.hash = hash;
        this.parentHash = parentHash;
        this.nonce = nonce;
        this.sha3Uncles = sha3Uncles;
        this.logsBloom = logsBloom;
        this.transactionsRoot = transactionsRoot;
        this.stateRoot = stateRoot;
        this.receiptsRoot = receiptsRoot;
        this.miner = miner;
        this.difficulty = difficulty;
        this.totalDifficulty = totalDifficulty;
        this.extraData = extraData;
        this.size = size;
        this.gasLimit = gasLimit;
        this.gasUsed = gasUsed;
        this.transactions = transactions;
        this.timestamp = timestamp;
        this.uncles = uncles;
    }

    public @NotNull int getNumber() {
        return number;
    }

    public @NotNull String getHash() {
        return hash;
    }

    public @NotNull String getParentHash() {
        return parentHash;
    }

    public @NotNull String getNonce() {
        return nonce;
    }

    public @NotNull String getSha3Uncles() {
        return sha3Uncles;
    }

    public @NotNull String getLogsBloom() {
        return logsBloom;
    }

    public @NotNull String getTransactionsRoot() {
        return transactionsRoot;
    }

    public @NotNull String getStateRoot() {
        return stateRoot;
    }

    public @NotNull String getReceiptsRoot() {
        return receiptsRoot;
    }

    public @NotNull String getMiner() {
        return miner;
    }

    public @NotNull String getDifficulty() {
        return difficulty;
    }

    public @NotNull String getTotalDifficulty() {
        return totalDifficulty;
    }

    public @NotNull String getExtraData() {
        return extraData;
    }

    public @NotNull String getSize() {
        return size;
    }

    public @NotNull String getGasLimit() {
        return gasLimit;
    }

    public @NotNull String getGasUsed() {
        return gasUsed;
    }

    public @NotNull String[] getTransactions() {
        return transactions;
    }

    public @NotNull String getTimestamp() {
        return timestamp;
    }

    public @NotNull String[] getUncles() {
        return uncles;
    }
}
