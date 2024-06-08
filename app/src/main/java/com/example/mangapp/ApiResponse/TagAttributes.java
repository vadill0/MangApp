package com.example.mangapp.ApiResponse;

import java.util.Map;

public class TagAttributes {
    private Map<String, String> name;
    private Map<String, String> description;
    private String group;
    private int version;

    // Getters and setters

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
