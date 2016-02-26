package com.device42.client.model;

public class Building {
    private final long id;
    private final String name;

    public Building(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Building [id=" + id + ", name=" + name + "]";
    }
}
