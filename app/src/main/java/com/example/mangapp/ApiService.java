package com.example.mangapp;

import com.example.mangapp.ApiResponse.CoverResponseModel;
import com.example.mangapp.ApiResponse.MangaListResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("manga/{id}?includes[]=cover_art")
    Call<MangaListResponse> getManga(@Path("id") String id);

    @GET("manga?includes[]=cover_art")
    Call<MangaListResponse> getMangaList(@Query("title") String title);
    @GET("manga?includes[]=cover_art")
    Call<MangaListResponse> getMangaList(@Query("title") String title,
                                         @Query("offset") int offset);

    @GET("manga?includes[]=cover_art")
    Call<MangaListResponse> getMangaList(@Query("offset") int offset);

    @GET("cover/{mangaOrCoverId}")
    Call<CoverResponseModel> getCover(@Path("mangaOrCoverId") String id);
}
