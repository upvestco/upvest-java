package co.upvest;

import kotlin.NotImplementedError;
import okhttp3.OkHttpClient;

public class MockAPIClient extends APIClient{

    public MockAPIClient() {
        host = "api.upvest.null";
    }

    public OkHttpClient getClient() {
        throw new NotImplementedError();
    }
}
