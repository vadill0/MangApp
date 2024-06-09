package com.example.mangapp.ApiResponse;

import java.util.List;

public class ResponseModelMangaList {
    private String result;
    private String response;
    private List<MangaData> data;

    // Getters and setters
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<MangaData> getData() {
        return data;
    }

    public void setData(List<MangaData> data) {
        this.data = data;
    }
}


