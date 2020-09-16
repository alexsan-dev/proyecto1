package com.alex.data;

import com.alex.models.ClientsModel;

public class Client implements ClientsModel {
    public String name, image;
    public int age, nit;
    public char sex;

    public Client(String name, int age, char sex, int nit, String image){
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.nit = nit;
        this.image = image;
    }
}
