package com.device42.client.parser;

import com.device42.client.model.Building;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicBuildingsJsonParser implements JsonObjectParser<List<Building>> {
    private final BasicBuildingJsonParser buildingJsonParser = new BasicBuildingJsonParser();

    @Override
    public List<Building> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("buildings");
        List<Building> res = new ArrayList<Building>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(buildingJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
