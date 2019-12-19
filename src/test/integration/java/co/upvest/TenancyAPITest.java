package co.upvest;

import co.upvest.models.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


public class TenancyAPITest {

    private String ethProtocol = "ethereum";
    private String ethNetwork = "ropsten";

    @Test public void testDefaultHost() {
        String testhost = "testhost";
        APIClient.setDefaultHost(testhost);
        assertEquals(APIClient.getDefaultHost(), testhost);
        APIClient.setDefaultHost(APIClient.PLAYGROUND_HOST);
    }

    @Test public void testEcho() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i){
                String echo = tenancyAPI.echo(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testEchoGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i){
                String echo = tenancyAPI.echoGet(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUserCreate() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
        try {
            User user = tenancyAPI.users().create(username, "password", new String[]{});

            assertEquals(username, user.getUsername());
            assertTrue("recovery kit", TestHelper.isValidRecoveryKit(user.getRecoverykit()));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUserUpdatePassword() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
        String pass1 = "password";
        String pass2 = "n3w-password";

        try {
            User user = tenancyAPI.users().create(username, pass1, new String[]{});
            user = tenancyAPI.users().update(user.getUsername(), pass1, pass2);

            assertEquals(username, user.getUsername());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUserDelete() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
        try {
            User user = tenancyAPI.users().create(username, "password", new String[]{});

            assertTrue(tenancyAPI.users().delete(user.getUsername()));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUsersListAndGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            User[] users = tenancyAPI.users().list().toArray();

            for (User user : users) {
                User otherUser = tenancyAPI.users().get(user.getUsername());

                assertEquals(user.getUsername(), otherUser.getUsername());
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUsersCursor() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            Cursor<User> users = tenancyAPI.users().list();

            int counter = 0;
            for (User user : users) {
                counter++;
            }

            assertEquals(users.toArray().length, counter);

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUsersCursorWithPageSize() {
        testCursorWithPageSize(TestHelper.getTenancyAPI().users(), 10);
    }

    @Test public void testAssetCursorWithPageSize() {
        testCursorWithPageSize(TestHelper.getTenancyAPI().assets(), 2);
    }

    public <Type extends Listable> void testCursorWithPageSize(Listable.Endpoint<Type> endpoint, int pageSize) {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
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

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testAssetsListAndGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            Asset[] assets = tenancyAPI.assets().list().toArray();

            for (Asset asset : assets) {
                System.out.println("\"" + asset.getName() + "\": \"" + asset.getId() + "\",");
                Asset otherAsset = tenancyAPI.assets().get(asset.getId());

                assertEquals("listed and retrieved ids differ", asset.getId(), otherAsset.getId());
                assertEquals("listed and retrieved names differ", asset.getName(), otherAsset.getName());
                assertEquals("listed and retrieved exponents differ", asset.getExponent(), otherAsset.getExponent());
                assertEquals("listed and retrieved protocols differ", asset.getProtocol(), otherAsset.getProtocol());

                assertTrue("asset.id  doesn't match UUID pattern", asset.getId().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testUserCreateWithWallet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());

            JSONObject assets = TestHelper.config.getJSONObject("assetIds");
            Object[] arr = assets.toMap().values().toArray();
            String[] assetIds = new String[arr.length];

            for (int i = 0; i < arr.length; ++i){
                assetIds[i] = (String) arr[i];
            }

            User user = tenancyAPI.users().create(username, "password", assetIds);

            assertEquals("ETH and ERC20 get combined in 1 wallet.", assetIds.length - 1, user.getWalletIds().length);
            assertTrue(TestHelper.isValidRecoveryKit(user.getRecoverykit()));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testWalletListAndGet(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());

            JSONObject assets = TestHelper.config.getJSONObject("assetIds");
            Object[] arr = assets.toMap().values().toArray();
            String[] assetIds = new String[arr.length];

            for (int i = 0; i < arr.length; ++i){
                assetIds[i] = (String) arr[i];
            }

            User user = tenancyAPI.users().create(username, "password", assetIds);

            assertEquals("ETH and ERC20 get combined in 1 wallet.", assetIds.length - 1, user.getWalletIds().length);
            assertTrue(TestHelper.isValidRecoveryKit(user.getRecoverykit()));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testHistoricalTransactionByTxHash(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String txHash = "0xa313aaad0b9b1fd356f7f42ccff1fa385a2f7c2585e0cf1e0fb6814d8bdb559a";

            HDTransaction tx = tenancyAPI.historical().getTxByHash(ethProtocol, ethNetwork, txHash);
            assertEquals("transaction hashes match", tx.getHash(), txHash.substring(2));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testHistoricalBlock(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String blockNumber = "6570890";

            HDBlock block = tenancyAPI.historical().getBlock(ethProtocol, ethNetwork, blockNumber);
            assertEquals("block numbers match", block.getNumber(), blockNumber);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testHistoricalAssetBalance(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String addr = "0x93b3d0b2894e99c2934bed8586ea4e2b94ce6bfd";

            HDBalance balance = tenancyAPI.historical().getAssetBalance(ethProtocol, ethNetwork, addr);
            assertNotNull(balance.getAddress());
            assertEquals("addresses match", balance.getAddress(), addr);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testHistoricalContractBalance(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String addr = "0x93b3d0b2894e99c2934bed8586ea4e2b94ce6bfd";
            String contractAddr = "0x1d7cf6ad190772cc6177beea2e3ae24cc89b2a10";

            HDBalance balance = tenancyAPI.historical().getContractBalance(ethProtocol, ethNetwork, addr, contractAddr);
            assertNotNull(balance.getAddress());
            assertEquals("addresses match", balance.getAddress(), addr);
            assertEquals("contract addresses match", balance.getContract(), contractAddr);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testHistoricalStatus(){
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            HDStatus status = tenancyAPI.historical().getStatus(ethProtocol, ethNetwork);
            assertNotNull(status.getHighest());
            assertNotNull(status.getLowest());
            assertNotNull(status.getLatest());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
