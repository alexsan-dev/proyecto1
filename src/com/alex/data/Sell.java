package com.alex.data;

import com.alex.models.SalesModel;
import com.alex.structures.LinkedList;

public class Sell implements SalesModel {
    public int code;
    public int nit;
    public LinkedList<Product> products;
    public LinkedList<String> sizes;
    public LinkedList<String> ivas;

    public Sell(int code, int nit, LinkedList<String> sizes, LinkedList<String> ivas, LinkedList<Product> products){
      this.code = code;
      this.nit = nit;
      this.products = products;
      this.sizes = sizes;
      this.ivas = ivas;
    }
}
