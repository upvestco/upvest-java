package co.upvest.endpoints;

import co.upvest.*;
import co.upvest.models.*;

import java.io.IOException;
import com.squareup.moshi.*;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;
import org.jetbrains.annotations.Nullable;

public class AssetsEndpoint implements Asset.Endpoint<Asset> {

    private APIClient apiClient;

    final Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<Object> objectAdapter = moshi.adapter(Object.class);
    final JsonAdapter<Asset> assetAdapter = moshi.adapter(Asset.class);
    final JsonAdapter<Cursor<Asset>> assetsCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, Asset.class));

    public AssetsEndpoint(@NotNull APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public @NotNull Cursor<@NotNull Asset> list() throws IOException {
        return list(null);
    }
    
    /**
     * @param pageSize number of assets to load per page (maximum 100)
     */
    public @NotNull Cursor<@NotNull Asset> list(int pageSize) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("assets")
            .addPathSegment("")
            .addQueryParameter("page_size", String.valueOf(pageSize))
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Asset> assets = assetsCursorAdapter.fromJson(response.body().source());
        assets.setEndpoint(this);
        return assets;
    }

    /**
     * Load a page
     * @param cursor url of the (next/previous) page to load
     */
    public @NotNull Cursor<@NotNull Asset> list(@Nullable String cursor) throws IOException {
        HttpUrl url;
        
        if (cursor == null) {
            url = apiClient.getBaseUrl()
                .addPathSegment("assets")
                .addPathSegment("")
                .build();
        } else {
            url = HttpUrl.parse(cursor);
        }
        
        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Asset> assets = assetsCursorAdapter.fromJson(response.body().source());
        assets.setEndpoint(this);
        return assets;
    }

    public @NotNull Asset get(@NotNull String id) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("assets")
                .addPathSegment(id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Asset asset = assetAdapter.fromJson(response.body().source());
        return asset;
    }

    APIClient getApiClient() {
        return this.apiClient;
    }
}