package com.alex.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class XActionPanel extends JPanel {
    private JPanel textPanel;
    private JLabel label, subLabel;
    private XButton button;

    public void constructor(String title, String subTitle, String buttonText, Color background, Color btnColor){
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // PANEL
        textPanel = new JPanel(new GridLayout(2, 1));

        // LABELS
        label = getLabel(title, 17, background);
        subLabel = getLabel(subTitle, 16, background);
        button = new XButton(buttonText, btnColor, Color.white);

        // PROPIEDADES
        setBorder(new EmptyBorder(10,25,10,25));
        setBackground(background);
        setProperties(background);
    }

    // CONSTRUCTORES
    public XActionPanel(String title, String subTitle, String buttonText){
        constructor(title, subTitle, buttonText, new Color(100, 100, 100), new Color(33,150,243));
    }

    public XActionPanel(String title, String subTitle, String buttonText, Color background, Color btnColor){
        constructor(title, subTitle, buttonText, background, btnColor);
    }

    public void setProperties(Color background){
        textPanel.setBackground(background);
        textPanel.setBorder(new EmptyBorder(0,0,0,20));

        // PANEL DE UPLOAD
        textPanel.add(label);
        textPanel.add(subLabel);

        add(textPanel);
        add(button);
    }

    public JLabel getLabel(String text, int size, Color background){
        // COMPONENTE
        JLabel label = new JLabel(text);

        // PROPIEDADES
        label.setOpaque(true);
        label.setForeground(Color.white);
        label.setBackground(background);
        label.setFont(new Font("Lato", Font.BOLD, size));

        return label;
    }

    public void setAction(ActionListener e){
        button.onClick(e);
    }
}
