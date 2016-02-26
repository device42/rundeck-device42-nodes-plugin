package com.device42.client.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonUtil {
    private static final String DEFAULT_STRING = "";
    private static final int DEFAULT_INT = 0;
    private static final long DEFAULT_LONG = 0L;

    public static int extractInt(JSONObject json, String key) throws JSONException {
        if (json == null) {
            return DEFAULT_INT;
        }
        return json.has(key) ? json.getInt(key) : DEFAULT_INT;
    }

    public static JSONObject extractJson(JSONObject json, String key) throws JSONException {
        if (json == null) {
            return null;
        }
        return json.has(key) ? json.getJSONObject(key) : null;
    }

    public static long extractLong(JSONObject json, String key) throws JSONException {
        if (json == null) {
            return DEFAULT_LONG;
        }
        return json.has(key) ? json.getLong(key) : DEFAULT_LONG;
    }

    public static String extractString(JSONObject json, String key) throws JSONException {
        if (json == null) {
            return DEFAULT_STRING;
        }
        return json.has(key) ? json.getString(key) : DEFAULT_STRING;
    }

    private JsonUtil() {
    }
}
