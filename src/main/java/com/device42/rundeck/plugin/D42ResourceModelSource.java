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

/**
 * This class allows to generate the INodeSet of the noted linked to the
 * Device42 instance according to filter criteria
 * 
 * @author yunusdawji
 *
 */
public class D42ResourceModelSource implements ResourceModelSource {
	/**
	 * Logger to log the error information to the console log of Rundesk
	 * application
	 */
	static Logger logger = Logger.getLogger(D42ResourceModelSource.class);
	/**
	 * The url of the Device42 server
	 */
	String apiUrl;
	/**
	 * The username that will be used to authenticate at Device42 server
	 */
	private String username;
	/**
	 * The password that will be used to authenticate at Device42 server
	 */
	private String password;

	/**
	 * The lifetime of the cache. If the time after the last scan exceeds this
	 * period the rescan is being ordered. In milliseconds.
	 */
	long refreshInterval = 0;
	/**
	 * The last time when the devices were collected. The linux time
	 * representation.
	 */
	long lastRefresh = 0;

	/**
	 * The variable to keep the nodeset on the asynchronous scan
	 */
	Future<INodeSet> futureResult = null;
	/**
	 * The actual noteset that will be returned on request
	 */
	INodeSet iNodeSet;
	/**
	 * The mapper between Device42 Devices and NodeSet implementations
	 */
	DeviceToNodeMapper mapper;

	/**
	 * Constructs the objects according to the properties.
	 * 
	 * @param configuration
	 *            The properties for configuration. Should contain server URL,
	 *            username and password as the requred parameters. Optionally
	 *            can contain the cache refresh interval and filtering
	 *            parameters
	 */
	public D42ResourceModelSource(final Properties configuration) {
		this.username = configuration.getProperty(D42ResourceModelSourceFactory.USERNAME);
		this.password = configuration.getProperty(D42ResourceModelSourceFactory.PASSWORD);

		this.apiUrl = configuration.getProperty(D42ResourceModelSourceFactory.SERVER_URL);

		int refreshSecs = 0;
		final String refreshStr = configuration.getProperty(D42ResourceModelSourceFactory.REFRESH_INTERVAL);
		// Apply the refresh interval if exists. If not - we are getting default
		// value of 0 seconds (cache is off)
		if (StringUtils.isNotBlank(refreshStr)) {
			try {
				refreshSecs = Integer.parseInt(refreshStr);
			} catch (NumberFormatException e) {
				logger.warn(D42ResourceModelSourceFactory.REFRESH_INTERVAL + " value is not valid: " + refreshStr);
			}
		}
		refreshInterval = refreshSecs * 1000;

		initialize(configuration);
	}

	/**
	 * Scan the parameters for the filter entries.
	 * 
	 * @param configuration
	 *            The properties that contain the filter entries. @see
	 *            D42ResourceModelSourceFactory for details
	 */
	private void initialize(final Properties configuration) {
		String filterParams = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_PARAMS);
		final HashMap<String, String> params = new HashMap<String, String>();
		// Scan for human readable key value parameters combined in groups
		for (int i = 1; i <= D42ResourceModelSourceFactory.GROUPS_AMOUNT; i++) {
			String filterKey = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_KEY_PREFIX + i);
			String filterValue = configuration.getProperty(D42ResourceModelSourceFactory.FILTER_VALUE_PREFIX + i);
			if (StringUtils.isNotBlank(filterKey) && StringUtils.isNotBlank(filterValue)) {
				params.put(filterKey.trim(), filterValue.trim());

			}
		}

		// Scan for default filter entry in the form of key=value&key=value
		if (null != filterParams && filterParams.length() > 0) {
			String[] list = filterParams.split("&");
			for (String s : list) {
				String[] pair = s.split("=");
				if (pair.length == 2)
					params.put(pair[0].trim(), pair[1].trim());
			}
		}
		mapper = new DeviceToNodeMapper(username, password, apiUrl);
		mapper.setFilterParams(params);
	}

	public synchronized INodeSet getNodes() throws ResourceModelSourceException {
		// collect the data if the async operation was completed
		checkFuture();
		if (!needsRefresh()) {
			if (null != iNodeSet) {
				logger.info("Returning " + iNodeSet.getNodeNames().size() + " cached nodes from Device42");
			}
			return iNodeSet;
		}
		if (lastRefresh > 0 && null == futureResult && refreshInterval > 0) {
			futureResult = mapper.performQueryAsync();
			lastRefresh = System.currentTimeMillis();
		} else if (lastRefresh < 1 || refreshInterval <= 0) {
			// always perform synchronous query the first time or when cache is off
			iNodeSet = mapper.performQuery();
			lastRefresh = System.currentTimeMillis();
		}
		if (null != iNodeSet) {
			logger.info("Read " + iNodeSet.getNodeNames().size() + " nodes from Device42");
		}
		return iNodeSet;
	}

	/**
	 * if any future results are pending, check if they are done and retrieve
	 * the results
	 */
	private void checkFuture() {
		if (null != futureResult && futureResult.isDone()) {
			try {
				iNodeSet = futureResult.get();
			} catch (InterruptedException e) {
				logger.debug("Interrupted", e);
			} catch (ExecutionException e) {
				logger.warn("Error performing query: " + e.getMessage(), e);
			}
			futureResult = null;
		}
	}

	/**
	 * Returns true if the last refresh time was longer ago than the refresh
	 * interval
	 */
	private boolean needsRefresh() {
		return refreshInterval <= 0 || (System.currentTimeMillis() - lastRefresh > refreshInterval);
	}

	/**
	 * Check if the Resource Model object contains necessary parameters
	 * 
	 * @throws ConfigurationException
	 *             when the server url, username or password were not provided
	 */
	public void validate() throws ConfigurationException {
		if (null == username || null == password || null == apiUrl) {
			throw new ConfigurationException("You should set up server url and credentials");
		}
	}
}
