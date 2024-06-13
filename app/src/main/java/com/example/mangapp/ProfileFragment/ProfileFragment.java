package com.example.mangapp.ProfileFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mangapp.LogIn.ForgotPasswordFragment;
import com.example.mangapp.LogIn.SignInActivity;
import com.example.mangapp.MainActivity;
import com.example.mangapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private static final int PICK_IMAGE_REQ = 1;
    Button buttonChangePFP, buttonChangePassword, buttonChangeUsername;
    ImageView imageViewReturn, imageViewSignOut, imageViewPFP;
    TextView textViewProfileUsername;
    FrameLayout frameLayout;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonChangePFP = view.findViewById(R.id.buttonChangePFP);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonChangeUsername = view.findViewById(R.id.buttonChangeUsername);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        imageViewSignOut = view.findViewById(R.id.imageViewSignOut);
        imageViewPFP = view.findViewById(R.id.imageViewProfileImage);
        textViewProfileUsername = view.findViewById(R.id.textViewProfileUsername);
        frameLayout = view.findViewById(R.id.profile_fragment_container);
        tabLayout = view.findViewById(R.id.tabLayout);

        imageViewReturn.setOnClickListener(v -> {
            if(getActivity() != null){
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        imageViewSignOut.setOnClickListener(v -> signOut());
        buttonChangePFP.setOnClickListener(v -> openFilePicker());
        buttonChangePassword.setOnClickListener(v -> ForgotPasswordFragment.sendRecoveryEMail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),mAuth));
        //buttonChangeUsername.setOnClickListener(v -> SignUpFragment.saveUserData());

        //Tablayoout listener implementado
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new ReadMangaFragment();
                        break;
                    case 1:
                        fragment = new PendingMangaFragment();
                        break;
                    case 2:
                        fragment = new ReadingMangaFragment();
                        break;
                    case 3:
                        fragment = new FavoriteMangaFragment();
                        break;
                }
                if (fragment != null) {
                    getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        if (savedInstanceState == null) {
            Objects.requireNonNull(tabLayout.getTabAt(0)).select();
            getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, new ReadMangaFragment()).commit();
        }

        loadUsername(firestore, textViewProfileUsername);
        loadProfilePicture(firestore, imageViewPFP, getActivity());

        // Boton para atras
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

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            uploadImgToFirebase(imageUri);
        }
    }

    private void uploadImgToFirebase(Uri imageUri) {
        if (imageUri != null) {
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profile_pictures/" + userId + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUri = uri.toString();
                        saveImgUrltoFirebase(downloadUri);
                        Toast.makeText(getActivity(), "File uploaded", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void saveImgUrltoFirebase(String imageUrl) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        Map<String, Object> updates = new HashMap<>();
        updates.put("profile_picture", imageUrl);

        firestore.collection("users").document(userId).set(updates, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
                        loadProfilePicture(firestore, imageViewPFP, getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Failed to save image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void loadProfilePicture(FirebaseFirestore firestore, ImageView imageViewPFP, Context context) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference documentReference = firestore.collection("users").document(userId);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String profilePictureUrl = document.getString("profile_picture");
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        Glide.with(context)
                                .load(profilePictureUrl)
                                .apply(new RequestOptions().placeholder(R.drawable.profileicon29).error(R.drawable.profileicon29).apply(RequestOptions.bitmapTransform(new RoundedCorners(100))))
                                .into(imageViewPFP);
                    }
                }
            } else {
                if(!context.getClass().getName().equals(MainActivity.class.getName())){
                    Toast.makeText(context, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUsername(FirebaseFirestore firestore, TextView textViewProfileUsername) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String username = document.getString("username");
                    textViewProfileUsername.setText(username != null ? username : "Username not available");
                } else {
                    textViewProfileUsername.setText("User document does not exist");
                }
            } else {
                Toast.makeText(getActivity(), "Failed to load username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }

}
