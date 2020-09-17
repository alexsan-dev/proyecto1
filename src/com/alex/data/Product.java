package com.alex.data;

import com.alex.models.ProductsModel;

public class Product implements ProductsModel {
    public String name;
    public float price;
    public int size;
    public String image;

    public Product(String name, float price, int size, String image){
        this.name = name;
        this.price = price;
        this.size = size;
        this.image = image;
    }
}
