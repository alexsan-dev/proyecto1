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
            XLabel lab = new XLabel(labels[labelIndex]);
            lab.setPreferredSize(new Dimension(100, 40));
            setTabComponentAt(labelIndex, lab);
        }
    }
}
