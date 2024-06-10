package com.example.mangapp.ApiResponse;

public class CoverRelationship {

    private String id;

    private String type;

    private String related;

    private CoverAttributes attributes;

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

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public CoverAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(CoverAttributes attributes) {
        this.attributes = attributes;
    }
}
