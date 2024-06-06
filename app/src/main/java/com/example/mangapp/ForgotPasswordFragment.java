package com.example.mangapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

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

    public void sendRecoveryEMail(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG,"Email sent");
            }
        });
    }
}