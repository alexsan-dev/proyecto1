package com.alex.components;

import javax.swing.*;

public class XField extends JPanel {
    public JTextField field;
    public XLabel label;
    private int width;
    private boolean isPassword;

    public XField(String text, int width){
        this.width = width;

        // COMPONENTES
        field = new XInput();
        label = new XLabel(text);

        // AGREGAR
        setComponents(label, field);
    }

    public XField(String text, int width, boolean isPassword){
        this.width = width;
        this.isPassword = isPassword;

        if(isPassword) {
            // COMPONENTES
            field = new XPass();
            label = new XLabel(text);

            // AGREGAR
            setComponents(label, field);
        }
    }

    public void setComponents(JLabel labelS, JTextField fieldS){
        // PROPIEDADES
        labelS.setBounds(25, 0, width - 50, 40);
        fieldS.setBounds(25, 40,  width - 50, 50);

        // AGREGAR
        add(labelS);
        add(fieldS);
    }

    public String getData(){
        // VERIFICAR SI ES UNA PASSWORD
        if(isPassword){
            JPasswordField tmpPass = (JPasswordField) field;
            return String.valueOf(tmpPass.getPassword());
        } else return field.getText();
    }
}
