package com.example.mangapp;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class SignUpFragment extends DialogFragment {

    Button signUpButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpButton = view.findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> signUp());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set the dialog size
        if (getDialog() != null) {
            int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
        }
    }

    public void signUp(){

    }
}