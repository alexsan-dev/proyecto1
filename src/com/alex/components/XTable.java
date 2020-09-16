package com.alex.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class XTable extends JTable {
    public XTable(DefaultTableModel model){
        // CONFIGURAR JTABLE
        super(model);

        setProperties();
    }

    public void setProperties(){
        setForeground(new Color(70, 70, 70));
        getTableHeader().setForeground(new Color(100, 100, 100));
        getTableHeader().setFont(new Font("Lato", Font.BOLD, 17));
        setFont(new Font("Lato", Font.BOLD, 17));
        setRowHeight(getRowHeight()+10);
        setBackground(new Color(220, 220, 220));
    }
}
