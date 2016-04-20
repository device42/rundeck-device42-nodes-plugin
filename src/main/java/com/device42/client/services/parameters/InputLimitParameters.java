package com.device42.client.services.parameters;

public interface InputLimitParameters extends InputParameters{
	String LIMIT_PARAMETER_TAG = "limit";
	String OFFSET_PARAMETER_TAG = "offset";
	void addLimit(int limit);
	void addOffset(int offset);
}
