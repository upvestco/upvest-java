package co.upvest;

import co.upvest.models.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


public class TenancyAPITest {

    private String ethProtocol = "ethereum";
    private String ethNetwork = "ropsten";

    @Test
    public void testDefaultHost() {
        String testhost = "testhost";
        APIClient.setDefaultHost(testhost);
        assertEquals(APIClient.getDefaultHost(), testhost);
        APIClient.setDefaultHost(APIClient.PLAYGROUND_HOST);
    }

    @Test
    public void testEcho() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i) {
                String echo = tenancyAPI.echo(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEchoGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            JSONArray arr = TestHelper.config.getJSONArray("strs");
            for (int i = 0; i < arr.length(); ++i) {
                String echo = tenancyAPI.echoGet(arr.getString(i)).getEcho();
                assertEquals(arr.getString(i), echo);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUserCreate() {
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

    @Test
    public void testUserUpdatePassword() {
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

    @Test
    public void testUserDelete() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
        try {
            User user = tenancyAPI.users().create(username, "password", new String[]{});

            assertTrue(tenancyAPI.users().delete(user.getUsername()));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUsersListAndGet() {
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

    @Test
    public void testUsersCursor() {
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

    @Test
    public void testUsersCursorWithPageSize() {
        testCursorWithPageSize(TestHelper.getTenancyAPI().users(), 10);
    }

    @Test
    public void testAssetCursorWithPageSize() {
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

    @Test
    public void testAssetsListAndGet() {
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

    @Test
    public void testUserCreateWithWallet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());

            JSONObject assets = TestHelper.config.getJSONObject("assetIds");
            Object[] arr = assets.toMap().values().toArray();
            String[] assetIds = new String[arr.length];

            for (int i = 0; i < arr.length; ++i) {
                assetIds[i] = (String) arr[i];
            }

            User user = tenancyAPI.users().create(username, "password", assetIds);

            assertEquals("ETH and ERC20 get combined in 1 wallet.", assetIds.length - 1, user.getWalletIds().length);
            assertTrue(TestHelper.isValidRecoveryKit(user.getRecoverykit()));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWalletListAndGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());

            JSONObject assets = TestHelper.config.getJSONObject("assetIds");
            Object[] arr = assets.toMap().values().toArray();
            String[] assetIds = new String[arr.length];

            for (int i = 0; i < arr.length; ++i) {
                assetIds[i] = (String) arr[i];
            }

            User user = tenancyAPI.users().create(username, "password", assetIds);

            assertEquals("ETH and ERC20 get combined in 1 wallet.", assetIds.length - 1, user.getWalletIds().length);
            assertTrue(TestHelper.isValidRecoveryKit(user.getRecoverykit()));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // historical API
    @Test
    public void testHistoricalTransactionByTxHash() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String txHash = "0xa313aaad0b9b1fd356f7f42ccff1fa385a2f7c2585e0cf1e0fb6814d8bdb559a";

            HDTransaction tx = tenancyAPI.historical().getTxByHash(ethProtocol, ethNetwork, txHash);
            assertEquals("transaction hashes match", tx.getHash(), txHash.substring(2));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHistoricalBlock() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String blockNumber = "6570890";

            HDBlock block = tenancyAPI.historical().getBlock(ethProtocol, ethNetwork, blockNumber);
            assertEquals("block numbers match", block.getNumber(), blockNumber);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testHistoricalRetrieveTransactions() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String address = "0x6590896988376a90326cb2f741cb4f8ace1882d5";
            HDTransactionList transactions = tenancyAPI.historical().listTransactions(ethProtocol, ethNetwork, address);
            assertFalse(transactions.getResults().isEmpty());

        } catch (Exception e) {
            fail(e.getMessage());
        }

        // filter by confirmations
        try {
            String address = "0x6590896988376a90326cb2f741cb4f8ace1882d5";
            int confirmations = 1000;
            HDFilters filters = new HDFilters();
            filters.setConfirmations(confirmations);
            HDTransactionList transactions = tenancyAPI.historical().listTransactions(ethProtocol, ethNetwork, address, filters);
            boolean confirmationsOK = transactions.getResults().stream().filter(s -> s.getConfirmations() < 1000)
                    .collect(Collectors.toList()).isEmpty();
            assertTrue(confirmationsOK);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHistoricalAssetBalance() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String address = "0x93b3d0b2894e99c2934bed8586ea4e2b94ce6bfd";

            HDBalance balance = tenancyAPI.historical().getAssetBalance(ethProtocol, ethNetwork, address);
            assertNotNull(balance.getAddress());
            assertEquals("addresses match", balance.getAddress(), address);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHistoricalContractBalance() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            String address = "0x93b3d0b2894e99c2934bed8586ea4e2b94ce6bfd";
            String contractAddress = "0x1d7cf6ad190772cc6177beea2e3ae24cc89b2a10";

            HDBalance balance = tenancyAPI.historical().getContractBalance(ethProtocol, ethNetwork, address, contractAddress);
            assertNotNull(balance.getAddress());
            assertEquals("addresses match", balance.getAddress(), address);
            assertEquals("contract addresses match", balance.getContract(), contractAddress);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHistoricalStatus() {
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

    // webhooks
    @Test public void testWebhooksVerify() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();
        String url = TestHelper.config.getJSONObject("webhooks").getString("webhook_verification_url");

        try {
            boolean isVerified = tenancyAPI.webhooks().verify(url);
            assertTrue(isVerified);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testWebhooksCreate() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();
        WebhookParams params = TestHelper.getWebhookParams();

        try {
            Webhook webhook = tenancyAPI.webhooks().create(params);

            assertEquals(params.getVersion(), webhook.getVersion());
            assertEquals(params.getStatus(), webhook.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test public void testWebhooksListAndGet() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();

        try {
            Webhook[] webhooks = tenancyAPI.webhooks().list().toArray();

            for (Webhook webhook : webhooks) {
                Webhook otherWebhook = tenancyAPI.webhooks().get(webhook.getId());

                assertEquals(webhook.getUrl(), otherWebhook.getUrl());
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test public void testWebhooksDelete() {
        TenancyAPI tenancyAPI = TestHelper.getTenancyAPI();
        WebhookParams params = TestHelper.getWebhookParams();

        try {
            Webhook webhook = tenancyAPI.webhooks().create(params);
            // TODO: test retrieve after delete
            boolean isDeleted = tenancyAPI.webhooks().delete(webhook.getId());

            assertTrue(isDeleted);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
