package co.upvest.endpoints;

import co.upvest.*;
import co.upvest.models.*;

import java.io.IOException;
import okhttp3.*;
import com.squareup.moshi.*;
import java.util.*;
import org.jetbrains.annotations.*;

public class UsersEndpoint implements User.Endpoint<User> {

    private TenancyAPI apiClient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<User> userJsonAdapter = moshi.adapter(User.class);
    final JsonAdapter<Cursor<User>> userCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, User.class));

    public UsersEndpoint(@NotNull TenancyAPI tenancy) {
        this.apiClient = tenancy;
    }

    public @NotNull Cursor<@NotNull User> list() throws IOException {
        return list(null);
    }

    /**
     * @param pageSize number of users to load per page (maximum 100)
     */
    public @NotNull Cursor<@NotNull User> list(int pageSize) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("users")
            .addPathSegment("")
            .addQueryParameter("page_size", String.valueOf(pageSize))
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<User> users = userCursorAdapter.fromJson(response.body().source());
        users.setEndpoint(this);
        return users;
    }

    /**
     * Load a page
     * @param cursor url of the (next/previous) page to load
     */
    public @NotNull Cursor<@NotNull User> list(@Nullable String cursor) throws IOException {
        HttpUrl url;
        
        if (cursor == null){
            url = apiClient.getBaseUrl()
                .addPathSegment("tenancy")
                .addPathSegment("users")
                .addPathSegment("")
                .build();
        } else {
            url = HttpUrl.parse(cursor);
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<User> users = userCursorAdapter.fromJson(response.body().source());
        users.setEndpoint(this);
        return users;
    }

    public @NotNull User create(@NotNull String username, @NotNull String password, @NotNull String[] assetIds) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("users")
            .addPathSegment("")
            .build();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        params.put("asset_ids", assetIds);

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(JSON, apiClient.objectAdapter.toJson(params)))
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        User user = userJsonAdapter.fromJson(response.body().source());
        
        return user;
    }

    public @NotNull User get(@NotNull String username) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("users")
            .addPathSegment(username)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        User user = userJsonAdapter.fromJson(response.body().source());
        
        return user;
    }

    public @NotNull User update(@NotNull String username, @NotNull String oldPassword, @NotNull String newPassword) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("users")
            .addPathSegment(username)
            .build();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("old_password", oldPassword);
        params.put("new_password", newPassword);

        Request request = new Request.Builder()
            .url(url)
            .patch(RequestBody.create(JSON, apiClient.objectAdapter.toJson(params)))
            .build();

        Response response = apiClient.getClient().newCall(request).execute();
        User user = userJsonAdapter.fromJson(response.body().source());
        
        return user;
    }

    public @NotNull boolean delete(@NotNull String username) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("users")
            .addPathSegment(username)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .delete()
            .build();
        
        Response response = apiClient.getClient().newCall(request).execute();
        return response.code() == 204;
    }

    APIClient getApiClient() {
        return this.apiClient;
    }

}