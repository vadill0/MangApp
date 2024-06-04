package com.example.mangapp;



import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpFragment extends DialogFragment {

    private static final String TAG = "SignUpDialogFragment";
    EditText editTextUser, editTextEmail, editTextPassword;
    Button buttonSignUp;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        editTextEmail = view.findViewById(R.id.editTextEmailSU);
        editTextPassword = view.findViewById(R.id.editTextPasswordSU);
        editTextUser = view.findViewById(R.id.editTextUser);

        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(v -> {
            String email, password;
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();

            if(editTextUser.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),"Invalid User", Toast.LENGTH_SHORT).show();
            }else if(SignInActivity.emailValidation(email)){
                Toast.makeText(getActivity(),"Invalid Email", Toast.LENGTH_SHORT).show();
            } else if(SignInActivity.passwordValidation(password)) {
                Toast.makeText(getActivity(),"Invalid Password", Toast.LENGTH_SHORT).show();
            }else{
                signUp(email, password);
            }
        });

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

    public void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                sendEmail(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(getActivity(), "Sign up error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendEmail(FirebaseUser user){
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Verification email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getActivity(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}