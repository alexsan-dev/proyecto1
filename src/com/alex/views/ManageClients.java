package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ClientController;
import com.alex.data.Client;

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

public class ManageClients extends XFrame {
    private final String[] tabsName = { "Clientes", "Editar" };
    private final ClientController clientController;
    private DefaultTableModel model;

    public ManageClients(ClientController clientController){
        // DATOS
        this.clientController = clientController;

        // CONFIGURAR VENTANA
        setFrame("Administración de clientes", 800, 500);
        setHeader("Interfaz de administración de clientes", "Esta área tiene como propósito, brindar al usuario herramientas de consulta de datos de clientes.");
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

    private boolean verifyNIT(int nit, int exclude){
        // VERIFICAR NIT
        boolean verifyNIT = true;
        for (int clientsIndex = 0; clientsIndex < clientController.getSize(); clientsIndex++){
            if(clientController.get(clientsIndex).nit == nit && (exclude == -1 || clientController.get(clientsIndex).nit != exclude)) {
                verifyNIT = false;
                break;
            }
        }

        return verifyNIT;
    }

    public void updateTable(){
        // LIMPIAR DATOS
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        // ACTUALIZAR DATOS
        if(clientController != null) {
            String[] clientRows = clientController.toCsv().split("\n");
            if(clientController.getSize() > 0) for (String clientRow : clientRows) model.addRow(clientRow.split(","));
        }
    }

    private Client findByNIT(int nit){
        Client tmpClient = null;

        // BUSCAR
        for(int index =0; index < clientController.getSize();index++){
            if(clientController.get(index).nit == nit) {
                tmpClient = clientController.get(index);
                break;
            }
        }

        return tmpClient;
    }

    private void dashboard(XTabPane tabs){
        // DATOS
        String[] columns = { "Nombre", "Edad", "Sexo", "NIT" };

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Clientes en CSV", "csv" );
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
                    Client data = new Client(dataLine[0], Integer.parseInt(dataLine[1]), dataLine[2].charAt(0), Integer.parseInt(dataLine[3]), dataLine[4]);

                    // VERIFICAR
                    if (clientController != null) {
                        // VERIFICAR NIT
                        boolean vNit = verifyNIT(data.nit, -1);

                        if(vNit && clientController.getSize() <= 100) {
                            // AGREGAR LOCAL
                            clientController.addData(data);

                            // AGREGAR A TABLA
                            model.addRow(dataLine);
                        } else if(clientController.getSize() > 100) XAlert.showError("Error al agregar", "El numero maximo de clientes es de 100");
                        else if(!vNit) XAlert.showError("Error al agregar", "Ya existe un cliente con el mismo NIT");

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

    private void clientForm(boolean update, boolean editable) {
        // PEDIR NIT PRIMERO
        int tmpNit;
        Client initialClient = null;

        // BUSCAR CLIENTE
        if(update) {
            if(clientController.getSize() == 0) {
                XAlert.showError("Sin clientes", "Aun no existen clientes registrados");
                return;
            }

            // NIT
            tmpNit = Integer.parseInt(XAlert.showPrompt("Ingresar nit"));
            if(tmpNit != -1 ){
                initialClient = findByNIT(tmpNit);

                if(initialClient == null) {
                    XAlert.showError("Sin datos", "No se encontró ningún cliente");
                    return;
                }
            } else {
                XAlert.showError("Nit invalido", "El nit ingresado no es valido");
                return;
            }
        }

        // CREAR FRAME
        XFrame creationForm = new XFrame();
        creationForm.setFrame((!editable?"Ver":update?"Modificar":"Crear")+" Cliente", 380, 525);
        creationForm.setHeader(!editable?"Obtener datos de un cliente":"Agrega datos a un cliente", !editable?"Ver datos completos de cliente":update?"Actualiza un cliente existente en el dashboard":"El NIT del cliente debe ser nuevo en los datos.");

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Avatar de cliente", "jpeg", "jpg", "gif", "png", "bmp" );
        chooser.setFileFilter(filter);

        // COMPONENTES
        final String[] avatarURL = {""};
        if (initialClient != null)
            avatarURL[0] = initialClient.image;

        XField name = new XField("Nombre: ", 200, initialClient != null?initialClient.name:"", editable);
        XField age = new XField("Edad: ", 200, initialClient != null?Integer.toString(initialClient.age):"", editable);
        XField sex = new XField("Sexo: ", 200, initialClient != null?Character.toString(initialClient.sex):"", editable);
        XField nit = new XField("NIT: ", 200, initialClient != null?Integer.toString(initialClient.nit):"", editable);
        XLabel imageLabel = new XLabel("Avatar: ");
        XButton imageBtn = new XButton("Seleccionar imagen", new Color(150, 150, 150), Color.white);
        XButton cancelBtn = new XButton("Cancelar", new Color(0,0,0,0), new Color(80,80,80));
        XButton confirmBtn = new XButton(!editable?"Aceptar":update?"Modificar":"Crear");
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(initialClient != null?initialClient.image:"./src/images/profile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = null;
        if (image != null)
            picLabel = new JLabel(new ImageIcon(image));

        // POSICIONES
        name.setBounds(0,10,200,90);
        age.setBounds(0,100,200,90);
        sex.setBounds(180,10,200,90);
        nit.setBounds(180, 100, 200, 90);
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
            avatarURL[0] = dataFile.getAbsolutePath();
            try {
                finalPicLabel.setIcon(new ImageIcon(ImageIO.read(new File(avatarURL[0]))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // CANCEL
        cancelBtn.onClick((e) -> creationForm.dispose());

        // CONFIRMAR
        Client finalInitialClient = initialClient;
        confirmBtn.onClick((e) -> {
            if(editable) {
                // VERIFICAR
                boolean verifyLength = (name.getData().length() * age.getData().length() * sex.getData().length() * nit.getData().length() * avatarURL[0].length()) > 0;
                boolean vNit = nit.getData().length() > 0 && verifyNIT(Integer.parseInt(nit.getData()), update ? Integer.parseInt(nit.getData()) : -1);

                // VERIFICAR
                if (verifyLength && vNit) {
                    // AGREGAR CLIENTE
                    Client data = new Client(name.getData(), Integer.parseInt(age.getData()), sex.getData().charAt(0), Integer.parseInt(nit.getData()), avatarURL[0]);
                    if (!update) clientController.addData(data);
                    else clientController.replaceData(finalInitialClient, data);

                    // CERRAR Y ACTUALIZAR
                    updateTable();
                    creationForm.dispose();
                } else if (!verifyLength)
                    XAlert.showError("Error al " + (update ? "modificar" : "crear"), "Todos los campos son requeridos.");
                else XAlert.showError("Error al " + (update ? "modificar" : "crear"), "El nit ya esta registrado.");
            } else creationForm.dispose();
            });

        // AGREGAR
        creationForm.addComponent(name);
        creationForm.addComponent(age);
        creationForm.addComponent(sex);
        creationForm.addComponent(nit);
        creationForm.addComponent(imageLabel);
        creationForm.addComponent(imageBtn);
        creationForm.addComponent(cancelBtn);
        creationForm.addComponent(confirmBtn);
        creationForm.addComponent(picLabel);
    }

    private void crud(XTabPane tabs){
        // COMPONENTES
        JPanel optionsPanel = new JPanel(null);
        XActionPanel createClient = new XActionPanel("Clientes nuevos", "Crea un cliente desde un formulario.", "Crear cliente");
        XActionPanel readClient = new XActionPanel("Obtener información", "Busca un cliente por su numero de NIT.", "Ver cliente");
        XActionPanel updateClient = new XActionPanel("Modificar clientes", "Edita los datos de un cliente por NIT.", "Modificar cliente");
        XActionPanel deleteClient = new XActionPanel("Remover clientes", "Elimina todo el registro de un cliente.", "Eliminar cliente");
        XActionPanel resetClients = new XActionPanel("Reiniciar dashboard", "Borra todos los datos existentes en el dashboard.", "Borrar todo", new Color(70, 70, 70),new Color(244,67,54));

        // EVENTOS
        createClient.setAction((e) -> clientForm(false, true));
        updateClient.setAction((e) -> clientForm(true, true));
        readClient.setAction((e) -> clientForm(true, false));
        deleteClient.setAction((e) ->{
            if(clientController.getSize() == 0) XAlert.showError("Sin clientes", "Aun no existen clientes registrados");
            else {
                // BUSCAR CLIENTE
                int tmpNit = Integer.parseInt(XAlert.showPrompt("Ingresar nit"));
                Client tmpClient = findByNIT(tmpNit);

                // BORRAR
                if (tmpClient != null) {
                    clientController.deleteData(tmpClient);
                    updateTable();
                    XAlert.showAlert("Cliente borrado", "Se elimino un cliente correctamente");
                } else XAlert.showError("Sin datos", "No se encontró ningún cliente");
            }
        });
        resetClients.setAction((e) ->{
            if(clientController.getSize() == 0) XAlert.showError("Sin clientes", "Aun no existen clientes registrados");
            else {
                int erase = XAlert.showConfirm("Esta seguro de querer borrar todo?");
                if(erase == 0) {
                    clientController.clear();
                    updateTable();
                    XAlert.showAlert("Borrado completo", "Se eliminaron todos los datos de clientes");
                }
            }
        });

        // PROPIEDADES
        createClient.setBounds(0, 0, getWidth() - 150, 75);
        readClient.setBounds(0, 75, getWidth() - 150, 75);
        updateClient.setBounds(0, 150, getWidth() - 150, 75);
        deleteClient.setBounds(0, 225, getWidth() - 150, 75);
        resetClients.setBounds(0, 316, getWidth() - 150, 75);

        // AGREGAR A PANEL
        optionsPanel.add(createClient);
        optionsPanel.add(readClient);
        optionsPanel.add(updateClient);
        optionsPanel.add(deleteClient);
        optionsPanel.add(resetClients);

        // PROPIEDADES
        optionsPanel.setBackground(new Color(220, 220, 220));

        // AGREGAR A TAB
        tabs.addTab(tabsName[1], optionsPanel);
    }
}
