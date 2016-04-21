package com.device42.client.services.parameters;

import java.util.Map;

/**
 * Abstract implementation of @see InputLimitParameters interface that allows to
 * add the limit and offset to the parameters map
 * 
 * @author Alexey Rjeutski
 *
 */
public abstract class AbstractInputLimitParameters implements InputLimitParameters {

	/**
	 * Keeps the limit for the query
	 */
	protected int limit;
	/**
	 * Keeps the offset for the query
	 */
	protected int offset;

	@Override
	public void addLimit(int limit) {
		this.limit = limit;

	}

	@Override
	public void addOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Adds the current limit and offset parameters into the parameters
	 * key-value map
	 * 
	 * @param parameters
	 *            The parameters map to add limit and offset information
	 */
	protected void addLimits(Map<String, String> parameters) {
		if (limit > 0) {
			parameters.put(LIMIT_PARAMETER_TAG, Long.toString(limit));
		}
		if (offset > 0) {
			parameters.put(OFFSET_PARAMETER_TAG, Long.toString(offset));
		}
	}

}
