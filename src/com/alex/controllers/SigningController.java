package com.alex.controllers;

import com.alex.views.MainMenu;

public class SigningController {
    public UserModelController userController;
    private UserModelController defUserController;
    private Boolean verify;
    private String pass;

    public SigningController(UserModelController userController){
        this.userController = userController;
        this.defUserController = new UserModelController("admin", "admin", "admin", false);

        // VERIFICAR Y CREAR
        verify();
        createFrame();
    }

    public SigningController(UserModelController userController, String pass){
        this.userController = userController;
        this.pass = pass;
        this.defUserController = new UserModelController("admin", "admin", "admin", false);

        // VERIFICAR Y CREAR
        verify();
        createFrame();
    }

    public boolean getVerify(){
        return verify;
    }

    public void verify(){
        verify = userController.isNew ? userController.pass.equals(this.pass) : userController.name.equals(defUserController.name) && userController.pass.equals(defUserController.pass);
    }

    public void createFrame(){
        // CREAR VENTANA
        if(verify) new MainMenu(userController);
    }
}
