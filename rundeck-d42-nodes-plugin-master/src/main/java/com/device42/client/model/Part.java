package com.device42.client.model;

public class Part {
    private final long id;
    private final String modelName;
    private final String modelType;
    private final String description;
    private final String assignment;
    private final String serialNo;
    private final int count;

    public Part(
            long id,
            String modelName,
            String modelType,
            String description,
            String assignment,
            String serialNo,
            int count) {
        this.id = id;
        this.modelName = modelName;
        this.modelType = modelType;
        this.description = description;
        this.assignment = assignment;
        this.serialNo = serialNo;
        this.count = count;
    }

    public String getAssignment() {
        return assignment;
    }

    public int getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public String getSerialNo() {
        return serialNo;
    }

    @Override
    public String toString() {
        return "Part [id=" + id + ", modelName=" + modelName + ", modelType=" + modelType + ", description="
                + description + ", assignment=" + assignment + ", serialNo=" + serialNo + ", count=" + count + "]";
    }
}
