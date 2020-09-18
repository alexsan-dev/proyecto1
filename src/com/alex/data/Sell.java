package com.alex.data;

import com.alex.models.ProductsModel;
import com.alex.models.SalesModel;

public class Sell implements SalesModel {
    public int code;
    public int nit;
    public String product;
    public int size;

    public Sell(int code, int nit, String product, int size){
      this.code = code;
      this.nit = nit;
      this.product = product;
      this.size = size;
    }
}
