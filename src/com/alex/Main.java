package com.alex;

import com.alex.controllers.SigningController;
import com.alex.views.Auth;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

class Main {
    public static void main(String[] args) {
        // LOOK AND FEEL
        FlatLightLaf.install();

        // CONTROLADOR DE USUARIOS
        SigningController signingController = new SigningController();

        // INICIAR AUTH
        new Auth(signingController);
    }
}