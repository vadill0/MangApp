package com.example.mangapp.ApiResponse;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class Attributes {
    private Map<String, String> title;
    private List<Map<String, String>> altTitles;
    private Map<String, String> description;
    private boolean isLocked;
    private Map<String, String> links;
    private String originalLanguage;
    private String lastVolume;
    private String lastChapter;
    private String publicationDemographic;
    private String status;
    private int year;
    private String contentRating;
    private boolean chapterNumbersResetOnNewVolume;
    private List<String> availableTranslatedLanguages;
    private String latestUploadedChapter;
    private List<Tag> tags;
    private String state;
    private int version;
    private String createdAt;
    private String updatedAt;

    // Getters and setters


    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public List<Map<String, String>> getAltTitles() {
        return altTitles;
    }

    public void setAltTitles(List<Map<String, String>> altTitles) {
        this.altTitles = altTitles;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getLastVolume() {
        return lastVolume;
    }

    public void setLastVolume(String lastVolume) {
        this.lastVolume = lastVolume;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getPublicationDemographic() {
        return publicationDemographic;
    }

    public void setPublicationDemographic(String publicationDemographic) {
        this.publicationDemographic = publicationDemographic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public boolean isChapterNumbersResetOnNewVolume() {
        return chapterNumbersResetOnNewVolume;
    }

    public void setChapterNumbersResetOnNewVolume(boolean chapterNumbersResetOnNewVolume) {
        this.chapterNumbersResetOnNewVolume = chapterNumbersResetOnNewVolume;
    }

    public List<String> getAvailableTranslatedLanguages() {
        return availableTranslatedLanguages;
    }

    public void setAvailableTranslatedLanguages(List<String> availableTranslatedLanguages) {
        this.availableTranslatedLanguages = availableTranslatedLanguages;
    }

    public String getLatestUploadedChapter() {
        return latestUploadedChapter;
    }

    public void setLatestUploadedChapter(String latestUploadedChapter) {
        this.latestUploadedChapter = latestUploadedChapter;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attributes{" +
                "title=" + title +
                ", altTitles=" + altTitles +
                ", description=" + description +
                ", isLocked=" + isLocked +
                ", links=" + links +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", lastVolume='" + lastVolume + '\'' +
                ", lastChapter='" + lastChapter + '\'' +
                ", publicationDemographic='" + publicationDemographic + '\'' +
                ", status='" + status + '\'' +
                ", year=" + year +
                ", contentRating='" + contentRating + '\'' +
                ", chapterNumbersResetOnNewVolume=" + chapterNumbersResetOnNewVolume +
                ", availableTranslatedLanguages=" + availableTranslatedLanguages +
                ", latestUploadedChapter='" + latestUploadedChapter + '\'' +
                ", tags=" + tags +
                ", state='" + state + '\'' +
                ", version=" + version +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
