package com.example.mangapp.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.mangapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements SignUpFragment.OnEmailVerificationRequestedListener{
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "EmailPass";
    View[] mainViews; //Views de la Activity
    View fragmentContainer; //Frame container

    //Declaracion de las variables para los controles usados
    TextView textViewSign, textViewForgotPass, textView;//debug
    EditText editTextEmail, editTextPassword;
    Button buttonSend, buttonGoogle;

    @Override
    protected void onStart() {
        super.onStart();
        //Comprobar si el usuario ya esta logeado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)updateUI(currentUser);//PoC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Iniciar la instancia de FB
        mAuth = FirebaseAuth.getInstance();

        //Configuracion Google SignIn
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Asignacion de las variables a los controles
        fragmentContainer = findViewById(R.id.fragment_container);
        editTextEmail = findViewById(R.id.editTextEmailSignIn);
        editTextPassword = findViewById(R.id.editTextPasswordSignIn);
        buttonSend = findViewById(R.id.buttonSignIn);
        textViewSign = findViewById(R.id.textViewSign);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        textViewForgotPass = findViewById(R.id.textViewForgotPass);

        //Views de la main activity
        mainViews = new View[]{editTextEmail, editTextPassword,
                buttonSend, buttonGoogle,
                textViewSign, textViewForgotPass
        };


        textView = findViewById(R.id.textView);//debug

        //Funcion onClick para el boton
        buttonSend.setOnClickListener((View v) -> {
            //Recogida de credenciales
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            //Control de la entrada
            if(emailValidation(email)){
                Toast.makeText(SignInActivity.this,"Invalid Email", Toast.LENGTH_SHORT).show();
            } else if(passwordValidation(password)) {
                Toast.makeText(SignInActivity.this,"Invalid Password", Toast.LENGTH_SHORT).show();
            }else{
                signIn(email, password);
            }
        });

        buttonGoogle.setOnClickListener((View v)-> signInWithGoogle());

        textViewSign.setOnClickListener((View v) -> openSignUpFragment());
        textViewForgotPass.setOnClickListener((View v) -> openForgotPassFragment());

        //Ocultacion de botones de la activity en los fragments
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 1) {
                    fm.popBackStack();
                } else {
                    // Show all views again when no fragment is in the stack
                    for (View view : mainViews) {
                        view.setVisibility(View.VISIBLE);
                    }
                    fragmentContainer.setVisibility(View.GONE);
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        // Remove the callback to allow default back press handling
                        setEnabled(false);
                        onBackPressedDispatcher.onBackPressed();
                    }
                }
            }
        });
    }

    public static boolean emailValidation(String email){
            return email == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // 1 char especial
                    "(?=\\S+$)" +            // sin espacios
                    ".{6,}" +                // minimo 6 caracteres
                    "$");

    public static boolean passwordValidation(String pass){
        return pass == null || !PASSWORD_PATTERN.matcher(pass).matches();
    }

    public void signIn(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(TAG,"signInEmail:Success");
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null && user.isEmailVerified()){
                    Toast.makeText(SignInActivity.this,"Sign in successful",Toast.LENGTH_SHORT).show();
                    updateUI(user);
                }else{
                    Toast.makeText(SignInActivity.this,"You need to verify your email to sign in",Toast.LENGTH_SHORT).show();
                    openEmailVerificationFragment(user);
                }
            }else{
                Log.d(TAG,"signInEmail:Failure");
                Toast.makeText(SignInActivity.this,"Sign in failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException.class);
                fbAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.e(TAG,"Google sign in failed", e);
            }
        }
    }

    private void fbAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG,"signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(Objects.requireNonNull(user));
                    }else{
                        Log.e(TAG,"signInWithCredential:failure",task.getException());
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Carga y ocultacion de los fragment
    private void loadFragment(Fragment fragment) {
        // Hide all views except the fragment container
        for (View view : mainViews) {
            view.setVisibility(View.GONE);
        }
        fragmentContainer.setVisibility(View.VISIBLE);

        // Create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    private void openSignUpFragment(){
        loadFragment(new SignUpFragment());
    }

    private void openForgotPassFragment(){
        loadFragment(new ForgotPasswordFragment());
    }

    private void openEmailVerificationFragment(FirebaseUser user){
        loadFragment(new EmailVerificationFragment(user));
    }

    @Override
    public void onEmailVerificationRequested() {
        // Perform fragment transaction to open the email verification fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EmailVerificationFragment(mAuth.getCurrentUser()))
                .addToBackStack(null)
                .commit();
    }

    public void updateUI(FirebaseUser user){
        textView.setText(user.getEmail());//debug
    }
}