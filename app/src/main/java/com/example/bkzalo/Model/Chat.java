package com.example.bkzalo.Model;

public class Chat {

    //fields
    private int sender;
    private int receiver;
    private String message;

    //constructors
    public Chat(){

    }

    public Chat(int sender, int receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    //getters and setters


    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
