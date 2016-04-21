package com.device42.client.parser;

import java.util.List;

/**
 * Indicate that implementation will be able to convert the arrays of the CIs
 * into the lists of model representation of the objects. The specific of this
 * interface is that it collects the results about the existing limit of the
 * output and total amount of the data in the request
 * 
 * @author Alexey Rjeutski
 *
 * @param <T>
 *            The type of object the incoming JSON that will be the list member
 */
public interface JsonObjectListParser<T> extends JsonObjectParser<List<T>> {
	/**
	 * The tag that used to get the amount of devices actually returned from the
	 * JSON object from REST API requests
	 */
	String LIMIT_TAG = "limit";
	/**
	 * The tag that used to get the total amount of data inside the initial
	 * request
	 */
	String TOTAL_COUNT_TAG = "total_count";

	/**
	 * Get the total count of data that is expected to collect
	 * 
	 * @return Total count of data that is expected to collect
	 */
	int getCount();

	/**
	 * Get the current size of the data array returned by REST API subrequest
	 * 
	 * @return The current size of the data array returned by REST API
	 *         subrequest
	 */
	int getLimit();
}
