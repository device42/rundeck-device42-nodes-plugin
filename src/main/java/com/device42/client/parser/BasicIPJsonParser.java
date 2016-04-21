package com.device42.client.parser;

import com.device42.client.model.IP;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Convert the incoming JSON Object into @see com.device42.client.model.IP
 * 
 * Created on 2016-02-21.
 * 
 * @author yunusdawji
 * 
 */
public class BasicIPJsonParser implements JsonObjectParser<IP> {

	/**
	 * Extract IP address from the JSON Object
	 */
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
