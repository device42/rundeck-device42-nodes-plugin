package com.device42.client.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Helper class of the abstract methods that helps to work with JSON objects
 * parameters when values under parameters can be null
 */
public abstract class JsonUtil {
	/**
	 * Default string value for non-existing parameters
	 */
	private static final String DEFAULT_STRING = "";
	/**
	 * Default integer value for non-existing parameters
	 */
	private static final int DEFAULT_INT = 0;
	/**
	 * Default long value for non-existing parameters
	 */
	private static final long DEFAULT_LONG = 0L;

	/**
	 * Get the integer value from the JSON Object
	 * 
	 * @param json
	 *            JSON Object to get the value from
	 * @param key
	 *            The key of the parameter inside JSON object
	 * @return the integer value from the JSON Object under the key or default
	 *         value if it does not exist
	 * @throws JSONException
	 *             When the JSON Object is malformed
	 */
	public static int extractInt(JSONObject json, String key) throws JSONException {
		if (json == null) {
			return DEFAULT_INT;
		}
		return json.has(key) ? json.getInt(key) : DEFAULT_INT;
	}

	/**
	 * Get the JSON Object value from the JSON Object
	 * 
	 * @param json
	 *            JSON Object to get the value from
	 * @param key
	 *            The key of the parameter inside JSON object
	 * @return the JSON Object value from the JSON Object under the key or null
	 *         if it does not exist
	 * @throws JSONException
	 *             When the JSON Object is malformed
	 */
	public static JSONObject extractJson(JSONObject json, String key) throws JSONException {
		if (json == null) {
			return null;
		}
		return json.has(key) ? json.getJSONObject(key) : null;
	}

	/**
	 * Get the long value from the JSON Object
	 * 
	 * @param json
	 *            JSON Object to get the value from
	 * @param key
	 *            The key of the parameter inside JSON object
	 * @return the long value from the JSON Object under the key or default
	 *         value if it does not exist
	 * @throws JSONException
	 *             When the JSON Object is malformed
	 */
	public static long extractLong(JSONObject json, String key) throws JSONException {
		if (json == null) {
			return DEFAULT_LONG;
		}
		return json.has(key) ? json.getLong(key) : DEFAULT_LONG;
	}

	/**
	 * Get the String value from the JSON Object
	 * 
	 * @param json
	 *            JSON Object to get the value from
	 * @param key
	 *            The key of the parameter inside JSON object
	 * @return the String value from the JSON Object under the key or default
	 *         value if it does not exist
	 * @throws JSONException
	 *             When the JSON Object is malformed
	 */
	public static String extractString(JSONObject json, String key) throws JSONException {
		if (json == null) {
			return DEFAULT_STRING;
		}
		return json.has(key) ? json.getString(key) : DEFAULT_STRING;
	}

	/**
	 * Blocked default constructor to restrict the instantiation of the objects
	 * or inheritance
	 */
	private JsonUtil() {
	}
}
