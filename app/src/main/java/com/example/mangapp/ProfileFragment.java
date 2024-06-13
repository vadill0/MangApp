package com.example.mangapp;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mangapp.LogIn.ForgotPasswordFragment;
import com.example.mangapp.LogIn.SignInActivity;
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
    Button buttonChangePFP, buttonChangePassword;
    ImageView imageViewReturn, imageViewSignOut, imageViewPFP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonChangePFP = view.findViewById(R.id.buttonChangePFP);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        imageViewSignOut = view.findViewById(R.id.imageViewSignOut);
        imageViewPFP = view.findViewById(R.id.imageViewProfileImage);

        imageViewReturn.setOnClickListener(v -> {
            if(getActivity() != null){
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
        imageViewSignOut.setOnClickListener(v -> signOut());
        buttonChangePFP.setOnClickListener(v -> openFilePicker());
        buttonChangePassword.setOnClickListener(v -> ForgotPasswordFragment.sendRecoveryEMail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),mAuth));

        loadProfilePicture(firestore, imageViewPFP, getActivity());

        // Handle back navigation and populate the activity
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
        if(requestCode == PICK_IMAGE_REQ && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null){
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

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }
}
