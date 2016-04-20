package com.device42.client.services.parameters;

import java.util.Map;

public abstract class AbstractInputLimitParameters implements InputLimitParameters{

	protected int limit;
	protected int offset;

	@Override
	public void addLimit(int limit) {
		this.limit = limit;
		
	}

	@Override
	public void addOffset(int offset) {
		this.offset = offset;
	}
	
	protected void addLimits(Map<String, String> parameters) {
		if (limit > 0) {
			parameters.put(LIMIT_PARAMETER_TAG, Long.toString(limit));
        }
        if (offset > 0) {
        	parameters.put(OFFSET_PARAMETER_TAG, Long.toString(offset));
        }
	}
	

}
