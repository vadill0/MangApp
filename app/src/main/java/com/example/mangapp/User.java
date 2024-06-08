package com.example.mangapp;

public class User {
    public String username;
    public String userId;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }
}