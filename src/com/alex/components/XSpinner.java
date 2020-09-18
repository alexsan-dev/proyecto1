package com.alex.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class XSpinner extends JSpinner {
    private Shape shape;
    private boolean onAdd;
    private final SpinnerNumberModel model;
    private int value;
    private final int width;

    public XSpinner(int defValue, int min, int max, int width){
        // MODELO
        this.width = width;
        value = min;
        model = new SpinnerNumberModel(defValue, min, max, 1);
        setModel(model);

        // PROPIEDADES
        setProperties();

        // LISTENER
        addChangeListener(e -> {
            if(!onAdd) {
                value = (int) model.getNumber();
            }
        });
    }

    public int getData(){
        return value;
    }

    public void setRange(int min, int max){
        // LIMPIAR
        onAdd = true;
        model.setMinimum(min);
        model.setMaximum(max);
        onAdd = false;
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
        setPreferredSize(new Dimension(width,getHeight()));
        setForeground(new Color(80, 80, 80));
        setFont(new Font("Lato", Font.BOLD, 17));
    }
}
