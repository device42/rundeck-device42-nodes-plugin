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
* Created: 9/1/11 4:27 PM
* 
*/
package com.device42.rundeck.plugin;

import com.device42.client.model.Device;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.*;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceFactory;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Plugin(name = "d42", service = "ResourceModelSource")
public class D42ResourceModelSourceFactory implements ResourceModelSourceFactory, Describable {
    public static final String PROVIDER_NAME = "d42";

    public static final String ENDPOINT = "endpoint";
    public static final String FILTER_PARAMS = "filter";
    public static final String MAPPING_PARAMS = "mappingParams";
    public static final String RUNNING_ONLY = "runningOnly";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MAPPING_FILE = "mappingFile";
    public static final String REFRESH_INTERVAL = "refreshInterval";
    public static final String USE_DEFAULT_MAPPING = "useDefaultMapping";
    public static final String HTTP_PROXY_HOST = "httpProxyHost";
    public static final String HTTP_PROXY_PORT = "httpProxyPort";
    public static final String HTTP_PROXY_USER = "httpProxyUser";
    public static final String HTTP_PROXY_PASS = "httpProxyPass";
    public static final String SERVER_LIST = "listServer";
    public static final String SERVER_URL = "serverUrl";

    public static List<Device> list;

    public D42ResourceModelSourceFactory() {
       
        //DevicesRestClient client = Device42ClientFactory.createDeviceClient("https://svnow01.device42.com", "admin", "adm!nd42");
        //list = client.getDevices(new DeviceParameters.DeviceParametersBuilder().parameter("tags","rundeck").build());
    }

    public ResourceModelSource createResourceModelSource(final Properties properties) throws ConfigurationException {
        final D42ResourceModelSource d42ResourceModelSource = new D42ResourceModelSource(properties);
        d42ResourceModelSource.validate();
        return d42ResourceModelSource;
    }

    /*private static List<String> getDeviceNames(List<Device> deviceList){
        List<String> list = new ArrayList<String>();
        for(Device d : deviceList){
            list.add(d.getName());
        }
        return list;
    }*/

    public Description getDescription() {
        Description DESC = DescriptionBuilder.builder()
                .name(PROVIDER_NAME)
                .title("Rundeck Resources")
                .description("Devices from d42")
                .property(PropertyUtil.string(SERVER_URL, "API Url", "", false, null))
                .property(PropertyUtil.string(USERNAME, "Username", "D42 console username", false, null))
                .property(
                        PropertyUtil.string(
                                PASSWORD,
                                "Password",
                                "D42 console password",
                                false,
                                null,
                                null,
                                null,
                                Collections.singletonMap("displayType", (Object) StringRenderingConstants.DisplayType.PASSWORD)
                        )
                )
                //.property(PropertyUtil.integer(REFRESH_INTERVAL, "Refresh Interval",
                //        "Minimum time in seconds between API requests to AWS (default is 30)", false, "30"))
                .property(PropertyUtil.string(FILTER_PARAMS, "Filter Params", "D42 filter params", false, null))
                //.property(PropertyUtil.string(MAPPING_PARAMS, "Mapping Params",
                //        "Property mapping definitions. Specify multiple mappings in the form " +
                //                "\"attributeName.selector=selector\" or \"attributeName.default=value\", " +
                //                "separated by \";\"",
                //        false, null))
                .property(PropertyUtil.string(MAPPING_FILE, "Mapping File", "Property mapping File", false, null,
                        new PropertyValidator() {
                            public boolean isValid(final String s) throws ValidationException {
                                if (!new File(s).isFile()) {
                                    throw new ValidationException("File does not exist: " + s);
                                }
                                return true;
                            }
                        }))
                .property(PropertyUtil.bool(USE_DEFAULT_MAPPING, "Use Default Mapping",
                        "Start with default mapping definition. (Defaults will automatically be used if no others are " +
                                "defined.)",
                        false, "true"))
                .build();
        return DESC;
    }
}
