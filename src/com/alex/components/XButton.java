package com.alex.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class XButton extends JButton {
    private Shape shape;
    Color background, color;

    public XButton(String text){
        // GLOBAL
        super(text);
        this.color = new Color(255, 255, 255);
        this.background = new Color(33,150,243);

        // PROPIEDADES
        setProperties();
    }


    public XButton(String text, Color background, Color color){
        // GLOBAL
        super(text);
        this.color = color;
        this.background = background;

        // PROPIEDADES
        setProperties();
    }

    public void onClick(ActionListener action){
        this.addActionListener(action);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(background);
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(background);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds()))
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        return shape.contains(x, y);
    }

    private void setProperties(){
        // PROPIEDADES
        setBackground(new Color(0,0,0,0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(color);
        setFont(new Font("Lato", Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
