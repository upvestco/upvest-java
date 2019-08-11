package co.upvest;

import com.squareup.moshi.Json;

public class AssetMetadata {

    @Json(name = "chain_id") private int chainId;
    private String contract;
    private String genesis;

    AssetMetadata(int chainId, String contract, String genesis) {
        this.chainId = chainId;
        this.contract = contract;
        this.genesis = genesis;
    }

    public int getChainId() {
        return chainId;
    }

    public String getContract() {
        return contract;
    }

    public String getGenesis() {
        return genesis;
    }
    
}
