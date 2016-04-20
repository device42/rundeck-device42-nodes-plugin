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
 * Created by yunusdawji on 2016-02-22.
 */
public class DeviceToNodeMapper {
	
	static final Logger logger = Logger.getLogger(DeviceToNodeMapper.class);
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private HashMap<String, String> filterParams;

	private String username;
	private String password;
	private String apiUrl;

	/**
	 * /** Create with the credentials definition
	 */
	DeviceToNodeMapper(String username, String password, String apiUrl) {
		this.username = username;
		this.password = password;
		this.apiUrl = apiUrl;
	}

	private INodeSet transformToNodeSet(List<Device> devices) {
		final NodeSetImpl nodeSet = new NodeSetImpl();
		mapInstances(nodeSet, devices);
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

			public INodeSet get(final long l, final TimeUnit timeUnit) throws InterruptedException, ExecutionException,
					TimeoutException {
				return get();
			}
		};
	}

	private List<Device> query() {
		DevicesRestClient client;
		try {
			client = Device42ClientFactory.createDeviceClient(apiUrl, username, password);
			logger.warn(filterParams.size() + "\n");
			return client.getAllDevices(new DeviceParameters.DeviceParametersBuilder().parameterAll(filterParams).build());
		} catch (URISyntaxException e) {
			logger.error("URL is malformed", e);
			return null;
		}
		
	}

	private void mapInstances(final NodeSetImpl nodeSet, final List<Device> instances) {
		for (Device inst : instances) {
			final INodeEntry iNodeEntry;
			try {
				iNodeEntry = instanceToNode(inst);
				if (null != iNodeEntry) {
					nodeSet.putNode(iNodeEntry);
				}
			} catch (Exception e) {
				logger.error("Exception on device mapping", e);
			}
		}
	}

	/**
	 * Convert an Device Instance to a RunDeck INodeEntry
	 * input
	 */

	INodeEntry instanceToNode(final Device device) {
		final NodeEntryImpl node = new NodeEntryImpl();

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
	
	private void setNodeAttribute(NodeEntryImpl node, String key, String value) {
		
		if (StringUtils.isNotBlank(value)) {
			String groupKey = D42ResourceModelSourceFactory.PROVIDER_NAME + ":" + key;
			node.setAttribute(groupKey, value);
		}
	}

	/**
	 * Return the list of "filter=value" filters
	 */
	public HashMap<String, String> getFilterParams() {
		return filterParams;
	}

	/**
	 * Set the list of "filter=value" filters
	 */
	public void setFilterParams(final HashMap<String, String> filterParams) {

		this.filterParams = filterParams;
	}

}
