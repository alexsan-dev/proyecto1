package com.alex;

import com.alex.controllers.ClientController;
import com.alex.views.Auth;
import com.alex.views.ManageClients;

import javax.swing.*;

class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // INICIAR AUTH
        new Auth();
    }
}