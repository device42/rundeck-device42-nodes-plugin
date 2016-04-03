package com.device42.client.model;

public class Rack {
    private final long id;
    private final String name;
    private final String building;
    private final String room;

    public Rack(long id, String name, String building, String room) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Rack [id=" + id + ", name=" + name + ", building=" + building
                + ", room=" + room + "]";
    }
}
