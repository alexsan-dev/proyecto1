package com.alex.views;

import com.alex.components.XFrame;
import com.alex.models.User;

public class ManageClients extends XFrame {
    public ManageClients(User user){
        // CONFIGURAR VENTANA
        setFrame("Administracion de clientes", 350, 400);
        setHeader("Hola!, ¿Quieres iniciar sesión?", "Si no tienes una cuenta puedes registrarte");
    }
}
