package com.example.mangapp.LogIn;

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


import com.example.mangapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = "PasswordRecovery";
    private FirebaseAuth mAuth;
    EditText editTextEmailRecovery;
    ImageView imageViewReturn;
    Button buttonRecovery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        editTextEmailRecovery = view.findViewById(R.id.editTextEmailRecovery);
        buttonRecovery = view.findViewById(R.id.buttonRecovery);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);

        mAuth = FirebaseAuth.getInstance();

        buttonRecovery.setOnClickListener((View v) ->{
            String email = editTextEmailRecovery.getText().toString();

            if(SignInActivity.emailValidation(email)){
                Toast.makeText(getActivity(),"Invalid email",Toast.LENGTH_SHORT).show();
            }else{
                sendRecoveryEMail(email, mAuth);
            }
        });

        imageViewReturn.setOnClickListener((View v) ->{
            if(getActivity() != null){
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

    public static void sendRecoveryEMail(String email, FirebaseAuth mAuth){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG,"Email sent");
            }
        });
    }
}