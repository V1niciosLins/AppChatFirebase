package com.example.appchatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appchatfirebase.databinding.ActivityMainBinding;
import com.example.appchatfirebase.databinding.ActivitySigninBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
ActivitySigninBinding binding;
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(click->{
            String email = binding.emailEditText.getText().toString();
            String senha = binding.passwordEditText.getText().toString();
            if (email.isEmpty() || senha.isEmpty()) return;

            mAuth.createUserWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Signin.this, MainActivity.class));
                        } else{
                            Toast.makeText(this, "Erro ao criar conta:\n"
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }
}