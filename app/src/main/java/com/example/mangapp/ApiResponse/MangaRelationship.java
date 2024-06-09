package com.example.mangapp.ApiResponse;

import androidx.annotation.NonNull;

public class MangaRelationship {
    private String id;
    private String type;
    private String related;
    private Object attributes;

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

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    @NonNull
    @Override
    public String toString() {
        return "Relationship{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", related='" + related + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}