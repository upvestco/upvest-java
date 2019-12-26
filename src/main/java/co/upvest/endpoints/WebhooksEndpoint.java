package co.upvest.endpoints;

import co.upvest.*;
import co.upvest.models.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.squareup.moshi.*;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;
import org.jetbrains.annotations.Nullable;

public class WebhooksEndpoint implements Webhook.Endpoint<Webhook> {

    private TenancyAPI apiClient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    final Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<Webhook> webhookAdapter = moshi.adapter(Webhook.class);
    final JsonAdapter<Cursor<Webhook>> webhooksCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, Webhook.class));

    public WebhooksEndpoint(@NotNull TenancyAPI tenancy) {
        this.apiClient = tenancy;
    }

    public @NotNull
    Cursor<@NotNull Webhook> list() throws IOException {
        return list(null);
    }

    /**
     * @param pageSize number of Webhooks to load per page (maximum 100)
     */
    public @NotNull
    Cursor<@NotNull Webhook> list(int pageSize) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("webhooks")
                .addPathSegment("")
                .addQueryParameter("page_size", String.valueOf(pageSize))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Webhook> webhooks = webhooksCursorAdapter.fromJson(response.body().source());
        webhooks.setEndpoint(this);
        return webhooks;
    }

    /**
     * Load a page
     *
     * @param cursor url of the (next/previous) page to load
     */
    public @NotNull
    Cursor<@NotNull Webhook> list(@Nullable String cursor) throws IOException {
        HttpUrl url;

        if (cursor == null) {
            url = apiClient.getBaseUrl()
                    .addPathSegment("tenancy")
                    .addPathSegment("webhooks")
                    .addPathSegment("")
                    .build();
        } else {
            url = HttpUrl.parse(cursor);
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Webhook> webhooks = webhooksCursorAdapter.fromJson(response.body().source());
        webhooks.setEndpoint(this);
        return webhooks;
    }

    public @NotNull Webhook create(@NotNull WebhookParams params) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("webhooks")
                .addPathSegment("")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, apiClient.objectAdapter.toJson(params.toMap())))
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Webhook webhook = webhookAdapter.fromJson(response.body().source());

        return webhook;
    }

    public boolean verify(String verifyUrl) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("webhooks-verify")
                .addPathSegment("")
                .build();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("verify_url", verifyUrl);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, apiClient.objectAdapter.toJson(params)))
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        return response.code() == 201;
    }

    public @NotNull Webhook get(@NotNull String id) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("webhooks")
                .addPathSegment(id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Webhook webhook = webhookAdapter.fromJson(response.body().source());
        return webhook;
    }

    public @NotNull boolean delete(@NotNull String id) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("webhooks")
                .addPathSegment(id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        return response.code() == 204;
    }

}