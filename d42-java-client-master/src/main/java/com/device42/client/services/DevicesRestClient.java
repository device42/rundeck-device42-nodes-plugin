package com.device42.client.services;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import com.device42.client.model.Device;
import com.device42.client.parser.BasicDeviceJsonParser;
import com.device42.client.parser.BasicDevicesJsonParser;
import com.device42.client.services.parameters.DeviceParameters;

public class DevicesRestClient extends AbstractAsynchronousRestClient {
    private BasicDeviceJsonParser deviceJsonParser = new BasicDeviceJsonParser();
    private BasicDevicesJsonParser devicesJsonParser = new BasicDevicesJsonParser();

    DevicesRestClient(String baseUrl, CloseableHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public Device getDeviceById(long deviceId) {
        return get(String.format("/api/1.0/devices/id/%d/", deviceId), deviceJsonParser);
    }

    public Device getDeviceByName(String deviceName) {
        return get(String.format("/api/1.0/devices/name/%s/", deviceName), deviceJsonParser);
    }

    public List<Device> getDevices(DeviceParameters deviceParameters) {
        return get("/api/1.0/devices/all/", devicesJsonParser, deviceParameters);
    }
}
