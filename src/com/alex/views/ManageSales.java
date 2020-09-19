package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ClientController;
import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.data.Product;
import com.alex.data.Sell;
import com.alex.structures.LinkedList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManageSales extends XFrame {
    private final String[] tabsName = { "Ventas", "Datos" };
    private final SalesController salesController;
    private final ProductController productController;
    private final ClientController clientController;
    private XComboField product;
    private DefaultTableModel model;

    public ManageSales(SalesController salesController, ProductController productController, ClientController clientController){
        // DATOS
        this.salesController = salesController;
        this.productController = productController;
        this.clientController = clientController;

        // CONFIGURAR VENTANA
        setFrame("Administración de ventas", 800, 500);
        setHeader("Interfaz de administración de ventas", "Esta área tiene como propósito, brindar al usuario herramientas de consulta de datos de ventas.");
    }

    @Override
    public void renderWithin() {
        XTabPane tabs = new XTabPane(tabsName);

        // PANELS
        dashboard(tabs);
        crud(tabs);

        // POSICIONES
        tabs.setBounds(25,0,getWidth() - 25, getHeight() - 80);
        tabs.setTabs();

        // AGREGAR
        addComponent(tabs);
    }

    public void updateTable(){
        // LIMPIAR DATOS
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        // ACTUALIZAR DATOS
        if(salesController != null) {
            String[] sellRows = salesController.toCsv().split("\n");
            if(salesController.getSize() > 0) for (String sellRow : sellRows) model.addRow(sellRow.split(","));
        }
    }

    private Sell findByCode(int code){
        Sell tmpSell = null;

        // BUSCAR
        for(int index = 0; index < salesController.getSize(); index++){
            if(salesController.get(index).code == code) {
                tmpSell = salesController.get(index);
                break;
            }
        }

        return tmpSell;
    }


    private void dashboard(XTabPane tabs){
        // DATOS
        String[] columns = { "Código", "NIT Cliente", "Producto", "Cantidad" };

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Ventas en CSV", "csv" );
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
                if(dataFile != null) {
                    Scanner fileReader = new Scanner(dataFile);
                    while (fileReader.hasNextLine()) {
                        // LEER VALORES
                        String[] dataLine = fileReader.nextLine().split(",");
                        int tmpCode = Integer.parseInt(dataLine[0]);
                        int tmpNit = Integer.parseInt(dataLine[1]);
                        int size = Integer.parseInt(dataLine[3]);
                        String product = dataLine[2];

                        // VENDER
                        sellProducts(tmpCode, tmpNit, product, size);
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
        tabs.addTab(tabsName[0], dashboard);
    }

    private void updateProducts(int size, Product currentProduct, Sell data, boolean isNew){
        // VERIFICAR EXISTENCIA
        if(currentProduct.size - size >= 0) {
            // REDUCIR CANTIDAD EN
            Product tmpProduct = new Product(currentProduct.name, currentProduct.price, currentProduct.size - size, currentProduct.image);
            productController.replaceData(currentProduct, tmpProduct);

            // AGREGAR VENTA
            if(isNew)
                salesController.addData(data);
            else{
                Sell tmpSell = salesController.getByCode(data.code);
                salesController.replaceData(tmpSell, data);
            }
        } else XAlert.showError("Error al vender", "Ya no existen mas productos " + currentProduct.name + " en inventario");
    }

    private void sellProducts(int code, int nit, String product, int size){
        // PRODUCTO
        Product tmpProduct = productController.getByName(product);

        // VENTA
        Sell tmpSell = salesController.getByCode(code);
        if(tmpSell == null){
            if(salesController.getSize() >= 1 && code != salesController.get(salesController.getSize() - 1).code + 1) XAlert.showError("Error al vender", "El correlativo de venta no es valido.");
            else {
                // CREAR LISTAS
                LinkedList<Product> sellProducts = new LinkedList<>();
                LinkedList<String> sizes = new LinkedList<>();
                LinkedList<String> ivas = new LinkedList<>();

                // IVA
                float iva = (float) (tmpProduct.price * 0.12 * size);

                // AGREGAR A LISTA
                sellProducts.add(tmpProduct);
                sizes.add(Integer.toString(size));
                ivas.add(Float.toString(iva));

                Sell newSell = new Sell(code, nit, sizes, ivas, sellProducts);

                // REDUCIR CANTIDAD DE PRODUCTOS
                updateProducts(size, tmpProduct, newSell, true);
                updateTable();
            }
        } else{
            // VENDER
            if(tmpSell.products.getSize() < 5) {
                // IVA
                float iva = (float) (tmpProduct.price * 0.12 * size);

                // AGREGAR
                tmpSell.products.add(tmpProduct);
                tmpSell.sizes.add(Integer.toString(size));
                tmpSell.ivas.add(Float.toString(iva));
            }

            else XAlert.showError("Error al vender", "La cantidad maxima de productos por venta es 5");

            // REDUCIR CANTIDAD DE PRODUCTOS
            updateProducts(size, tmpProduct, tmpSell, false);
            updateTable();
        }
    }

    private Product[] updateProductList(XComboField product){
        // LIMPIAR
        product.clear();

        // AGREGAR
        for(int index = 0; index < productController.getSize(); index++)
            if (productController.get(index).size > 0) product.addItem(productController.get(index).name);

        // ARRAY
        String[] names = product.toArray();
        Product[] products = new Product[names.length];

        for(int index = 0; index < names.length; index++)
            products[index] = productController.getByName(names[index]);

        return products;
    }

    private void sellForm(boolean editable) {
        // PEDIR NIT PRIMERO
        Sell initialSell = null;

        // BUSCAR CLIENTE
        if(!editable) {
            if(salesController.getSize() == 0) {
                XAlert.showError("Sin ventas", "Aun no existen ventas registradas");
                return;
            }

            // NIT
            int tmpCode = Integer.parseInt(XAlert.showPrompt("Ingresar código"));
            if(tmpCode >= 0){
                initialSell = findByCode(tmpCode);

                if(initialSell == null) {
                    XAlert.showError("Sin datos", "No se encontró ninguna venta");
                    return;
                }
            } else {
                XAlert.showError("Código invalido", "El código ingresado no es valido");
                return;
            }
        } else {
            if(clientController.getSize() == 0){
                XAlert.showError("Sin clientes", "Aun no existen clientes registrados");
                return;
            }
            else if(productController.getSize() == 0) {
                XAlert.showError("Sin productos", "Aun no existen productos registrados");
                return;
            }
        }

        // CREAR FRAME
        XFrame creationForm = new XFrame();
        creationForm.setFrame((!editable?"Ver":"Crear")+" Venta", 380, !editable?565:390);
        creationForm.setHeader(!editable?"Obtener datos de una venta":"Agrega datos a una venta", !editable?"Ver datos completos de la venta":"El código de la venta debe ser nuevo.");

        // VALORES INICIALES
        int initialCode = salesController.getSize() > 0? salesController.get(salesController.getSize() - 1).code: 0;
        String[] nits = clientController.getNits();
        String[] products = productController.getNames();

        // COMPONENTES
        product = new XComboField("Producto: ", products, 152, initialSell != null?initialSell.products.get(0).name:"", editable);
        Product[] newProducts = updateProductList(product);
        int defMax = !editable?Integer.parseInt(initialSell.sizes.get(0)):newProducts[0].size;

        XField code = new XField("Código: ", 200, initialSell != null?Integer.toString(initialSell.code):Integer.toString(initialCode + 1), false);
        XComboField nit = new XComboField("NIT: ", nits,152, initialSell != null?Integer.toString(initialSell.nit):"", editable);
        XSpinnerField size = new XSpinnerField("Cantidad: ",!editable?0:1, defMax, 152, initialSell != null?Integer.parseInt(initialSell.sizes.get(0)):1, editable);
        XButton cancelBtn = new XButton("Cancelar", new Color(0,0,0,0), new Color(80,80,80));
        XButton confirmBtn = new XButton(!editable?"Aceptar":"Crear");

        // VISTA GENERAL
        if(!editable) {
            // COMPONENTES
            XLabel sellsLabel = new XLabel("Productos: ");
            float ivasTotal = 0;
            float priceTotal = 0;

            // RECORRER VENTAS
            for (int dIndex = 0; dIndex < 5; dIndex++) {
                // CREAR LABEL
                XLabel tmpLabel;
                if(initialSell.products.get(dIndex) != null) {
                    tmpLabel = new XLabel(initialSell.sizes.get(dIndex) + " de " + initialSell.products.get(dIndex).name + " con Q" + initialSell.ivas.get(dIndex) + " iva");
                    ivasTotal += Float.parseFloat(initialSell.ivas.get(dIndex));
                    priceTotal += initialSell.products.get(dIndex).price * Integer.parseInt(initialSell.sizes.get(dIndex));
                } else tmpLabel = new XLabel("-------------------------------");

                // POSICIONES
                tmpLabel.setBounds(25, 140 + (dIndex * 30), 380, 40);
                creationForm.addComponent(tmpLabel);
            }

            // LABEL DE TOTAL
            XLabel ivasTotalLabel = new XLabel("Total IVA: Q" + ivasTotal);
            XLabel pricesTotalLabel = new XLabel("Total de venta: Q" + priceTotal);

            // POSICIONES
            ivasTotalLabel.setBounds(25, 290, 200, 40);
            pricesTotalLabel.setBounds(25, 330, 300, 40);
            sellsLabel.setBounds(25, 110, 200, 40);

            // AGREGAR
            creationForm.addComponent(sellsLabel);
            creationForm.addComponent(pricesTotalLabel);
            creationForm.addComponent(ivasTotalLabel);
        }


        // EVENTOS
        product.setListener(e -> {
            int tmpIndex = Math.max(product.getIndex(), 0);
            size.setRange(1, newProducts[tmpIndex].size);
        });

        // POSICIONES
        code.setBounds(0,10,200,90);
        nit.setBounds(202,10,152,90);

        if(editable) {
            product.setBounds(25, 100, 152, 90);
            size.setBounds(202, 100, 152, 90);
        }

        cancelBtn.setBounds(100, !editable?380:210, 130, 50);
        confirmBtn.setBounds(225, !editable?380:210, 130, 50);

        // CANCEL
        cancelBtn.onClick((e) -> creationForm.dispose());

        // CONFIRMAR
        confirmBtn.onClick((e) -> {
            if(editable) {
                // VERIFICAR
                boolean verifyLength = (code.getData().length() * nit.getData().length() * size.getData() * product.getData().length()) > 0;

                // LONGITUD
                if(salesController.getSize() >= 100) XAlert.showError("Error al agregar", "El numero maximo de ventas es 100");

                // VERIFICAR
                if (verifyLength) {
                    // VENDER
                    sellProducts(Integer.parseInt(code.getData()), Integer.parseInt(nit.getData()), product.getData(), size.getData());

                    // CERRAR
                    updateProductList(product);
                    creationForm.dispose();
                } else XAlert.showError("Error al crear" ,"Todos los campos son requeridos.");
            } else creationForm.dispose();
        });

        // AGREGAR
        creationForm.addComponent(code);
        creationForm.addComponent(nit);

        if(editable) {
            creationForm.addComponent(product);
            creationForm.addComponent(size);
        }

        creationForm.addComponent(cancelBtn);
        creationForm.addComponent(confirmBtn);
    }

    private void detailForm(){
        // CONFIGURAR PANEL
        XFrame detail = new XFrame();
        detail.setFrame("Detalle de ventas", 325, 328);
        detail.setHeader("Información completa", "Aquí se detallan todas las ventas");

        // DATOS
        float totalCount = salesController.getTotalSell();
        float totalIVA = salesController.getTotalIVA();
        int totalSell = salesController.getMaxSize();
        float totalPrice = salesController.getMaxSell();

        // COMPONENTES
        XLabel total = new XLabel("Total de ventas:" );
        XLabel iva = new XLabel("Total de IVA:");
        XLabel maxProducts = new XLabel("Mas vendidos:");
        XLabel maxPrice = new XLabel("Mayor venta:");

        XButton totalBtn = new XButton("Q"+totalCount);
        XButton ivaBtn = new XButton("Q"+totalIVA);
        XButton maxPBtn = new XButton(totalSell + "");
        XButton maxPrBtn = new XButton("Q"+totalPrice);

        // POSICIONES
        total.setBounds(25, 15, 150, 40);
        iva.setBounds(25, 65, 150, 40);
        maxProducts.setBounds(25, 115, 150, 40);
        maxPrice.setBounds(25, 165, 150, 40);

        totalBtn.setBounds(170, 15, 130, 40);
        ivaBtn.setBounds(170, 65, 130, 40);
        maxPBtn.setBounds(170, 115, 130, 40);
        maxPrBtn.setBounds(170, 165, 130, 40);

        // PROPIEDADES
        totalBtn.setEnabled(false);
        ivaBtn.setEnabled(false);
        maxPBtn.setEnabled(false);
        maxPrBtn.setEnabled(false);

        // AGREGAR
        detail.addComponent(total);
        detail.addComponent(iva);
        detail.addComponent(maxProducts);
        detail.addComponent(maxPrice);
        detail.addComponent(totalBtn);
        detail.addComponent(ivaBtn);
        detail.addComponent(maxPBtn);
        detail.addComponent(maxPrBtn);
    }

    private void crud(XTabPane tabs){
        // COMPONENTES
        JPanel optionsPanel = new JPanel(null);
        XActionPanel createSell = new XActionPanel("Ventas nuevas", "Crea una venta desde un formulario.", "Crear venta");
        XActionPanel readSell = new XActionPanel("Obtener venta", "Busca una venta por su código.", "Ver venta");
        XActionPanel detailSell = new XActionPanel("Obtener información", "Ver detalles de todas las ventas.", "Ver detalle");

        // EVENTOS
        createSell.setAction(e -> sellForm(true));
        readSell.setAction(e -> sellForm(false));
        detailSell.setAction(e -> {
            if(salesController.getSize() > 0)
                detailForm();
            else XAlert.showError("Error al obtener", "Aun no existen ventas registradas");
        });

        // PROPIEDADES
        createSell.setBounds(0, 0, getWidth() - 150, 75);
        readSell.setBounds(0, 75, getWidth() - 150, 75);
        detailSell.setBounds(0, 150, getWidth() - 150, 75);

        // AGREGAR A PANEL
        optionsPanel.add(createSell);
        optionsPanel.add(readSell);
        optionsPanel.add(detailSell);

        // PROPIEDADES
        optionsPanel.setBackground(new Color(220, 220, 220));

        // AGREGAR A TAB
        tabs.addTab(tabsName[1], optionsPanel);
    }
}
