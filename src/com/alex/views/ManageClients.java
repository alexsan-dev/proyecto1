package com.alex.views;

import com.alex.components.XFrame;
import com.alex.models.UserModel;

public class ManageClients extends XFrame {
    public ManageClients(UserModel userModel){
        // CONFIGURAR VENTANA
        setFrame("Administracion de clientes", 350, 400);
        setHeader("Hola!, ¿Quieres iniciar sesión?", "Si no tienes una cuenta puedes registrarte");
    }
}
