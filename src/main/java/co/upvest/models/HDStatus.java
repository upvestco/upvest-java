package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;

public class HDStatus {
    private @NotNull int lowest;
    private @NotNull int highest;
    private @NotNull int latest;

    public HDStatus(@NotNull int lowest, @NotNull int highest, @NotNull int latest) {
        this.lowest = lowest;
        this.highest = highest;
        this.latest = latest;
    }

    public @NotNull int getLowest() {
        return lowest;
    }

    public @NotNull int getHighest() {
        return highest;
    }

    public @NotNull int getLatest() {
        return latest;
    }
}
