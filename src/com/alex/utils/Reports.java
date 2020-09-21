package com.alex.utils;

import com.alex.components.XAlert;
import com.alex.controllers.ClientController;
import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.data.Client;
import com.alex.data.Product;
import com.alex.data.Sell;
import com.alex.data.User;
import com.alex.structures.LinkedList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reports {
    private static String orderClients(ClientController clientController){
        // SALIDA
        StringBuilder out = new StringBuilder();
        out.append("<div><div class='tableTitle'><h1>Clientes registrados</h1><p>A continuación se muestra toda la información con excepción de las imágenes de todos los clientes registrados en el sistema ordenados por edades.</p></div><div class='tableHead'><h2>Nombre</h2><h2>Edad</h2><h2>Sexo</h2><h2>NIT</h2></div><ol>");

        // CLONAR LISTA
        LinkedList<Client> clients = clientController.dataList.clone();

        // ORDENAR
        for(int i = 0; i < clients.getSize() - 1; i++) {
            for(int j = 0; j < clients.getSize() - 1; j++) {
                if (clients.get(j).age < clients.get(j + 1).age) {
                    Client tmp = clients.get(j+1);
                    clients.replace(clients.get(j+1), clients.get(j));
                    clients.replace(clients.get(j), tmp);
                }
            }
        }

        // CREAR HTML
        for(int index = 0; index < clients.getSize(); index++)
            out.append("<li><div>").append(clients.get(index).name).append("</div><div>").append(clients.get(index).age).append("</div><div>").append(clients.get(index).sex).append("</div><div>").append(clients.get(index).nit).append("</div></li>");

        out.append("</ol></div>");
        return out.toString();
    }

    private static String orderProducts(ProductController productController){
        // SALIDA
        StringBuilder out = new StringBuilder();
        out.append("<div><div class='tableTitle'><h1>Productos registrados</h1><p>A continuación se muestra toda la información con excepción de las imágenes de todos los productos registrados en el sistema ordenados por precios.</p></div><div class='tableHead'><h2>Nombre</h2><h2>Precio</h2><h2>Cantidad</h2></div><ol>");

        // CLONAR LISTA
        LinkedList<Product> products = productController.dataList.clone();

        // ORDENAR
        for(int i = 0; i < products.getSize() - 1; i++) {
            for(int j = 0; j < products.getSize() - 1; j++) {
                if (products.get(j).price < products.get(j + 1).price) {
                    Product tmp = products.get(j+1);
                    products.replace(products.get(j+1), products.get(j));
                    products.replace(products.get(j), tmp);
                }
            }
        }

        // CREAR HTML
        for(int index = 0; index < products.getSize(); index++)
            out.append("<li class='").append(products.get(index).size == 0 ? "empty'" : "'").append("><div>").append(products.get(index).name).append("</div><div>Q").append(products.get(index).price).append("</div><div>").append(products.get(index).size).append("</div></li>");

        out.append("</ol></div>");
        return out.toString();
    }

    private static LinkedList<Sell> orderMaxSales(SalesController salesController){
        // CLONAR LISTA
        LinkedList<Sell> sales = salesController.dataList.clone();

        // ORDENAR
        for(int i = 0; i < sales.getSize() - 1; i++) {
            for(int j = 0; j < sales.getSize() - 1; j++) {
                if (sales.get(j).getTotal() < sales.get(j + 1).getTotal()) {
                    Sell tmp = sales.get(j+1);
                    sales.replace(sales.get(j+1), sales.get(j));
                    sales.replace(sales.get(j), tmp);
                }
            }
        }

        return sales;
    }

    private static String orderSales(SalesController salesController){
        // SALIDA
        StringBuilder out = new StringBuilder();
        out.append("<div><div class='tableTitle'><h1>Ventas registradas</h1><p>A continuación se muestra toda la información de todas las ventas registradas en el sistema ordenadas por total de ventas.</p></div><div class='tableHead'><h2>Código</h2><h2>NIT</h2><h2>Total</h2></div><ol>");

        // CLONAR LISTA
        LinkedList<Sell> sales = orderMaxSales(salesController);
        // CREAR HTML
        for(int index = 0; index < sales.getSize(); index++)
            out.append("<li><div>").append(sales.get(index).code).append("</div><div>").append(sales.get(index).nit).append("</div><div>Q").append(sales.get(index).getTotal()).append("</div></li>");

        out.append("</ol></div>");
        return out.toString();
    }

    private static String getDate(){
        // FECHA
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void createHTML(ClientController clientController, ProductController productController, SalesController salesController, User currentUser){
        // SALIDA
        StringBuilder html = new StringBuilder();

        // HTML
        html.append("<!DOCTYPE html><html><head><title>Proyecto 1 - Reportes</title><meta charset='utf-8'/><link href='./styles.css' rel='stylesheet'/><link href='https://fonts.googleapis.com/css2?family=Cabin&display=swap' rel='stylesheet'></head><body><div class='userInfo'><p>Generado por <strong>").append(currentUser.name).append("</strong> el ").append(getDate()).append("</p></div>");
        html.append(orderClients(clientController)).append(orderProducts(productController)).append(orderSales(salesController));
        html.append("</body></html>");

        // ARCHIVO
        try {
            FileWriter htmlFile = new FileWriter("./src/reports/index.html");
            htmlFile.write(html.toString());
            htmlFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatePDF(String[] lines, String name, PDRectangle size){
        // CREAR PAGINA
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(size);

        //  AGREGAR
        doc.addPage(page);

        // CONTENIDO
        try {
            PDPageContentStream content = new PDPageContentStream(doc, page);
            content.beginText();

            content.setFont(PDType1Font.TIMES_ROMAN, 12);
            content.setLeading(14.5f);

            content.newLineAtOffset(25, 700);

            for (String line : lines) {
                content.showText(line);
                content.newLine();
            }

            content.endText();
            content.close();

            // GUARDAR
            doc.save("./src/reports/"+name+".pdf");
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createPDF(User user, ProductController productController,SalesController salesController, int code){
        // VENTAS
        Sell sell = salesController.getByCode(code);
        if(sell != null) {
            String[] bill = new String[11];
            bill[0] = "Generado por " + user.name + " el " + getDate();
            bill[1] = "Código: " + code;
            bill[2] = "NIT: " + sell.nit;
            bill[3] = "Ventas: ";

            for (int index = 0; index < 5; index++) {
                if (sell.products.get(index) != null)
                    bill[index + 4] = sell.sizes.get(index) + " de " + sell.products.get(index).name + " por Q" + (Integer.parseInt(sell.sizes.get(index)) * sell.products.get(index).price);
                else bill[index + 4] = "-----------------";
            }

            bill[9] = "Total de la venta: Q" + sell.getTotal();
            bill[10] = "Total de IVA (12%): Q" + (sell.getTotal() * 0.120);

            // PRODUCTOS MAS VENDIDOS
            LinkedList<String> prices = Tools.orderProducts(productController, salesController);
            String[] pricesRep = new String[1 + Math.min(10, prices.getSize())];
            pricesRep[0] = "10 productos mas vendidos";
            for(int index =0; index < Math.min(10, prices.getSize()); index++){
                String[] vals = prices.get(index).split(",");
                pricesRep[index + 1] = vals[0] + " con " + vals[1] + " ventas";
            }

            // MAYORES VENTAS
            LinkedList<Sell> sales = orderMaxSales(salesController);
            String[] salesRep = new String[1 + Math.min(10, sales.getSize())];
            salesRep[0] = "10 Total de ventas";
            for(int index =0; index < Math.min(10, sales.getSize()); index++){
                Sell cSell = sales.get(index);
                salesRep[index + 1] = "Código: " + cSell.code + " NIT: " + cSell.nit + " Total: Q" + cSell.getTotal();
            }

            // REPORTE DE VENTA
            generatePDF(bill, "bill", PDRectangle.LETTER);

            // REPORTES DE TABLAS
            generatePDF(pricesRep, "productos", PDRectangle.LETTER);
            generatePDF(salesRep, "ventas", PDRectangle.LETTER);
        } else XAlert.showAlert("Error al crear", "No se encontró el código de venta");
    }
}
