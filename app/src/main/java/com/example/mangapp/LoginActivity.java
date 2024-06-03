package com.example.mangapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    //Declaracion de las variables para los controles usados
    TextView textViewSign;
    EditText editTextEmail, editTextPassword;
    Button buttonSend;

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
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Iniciar la instancia de FB
        mAuth = FirebaseAuth.getInstance();

        //Asignacion de las variables a los controles
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSend = findViewById(R.id.buttonSend);
        textViewSign = findViewById(R.id.textViewSign);

        //Funcion onClick para el boton
        buttonSend.setOnClickListener((View v) -> {
            //Recogida de credenciales
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            //Control de la entrada
            if(!emailValid(email)) Toast.makeText(LoginActivity.this,"Invalid Email", Toast.LENGTH_SHORT).show();
            if(!passwordValid(password))Toast.makeText(LoginActivity.this,"Invalid Password", Toast.LENGTH_SHORT).show();
        });

        textViewSign.setOnClickListener((View v) -> openSignInDialog());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public static boolean emailValid(String email){
            return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // 1 char especial
                    "(?=\\S+$)" +            // sin espacios
                    ".{6,}" +                // minimo 6 caracteres
                    "$");

    public static boolean passwordValid(String pass){
        return pass != null && PASSWORD_PATTERN.matcher(pass).matches();
    }

    public void login(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d("EmailPass","signInEmail:Success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }else{
                Log.d("EmailPass","signInEmail:Failure");
                Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSignInDialog(){
        SignInFragment signInFragment = new SignInFragment();
        signInFragment.show(getSupportFragmentManager(),"SignInFragment");
    }

    public void updateUI(FirebaseUser user){

    }
}