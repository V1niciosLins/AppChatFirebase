package com.example.appchatfirebase;

import android.content.Context;
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

import com.example.appchatfirebase.Others.Message;
import com.example.appchatfirebase.Others.User;
import com.example.appchatfirebase.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class Chat extends AppCompatActivity {
ActivityChatBinding binding;
ArrayList<Message> conversa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        conversa = new ArrayList<>(
                Arrays.asList(
                        new Message("Olá, esta é uma mensagem de teste",true),
                        new Message("Sério? gostei muito desta mensagem de teste",false)
                )
        );
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        mAdap2 adap = new mAdap2(this, conversa);
        binding.recyclerViewMessages.setAdapter(adap);
    }
}

class mAdap2 extends RecyclerView.Adapter<mAdap2.vh2>{
    private Context context;
    private ArrayList<Message> list;

    public mAdap2(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public mAdap2.vh2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chat_item,null,false);
        return new vh2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mAdap2.vh2 holder, int position) {
        if (list.get(position).isMine()){
            holder.Mensagem_Minha.setVisibility(View.VISIBLE);
            holder.Mensagem_Sua.setVisibility(View.GONE);

            holder.Mensagem_Minha.setText(list.get(position).getMensagem());
        } else {
            holder.Mensagem_Minha.setVisibility(View.GONE);
            holder.Mensagem_Sua.setVisibility(View.VISIBLE);

            holder.Mensagem_Sua.setText(list.get(position).getMensagem());
        }
    }

    public void Atualize(ArrayList<Message> arrayList){
        list = arrayList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class vh2 extends RecyclerView.ViewHolder{
        TextView Mensagem_Minha, Mensagem_Sua;
        public vh2(@NonNull View itemView) {
            super(itemView);
            Mensagem_Minha = itemView.findViewById(R.id.textViewMessage);
            Mensagem_Sua = itemView.findViewById(R.id.textViewMessage2);
        }
    }
}