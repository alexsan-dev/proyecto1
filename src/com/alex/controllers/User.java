package com.alex.controllers;

public class User implements com.alex.models.User {
    public String user;
    public String name;
    public String pass;

    public User(String user, String name, String pass){
        this.user = user;
        this.name = name;
        this.pass = pass;
    }
}
