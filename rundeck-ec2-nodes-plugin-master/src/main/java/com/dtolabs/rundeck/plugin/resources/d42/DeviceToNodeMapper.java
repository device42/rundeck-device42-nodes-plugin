package com.dtolabs.rundeck.plugin.resources.d42;

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
import org.identityconnectors.common.security.GuardedString;

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


    private String decryptToString(GuardedString string) {
        final StringBuilder buf = new StringBuilder();
        string.access(
                new GuardedString.Accessor() {
                    public void access(char [] chars) {
                        buf.append(chars);
                    }
                });
        return buf.toString();
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
    @SuppressWarnings("unchecked")
    static INodeEntry instanceToNode(final Device device, final Properties mapping) throws GeneratorException {
        final NodeEntryImpl node = new NodeEntryImpl();

        //evaluate single settings.selector=tags/* mapping
        /*
         *
         * if ("tags/*".equals(mapping.getProperty("attributes.selector"))) {
            //iterate through instance tags and generate settings
            for (final Tag tag : inst.getTags()) {
                if (null == node.getAttributes()) {
                    node.setAttributes(new HashMap<String, String>());
                }
                node.getAttributes().put(tag.getKey(), tag.getValue());
            }
        }

        if (null != mapping.getProperty("tags.selector")) {
            final String selector = mapping.getProperty("tags.selector");
            final String value = applySelector(inst, selector, mapping.getProperty("tags.default"), true);
            if (null != value) {
                final String[] values = value.split(",");
                final HashSet<String> tagset = new HashSet<String>();
                for (final String s : values) {
                    tagset.add(s.trim());
                }
                if (null == node.getTags()) {
                    node.setTags(tagset);
                } else {
                    final HashSet orig = new HashSet(node.getTags());
                    orig.addAll(tagset);
                    node.setTags(orig);
                }
            }
        }
        if (null == node.getTags()) {
            node.setTags(new HashSet());
        }
        final HashSet orig = new HashSet(node.getTags());
        //apply specific tag selectors
        final Pattern tagPat = Pattern.compile("^tag\\.(.+?)\\.selector$");
        //evaluate tag selectors
        for (final Object o : mapping.keySet()) {
            final String key = (String) o;
            final String selector = mapping.getProperty(key);
            //split selector by = if present
            final String[] selparts = selector.split("=");
            final Matcher m = tagPat.matcher(key);
            if (m.matches()) {
                final String tagName = m.group(1);
                if (null == node.getAttributes()) {
                    node.setAttributes(new HashMap<String, String>());
                }
                final String value = applySelector(inst, selparts[0], null);
                if (null != value) {
                    if (selparts.length > 1 && !value.equals(selparts[1])) {
                        continue;
                    }
                    //use add the tag if the value is not null
                    orig.add(tagName);
                }
            }
        }
        node.setTags(orig);
*/
        //apply default values which do not have corresponding selector
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
       /* final Pattern attribPat = Pattern.compile("^([^.]+?)\\.selector$");
        //evaluate selectors
        for (final Object o : mapping.keySet()) {
            final String key = (String) o;
            final String selector = mapping.getProperty(key);
            final Matcher m = attribPat.matcher(key);
            if (m.matches()) {
                final String attrName = m.group(1);
                if(attrName.equals("tags")){
                    //already handled
                    continue;
                }
                if (null == node.getAttributes()) {
                    node.setAttributes(new HashMap<String, String>());
                }
                final String value = applySelector(inst, selector, mapping.getProperty(attrName + ".default"));
                if (null != value) {
                    //use nodename-settingname to make the setting unique to the node
                    node.getAttributes().put(attrName, value);
                }
            }
        }
        */

        String hostSel = mapping.getProperty("hostname.selector");
        List<IP> devices = device.getIps();
        logger.warn(device.getName());
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

        // Set ssh port on hostname if not 22
        String sshport = node.getAttributes().get("sshport");
        if (sshport != null && !sshport.equals("") && !sshport.equals("22")) {
            node.setHostname(node.getHostname() + ":" + sshport);
        }
        return node;
    }

    /**
     * Return the result of the selector applied to the instance, otherwise return the defaultValue. The selector can be
     * a comma-separated list of selectors
     */
 /*   public static String applySelector(final Instance inst, final String selector, final String defaultValue) throws
            GeneratorException {
        return applySelector(inst, selector, defaultValue, false);
    }
*/
    /**
     * Return the result of the selector applied to the instance, otherwise return the defaultValue. The selector can be
     * a comma-separated list of selectors.
     * @param inst the instance
     * @param selector the selector string
     * @param defaultValue a default value to return if there is no result from the selector
     * @param tagMerge if true, allow | separator to merge multiple values
     */
 /*   public static String applySelector(final Instance inst, final String selector, final String defaultValue,
                                       final boolean tagMerge) throws
            GeneratorException {

        if (null != selector) {
            for (final String selPart : selector.split(",")) {
                if (tagMerge) {
                    final StringBuilder sb = new StringBuilder();
                    for (final String subPart : selPart.split(Pattern.quote("|"))) {
                        final String val = applySingleSelector(inst, subPart);
                        if (null != val) {
                            if (sb.length() > 0) {
                                sb.append(",");
                            }
                            sb.append(val);
                        }
                    }
                    if (sb.length() > 0) {
                        return sb.toString();
                    }
                } else {
                    final String val = applySingleSelector(inst, selPart);
                    if (null != val) {
                        return val;
                    }
                }
            }
        }
        return defaultValue;
    }

    private static String applySingleSelector(final Instance inst, final String selector) throws
            GeneratorException {
        if (null != selector && !"".equals(selector) && selector.startsWith("tags/")) {
            final String tag = selector.substring("tags/".length());
            final List<Tag> tags = inst.getTags();
            for (final Tag tag1 : tags) {
                if (tag.equals(tag1.getKey())) {
                    return tag1.getValue();
                }
            }
        } else if (null != selector && !"".equals(selector)) {
            try {
                final String value = BeanUtils.getProperty(inst, selector);
                if (null != value) {
                    return value;
                }
            } catch (Exception e) {
                throw new GeneratorException(e);
            }
        }

        return null;
    }
*/
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
