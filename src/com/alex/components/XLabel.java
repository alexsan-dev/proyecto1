package com.alex.components;
import java.awt.*;
import javax.swing.*;

/**
 * LABEL
 */
public class XLabel extends JLabel {
    private static final long serialVersionUID = 1L;

    public XLabel(String text) {
        // LABEL NAME
        setText(text);

        // PROPIEDADES
        setForeground(new Color(60, 60, 60));
        setFont(new Font("Lato", Font.BOLD, 18));
    }
}