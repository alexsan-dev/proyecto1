package com.alex.views;

import com.alex.components.XFrame;
import com.alex.components.XTabPane;
import com.alex.models.UserModel;

import javax.swing.*;

public class ManageClients extends XFrame {
    public ManageClients(){
        // CONFIGURAR VENTANA
        setFrame("Administracion de clientes", 800, 500);
        setHeader("Interfaz de administración de clientes", "Esta área tiene como propósito, brindar al usuario herramientas de consulta de datos de clientes.");
    }

    @Override
    public void renderWithin() {
        String[] tabsName = { "Dashboard" };
        XTabPane tabs = new XTabPane(tabsName);
        JPanel dashboard = new JPanel();

        // AGREGAR A DASHBOARD
        tabs.addTab(tabsName[0], dashboard);

        // POSICIONES
        tabs.setBounds(25,0,getWidth() - 25, getHeight() - 80);
        tabs.setTabs();

        // AGREGAR
        addComponent(tabs);
    }
}
