package com.device42.client.parser;

import org.codehaus.jettison.json.JSONObject;

/**
 * Abstract implementation of @see JsonObjectListParser<T> interface that allows
 * to gather the limit and total count locally and provides the primitive
 * accessors to those variables
 * 
 * @author Alexey Rjeutski
 *
 * @param <T>
 *            The type of object the incoming JSON that will be the list member
 */
public abstract class AbstractJsonObjectListParser<T> implements JsonObjectListParser<T> {

	/**
	 * Keeps the total amount of data that can be collected from the request
	 */
	private int count;
	/**
	 * Keeps the current amount of data inside the current response
	 */
	private int limit;

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	/**
	 * Extract the limit and total count from the incoming JSON Object
	 * 
	 * @param json
	 *            JSON Object that was returned from the REST API call
	 */
	protected void storeLimits(JSONObject json) {
		try {
			limit = json.getInt(LIMIT_TAG);
		} catch (Exception e) {
			// limit not found
		}
		try {
			count = json.getInt(TOTAL_COUNT_TAG);
		} catch (Exception e) {
			// total count not found
		}
	}

}
