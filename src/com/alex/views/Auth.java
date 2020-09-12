package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.SigningController;
import com.alex.controllers.UserModelController;

import java.awt.*;
import java.awt.event.ActionListener;

public class Auth extends XFrame {
    private static final long serialVersionUID = 1L;

    public Auth() {
        // CONFIGURAR VENTANA
        setFrame("Login y Registro", 350, 400);
        setHeader("Hola!, ¿Quieres iniciar sesión?", "Si no tienes una cuenta puedes registrarte");
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
        XField name = new XField("Nombre de usuario: ", getWidth());
        XField pass = new XField("Contraseña", getWidth(),true);
        XButton register = new XButton("REGISTRARSE", new Color(0,0,0,0), new Color(80,80,80));
        XButton signin = new XButton("INICIAR SESION");

        // PROPIEDADES
        name.setBounds(0,10,350,90);
        pass.setBounds(0,100,350,90);
        register.setBounds(65, 215, 120, 50);
        signin.setBounds(185, 215, 140, 50);

        // EVENTOS
        ActionListener login = e -> {
            // CREAR CONTROLADOR DE USUARIOS
            UserModelController userController = new UserModelController(name.getData(), name.getData(), pass.getData(), false);
            SigningController controller = new SigningController(userController);

            // CERRAR VENTANA
            if(controller.getVerify()) this.dispose();
        };

        // FRAME DE INICIAR SESION
        ActionListener registryFrame = e -> {
            this.dispose();
            new Signing();
        };

        register.onClick(registryFrame);
        signin.onClick(login);

        // AGREGAR
        addComponent(name);
        addComponent(pass);
        addComponent(register);
        addComponent(signin);
    }
}