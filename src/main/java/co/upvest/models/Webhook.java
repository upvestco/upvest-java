package co.upvest.models;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Webhook implements Listable {
    private @NotNull String id;
    private @NotNull String url;
    private @NotNull String name;
    private @Json(name = "hmac_secret_key") @NotNull String HMACSecretKey;
    private Map<String, String> headers;
    private @NotNull String version;
    private @NotNull String status;
    private @Nullable List<EventFilter> event_filters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHMACSecretKey() {
        return HMACSecretKey;
    }

    public void setHMACSecretKey(String HMACSecretKey) {
        this.HMACSecretKey = HMACSecretKey;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EventFilter> getEvent_filters() {
        return event_filters;
    }

    public void setEvent_filters(List<EventFilter> event_filters) {
        this.event_filters = event_filters;
    }

    private static class EventFilter {
        private @Json(name = "event_noun") @NotNull String eventNoun;
        private @Json(name = "event_verb") @NotNull String eventVerb;
        private @Json(name = "limit_to_application") @NotNull String limitToApplication;
        private @Json(name = "max_confirmations") @NotNull String maxConfirmations;
        private @Json(name = "protocol_name") @NotNull String protocolName;
        private @Json(name = "wallet_address") @NotNull String walletAddress;

        public EventFilter(String eventNoun, String eventVerb, String limitToApplication, String maxConfirmations, String protocolName, String walletAddress) {
            this.eventNoun = eventNoun;
            this.eventVerb = eventVerb;
            this.limitToApplication = limitToApplication;
            this.maxConfirmations = maxConfirmations;
            this.protocolName = protocolName;
            this.walletAddress = walletAddress;
        }

        public String getEventNoun() {
            return eventNoun;
        }

        public String getEventVerb() {
            return eventVerb;
        }

        public String getLimitToApplication() {
            return limitToApplication;
        }

        public String getMaxConfirmations() {
            return maxConfirmations;
        }

        public String getProtocolName() {
            return protocolName;
        }

        public String getWalletAddress() {
            return walletAddress;
        }
    }
}

