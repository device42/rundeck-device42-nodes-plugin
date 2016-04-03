package com.device42.client.parser;

import com.device42.client.model.Part;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicPartsJsonParser implements JsonObjectParser<List<Part>> {
    private BasicPartJsonParser partJsonParser = new BasicPartJsonParser();

    @Override
    public List<Part> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("parts");
        List<Part> res = new ArrayList<Part>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(partJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
