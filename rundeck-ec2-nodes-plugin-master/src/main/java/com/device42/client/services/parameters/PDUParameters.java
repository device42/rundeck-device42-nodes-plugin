package com.device42.client.services.parameters;

import java.util.HashMap;
import java.util.Map;

public class PDUParameters implements InputParameters {
    public static class PDUParametersBuilder {
        public PDUParametersBuilder() {}

        public PDUParameters build() {
            return new PDUParameters();
        }
    }

    private PDUParameters() {
    }

    @Override
    public Map<String, String> parametersMap() {
        Map<String, String> parameters = new HashMap<String, String>();
        return parameters;
    }
}
