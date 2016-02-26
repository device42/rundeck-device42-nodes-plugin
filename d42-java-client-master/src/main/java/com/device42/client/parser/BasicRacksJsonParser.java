package com.device42.client.parser;

import com.device42.client.model.Rack;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicRacksJsonParser implements JsonObjectParser<List<Rack>> {
    private BasicRackJsonParser rackJsonParser = new BasicRackJsonParser();

    @Override
    public List<Rack> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("racks");
        List<Rack> res = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(rackJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
