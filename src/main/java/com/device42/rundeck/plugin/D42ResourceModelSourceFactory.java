
package com.device42.rundeck.plugin;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.*;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceFactory;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * Generated the ResourceModelSource entity after setting up the parameters inside the configuration
 * @author yunusdawji
 *
 */
@Plugin(name = "d42", service = "ResourceModelSource")
public class D42ResourceModelSourceFactory implements ResourceModelSourceFactory, Describable {
	/**
	 * Name of the provider to store the scope
	 */
	public static final String PROVIDER_NAME = "d42";

	/**
	 * Device42 URL tag inside inside the module properties
	 */
	public static final String SERVER_URL = "serverUrl";
	/**
	 * Device42 username tag inside the module properties
	 */
	public static final String USERNAME = "username";
	/**
	 * Device42 password tag inside the module properties
	 */
	public static final String PASSWORD = "password";
	/**
	 * Device42 generic filter tag inside the module parameters. The data under the tag
	 * contains the string in the form "key=value&key=value" etc.
	 */
	public static final String FILTER_PARAMS = "filter";
	
	/**
	 * The tag for the amount of seconds until the cached data will be marked as not actual and the node list 
	 * refresh will be called
	 */
	public static final String REFRESH_INTERVAL = "refreshInterval";

	/**
	 * The amount of filter key-value groups for user-friendly interface
	 */
	public static final int GROUPS_AMOUNT = 5;
	
	/**
	 * The prefix for the key of the filter key-value pair
	 */
	public static final String FILTER_KEY_PREFIX = "filterKey";
	/**
	 * The prefix for the value of the filter key-value pair
	 */
	public static final String FILTER_VALUE_PREFIX = "filterValue";
	/**
	 * The prefix for the name of the filter group (each pair is in separate group)
	 */
	public static final String FILTER_GROUP_PREFIX = "Filter ";
	
	/**
	 * The default keys for the selection of the filter
	 */
	public static final String[] FILTER_KEYS = { "tags", "os", "service_level", "customer" };
	
	
	@Override
	public ResourceModelSource createResourceModelSource(Properties properties) throws ConfigurationException {
		final D42ResourceModelSource d42ResourceModelSource = new D42ResourceModelSource(properties);
		d42ResourceModelSource.validate();
		return d42ResourceModelSource;
	}

	
	@Override
	public Description getDescription() {
		DescriptionBuilder descBuilder = DescriptionBuilder.builder()
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
								Collections.singletonMap("displayType",
										(Object) StringRenderingConstants.DisplayType.PASSWORD)))
				.property(PropertyUtil.integer(REFRESH_INTERVAL, "Refresh Interval",
						"Minimum time in seconds between API requests to Device42 (default is 30)", false, "30",
						new PropertyValidator() {
							public boolean isValid(final String value) throws ValidationException {
								try {
									if (null == value || value.length() == 0)
										return true;
									int num = Integer.parseInt(value);
									if (num <= 0)
										throw new ValidationException(D42ResourceModelSourceFactory.REFRESH_INTERVAL
												+ " value is not valid: " + value);
								} catch (NumberFormatException e) {
									throw new ValidationException(D42ResourceModelSourceFactory.REFRESH_INTERVAL
											+ " value is not valid: " + value);
								}
								return true;
							}
						}));
		// Add the filter key-values groups to the configuration dialog
		for (int i = 1; i <= GROUPS_AMOUNT; i++) {
			addFilterGroup(descBuilder, i);
		}
		// Add the filter parameters to the configuration dialog
		descBuilder.property(PropertyUtil.string(FILTER_PARAMS, "Filter Params", "D42 filter params", false, null, null,
				null, getGroupRenderingOptions("Filter String")));

		return descBuilder.build();
	}
	
	/**
	 * Get the rendering options map to put the property into the group with the defined name
	 * @param groupName the name of the group
	 * @return The rendering options map that can be used to add the property to the group
	 */
	private Map<String, Object> getGroupRenderingOptions(String groupName) {
		Map<String, Object> renderingOptions = new HashMap<String, Object>();
		renderingOptions.put("groupName", groupName);
		renderingOptions.put("grouping", "secondary");
		return renderingOptions;
	}

	/**
	 * Add two properties inside the same group (key-value filter properties)
	 * @param descBuilder The buielder to add the properties to
	 * @param index the index id of the group / properties names
	 */
	private void addFilterGroup(DescriptionBuilder descBuilder, int index) {
		String groupName = FILTER_GROUP_PREFIX + index;
		Map<String, Object> renderingOptions = getGroupRenderingOptions(groupName);
		descBuilder.property(PropertyUtil.freeSelect(FILTER_KEY_PREFIX + index,
				"Filter Key", "Please select the key for the filtering from a list or enter your key id", false, null,
				Arrays.asList(FILTER_KEYS), null, null, renderingOptions));
		descBuilder.property(PropertyUtil.string(FILTER_VALUE_PREFIX + index, "Filter Value",
				"Please enter the value you want to filter for", false, null, null, null, renderingOptions));

	}
}
