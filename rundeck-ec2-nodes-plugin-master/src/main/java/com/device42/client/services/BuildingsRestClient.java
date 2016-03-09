package com.device42.client.services;

import com.device42.client.model.Building;
import com.device42.client.parser.BasicBuildingsJsonParser;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

public class BuildingsRestClient extends AbstractAsynchronousRestClient {
    private BasicBuildingsJsonParser buildingsJsonParser = new BasicBuildingsJsonParser();

    BuildingsRestClient(String baseUrl, CloseableHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public List<Building> getBuildings() {
        List<Building> buildings = get("/api/1.0/buildings/", buildingsJsonParser);
        return buildings;
    }
}
