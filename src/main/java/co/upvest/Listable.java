package co.upvest;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

interface Listable {

    interface Endpoint<@NotNull Type extends Listable> {

        public Cursor<@NotNull Type> list() throws IOException;
        public Cursor<@NotNull Type> list(@NotNull int pageSize) throws IOException;
        public Cursor<@NotNull Type> list(@NotNull String cursor) throws IOException;

    }

}