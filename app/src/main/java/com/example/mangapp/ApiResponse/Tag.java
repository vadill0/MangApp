package com.example.mangapp.ApiResponse;

import java.util.List;

public class Tag {
    private String id;
    private String type;
    private TagAttributes attributes;
    private List<Relationship> relationships;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TagAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(TagAttributes attributes) {
        this.attributes = attributes;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
}
