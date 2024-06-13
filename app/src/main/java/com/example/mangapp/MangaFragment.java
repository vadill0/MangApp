package com.example.mangapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangapp.ApiResponse.MangaData;
import com.example.mangapp.ApiResponse.MangaResponse;
import com.example.mangapp.DataBase.DatabaseHelper;
import com.example.mangapp.DataBase.DatabaseManager;
import com.example.mangapp.ProfileFragment.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaFragment extends Fragment {
    private FirebaseUser user;
    private ApiService apiService;
    private FirebaseFirestore firestore;
    private DatabaseManager databaseManager;
    private final String MANGA_ID, COVER_ID;
    private MangaData mangaData = null;
    private int whereIsTheManga = -1;
    private final String TAG = "MangaAddMethod";
    View mangaFragmentLayout;
    ImageView imageViewReturn, imageViewPFP, imageViewMangaCover, buttonRead, buttonPending, buttonReading, buttonFavorite;
    TextView textViewType, textViewMangaTitle, textViewMangaType, textViewMangaDescription;
    Drawable originalBackgroundForListButtons;

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
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseManager = new DatabaseManager(getActivity());
        databaseManager.open();

        mangaFragmentLayout = view.findViewById(R.id.mangaFragmentLayout);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        imageViewPFP = view.findViewById(R.id.imageViewPFP);
        imageViewMangaCover = view.findViewById(R.id.imageViewMangaCover);
        textViewType = view.findViewById(R.id.textViewType);
        textViewMangaTitle = view.findViewById(R.id.textViewMangaTitle);
        textViewMangaType = view.findViewById(R.id.textViewMangaType);
        textViewMangaDescription = view.findViewById(R.id.textViewMangaDescription);
        buttonRead = view.findViewById(R.id.buttonRead);
        buttonPending = view.findViewById(R.id.buttonPending);
        buttonFavorite = view.findViewById(R.id.buttonFavorite);
        buttonReading = view.findViewById(R.id.buttonReading);

        originalBackgroundForListButtons = buttonRead.getBackground();

        textViewMangaDescription.setMaxLines(15);
        textViewMangaDescription.setEllipsize(TextUtils.TruncateAt.END);

        getManga(MANGA_ID);
        MangaAdapter.loadCoverImage(COVER_ID, imageViewMangaCover, MANGA_ID, apiService, getActivity());
        textViewMangaType.setWidth(imageViewMangaCover.getWidth());
        ProfileFragment.loadProfilePicture(firestore, imageViewPFP,getActivity());
        checkForList(user, MANGA_ID);

        //Funciones OnClick
        imageViewReturn.setOnClickListener(v ->{
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        boolean esta = databaseManager.isMangaInList(DatabaseHelper.TABLE_READ, user.getUid(), MANGA_ID);

        Log.d("MANGAEXISTEENLALISTA", String.valueOf(esta));

        imageViewPFP.setOnClickListener(v -> openProfileFragment());
        buttonRead.setOnClickListener(v -> {
            clickListButton(DatabaseHelper.TABLE_READ, user, MANGA_ID);
            checkForList(user, MANGA_ID);
        });
        buttonPending.setOnClickListener(v -> {
            clickListButton(DatabaseHelper.TABLE_PENDING, user, MANGA_ID);
            checkForList(user, MANGA_ID);
        });
        buttonFavorite.setOnClickListener(v -> {
            clickListButton(DatabaseHelper.TABLE_FAVORITES, user, MANGA_ID);
            checkForList(user, MANGA_ID);
        });
        buttonReading.setOnClickListener(v -> {
            clickListButton(DatabaseHelper.TABLE_READING, user, MANGA_ID);
            checkForList(user, MANGA_ID);
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
                        if(mangaData.getAttributes().getPublicationDemographic() != null){
                            textViewMangaType.setText(mangaData.getAttributes().getPublicationDemographic().toUpperCase());
                        }
                        if (!mangaData.getAttributes().getDescription().isEmpty()){
                            textViewMangaDescription.setText(mangaData.getAttributes().getDescription().get("en"));
                        }else{
                            textViewMangaDescription.setText("Description not available");
                        }

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

    public interface OnProfileButtonListener {
        void onProfileButtonListener();
    }

    private MangaFragment.OnProfileButtonListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MangaFragment.OnProfileButtonListener) {
            mListener = (MangaFragment.OnProfileButtonListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnProfileButtonListener");
        }
    }

    // Call this method when you want to open the email verification fragment
    private void openProfileFragment() {
        if (mListener != null) {
            mListener.onProfileButtonListener();
        }
    }

    private void checkForList(FirebaseUser user, String mangaId){
        if(databaseManager.isMangaInList(DatabaseHelper.TABLE_READ, user.getUid(), mangaId)){
            whereIsTheManga = 0;
            buttonRead.setBackgroundColor(Color.rgb(24, 117, 69));
            buttonPending.setBackground(originalBackgroundForListButtons);
            buttonFavorite.setBackground(originalBackgroundForListButtons);
            buttonReading.setBackground(originalBackgroundForListButtons);
        } else if (databaseManager.isMangaInList(DatabaseHelper.TABLE_PENDING, user.getUid(), mangaId)) {
            whereIsTheManga = 1;
            buttonPending.setBackgroundColor(Color.rgb(248, 148, 6));
            buttonRead.setBackground(originalBackgroundForListButtons);
            buttonFavorite.setBackground(originalBackgroundForListButtons);
            buttonReading.setBackground(originalBackgroundForListButtons);
        } else if (databaseManager.isMangaInList(DatabaseHelper.TABLE_FAVORITES, user.getUid(), mangaId)) {
            whereIsTheManga = 2;
            buttonFavorite.setBackgroundColor(Color.rgb(156, 38, 38));
            buttonRead.setBackground(originalBackgroundForListButtons);
            buttonPending.setBackground(originalBackgroundForListButtons);
            buttonReading.setBackground(originalBackgroundForListButtons);
        } else if (databaseManager.isMangaInList(DatabaseHelper.TABLE_READING, user.getUid(), mangaId)) {
            whereIsTheManga = 3;
            buttonReading.setBackgroundColor(Color.rgb(47, 150, 180));
            buttonRead.setBackground(originalBackgroundForListButtons);
            buttonPending.setBackground(originalBackgroundForListButtons);
            buttonFavorite.setBackground(originalBackgroundForListButtons);
        }else{
            buttonRead.setBackground(originalBackgroundForListButtons);
            buttonPending.setBackground(originalBackgroundForListButtons);
            buttonFavorite.setBackground(originalBackgroundForListButtons);
            buttonReading.setBackground(originalBackgroundForListButtons);
        }
    }

    private void clickListButton(String TABLE_NAME, FirebaseUser user, String mangaId) {
        boolean result;
        switch (whereIsTheManga){
            case 0:
                result = databaseManager.deleteMangaFromList(DatabaseHelper.TABLE_READ, user.getUid(), mangaId);
                Log.d(TAG, "Manga deleted from readList" + result);
                if(!TABLE_NAME.equals(DatabaseHelper.TABLE_READ)){
                    addMangaToList(TABLE_NAME, user, mangaId);
                }
                break;
            case 1:
                result = databaseManager.deleteMangaFromList(DatabaseHelper.TABLE_PENDING, user.getUid(), mangaId);
                Log.d(TAG, "Manga deleted from pendingList" + result);
                if(!TABLE_NAME.equals(DatabaseHelper.TABLE_PENDING)){
                    addMangaToList(TABLE_NAME, user, mangaId);
                }
                break;
            case 2:
                result = databaseManager.deleteMangaFromList(DatabaseHelper.TABLE_FAVORITES, user.getUid(), mangaId);
                Log.d(TAG, "Manga deleted from favoritesList" + result);
                if(!TABLE_NAME.equals(DatabaseHelper.TABLE_FAVORITES)){
                    addMangaToList(TABLE_NAME, user, mangaId);
                }
                break;
            case 3:
                result = databaseManager.deleteMangaFromList(DatabaseHelper.TABLE_READING, user.getUid(), mangaId);
                Log.d(TAG, "Manga deleted from readingList" + result);
                if(!TABLE_NAME.equals(DatabaseHelper.TABLE_READING)){
                    addMangaToList(TABLE_NAME, user, mangaId);
                }
                break;
            default:
                Log.d(TAG, "The manga isnt in a list");
                addMangaToList(TABLE_NAME, user, mangaId);
                break;
        }
    }

    private void addMangaToList(String TABLE_NAME, FirebaseUser user, String mangaId){
        long insertManga = databaseManager.insertManga(TABLE_NAME, user.getUid(), mangaId);
        if(insertManga != -1){
            Log.d(TAG,"Manga added to " + TABLE_NAME + " successfully");
        }else{
            Log.e(TAG,"Error adding manga to table");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }
}