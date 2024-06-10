package com.example.mangapp.ApiResponse;


public class CoverResponseModel {
    private String result;

    private String response;

    private CoverData data;

    // Getters and Setters

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

    public CoverData getData() {
        return data;
    }

    public void setData(CoverData data) {
        this.data = data;
    }
}


