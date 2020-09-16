package com.alex.controllers;

import com.alex.data.Client;
import com.alex.structures.LinkedList;

public class ClientController {
    public LinkedList<Client> dataList;

    public ClientController(){
        dataList = new LinkedList<>();
    }

    public void addData(Client data) {
        dataList.add(data);
    }

    public void replaceData(Client data, Client newData) { dataList.replace(data, newData); }

    public void deleteData(Client data) { dataList.delete(data); }

    public int getSize(){
        return dataList.getSize();
    }

    public void clear() { dataList = new LinkedList<>(); }

    public Client get(int index){
        return dataList.get(index);
    }

    public String toCsv(){
        // SALIDA
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size; index++)
            out.append(dataList.get(index).name).append(",").append(dataList.get(index).age).append(",").append(dataList.get(index).sex).append(",").append(dataList.get(index).nit).append("\n");

        return out.toString();
    }

    @Override
    public String toString(){
        // SALIDA Y DIMENSION
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size;index++)
            out.append("Nombre: ").append(dataList.get(index).name).append(" Edad: ").append(dataList.get(index).age).append(" Sexo: ").append(dataList.get(index).sex).append(" NIT: ").append(dataList.get(index).nit).append("\n");

        // SALIDA
        return out.toString();
    }

}
