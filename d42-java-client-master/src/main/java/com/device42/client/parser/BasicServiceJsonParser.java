package com.device42.client.parser;

import com.device42.client.model.Service;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicServiceJsonParser implements JsonObjectParser<Service> {
    @Override
    public Service parse(JSONObject json) throws JSONException {
        final long id = json.getLong("id");
        final String name = json.getString("name");
        final String displayName = json.getString("displayname");
        final String description = json.getString("description");
        final String category = json.getString("category");
        return new Service(id, name, displayName, description, category);
    }
}
