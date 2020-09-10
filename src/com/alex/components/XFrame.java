package com.alex.components;

import java.awt.*;
import javax.swing.*;

public class XFrame extends JFrame implements com.alex.models.XFrame {
    private static final long serialVersionUID = 1L;
    public JPanel contentPanel;

    public XFrame() {
        // AGREGAR ICONO Y COLOR
        setLayout(null);
        setBackground(new Color(238, 238, 238));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // PANEL DE CONTENIDO
        contentPanel = new JPanel(null);

        // AGREGAR DIMENSIONES
        setResizable(false);
        setVisible(true);
    }

    public void setFrame(String name, int width, int height) {
        // CONFIGURAR FRAME
        setTitle(name);
        setSize(width, height);

        // CONFIGURAR PANEL DE CONTENIDO
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

        // RENDERIZAR COMPONENTES
        /*
            EL CONSTRUCTOR DE LAS CLASES HIJAS DEBE INICIAR SETFRAME (OBLIGATORIAMENTE)
            LUEGO LLAMAR A SETHEADER SI DESEA EL ESTILO DE VENTANA CON TITULO
            SI NO SE INICIA SETHEADER JAMAS SE LLAMA A RENDERWITHIN Y CREARIA UNA VENTANA NORMAL
            TAMPOCO TENDRIA EFECTO EL METODO ADDCOMPONENT
         */
        renderWithin();
    }


    public void addComponent(Component component){
        contentPanel.add(component);
    }

    @Override
    public void renderWithin() {}
}