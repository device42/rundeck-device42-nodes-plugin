package com.device42.client.model;

/**
 * Represent the IP instance that is coming from Device42 application.
 * 
 * Created on 2016-02-21.
 * 
 * @author yunusdawji
 */
public class IP {

	/**
	 * IP address as a string
	 */
	private String ip;
	/**
	 * Label for IP address
	 */
	private String label;
	/**
	 * Mac address that is linked to that IP address
	 */
	private String macaddress;
	/**
	 * The subnet IP address belongs to
	 */
	private String subnet;
	/**
	 * IP address type
	 */
	private String type;

	/**
	 * Get the IP address as a string
	 * 
	 * @return IP address as a string
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Set the IP address
	 * 
	 * @param ip
	 *            IP address as a string
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Get the label for IP address
	 * 
	 * @return Label for IP address
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label for IP address
	 * 
	 * @param label
	 *            Label for IP address
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get the mac address that is linked to that IP address
	 * 
	 * @return Mac address that is linked to that IP address
	 */
	public String getMacaddress() {
		return macaddress;
	}

	/**
	 * Set the mac address that is linked to that IP address
	 * 
	 * @param macaddress
	 *            Mac address that is linked to that IP address
	 */
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	/**
	 * Get the subnet IP address belongs to
	 * 
	 * @return The subnet IP address belongs to
	 */
	public String getSubnet() {
		return subnet;
	}

	/**
	 * Set the subnet IP address belongs to
	 * 
	 * @param subnet
	 *            The subnet IP address belongs to
	 */
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	/**
	 * Get IP address type
	 * 
	 * @return IP address type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set IP address type
	 * 
	 * @param type
	 *            IP address type
	 */
	public void setType(String type) {
		this.type = type;
	}

}
