package com.example.bkzalo.Model;

public class User {

    //fields
    private int ID;
    private String username;
    private String displayname;
    private String password;

    // constructors
    public User(){

    }

    public User(int ID, String username, String password, String displayname) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.displayname = displayname;
    }

    // getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
