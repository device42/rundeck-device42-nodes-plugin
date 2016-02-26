package com.device42.client.model;

public class Device {
    private long id;
    private String name;
    private String hardwareModel;
    private String serialNo;
    private String assetNo;
    private String serviceLevel;
    private String type;
    private String virtualHostName;
    private String manufacturer;
    private String uuid;
    private String rack;

    public Device() {}

    public String getAssetNo() {
        return assetNo;
    }

	public String getHardwareModel() {
        return hardwareModel;
    }

	public long getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getRack() {
		return rack;
	}

    public String getSerialNo() {
        return serialNo;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public void setHardwareModel(String hardwareModel) {
        this.hardwareModel = hardwareModel;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRack(String rack) {
		this.rack = rack;
	}

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setVirtualHostName(String virtualHostName) {
        this.virtualHostName = virtualHostName;
    }

    @Override
    public String toString() {
        return "Device [id=" + id + ", name=" + name + ", hardwareModel=" + hardwareModel + ", serialNo=" + serialNo
                + ", assetNo=" + assetNo + ", serviceLevel=" + serviceLevel + ", type=" + type + ", virtualHostName="
                + virtualHostName + ", manufacturer=" + manufacturer + ", uuid=" + uuid + ", rack=" + rack + "]";
    }
}
