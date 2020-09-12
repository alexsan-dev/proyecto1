package com.alex.components;

import javax.swing.*;
import java.awt.*;

public class XTabPane extends JTabbedPane {
    String[] labels;

    public XTabPane(String[] labels){
        super(JTabbedPane.LEFT);
        this.labels = labels;
    }

    public void setTabs(){
        for(int labelIndex = 0;labelIndex < labels.length; labelIndex++){
            JLabel lab = new JLabel(labels[labelIndex]);
            lab.setPreferredSize(new Dimension(200, 30));
            setTabComponentAt(labelIndex, lab);
        }
    }
}
