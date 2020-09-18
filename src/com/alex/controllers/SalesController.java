package com.alex.controllers;

import com.alex.data.Sell;
import com.alex.structures.LinkedList;

public class SalesController {
    public LinkedList<Sell> dataList;

    public SalesController(){
        dataList = new LinkedList<>();
    }

    public void addData(Sell data) {
        dataList.add(data);
    }

    public void replaceData(Sell data, Sell newData) { dataList.replace(data, newData); }

    public void deleteData(Sell data) { dataList.delete(data); }

    public int getSize(){
        return dataList.getSize();
    }

    public void clear() { dataList = new LinkedList<>(); }

    public Sell get(int index){
        return dataList.get(index);
    }

    public String toCsv(){
        // SALIDA
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size; index++)
            out.append(dataList.get(index).code).append(",").append(dataList.get(index).nit).append(",").append(dataList.get(index).product).append(",").append(dataList.get(index).size).append("\n");

        return out.toString();
    }

    @Override
    public String toString(){
        // SALIDA Y DIMENSION
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size;index++)
            out.append("Código: ").append(dataList.get(index).code).append(" Nit: ").append(dataList.get(index).nit).append(" Producto: ").append(dataList.get(index).product).append(" Cantidad: ").append(dataList.get(index).size).append("\n");

        // SALIDA
        return out.toString();
    }
}
