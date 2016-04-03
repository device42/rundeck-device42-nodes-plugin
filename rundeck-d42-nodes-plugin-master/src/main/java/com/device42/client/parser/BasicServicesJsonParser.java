package com.device42.client.parser;

import com.device42.client.model.Service;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicServicesJsonParser implements JsonObjectParser<List<Service>> {
    private final BasicServiceJsonParser serviceJsonParser = new BasicServiceJsonParser();

    @Override
    public List<Service> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("services");
        List<Service> res = new ArrayList<Service>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(serviceJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
