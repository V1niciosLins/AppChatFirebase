package com.example.appchatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appchatfirebase.databinding.ActivityMainBinding;
import com.example.appchatfirebase.databinding.ActivitySigninBinding;
import com.example.appchatfirebase.databinding.AlertBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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
            String email = binding.emailEditText.getText().toString().trim();
            String senha = binding.passwordEditText.getText().toString();
            if (email.isEmpty() || senha.isEmpty()) return;
            binding.Progress.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Signin.this, MainActivity.class));
                        } else{
                            String exception = getException(task);

                            View v = getLayoutInflater().
                                    inflate(R.layout.alert,null,false);
                            ((TextView) v.findViewById(R.id.AlertText)).setText(exception);
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setView(v);
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.show();
                            binding.Progress.setVisibility(View.GONE);

                        }
                    });

        });
    }

    @NonNull
    private static String getException(Task<AuthResult> task) {
        String exception ="Erro desconhecido.\nTente novamente";
        if (task.getException() instanceof FirebaseAuthWeakPasswordException)
            exception="Senha fraca, precisa conter 6 caracteres ou mais.";
        if (task.getException() instanceof FirebaseAuthUserCollisionException)
            exception="Já existe um usuário registrado com esta conta.";
        if (task.getException() instanceof FirebaseNetworkException)
            exception="Erro de internet";
        return exception;
    }
}