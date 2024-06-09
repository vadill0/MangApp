package com.example.mangapp.ApiResponse;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class Attributes {
    private Map<String, String> title;
    private Map<String, String> links;  // Assuming the cover image URL is stored in links

    // Getters and setters
    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
