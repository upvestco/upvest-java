package co.upvest.models;

import com.squareup.moshi.Json;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class User implements Listable {
    
    private @NotNull String username;
    private @Nullable String recoverykit;
    private @Json(name = "wallet_ids") @Nullable String[] walletIds;

    User(String username, String recoverykit, @Json(name = "wallet_ids") String[] walletIds) {
        this.username = username;
        this.recoverykit = recoverykit;
        this.walletIds = walletIds;
    }

    public @NotNull String getUsername() {
        return this.username;
    }

    public @Nullable String getRecoverykit() {
        return this.recoverykit;
    }

    public @Nullable String[] getWalletIds() {
        return this.walletIds;
    }

}