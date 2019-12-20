package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HDTransactionList {
    private @NotNull
    List<HDTransaction> result;
    private @Json(name = "next_cursor") @NotNull String nextCursor;

    public HDTransactionList(@NotNull List<HDTransaction> result, @NotNull String nextCursor) {
        this.result = result;
        this.nextCursor = nextCursor;
    }

    public @NotNull List<HDTransaction> getResults() {
        return result;
    }

    public @NotNull String getNextCursor() {
        return nextCursor;
    }
}
