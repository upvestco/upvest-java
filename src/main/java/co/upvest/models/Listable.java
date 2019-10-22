package co.upvest.models;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

public interface Listable {

    interface Endpoint<@NotNull Type extends Listable> {

        public Cursor<@NotNull Type> list() throws IOException;
        public Cursor<@NotNull Type> list(@NotNull int pageSize) throws IOException;
        public Cursor<@NotNull Type> list(@NotNull String cursor) throws IOException;

    }

}