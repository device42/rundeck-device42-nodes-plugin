package com.device42.client.parser;

import com.device42.client.model.Device;
import com.device42.client.model.IP;
import com.device42.client.util.JsonUtil;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;

public class BasicDeviceJsonParser implements JsonObjectParser<Device> {

	@Override
	public Device parse(JSONObject json) throws JSONException {

		Device device = new Device();
		device.setId(JsonUtil.extractLong(json, "device_id"));
		device.setName(JsonUtil.extractString(json, "name"));
		device.setHardwareModel(JsonUtil.extractString(json, "hw_model"));
		device.setSerialNo(JsonUtil.extractString(json, "serial_no"));
		device.setAssetNo(JsonUtil.extractString(json, "asset_no"));
		device.setServiceLevel(JsonUtil.extractString(json, "service_level"));
		device.setType(JsonUtil.extractString(json, "type"));
		device.setVirtualHostName(JsonUtil.extractString(json, "virtual_host_name"));
		device.setManufacturer(JsonUtil.extractString(json, "manufacturer"));
		device.setUuid(JsonUtil.extractString(json, "uuid"));
		device.setRack(JsonUtil.extractString(json, "rack"));
		device.setCustomer(JsonUtil.extractString(json, "customer"));
		device.setRoom(JsonUtil.extractString(json, "room"));
		device.setBuilding(JsonUtil.extractString(json, "building"));
		device.setOs(JsonUtil.extractString(json, "os"));
		device.setOsVer(JsonUtil.extractString(json, "osver"));
		// Consider default as true
		device.setInService(!"false".equals(JsonUtil.extractString(json, "in_service")));
		if (json.has("ip_addresses")) {

			ArrayList<IP> listdata = new ArrayList<IP>();
			JSONArray jArray = json.getJSONArray("ip_addresses");
			if (jArray != null) {
				for (int i = 0; i < jArray.length(); i++) {
					listdata.add(new BasicIPJsonParser().parse(jArray.getJSONObject(i)));
				}
			}
			device.setIps(listdata);
		}
		if (json.has("tags")) {

			JSONArray tagsArray = json.getJSONArray("tags");
			ArrayList<String>tagsList = new ArrayList<String>();
			if (tagsArray != null)
				
			for (int i = 0; i < tagsArray.length(); i++) {
				tagsList.add(tagsArray.getString(i));
			}
			device.setTags(tagsList.toArray(new String[tagsArray.length()]));
		}

		return device;

	}

	
}
