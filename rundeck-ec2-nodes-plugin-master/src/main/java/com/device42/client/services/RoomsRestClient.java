package com.device42.client.services;

import com.device42.client.model.Room;
import com.device42.client.parser.BasicRoomsJsonParser;
import com.device42.client.services.parameters.RoomParameters;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

public class RoomsRestClient extends AbstractAsynchronousRestClient {
    private BasicRoomsJsonParser roomsJsonParser = new BasicRoomsJsonParser();

    RoomsRestClient(String baseUrl, CloseableHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public List<Room> getRooms(RoomParameters roomParameters) {
        return get("/api/1.0/rooms/", roomsJsonParser, roomParameters);
    }
}
