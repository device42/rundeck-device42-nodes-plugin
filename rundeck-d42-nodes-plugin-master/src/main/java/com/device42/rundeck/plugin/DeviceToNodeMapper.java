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
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yunusdawji on 2016-02-22.
 */
public class DeviceToNodeMapper {

    static final Logger logger = Logger.getLogger(DeviceToNodeMapper.class);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private HashMap<String,String> filterParams;
    private String endpoint;
    private boolean runningStateOnly = true;
    private Properties mapping;
    private String username;
    private String  password;
    private String apiUrl;

    /**
     /**
     * Create with the credentials and mapping definition
     */
    DeviceToNodeMapper(final Properties mapping, String username, String password, String apiUrl) {
        this.mapping = mapping;
        this.username = username;
        this.password = password;
        this.apiUrl = apiUrl;
    }

    /**
     * Perform the query and return the set of instances
     *
     */
    public INodeSet performQuery() {
        final NodeSetImpl nodeSet = new NodeSetImpl();
        final ArrayList<Device> instances = (ArrayList<Device>) query();
        mapInstances(nodeSet, instances);
        return nodeSet;
    }



    /**
     * Perform the query asynchronously and return the set of instances
     *
     */
    public Future<INodeSet> performQueryAsync() {

        final Future<List<Device>> describeInstancesRequest = executorService.submit(new Callable<List<Device>>() {
            public List<Device> call() throws Exception {
                return query();
            }});

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
                final NodeSetImpl nodeSet = new NodeSetImpl();
                final ArrayList<Device> instances = (ArrayList<Device>) describeInstancesRequest.get();
                mapInstances(nodeSet, instances);
                return nodeSet;
            }

            public INodeSet get(final long l, final TimeUnit timeUnit) throws InterruptedException, ExecutionException,
                    TimeoutException {
                final NodeSetImpl nodeSet = new NodeSetImpl();
                final ArrayList<Device> instances = (ArrayList<Device>) describeInstancesRequest.get();
                mapInstances(nodeSet, instances);
                return nodeSet;
            }
        };
    }

    private List<Device> query() {
        DevicesRestClient client = Device42ClientFactory.createDeviceClient(apiUrl, username,password);
        logger.warn(filterParams.size()+"\n");
        return client.getDevices(new DeviceParameters.DeviceParametersBuilder().parameterAll(filterParams).parameter("include_cols","name,ip_addresses,device_id,tags").build());
    }

    private void mapInstances(final NodeSetImpl nodeSet, final List<Device> instances) {
        for (Device inst : instances) {
            final INodeEntry iNodeEntry;
            try {
                iNodeEntry = DeviceToNodeMapper.instanceToNode(inst, mapping);
                if (null != iNodeEntry) {
                    nodeSet.putNode(iNodeEntry);
                }
            } catch (GeneratorException e) {
                logger.error(e);
            }
        }
    }

    /**
     * Convert an AWS EC2 Instance to a RunDeck INodeEntry based on the mapping input
     */
    
    static INodeEntry instanceToNode(final Device device, final Properties mapping) throws GeneratorException {
        final NodeEntryImpl node = new NodeEntryImpl();
      final Pattern attribDefPat = Pattern.compile("^([^.]+?)\\.default$");
        //evaluate selectors
        for (final Object o : mapping.keySet()) {
            final String key = (String) o;
            final String value = mapping.getProperty(key);
            final Matcher m = attribDefPat.matcher(key);
            if (m.matches() && (!mapping.containsKey(key + ".selector") || "".equals(mapping.getProperty(
                    key + ".selector")))) {
                final String attrName = m.group(1);
                if (null == node.getAttributes()) {
                    node.setAttributes(new HashMap<String, String>());
                }
                if (null != value) {
                    node.getAttributes().put(attrName, value);
                }
            }
        }
        List<IP> devices = device.getIps();
        logger.warn(device.getName());
        node.setNodename(device.getName());
        
        try {
            String host = devices.get(0).getIp();
            node.setHostname(host);
            if (null == node.getHostname()) {
                System.err.println("Unable to determine hostname for instance: " + device.getName());
                return null;
            }
            String name = node.getNodename();
            if (null == name || "".equals(name)) {
                name = node.getHostname();
            }
            if (null == name || "".equals(name)) {
                name = device.getName();
            }
            node.setNodename(name);
        } catch(IndexOutOfBoundsException ex) {
            //no ip so lets use the node name instead
            node.setHostname(device.getName());
            node.setNodename(device.getName());
        }

        // Set ssh port on hostname if not 22
        String sshport = node.getAttributes().get("sshport");
        if (sshport != null && !sshport.equals("") && !sshport.equals("22")) {
            node.setHostname(node.getHostname() + ":" + sshport);
        }
        return node;
    }

 
    /**
     * Return the list of "filter=value" filters
     */
    public HashMap<String, String> getFilterParams() {
        return filterParams;
    }

    /**
     * Return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Return true if runningStateOnly
     */
    public boolean isRunningStateOnly() {
        return runningStateOnly;
    }

    /**
     * If true, the an automatic "running" state filter will be applied
     */
    public void setRunningStateOnly(final boolean runningStateOnly) {
        this.runningStateOnly = runningStateOnly;
    }

    /**
     * Set the list of "filter=value" filters
     */
    public void setFilterParams(final HashMap<String, String> filterParams) {

        this.filterParams = filterParams;
    }

    /**
     * Set the region endpoint to use.
     */
    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public Properties getMapping() {
        return mapping;
    }

    public void setMapping(Properties mapping) {
        this.mapping = mapping;
    }

    public static class GeneratorException extends Exception {
        /**
		 * generated
		 */
		private static final long serialVersionUID = 8308233781272424538L;

		public GeneratorException() {
        }

        public GeneratorException(final String message) {
            super(message);
        }

        public GeneratorException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public GeneratorException(final Throwable cause) {
            super(cause);
        }
    }
}
