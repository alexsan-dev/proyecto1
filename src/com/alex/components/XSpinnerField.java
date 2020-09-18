package com.alex.components;

import javax.swing.*;
import java.awt.*;

public class XSpinnerField extends JPanel {
    private final XSpinner spinner;
    private final XLabel titleLabel;

    public XSpinnerField(String label, int min, int max, int width, int defValue, boolean editable){
        // LAYOUT
        setLayout(null);

        // COMPONENT
        titleLabel = new XLabel(label);
        spinner = new XSpinner(defValue, min, max, width);

        // LISTA
        setProperties(width, editable);
    }

    public void setRange(int min, int max){
        spinner.setRange(min, max);
    }

    public void setProperties(int width, boolean editable){
        // PROPIEDADES
        setBackground(new Color(220, 220, 220));

        // EDITABLE
        spinner.setEnabled(editable);

        // POSICIONES
        titleLabel.setBounds(0, 0, width, 40);
        spinner.setBounds(0, 40, width, 50);

        // AGREGAR
        add(titleLabel);
        add(spinner);
    }

    public int getData(){
        return spinner.getData();
    }
}
