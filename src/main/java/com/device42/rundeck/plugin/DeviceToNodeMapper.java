package com.device42.rundeck.plugin;

import com.device42.client.model.Device;
import com.device42.client.model.IP;
import com.device42.client.services.Device42ClientFactory;
import com.device42.client.services.DevicesRestClient;
import com.device42.client.services.parameters.DeviceParameters;
import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.common.NodeEntryImpl;
import com.dtolabs.rundeck.core.common.NodeSetImpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

/**
 * Class needed to convert the list of the devices to the set of the nodes.
 * Created by yunusdawji on 2016-02-22.
 */
public class DeviceToNodeMapper {
	/**
	 * Used to log information messages and warnings to the console
	 */
	static final Logger logger = Logger.getLogger(DeviceToNodeMapper.class);
	/**
	 * Executes the scan in the separate thread and returns the object, that
	 * will contain the results after the async process completed
	 */
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	/**
	 * The filter parameters for the Device42 instance in the form of key -
	 * value
	 */
	private HashMap<String, String> filterParams;

	/**
	 * The url of the Device42 server
	 */
	private String apiUrl;
	/**
	 * The username that will be used to authenticate at Device42 server
	 */
	private String username;
	/**
	 * The password that will be used to authenticate at Device42 server
	 */
	private String password;

	/**
	 * Create the instance the object with the Device42 instance parameters
	 * 
	 * @param username
	 *            username to authenticate in Device42 server
	 * @param password
	 *            password to authenticate in Device42 server
	 * @param apiUrl
	 *            The URL of Device42 instance. Should be valid URL
	 */
	DeviceToNodeMapper(String username, String password, String apiUrl) {
		this.username = username;
		this.password = password;
		this.apiUrl = apiUrl;
	}

	/**
	 * Convert the list of the devices to the INodeSet object
	 * 
	 * @param devices
	 *            The devices that were collected from Device42 instance
	 * @return The INodeSet containing information for the Rackdesk with the set
	 *         of the nodes
	 */
	private INodeSet transformToNodeSet(List<Device> devices) {
		final NodeSetImpl nodeSet = new NodeSetImpl();
		for (Device device : devices) {
			final INodeEntry iNodeEntry;
			try {
				iNodeEntry = deviceToNode(device);
				if (null != iNodeEntry) {
					nodeSet.putNode(iNodeEntry);
				}
			} catch (Exception e) {
				logger.error("Exception on device mapping", e);
			}
		}
		return nodeSet;
	}

	/**
	 * Perform the query and return the set of instances
	 *
	 */
	public INodeSet performQuery() {
		return transformToNodeSet(query());
	}

	/**
	 * Perform the query asynchronously and return the set of instances
	 *
	 */
	public Future<INodeSet> performQueryAsync() {

		final Future<List<Device>> describeInstancesRequest = executorService.submit(new Callable<List<Device>>() {
			public List<Device> call() throws Exception {
				return query();
			}
		});

		return new Future<INodeSet>() {

			public boolean cancel(boolean b) {
				return describeInstancesRequest.cancel(b);
			}

			public boolean isCancelled() {
				return describeInstancesRequest.isCancelled();
			}

			public boolean isDone() {
				return describeInstancesRequest.isDone();
			}

			public INodeSet get() throws InterruptedException, ExecutionException {
				return transformToNodeSet(describeInstancesRequest.get());
			}

			/**
			 * We ignore the timeout parameters here. The Device42 REST library
			 * does not support terminations and cancellations
			 */
			public INodeSet get(final long l, final TimeUnit timeUnit) throws InterruptedException, ExecutionException,
					TimeoutException {
				return get();
			}
		};
	}

	/**
	 * Collect the list of the devices from the Device REST API library
	 */
	private List<Device> query() {
		DevicesRestClient client;
		try {
			client = Device42ClientFactory.createDeviceClient(apiUrl, username, password);
			logger.warn(filterParams.size() + "\n");
			return client
					.getAllDevices(new DeviceParameters.DeviceParametersBuilder().parameterAll(filterParams).build());
		} catch (URISyntaxException e) {
			logger.error("URL is malformed", e);
			return null;
		}

	}

	/**
	 * Convert the Device from Device42 application into Node of Rundesk application
	 * @param device Device from Device42 application
	 * @return Node entry from Rundesk application
	 */
	INodeEntry deviceToNode(final Device device) {
		final NodeEntryImpl node = new NodeEntryImpl();

		// We are not using the ip mapping for now
		List<IP> ips = device.getIps();

		/*if (ips != null && ips.size() > 0) {
			for (IP ip : ips) {
				String host = ip.getIp();
				if (StringUtils.isNotBlank(host)) {
					node.setHostname(host);
					String name = device.getName();
					if (StringUtils.isBlank(name)) {
						name = host;
					}
					node.setNodename(name);
					break;
				}
			}

		} else {*/
			// no ip so lets use the node name instead
			node.setHostname(device.getName());
			node.setNodename(device.getName());
		//}

		node.setOsName(device.getOs());
		node.setOsVersion(device.getOsVer());
		node.setTags(new HashSet<String>(Arrays.asList(device.getTags())));

		setNodeAttribute(node, "customer", device.getCustomer());
		setNodeAttribute(node, "service_level", device.getServiceLevel());
		setNodeAttribute(node, "uuid", device.getUuid());
		setNodeAttribute(node, "rack", device.getRack());
		setNodeAttribute(node, "asset_no", device.getAssetNo());
		setNodeAttribute(node, "type", device.getType());
		setNodeAttribute(node, "manufacturer", device.getManufacturer());
		setNodeAttribute(node, "serial_no", device.getSerialNo());
		setNodeAttribute(node, "virtual_host_name", device.getVirtualHostName());
		setNodeAttribute(node, "hw_model", device.getHardwareModel());
		setNodeAttribute(node, "room", device.getRoom());
		setNodeAttribute(node, "building", device.getBuilding());

		// Set ssh port on hostname if not 22
		String sshport = node.getAttributes().get("sshport");
		if (sshport != null && !sshport.equals("") && !sshport.equals("22")) {
			node.setHostname(node.getHostname() + ":" + sshport);
		}
		return node;
	}
	
	/**
	 * Add the attribute to the node entry if key and value are properly set. 
	 * The attribute is being prefixed with the D42 prefix to identify
	 * Device42 attributes from the other Node attributes.
	 * @param node The node to add the attribute to
	 * @param key The key of the attribute. If empty - will be ignored 
	 * @param value The value of the attribute. If empty - will be ignored.
	 */
	private void setNodeAttribute(NodeEntryImpl node, String key, String value) {

		if (StringUtils.isNotBlank(value)) {
			String groupKey = D42ResourceModelSourceFactory.PROVIDER_NAME + ":" + key;
			node.setAttribute(groupKey, value);
		}
	}

	/**
	 * Return the map of "filter=value" filters
	 */
	public HashMap<String, String> getFilterParams() {
		return filterParams;
	}

	/**
	 * Set the map of "filter=value" filters
	 * @param filterParams the filtering map
	 */
	public void setFilterParams(final HashMap<String, String> filterParams) {

		this.filterParams = filterParams;
	}

}
