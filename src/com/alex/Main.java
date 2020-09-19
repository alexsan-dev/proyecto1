package com.alex;

import com.alex.controllers.SigningController;
import com.alex.views.Auth;

import javax.swing.*;

class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // CONTROLADOR DE USUARIOS
        SigningController signingController = new SigningController();

        // INICIAR AUTH
        new Auth(signingController);
    }
}