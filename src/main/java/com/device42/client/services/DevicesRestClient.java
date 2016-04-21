package com.device42.client.services;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import com.device42.client.model.Device;
import com.device42.client.parser.BasicDevicesJsonParser;
import com.device42.client.services.parameters.DeviceParameters;

/**
 * The REST API Client to collect the devices information
 */
public class DevicesRestClient extends AbstractAsynchronousRestClient {

	/**
	 * The parser to get the information from the JSON Object result of all
	 * devices query
	 */
	private BasicDevicesJsonParser devicesJsonParser = new BasicDevicesJsonParser();
	/**
	 * All devices REST endpoint
	 */
	private static final String DEVICE_ALL_PATH = "/api/1.0/devices/all/";

	/**
	 * Create new Devices REST API client according to specified URL and
	 * credentials
	 * 
	 * @param baseUrl
	 *            The ULS of Device42 application
	 * @param username
	 *            Authentication username
	 * @param password
	 *            Authentication password
	 * @return REST API client to collect the devices information
	 * @throws URISyntaxException
	 *             when the URL is malformed
	 */
	public static DevicesRestClient createDeviceClient(String baseUrl, String username, String password)
			throws URISyntaxException {
		return new DevicesRestClient(baseUrl, createHttpClient(username, password));
	}

	/**
	 * Internally create the REST API Client from the static factory method
	 * 
	 * @param baseUrl
	 *            URL of the endpoint
	 * @param httpClient
	 *            HTTP client with the credentials information
	 * @throws URISyntaxException
	 */
	private DevicesRestClient(String baseUrl, CloseableHttpClient httpClient) throws URISyntaxException {
		super(baseUrl, httpClient);
	}

	/**
	 * Collect all the devices information disregarding the limit settings set
	 * up on the Device42 Application
	 * 
	 * @param deviceParameters
	 *            The parameters of the query including the columns and filter
	 *            parameters
	 * @return List of the collected devices
	 */
	public List<Device> getAllDevices(DeviceParameters deviceParameters) {
		return getAll(DEVICE_ALL_PATH, devicesJsonParser, deviceParameters);
	}

}
