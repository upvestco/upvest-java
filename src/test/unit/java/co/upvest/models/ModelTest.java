package co.upvest.models;

import co.upvest.*;

import com.squareup.moshi.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class ModelTest {

    public static final Moshi moshi = new Moshi.Builder()
        .add(new WalletAdapter(null))
        .build();
    public static final JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    public static final JsonAdapter<Asset> assetJsonAdapter = moshi.adapter(Asset.class);
    public static final JsonAdapter<AssetMetadata> assetMetadataJsonAdapter = moshi.adapter(AssetMetadata.class);
    public static final JsonAdapter<Balance> balanceJsonAdapter = moshi.adapter(Balance.class);
    public static final JsonAdapter<Signature.PublicKey> publicKeyJsonAdapter = moshi.adapter(Signature.PublicKey.class);
    public static final JsonAdapter<Signature> signatureJsonAdapter = moshi.adapter(Signature.class);
    public static final JsonAdapter<Transaction> transactionJsonAdapter = moshi.adapter(Transaction.class);
    public static final JsonAdapter<User> userJsonAdapter = moshi.adapter(User.class);
    public static final JsonAdapter<Wallet> walletAdapter = moshi.adapter(Wallet.class);
    public static final JsonAdapter<OAuth> oAuthAdapter = moshi.adapter(OAuth.class);
    public static final JsonAdapter<HDTransaction> hdTransactionAdapter = moshi.adapter(HDTransaction.class);
    public static final JsonAdapter<HDBalance> hdBalanceAdapter = moshi.adapter(HDBalance.class);
    public static final JsonAdapter<HDStatus> hdStatusAdapter = moshi.adapter(HDStatus.class);

    @Test
    public void testEcho() throws IOException {
        String source = "{\"echo\":\"Hello World\"}";
        Echo echo = echoJsonAdapter.fromJson(source);

        assertEquals("Hello World", echo.getEcho());
    }

    @Test
    public void testAssetMetadata() throws IOException {
        int chainId = 3;
        String contract = "0xb95185ca02B704e46721d878EE7f566C6a2a959f";

        String source = "{" +
            "\"chain_id\": " + chainId + "," +
            "\"contract\":\"" + contract + "\"" +
        "}";

        AssetMetadata assetMetadata = assetMetadataJsonAdapter.fromJson(source);

       assertEquals(chainId, assetMetadata.getChainId());
       assertEquals(contract, assetMetadata.getContract());
    }

    @Test public void testAsset() throws IOException {
        String id = "cfc59efb-3b21-5340-ae96-8cadb4ce31a8";
        String name = "Example coin (Ropsten)";
        String symbol = "COIN";
        int exponent = 12;
        String protocol = "erc20_ropsten";

        int chainId = 3;
        String contract = "0xb95185ca02B704e46721d878EE7f566C6a2a959f";

        String source = "{" +
            "\"id\" : \"" + id + "\"," +
            "\"name\" : \"" + name + "\"," +
            "\"symbol\" : \"" + symbol + "\"," +
            "\"exponent\" : \"" + exponent + "\"," +
            "\"protocol\" : \"" + protocol + "\"," +
            "\"metadata\" : {" +
                "\"chain_id\": " + chainId + "," +
                "\"contract\":\"" + contract + "\"" +
            "}" +
        "}";

        Asset asset = assetJsonAdapter.fromJson(source);

        assertEquals(id, asset.getId());
        assertEquals(name, asset.getName());
        assertEquals(symbol, asset.getSymbol());
        assertEquals(exponent, asset.getExponent());
        assertEquals(protocol, asset.getProtocol());
    }

    @Test
    public void testBalance() throws IOException {
        String name = "Example coin (Ropsten)";
        String symbol = "COIN";
        int exponent = 12;
        String amount = "100";

        String source = "{" +
            "\"name\" : \"" + name + "\"," +
            "\"symbol\" : \"" + symbol + "\"," +
            "\"exponent\" : " + exponent + "," +
            "\"amount\" : \"" + amount + "\"" +
        "}";

        Balance balance = balanceJsonAdapter.fromJson(source);

        assertEquals(name, balance.getName());
        assertEquals(symbol, balance.getSymbol());
        assertEquals(exponent, balance.getExponent());
        assertEquals(amount, balance.getAmount());
    }

    @Test
    public void testPublicKey() throws IOException {
        String x = "x";
        String y = "y";

        String source = "{" +
            "\"x\" : \"" + x + "\"," +
            "\"y\" : \"" + y + "\"" +
        "}";

        Signature.PublicKey publicKey = publicKeyJsonAdapter.fromJson(source);

        assertEquals(x, publicKey.getX());
        assertEquals(y, publicKey.getY());
    }

    @Test
    public void testSignature() throws IOException {
        String bigNumberFormat = "bigNumberFormat";
        String algorithm = "algorithm";
        String curve = "curve";
        String r = "r";
        String s = "s";
        String recover = "recover";
        String x = "x";
        String y = "y";

        String source = "{" +
            "\"big_number_format\": \"" + bigNumberFormat + "\"," +
            "\"algorithm\": \"" + algorithm + "\"," +
            "\"curve\": \"" + curve + "\"," +
            "\"r\": \"" + r + "\"," +
            "\"s\": \"" + s + "\"," +
            "\"recover\": \"" + recover + "\"," +
            "\"public_key\": {"  +
                "\"x\" : \"" + x + "\"," +
                "\"y\" : \"" + y + "\"" +
            "}"+
        "}";

        Signature signature = signatureJsonAdapter.fromJson(source);

        assertEquals(bigNumberFormat, signature.getBigNumberFormat());
        assertEquals(algorithm, signature.getAlgorithm());
        assertEquals(curve, signature.getCurve());
        assertEquals(r, signature.getR());
        assertEquals(s, signature.getS());
        assertEquals(recover, signature.getRecover());
        assertEquals(x, signature.getPublicKey().getX());
        assertEquals(y, signature.getPublicKey().getY());
    }

    @Test
    public void testTransactions() throws IOException {
        String quantity = "37";
        String fee = "0";
        String recipient = "0x342b13903CFAC27aF1133bb29F2E3Fe9Bf4b0B9B";
        String sender = "0x6720d291a72b8673e774a179434c96d21eb85e71";
        String id = "ae110533-2e23-4f6a-9c3e-adfaa95b1440";
        String status = "CONFIRMED";
        String txhash = "0xe947bf4832ab5360aebc6e4407eb27e493c5da5ae5304aa10dbfddf123379f5b";
        String wallet_id = "3bf016a1-24d4-46e4-9800-9e3f223b9fab";
        String asset_id = "cfc59efb-3b21-5340-ae96-8cadb4ce31a8";
        String asset_name = "Example coin";
        int exponent = 2;

        String source = "{" +
            "\"quantity\":\"" + quantity + "\"," +
            "\"fee\":\"" + fee + "\"," +
            "\"recipient\":\"" + recipient + "\"," +
            "\"sender\":\"" + sender + "\"," +
            "\"id\":\"" + id + "\"," +
            "\"status\":\"" + status + "\"," +
            "\"txhash\":\"" + txhash + "\"," +
            "\"wallet_id\":\"" + wallet_id + "\"," +
            "\"asset_id\":\"" + asset_id + "\"," +
            "\"asset_name\":\"" + asset_name + "\"," +
            "\"exponent\": " + exponent +
        "}";

        Transaction transaction = transactionJsonAdapter.fromJson(source);

        assertEquals(quantity, transaction.getQuantity());
        assertEquals(fee, transaction.getFee());
        assertEquals(recipient, transaction.getRecipient());
        assertEquals(sender, transaction.getSender());
        assertEquals(id, transaction.getId());
        assertEquals(status, transaction.getStatus());
        assertEquals(txhash, transaction.getTxhash());
        assertEquals(wallet_id, transaction.getWalletId());
        assertEquals(asset_id, transaction.getAssetId());
        assertEquals(asset_name, transaction.getAssetName());
        assertEquals(exponent, transaction.getExponent());

    }

    @Test
    public void testUser() throws IOException {
        String username = "Jane Doe";
        String recoverykit = "<svg height='125mm' version='1.1' viewBox='0 0 125 125' width='125mm' xmlns='http://www.w3.org/2000/ ...";
        String[] walletIds = new String[]{
            "7e0af700-baed-45c2-9455-e43f88e9501a",
            "8bd7a1ef-4a92-4767-b59a-714cb487cbd9"
        };

        String source = "{" +
            "\"username\": \"" + username + "\"," +
            "\"recoverykit\": \"" + recoverykit + "\"," +
            "\"wallet_ids\": [" +
                "\"" + walletIds[0] + "\"," +
                "\"" + walletIds[1] + "\"" +
            "]"+
        "}";

        User user = userJsonAdapter.fromJson(source);

        assertEquals(username, user.getUsername());
        assertEquals(recoverykit, user.getRecoverykit());
        assertEquals(walletIds[0], user.getWalletIds()[0]);
        assertEquals(walletIds[1], user.getWalletIds()[1]);
    }


    @Test
    public void testWallet() throws IOException {
        String id = "3bf016a1-24d4-46e4-9800-9e3f223b9fab";
        String protocol = "co.upvest.kinds.Erc20";
        String address = "0x0123456789ABCDEF";
        int index = 0;
        String status = "ACTIVE";

        String name = "Example coin (Ropsten)";
        String symbol = "COIN";
        int exponent = 12;
        String amount = "100";

        String source = "{" +
            "\"id\": \"" + id + "\"," +
            "\"protocol\": \"" + protocol + "\"," +
            "\"address\": \"" + address + "\"," +
            "\"index\": \"" + index + "\"," +
            "\"status\": \"" + status + "\"," +
            "\"balances\": [{" +
                "\"name\" : \"" + name + "\"," +
                "\"symbol\" : \"" + symbol + "\"," +
                "\"exponent\" : " + exponent + "," +
                "\"amount\" : \"" + amount + "\"" +
            "}]" +
        "}";

        Wallet wallet = walletAdapter.fromJson(source);

        assertEquals(id, wallet.getId());
        assertEquals(protocol, wallet.getProtocol());
        assertEquals(address, wallet.getAddress());
        // assertEquals(index, wallet.getIndex());
        assertEquals(status, wallet.getStatus());

        assertEquals(name, wallet.getBalances()[0].getName());
        assertEquals(symbol, wallet.getBalances()[0].getSymbol());
        assertEquals(exponent, wallet.getBalances()[0].getExponent());
        assertEquals(amount, wallet.getBalances()[0].getAmount());
    }

    @Test
    public void testOAuth() throws IOException {
        String accessToken = "zSMWkPGyMatY8oYVsFEv1Pr9sjMS3Q";
        int expiresIn = 36000;
        String tokenType = "Bearer";
        String scope = "read write echo wallet transaction";
        String refreshToken = "iYmTFUisTiNSwdwFaNQ63U1a6bOBNs";

        String source = "{" +
            "\"access_token\": \"" + accessToken + "\"," +
            "\"expires_in\": \"" + expiresIn + "\"," +
            "\"token_type\": \"" + tokenType + "\"," +
            "\"scope\": \"" + scope + "\"," +
            "\"refresh_token\": \"" + refreshToken + "\"" +
        "}";

        OAuth oAuth = oAuthAdapter.fromJson(source);

        assertEquals(accessToken, oAuth.getAccessToken());
        assertEquals(expiresIn, oAuth.getExpiresIn());
        assertEquals(tokenType, oAuth.getTokenType());
        assertEquals(scope, oAuth.getScope());
        assertEquals(refreshToken, oAuth.getRefreshToken());
    }

    @Test
    public void testHDTransaction() throws IOException {

        String blockHash = "e33e56bb84929c297400fa9baa219804f0096fb1d2f7ee49d90d9f859c1e4940";
        int blockNumber = 6703934;
        String from = "0xb81e88279f3208001aeda20689d3e5d818758dbf";
        String hash = "a313aaad0b9b1fd356f7f42ccff1fa385a2f7c2585e0cf1e0fb6814d8bdb559a";
        String nonce = "7";
        int transactionIndex = 33;
        String to = "0x14039994048f873dc5f86a32075669652b9f0fa7";
        String value = "0";
        int gasPrice = 1000000000;
        String input = "0xa9059cbb0000000000000000000000003952ce12818b72443baa87bc1840d2f3df355972000000000000000000000000000000000000000000000000000000e8d4a51000";
        int confirmations = 282098;
        int gasLimit = 64896;
        // TODO: enfpre Map<String, Object> error type
        String error = "{}";

        String source = "{" +
                "\"blockHash\": \"" + blockHash + "\"," +
                "\"blockNumber\": \"" +  blockNumber + "\"," +
                "\"from\": \"" + from + "\"," +
                "\"hash\": \"" + hash + "\"," +
                "\"nonce\": \"" + nonce + "\"," +
                "\"transactionIndex\": \"" + transactionIndex + "\"," +
                "\"to\": \"" + to + "\"," +
                "\"value\": \"" + value +"\"," +
                "\"gasPrice\": \"" + gasPrice + "\"," +
                "\"input\": \"" + input + "\"," +
                "\"confirmations\": \"" + confirmations + "\"," +
                "\"gasLimit\": \"" + gasLimit + "\"," +
                "\"error\": \"" + error +"\"" +
                "}";

        HDTransaction tx = hdTransactionAdapter.fromJson(source);
        assertEquals(blockHash, tx.getBlockHash());
        assertEquals(blockNumber, tx.getBlockNumber());
        assertEquals(from, tx.getFrom());
        assertEquals(nonce, tx.getNonce());
        assertEquals(transactionIndex, tx.getTransactionIndex());
        assertEquals(to, tx.getTo());
        assertEquals(value, tx.getValue());
        assertEquals(gasPrice, tx.getGasPrice());
        assertEquals(input, tx.getInput());
        assertEquals(confirmations, tx.getConfirmations());
        assertEquals(gasLimit, tx.getGasLimit());
    }

    @Test
    public void testHDBalance() throws IOException {
        String id = "f439aa1f-977b-45e8-9c44-118f2de7beaa";
        String address = "0x93b3d0b2894e99c2934bed8586ea4e2b94ce6bfd";
        String balance_amount = "2901946820265775304";
        String transactionHash = "eaa8053f03c4d4bed5c0246f2b11cc030afe7fc992c7a49b85b7ac64fc394b4b";
        String transactionIndex = "49";
        String blockHash = "992cae09df00e2e3677e715c5d38902e6a5a97bb7a2186c2f27f40bd57995914";
        int blockNumber = 6979367;
        String timestamp = "2019-12-16T17:13:37.821969Z";
        boolean isMainChain = true;

        String source = "{" +
                "\"id\": \"" + id + "\"," +
                "\"address\": \"" + address + "\"," +
                "\"balance\": \"" + balance_amount + "\"," +
                "\"transactionHash\": \"" + transactionHash + "\"," +
                "\"transactionIndex\": \"" + transactionIndex + "\"," +
                "\"blockHash\": \"" + blockHash + "\"," +
                "\"blockNumber\": \"" + blockNumber + "\"," +
                "\"timestamp\": \"" + timestamp + "\"," +
                "\"isMainChain\":" + isMainChain +
                "}";

        HDBalance balance = hdBalanceAdapter.fromJson(source);
        assertEquals(id, balance.getID());
        assertEquals(address, balance.getAddress());
        assertEquals(transactionHash, balance.getTransactionHash());
        assertEquals(transactionIndex, balance.getTransactionIndex());
        assertEquals(blockHash, balance.getBlockHash());
        assertEquals(blockNumber, balance.getBlockNumber());
        assertEquals(timestamp, balance.getTimestamp());
        assertEquals(isMainChain, balance.getIsMainChain());
    }

    @Test
    public void testHDStatus() throws IOException {
        int highest = 6986410;
        int lowest = 2;
        int latest = 6986410;

        String source = "{" +
                "\"highest\": \"" + highest + "\"," +
                "\"lowest\": \"" + lowest + "\"," +
                "\"latest\": \"" + latest + "\"" +
                "}";

        HDStatus status = hdStatusAdapter.fromJson(source);
        assertEquals(highest, status.getHighest());
        assertEquals(lowest, status.getLowest());
        assertEquals(latest, status.getLatest());
    }
}