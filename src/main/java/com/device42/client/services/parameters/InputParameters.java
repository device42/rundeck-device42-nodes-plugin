package com.device42.client.services.parameters;

import java.util.Map;

/**
 * Indicates that implementation will provide the key-value parameters map for
 * the REST API request
 */
public interface InputParameters {
	/**
	 * Get the key-value parameters map for the REST API request
	 * 
	 * @return key-value parameter map
	 */
	Map<String, String> parametersMap();
}
