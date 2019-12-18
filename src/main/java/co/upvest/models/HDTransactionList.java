package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDTransactionList {
    private @NotNull HDTransaction[] result;
    private @Json(name = "next_cursor") @NotNull String nextCursor;

    public HDTransactionList(@NotNull HDTransaction[] result, @NotNull String nextCursor) {
        this.result = result;
        this.nextCursor = nextCursor;
    }

    public @NotNull HDTransaction[] getResults() {
        return result;
    }

    public @NotNull String getNextCursor() {
        return nextCursor;
    }
}
