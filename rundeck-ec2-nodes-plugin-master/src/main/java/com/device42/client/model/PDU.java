package com.device42.client.model;

public class PDU {
    private final long id;
    private final String name;
    private final String notes;

    public PDU(long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "PDU [id=" + id + ", name=" + name + ", notes=" + notes + "]";
    }
}
