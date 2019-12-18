package co.upvest.endpoints;

import co.upvest.APIClient;
import co.upvest.models.*;

import com.squareup.moshi.*;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;

import java.io.IOException;

public class  HistoricalDataEndpoint {
    private class HDResponse {
        private @NotNull String result;

        public String getResult() {
            return result;
        }
    }

    private APIClient apiClient;

    final Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<HDBalance> hdBalanceAdapter = moshi.adapter(HDBalance.class);
    final JsonAdapter<HDTransaction> hdTransactionAdapter = moshi.adapter(HDTransaction.class);
    final JsonAdapter<HDBlock> hdBlockAdapter = moshi.adapter(HDBlock.class);
    final JsonAdapter<HDTransactionList> hdTransactionListAdapter = moshi.adapter(HDTransactionList.class);
    final JsonAdapter<HDStatus> hdStatusAdapter = moshi.adapter(HDStatus.class);
    final JsonAdapter<HDResponse> hdResponseAdapter = moshi.adapter(HDResponse.class);

    public HistoricalDataEndpoint(@NotNull APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public HDBlock getBlock(@NotNull String protocol, @NotNull String network, @NotNull String blockNumber) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
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
        HDResponse hdResponse = hdResponseAdapter.fromJson(response.body().source());
        HDBlock block = hdBlockAdapter.fromJson(hdResponse.getResult());
        return block;
    }

    public HDTransaction getTxByHash(@NotNull String protocol, @NotNull String network, @NotNull String txHash) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
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
        HDResponse hdResponse = hdResponseAdapter.fromJson(response.body().source());
        HDTransaction transaction = hdTransactionAdapter.fromJson(hdResponse.getResult());
        return transaction;
    }

    public HDTransactionList listTransactions(@NotNull String protocol, @NotNull String network, @NotNull String address) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
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
        HDTransactionList transactions = hdTransactionListAdapter.fromJson(response.body().source());
        return transactions;
    }

    public HDBalance getAssetBalance(@NotNull String protocol, @NotNull String network, @NotNull String address) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
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
        HDResponse hdResponse = hdResponseAdapter.fromJson(response.body().source());
        HDBalance balance = hdBalanceAdapter.fromJson(hdResponse.getResult());
        return balance;
    }

    public HDBalance getContractBalance(@NotNull String protocol, @NotNull String network, @NotNull String address,
                                        @NotNull String contractAddress) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
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
        HDResponse hdResponse = hdResponseAdapter.fromJson(response.body().source());
        HDBalance balance = hdBalanceAdapter.fromJson(hdResponse.getResult());
        return balance;
    }

    public HDStatus getStatus(@NotNull String protocol, @NotNull String network) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
                .addPathSegment("data")
                .addPathSegment(protocol)
                .addPathSegment(network)
                .addPathSegment("status")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        HDResponse hdResponse = hdResponseAdapter.fromJson(response.body().source());
        HDStatus status = hdStatusAdapter.fromJson(hdResponse.getResult());
        return status;
    }

}