package co.upvest;

import java.io.IOException;

import okhttp3.*;

class APIErrorInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {        
        Response response = chain.proceed(chain.request());

        if (response.code() < 300){
            return response;
        } else if (response.code() >= 400 && response.code() < 404){
            throw new AuthError(response.code(), response.body().string());
        } else {
            throw new APIError(response.code(), response.body().string());
        }
    }

    class APIError extends RuntimeException {

        private int code;

        APIError(int code, String msg) {
            super("[HTTP " + code + "] " +msg);
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

    }

    class AuthError extends APIError {

        AuthError(int code, String msg) {
            super(code, msg);
        }

    }

}