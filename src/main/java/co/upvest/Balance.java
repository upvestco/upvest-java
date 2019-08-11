package co.upvest;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;

public class Balance {
    
    private @NotNull String name;
    private @NotNull String symbol;
    private @NotNull int exponent;
    private @NotNull String amount;
    @Json(name = "asset_id") private @NotNull String assetId;

    public Balance(@NotNull String name, @NotNull String symbol, @NotNull int exponent, @NotNull String amount, @Json(name = "asset_id") @NotNull String assetId) {
        this.name = name;
        this.symbol = symbol;
        this.exponent = exponent;
        this.amount = amount;
        this.assetId = assetId;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getSymbol() {
        return symbol;
    }

    public @NotNull int getExponent() {
        return exponent;
    }

    public @NotNull String getAmount() {
        return amount;
    }
    
    public @NotNull String getAssetId() {
        return assetId;
    }
}
