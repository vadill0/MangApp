package com.example.mangapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    //Declaracion de las variables para los controles usados
    EditText editTextEmail, editTextPassword;
    Button buttonSend;

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

        //Asignacion de las variables a los controles
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSend = findViewById(R.id.buttonSend);

        //Funcion onClick para el boton
        buttonSend.setOnClickListener((View v) -> {
            //Recogida de credenciales
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            //Control de la entrada
            if(!emailValid(email)) Toast.makeText(LoginActivity.this,"Invalid Email", Toast.LENGTH_SHORT).show();
            if(!passwordValid(password))Toast.makeText(LoginActivity.this,"Invalid Password", Toast.LENGTH_SHORT).show();
        });
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
}