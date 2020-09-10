package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.Signing;
import com.alex.controllers.User;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Auth Frame de login y registro
 */
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
        XButton signin = new XButton("INICIAR SESION", new Color(33,150,243), Color.white);

        // PROPIEDADES
        name.setBounds(0,10,400,90);
        pass.setBounds(0,100,400,90);
        register.setBounds(65, 215, 120, 50);
        signin.setBounds(185, 215, 140, 50);

        // EVENTOS
        ActionListener login = e -> {
            // CREAR CONTROLADOR DE USUARIOS
            User user = new User(name.getData(), name.getData(), pass.getData());
            Signing controller = new Signing(user);

            // CERRAR VENTANA
            if(controller.getVerify()) this.dispose();
        };

        // FRAME DE INICIAR SESION
        ActionListener registryFrame = e -> new com.alex.views.Signing();


        register.onClick(registryFrame);
        signin.onClick(login);

        // AGREGAR
        addComponent(name);
        addComponent(pass);
        addComponent(register);
        addComponent(signin);
    }
}