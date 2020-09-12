package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.SigningController;
import com.alex.controllers.UserModelController;

import java.awt.*;
import java.awt.event.ActionListener;

public class Signing extends XFrame {
    private static final long serialVersionUID = 1L;

    public Signing() {
        setFrame("Registro de usuarios", 350, 580);
        setHeader("Gracias por crear una cuenta", "Inicia sesión si ya tienes una cuenta.");
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
        XField user = new XField("Usuario: ", getWidth());
        XField name = new XField("Nombre: ", getWidth());
        XField pass = new XField("Contraseña", getWidth(), true);
        XField confirmPass = new XField("Confirmar contraseña", getWidth(), true);
        XButton signingBtn = new XButton("INICIAR SESIÓN", new Color(0,0,0,0), new Color(80,80,80));
        XButton registerBtn = new XButton("REGISTRARSE");

        // PROPIEDADES
        user.setBounds(0,10,350,90);
        name.setBounds(0,100,350,90);
        pass.setBounds(0,190,350,90);
        confirmPass.setBounds(0,280,350,90);

        signingBtn.setBounds(55, 395, 130, 50);
        registerBtn.setBounds(185, 395, 140, 50);

        // EVENTOS
        ActionListener signing = (e) ->{
            new Auth();
            this.dispose();
        };

        ActionListener createUser = (e) -> {
            // CREAR CONTROLADOR DE USUARIOS
            UserModelController userController = new UserModelController(user.getData(), name.getData(), pass.getData(), true);
            SigningController controller = new SigningController(userController, confirmPass.getData());

            // CERRAR VENTANA
            if(controller.getVerify()) this.dispose();
        };

        // LISTENERS
        signingBtn.onClick(signing);
        registerBtn.onClick(createUser);

        // AGREGAR
        addComponent(user);
        addComponent(name);
        addComponent(pass);
        addComponent(confirmPass);
        addComponent(registerBtn);
        addComponent(signingBtn);
    }
}