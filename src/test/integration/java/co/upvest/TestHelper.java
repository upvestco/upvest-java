package co.upvest;

import co.upvest.models.*;

import com.squareup.moshi.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestHelper {

    static TenancyAPI tenancyAPI;
    static ClienteleAPI commonClienteleAPI;
    static ClienteleAPI moneyClienteleAPI;
    static User commonUser;
    static User userWithMoney;
    static String commonPassword;

    public static final Moshi moshi = new Moshi.Builder()
        .add(new WalletAdapter(null))
        .build();
    public static final JsonAdapter<Wallet> walletAdapter = moshi.adapter(Wallet.class);

    static JSONObject config;

    static {
        try {
            config = new JSONObject(new String(Files.readAllBytes(Paths.get("test_config.json"))));
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static TenancyAPI getTenancyAPI() {
        if (tenancyAPI == null)
            tenancyAPI = new TenancyAPI(config.getString("API_KEY"), config.getString("API_SECRET"), config.getString("API_PASSPHRASE"));
        return tenancyAPI;
    }

    public static ClienteleAPI getClienteleAPI() throws IOException {
        if (commonClienteleAPI == null)
            commonClienteleAPI = getClienteleAPI(getUser().getUsername(), commonPassword);
        return commonClienteleAPI;
    }


    public static ClienteleAPI getClienteleAPIWithMoney() throws IOException {
        if (moneyClienteleAPI == null)
            moneyClienteleAPI = getClienteleAPI(config.getJSONObject("user").getString("username"), config.getJSONObject("user").getString("password"));
        return moneyClienteleAPI;
    }

    public static ClienteleAPI getClienteleAPI(String username, String password) throws IOException {
        return new ClienteleAPI(
            config.getString("OAUTH2_CLIENT_ID"),
            config.getString("OAUTH2_CLIENT_SECRET"),
            username,
            password
        );
    }

    public static User getUser() throws IOException {
        if (commonUser == null) {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
            commonPassword = "psswd_" + String.valueOf(java.time.Instant.now().getEpochSecond());
            commonUser = getTenancyAPI().users().create(username, commonPassword, new String[]{});
        }
        return commonUser;
    }

    public static User getUserWithMoney() throws IOException {
        if (userWithMoney == null) {
            userWithMoney = getTenancyAPI().users().get(config.getJSONObject("user").getString("username"));
        }
        return userWithMoney;
    }

    public static boolean isValidRecoveryKit(String input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(input)));

        return doc.getDocumentElement() != null;
    }

    public static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static int getBalanceForAssetId(Wallet wallet, String assetId) {
        return 0;
    //     for (Balance balance : wallet.getBalances()) {
    //         if (balance.getSymbol())
    //     }
    //     wallet.balances.forEach(function callback(loopBalance, index) {
    //         if (loopBalance.asset_id == assetId) {
    //         balance = loopBalance;
    //         }
    //     });
        // }
    // return balance;
    }

    public static Wallet getWallet() throws IOException {
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
        return wallet;
    }
}