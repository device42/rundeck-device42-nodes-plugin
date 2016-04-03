package com.device42.client.model;

public class Room {
    private final long id;
    private final String name;
    private final long buildingId;
    private final String buildingName;

    public Room(long id, String name, long buildingId, String buildingName) {
        this.id = id;
        this.name = name;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Room [id=" + id + ", name=" + name + ", buildingId="
                + buildingId + ", buildingName=" + buildingName + "]";
    }
}
