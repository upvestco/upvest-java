package co.upvest.models;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cursor<Type extends Listable> implements Iterable<Type> {

    @Json(name = "results") private @NotNull Type[] arr;
    private String previous;
    private String next;
    private transient Listable.Endpoint<Type> endpoint;

    Cursor(@NotNull Type[] results, @Nullable String previous, @Nullable String next, @NotNull Listable.Endpoint<Type> endpoint) {
        this.arr = results;
        this.previous = previous;
        this.next = next;
        this.endpoint = endpoint;
    }

    public @NotNull Type[] toArray() {
        return arr;
    }

    public @NotNull boolean hasNextPage() {
        return next != null;
    }

    public @NotNull boolean hasPreviousPage() {
        return previous != null;
    }

    public @NotNull Cursor<@NotNull Type> nextPage() throws IOException {
        return this.endpoint.list(this.next);
    }

    public @NotNull Cursor<@NotNull Type> previousPage() throws IOException {
        return this.endpoint.list(this.previous);
    }

    public void setEndpoint(@NotNull Listable.Endpoint<Type> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public @NotNull Iterator<@NotNull Type> iterator() {
        return new Iterator<@NotNull Type>() {
            private int index = 0;

            @Override
            public @NotNull boolean hasNext() {
                return index < arr.length;
            }

            @Override
            public @NotNull Type next() {
                if(index < arr.length)
                    return arr[index++];
                else
                    throw new NoSuchElementException();
            }

            public @NotNull Type previous() {
                if(index >= 0)
                    return arr[index--];
                else
                    throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}