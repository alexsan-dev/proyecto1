package com.alex.controllers;

import com.alex.models.UserModel;

public class UserModelController implements UserModel {
    public String user;
    public String name;
    public String pass;
    public Boolean isNew;

    public UserModelController(String user, String name, String pass, Boolean isNew){
        this.user = user;
        this.name = name;
        this.pass = pass;
        this.isNew = isNew;
    }
}
