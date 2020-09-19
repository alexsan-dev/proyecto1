package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.data.Product;
import com.alex.structures.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ManageProducts extends XFrame {
    private final String[] tabsName = { "Productos", "Graficas", "Editar" };
    private final ProductController productController;
    private final SalesController salesController;
    private DefaultTableModel model;
    private XPieChart pieChart;
    private XBarChart barChart;

    public ManageProducts(ProductController productController, SalesController salesController){
        // DATOS
        this.productController = productController;
        this.salesController = salesController;

        // CONFIGURAR VENTANA
        setFrame("Administración de productos", 800, 500);
        setHeader("Interfaz de administración de productos", "Esta área tiene como propósito, brindar al usuario herramientas de consulta de datos de productos.");
    }

    @Override
    public void renderWithin() {
        XTabPane tabs = new XTabPane(tabsName);

        // PANELS
        dashboard(tabs);
        charts(tabs);
        crud(tabs);

        // POSICIONES
        tabs.setBounds(25,0,getWidth() - 25, getHeight() - 80);
        tabs.setTabs();

        // AGREGAR
        addComponent(tabs);
    }

    private boolean verifyName(String name, String exclude){
        // VERIFICAR NIT
        boolean verifyName = true;
        for (int productsIndex = 0; productsIndex < productController.getSize(); productsIndex++){
            if(productController.get(productsIndex).name.equals(name) && (exclude.equals("") || !productController.get(productsIndex).name.equals(exclude))) {
                verifyName = false;
                break;
            }
        }

        return verifyName;
    }

    public void updateTable(){
        // LIMPIAR DATOS
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        // ACTUALIZAR DATOS
        if(productController != null) {
            String[] productRows = productController.toCsv().split("\n");
            if(productController.getSize() > 0) for (String productRow : productRows) model.addRow(productRow.split(","));
        }
    }

    private Product findByName(String name){
        Product tmpProduct = null;

        // BUSCAR
        for(int index = 0; index < productController.getSize(); index++){
            if(productController.get(index).name.equals(name)) {
                tmpProduct = productController.get(index);
                break;
            }
        }

        return tmpProduct;
    }

    public void updateChart(){
        // CONTAR PRECIOS
        LinkedList<String> products = new LinkedList<>();
        int quarter = 0, half = 0, seventy = 0, expensive = 0;

        for(int index = 0; index < productController.getSize(); index++){
            // PRECIO
            float currentPrice = productController.get(index).price;

            // RANGOS
            if(currentPrice >= 0 && currentPrice <= 100) quarter++;
            else if(currentPrice > 100 && currentPrice <= 250) half++;
            else if(currentPrice > 250 && currentPrice <= 500) seventy++;
            else if(currentPrice > 500) expensive++;
        }
        products.add("0 - 100" + "," + quarter);
        products.add("100 - 250" + "," + half);
        products.add("250 - 500" + "," + seventy);
        products.add("> 500" + "," + expensive);

        // CONTAR VENTAS
        LinkedList<String> sales = new LinkedList<>();
        for(int productsIndex = 0; productsIndex < productController.getSize(); productsIndex++){
            // PRODUCTO
            String name = productController.get(productsIndex).name;
            int total = 0;

            // BUSCAR
            for(int salesIndex = 0; salesIndex < salesController.getSize(); salesIndex++){
                for(int sPrIndex = 0; sPrIndex < salesController.get(salesIndex).products.getSize(); sPrIndex++){
                    if(salesController.get(salesIndex).products.get(sPrIndex).name.equals(name)) {
                        total += Integer.parseInt(salesController.get(salesIndex).sizes.get(sPrIndex));
                    }
                }
            }

            // AGREGAR
            sales.add(name + "," + total);
        }

        // DATASET
        if(pieChart != null && barChart != null) {
            // LIMPIAR
            barChart.dts.clear();
            pieChart.dts.clear();

            // BARRAS
            for(int dataIndex = 0; dataIndex < products.getSize(); dataIndex++){
                String[] vals = products.get(dataIndex).split(",");
                barChart.setValue(vals[0], Integer.parseInt(vals[1]));
            }

            // PIE
            for(int dataIndex = 0; dataIndex < sales.getSize(); dataIndex++){
                String[] vals = sales.get(dataIndex).split(",");
                pieChart.setValue(vals[0], Integer.parseInt(vals[1]));
            }
        }
    }

    private void charts(XTabPane tabs){
        // LAYOUT
        JPanel chartPanel = new JPanel(null);
        JScrollPane scrollPane = new JScrollPane(chartPanel);

        // COMPONENTES
        pieChart = new XPieChart("Mas vendidos");
        barChart = new XBarChart("Rango de precios", "Precio", "Cantidad");

        // CHARTS
        new XInterval(e -> updateChart(), 500);

        // POSICIONES
        pieChart.chartPanel.setBounds(0,0,getWidth() - 170,getHeight() - 115 );
        barChart.chartPanel.setBounds(0,getHeight() - 115,getWidth() - 170,getHeight() - 115);
        chartPanel.setBounds(0,0, getWidth() - 170, (2 * getHeight()) - 200);
        chartPanel.setPreferredSize(new Dimension(getWidth() - 170, (2 * getHeight()) - 200));
        scrollPane.setBounds(0,0, getWidth() - 155, getHeight() - 80);

        // AGREGAR
        chartPanel.add(pieChart.chartPanel);
        chartPanel.add(barChart.chartPanel);

        // AGREGAR A TAB
        tabs.addTab(tabsName[1], scrollPane);
    }

    private void dashboard(XTabPane tabs){
        // DATOS
        String[] columns = { "Nombre", "Precio", "Cantidad" };

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Productos en CSV", "csv" );
        chooser.setFileFilter(filter);

        // TABLA
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        XTable table = new XTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // DATOS INICIALES
        new XInterval(e -> updateTable(), 500);

        updateChart();

        // UPLOAD PANEL
        XActionPanel uploadPanel = new XActionPanel("Carga masiva de datos CSV", "Aquí puedes cargar mas datos al dashboard.", "Subir CSV");
        JPanel dashboard = new JPanel(null);

        // EVENTOS
        uploadPanel.setAction((e) ->{
            // ARCHIVO
            chooser.showOpenDialog(null);
            File dataFile = chooser.getSelectedFile();
            try {
                // LEER Y GUARDAR
                Scanner fileReader = new Scanner(dataFile);
                while(fileReader.hasNextLine()){
                    // CREAR OBJETO
                    String[] dataLine = fileReader.nextLine().split(",");
                    Product data = new Product(dataLine[0], Float.parseFloat(dataLine[1]), Integer.parseInt(dataLine[2]), dataLine[3]);

                    // VERIFICAR
                    if (productController != null) {
                        // VERIFICAR NOMBRE
                        boolean vName = verifyName(data.name, "");

                        if(vName && productController.getSize() <= 100) {
                            // AGREGAR LOCAL
                            if(data.size > 0)
                                productController.addData(data);
                            else XAlert.showError("Error al agregar", "La cantidad debe ser mayor que 0");

                            // AGREGAR A TABLA
                            updateTable();
                            updateChart();
                        } else if(productController.getSize() > 100) XAlert.showError("Error al agregar", "El numero maximo de productos es de 100");
                        else if(!vName) XAlert.showError("Error al agregar", "Ya existe un producto con el mismo Nombre");

                    }
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        // PROPIEDADES
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(220, 220, 220));
        uploadPanel.setBounds(0,getHeight() - 184, getWidth() - 150, 75);

        scrollPane.setBounds(0,0, getWidth() - 150, getHeight() - 185);

        // DASHBOARD
        dashboard.add(scrollPane);
        dashboard.add(uploadPanel);

        // AGREGAR A TAB
        tabs.addTab(tabsName[2], dashboard);
    }

    private void productForm(boolean update, boolean editable) {
        // PEDIR NIT PRIMERO
        String tmpName = "";
        Product initialProduct = null;

        // BUSCAR CLIENTE
        if(update) {
            if(productController.getSize() == 0) {
                XAlert.showError("Sin productos", "Aun no existen productos registrados");
                return;
            }

            // NIT
            tmpName = XAlert.showPrompt("Ingresar nombre");
            if(!tmpName.equals("")){
                initialProduct = findByName(tmpName);

                if(initialProduct == null) {
                    XAlert.showError("Sin datos", "No se encontró ningún producto");
                    return;
                }
            } else {
                XAlert.showError("Nombre invalido", "El nombre ingresado no es valido");
                return;
            }
        }

        // CREAR FRAME
        XFrame creationForm = new XFrame();
        creationForm.setFrame((!editable?"Ver":update?"Modificar":"Crear")+" Producto", 380, 525);
        creationForm.setHeader(!editable?"Obtener datos de un producto":"Agrega datos a un producto", !editable?"Ver datos completos de producto":update?"Actualiza un producto existente en el dashboard":"El nombre del producto debe ser nuevo.");

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagen de producto", "jpeg", "jpg", "gif", "png", "bmp" );
        chooser.setFileFilter(filter);

        // COMPONENTES
        final String[] imageURL = {""};
        if (initialProduct != null)
            imageURL[0] = initialProduct.image;

        XField name = new XField("Nombre: ", 380, initialProduct != null?initialProduct.name:"", editable);
        XField price = new XField("Precio: ", 200, initialProduct != null?Float.toString(initialProduct.price):"", editable);
        XSpinnerField size = new XSpinnerField("Cantidad: ", 1, Integer.MAX_VALUE,152, initialProduct != null?initialProduct.size:1, editable);
        XLabel imageLabel = new XLabel("Imagen: ");
        XButton imageBtn = new XButton("Seleccionar", new Color(150, 150, 150), Color.white);
        XButton cancelBtn = new XButton("Cancelar", new Color(0,0,0,0), new Color(80,80,80));
        XButton confirmBtn = new XButton(!editable?"Aceptar":update?"Modificar":"Crear");
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(initialProduct != null?initialProduct.image:"./src/images/product.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = null;
        if (image != null)
            picLabel = new JLabel(new ImageIcon(image));

        // POSICIONES
        name.setBounds(0,10,380,90);
        price.setBounds(0,100,200,90);
        size.setBounds(202,100,152,90);
        imageLabel.setBounds(25, 210, 150, 30);
        imageBtn.setBounds(25, 250, 150, 50);
        cancelBtn.setBounds(100, 340, 130, 50);
        confirmBtn.setBounds(225, 340, 130, 50);
        if (picLabel != null)
            picLabel.setBounds(230, 208, 115, 115);

        // PROPIEDADES
        imageBtn.setEnabled(editable);

        // EVENTOS
        JLabel finalPicLabel = picLabel;
        imageBtn.onClick((e) ->{
            // ARCHIVO
            chooser.showOpenDialog(null);
            File dataFile = chooser.getSelectedFile();
            imageURL[0] = dataFile.getAbsolutePath();
            try {
                finalPicLabel.setIcon(new ImageIcon(ImageIO.read(new File(imageURL[0]))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // CANCEL
        cancelBtn.onClick((e) -> creationForm.dispose());

        // CONFIRMAR
        Product finalInitialProduct = initialProduct;
        String finalTmpName = tmpName;
        confirmBtn.onClick((e) -> {
            if(editable) {
                // VERIFICAR
                boolean verifyLength = (name.getData().length() * price.getData().length() * size.getData() * imageURL[0].length()) > 0;
                boolean vName = name.getData().length() > 0 && verifyName(name.getData(), update ? finalTmpName : "");

                // LONGITUD
                if(productController.getSize() >= 100) XAlert.showError("Error al agregar", "El numero maximo de productos es 100");

                // VERIFICAR
                else if (verifyLength && vName) {
                    // AGREGAR CLIENTE
                    Product data = new Product(name.getData(), Float.parseFloat(price.getData()), size.getData(), imageURL[0]);
                    if (!update) productController.addData(data);
                    else productController.replaceData(finalInitialProduct, data);

                    // CERRAR Y ACTUALIZAR
                    updateTable();
                    updateChart();
                    creationForm.dispose();
                } else if (!verifyLength)
                    XAlert.showError("Error al " + (update ? "modificar" : "crear"), "Todos los campos son requeridos.");
                else XAlert.showError("Error al " + (update ? "modificar" : "crear"), "El nombre ya esta registrado.");
            } else creationForm.dispose();
        });

        // AGREGAR
        creationForm.addComponent(name);
        creationForm.addComponent(price);
        creationForm.addComponent(size);
        creationForm.addComponent(imageLabel);
        creationForm.addComponent(imageBtn);
        creationForm.addComponent(cancelBtn);
        creationForm.addComponent(confirmBtn);
        creationForm.addComponent(picLabel);
    }

    private void crud(XTabPane tabs){
        // COMPONENTES
        JPanel optionsPanel = new JPanel(null);
        XActionPanel createProduct = new XActionPanel("Productos nuevos", "Crea un producto desde un formulario.", "Crear producto");
        XActionPanel readProduct = new XActionPanel("Obtener información", "Busca un producto por su nombre.", "Ver producto");
        XActionPanel updateProduct = new XActionPanel("Modificar productos", "Edita los datos de un producto por nombre.", "Modificar producto");
        XActionPanel deleteProduct = new XActionPanel("Remover productos", "Elimina todo el registro de un producto.", "Eliminar producto");
        XActionPanel resetProducts = new XActionPanel("Reiniciar dashboard", "Borra todos los datos existentes en el dashboard.", "Borrar todo", new Color(70, 70, 70),new Color(244,67,54));

        // EVENTOS
        createProduct.setAction((e) -> productForm(false, true));
        updateProduct.setAction((e) -> productForm(true, true));
        readProduct.setAction((e) -> productForm(true, false));
        deleteProduct.setAction((e) ->{
            if(productController.getSize() == 0) XAlert.showError("Sin productos", "Aun no existen productos registrados");
            else {
                // BUSCAR CLIENTE
                String tmpName = XAlert.showPrompt("Ingresar nombre");
                Product tmpProduct = findByName(tmpName);

                // BORRAR
                if (tmpProduct != null) {
                    productController.deleteData(tmpProduct);
                    updateTable();
                    updateChart();
                    XAlert.showAlert("Producto borrado", "Se elimino un producto correctamente");
                } else XAlert.showError("Sin datos", "No se encontró ningún producto");
            }
        });
        resetProducts.setAction((e) ->{
            if(productController.getSize() == 0) XAlert.showError("Sin productos", "Aun no existen productos registrados");
            else {
                int erase = XAlert.showConfirm("Esta seguro de querer borrar todo?");
                if(erase == 0) {
                    productController.clear();
                    updateTable();
                    updateChart();
                    XAlert.showAlert("Borrado completo", "Se eliminaron todos los datos de productos");
                }
            }
        });

        // PROPIEDADES
        createProduct.setBounds(0, 0, getWidth() - 150, 75);
        readProduct.setBounds(0, 75, getWidth() - 150, 75);
        updateProduct.setBounds(0, 150, getWidth() - 150, 75);
        deleteProduct.setBounds(0, 225, getWidth() - 150, 75);
        resetProducts.setBounds(0, 316, getWidth() - 150, 75);

        // AGREGAR A PANEL
        optionsPanel.add(createProduct);
        optionsPanel.add(readProduct);
        optionsPanel.add(updateProduct);
        optionsPanel.add(deleteProduct);
        optionsPanel.add(resetProducts);

        // PROPIEDADES
        optionsPanel.setBackground(new Color(220, 220, 220));

        // AGREGAR A TAB
        tabs.addTab(tabsName[1], optionsPanel);
    }
}
