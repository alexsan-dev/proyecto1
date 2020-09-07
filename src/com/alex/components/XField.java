package com.alex.components;

import javax.swing.*;

public class XField extends JPanel {
    public JTextField field;
    public XLabel label;

    public XField(String text){
        // COMPONENTES
        field = new XInput();
        label = new XLabel(text);

        // AGREGAR
        setComponents(label, field);
    }

    public XField(String text, boolean isPassword){
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
        labelS.setBounds(50, 0, 300, 40);
        fieldS.setBounds(50, 40, 300, 50);

        // AGREGAR
        add(labelS);
        add(fieldS);
    }
}
