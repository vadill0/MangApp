package com.example.mangapp.ApiResponse;

import java.util.List;

public class MangaListResponse {
    private String result;
    private String response;
    private List<MangaData> data;
    private int limit;
    private int offset;
    private int total;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ResponseModelMangaList{" +
                "result='" + result + '\'' +
                ", response='" + response + '\'' +
                ", data=" + data +
                ", limit=" + limit +
                ", offset=" + offset +
                ", total=" + total +
                '}';
    }
}

