package com.alex.components;

import javax.swing.*;

public class XField extends JPanel {
    public JTextField field;
    public XLabel label;
    private boolean isPassword;

    public XField(String text, int width){
        setLayout(null);
        setSize(width,90);

        // COMPONENTES
        field = new XInput();
        label = new XLabel(text);

        // AGREGAR
        setComponents();
    }

    public XField(String text, int width, boolean isPassword){
        setLayout(null);
        setSize(width,90);
        this.isPassword = isPassword;

        if(isPassword) {
            // COMPONENTES
            field = new XPass();
            label = new XLabel(text);

            // AGREGAR
            setComponents();
        }
    }

    public void setComponents(){
        // PROPIEDADES
        label.setBounds(25, 0, getWidth() - 50, 40);
        field.setBounds(25, 40,  getWidth() - 50, 50);

        // AGREGAR
        add(label);
        add(field);
    }

    public String getData(){
        // VERIFICAR SI ES UNA PASSWORD
        if(isPassword){
            JPasswordField tmpPass = (JPasswordField) field;
            return String.valueOf(tmpPass.getPassword());
        } else return field.getText();
    }
}
