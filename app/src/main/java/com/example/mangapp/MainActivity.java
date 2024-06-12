package com.example.mangapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaListResponse;
import com.example.mangapp.ApiResponse.MangaRelationship;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, MangaFragment.OnProfileButtonListener{


    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ApiService apiService;
    private MangaAdapter mangaAdapter;
    private RecyclerView recyclerView;
    private final List<MangaData> mangaList = new ArrayList<>();
    private int offsetCounter = 0;

    //Resultados de un titulo especifico
    private boolean titleBeingSearched = false;
    private String titleSearched = null;

    View[] mainViews;
    View fragmentContainer;
    SearchView searchView;
    ImageView imageViewPreviousPage, imageViewNextPage, imageViewPFP;

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
        firestore = FirebaseFirestore.getInstance();

        apiService = ApiClient.getClient().create(ApiService.class);

        fragmentContainer = findViewById(R.id.fragment_container);
        searchView = findViewById(R.id.searchView);
        imageViewPreviousPage = findViewById(R.id.imageViewBackwards);
        imageViewNextPage = findViewById(R.id.imageViewForward);
        imageViewPFP = findViewById(R.id.imageViewPFP);

        recyclerView = findViewById(R.id.recyclerViewManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mangaAdapter = new MangaAdapter(this, mangaList, this);
        recyclerView.setAdapter(mangaAdapter);

        ProfileFragment.loadProfilePicture(firestore, imageViewPFP,this);

        mainViews = new View[]{searchView, imageViewPreviousPage, imageViewNextPage, recyclerView, imageViewPFP};

        imageViewPreviousPage.setOnClickListener(v -> {
            if (offsetCounter - 10 >= 0) {
                if(titleBeingSearched){
                    mangaList.clear();
                    offsetCounter -= 10;
                    getMangaList(titleSearched, offsetCounter);
                    recyclerView.scrollToPosition(0);
                }else{
                    mangaList.clear();
                    offsetCounter -= 10;
                    getMangaList(offsetCounter);
                    recyclerView.scrollToPosition(0);
                }
            }
        });

        imageViewNextPage.setOnClickListener(v -> {
            if(titleBeingSearched){
                mangaList.clear();
                offsetCounter += 10;
                getMangaList(titleSearched, offsetCounter);
                recyclerView.scrollToPosition(0);
            }else{
                mangaList.clear();
                offsetCounter += 10;
                getMangaList(offsetCounter);
                recyclerView.scrollToPosition(0);
            }
        });

        //Primera llamada
        getMangaList(0);

        imageViewPFP.setOnClickListener(v -> openProfileFragment());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    titleBeingSearched = false;
                }else{
                    mangaList.clear();
                    getMangaList(query);
                    recyclerView.scrollToPosition(0);
                    titleBeingSearched = true;
                    titleSearched = query;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    titleBeingSearched = false;
                }
                return true;
            }
        });

        //Ocultacion para fragments
        //Ocultacion de botones de la activity en los fragments
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 1) {
                    fm.popBackStack();
                } else {
                    // Show all views again when no fragment is in the stack
                    for (View view : mainViews) {
                        view.setVisibility(View.VISIBLE);
                    }
                    fragmentContainer.setVisibility(View.GONE);
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        // Remove the callback to allow default back press handling
                        setEnabled(false);
                        onBackPressedDispatcher.onBackPressed();
                    }
                }
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
                Log.e("APICALL", String.valueOf(t.getCause()));
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
                Log.e("APICALL", String.valueOf(t.getCause()));
            }
        });
    }

    private void getMangaList(String title, int offset){
        Call<MangaListResponse> call = apiService.getMangaList(title, offset);

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
                Log.e("APICALL", String.valueOf(t.getCause()));
            }
        });
    }


    @Override
    public void onItemClick(MangaData manga) {
        // Handle the item click event, e.g., navigate to a detail screen
        Toast.makeText(this, "Clicked: " + manga.getAttributes().getTitle().get("en"), Toast.LENGTH_SHORT).show();
        String coverId = null;
        for (MangaRelationship mangaRelationship : manga.getRelationships()) {
            if (mangaRelationship.getType().equals("cover_art")) {
                coverId = mangaRelationship.getId();
                break;
            }
        }
        openMangaFragment(manga.getId(), coverId);
    }

    public void openMangaFragment(String mangaId, String coverId){
        loadFragment(new MangaFragment(mangaId, coverId));
    }

    public void openProfileFragment(){
        loadFragment(new ProfileFragment());
    }

    private void loadFragment(Fragment fragment) {
        // Hide all views except the fragment container
        for (View view : mainViews) {
            view.setVisibility(View.GONE);
        }
        fragmentContainer.setVisibility(View.VISIBLE);

        // Create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    @Override
    public void onProfileButtonListener() {
        // Handle the fragment transaction to open the ProfileFragment
        Fragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }

}

