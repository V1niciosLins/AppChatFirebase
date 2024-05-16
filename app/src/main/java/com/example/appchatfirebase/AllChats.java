package com.example.appchatfirebase;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appchatfirebase.databinding.ActivityAllChatsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AllChats extends AppCompatActivity {
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
ActivityAllChatsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAllChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.floating.setOnClickListener(click->{
            if (binding.floating.isExtended()){
                binding.floating.shrink();
            }else {
                binding.floating.extend();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
    }
}