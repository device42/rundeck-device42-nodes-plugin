package com.device42.client.parser;

import com.device42.client.model.Device;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Convert the incoming JSON Object into the list of the @see
 * com.device42.client.model.Device
 */
public class BasicDevicesJsonParser extends AbstractJsonObjectListParser<Device> {
	/**
	 * Helper parser to convert the devices one by one
	 */
	private BasicDeviceJsonParser deviceJsonParser = new BasicDeviceJsonParser();

	/**
	 * Extract the list of the devices from the JSON Object and save the
	 * information about the limits and total volume of query
	 */
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
