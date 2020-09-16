package com.alex.views;

import com.alex.components.XButton;
import com.alex.components.XFrame;
import com.alex.components.XTabPane;
import com.alex.components.XTable;
import com.alex.controllers.ClientController;
import com.alex.structures.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManageClients extends XFrame {
    private final String[] tabsName = { "Datos" };
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

        // DASHBOARD
        dashboard(tabs);

        // POSICIONES
        tabs.setBounds(25,0,getWidth() - 25, getHeight() - 80);
        tabs.setTabs();

        // AGREGAR
        addComponent(tabs);
    }

    public JLabel getLabel(String text, int size){
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setForeground(Color.white);
        label.setBackground(new Color(100, 100, 100));
        label.setFont(new Font("Lato", Font.BOLD, size));
        return label;
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
            for (String clientRow : clientRows) model.addRow(clientRow.split(","));
        }

        // UPLOAD PANEL
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel uploadTextPanel = new JPanel(new GridLayout(2, 1));

        JLabel uploadLabel = getLabel("Carga masiva de datos CSV", 17);
        JLabel uploadSubLabel = getLabel("Aquí puedes cargar mas datos al dashboard", 16);
        XButton uploadBtn = new XButton("Subir CSV");

        JPanel dashboard = new JPanel(null);

        // EVENTOS
        uploadBtn.onClick((e) ->{
            // ARCHIVO
            chooser.showOpenDialog(null);
            File dataFile = chooser.getSelectedFile();
            try {
                // LEER Y GUARDAR
                Scanner fileReader = new Scanner(dataFile);
                while(fileReader.hasNextLine()){
                    // CREAR OBJETO
                    String[] dataLine = fileReader.nextLine().split(",");
                    Client data = new Client(dataLine[0], Integer.parseInt(dataLine[1]), dataLine[2].charAt(0), Integer.parseInt(dataLine[3]));

                    // AGREGAR LOCAL
                    if (clientController != null)
                        clientController.addData(data);

                    // AGREGAR A TABLA
                    model.addRow(dataLine);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        // POSICIONES
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(220, 220, 220));
        uploadTextPanel.setBackground(new Color(100, 100, 100));
        uploadTextPanel.setBorder(new EmptyBorder(0,0,0,20));
        uploadPanel.setBorder(new EmptyBorder(10,25,10,25));
        uploadPanel.setBackground(new Color(100, 100, 100));
        uploadPanel.setBounds(0,getHeight() - 184, getWidth() - 150, 75);

        scrollPane.setBounds(0,0, getWidth() - 150, getHeight() - 180);

        // PANEL DE UPLOAD
        uploadTextPanel.add(uploadLabel);
        uploadTextPanel.add(uploadSubLabel);

        uploadPanel.add(uploadTextPanel);
        uploadPanel.add(uploadBtn);

        // DASHBOARD
        dashboard.add(scrollPane);
        dashboard.add(uploadPanel);

        // AGREGAR A TAB
        tabs.addTab(tabsName[0], dashboard);
    }
}
