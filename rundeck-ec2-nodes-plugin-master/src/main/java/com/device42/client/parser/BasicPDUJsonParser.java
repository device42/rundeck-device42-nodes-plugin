package com.device42.client.parser;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.device42.client.model.PDU;
import com.device42.client.util.JsonUtil;

public class BasicPDUJsonParser implements JsonObjectParser<PDU> {
    @Override
    public PDU parse(JSONObject json) throws JSONException {
        final long id = JsonUtil.extractLong(json, "pdu_id");
        final String name = JsonUtil.extractString(json, "name");
        final String notes = JsonUtil.extractString(json, "notes");
        return new PDU(id, name, notes);
    }
}
