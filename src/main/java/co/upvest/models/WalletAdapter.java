package co.upvest.models;

import co.upvest.endpoints.*;

import com.squareup.moshi.*;

public class WalletAdapter {

    private WalletsEndpoint walletsEndpoint;

    public WalletAdapter(WalletsEndpoint walletsEndpoint) {
        this.walletsEndpoint = walletsEndpoint;
    }

    @FromJson Wallet fromJson(WalletJson walletJson) {
        Wallet wallet = new Wallet(
            walletJson.id,
            walletJson.balances,
            walletJson.protocol,
            walletJson.address,
            walletJson.index,
            walletJson.status,
            walletsEndpoint
        );
        return wallet;
    }

    @ToJson WalletJson eventToJson(Wallet wallet) {
        WalletJson walletJson = new WalletJson(
            wallet.getId(),
            wallet.getBalances(),
            wallet.getProtocol(),
            wallet.getAddress(),
            wallet.getIndex(),
            wallet.getStatus()
        );
        return walletJson;
    }

    private static class WalletJson {

        String id;
        Balance[] balances;
        String protocol;
        String address;
        int index;
        String status;

        public WalletJson(String id, Balance[] balances, String protocol, String address, int index, String status) {
            this.id = id;
            this.balances = balances;
            this.protocol = protocol;
            this.address = address;
            this.index = index;
            this.status = status;
        }
        
    }
}