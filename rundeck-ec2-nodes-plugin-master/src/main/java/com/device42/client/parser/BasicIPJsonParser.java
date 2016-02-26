package com.device42.client.parser;

import com.device42.client.model.Device;
import com.device42.client.model.IP;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by yunusdawji on 2016-02-21.
 */
public class BasicIPJsonParser implements JsonObjectParser<IP> {

    @Override
    public IP parse(JSONObject json) throws JSONException {
        IP ip = new IP();
        ip.setIp(json.has("ip") ? json.getString("ip") : "");
        ip.setLabel(json.has("label") ? json.getString("label") : "");
        ip.setMacaddress(json.has("macaddress") ? json.getString("macaddress") : "");
        ip.setSubnet(json.has("subnet") ? json.getString("subnet") : "");
        ip.setType(json.has("type") ? json.getString("type") : "");
        return ip;
    }
}
