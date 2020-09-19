package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.SigningController;
import com.alex.data.User;

import java.awt.*;
import java.awt.event.ActionListener;

public class Auth extends XFrame {
    private static final long serialVersionUID = 1L;
    private final SigningController signingController;

    public Auth(SigningController signingController) {
        // CONFIGURAR VENTANA
        setFrame("Login y Registro", 350, 400);
        setHeader("Hola!, ¿Quieres iniciar sesión?", "Si no tienes una cuenta puedes registrarte");

        this.signingController = signingController;
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
        XField name = new XField("Nombre de usuario: ", getWidth());
        XField pass = new XField("Contraseña", getWidth(),true);
        XButton register = new XButton("REGISTRARSE", new Color(0,0,0,0), new Color(80,80,80));
        XButton signing = new XButton("INICIAR SESIÓN");

        // PROPIEDADES
        name.setBounds(0,10,350,90);
        pass.setBounds(0,100,350,90);
        register.setBounds(15, 215, 150, 50);
        signing.setBounds(155, 215, 170, 50);

        // EVENTOS
        ActionListener login = e -> {
            // CREAR CONTROLADOR DE USUARIOS
            User user = new User(name.getData(), name.getData(), pass.getData(), false);
            signingController.addUser(user);
        };

        // FRAME DE INICIAR SESIÓN
        ActionListener registryFrame = e -> {
            new Signing(signingController);
            this.dispose();
        };

        register.onClick(registryFrame);
        signing.onClick(login);

        // AGREGAR
        addComponent(name);
        addComponent(pass);
        addComponent(register);
        addComponent(signing);
    }
}