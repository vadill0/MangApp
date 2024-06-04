package com.example.mangapp;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class ForgotPasswordFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set the dialog size
        if (getDialog() != null) {
            int width = getResources().getDimensionPixelSize(R.dimen.FPdialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.FPdialog_height);
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
        }
    }
}