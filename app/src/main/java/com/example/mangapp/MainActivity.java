package com.example.mangapp;

import android.os.Bundle;
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

import com.example.mangapp.ApiResponse.Data;
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

    private List<Data> mangaList = new ArrayList<>();
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
        mangaAdapter = new MangaAdapter(mangaList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getMangaList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }
    private void getMangaList(String filter) {
        //Ejemplo de llamada a la API

        Call<ResponseModelMangaList> call = apiService.getMangaList(filter);
        call.enqueue(new Callback<ResponseModelMangaList>() {
            @Override
            public void onResponse(Call<ResponseModelMangaList> call, Response<ResponseModelMangaList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mangaList.clear();
                    mangaList.addAll(response.body().getData());
                    mangaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseModelMangaList> call, Throwable t) {
                // Handle failure
                Toast.makeText(MainActivity.this,"API response failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}