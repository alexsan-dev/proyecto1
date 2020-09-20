package com.alex.utils;

import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.structures.LinkedList;

public class Tools {
    public static void orderArry(float[] arry){
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

    public static void orderArry(int[] arry){
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

    public static LinkedList<String> orderProducts(ProductController productController, SalesController salesController){
        // CONTAR VENTAS
        LinkedList<String> prices = new LinkedList<>();
        for(int productsIndex = 0; productsIndex < productController.getSize(); productsIndex++){
            // PRODUCTO
            String name = productController.get(productsIndex).name;
            int total = 0;

            // BUSCAR
            for(int salesIndex = 0; salesIndex < salesController.getSize(); salesIndex++)
                for(int sPrIndex = 0; sPrIndex < salesController.get(salesIndex).products.getSize(); sPrIndex++)
                    if(salesController.get(salesIndex).products.get(sPrIndex).name.equals(name))
                        total += Integer.parseInt(salesController.get(salesIndex).sizes.get(sPrIndex));

            // AGREGAR
            prices.add(name + "," + total);
        }

        // ORDENAR
        for(int i = 0; i < prices.getSize() - 1; i++) {
            for(int j = 0; j < prices.getSize() - 1; j++) {
                int cPrice = Integer.parseInt(prices.get(j).split(",")[1]);
                int nextPrice = Integer.parseInt(prices.get(j + 1).split(",")[1]);

                if (cPrice < nextPrice) {
                    String tmpName = prices.get(j + 1).split(",")[0];

                    prices.replace(prices.get(j+1), prices.get(j));
                    prices.replace(prices.get(j), tmpName+","+nextPrice);
                }
            }
        }
        return prices;
    }
}
