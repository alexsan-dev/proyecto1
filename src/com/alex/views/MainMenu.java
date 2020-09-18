package com.alex.views;

import com.alex.components.XButton;
import com.alex.components.XFrame;
import com.alex.controllers.ClientController;
import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.controllers.UserModelController;

import java.awt.*;

public class MainMenu extends XFrame {
    // DATOS
    private final ClientController clientController;
    private final ProductController productController;
    private final SalesController salesController;

    public MainMenu(UserModelController userController){
        // CREAR CONTROLADOR
        clientController = new ClientController();
        productController = new ProductController();
        salesController = new SalesController();

        // CONFIGURAR VENTANA
        setFrame("Menu principal", 385, 315);
        setHeader("Bienvenido de nuevo " + userController.name, "Aquí hay algunas herramientas que puedes usar");
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
        XButton manageClientsBtn = new XButton("Administrar Clientes", new Color(96,125,139), Color.white);
        XButton manageProductsBtn = new XButton("Administrar Productos", new Color(96,125,139), Color.white);
        XButton manageSalesBtn = new XButton("Administrar Ventas", new Color(96,125,139), Color.white);
        XButton reportsBtn = new XButton("Reportes");

        // EVENTOS
        manageClientsBtn.onClick((e) -> new ManageClients(clientController));
        manageProductsBtn.onClick((e) -> new ManageProducts(productController, salesController));
        manageSalesBtn.onClick((e) -> new ManageSales(salesController, productController, clientController));

        // POSICIÓN
        manageClientsBtn.setBounds(25,25, 160, 70);
        manageProductsBtn.setBounds(195,25, 160, 70);
        manageSalesBtn.setBounds(25,105, 160, 70);
        reportsBtn.setBounds(195,105, 160, 70);

        // AGREGAR
        addComponent(manageClientsBtn);
        addComponent(manageProductsBtn);
        addComponent(manageSalesBtn);
        addComponent(reportsBtn);
    }
}
