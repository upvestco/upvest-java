package co.upvest.endpoints;

import co.upvest.APIClient;
import co.upvest.models.*;

import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;

import com.squareup.moshi.*;
import com.squareup.moshi.Types;
import org.jetbrains.annotations.NotNull;
import okhttp3.*;

import java.io.IOException;

public class HistoricalDataEndpoint {

    final Moshi moshi = new Moshi.Builder().add(EnvelopeJsonAdapter.FACTORY).build();
    final JsonAdapter<HDBalance> hdBalanceAdapter = moshi.adapter(HDBalance.class);
    final JsonAdapter<HDBlock> hdBlockAdapter = moshi.adapter(HDBlock.class);
    final JsonAdapter<HDTransactionList> hdTransactionListAdapter = moshi.adapter(HDTransactionList.class);
    final JsonAdapter<HDStatus> hdStatusAdapter = moshi.adapter(HDStatus.class);
    private APIClient apiClient;

    public HistoricalDataEndpoint(@NotNull APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public HDBlock getBlock(@NotNull String protocol, @NotNull String network, @NotNull String blockNumber) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("block")
                .addPathSegment(blockNumber)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        JsonAdapter<HDBlock> adapter = moshi.adapter(HDBlock.class, EnvelopeJsonAdapter.Enveloped.class);
        HDBlock block = adapter.fromJson(response.body().source());
        return block;
    }

    public HDTransaction getTxByHash(@NotNull String protocol, @NotNull String network, @NotNull String txHash) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("transaction")
                .addPathSegment(txHash)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        JsonAdapter<HDTransaction> adapter = moshi.adapter(HDTransaction.class, EnvelopeJsonAdapter.Enveloped.class);
        HDTransaction transaction = adapter.fromJson(response.body().source());
        return transaction;
    }

    public HDTransactionList listTransactions(@NotNull String protocol, @NotNull String network, @NotNull String address) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("transactions")
                .addPathSegment(address)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<HDTransactionList> hdTransactionListAdapter = moshi.adapter(HDTransactionList.class);
        HDTransactionList transactions = hdTransactionListAdapter.fromJson(response.body().source());
        return transactions;
    }

    public HDBalance getAssetBalance(@NotNull String protocol, @NotNull String network, @NotNull String address) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("balance")
                .addPathSegment(address)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        JsonAdapter<HDBalance> adapter = moshi.adapter(HDBalance.class, EnvelopeJsonAdapter.Enveloped.class);
        HDBalance balance = adapter.fromJson(response.body().source());
        return balance;
    }

    public HDBalance getContractBalance(@NotNull String protocol, @NotNull String network, @NotNull String address,
                                        @NotNull String contractAddress) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("balance")
                .addPathSegment(address)
                .addPathSegment(contractAddress)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        JsonAdapter<HDBalance> adapter = moshi.adapter(HDBalance.class, EnvelopeJsonAdapter.Enveloped.class);
        HDBalance balance = adapter.fromJson(response.body().source());
        return balance;
    }

    public HDStatus getStatus(@NotNull String protocol, @NotNull String network) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("status")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        JsonAdapter<HDStatus> adapter = moshi.adapter(HDStatus.class, EnvelopeJsonAdapter.Enveloped.class);
        HDStatus status = adapter.fromJson(response.body().source());
        return status;
    }

}