package co.upvest.models;

import co.upvest.endpoints.*;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Wallet implements Listable {

    private WalletsEndpoint walletsEndpoint;

    private @NotNull String id;
    private @NotNull Balance[] balances;
    private @NotNull String protocol;
    private @NotNull String address;
    private @NotNull String status;

    private TransactionsEndpoint transactionsEndpoint;

    Wallet (String id, Balance[] balances, String protocol, String address, String status, WalletsEndpoint walletsEndpoint) {
        this.id = id;
        this.balances = balances;
        this.protocol = protocol;
        this.address = address;
        this.status = status;
        this.walletsEndpoint = walletsEndpoint;
    }

    public TransactionsEndpoint transactions() {
        if (transactionsEndpoint == null)
            transactionsEndpoint = new TransactionsEndpoint(walletsEndpoint.getAPIClient(), this);
        return transactionsEndpoint;
    }

    public String getId() {
        return id;
    }

    public Balance[] getBalances() {
        return balances;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public Signature sign(String password, String toSign, String inputFormat, String outputFormat) throws IOException {
        return walletsEndpoint.sign(this, password, toSign, inputFormat, outputFormat);
    }

}
