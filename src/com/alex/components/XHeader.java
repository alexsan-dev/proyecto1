package com.alex.components;

import javax.swing.*;
import java.awt.*;

/**
 * Header
 */
public class XHeader extends JPanel {

    private static final long serialVersionUID = 1L;

    public XHeader(String name, String description, int width) {
        // PROPIEDADES
        setLayout(new GridLayout(2, 1));
        setBounds(0,0,width,80);
        setBackground(new Color(38, 50, 56));
        setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        setMaximumSize(new Dimension(width, 80));

        // LABELS
        JLabel title = getLabel(name, false);
        JLabel subtitle = getLabel(description, true);

        // ASIGNAR
        add(title);
        add(subtitle);
    }

    public JLabel getLabel(String text, boolean subtitle) {
        // CREAR
        JLabel title = new JLabel(text);
        Color color = subtitle ? new Color(200, 200, 200) : new Color(255, 255, 255);

        // PROPIEDADES
        title.setOpaque(true);
        title.setBackground(new Color(38, 50, 56));
        title.setForeground(color);
        title.setFont(new Font("Lato", Font.BOLD, subtitle ? 15 : 18));

        // RETORNAR
        return title;
    }
}