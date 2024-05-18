package com.example.appchatfirebase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatfirebase.Others.Message;
import com.example.appchatfirebase.Others.User;
import com.example.appchatfirebase.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Chat extends AppCompatActivity {
ActivityChatBinding binding;
ArrayList<Message> conversa = new ArrayList<>();
String currentContactUid = "Error";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        if (b!= null){
            currentContactUid = b.getString("UID");
            binding.Toolbar.setTitle(b.getString("Name"));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MM), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        mAdap2 adap = new mAdap2(this, conversa);

        /*FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Conversas")
                .document(currentContactUid)
                .collection("menssagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        QuerySnapshot qs = task.getResult();
                        if (qs!=null){
                            for (DocumentSnapshot doc: qs){
                                conversa.add(doc.toObject(Message.class));
                            }
                            adap.Atualize(conversa);
                        }


                    }
                });*/

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Conversas")
                .document(currentContactUid)
                .collection("menssagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null){
                            List<DocumentChange> documentChanges = value.getDocumentChanges();
                            for (DocumentChange doc: documentChanges){
                                if (doc.getType()== DocumentChange.Type.ADDED){
                                    conversa.add(doc.getDocument().toObject(Message.class));
                                    adap.Atualize(conversa);
                                    binding.recyclerViewChat.smoothScrollToPosition(conversa.size());
                                }
                            }
                        } else {
                            Toast.makeText(Chat.this, "É null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        binding.recyclerViewChat.setAdapter(adap);




        binding.btnSendMessage.setOnClickListener(click->{
            enviarMenssagem(binding.editTextMessage);
        });

        if (!conversa.isEmpty()) binding.BVAB.setVisibility(View.GONE);
    }

    void enviarMenssagem(EditText editText){

        if (editText.getText().toString().isEmpty()) return;
        long timestamp = System.currentTimeMillis();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Conversas")
                .document(currentContactUid)
                .collection("menssagens")
                .add(new Message(editText.getText().toString(),true,timestamp))
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       Toast.makeText(this, "Menssagem enviada", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(this, "Menssagem não enviada.\nErro estranho.", Toast.LENGTH_SHORT).show();
                   }
                });

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentContactUid)
                .collection("Conversas")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("menssagens")
                .add(new Message(editText.getText().toString(),false,timestamp))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Menssagem Chegou no contato", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Menssagem não chegou no contato.\nErro estranho.", Toast.LENGTH_SHORT).show();
                    }
                });

        editText.getText().clear();
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

    public void addMenssage(Message m){
        list.add(m);
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