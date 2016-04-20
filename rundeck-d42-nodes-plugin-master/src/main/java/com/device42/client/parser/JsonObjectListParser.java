package com.device42.client.parser;

import java.util.List;

public interface JsonObjectListParser<T> extends JsonObjectParser<List<T>> {
	String LIMIT_TAG = "limit";
	String TOTAL_COUNT_TAG = "total_count";

	int getCount();

	int getLimit();
}
