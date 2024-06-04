package com.example.mangapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordFragment extends DialogFragment {

    private static final String TAG = "PasswordRecovery";
    private FirebaseAuth mAuth;
    EditText editTextEmailRecovery;
    Button buttonRecovery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        editTextEmailRecovery = view.findViewById(R.id.editTextEmailRecovery);
        buttonRecovery = view.findViewById(R.id.buttonRecovery);

        mAuth = FirebaseAuth.getInstance();

        buttonRecovery.setOnClickListener((View v) ->{
            String email = editTextEmailRecovery.getText().toString();

            if(SignInActivity.emailValidation(email)){
                Toast.makeText(getActivity(),"Invalid email",Toast.LENGTH_SHORT).show();
            }else{
                sendRecoveryEMail(email);
            }
        });

        return view;
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

    public void sendRecoveryEMail(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG,"Email sent");
            }
        });
    }
}