package com.example.mangapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationFragment extends Fragment {

    private final FirebaseUser USER;
    private static final String TAG = "EmailVerificationFragment";
    EditText editTextEmailVerification;
    Button buttonVerification;
    ImageView imageViewReturn;

    public EmailVerificationFragment(FirebaseUser user){
        this.USER = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_verification, container, false);

        editTextEmailVerification = view.findViewById(R.id.editTextEmailVerification);
        buttonVerification = view.findViewById(R.id.buttonVerification);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);


        buttonVerification.setOnClickListener((View v) ->{
            String email = editTextEmailVerification.getText().toString();

            if(SignInActivity.emailValidation(email)){
                Toast.makeText(getActivity(),"Invalid email",Toast.LENGTH_SHORT).show();
            }else{
                sendEmail(this.USER);
            }
        });

        imageViewReturn.setOnClickListener((View v) ->{
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
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
        }else{
            Toast.makeText(getActivity(),"User is currently not signed in",Toast.LENGTH_SHORT).show();
        }
    }
}