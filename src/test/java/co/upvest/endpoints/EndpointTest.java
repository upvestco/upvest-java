package co.upvest.endpoints;

import co.upvest.*;
import kotlin.NotImplementedError;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class EndpointTest {

    @Test
    public void testAssets() throws IOException {
        MockAPIClient mockAPIClient = new MockAPIClient();
        AssetsEndpoint assetsEndpoint = new AssetsEndpoint(mockAPIClient);

        assertEquals(mockAPIClient, assetsEndpoint.getApiClient());
    }

    @Test(expected = NotImplementedError.class)
    public void testGetAsset() throws IOException {
        AssetsEndpoint assetsEndpoint = new AssetsEndpoint(new MockAPIClient());
        assetsEndpoint.get("id");
    }

    @Test
    public void testTransactions() throws IOException {
        MockAPIClient mockAPIClient = new MockAPIClient();
        TransactionsEndpoint transactionsEndpoint = new TransactionsEndpoint(mockAPIClient, TestHelper.getWallet());

        assertEquals(mockAPIClient, transactionsEndpoint.getApiClient());
    }

    @Test(expected = NotImplementedError.class)
    public void testGetTransaction() throws IOException {
        TransactionsEndpoint transactionsEndpoint = new TransactionsEndpoint(new MockAPIClient(), TestHelper.getWallet());
        transactionsEndpoint.get("id");
    }

    @Test
    public void testUsers() throws IOException {
        UsersEndpoint usersEndpoint = new UsersEndpoint(TestHelper.getTenancyAPI());

        assertEquals(TestHelper.getTenancyAPI(), usersEndpoint.getApiClient());
    }

    // @Test(expected = NotImplementedError.class)
    // public void testGetUser(UsersEndpoint usersEndpoint) throws IOException {
    //     UsersEndpoint usersEndpoint = new UsersEndpoint(TestHelper.getTenancyAPI());
    //     usersEndpoint.get("id");
    // }

    @Test
    public void testWallets() throws IOException {
        MockAPIClient mockAPIClient = new MockAPIClient();
        WalletsEndpoint walletsEndpoint = new WalletsEndpoint(mockAPIClient);

        assertEquals(mockAPIClient, walletsEndpoint.getApiClient());
    }

    @Test(expected = NotImplementedError.class)
    public void testGetWallet() throws IOException {
        WalletsEndpoint walletsEndpoint = new WalletsEndpoint(new MockAPIClient());
        walletsEndpoint.get("id");
    }
}