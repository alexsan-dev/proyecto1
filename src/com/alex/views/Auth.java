package com.alex.views;

import com.alex.components.*;

import java.awt.*;

/**
 * Auth Frame de login y registro
 */
public class Auth extends XFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor iniciar ventana y propiedades iniciales de login
     * 
     * @return
     */
    public Auth() {
        setFrame("Login y Registro", 400, 400);
        setHeader("Hola!, ¿Quieres iniciar sesión?", "Si no tienes una cuenta puedes registrarte");
        render();
    }

    private void render() {
        // COMPONENTES
        XField name = new XField("Nombre de usuario: ");
        XField pass = new XField("Contraseña", true);
        XButton register = new XButton("REGISTRARSE", new Color(0,0,0,0), new Color(80,80,80));
        XButton signin = new XButton("INICIAR SESION", new Color(33,150,243), Color.white);

        // PROPIEDADES
        name.setBounds(0,10,400,90);
        pass.setBounds(0,100,400,90);
        register.setBounds(110, 215, 120, 50);
        signin.setBounds(230, 215, 140, 50);

        // AGREGAR
        addComponent(name);
        addComponent(pass);
        addComponent(register);
        addComponent(signin);
    }
}