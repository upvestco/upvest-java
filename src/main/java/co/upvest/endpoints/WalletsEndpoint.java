package co.upvest.endpoints;

import co.upvest.endpoints.WalletsEndpoint;

import co.upvest.*;
import co.upvest.models.*;

import java.io.IOException;
import okhttp3.*;
import com.squareup.moshi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WalletsEndpoint implements Wallet.Endpoint<Wallet> {

    private APIClient apiClient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    final Moshi moshi = new Moshi.Builder()
        .add(new WalletAdapter(this))
        .build();
    final JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    final JsonAdapter<Object> objectAdapter = moshi.adapter(Object.class);
    final JsonAdapter<OAuth> oAuthAdapter = moshi.adapter(OAuth.class);
    final JsonAdapter<Wallet> walletAdapter;
    final JsonAdapter<Cursor<Wallet>> walletCursorAdapter;
    final JsonAdapter<Signature> signatureAdapter;

    public WalletsEndpoint(@NotNull APIClient apiClient) {
        this.apiClient = apiClient;

        walletAdapter = moshi.adapter(Wallet.class);
        walletCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, Wallet.class));
        signatureAdapter = moshi.adapter(Signature.class);
    }

    public @NotNull Wallet create(@NotNull String password, @NotNull String assetId, @Nullable String type, @Nullable Integer index, @Nullable Integer n, @Nullable Integer m) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("kms")
            .addPathSegment("wallets")
            .addPathSegment("")
            .build();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("password", password);
        params.put("asset_id", assetId);
        if (type != null)
            params.put("type", type);
        if (index != null)
            params.put("index", index);
        if (n != null)
            params.put("n", n);
        if (m != null)
            params.put("m", m);

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
            .build();

        Response response = apiClient.client.newCall(request).execute();
        Wallet wallet = walletAdapter.fromJson(response.body().source());
        
        return wallet;
    }

    public @NotNull Cursor<@NotNull Wallet> list() throws IOException {
        return list(null);
    }

    /**
     * @param pageSize number of wallets to load per page (maximum 100)
     */
    public @NotNull Cursor<@NotNull Wallet> list(@NotNull int pageSize) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("kms")
            .addPathSegment("wallets")
            .addPathSegment("")
            .addQueryParameter("page_size", String.valueOf(pageSize))
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.client.newCall(request).execute();
        Cursor<Wallet> wallets = walletCursorAdapter.fromJson(response.body().source());
        wallets.setEndpoint(this);

        return wallets;
    }

    /**
     * Load a page
     * @param cursor url of the (next/previous) page to load
     */
    public @NotNull Cursor<@NotNull Wallet> list(@Nullable String cursor) throws IOException {
        HttpUrl url;
        
        if (cursor == null) {
            url = apiClient.getBaseUrl()
                .addPathSegment("kms")
                .addPathSegment("wallets")
                .addPathSegment("")
                .build();
        } else {
            url = HttpUrl.parse(cursor).newBuilder().scheme("https").build();
        }

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.client.newCall(request).execute();
        Cursor<Wallet> wallets = walletCursorAdapter.fromJson(response.body().source());
        wallets.setEndpoint(this);

        return wallets;
    }

    public @NotNull Wallet get(@NotNull String id) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("kms")
            .addPathSegment("wallets")
            .addPathSegment(id)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.client.newCall(request).execute();
        Wallet wallet = walletAdapter.fromJson(response.body().source());
        
        return wallet;
    }

    public @NotNull Signature sign(@NotNull Wallet wallet, @NotNull String password, @NotNull String toSign, @NotNull String inputFormat, @NotNull String outputFormat) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("kms")
            .addPathSegment("wallets")
            .addPathSegment(wallet.getId())
            .addPathSegment("sign")
            .build();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("password", password);
        params.put("to_sign", toSign);
        params.put("input_format", inputFormat);
        params.put("output_format", outputFormat);

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
            .build();

        Response response = apiClient.client.newCall(request).execute();
        Signature signature = signatureAdapter.fromJson(response.body().source());
        
        return signature;
    }

    public APIClient getAPIClient() {
        return apiClient;
    }
}