package com.alex.views;

import com.alex.components.XFrame;
import com.alex.controllers.UserModelController;

public class MainMenu extends XFrame {
    public MainMenu(UserModelController userController){
        // CONFIGURAR VENTANA
        setFrame("Menu principal", 400, 400);
        setHeader("Bienvenido de nuevo " + userController.name, "Aqui hay algunas herramientas que puedes usar");
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
    }
}
