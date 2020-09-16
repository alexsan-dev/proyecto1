package com.alex.structures;

import com.alex.models.ClientsModel;

public class Client implements ClientsModel {
    public String name;
    public int age, nit;
    public char sex;

    public Client(String name, int age, char sex, int nit){
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.nit = nit;
    }
}
