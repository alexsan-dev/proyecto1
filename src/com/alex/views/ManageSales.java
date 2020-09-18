package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ClientController;
import com.alex.controllers.ProductController;
import com.alex.controllers.SalesController;
import com.alex.data.Product;
import com.alex.data.Sell;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManageSales extends XFrame {
    private final String[] tabsName = { "Ventas", "Editar" };
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

    private boolean vCode(int code, int exclude){
        // VERIFICAR NIT
        boolean vCode = true;
        for (int codesIndex = 0; codesIndex < salesController.getSize(); codesIndex++){
            if(salesController.get(codesIndex).code == code && (exclude == -1 || salesController.get(codesIndex).code != exclude)) {
                vCode = false;
                break;
            }
        }

        return vCode;
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
                Scanner fileReader = new Scanner(dataFile);
                while(fileReader.hasNextLine()){
                    // CREAR OBJETO
                    String[] dataLine = fileReader.nextLine().split(",");
                    Sell data = new Sell(Integer.parseInt(dataLine[0]), Integer.parseInt(dataLine[1]), dataLine[2], Integer.parseInt(dataLine[3]));

                    // VERIFICAR
                    if (salesController != null) {
                        // VERIFICAR NIT
                        boolean vCode = vCode(data.code, -1);

                        if(vCode && salesController.getSize() <= 100) {
                            // AGREGAR LOCAL
                            updateProducts(data);

                            // AGREGAR A TABLA
                            updateTable();
                            if(product != null) updateProductList(product);
                        } else if(salesController.getSize() > 100) XAlert.showError("Error al agregar", "El numero maximo de ventas es de 100");
                        else if(!vCode) XAlert.showError("Error al agregar", "Ya existe una venta con el mismo código");

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
        tabs.addTab(tabsName[0], dashboard);
    }

    private void updateProducts(Sell data){
        // AGREGAR PRODUCTO
        Product currentProduct = productController.getByName(data.product);
        int selSize = data.size;

        // VERIFICAR CÓDIGO
        if(data.code > 1 && data.code != salesController.get(salesController.getSize() - 1).code + 1){
            XAlert.showError("Error al vender", "El código no es valido");
            return;
        }

            // VERIFICAR EXISTENCIA
        if(currentProduct.size - selSize >= 0) {
            // REDUCIR CANTIDAD EN
            Product tmpProduct = new Product(currentProduct.name, currentProduct.price, currentProduct.size - selSize, currentProduct.image);
            productController.replaceData(currentProduct, tmpProduct);

            // AGREGAR VENTA
            salesController.addData(data);
        } else XAlert.showError("Error al vender", "Ya no existen mas productos " + currentProduct.name + "en inventario");
    }

    private Product[] updateProductList(XComboField product){
        // LIMPIAR
        product.clear();

        // AGREGAR
        for(int index = 0; index < productController.getSize(); index++) {
            if (productController.get(index).size > 0) product.addItem(productController.get(index).name);
        }

        // ARRAY
        String[] names = product.toArray();
        Product[] products = new Product[names.length];

        for(int index = 0; index < names.length; index++)
            products[index] = productController.getByName(names[index]);

        return products;
    }

    private void sellForm(boolean editable) {
        // PEDIR NIT PRIMERO
        int tmpCode = -1;
        Sell initialSell = null;

        // BUSCAR CLIENTE
        if(!editable) {
            if(salesController.getSize() == 0) {
                XAlert.showError("Sin ventas", "Aun no existen ventas registradas");
                return;
            }

            // NIT
            tmpCode = Integer.parseInt(XAlert.showPrompt("Ingresar código"));
            if(tmpCode != -1){
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
        creationForm.setFrame((!editable?"Ver":"Crear")+" Venta", 380, 390);
        creationForm.setHeader(!editable?"Obtener datos de una venta":"Agrega datos a una venta", !editable?"Ver datos completos de la venta":"El código de la venta debe ser nuevo.");

        // VALORES INICIALES
        int initialCode = salesController.getSize() > 0? salesController.get(salesController.getSize() - 1).code: 0;
        String[] nits = clientController.getNits();
        String[] products = productController.getNames();

        // COMPONENTES
        product = new XComboField("Producto: ", products, 152, initialSell != null?initialSell.product:"", editable);
        Product[] newProducts = updateProductList(product);

        XField code = new XField("Código: ", 200, initialSell != null?Integer.toString(initialSell.code):Integer.toString(initialCode + 1), false);
        XComboField nit = new XComboField("NIT: ", nits,152, initialSell != null?Integer.toString(initialSell.nit):"", editable);
        XSpinnerField size = new XSpinnerField("Cantidad: ",1, newProducts[0].size, 152, initialSell != null?initialSell.size:1, editable);
        XButton cancelBtn = new XButton("Cancelar", new Color(0,0,0,0), new Color(80,80,80));
        XButton confirmBtn = new XButton(!editable?"Aceptar":"Crear");

        // EVENTOS
        product.setListener(e -> size.setRange(1, newProducts[product.getIndex()].size));

        // POSICIONES
        code.setBounds(0,10,200,90);
        nit.setBounds(202,10,152,90);
        product.setBounds(25,100,152,90);
        size.setBounds(202, 100, 152, 90);
        cancelBtn.setBounds(155, 210, 100, 50);
        confirmBtn.setBounds(255, 210, 100, 50);

        // CANCEL
        cancelBtn.onClick((e) -> creationForm.dispose());

        // CONFIRMAR
        int finalTmpCode = tmpCode;
        confirmBtn.onClick((e) -> {
            if(editable) {
                // VERIFICAR
                boolean verifyLength = (code.getData().length() * nit.getData().length() * size.getData() * product.getData().length()) > 0;
                boolean vCode = code.getData().length() > 0 && vCode(Integer.parseInt(code.getData()), finalTmpCode);

                // VERIFICAR
                if (verifyLength && vCode) {
                    // AGREGAR CLIENTE
                    Sell data = new Sell(Integer.parseInt(code.getData()), Integer.parseInt(nit.getData()),product.getData(), size.getData());
                    updateProducts(data);

                    // CERRAR Y ACTUALIZAR
                    updateTable();
                    creationForm.dispose();
                    updateProductList(product);
                } else if (!verifyLength)
                    XAlert.showError("Error al crear" ,"Todos los campos son requeridos.");
                else XAlert.showError("Error al crear", "El código ya esta registrado.");
            } else creationForm.dispose();
        });

        // AGREGAR
        creationForm.addComponent(code);
        creationForm.addComponent(nit);
        creationForm.addComponent(product);
        creationForm.addComponent(size);
        creationForm.addComponent(cancelBtn);
        creationForm.addComponent(confirmBtn);
    }

    private void crud(XTabPane tabs){
        // COMPONENTES
        JPanel optionsPanel = new JPanel(null);
        XActionPanel createSell = new XActionPanel("Ventas nuevas", "Crea una venta desde un formulario.", "Crear venta");
        XActionPanel readSell = new XActionPanel("Obtener información", "Busca una venta por su código.", "Ver venta");

        // EVENTOS
        createSell.setAction((e) -> sellForm(true));
        readSell.setAction((e) -> sellForm(false));

        // PROPIEDADES
        createSell.setBounds(0, 0, getWidth() - 150, 75);
        readSell.setBounds(0, 75, getWidth() - 150, 75);

        // AGREGAR A PANEL
        optionsPanel.add(createSell);
        optionsPanel.add(readSell);

        // PROPIEDADES
        optionsPanel.setBackground(new Color(220, 220, 220));

        // AGREGAR A TAB
        tabs.addTab(tabsName[1], optionsPanel);
    }
}
