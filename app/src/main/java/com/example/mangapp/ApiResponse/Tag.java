package com.example.mangapp.ApiResponse;

import androidx.annotation.NonNull;

import java.util.List;

public class Tag {
    private String id;
    private String type;
    private TagAttributes attributes;
    private List<MangaRelationship> relationships;

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

    public List<MangaRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<MangaRelationship> relationships) {
        this.relationships = relationships;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                ", relationships=" + relationships +
                '}';
    }
}