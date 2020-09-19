package com.alex.data;

import com.alex.models.UserModel;

public class User implements UserModel {
    public String user;
    public String name;
    public String pass;
    public Boolean isNew;

    public User(String user, String name, String pass, Boolean isNew){
        this.user = user;
        this.name = name;
        this.pass = pass;
        this.isNew = isNew;
    }
}
