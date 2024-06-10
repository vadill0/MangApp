package com.example.mangapp.ApiResponse;

import java.util.List;

public class CoverData {
    private String id;

    private String type;

    private CoverAttributes attributes;

    private List<CoverRelationship> relationships;

    // Getters and Setters

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

    public CoverAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(CoverAttributes attributes) {
        this.attributes = attributes;
    }

    public List<CoverRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<CoverRelationship> relationships) {
        this.relationships = relationships;
    }
}
