package com.device42.client.model;

import java.util.List;

/**
 * Representation of Device entity inside the Device42 Application
 * @author Alexey
 *
 */
public class Device {
	
	/**
	 * The id of the device
	 */
	private long id;
	/**
	 * The name of the device
	 */
	private String name;
	/**
	 * The hardware model of the device
	 */
	private String hardwareModel;
	/**
	 * The serial number of the device
	 */
	private String serialNo;
	/**
	 * The asset number of the device
	 */
	private String assetNo;
	/**
	 * The service level of the device
	 */
	private String serviceLevel;
	/**
	 * The type of the device
	 */
	private String type;
	/**
	 * The name of virtual host if the device is virtual
	 */
	private String virtualHostName;
	/**
	 * The manufacturer of the device
	 */
	private String manufacturer;
	/**
	 * The UUID of the device
	 */
	private String uuid;
	/**
	 * The rack the device belongs to
	 */
	private String rack;
	/**
	 * The customer entry of the device
	 */
	private String customer;
	/**
	 * The room the device belongs to
	 */
	private String room;
	/**
	 * The building the device belongs to
	 */
	private String building;
	/**
	 * The operative system name of the device
	 */
	private String os;
	/**
	 * The operative system version of the device
	 */
	private String osVer;
	/**
	 * Is the device in service or not
	 */
	private boolean inService;
	/**
	 * The tags of the device
	 */
	private String[] tags;
	/**
	 * IPs list of the device
	 */
	private List<IP> ips;

	/**
	 * Get the asset number of the device
	 * @return The asset number of the device
	 */
	public String getAssetNo() {
		return assetNo;
	}

	/**
	 * Get the hardware model of the device
	 * @return The hardware model of the device
	 */
	public String getHardwareModel() {
		return hardwareModel;
	}

	/**
	 * Get the id of the device
	 * @return The id of the device
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get the manufacturer of the device
	 * @return The manufacturer of the device
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Get the name of the device
	 * @return The name of the device
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the rack the device belongs to
	 * @return The rack the device belongs to
	 */
	public String getRack() {
		return rack;
	}

	/**
	 * Get the serial number of the device
	 * @return The serial number of the device
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * Get the service level of the device
	 * @return The service level of the device
	 */
	public String getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * Get the type of the device
	 * @return The type of the device
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get the UUID of the device
	 * @return The UUID of the device
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Get the name of virtual host if the device is virtual
	 * @return The name of virtual host if the device is virtual
	 */
	public String getVirtualHostName() {
		return virtualHostName;
	}

	/**
	 * Set the asset number of the device
	 * @param assetNo The asset number of the device
	 */
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	/**
	 * Set the hardware model of the device
	 * @param hardwareModel The hardware model of the device
	 */
	public void setHardwareModel(String hardwareModel) {
		this.hardwareModel = hardwareModel;
	}

	/**
	 * Set the id of the device
	 * @param id The id of the device
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Set the manufacturer of the device
	 * @param manufacturer The manufacturer of the device
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Set the name of the device
	 * @param name The name of the device
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the rack the device belongs to
	 * @param rack The rack the device belongs to
	 */
	public void setRack(String rack) {
		this.rack = rack;
	}

	/**
	 * Set the serial number of the device
	 * @param serialNo The serial number of the device
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * Set the service level of the device
	 * @param serviceLevel The service level of the device
	 */
	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	/**
	 * Set the type of the device
	 * @param type The type of the device
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Set the UUID of the device
	 * @param uuid The UUID of the device
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Set the name of virtual host if the device is virtual
	 * @param virtualHostName The name of virtual host if the device is virtual
	 */
	public void setVirtualHostName(String virtualHostName) {
		this.virtualHostName = virtualHostName;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", hardwareModel=" + hardwareModel + ", serialNo=" + serialNo
				+ ", assetNo=" + assetNo + ", serviceLevel=" + serviceLevel + ", type=" + type + ", virtualHostName="
				+ virtualHostName + ", manufacturer=" + manufacturer + ", uuid=" + uuid + ", rack=" + rack
				+ ", customer=" + customer + ", tags=" + tags + "]";
	}

	/**
	 * Get the tags of the device
	 * @return The tags of the device
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Set the tags of the device
	 * @param tags The tags of the device
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
	 * Get the customer entry of the device
	 * @return The customer entry of the device
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * Set the customer entry of the device
	 * @param customer The customer entry of the device
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * Get the room the device belongs to
	 * @return The room the device belongs to
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * Set the room the device belongs to
	 * @param room The room the device belongs to
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * Get the operative system name of the device
	 * @return The operative system name of the device
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * Set the operative system name of the device
	 * @param building The operative system name of the device
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * Is the device in service or not
	 * @return Is the device in service or not
	 */
	public boolean isInService() {
		return inService;
	}

	/**
	 * Set is the device in service or not
	 * @param inService Is the device in service or not
	 */
	public void setInService(boolean inService) {
		this.inService = inService;
	}
	
	/**
	 * Get IPs list of the device
	 * @return IPs list of the device
	 */
	public List<IP> getIps() {
        return ips;
    }

    /**
     * Set IPs list of the device
     * @param ips IPs list of the device
     */
    public void setIps(List<IP> ips) {
        this.ips = ips;
    }

	/**
	 * Get the operative system name of the device
	 * @return The operative system name of the device
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Set the operative system name of the device
	 * @param os The operative system name of the device
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Get the operative system version of the device
	 * @return The operative system version of the device
	 */
	public String getOsVer() {
		return osVer;
	}

	/**
	 * Set the operative system version of the device
	 * @param osVer The operative system version of the device
	 */
	public void setOsVer(String osVer) {
		this.osVer = osVer;
	}
}
