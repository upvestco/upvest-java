package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;

public class Signature {

    private @Json(name = "big_number_format")  @NotNull String bigNumberFormat;
    private @NotNull String algorithm;
    private @NotNull String curve;
    private @Json(name = "public_key") @NotNull PublicKey publicKey;
    private @NotNull String r;
    private @NotNull String s;
    private @NotNull String recover;

    public Signature(
            @NotNull String bigNumberFormat,
            @NotNull String algorithm,
            @NotNull String curve,
            @NotNull PublicKey publicKey,
            @NotNull String r,
            @NotNull String s,
            @NotNull String recover
    ) {
        this.bigNumberFormat = bigNumberFormat;
        this.algorithm = algorithm;
        this.curve = curve;
        this.publicKey = publicKey;
        this.r = r;
        this.s = s;
        this.recover = recover;
    }

    public @NotNull String getBigNumberFormat() {
        return bigNumberFormat;
    }

    public @NotNull String getAlgorithm() {
        return algorithm;
    }

    public @NotNull String getCurve() {
        return curve;
    }

    public @NotNull PublicKey getPublicKey() {
        return publicKey;
    }

    public @NotNull String getR() {
        return r;
    }

    public @NotNull String getS() {
        return s;
    }

    public @NotNull String getRecover() {
        return recover;
    }

    public static class PublicKey {

        private String x;
        private String y;

        PublicKey(@NotNull String x, @NotNull String y) {
            this.x = x;
            this.y = y;
        }

        @NotNull String getX() {
            return x;
        }

        @NotNull String getY() {
            return y;
        }
    }
}