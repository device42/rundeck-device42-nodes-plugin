package com.device42.client.services.parameters;

import java.util.HashMap;
import java.util.Map;

public class ServiceParameters implements InputParameters {
    public static class ServiceParametersBuilder {
        private long id;

        public ServiceParametersBuilder() {}

        public ServiceParametersBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ServiceParameters build() {
            return new ServiceParameters(id);
        }
    }

    private long id;

    private ServiceParameters(long id) {
        this.id = id;
    }

    @Override
    public Map<String, String> parametersMap() {
        Map<String, String> parameters = new HashMap<>();
        if (id > 0) {
            parameters.put("service_id", Long.toString(id));
        }
        return parameters;
    }
}
