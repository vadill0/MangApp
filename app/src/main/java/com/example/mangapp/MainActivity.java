package com.example.mangapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mangapp.ApiResponse.ResponseModelMangaList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        textView = findViewById(R.id.textView2);
//        Ejemplo de llamada a la API
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<ResponseModelMangaList> call = apiService.getMangaList("One Piece");
//
//        call.enqueue(new Callback<ResponseModelMangaList>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseModelMangaList> call, @NonNull Response<ResponseModelMangaList> response) {
//                if (response.isSuccessful()) {
//                    ResponseModelMangaList responseModel = response.body();
//                    textView.setText(Objects.requireNonNull(responseModel).toString());
//                } else {
//                    // Handle the error
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseModelMangaList> call, @NonNull Throwable t) {
//                // Handle the failure
//            }
//        });

    }
}