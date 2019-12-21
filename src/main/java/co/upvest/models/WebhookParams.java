package co.upvest.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebhookParams {

    private @NotNull
    String url;
    private @NotNull
    String name;
    private @NotNull
    String hmac_secret_key;
    private @Nullable
    Map<String, String> headers;
    private @NotNull
    String version;
    private @NotNull
    String status;
    private @NotNull
    List<String> event_filters;

    public WebhookParams(String url, String name, String hmac_secret_key, Map<String, String> headers, String version, String status) {
        this.url = url;
        this.name = name;
        this.hmac_secret_key = hmac_secret_key;
        this.headers = headers;
        this.version = version;
        this.status = status;
    }

    public WebhookParams(String url, String name, String hmac_secret_key, Map<String, String> headers, String version, String status, List<String> event_filters) {
        this.url = url;
        this.name = name;
        this.hmac_secret_key = hmac_secret_key;
        this.headers = headers;
        this.version = version;
        this.status = status;
        this.event_filters = event_filters;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("url", url);
        params.put("name", name);
        params.put("hmac_secret_key", hmac_secret_key);
        params.put("headers", headers);
        params.put("version", version);
        params.put("status", status);
        params.put("event_filters", event_filters);

        return params.entrySet().stream().filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
