package com.device42.client.services.parameters;

/**
 * The extention of @see InputParameters to support limits and offcets by
 * default
 *
 * @author Alexey Rjeutski
 */
public interface InputLimitParameters extends InputParameters {
	/**
	 * The tag that is used as the key for limit parameter
	 */
	String LIMIT_PARAMETER_TAG = "limit";
	/**
	 * The tag that is used as the key for offset parameter
	 */
	String OFFSET_PARAMETER_TAG = "offset";

	/**
	 * Set limit of the query
	 * 
	 * @param limit
	 *            The limit of the query. Maximum amount of entities expected to
	 *            receive from REST API call
	 */
	void addLimit(int limit);

	/**
	 * Set offset of the query
	 * 
	 * @param offset
	 *            The offset of the query. Starting number of the entity inside
	 *            the query for the REST API call
	 */
	void addOffset(int offset);
}
