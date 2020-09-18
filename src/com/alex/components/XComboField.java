package com.alex.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class XComboField extends JPanel {
    private final XComboBox combo;
    private final XLabel titleLabel;

    public XComboField(String label, String[] items, int width, String defValue, boolean editable){
        // LAYOUT
        setLayout(null);

        // COMPONENT
        titleLabel = new XLabel(label);
        combo = new XComboBox(items, defValue);

        // LISTA
        setProperties(width, editable);
    }

    public String[] toArray(){
        return combo.toArray();
    }

    public void clear(){
        combo.clear();
    }

    public void addItem(String item){
        combo.addItem(item);
    }

    public void setListener(ActionListener e){
        combo.setListener(e);
    }

    public int getIndex(){
        return combo.getIndex();
    }

    public void setProperties(int width, boolean editable){
        // PROPIEDADES
        setBackground(new Color(220, 220, 220));

        // EDITABLE
        combo.setEnabled(editable);

        // POSICIONES
        titleLabel.setBounds(0, 0, width, 40);
        combo.setBounds(0, 40, width, 50);

        // AGREGAR
        add(titleLabel);
        add(combo);
    }

    public String getData(){
        return combo.getData();
    }
}
