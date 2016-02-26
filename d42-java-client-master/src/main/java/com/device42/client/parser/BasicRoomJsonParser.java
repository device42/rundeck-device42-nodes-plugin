package com.device42.client.parser;

import com.device42.client.model.Room;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicRoomJsonParser implements JsonObjectParser<Room> {
    @Override
    public Room parse(JSONObject json) throws JSONException {
        final long id = json.getLong("room_id");
        final String name = json.getString("name");
        final long buildingId = json.getLong("building_id");
        final String buildingName = json.getString("building");
        return new Room(id, name, buildingId, buildingName);
    }
}
