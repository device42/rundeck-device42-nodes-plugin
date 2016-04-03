package com.device42.client.model;

public class Service {
    private final long id;
    private final String name;
    private final String displayName;
    private final String description;
    private final String category;

    public Service(long id, String name, String displayName, String description, String category) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Services [id=" + id + ", name=" + name + ", displayName="
                + displayName + ", description=" + description + ", category="
                + category + "]";
    }
}
