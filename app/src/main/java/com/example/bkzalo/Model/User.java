package com.example.bkzalo.Model;

public class User {

    //fields
    private int ID;
    private String username;
    private String displayname;
    private String password;
    private String status;
    private String imageURL;

    // constructors
    public User(){

    }

    public User(int ID, String username, String password, String displayname, String status, String imageURL) {
        this.ID = ID;
        this.username = username;
        this.displayname = displayname;
        this.password = password;
        this.status = status;
        this.imageURL = imageURL;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
