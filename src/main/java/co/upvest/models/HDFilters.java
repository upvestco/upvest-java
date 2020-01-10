package co.upvest.models;

import java.util.HashMap;
import java.util.Map;

// HDFilters is for filtering historical Data API queries
public class HDFilters {
    private String before;
    private String after;
    private int confirmations;
    private String cursor;
    private int limit;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("after", after);
        params.put("before", before);
        params.put("confirmations", confirmations);
        params.put("cursor", cursor);
        params.put("limit", limit);
        return params;
    }
}
