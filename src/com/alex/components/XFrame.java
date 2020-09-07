package com.alex.components;

import java.awt.*;
import javax.swing.*;

public class XFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    public JPanel contentPanel;

    /**
     * @return
     */
    public XFrame() {
        // AGREGAR ICONO Y COLOR
        setLayout(null);
        setBackground(new Color(238, 238, 238));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // PANEL DE CONTENIDO
        contentPanel = new JPanel(null);

        // AGREGAR DIMENSIONES
        setResizable(false);
        setVisible(true);
    }

    public void setFrame(String name, int width, int height) {
        setTitle(name);
        setSize(width, height);
        contentPanel.setOpaque(true);
        contentPanel.setVisible(true);
        contentPanel.setBackground(new Color(220, 220, 220));
        contentPanel.setBounds(0, 80, getWidth(), height - 80);
    }

    public void setHeader(String name, String description) {
        // COMPONENTES
        XHeader header = new XHeader(name, description, getWidth());

        // ASIGNAR
        add(header);
        add(contentPanel);
    }

    public void addComponent(Component component){
        contentPanel.add(component);
    }
}