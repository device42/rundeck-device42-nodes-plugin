package com.device42.client.services.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.device42.client.util.Device42ClientException;

public class DeviceParameters implements InputParameters {
    public static class DeviceParametersBuilder {
        private static final String[] DEFAULT_COLUMNS = {
                "uuid", "service_level", "rack", "device_id", "name", "asset_no",
                "type", "manufacturer", "serial_no", "virtual_host_name", "hw_model"
        };

        private List<String> columns;
        private Map<String, String> parameters;
        private long limit;
        private long offset;

        public DeviceParametersBuilder() {
            this.columns = Arrays.asList(DEFAULT_COLUMNS);
            this.parameters = new HashMap<>();
        }

        public DeviceParametersBuilder(List<String> columns) {
            if (columns == null || columns.isEmpty()) {
                throw new Device42ClientException("List device's columns must not be null or empty.");
            }
            this.columns = columns;
            this.parameters = new HashMap<>();
        }

        public DeviceParameters build() {
            return new DeviceParameters(columns, parameters, limit, offset);
        }

        public DeviceParametersBuilder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public DeviceParametersBuilder offset(long offset) {
            this.offset = offset;
            return this;
        }

        public DeviceParametersBuilder parameter(String parameter, String value) {
            this.parameters.put(parameter, value);
            return this;
        }
    }

    private List<String> columns;
    private Map<String, String> parameters;
    private long limit;
    private long offset;

    private DeviceParameters(
            List<String> columns,
            Map<String, String> parameters,
            long limit,
            long offset) {
        this.columns = columns;
        this.parameters = parameters;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public Map<String, String> parametersMap() {
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put("include_cols", StringUtils.join(columns, ","));
        for (Map.Entry<String, String> parameterEntry : parameters.entrySet()) {
            parametersMap.put(parameterEntry.getKey(), parameterEntry.getValue());
        }
        if (limit > 0) {
            parametersMap.put("limit", Long.toString(limit));
        }
        if (offset > 0) {
            parametersMap.put("offset", Long.toString(offset));
        }
        return parametersMap;
    }
}
