package com.example.mangapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaListResponse;
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

        mangaAdapter = new MangaAdapter(this, mangaList);
        recyclerView.setAdapter(mangaAdapter);

        //Primera llamada
        getMangaList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void getMangaList(){
        Call<MangaListResponse> call = apiService.getMangaList();

        call.enqueue(new Callback<MangaListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaListResponse> call, @NonNull Response<MangaListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mangaList.addAll(response.body().getData());
                        mangaAdapter.notifyDataSetChanged();
                    }
                    Log.d("APICALL","SUCCESS");
                } else {
                    // Handle the error
                    Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaListResponse> call, @NonNull Throwable t) {
                // Handle the failure
                Log.e("APICALL",t.getMessage());
            }
        });
    }

    private void getMangaList(String manga){
        Call<MangaListResponse> call = apiService.getMangaList(manga);

        call.enqueue(new Callback<MangaListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaListResponse> call, @NonNull Response<MangaListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mangaList.addAll(response.body().getData());
                        mangaAdapter.notifyDataSetChanged();
                    }
                    Log.d("APICALL","SUCCESS");
                } else {
                    // Handle the error
                    Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaListResponse> call, @NonNull Throwable t) {
                // Handle the failure
                Log.e("APICALL",t.getMessage());
            }
        });
    }
}

//    private void fetchCoverImages() {
//        for (MangaData manga : mangaList) {
//            Call<CoverResponseModel> call = apiService.getCoverImage(manga.getId());
//            call.enqueue(new Callback<CoverResponseModel>() {
//                @Override
//                public void onResponse(Call<CoverResponseModel> call, Response<CoverResponseModel> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String fileName = response.body().getData().getAttributes().getFileName();
//                        String imageUrl = ApiClient.BASE_URL + fileName;
//                        manga.setCoverImageUrl(imageUrl);
//                        mangaAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CoverResponseModel> call, Throwable t) {
//                    // Handle failure
//                }
//            });
//        }

