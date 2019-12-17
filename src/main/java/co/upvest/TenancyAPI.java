package co.upvest;

import co.upvest.endpoints.*;
import co.upvest.models.*;

import java.io.IOException;
import okhttp3.*;
import com.squareup.moshi.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.*;

public class TenancyAPI extends APIClient {

    public Moshi moshi = new Moshi.Builder().build();
    public JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    public JsonAdapter<Object> objectAdapter = moshi.adapter(Object.class);

    private UsersEndpoint usersEndpoint;
    private AssetsEndpoint assetsEndpoint;
    private WalletsEndpoint walletsEndpoint;
    private HistoricalDataEndpoint historicalEndpoint;

    public @NotNull TenancyAPI(@NotNull String key, @NotNull String secret, @NotNull String passphrase) {
        this(getDefaultHost(), key, secret, passphrase);
    }

    public @NotNull TenancyAPI(@NotNull String host, @NotNull String key, @NotNull String secret, @NotNull String passphrase) {
        this.host = host;
        
        KeyAuth auth = new KeyAuth(key, secret, passphrase);

        this.client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(new APIErrorInterceptor())
            .addInterceptor(auth)
            .build();
    }

    public @NotNull UsersEndpoint users() {
        if (usersEndpoint == null)
            usersEndpoint = new UsersEndpoint(this);
        return usersEndpoint;
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

    public @NotNull HistoricalDataEndpoint historical() {
        if (historicalEndpoint == null)
            historicalEndpoint = new HistoricalDataEndpoint(this);
        return historicalEndpoint;
    }

    public @NotNull Echo echo(@NotNull String what) throws IOException {
        HttpUrl url = getBaseUrl()
            .addPathSegment("tenancy")
            .addPathSegment("echo-signed")
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
            .addPathSegment("tenancy")
            .addPathSegment("echo-signed")
            .addQueryParameter("echo", what)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();
        Echo echo = echoJsonAdapter.fromJson(response.body().source());
        
        return echo;
    }
}
