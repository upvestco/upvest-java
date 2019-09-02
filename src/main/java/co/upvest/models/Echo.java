package co.upvest.models;

import org.jetbrains.annotations.NotNull;

public class Echo {

    private @NotNull String echo;

    Echo(@NotNull String echo) {
        this.echo = echo;
    }

    public @NotNull String getEcho() {
        return this.echo;
    }
}