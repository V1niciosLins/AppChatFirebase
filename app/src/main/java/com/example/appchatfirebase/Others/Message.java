package com.example.appchatfirebase.Others;

public class Message {
    private String Mensagem;
    private boolean isMine;
    private long timestamp;

    public Message(String mensagem, boolean isMine, long timestamp) {
        Mensagem = mensagem;
        this.isMine = isMine;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
