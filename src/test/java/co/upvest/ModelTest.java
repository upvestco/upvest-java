package co.upvest;

import co.upvest.models.*;

import com.squareup.moshi.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class ModelTest {

    final Moshi moshi = new Moshi.Builder()
        .add(new WalletAdapter(null))
        .build();
    final private JsonAdapter<Echo> echoJsonAdapter = moshi.adapter(Echo.class);
    final private JsonAdapter<Asset> assetJsonAdapter = moshi.adapter(Asset.class);
    final private JsonAdapter<AssetMetadata> assetMetadataJsonAdapter = moshi.adapter(AssetMetadata.class);
    final private JsonAdapter<Balance> balanceJsonAdapter = moshi.adapter(Balance.class);
    final private JsonAdapter<Signature.PublicKey> publicKeyJsonAdapter = moshi.adapter(Signature.PublicKey.class);
    final private JsonAdapter<Signature> signatureJsonAdapter = moshi.adapter(Signature.class);
    final private JsonAdapter<Transaction> transactionJsonAdapter = moshi.adapter(Transaction.class);
    final private JsonAdapter<User> userJsonAdapter = moshi.adapter(User.class);
    final JsonAdapter<Wallet> walletAdapter = moshi.adapter(Wallet.class);
    final JsonAdapter<OAuth> oAuthAdapter = moshi.adapter(OAuth.class);

    final JsonAdapter<Cursor<User>> userCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, User.class));
    
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
    public void testCursor() throws IOException {
        String previous = "https://api.playground.upvest.co/1.0/kms/wallets/?cursor=abc";
        String next = "https://api.playground.upvest.co/1.0/kms/wallets/?cursor=xyz";

        String username = "Jane Doe";
        String recoverykit = "<svg height='125mm' version='1.1' viewBox='0 0 125 125' width='125mm' xmlns='http://www.w3.org/2000/ ...";
        String[] walletIds = new String[]{
            "7e0af700-baed-45c2-9455-e43f88e9501a",
            "8bd7a1ef-4a92-4767-b59a-714cb487cbd9"
        };

        String source = "{" +
            "\"previous\": \"" + previous + "\"," +
            "\"next\": \"" + next + "\"," +
            "\"results\": [{" +
                "\"username\": \"" + username + "\"," +
                "\"recoverykit\": \"" + recoverykit + "\"," +
                "\"wallet_ids\": [" +
                    "\"" + walletIds[0] + "\"," +
                    "\"" + walletIds[1] + "\"" +
                "]"+
            "}]" +
        "}";

        Cursor<User> users = userCursorAdapter.fromJson(source);

        assertEquals(username, users.toArray()[0].getUsername());
        assertEquals(recoverykit, users.toArray()[0].getRecoverykit());
        assertEquals(walletIds[0], users.toArray()[0].getWalletIds()[0]);
        assertEquals(walletIds[1], users.toArray()[0].getWalletIds()[1]);
        
        int counter = 0;
        for (User user : users) {
            counter++;
        }

        assertEquals(users.toArray().length, counter);
        assertEquals(1, counter);
        assertTrue(users.hasNextPage());
        assertTrue(users.hasPreviousPage());
    }


}