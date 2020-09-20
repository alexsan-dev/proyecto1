package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.SigningController;
import com.alex.data.User;

import java.awt.*;
import java.awt.event.ActionListener;

public class Signing extends XFrame {
    private static final long serialVersionUID = 1L;
    private final SigningController signingController;

    public Signing(SigningController signingController) {
        setFrame("Registro de usuarios", 350, 580);
        setHeader("Gracias por crear una cuenta", "Inicia sesión si ya tienes una cuenta.");

        this.signingController = signingController;
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

        signingBtn.setBounds(12, 395, 150, 50);
        registerBtn.setBounds(155, 395, 170, 50);

        // EVENTOS
        ActionListener signing = (e) ->{
            new Auth(signingController);
            this.dispose();
        };

        ActionListener createUser = (e) -> {
            // CREAR CONTROLADOR DE USUARIOS
            if(!pass.getData().equals(confirmPass.getData())) XAlert.showError("Error al crear", "Las contraseñas no coinciden.");
            else {
                User userC = new User(user.getData(), name.getData(), pass.getData(), true);

                signingController.addUser(userC);
            }
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