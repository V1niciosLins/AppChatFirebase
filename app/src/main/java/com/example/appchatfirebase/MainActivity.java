package com.example.appchatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.ButtonLogin.setOnClickListener(click->{
            Login(binding.EditTextEmail,binding.EditTextPassword);
            binding.LoginProgress.setVisibility(View.VISIBLE);
        });

        binding.ButtonSigin.setOnClickListener(click->{
            startActivity(new Intent(this, Signin.class));
        });
    }

    void Login(EditText edtEmail, EditText edtSenha)
    {
        if (edtEmail.getText().toString().isEmpty() ||edtEmail.getText().toString().isEmpty()) return;
        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this, AllChats.class)); finish();
                        } else {
                            String exception = getException(task);

                            View v = getLayoutInflater().
                                    inflate(R.layout.alert,null,false);
                            ((TextView) v.findViewById(R.id.AlertText)).setText(exception);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setView(v);
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.show();
                            binding.LoginProgress.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @NonNull
    private static String getException(Task<AuthResult> task) {
        String exception ="Erro desconhecido.\nTente novamente.";
        if (task.getException() instanceof FirebaseAuthInvalidUserException)
            exception="Usuário não existe ou está desativado.";
        if (task.getException() instanceof FirebaseAuthEmailException)
            exception="Email fornecido é inválido.";
        if (task.getException() instanceof FirebaseNetworkException)
            exception="Erro de internet";
        return exception;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null) return;
        currentUser.reload().addOnCompleteListener(task->{
            if (task.isSuccessful()){
                if (currentUser.getEmail() == null) return;
                startActivity(new Intent(this, AllChats.class));
                finish();
            }
        });

    }
}