package com.device42.client.parser;

import com.device42.client.model.Error;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Extract error object from the incoming JSON from REST API calls
 */
public class BasicErrorJsonParser implements JsonObjectParser<Error> {
	/**
	 * Parse the error from JSON and return the Error object
	 */
	@Override
	public Error parse(JSONObject json) throws JSONException {
		final String message = json.getString("msg");
		final int code = json.getInt("code");
		return new Error(message, code);
	}
}
