package com.example.mangapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
    private final List<MangaData> mangaList = new ArrayList<>();
    private int offsetCounter = 0;


    SearchView searchView;
    ImageView imageViewPreviousPage, imageViewNextPage;

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
        imageViewPreviousPage = findViewById(R.id.imageViewBackwards);
        imageViewNextPage = findViewById(R.id.imageViewForward);

        imageViewPreviousPage.setOnClickListener(v -> {
            if (offsetCounter - 10 >= 0) {
                mangaList.clear();
                offsetCounter -= 10;
                getMangaList(offsetCounter);
                recyclerView.scrollToPosition(0);
            }
        });

        imageViewNextPage.setOnClickListener(v -> {
            mangaList.clear();
            offsetCounter += 10;
            getMangaList(offsetCounter);
            recyclerView.scrollToPosition(0);
        });

        recyclerView = findViewById(R.id.recyclerViewManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mangaAdapter = new MangaAdapter(this, mangaList);
        recyclerView.setAdapter(mangaAdapter);

        //Primera llamada
        getMangaList(0);

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


    private void getMangaList(int offset){
        Call<MangaListResponse> call = apiService.getMangaList(offset);

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

