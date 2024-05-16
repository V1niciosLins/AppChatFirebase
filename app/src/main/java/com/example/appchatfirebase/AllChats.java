package com.example.appchatfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatfirebase.Others.User;
import com.example.appchatfirebase.databinding.ActivityAllChatsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class AllChats extends AppCompatActivity {
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
private FirebaseFirestore fs = FirebaseFirestore.getInstance();
private ArrayList<User> list ;
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
        binding.floating.shrink();
        binding.LogOut.setVisibility(View.GONE);
        binding.AdicionarContatos.setVisibility(View.GONE);
        binding.floating.setOnClickListener(click->{
            if (binding.floating.isExtended()){
                binding.floating.shrink();
                binding.LogOut.setVisibility(View.GONE);
                binding.AdicionarContatos.setVisibility(View.GONE);
            }else {
                binding.floating.extend();
                binding.LogOut.setVisibility(View.VISIBLE);
                binding.AdicionarContatos.setVisibility(View.VISIBLE);
            }
        });
        list = new ArrayList<>();
        binding.NoContactsText.setVisibility(View.VISIBLE);

        binding.LogOut.setOnClickListener(click->{
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        mAdap adap = new mAdap(this, list);
        binding.Recycler.setLayoutManager(new LinearLayoutManager(this));

        binding.Recycler.setAdapter(adap);
        fs.collection("Users")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            if (!Objects.equals(document.toObject(User.class).getUid(), mAuth.getUid()))
                                list.add(document.toObject(User.class));
                        }
                        adap.Atualize(list);
                    }
                });
    }


}

class mAdap extends RecyclerView.Adapter<mAdap.vh>{
private Context context;
private ArrayList<User> list;

    public mAdap(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public mAdap.vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.all_chats_list_item,null,false);
        return new vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mAdap.vh holder, int position) {
        holder.nome.setText(list.get(position).getNome());
    }

    public void Atualize(ArrayList<User> arrayList){
        list = arrayList;
        notify();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class vh extends RecyclerView.ViewHolder{
        TextView nome;
        public vh(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.ContactName);
        }
    }
}