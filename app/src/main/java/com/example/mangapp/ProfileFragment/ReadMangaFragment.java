package com.example.mangapp.ProfileFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mangapp.ApiClient;
import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaResponse;
import com.example.mangapp.ApiService;
import com.example.mangapp.DataBase.DatabaseHelper;
import com.example.mangapp.DataBase.DatabaseManager;
import com.example.mangapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadMangaFragment extends Fragment{

    private ApiService apiService;
    private DatabaseManager databaseManager;
    private MangaAdapterProfile mangaAdapter;
    private final List<MangaData> mangaList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_manga, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        apiService = ApiClient.getClient().create(ApiService.class);
        databaseManager = new DatabaseManager(getActivity());
        databaseManager.open();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fillMangaList(user);

        mangaAdapter = new MangaAdapterProfile(getActivity(), mangaList);
        recyclerView.setAdapter(mangaAdapter);


        Log.d("frag iniciado", "sdadasdsa");

        getAllReadManga(Objects.requireNonNull(user));
        return view;
    }

    private List<String> getAllReadManga(FirebaseUser user){
        List<String> mangaIds = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = databaseManager.getMangaFromUser(DatabaseHelper.TABLE_READ, user.getUid());
            if (cursor != null && cursor.moveToFirst()) {
                int mangaIdColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MANGA_ID);
                do {
                    String mangaId = cursor.getString(mangaIdColumnIndex);
                    mangaIds.add(mangaId);
                } while (cursor.moveToNext());
                Log.d("DatabaseManager", mangaIds.toString());

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mangaIds;
    }


    private void getManga(String mangaId){
        Call<MangaResponse> call = apiService.getManga(mangaId);

        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mangaList.add(response.body().getData());
                    Log.d("ENtro en el if", "Si");
                    mangaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(),"Error in response",Toast.LENGTH_SHORT).show();
                    Log.d("ELSE", "no");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(),"API call failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillMangaList(FirebaseUser user){
        List<String> mangaIds = getAllReadManga(user);
        Log.d("FILL", String.valueOf(mangaIds.size()));
        for (String mangaId: mangaIds) {
            getManga(mangaId);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }
}