package com.alex.models;

import com.alex.data.Product;
import com.alex.structures.LinkedList;

public interface SalesModel {
    int code = 0;
    int nit = 0;
    LinkedList<Product> products = new LinkedList<>();
    LinkedList<String> sizes = new LinkedList<>();
    LinkedList<String> ivas = new LinkedList<>();
}
