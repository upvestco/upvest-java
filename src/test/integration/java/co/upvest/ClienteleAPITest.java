package co.upvest;

import co.upvest.models.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import co.upvest.APIErrorInterceptor.APIError;

import static org.junit.Assert.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class ClienteleAPITest {

    @Test
    public void testEcho() {
        try {
            ClienteleAPI clienteleAPI = TestHelper.getClienteleAPI();
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i) {
                String echo = clienteleAPI.echo(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEchoGet() {
        try {
            ClienteleAPI clienteleAPI = TestHelper.getClienteleAPI();
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i) {
                String echo = clienteleAPI.echoGet(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAssetsListAndGet() {
        try {
            ClienteleAPI clienteleAPI = TestHelper.getClienteleAPI();

            Asset[] assets = clienteleAPI.assets().list().toArray();

            for (Asset asset : assets) {
                Asset otherAsset = clienteleAPI.assets().get(asset.getId());

                assertNotNull(otherAsset.getId());
                assertNotNull(otherAsset.getName());
                assertNotNull(otherAsset.getSymbol());
                assertNotNull(otherAsset.getExponent());
                assertNotNull(otherAsset.getProtocol());
                assertNotNull(otherAsset.getMetadata());

                assertEquals("listed and retrieved ids differ", asset.getId(), otherAsset.getId());
                assertEquals("listed and retrieved names differ", asset.getName(), otherAsset.getName());
                assertEquals("listed and retrieved symbols differ", asset.getSymbol(), otherAsset.getSymbol());
                assertEquals("listed and retrieved exponents differ", asset.getExponent(), otherAsset.getExponent());
                assertEquals("listed and retrieved protocols differ", asset.getProtocol(), otherAsset.getProtocol());

                assertTrue("asset.id  doesn't match UUID pattern", asset.getId()
                        .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInvalidOAuth() {
        try {

            String[] usernames = new String[] { null, TestHelper.getUser().getUsername(), null,
                    TestHelper.getUser().getUsername(), "wrong", "wrong", };

            String[] passwords = new String[] { null, null, TestHelper.commonPassword, "wrong",
                    TestHelper.commonPassword, "wrong" };

            int[] results = new int[] { 400, 400, 400, 401, 401, 401 };

            for (int i = 0; i < results.length; ++i) {
                Throwable e = null;

                try {
                    TestHelper.getClienteleAPI(usernames[i], passwords[i]);
                } catch (Throwable ex) {
                    e = ex;
                }

                assertTrue("type is " + e.getClass().toString() + " - " + e.getMessage(),
                        e instanceof APIError || e instanceof IllegalArgumentException);
                if (e instanceof APIError) {
                    assertEquals(results[i], ((APIError) e).getCode());
                }
            }

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSigning() {
        try {
            User user = TestHelper.getUser();
            ClienteleAPI clienteleAPI = TestHelper.getClienteleAPI();

            Wallet createdWallet = clienteleAPI.wallets().create(TestHelper.commonPassword,
                    "deaaa6bf-d944-57fa-8ec4-2dd45d1f5d3f", null, null, null, null);

            Wallet[] wallets = clienteleAPI.wallets().list().toArray();

            for (Wallet wallet : wallets) {
                String[] protocolNames = new String[] { "ethereum", "ethereum_ropsten", "ethereum_kovan" };

                String[] asd = new String[] { "erc20", "erc20_ropsten", "erc20_kovan" };

                Signature signature = clienteleAPI.wallets().sign(wallet, TestHelper.commonPassword,
                        TestHelper.getRandomHexString(32 * 2), "hex", "hex");

                assertEquals(signature.getBigNumberFormat(), "hex");
                assertEquals(signature.getAlgorithm(), "ECDSA");
                assertEquals(signature.getCurve(), "secp256k1");
            }

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTransactionCreate() {
        try {
            User user = TestHelper.getUserWithMoney();
            ClienteleAPI clienteleAPI = TestHelper.getClienteleAPIWithMoney();

            JSONObject ethConfig = TestHelper.config.getJSONObject("faucet").getJSONObject("ethereum");
            String[] assetIds = new String[] { ethConfig.getJSONObject("eth").getString("assetId"),
                    ethConfig.getJSONObject("erc20").getString("assetId") };

            clienteleAPI.wallets().create(TestHelper.config.getJSONObject("user").getString("password"), assetIds[0],
                    null, null, null, null);
            clienteleAPI.wallets().create(TestHelper.config.getJSONObject("user").getString("password"), assetIds[1],
                    null, null, null, null);

            // Only test Tx creation for ETH and ERC20.
            String[] protocolNamesToTestTxWith = new String[] { "ethereum", "erc20", "ethereum_ropsten",
                    "erc20_ropsten", "ethereum_kovan", "erc20_kovan", };

            int counter = 0;
            Wallet[] wallets = clienteleAPI.wallets().list().toArray();
            for (Wallet wallet : wallets) {

                if (!Arrays.asList(protocolNamesToTestTxWith).contains(wallet.getProtocol())) {
                    continue;
                }

                // Create ETH transaction
                Transaction transaction = wallet.transactions().create(
                        TestHelper.config.getJSONObject("user").getString("password"),
                        ethConfig.getJSONObject("eth").getString("assetId"),
                        String.valueOf(ethConfig.getJSONObject("eth").getBigDecimal("amount")),
                        String.valueOf(ethConfig.getBigDecimal("gasPrice").multiply(new BigDecimal("21000"))),
                        ethConfig.getJSONObject("holder").getString("address"));

                assertNotEquals(null, transaction.getAssetName());
                assertNotEquals(null, transaction.getFee());
                assertNotEquals(null, transaction.getId());
                assertNotEquals(null, transaction.getQuantity());
                assertEquals(ethConfig.getJSONObject("holder").getString("address"), transaction.getRecipient());
                assertNotEquals(null, transaction.getSender());
                assertEquals("PENDING", transaction.getStatus());
                assertNotEquals(null, transaction.getTxhash());
                assertNotEquals(null, transaction.getWalletId());

                counter++;
            }

            testCursorWithPageSize(wallets[0].transactions(), 1);

            if (counter == 0)
                fail("no tests performed");

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWalletListAndGet() throws IOException {
        ClienteleAPI clienteleAPI = TestHelper.getClienteleAPI();
        JSONObject assets = TestHelper.config.getJSONObject("assetIds");
        Object[] arr = assets.toMap().values().toArray();

        for (int i = 0; i < arr.length; ++i) {
            clienteleAPI.wallets().create(TestHelper.commonPassword, (String) arr[i], null, null, null, null);
        }

        testCursorWithPageSize(clienteleAPI.wallets(), 1);

        Wallet[] wallets = clienteleAPI.wallets().list().toArray();
        assertEquals("ETH and ERC20 get combined in 1 wallet.", arr.length - 1, wallets.length);

        assertNotEquals(0, wallets.length);

        for (Wallet wallet : wallets) {
            Wallet otherWallet = clienteleAPI.wallets().get(wallet.getId());

            assertEquals(wallet.getAddress(), otherWallet.getAddress());
            assertEquals(wallet.getId(), otherWallet.getId());
            assertEquals(wallet.getProtocol(), otherWallet.getProtocol());
            assertEquals(wallet.getStatus(), otherWallet.getStatus());
            assertTrue(wallet.getStatus().equals("PENDING") || wallet.getStatus().equals("ACTIVE"));
            assertEquals(wallet.getBalances().length, otherWallet.getBalances().length);

            for (int i = 0; i < wallet.getBalances().length; ++i) {
                assertNotEquals(null, otherWallet.getBalances()[i].getAmount());
                assertNotEquals(null, otherWallet.getBalances()[i].getExponent());
                assertNotEquals(null, otherWallet.getBalances()[i].getName());
                assertNotEquals(null, otherWallet.getBalances()[i].getSymbol());
                assertNotEquals(null, otherWallet.getBalances()[i].getAssetId());

                assertEquals(wallet.getBalances()[i].getAmount(), otherWallet.getBalances()[i].getAmount());
                assertEquals(wallet.getBalances()[i].getExponent(), otherWallet.getBalances()[i].getExponent());
                assertEquals(wallet.getBalances()[i].getName(), otherWallet.getBalances()[i].getName());
                assertEquals(wallet.getBalances()[i].getSymbol(), otherWallet.getBalances()[i].getSymbol());
                assertEquals(wallet.getBalances()[i].getAssetId(), otherWallet.getBalances()[i].getAssetId());
            }
        }
    }

    public <Type extends Listable> void testCursorWithPageSize(Listable.Endpoint<Type> endpoint, int pageSize)
            throws IOException {
        endpoint.list();

        Cursor<Type> cursor = endpoint.list(pageSize);

        int counter = 0;
        for (Type t : cursor) {
            counter++;
        }

        assertEquals(cursor.toArray().length, counter);
        assertEquals(pageSize, counter);
        assertTrue(cursor.hasNextPage());
        assertFalse(cursor.hasPreviousPage());

        cursor = cursor.nextPage();
        assertTrue(cursor.hasPreviousPage());
    }

}
