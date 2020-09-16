package com.alex.views;

import com.alex.components.*;
import com.alex.controllers.ClientController;
import com.alex.data.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManageClients extends XFrame {
    private final String[] tabsName = { "Clientes", "Editar" };
    private final ClientController clientController;

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

    public void dashboard(XTabPane tabs){
        // DATOS
        String[] columns = { "Nombre", "Edad", "Sexo", "NIT" };

        // FILECHOOSER
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Clientes en CSV", "csv" );
        chooser.setFileFilter(filter);

        // TABLA
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        XTable table = new XTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // DATOS INICIALES
        if(clientController != null) {
            String[] clientRows = clientController.toCsv().split("\n");
            if(clientController.getSize() > 0) for (String clientRow : clientRows) model.addRow(clientRow.split(","));
        }

        // UPLOAD PANEL
        XActionPanel uploadPanel = new XActionPanel("Carga masiva de datos CSV", "Aquí puedes cargar mas datos al dashboard", "Subir CSV");
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
                        boolean verifyNIT = true;
                        for (int clientsIndex = 0; clientsIndex < clientController.getSize(); clientsIndex++){
                            if(clientController.get(clientsIndex).nit == data.nit) {
                                verifyNIT = false;
                                break;
                            }
                        }

                        if(verifyNIT && clientController.getSize() <= 100) {
                            // AGREGAR LOCAL
                            clientController.addData(data);

                            // AGREGAR A TABLA
                            model.addRow(dataLine);
                        } else if(clientController.getSize() > 100) JOptionPane.showMessageDialog(null, "El numero maximo de clientes es de 100", "Error al agregar", JOptionPane.ERROR_MESSAGE);
                        else if(!verifyNIT) JOptionPane.showMessageDialog(null, "Ya existe un cliente con el mismo NIT", "Error al agregar", JOptionPane.ERROR_MESSAGE);

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

    public void crud(XTabPane tabs){
        // COMPONENTES
        JPanel optionsPanel = new JPanel(null);
        XActionPanel createClient = new XActionPanel("Clientes nuevos", "Crea un cliente desde un formulario.", "Crear cliente");
        XActionPanel readClient = new XActionPanel("Obtener información", "Busca un cliente por su numero de NIT.", "Ver cliente");
        XActionPanel updateClient = new XActionPanel("Modificar clientes", "Edita los datos de un cliente por NIT.", "Modificar cliente");
        XActionPanel deleteClient = new XActionPanel("Remover clientes", "Elimina todo el registro de un cliente.", "Eliminar cliente");
        XActionPanel resetClients = new XActionPanel("Reiniciar dashboard", "Borra todos los datos existentes en el dashboard.", "Borrar todo", new Color(70, 70, 70),new Color(244,67,54));

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
