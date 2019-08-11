package co.upvest;

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

class TestHelper {

    static TenancyAPI tenancyAPI;
    static ClienteleAPI commonClienteleAPI;
    static User commonUser;
    static String commonPassword;

    static JSONObject config;

    static {
        try {
            config = new JSONObject(new String(Files.readAllBytes(Paths.get("test_config.json"))));
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    static TenancyAPI getTenancyAPI() {
        if (tenancyAPI == null)
            tenancyAPI = new TenancyAPI(config.getString("API_KEY"), config.getString("API_SECRET"), config.getString("API_PASSPHRASE"));
        return tenancyAPI;
    }

    static ClienteleAPI getClienteleAPI() throws IOException {
        if (commonClienteleAPI == null)
            commonClienteleAPI = getClienteleAPI(getUser().getUsername(), commonPassword);
        return commonClienteleAPI;
    }

    static ClienteleAPI getClienteleAPI(String username, String password) throws IOException {
        return new ClienteleAPI(
            config.getString("OAUTH2_CLIENT_ID"),
            config.getString("OAUTH2_CLIENT_SECRET"),
            username,
            password
        );
    }

    static User getUser() throws IOException {
        if (commonUser == null) {
            String username = "user_" + String.valueOf(java.time.Instant.now().getEpochSecond());
            commonPassword = "psswd_" + String.valueOf(java.time.Instant.now().getEpochSecond());
            commonUser = getTenancyAPI().users().create(username, commonPassword, new String[]{});
        }
        return commonUser;
    }

    static boolean isValidRecoveryKit(String input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(input)));

        return doc.getDocumentElement() != null;
    }

    static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    static int getBalanceForAssetId(Wallet wallet, String assetId) {
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
}