package com.device42.client.parser;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Indicate that implementation will be able to convert the collected result as
 * the JSON Object from the REST API call into the model object of the plugin
 * 
 * @param <T>
 *            The type of object the incoming JSON should be converted
 */
public interface JsonObjectParser<T> {

	/**
	 * Convert incoming JSON Object into the plugin representation of the CI
	 * 
	 * @param json
	 *            The JSON with the parameters expected for the type of
	 *            conversion
	 * @return The representation of CI that can be used in plugin
	 * @throws JSONException
	 */
	T parse(JSONObject json) throws JSONException;
}
