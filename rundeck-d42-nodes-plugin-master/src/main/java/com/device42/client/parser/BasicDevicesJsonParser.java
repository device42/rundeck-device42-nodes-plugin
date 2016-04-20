package com.device42.client.parser;

import com.device42.client.model.Device;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BasicDevicesJsonParser extends AbstractJsonObjectListParser<Device> {
	private BasicDeviceJsonParser deviceJsonParser = new BasicDeviceJsonParser();

	@Override
	public List<Device> parse(JSONObject json) throws JSONException {

		JSONArray jsonArray = json.getJSONArray("Devices");
		storeLimits(json);
		List<Device> res = new ArrayList<Device>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			res.add(deviceJsonParser.parse(jsonArray.getJSONObject(i)));
		}
		return res;

	}
}
