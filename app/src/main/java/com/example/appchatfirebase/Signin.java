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

import com.example.appchatfirebase.Others.User;
import com.example.appchatfirebase.databinding.ActivityMainBinding;
import com.example.appchatfirebase.databinding.ActivitySigninBinding;
import com.example.appchatfirebase.databinding.AlertBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;

public class Signin extends AppCompatActivity {
ActivitySigninBinding binding;
private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
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
            String nome = binding.nameEditText.getText().toString();
            if (email.isEmpty() || senha.isEmpty() || nome.isEmpty()) return;
            binding.Progress.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            User user = new User(FirebaseAuth.getInstance().getUid(),nome);
                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .set(user)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()){
                                            Toast.makeText(this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Signin.this, MainActivity.class)); finish();
                                        }else{
                                            Toast.makeText(this, "Um erro estranho ocorreu.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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