package com.alex.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class XComboBox extends JComboBox<String> {
    private Shape shape;
    private String text;
    private boolean onStay;

    public XComboBox(String[] items, String defValue){
        // AGREGAR ITEMS
        text = items[0];
        for (String item : items) addItem(item);
        setSelectedItem(defValue);

        // LISTENER AL CAMBIAR
        addActionListener(e -> {
            if(!onStay) text = Objects.requireNonNull(getSelectedItem()).toString();
        });
        setProperties();
    }

    public void clear(){
        onStay = true;
        removeAllItems();
        onStay = false;
    }

    public String getData(){
        return text;
    }

    public int getIndex(){
        return getSelectedIndex();
    }

    public String[] toArray(){
        String[] out = new String[getItemCount()];
        for(int index = 0; index < out.length; index++) out[index] = getItemAt(index);
        return out;
    }

    public void setListener(ActionListener e){
        addActionListener(e);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(Color.white);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds()))
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        return shape.contains(x, y);
    }

    private void setProperties(){
        // PROPIEDADES
        setForeground(new Color(80, 80, 80));
        setFont(new Font("Lato", Font.BOLD, 17));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
