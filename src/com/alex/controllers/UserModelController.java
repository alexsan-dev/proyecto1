package com.alex.controllers;

import com.alex.models.UserModel;

public class UserModelController implements UserModel {
    public String user;
    public String name;
    public String pass;

    public UserModelController(String user, String name, String pass){
        this.user = user;
        this.name = name;
        this.pass = pass;
    }
}
