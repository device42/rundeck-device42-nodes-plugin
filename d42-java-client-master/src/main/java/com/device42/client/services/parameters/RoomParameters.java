package com.device42.client.services.parameters;

import java.util.HashMap;
import java.util.Map;

public class RoomParameters implements InputParameters {
    public static class RoomParametersBuilder {
        private long buildingId;

        public RoomParametersBuilder() {}

        public RoomParametersBuilder buildingId(long buildingId) {
            this.buildingId = buildingId;
            return this;
        }

        public RoomParameters build() {
            return new RoomParameters(buildingId);
        }
    }

    private long buildingId;

    private RoomParameters(long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public Map<String, String> parametersMap() {
        Map<String, String> parameters = new HashMap<>();
        if (buildingId > 0) {
            parameters.put("building_id", Long.toString(buildingId));
        }
        return parameters;
    }
}
