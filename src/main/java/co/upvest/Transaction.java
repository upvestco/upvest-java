package co.upvest;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Transaction implements Listable {

    private @NotNull String id;
    private @NotNull String txhash;
    private @Json(name = "wallet_id") @NotNull String walletId;
    private @Json(name = "asset_name") @NotNull String assetName;
    private @Json(name = "asset_id") @NotNull String assetId;
    private @NotNull String sender;
    private @Nullable String recipient;
    private @Nullable String quantity;
    private @Nullable String fee;
    private @Nullable String status;

    public Transaction(@NotNull String id, @NotNull String txhash, @NotNull String walletId, @NotNull String assetName, @NotNull String assetId, @NotNull String sender, @Nullable String recipient,
    @Nullable String quantity, @Nullable String fee, @Nullable String status) {
        this.id = id;
        this.txhash = txhash;
        this.walletId = walletId;
        this.assetName = assetName;
        this.assetId = assetId;
        this.sender = sender;
        this.recipient = recipient;
        this.quantity = quantity;
        this.fee = fee;
        this.status = status;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getTxhash() {
        return txhash;
    }

    public @NotNull String getWalletId() {
        return walletId;
    }

    public @NotNull String getAssetName() {
        return assetName;
    }

    public @NotNull String getAssetId() {
        return assetId;
    }

    public @NotNull String getSender() {
        return sender;
    }

    public @Nullable String getRecipient() {
        return recipient;
    }

    public @Nullable String getQuantity() {
        return quantity;
    }

    public @Nullable String getFee() {
        return fee;
    }

    public @Nullable String getStatus() {
        return status;
    }
    
}