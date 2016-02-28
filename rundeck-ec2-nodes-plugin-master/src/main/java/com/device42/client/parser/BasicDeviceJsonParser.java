package com.device42.client.parser;

import com.device42.client.model.Device;
import com.device42.client.model.IP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;

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
        if(json.has("ip_addresses")) {

            ArrayList<IP> listdata = new ArrayList<IP>();
            JSONArray jArray = json.getJSONArray("ip_addresses");
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    listdata.add(new BasicIPJsonParser().parse(jArray.getJSONObject(i)));
                }
            }
            device.setIps(listdata);
        }
        return device;
    }
}
