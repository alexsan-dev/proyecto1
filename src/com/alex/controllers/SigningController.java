package com.alex.controllers;

import com.alex.views.MainMenu;

public class SigningController {
    public UserModelController userController;
    private UserModelController defUserController;
    private Boolean verify;
    private Boolean isNew;

    public SigningController(UserModelController userController, Boolean isNew){
        this.userController = userController;
        this.isNew = isNew;
        this.defUserController = new UserModelController("admin", "admin", "admin");

        // VERIFICAR Y CREAR
        verify();
        createFrame();
    }

    public boolean getVerify(){
        return verify;
    }

    public void verify(){
        // VERIFICAR SI SE CREARA UN USUARIO
        if(isNew) verify = true;
        else {
            // VERIFICAR PRIMERO USUARIO POR DEFECTO
            if (userController.name.equals(defUserController.name) && userController.pass.equals(defUserController.pass))
                verify = true;
            else verify = false;
        }
    }

    public void createFrame(){
        // CREAR VENTANA
        if(verify) new MainMenu(userController);
    }
}
