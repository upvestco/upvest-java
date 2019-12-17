package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;

public class HDStatus {
    private @NotNull String lowest;
    private @NotNull String highest;
    private @NotNull String latest;

    public HDStatus(@NotNull String lowest, @NotNull String highest, @NotNull String latest) {
        this.lowest = lowest;
        this.highest = highest;
        this.latest = latest;
    }

    public @NotNull String getLowest() {
        return lowest;
    }

    public @NotNull String getHighest() {
        return highest;
    }

    public @NotNull String getLatest() {
        return latest;
    }
}
