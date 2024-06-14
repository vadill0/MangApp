package com.example.mangapp.ProfileFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mangapp.LogIn.SignUpFragment;
import com.example.mangapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ChangeUsernameFragment extends DialogFragment {
    EditText editText;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        editText = view.findViewById(R.id.editTextUsername);
        button = view.findViewById(R.id.buttonChangeUsernameDF);

        button.setOnClickListener(v -> {
            String username = editText.getText().toString();
            if(!username.isEmpty()){
                SignUpFragment.saveUserData(firestore, getActivity(), Objects.requireNonNull(user).getUid(), username, user.getEmail());
            }
        });

        return view;
    }
}