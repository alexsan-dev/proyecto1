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
        setBackground(new Color(220, 220, 220));
    }
}
