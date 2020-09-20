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
        setFrame("Login y Registro", 350, 425);
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
        XButton forgot = new XButton("Recuperar", new Color(0,0,0,0), new Color(50, 50, 50));
        XLabel forgotLabel = new XLabel("¿Olvidaste tu contraseña?");

        // PROPIEDADES
        name.setBounds(0,10,350,90);
        pass.setBounds(0,100,350,90);
        register.setBounds(15, 240, 150, 50);
        signing.setBounds(155, 240, 170, 50);
        forgotLabel.setBounds(25, 190, 150, 50);
        forgotLabel.setFont(new Font("Lato", Font.BOLD, 13));
        forgot.setBounds(155, 190, 130, 50);
        forgot.setFont(new Font("Lato", Font.BOLD, 13));

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

        // RECUPERAR PASS
        ActionListener forgotPass = e -> {
            // BUSCAR USER
            String userName = XAlert.showPrompt("Ingresar nombre de usuario: ");
            User tmpUser = signingController.findByUser(userName);

            // MOSTRAR PASS
            if(tmpUser == null) XAlert.showError("Error al buscar", "Usuario no encontrado");
            else XAlert.showAlert("Contraseña", "Tu contraseña es: " + tmpUser.pass);
        };

        register.onClick(registryFrame);
        signing.onClick(login);
        forgot.onClick(forgotPass);

        // AGREGAR
        addComponent(name);
        addComponent(pass);
        addComponent(register);
        addComponent(signing);
        addComponent(forgotLabel);
        addComponent(forgot);
    }
}