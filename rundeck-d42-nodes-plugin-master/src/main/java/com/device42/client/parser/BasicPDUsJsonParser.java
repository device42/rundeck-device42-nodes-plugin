package com.device42.client.parser;

import com.device42.client.model.PDU;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicPDUsJsonParser implements JsonObjectParser<List<PDU>> {
    private BasicPDUJsonParser pduJsonParser = new BasicPDUJsonParser();

    @Override
    public List<PDU> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("pdus");
        List<PDU> res = new ArrayList<PDU>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(pduJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
