package com.alex.components;

import javax.swing.*;

public class XAlert {
    public static void showError(String title, String text){
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.ERROR_MESSAGE);
    }
    public static String showPrompt(String title){
        return JOptionPane.showInputDialog(null, title, "");
    }
    public static void showAlert(String title, String text){
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
    }
    public static int showConfirm(String text){
        return JOptionPane.showConfirmDialog(null, text);
    }
}
