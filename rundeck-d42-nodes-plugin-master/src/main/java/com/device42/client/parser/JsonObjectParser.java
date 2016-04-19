package com.device42.client.parser;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public interface JsonObjectParser<T> {

	T parse(JSONObject json) throws JSONException;
}
