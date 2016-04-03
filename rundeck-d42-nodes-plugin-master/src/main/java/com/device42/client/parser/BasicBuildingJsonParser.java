package com.device42.client.parser;

import com.device42.client.model.Building;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicBuildingJsonParser implements JsonObjectParser<Building> {
    @Override
    public Building parse(JSONObject json) throws JSONException {
        final long id = json.getLong("building_id");
        final String name = (json.has("name")) ? json.getString("name") : "";
        return new Building(id, name);
    }
}
