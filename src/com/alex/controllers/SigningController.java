package com.alex.controllers;

import com.alex.components.XAlert;
import com.alex.data.User;
import com.alex.structures.LinkedList;
import com.alex.views.MainMenu;
import com.sun.tools.javac.Main;

public class SigningController {
    private final User defUser;
    private final LinkedList<User> userList;
    private boolean verify;

    public SigningController(){
        this.userList = new LinkedList<>();
        this.defUser = new User("admin", "admin", "admin", false);
    }

    public User findByUser(String userName){
        User tmpUser = null;
        for(int index = 0; index < userList.getSize(); index++)
            if(userList.get(index).user.equals(userName)){
                tmpUser = userList.get(index);
                break;
            }
        return tmpUser;
    }

    public User findByPassAndUser(String userName, String pass){
        User tmpUser = null;
        for(int index = 0; index < userList.getSize(); index++)
            if(userList.get(index).user.equals(userName) && userList.get(index).pass.equals(pass) ){
                tmpUser = userList.get(index);
                break;
            }
        return tmpUser;
    }

    public boolean getVerify(){
        return verify;
    }

    public void addUser(User user){
        User tmpUser = findByPassAndUser(user.user, user.pass);

        if(user.isNew) {
            if(tmpUser != null) {
                XAlert.showError("Error al crear", "El nombre de usuario ya existe");
                verify = false;
            }
            else {
                if(userList.getSize() < 10) {
                    userList.add(user);
                    new MainMenu(user);
                    verify = true;
                } else {
                    XAlert.showError("Error al crear", "La cantidad maxima de usuarios es de 10");
                    verify = false;
                }
            }
        } else{
            if(tmpUser == null) {
                if(user.user.equals(defUser.user) && user.pass.equals(defUser.pass)){
                    new MainMenu(user);
                    verify = true;
                } else {
                    XAlert.showError("Error al iniciar", "No se encontró el usuario");
                    verify = false;
                }
            } else{
                new MainMenu(tmpUser);
                verify = true;
            }
        }
    }
}
