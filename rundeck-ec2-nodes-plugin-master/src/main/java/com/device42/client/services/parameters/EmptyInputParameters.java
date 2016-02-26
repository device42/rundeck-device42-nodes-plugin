package com.device42.client.services.parameters;

import java.util.Collections;
import java.util.Map;

public class EmptyInputParameters implements InputParameters {
    public EmptyInputParameters() {}

    @Override
    public Map<String, String> parametersMap() {
        return Collections.<String, String>emptyMap();
    }
}
