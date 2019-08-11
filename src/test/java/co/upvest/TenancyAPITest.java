package co.upvest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class TenancyAPITest {

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
}
