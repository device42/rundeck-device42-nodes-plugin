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


import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.plugins.configuration.ConfigurationException;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class D42ResourceModelSource implements ResourceModelSource {
    static Logger logger = Logger.getLogger(D42ResourceModelSource.class);
    String apiUrl;
    private String username;
    private String password;
    String filterParams;
    long refreshInterval = 30000;
    long lastRefresh = 0;
    
    boolean queryAsync = true;
    Future<INodeSet> futureResult = null;
    INodeSet iNodeSet;
    DeviceToNodeMapper mapper;
    public D42ResourceModelSource(final Properties configuration) {
        this.username = configuration.getProperty(D42ResourceModelSourceFactory.USERNAME);
        this.password = configuration.getProperty(D42ResourceModelSourceFactory.PASSWORD);
        
        this.apiUrl = configuration.getProperty(D42ResourceModelSourceFactory.SERVER_URL);

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
        
        
        initialize(configuration);
    }

    private void initialize(final Properties configuration) {
    	this.filterParams = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_PARAMS);
        final HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 1; i <= D42ResourceModelSourceFactory.GROUPS_AMOUNT; i++) {
        	String filterKey = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_KEY_PREFIX + i);
        	String filterValue = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_VALUE_PREFIX + i);
        	if (StringUtils.isNotBlank(filterKey) && StringUtils.isNotBlank(filterValue)) {
        		params.put(filterKey.trim(), filterValue.trim());
        	}
        }
        if (null != filterParams && filterParams.length() > 0) {
            String[] list = filterParams.split("&");
            for (String s : list){
                String[] pair = s.split("=");
                if(pair.length == 2)
                	params.put(pair[0].trim(),pair[1].trim());
            }
        }
        mapper = new DeviceToNodeMapper(username, password, apiUrl);
        mapper.setFilterParams(params);
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

   

    public void validate() throws ConfigurationException {
        if (null == username || null == password || null == apiUrl) {
            throw new ConfigurationException("You should set up server credentials");
        }
    }
}
