package co.upvest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.*;

class KeyAuth extends Auth {

    private String key;
    private String secret;
    private String passphrase;

    private static final String HMAC_SHA512 = "HmacSHA512";

    public KeyAuth(String key, String secret, String passphrase) {
        this.key = key;
        this.secret = secret;
        this.passphrase = passphrase;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {        
        Request originalRequest = chain.request();

        String timestamp = String.valueOf(java.time.Instant.now().getEpochSecond());

        String path = originalRequest.url().encodedPath() +
            (originalRequest.url().encodedQuery() == null ? "" : "?" + originalRequest.url().encodedQuery());

        String method = originalRequest.method();

        String body = bodyToString(originalRequest.body());

        String preHashedMessage = timestamp + method + path + body;
        String signature = generateSignature(preHashedMessage);

        Request request = originalRequest.newBuilder()
            .header("X-UP-API-Key", key)
            .header("X-UP-API-Passphrase", passphrase)
            .header("X-UP-API-Timestamp", timestamp)
            .header("X-UP-API-Signature", signature)
            .header("X-UP-API-Signed-Path", path)
            .method(originalRequest.method(), originalRequest.body())
            .build();
        
        return chain.proceed(request);
    }

    private String generateSignature(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_SHA512);
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Signature can't be generated");
        }
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private String bodyToString(RequestBody body) throws IOException {
        if (body == null)
            return "";

        okio.Buffer buffer = new okio.Buffer();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }
}