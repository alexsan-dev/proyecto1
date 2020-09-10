package com.alex.views;

import com.alex.components.XButton;
import com.alex.components.XFrame;
import com.alex.controllers.User;

public class MainMenu extends XFrame {
    public MainMenu(User user){
        // CONFIGURAR VENTANA
        setFrame("Menu principal", 400, 400);
        setHeader("Bienvenido de nuevo " + user.name, "Aqui hay algunas herramientas que puedes usar");
    }

    @Override
    public void renderWithin() {
        // COMPONENTES
    }
}
