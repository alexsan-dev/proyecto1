package com.alex.controllers;

import com.alex.views.MainMenu;

public class Signing {
    public User user;
    private User defUser;
    private Boolean verify;

    public Signing(User user){
        this.user = user;
        this.defUser = new User("admin", "admin", "admin");

        // VERIFICAR Y CREAR
        verify();
        createFrame();
    }

    public boolean getVerify(){
        return verify;
    }

    public void verify(){
        // VERIFICAR PRIMERO USUARIO POR DEFECTO
        if(user.name.equals(defUser.name) && user.pass.equals(defUser.pass)) verify = true;
        else verify = false;
    }

    public void createFrame(){
        // CREAR VENTANA
        if(verify) new MainMenu(user);
    }
}
