package com.alex.controllers;

import com.alex.data.Product;
import com.alex.structures.LinkedList;

public class ProductController {
    public LinkedList<Product> dataList;

    public ProductController(){
        dataList = new LinkedList<>();
    }

    public void addData(Product data) {
        dataList.add(data);
    }

    public void replaceData(Product data, Product newData) { dataList.replace(data, newData); }

    public void deleteData(Product data) { dataList.delete(data); }

    public int getSize(){
        return dataList.getSize();
    }

    public void clear() { dataList = new LinkedList<>(); }

    public String[] getNames(){
        // SALIDA
        String[] out = new String[dataList.getSize()];

        // NITS
        for(int index = 0; index < out.length; index++)
            out[index] = dataList.get(index).name;

        return out;
    }

    public Product get(int index){
        return dataList.get(index);
    }

    public Product getByName(String name){
        Product out = null;

        for(int index = 0; index < dataList.getSize(); index++)
            if(dataList.get(index).name.equals(name)){
                out = dataList.get(index);
                break;
            }

        return out;
    }

    public String toCsv(){
        // SALIDA
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size; index++)
            out.append(dataList.get(index).name).append(",").append(dataList.get(index).price).append(",").append(dataList.get(index).size).append(",").append(dataList.get(index).image).append("\n");

        return out.toString();
    }

    @Override
    public String toString(){
        // SALIDA Y DIMENSION
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size;index++)
            out.append("Nombre: ").append(dataList.get(index).name).append(" Precio: ").append(dataList.get(index).price).append(" Cantidad: ").append(dataList.get(index).size).append(" Imagen: ").append(dataList.get(index).image).append("\n");

        // SALIDA
        return out.toString();
    }
}
