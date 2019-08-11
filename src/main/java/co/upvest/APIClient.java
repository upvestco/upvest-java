package co.upvest;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

abstract class APIClient {

    public static final String PLAYGROUND_HOST = "api.playground.upvest.co";
    public static final String PRODUCTION_HOST = "api.upvest.co";

    public static final String API_VERSION = "1.0";

    // some calls take longer time than the default timeout would accept
    int CONNECT_TIMEOUT = 100;
    int WRITE_TIMEOUT = 100;
    int READ_TIMEOUT = 300;

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // instance fields

    protected String host;

    protected OkHttpClient client;

    protected final HttpUrl.Builder getBaseUrl() {
        return new HttpUrl.Builder()
                    .scheme("https")
                    .host(host)
                    .addPathSegment(API_VERSION);
    }
}