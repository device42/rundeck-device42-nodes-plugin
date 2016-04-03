package com.device42.client.parser;

import com.device42.client.model.Rack;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicRackJsonParser implements JsonObjectParser<Rack> {
    @Override
    public Rack parse(JSONObject json) throws JSONException {
        final long id = json.getLong("rack_id");
        final String name = (json.has("name")) ? json.getString("name") : "";
        final String building = (json.has("building")) ? json.getString("building") : "";
        final String room = (json.has("room")) ? json.getString("room") : "";
        return new Rack(id, name, building, room);
    }
}
