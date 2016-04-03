package com.device42.client.parser;

import com.device42.client.model.Error;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicErrorJsonParser implements JsonObjectParser<Error> {
    @Override
    public Error parse(JSONObject json) throws JSONException {
        final String message = json.getString("msg");
        final int code = json.getInt("code");
        return new Error(message, code);
    }
}
