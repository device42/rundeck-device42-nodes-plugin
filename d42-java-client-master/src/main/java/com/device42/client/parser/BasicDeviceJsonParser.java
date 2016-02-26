package com.device42.client.parser;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.device42.client.model.Device;

public class BasicDeviceJsonParser implements JsonObjectParser<Device> {
    @Override
    public Device parse(JSONObject json) throws JSONException {
        Device device = new Device();
        device.setId(json.getLong("device_id"));
        device.setName(json.getString("name"));
        device.setHardwareModel(json.has("hw_model") ? json.getString("hw_model") : "");
        device.setSerialNo(json.has("serial_no") ? json.getString("serial_no") : "");
        device.setAssetNo(json.has("asset_no") ? json.getString("asset_no") : "");
        device.setServiceLevel(json.has("service_level") ? json.getString("service_level") : "");
        device.setType(json.has("type") ? json.getString("type") : "");
        device.setVirtualHostName(json.has("virtual_host_name") ? json.getString("virtual_host_name") : "");
        device.setManufacturer(json.has("manufacturer") ? json.getString("manufacturer") : "");
        device.setUuid(json.has("uuid") ? json.getString("uuid") : "");
        device.setRack(json.has("rack") ? json.getString("rack") : "");
        return device;
    }
}
