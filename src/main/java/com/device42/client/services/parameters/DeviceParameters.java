package com.device42.client.services.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.device42.client.util.Device42ClientException;

/**
 * The input parameters for the Device queries The detailed information about
 * the request parameters can be found at
 * http://api.device42.com/#get-all-devices-with-detailed-output-added-in-v6-3-4
 */
public class DeviceParameters extends AbstractInputLimitParameters {
	/**
	 * The builder for the input parameters for the Device queries
	 */
	public static class DeviceParametersBuilder {
		/**
		 * The default columns that should be added to the query request if no
		 * other columns specified
		 */
		private static final String[] DEFAULT_COLUMNS = {
				"uuid", "service_level", "rack", "device_id", "name", "asset_no",
				"type", "manufacturer", "serial_no", "virtual_host_name", "hw_model",
				"customer", "type", "in_service", "room", "building", "os", "osver", "ip_addresses", "tags"
		};

		/**
		 * The columns that should be requested from the Device42 application
		 * for each device
		 */
		private List<String> columns;
		/**
		 * Other key-value parameters like filter requests
		 */
		private Map<String, String> parameters;
		/**
		 * The maximum amount of data that should be returned by the request
		 */
		private int limit;
		/**
		 * The starting device number that the Device42 application should start
		 * collection from
		 */
		private int offset;

		/**
		 * Basic builder with the default column list
		 */
		public DeviceParametersBuilder() {
			this.columns = Arrays.asList(DEFAULT_COLUMNS);
			this.parameters = new HashMap<String, String>();
		}

		/**
		 * The builder with specified column list
		 * 
		 * @param columns
		 *            The list of the columns that would contain additional data
		 *            about the device inside the Device42 application.
		 */

		public DeviceParametersBuilder(List<String> columns) {
			if (columns == null || columns.isEmpty()) {
				throw new Device42ClientException("List device's columns must not be null or empty.");
			}
			this.columns = columns;
			this.parameters = new HashMap<String, String>();
		}

		/**
		 * Build the device parameters according to set of criterias
		 * 
		 * @return
		 */
		public DeviceParameters build() {
			return new DeviceParameters(columns, parameters, limit, offset);
		}

		/**
		 * Add the limit to the request
		 * 
		 * @param limit
		 *            The limit of the request. Maximum number of the devices
		 *            that can be returned by the REST API query
		 * @return The builder instance
		 */
		public DeviceParametersBuilder limit(int limit) {
			this.limit = limit;
			return this;
		}

		/**
		 * Add the offset to the request
		 * 
		 * @param offset
		 *            The offset of the request. The starting device number to
		 *            start the collection data from by the Device42 application
		 * @return The builder instance
		 */
		public DeviceParametersBuilder offset(int offset) {
			this.offset = offset;
			return this;
		}

		/**
		 * Add one specific parameter to the request
		 * 
		 * @param parameter
		 *            key of the parameter
		 * @param value
		 *            value of the parameter
		 * @return The builder instance
		 */
		public DeviceParametersBuilder parameter(String parameter, String value) {
			this.parameters.put(parameter, value);
			return this;
		}

		/**
		 * Add all parameters from the map into the request query
		 * 
		 * @param para
		 *            map containing the parameters
		 * @return The builder instance
		 */
		public DeviceParametersBuilder parameterAll(Map<String, String> para) {
			this.parameters.putAll(para);
			return this;
		}
	}

	/**
	 * The columns that should be requested from the Device42 application for
	 * each device
	 */
	private List<String> columns;
	/**
	 * Other key-value parameters that should be added to the query
	 */
	private Map<String, String> parameters;

	/**
	 * Constructor that can be accessed from the builder
	 * 
	 * @param columns
	 *            the column list
	 * @param parameters
	 *            the other parameters key-value map
	 * @param limit
	 *            the limit of the query. Maximum number of the devices that can
	 *            be returned
	 * @param offset
	 *            the offset of the query. The starting device number to start
	 *            the collection data
	 */
	private DeviceParameters(
			List<String> columns,
			Map<String, String> parameters,
			int limit,
			int offset) {
		this.columns = columns;
		this.parameters = parameters;
		addLimit(limit);
		addOffset(offset);
	}

	@Override
	public Map<String, String> parametersMap() {
		Map<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put("include_cols", StringUtils.join(columns, ","));
		for (Map.Entry<String, String> parameterEntry : parameters.entrySet()) {
			parametersMap.put(parameterEntry.getKey(), parameterEntry.getValue());
		}
		addLimits(parametersMap);
		return parametersMap;
	}
}
