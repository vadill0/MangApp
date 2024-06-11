package com.example.mangapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MangaFragment extends Fragment {

    ImageView imageViewReturn, imageViewPFP, imageViewMangaCover;
    TextView textViewType, textViewMangaTitle, textViewMangaReleaseDate, textViewMangaDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manga, container, false);

        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        imageViewPFP = view.findViewById(R.id.imageViewPFP);
        imageViewMangaCover = view.findViewById(R.id.imageViewMangaCover);
        textViewType = view.findViewById(R.id.textViewType);
        textViewMangaTitle = view.findViewById(R.id.textViewMangaTitle);
        textViewMangaReleaseDate = view.findViewById(R.id.textViewMangaReleaseDate);
        textViewMangaDescription = view.findViewById(R.id.textViewMangaDescription);

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
}