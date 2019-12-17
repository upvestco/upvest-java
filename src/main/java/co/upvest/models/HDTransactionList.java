package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDTransactionList {
    private @NotNull HDTransaction[] results;
    private @Json(name = "next_cursor") @NotNull String nextCursor;

    public HDTransactionList(@NotNull HDTransaction[] results, @NotNull String nextCursor) {
        this.results = results;
        this.nextCursor = nextCursor;
    }

    public @NotNull HDTransaction[] getResults() {
        return results;
    }

    public @NotNull String getNextCursor() {
        return nextCursor;
    }
}
