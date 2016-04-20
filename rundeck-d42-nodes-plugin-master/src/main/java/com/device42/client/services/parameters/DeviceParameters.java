package com.device42.client.services.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.device42.client.util.Device42ClientException;

public class DeviceParameters extends AbstractInputLimitParameters {
	public static class DeviceParametersBuilder {
		private static final String[] DEFAULT_COLUMNS = {
				"uuid", "service_level", "rack", "device_id", "name", "asset_no",
				"type", "manufacturer", "serial_no", "virtual_host_name", "hw_model",
				"customer", "type", "in_service", "room", "building", "os", "osver", "ip_addresses", "tags"
		};

		private List<String> columns;
		private Map<String, String> parameters;
		private int limit;
		private int offset;

		public DeviceParametersBuilder() {
			this.columns = Arrays.asList(DEFAULT_COLUMNS);
			this.parameters = new HashMap<String, String>();
		}

		public DeviceParametersBuilder(List<String> columns) {
			if (columns == null || columns.isEmpty()) {
				throw new Device42ClientException("List device's columns must not be null or empty.");
			}
			this.columns = columns;
			this.parameters = new HashMap<String, String>();
		}

		public DeviceParameters build() {
			return new DeviceParameters(columns, parameters, limit, offset);
		}

		public DeviceParametersBuilder limit(int limit) {
			this.limit = limit;
			return this;
		}

		public DeviceParametersBuilder offset(int offset) {
			this.offset = offset;
			return this;
		}

		public DeviceParametersBuilder parameter(String parameter, String value) {
			this.parameters.put(parameter, value);
			return this;
		}
		public DeviceParametersBuilder parameterAll(Map<String, String> para) {
            this.parameters.putAll(para);
            return this;
        }
	}

	private List<String> columns;
	private Map<String, String> parameters;

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
