package com.example.mangapp;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.CoverImageResponseModel;
import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.ResponseModelMangaList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ApiService apiService;
    private MangaAdapter mangaAdapter;
    private RecyclerView recyclerView;

    private List<MangaData> mangaList = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        apiService = ApiClient.getClient().create(ApiService.class);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mangaAdapter = new MangaAdapter(this,mangaList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                apiService.getMangaList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void searchManga(String query) {
        Call<ResponseModelMangaList> call = apiService.getMangaList(query);
        call.enqueue(new Callback<ResponseModelMangaList>() {
            @Override
            public void onResponse(Call<ResponseModelMangaList> call, Response<ResponseModelMangaList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mangaList.clear();
                    mangaList.addAll(response.body().getData());
                    mangaAdapter.notifyDataSetChanged();
                    //fetchCoverImages();
                }
            }

            @Override
            public void onFailure(Call<ResponseModelMangaList> call, Throwable t) {
                // Handle failure
            }
        });
    }

//    private void fetchCoverImages() {
//        for (MangaData manga : mangaList) {
//            Call<CoverImageResponseModel> call = apiService.getCoverImage(manga.getId());
//            call.enqueue(new Callback<CoverImageResponseModel>() {
//                @Override
//                public void onResponse(Call<CoverImageResponseModel> call, Response<CoverImageResponseModel> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String fileName = response.body().getData().getAttributes().getFileName();
//                        String imageUrl = ApiClient.BASE_URL + fileName;
//                        manga.setCoverImageUrl(imageUrl);
//                        mangaAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CoverImageResponseModel> call, Throwable t) {
//                    // Handle failure
//                }
//            });
//        }
    }
}