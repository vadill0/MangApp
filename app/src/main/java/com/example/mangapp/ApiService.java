package com.example.mangapp;

import com.example.mangapp.ApiResponse.CoverImageResponseModel;
import com.example.mangapp.ApiResponse.MangaListResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("manga/{id}")
    Call<MangaListResponse> getManga(@Path("id") String id);

    @GET("manga")
    Call<MangaListResponse> getMangaList(@Query("title") String title);

    @GET("manga")
    Call<MangaListResponse> getMangaList();

    @GET("cover/{mangaOrCoverId}")
    Call<CoverImageResponseModel> getCover(@Path("id") String id);
}
