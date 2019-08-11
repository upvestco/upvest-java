package co.upvest;

import org.jetbrains.annotations.NotNull;

class Echo {

    private @NotNull String echo;

    Echo(@NotNull String echo) {
        this.echo = echo;
    }

    @NotNull String getEcho() {
        return this.echo;
    }
}