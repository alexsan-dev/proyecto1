package com.alex.components;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class XPass extends JPasswordField {
    private Shape shape;

    public XPass() {
        setProperties(this);
        setBackground(new Color(0,0,0,0));
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

    private void setProperties(JTextComponent component){
        // PROPIEDADES
        component.setForeground(new Color(80, 80, 80));
        component.setFont(new Font("Lato", Font.BOLD, 17));
        component.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
