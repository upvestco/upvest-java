package co.upvest;

import org.jetbrains.annotations.NotNull;

public class Asset implements Listable {
    
    private @NotNull String id;
    private @NotNull String name;
    private @NotNull String symbol;
    private @NotNull int exponent;
    private @NotNull String protocol;
    private @NotNull AssetMetadata metadata;

    Asset(String id, String name, String symbol, int exponent, String protocol, AssetMetadata metadata) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.exponent = exponent;
        this.protocol = protocol;
        this.metadata = metadata;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getSymbol() {
        return symbol;
    }

    public @NotNull int getExponent() {
        return exponent;
    }

    public @NotNull String getProtocol() {
        return protocol;
    }

    public @NotNull AssetMetadata getMetadata() {
        return metadata;
    }
    
}