package com.alex;

import javax.swing.*;

import com.alex.controllers.UserModelController;
import com.alex.views.Auth;
import com.alex.views.MainMenu;

class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // INICIAR AUTH
        new MainMenu(new UserModelController("admin", "admin", "admin"));
    }
}