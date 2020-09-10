package com.alex.components;

import com.alex.models.XFrameModel;

import java.awt.*;
import javax.swing.*;

public class XFrame extends JFrame implements XFrameModel {
    private static final long serialVersionUID = 1L;
    private JPanel contentPanel;

    public void setFrame(String name, int width, int height) {
        // AGREGAR ICONO Y COLOR
        setLayout(null);
        setBackground(new Color(220, 220, 220));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0,0,width, height);

        // AGREGAR DIMENSIONES
        setResizable(false);
        setVisible(true);

        // CONFIGURAR FRAME
        setTitle(name);
        setSize(width, height);
    }

    public void setHeader(String name, String description) {
        // COMPONENTES
        XHeader header = new XHeader(name, description, getWidth());

        // PANEL DE CONTENIDO
        contentPanel = new JPanel(null);

        // CONFIGURAR PANEL DE CONTENIDO
        contentPanel.setOpaque(true);
        contentPanel.setVisible(true);
        contentPanel.setBackground(new Color(220, 220, 220));
        contentPanel.setBounds(0, 80, getWidth(), getHeight() - 80);

        // ASIGNAR
        getContentPane().add(header);

        // RENDERIZAR COMPONENTES
        /*
            EL CONSTRUCTOR DE LAS CLASES HIJAS DEBE INICIAR SETFRAME (OBLIGATORIAMENTE)
            LUEGO LLAMAR A SETHEADER SI DESEA EL ESTILO DE VENTANA CON TITULO
            SI NO SE INICIA SETHEADER JAMAS SE LLAMA A RENDERWITHIN Y CREARIA UNA VENTANA NORMAL
            TAMPOCO TENDRIA EFECTO EL METODO ADDCOMPONENT
         */
        renderWithin();
        getContentPane().add(contentPanel);
    }


    public void addComponent(Component component){
        contentPanel.add(component);
    }

    @Override
    public void renderWithin() {}
}