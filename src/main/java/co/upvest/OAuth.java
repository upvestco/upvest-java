package co.upvest;

import java.io.IOException;
import com.squareup.moshi.Json;
import okhttp3.*;

public class OAuth extends Auth {
    
    @Json(name = "access_token") private String accessToken;
    @Json(name = "expires_in") private int expiresIn;
    @Json(name = "token_type") private String tokenType;
    @Json(name = "scope") private String scope;
    @Json(name = "refresh_token") private String refreshToken;

    OAuth(
        @Json(name = "access_token") String accessToken,
        @Json(name = "expires_in") int expiresIn,
        @Json(name = "token_type") String tokenType,
        @Json(name = "scope") String scope,
        @Json(name = "refresh_token") String refreshToken
    ) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
        this.refreshToken = refreshToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request request = originalRequest.newBuilder()
            .header("Authorization", "Bearer " + accessToken)
            .method(originalRequest.method(), originalRequest.body())
            .build();
        
        return chain.proceed(request);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}