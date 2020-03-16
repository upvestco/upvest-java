package co.upvest.endpoints;

import co.upvest.*;
import co.upvest.models.*;

import java.io.IOException;
import okhttp3.*;
import com.squareup.moshi.*;
import java.util.*;

public class TransactionsEndpoint implements Transaction.Endpoint<Transaction> {
    private APIClient apiClient;
    private Wallet wallet;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    final Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    final JsonAdapter<Object> objectAdapter = moshi.adapter(Object.class);
    final JsonAdapter<OAuth> oAuthAdapter = moshi.adapter(OAuth.class);
    final JsonAdapter<Transaction> transactionAdapter = moshi.adapter(Transaction.class);
    final JsonAdapter<Cursor<Transaction>> transactionCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, Transaction.class));

    public TransactionsEndpoint(APIClient apiClient, Wallet wallet) {
        this.apiClient = apiClient;
        this.wallet = wallet;
    }

    public Transaction create(String password, String assetId, String quantity, String fee, String recipient) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("kms")
                .addPathSegment("wallets")
                .addPathSegment(wallet.getId())
                .addPathSegment("transactions")
                .addPathSegment("")
                .build();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("password", password);
        params.put("asset_id", assetId);
        params.put("quantity", quantity);
        params.put("fee", fee);
        params.put("recipient", recipient);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Transaction transaction = transactionAdapter.fromJson(response.body().source());

        return transaction;
    }

    /**
     * Sign and broadcast complex transaction
     */
    public Transaction createComplex(String walletID, String Password, String transaction) throws IOException {
        return createComplex(walletID, Password, transaction, true);
    }

    public Transaction createComplex(String walletID, String password, String transaction, boolean fund) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment(walletID)
                .addPathSegment("transactions")
                .addPathSegment("complex")
                .addPathSegment("")
                .build();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("password", password);
        params.put("fund", fund);
        params.put("tx", transaction);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Transaction txn = transactionAdapter.fromJson(response.body().source());
        return txn;
    }

    /**
     * Sign a raw transaction and broadcast it to the blockchain.
     */
    public Object createRaw(String walletID, String password, String rawTx) throws IOException {
        return createRaw(walletID, password, rawTx, "base64", true);
    }

    public Object createRaw(String walletID, String password, String rawTx, String inputFormat, boolean fund) throws IOException{
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment(walletID)
                .addPathSegment("transactions")
                .addPathSegment("raw")
                //.addPathSegment("")
                .build();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("input_format", inputFormat);
        params.put("password", password);
        params.put("raw_tx", rawTx);
        params.put("fund", fund);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectAdapter.toJson(params)))
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Transaction transaction = transactionAdapter.fromJson(response.body().source());
        return transaction;
    }

    public Cursor<Transaction> list() throws IOException {
        return list(null);
    }

    /**
     * @param pageSize number of transactions to load per page (maximum 100)
     */
    public Cursor<Transaction> list(int pageSize) throws IOException {
        HttpUrl url =  apiClient.getBaseUrl()
                .addPathSegment("kms")
                .addPathSegment("wallets")
                .addPathSegment(wallet.getId())
                .addPathSegment("transactions")
                .addPathSegment("")
                .addQueryParameter("page_size", String.valueOf(pageSize))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Transaction> transactions = transactionCursorAdapter.fromJson(response.body().source());
        transactions.setEndpoint(this);
        return transactions;
    }

    /**
     * Load a page
     * @param cursor url of the (next/previous) page to load
     */
    public Cursor<Transaction> list(String cursor) throws IOException {
        HttpUrl url;

        if (cursor == null){
            url = apiClient.getBaseUrl()
                    .addPathSegment("kms")
                    .addPathSegment("wallets")
                    .addPathSegment(wallet.getId())
                    .addPathSegment("transactions")
                    .addPathSegment("")
                    .build();
        } else {
            url = HttpUrl.parse(cursor).newBuilder().scheme("https").build();
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Cursor<Transaction> transactions = transactionCursorAdapter.fromJson(response.body().source());
        transactions.setEndpoint(this);

        return transactions;
    }

    public Transaction get(String id) throws IOException {
        HttpUrl url = apiClient.getBaseUrl()
                .addPathSegment("kms")
                .addPathSegment("wallets")
                .addPathSegment(wallet.getId())
                .addPathSegment("transactions")
                .addPathSegment(id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = apiClient.getClient().newCall(request).execute();
        Transaction transaction = transactionAdapter.fromJson(response.body().source());

        return transaction;
    }

    APIClient getApiClient() {
        return this.apiClient;
    }

}
