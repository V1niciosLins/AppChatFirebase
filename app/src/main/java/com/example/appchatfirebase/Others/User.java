package com.example.appchatfirebase.Others;

public class User {
    private final String Uid ,Nome;

    public User(String uid, String nome) {
        Uid = uid;
        Nome = nome;
    }

    public String getUid() {
        return Uid;
    }

    public String getNome() {
        return Nome;
    }

}
