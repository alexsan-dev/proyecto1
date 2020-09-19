package com.alex.controllers;

import com.alex.data.Product;
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

    private void orderArry(int[] arry){
        for(int i = 0; i < arry.length - 1; i++) {
            for(int j = 0; j < arry.length - 1; j++) {
                if (arry[j] < arry[j + 1]) {
                    int tmp = arry[j+1];
                    arry[j+1] = arry[j];
                    arry[j] = tmp;
                }
            }
        }
    }

    private void orderArry(float[] arry){
        for(int i = 0; i < arry.length - 1; i++) {
            for(int j = 0; j < arry.length - 1; j++) {
                if (arry[j] < arry[j + 1]) {
                    float tmp = arry[j+1];
                    arry[j+1] = arry[j];
                    arry[j] = tmp;
                }
            }
        }
    }

    public float getTotalIVA(){
        float iva = 0;
        for(int index = 0; index < dataList.getSize(); index++)
            for(int subIndex = 0; subIndex < dataList.get(index).ivas.getSize(); subIndex++)
                iva += Float.parseFloat(dataList.get(index).ivas.get(subIndex));
        return iva;
    }

    public float getTotalSell(){
        float total = 0;
        for(int index = 0; index < dataList.getSize(); index++)
            for(int subIndex = 0; subIndex < dataList.get(index).products.getSize(); subIndex++)
                total += dataList.get(index).products.get(subIndex).price * Integer.parseInt(dataList.get(index).sizes.get(subIndex));
        return total;
    }

    public int getMaxSize(){
        // CANTIDAD
        int[] sizes = new int[dataList.getSize()];
        for(int index = 0; index < sizes.length; index++){
            int sizesTotal = 0;
            for(int subIndex = 0; subIndex < dataList.get(index).sizes.getSize(); subIndex++)
                sizesTotal += Integer.parseInt(dataList.get(index).sizes.get(subIndex));
            sizes[index] = sizesTotal;
        }

        // ORDENAR
        orderArry(sizes);
        return sizes[0];
    }

    public float getMaxSell(){
        // CANTIDAD
        float[] prices = new float[dataList.getSize()];
        for(int index = 0; index < prices.length; index++){
            float totalSell = 0;
            LinkedList<Product> products = dataList.get(index).products;
            LinkedList<String> sizes = dataList.get(index).sizes;
            for(int subIndex = 0; subIndex < products.getSize(); subIndex++)
                totalSell += products.get(subIndex).price * Integer.parseInt(sizes.get(subIndex));
            prices[index] = totalSell;
        }

        // ORDENAR
        orderArry(prices);
        return prices[0];
    }

    public void replaceData(Sell data, Sell newData) { dataList.replace(data, newData); }

    public int getSize(){
        return dataList.getSize();
    }

    public void clear() { dataList = new LinkedList<>(); }

    public Sell get(int index){
        return dataList.get(index);
    }

    public Sell getByCode(int code){
        Sell tmpSell = null;
        for(int index = 0 ; index < getSize(); index++)
            if(get(index).code == code){
                tmpSell = get(index);
                break;
            }
        return tmpSell;
    }

    public String toCsv(){
        // SALIDA
        StringBuilder out = new StringBuilder();
        int size = dataList.getSize();

        // CREAR STRING
        for(int index = 0; index < size; index++)
            for(int subIndex = 0; subIndex < dataList.get(index).sizes.getSize(); subIndex++)
                out.append(dataList.get(index).code).append(",").append(dataList.get(index).nit).append(",").append(dataList.get(index).products.get(subIndex).name).append(",").append(dataList.get(index).sizes.get(subIndex)).append("\n");

        return out.toString();
    }

}
