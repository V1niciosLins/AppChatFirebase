package com.example.appchatfirebase.Others;

public class Message {
    private String Mensagem;
    private boolean isMine;

    public Message(String mensagem, boolean isMine) {
        Mensagem = mensagem;
        this.isMine = isMine;
    }

    public Message() {
        super();
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
