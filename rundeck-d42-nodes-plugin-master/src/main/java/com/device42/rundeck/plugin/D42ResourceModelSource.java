/*
 * Copyright 2011 DTO Solutions, Inc. (http://dtosolutions.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
* EC2ResourceModelSource.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 9/1/11 4:34 PM
* 
*/
package com.device42.rundeck.plugin;

import com.device42.client.model.Device;
import com.device42.client.model.IP;
import com.device42.client.services.Device42ClientFactory;
import com.device42.client.services.DevicesRestClient;
import com.device42.client.services.parameters.DeviceParameters;
import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.plugins.configuration.ConfigurationException;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class D42ResourceModelSource implements ResourceModelSource {
    static Logger logger = Logger.getLogger(D42ResourceModelSource.class);
    private String username;
    private String password;
    long refreshInterval = 30000;
    long lastRefresh = 0;
    String filterParams;
    String endpoint;
    String httpProxyHost;
    int httpProxyPort = 80;
    String httpProxyUser;
    String httpProxyPass;
    String mappingParams;
    File mappingFile;
    String serverName;
    String apiUrl;
    boolean useDefaultMapping = true;
    boolean runningOnly = true;
    boolean queryAsync = true;
    Future<INodeSet> futureResult = null;
    final Properties mapping = new Properties();

    INodeSet iNodeSet;
    static final Properties defaultMapping = new Properties();
    DeviceToNodeMapper mapper;
    public static List<Device> list;


    String mappingString = "nodename.selector=tags/Name,instanceId\n"
                           + "sshport.default=22\n"
                           + "sshport.selector=tags/ssh_config_Port\n"
                           + "description.default=Device42 Instance Node\n"
                           + "osArch.selector=architecture\n"
                           + "osFamily.selector=platform\n"
                           + "osFamily.default=unix\n"
                           + "osName.selector=platform\n"
                           + "osName.default=Linux\n"
                           + "username.selector=tags/Rundeck-User\n"
                           + "username.default=rundeck-user\n"
                           + "privateIpAddress.selector=privateIpAddress\n"
                           + "privateDnsName.selector=privateDnsName\n"
                           + "tags.selector=tags/Rundeck-Tags\n"
                           + "instanceId.selector=instanceId\n"
                           + "tag.running.selector=state.name=running\n"
                           + "tag.stopped.selector=state.name=stopped\n"
                           + "tag.stopping.selector=state.name=stopping\n"
                           + "tag.shutting-down.selector=state.name=shutting-down\n"
                           + "tag.terminated.selector=state.name=terminated\n"
                           + "tag.pending.selector=state.name=pending\n"
                           + "state.selector=state.name\n"
                           + "tags.default=d42\n";

    public D42ResourceModelSource(final Properties configuration) {
        this.username = configuration.getProperty(D42ResourceModelSourceFactory.USERNAME);
        this.password = configuration.getProperty(D42ResourceModelSourceFactory.PASSWORD);
        this.endpoint = configuration.getProperty(D42ResourceModelSourceFactory.ENDPOINT);
        this.apiUrl = configuration.getProperty(D42ResourceModelSourceFactory.SERVER_URL);

        this.filterParams = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_PARAMS);
        this.mappingParams = configuration.getProperty(D42ResourceModelSourceFactory.MAPPING_PARAMS);
        final String mappingFilePath = configuration.getProperty(D42ResourceModelSourceFactory.MAPPING_FILE);
        if (null != mappingFilePath) {
            mappingFile = new File(mappingFilePath);
        }
        int refreshSecs = 30;
        final String refreshStr = configuration.getProperty(D42ResourceModelSourceFactory.REFRESH_INTERVAL);
        if (null != refreshStr && !"".equals(refreshStr)) {
            try {
                refreshSecs = Integer.parseInt(refreshStr);
            } catch (NumberFormatException e) {
                logger.warn(D42ResourceModelSourceFactory.REFRESH_INTERVAL + " value is not valid: " + refreshStr);
            }
        }
        refreshInterval = refreshSecs * 1000;
        if (configuration.containsKey(D42ResourceModelSourceFactory.USE_DEFAULT_MAPPING)) {
            useDefaultMapping = Boolean.parseBoolean(configuration.getProperty(
                D42ResourceModelSourceFactory.USE_DEFAULT_MAPPING));
        }
        if (configuration.containsKey(D42ResourceModelSourceFactory.RUNNING_ONLY)) {
            runningOnly = Boolean.parseBoolean(configuration.getProperty(
                D42ResourceModelSourceFactory.RUNNING_ONLY));
        }

        //TODO:: remove it once everything is finallized - not needed anymore
        //if (null != username && null != password) {
        //    credentials = new BasicAWSCredentials(username.trim(), password.trim());
        //}
        
        initialize();
    }

    private void initialize() {
        final HashMap<String, String> params = new HashMap<String, String>();
        if (null != filterParams) {
            List<String> string = new ArrayList<String>();
            String[] list = filterParams.split("&");
            for (String s : list){
                String[] pair = s.split("=");
                params.put(pair[0],pair[1]);
            }
            string.add(filterParams);
        }
        loadMapping();
        mapper = new DeviceToNodeMapper(mapping, username, password, apiUrl);
        mapper.setFilterParams(params);
        mapper.setEndpoint(endpoint);
        mapper.setRunningStateOnly(runningOnly);

        DevicesRestClient client = Device42ClientFactory.createDeviceClient(apiUrl, username, password);
        list = client.getDevices(new DeviceParameters.DeviceParametersBuilder().parameterAll(params).parameter("include_cols","name,ip_addresses,device_id,tags").build());
    }

    public static List<IP> getIP(String device){
        List<IP> mlist = new ArrayList<IP>();
        for(Device d : list){
            if(d.getName().equalsIgnoreCase(device))
                mlist = d.getIps();
        }
        return mlist;
    }

    public synchronized INodeSet getNodes() throws ResourceModelSourceException {
        checkFuture();
        if (!needsRefresh()) {
            if (null != iNodeSet) {
                logger.info("Returning " + iNodeSet.getNodeNames().size() + " cached nodes from EC2");
            }
            return iNodeSet;
        }
        if (lastRefresh > 0 && queryAsync && null == futureResult) {
            futureResult = mapper.performQueryAsync();
            lastRefresh = System.currentTimeMillis();
        } else if (!queryAsync || lastRefresh < 1) {
            //always perform synchronous query the first time
            iNodeSet = mapper.performQuery();
            lastRefresh = System.currentTimeMillis();
        }
        if (null != iNodeSet) {
            logger.info("Read " + iNodeSet.getNodeNames().size() + " nodes from EC2");
        }
        return iNodeSet;
    }

    /**
     * if any future results are pending, check if they are done and retrieve the results
     */
    private void checkFuture() {
        if (null != futureResult && futureResult.isDone()) {
            try {
                iNodeSet = futureResult.get();
            } catch (InterruptedException e) {
                logger.debug(e);
            } catch (ExecutionException e) {
                logger.warn("Error performing query: " + e.getMessage(), e);
            }
            futureResult = null;
        }
    }

    /**
     * Returns true if the last refresh time was longer ago than the refresh interval
     */
    private boolean needsRefresh() {
        return refreshInterval < 0 || (System.currentTimeMillis() - lastRefresh > refreshInterval);
    }

    private void loadMapping() {
        if (useDefaultMapping) {
            try {
                final InputStream resourceAsStream = D42ResourceModelSource.class.getClassLoader().getResourceAsStream(
                        "defaultMapping.properties");
                if (null != resourceAsStream) {
                    try {
                        defaultMapping.load(resourceAsStream);
                    } finally {
                        resourceAsStream.close();
                    }
                }else{
                    mappingString = mappingString + "hostname.selector="+ D42ResourceModelSource.getIP(this.serverName).get(0).getIp() + "\n";
                    //fallback in case class loader is misbehaving
                    final StringReader stringReader = new StringReader(mappingString);
                    try {
                        defaultMapping.load(stringReader);
                    } finally {
                        stringReader.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            mapping.putAll(defaultMapping);
        }
        if (null != mappingFile) {
            try {
                final FileInputStream fileInputStream = new FileInputStream(mappingFile);
                try {
                    mapping.load(fileInputStream);
                } finally {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        if (null != mappingParams) {
            for (final String s : mappingParams.split(";")) {
                if (s.contains("=")) {
                    final String[] split = s.split("=", 2);
                    if (2 == split.length) {
                        mapping.put(split[0], split[1]);
                    }
                }
            }
        }
        if (mapping.size() < 1) {
            mapping.putAll(defaultMapping);
        }
    }

    public void validate() throws ConfigurationException {
        if (null != username && null == password && null == apiUrl) {
            throw new ConfigurationException("password is required for use with username");
        }
    }
}
