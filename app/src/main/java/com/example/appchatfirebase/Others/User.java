package com.example.appchatfirebase.Others;

public class User {
    private String uid ,nome;

    public User(String uid, String nome) {
        this.uid = uid;
        this.nome = nome;
    }

    public User() {
        super();
    }

    public String getUid() {
        return uid;
    }

    public String getNome() {
        return nome;
    }

}
