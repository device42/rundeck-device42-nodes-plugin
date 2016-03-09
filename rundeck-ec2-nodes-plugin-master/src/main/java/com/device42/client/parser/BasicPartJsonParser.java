package com.device42.client.parser;

import com.device42.client.model.Part;
import com.device42.client.util.JsonUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicPartJsonParser implements JsonObjectParser<Part> {
    @Override
    public Part parse(JSONObject json) throws JSONException {
        final long id = JsonUtil.extractLong(json, "part_id");
        final String description = JsonUtil.extractString(json, "description");
        final String assignment = JsonUtil.extractString(json, "assignment");
        final String serialNo = JsonUtil.extractString(json, "serial_no");
        final int count = JsonUtil.extractInt(json, "count");
        JSONObject partModel = JsonUtil.extractJson(json, "partmodel");
        final String modelName = JsonUtil.extractString(partModel, "name");
        final String modelType = JsonUtil.extractString(partModel, "type");
        return new Part(id, modelName, modelType, description, assignment, serialNo, count);
    }
}
