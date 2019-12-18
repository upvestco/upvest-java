package co.upvest.models;

import java.util.Map;
import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDTransaction {

    private @NotNull String blockHash;
    private @NotNull int blockNumber;
    private @NotNull String from;
    private @NotNull String to;
    private @NotNull int gasLimit;
    private @NotNull String hash;
    private @NotNull String nonce;
    private @NotNull int transactionIndex;
    private @NotNull String value;
    private @NotNull int gasPrice;
    private @NotNull String input;
    private @NotNull int confirmations;
    private @Nullable Object error;

    public HDTransaction(@NotNull String blockHash, @NotNull int blockNumber, @NotNull String from, @NotNull String to, Object error,
                         @NotNull int gasLimit, @NotNull String hash, @NotNull String nonce, @NotNull int transactionIndex,
                         @NotNull String value, @NotNull int gasPrice, @NotNull String input, @NotNull int confirmations) {
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.gasLimit = gasLimit;
        this.hash = hash;
        this.nonce = nonce;
        this.transactionIndex = transactionIndex;
        this.value = value;
        this.gasPrice = gasPrice;
        this.input = input;
        this.confirmations = confirmations;
        this.error = error;
    }

    public @NotNull String getBlockHash() {
        return blockHash;
    }

    public @NotNull int getBlockNumber() {
        return blockNumber;
    }

    public @NotNull String getFrom() {
        return from;
    }

    public @NotNull String getTo() {
        return to;
    }

    public @NotNull int getGasLimit() {
        return gasLimit;
    }

    public @NotNull String getHash() {
        return hash;
    }

    public @NotNull String getNonce() {
        return nonce;
    }

    public @NotNull int getTransactionIndex() {
        return transactionIndex;
    }

    public @NotNull String getValue() {
        return value;
    }

    public @NotNull int getGasPrice() {
        return gasPrice;
    }

    public @NotNull String getInput() {
        return input;
    }

    public @NotNull int getConfirmations() {
        return confirmations;
    }

    public @Nullable Object getError() {
        return error;
    }
}
