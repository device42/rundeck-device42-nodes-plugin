package com.device42.client.parser;

import com.device42.client.model.Device;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasicDevicesJsonParser implements JsonObjectParser<List<Device>> {
    private BasicDeviceJsonParser deviceJsonParser = new BasicDeviceJsonParser();
    static final Logger logger = Logger.getLogger(BasicDevicesJsonParser.class);

    @Override
    public List<Device> parse(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("Devices");
        logger.warn(jsonArray.toString());
        List<Device> res = new ArrayList<Device>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            res.add(deviceJsonParser.parse(jsonArray.getJSONObject(i)));
        }
        return res;
    }
}
