package com.example.mangapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaFragment extends Fragment {
    private ApiService apiService;
    private final String MANGA_ID, COVER_ID;
    private MangaData mangaData = null;
    ImageView imageViewReturn, imageViewPFP, imageViewMangaCover;
    TextView textViewType, textViewMangaTitle, textViewMangaType, textViewMangaDescription;

    public MangaFragment(String mangaId, String coverId){
        this.MANGA_ID = mangaId;
        this.COVER_ID = coverId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manga, container, false);

        apiService = ApiClient.getClient().create(ApiService.class);

        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        imageViewPFP = view.findViewById(R.id.imageViewPFP);
        imageViewMangaCover = view.findViewById(R.id.imageViewMangaCover);
        textViewType = view.findViewById(R.id.textViewType);
        textViewMangaTitle = view.findViewById(R.id.textViewMangaTitle);
        textViewMangaType = view.findViewById(R.id.textViewMangaType);
        textViewMangaDescription = view.findViewById(R.id.textViewMangaDescription);

        getManga(MANGA_ID);
        MangaAdapter.loadCoverImage(COVER_ID, imageViewMangaCover, MANGA_ID, apiService, getActivity());
        //Funciones OnClick
        imageViewReturn.setOnClickListener(v ->{
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        imageViewPFP.setOnClickListener(v -> {

        });

        // Funcion para ir para atras y rellenar el activity
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    if (fragmentManager.getBackStackEntryCount() > 1) {
                        fragmentManager.popBackStack();
                    } else {
                        setEnabled(false);
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                }
            }
        });


        return view;
    }

    private void getManga(String mangaId){
        Call<MangaResponse> call = apiService.getManga(mangaId);

        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mangaData = response.body().getData();
                        textViewType.setText(mangaData.getType().toUpperCase());
                        textViewMangaTitle.setText(mangaData.getAttributes().getTitle().get("en"));
                        textViewMangaType.setText(mangaData.getAttributes().getCreatedAt());
                        textViewMangaDescription.setText(mangaData.getAttributes().getDescription().get("en"));
                    }
                    Log.d("APICALL","SUCCESS");
                } else {
                    // Handle the error
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                // Handle the failure
                Log.e("APICALL", String.valueOf(t.getCause()));
            }
        });
    }
}