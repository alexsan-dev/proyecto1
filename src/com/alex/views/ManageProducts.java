package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ProductController;
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
    private DefaultTableModel model;
    private XPieChart pieChart;
    private XBarChart barChart;

    public ManageProducts(ProductController productController){
        // DATOS
        this.productController = productController;

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
        for (int clientsIndex = 0; clientsIndex < productController.getSize(); clientsIndex++){
            if(productController.get(clientsIndex).name.equals(name) && (exclude.equals("") || !productController.get(clientsIndex).name.equals(exclude))) {
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
            String[] clientRows = productController.toCsv().split("\n");
            if(productController.getSize() > 0) for (String clientRow : clientRows) model.addRow(clientRow.split(","));
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
        // CONTAR EDADES
        LinkedList<String> products = new LinkedList<>();
        for(int index = 0; index < productController.getSize(); index++){
            // EDAD
            float currentPrice = productController.get(index).price;
            int count = 0;

            // CONTAR
            for(int subIndex = 0; subIndex < productController.getSize(); subIndex++) {
                if (productController.get(subIndex).price == currentPrice) count++;
            }

            // AGREGAR
            products.add(currentPrice + "," + count);
        }

        // DATASET
        if(pieChart != null && barChart != null) {
            barChart.dts.clear();

            for(int dataIndex =0; dataIndex < products.getSize(); dataIndex++){
                String[] vals = products.get(dataIndex).split(",");
                barChart.setValue(Float.parseFloat(vals[0]), Integer.parseInt(vals[1]));
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
        updateChart();

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
        updateTable();
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
                        // VERIFICAR NIT
                        boolean vName = verifyName(data.name, "");

                        if(vName && productController.getSize() <= 100) {
                            // AGREGAR LOCAL
                            productController.addData(data);

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

        scrollPane.setBounds(0,0, getWidth() - 150, getHeight() - 180);

        // DASHBOARD
        dashboard.add(scrollPane);
        dashboard.add(uploadPanel);

        // AGREGAR A TAB
        tabs.addTab(tabsName[2], dashboard);
    }

    private void clientForm(boolean update, boolean editable) {
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

        XField name = new XField("Nombre: ", 200, initialProduct != null?initialProduct.name:"", editable);
        XField price = new XField("Precio: ", 200, initialProduct != null?Float.toString(initialProduct.price):"", editable);
        XField size = new XField("Cantidad: ", 200, initialProduct != null?Integer.toString(initialProduct.size):"", editable);
        XLabel imageLabel = new XLabel("Imagen: ");
        XButton imageBtn = new XButton("Seleccionar imagen", new Color(150, 150, 150), Color.white);
        XButton cancelBtn = new XButton("Cancelar", new Color(0,0,0,0), new Color(80,80,80));
        XButton confirmBtn = new XButton(!editable?"Aceptar":update?"Modificar":"Crear");
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(initialProduct != null?initialProduct.image:"./src/images/profile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = null;
        if (image != null)
            picLabel = new JLabel(new ImageIcon(image));

        // POSICIONES
        name.setBounds(0,10,200,90);
        price.setBounds(0,100,200,90);
        size.setBounds(180,10,200,90);
        imageLabel.setBounds(25, 210, 150, 30);
        imageBtn.setBounds(25, 250, 150, 50);
        cancelBtn.setBounds(155, 340, 100, 50);
        confirmBtn.setBounds(255, 340, 100, 50);
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
                boolean verifyLength = (name.getData().length() * price.getData().length() * size.getData().length() * imageURL[0].length()) > 0;
                boolean vName = name.getData().length() > 0 && verifyName(name.getData(), update ? finalTmpName : "");

                // VERIFICAR
                if (verifyLength && vName) {
                    // AGREGAR CLIENTE
                    Product data = new Product(name.getData(), Float.parseFloat(price.getData()), Integer.parseInt(size.getData()), imageURL[0]);
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
        createProduct.setAction((e) -> clientForm(false, true));
        updateProduct.setAction((e) -> clientForm(true, true));
        readProduct.setAction((e) -> clientForm(true, false));
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
