package co.upvest.models;

import co.upvest.*;

import com.squareup.moshi.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class CursorTest {
    final Moshi moshi = new Moshi.Builder().build();
    final JsonAdapter<Cursor<User>> userCursorAdapter = moshi.adapter(Types.newParameterizedType(Cursor.class, User.class));

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