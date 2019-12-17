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
    private @NotNull String gas;
    private @NotNull String hash;
    private @NotNull String nonce;
    private @NotNull String transactionIndex;
    private @NotNull String value;
    private @NotNull String gasPrice;
    private @NotNull String input;
    private @NotNull String confirmations;
    private @Nullable Map<String, Object> error;

    public HDTransaction(@NotNull String blockHash, @NotNull int blockNumber, @NotNull String from, @NotNull String to,
                         @NotNull String gas, @NotNull String hash, @NotNull String nonce, @NotNull String transactionIndex,
                         @NotNull String value, @NotNull String gasPrice, @NotNull String input, @NotNull String confirmations) {
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.gas = gas;
        this.hash = hash;
        this.nonce = nonce;
        this.transactionIndex = transactionIndex;
        this.value = value;
        this.gasPrice = gasPrice;
        this.input = input;
        this.confirmations = confirmations;
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

    public @NotNull String getGas() {
        return gas;
    }

    public @NotNull String getHash() {
        return hash;
    }

    public @NotNull String getNonce() {
        return nonce;
    }

    public @NotNull String getTransactionIndex() {
        return transactionIndex;
    }

    public @NotNull String getValue() {
        return value;
    }

    public @NotNull String getGasPrice() {
        return gasPrice;
    }

    public @NotNull String getInput() {
        return input;
    }

    public @NotNull String getConfirmations() {
        return confirmations;
    }

    public @Nullable Map<String, Object> getError() {
        return error;
    }
}
