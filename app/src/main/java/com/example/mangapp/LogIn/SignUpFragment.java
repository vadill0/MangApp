package com.example.mangapp.LogIn;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpDialogFragment";
    TextView textViewVerificationNotSent;
    EditText editTextUser, editTextEmailRecovery, editTextPassword, editTextPasswordVerify;
    ImageView imageViewReturn;
    Button buttonSignUp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        editTextEmailRecovery = view.findViewById(R.id.editTextEmailSU);
        editTextPassword = view.findViewById(R.id.editTextPasswordSU);
        editTextUser = view.findViewById(R.id.editTextUserSU);
        editTextPasswordVerify = view.findViewById(R.id.editTextPasswordVerifySU);
        imageViewReturn = view.findViewById(R.id.imageViewReturn);
        textViewVerificationNotSent = view.findViewById(R.id.textViewVerificationNotSent);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonSignUp.setOnClickListener(v -> {
            String username, email, password, passwordVerify;
            username = editTextUser.getText().toString();
            email = editTextEmailRecovery.getText().toString();
            password = editTextPassword.getText().toString();
            passwordVerify = editTextPasswordVerify.getText().toString();

            if(editTextUser.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),"Invalid User", Toast.LENGTH_SHORT).show();
            }else if(SignInActivity.emailValidation(email)){
                Toast.makeText(getActivity(),"Invalid Email", Toast.LENGTH_SHORT).show();
            } else if(SignInActivity.passwordValidation(password)) {
                Toast.makeText(getActivity(),"Invalid Password", Toast.LENGTH_SHORT).show();
            } else if (!passwordVerify.equals(password)) {
                Toast.makeText(getActivity(),"The passwords don't match", Toast.LENGTH_SHORT).show();
            } else{
                signUp(username, email, password);
            }
        });

        textViewVerificationNotSent.setOnClickListener((View v) -> requestEmailVerification());

        imageViewReturn.setOnClickListener(v -> {
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

    public void signUp(String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUserData(firestore, getActivity(), user.getUid(), username, user.getEmail());
                    sendEmail(user);
                    FirebaseAuth.getInstance().signOut();
                }
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

    public interface OnEmailVerificationRequestedListener {
        void onEmailVerificationRequested();
    }

    private OnEmailVerificationRequestedListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnEmailVerificationRequestedListener) {
            mListener = (OnEmailVerificationRequestedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnEmailVerificationRequestedListener");
        }
    }

    // Call this method when you want to open the email verification fragment
    private void requestEmailVerification() {
        if (mListener != null) {
            mListener.onEmailVerificationRequested();
        }
    }

    public static void saveUserData(FirebaseFirestore firestore, Context context, String userId, String username, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);

        firestore.collection("users").document(userId).set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "User data saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
