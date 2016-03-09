package com.device42.client.services;

import com.device42.client.model.Rack;
import com.device42.client.parser.BasicRacksJsonParser;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Iterator;
import java.util.List;

public class RacksRestClient extends AbstractAsynchronousRestClient {
    private BasicRacksJsonParser racksJsonParser = new BasicRacksJsonParser();

    RacksRestClient(String baseUrl, CloseableHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public List<Rack> getRacks() {
        return get("/api/1.0/racks/", racksJsonParser);
    }

    public List<Rack> getRacksByRoom(String room) {
        List<Rack> racks = get("/api/1.0/racks/", racksJsonParser);
        Iterator<Rack> iter = racks.iterator();
        while (iter.hasNext()) {
            Rack rack = iter.next();
            if (!rack.getRoom().equals(room)) {
                iter.remove();
            }
        }
        return racks;
    }
}
