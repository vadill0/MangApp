package com.example.mangapp.ProfileFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChangeUsernameFragment extends DialogFragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    EditText editText;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editText = view.findViewById(R.id.editTextUsername);
        button = view.findViewById(R.id.buttonChangeUsernameDF);

        button.setOnClickListener(v -> {
            String username = editText.getText().toString();
            if(!username.isEmpty()){
                saveUsername(username);
            }
        });

        return view;
    }

    private void saveUsername(String newUsername) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = firestore.collection("users").document(user.getUid());
            userRef.update("username", newUsername)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating username", Toast.LENGTH_SHORT).show());
        }
    }
}