package com.example.mangapp.ApiResponse;


public class MangaResponse {
    private String result;
    private String response;
    private MangaData data;
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

    public MangaData getData() {
        return data;
    }

    public void setData(MangaData data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "ResponseModelMangaList{" +
                "result='" + result + '\'' +
                ", response='" + response + '\'' +
                ", data=" + data +
                '}';
    }
}

