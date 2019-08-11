package co.upvest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.squareup.moshi.*;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;

class ClienteleAPI extends APIClient{

    final private Moshi moshi = new Moshi.Builder().build();
    final private JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    final private JsonAdapter<Object> objectAdapter = moshi.adapter(Object.class);
    final private JsonAdapter<OAuth> oAuthAdapter = moshi.adapter(OAuth.class);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private AssetsEndpoint assetsEndpoint;
    private WalletsEndpoint walletsEndpoint;

    public ClienteleAPI(@NotNull String oAuthClientId, @NotNull String oAuthClientSecret, @NotNull String username, @NotNull String password) throws IOException {
        this(PLAYGROUND_HOST, oAuthClientId, oAuthClientSecret, username, password);
    }

    public ClienteleAPI(@NotNull String host, @NotNull String oAuthClientId, @NotNull String oAuthClientSecret, @NotNull String username, @NotNull String password) throws IOException {
        this.host = host;

        OAuth auth = obtainOAuth(oAuthClientId, oAuthClientSecret, username, password);

        this.client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(new APIErrorInterceptor())
            .addInterceptor(auth)
            .build();
    }

    public @NotNull AssetsEndpoint assets() {
        if (assetsEndpoint == null)
            assetsEndpoint = new AssetsEndpoint(this);
        return assetsEndpoint;
    }

    public @NotNull WalletsEndpoint wallets() {
        if (walletsEndpoint == null)
            walletsEndpoint = new WalletsEndpoint(this);
        return walletsEndpoint;
    }

    public @NotNull TransactionsEndpoint transactions(Wallet wallet) {
        return new TransactionsEndpoint(this, wallet);
    }

    public @NotNull Echo echo(@NotNull String what) throws IOException {
        HttpUrl url = getBaseUrl()
                .addPathSegment("clientele")
                .addPathSegment("echo-oauth2")
                .build();
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("echo", what);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        Echo echo = echoJsonAdapter.fromJson(body.source());
        
        return echo;
    }

    public @NotNull Echo echoGet(@NotNull String what) throws IOException {
        HttpUrl url = getBaseUrl()
                .addPathSegment("clientele")
                .addPathSegment("echo-oauth2")
                .addQueryParameter("echo", what)
                .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        Echo echo = echoJsonAdapter.fromJson(body.source());
        
        return echo;
    }

    private OAuth obtainOAuth(String oAuthClientId, String oAuthClientSecret, String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new APIErrorInterceptor())
            .build();

        HttpUrl url = getBaseUrl()
                .addPathSegment("clientele")
                .addPathSegment("oauth2")
                .addPathSegment("token")
                .build();

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("scope", "read write echo wallet transaction")
                .add("client_id", oAuthClientId)
                .add("client_secret", oAuthClientSecret)
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        OAuth auth = oAuthAdapter.fromJson(response.body().source());
        return auth;
    }

}
