package com.alex.components;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;

public class XPieChart {
    public DefaultPieDataset dts;
    public JPanel chartPanel;

    public XPieChart(String title){
        // DATASET
        dts = new DefaultPieDataset();

        // PIE
        JFreeChart jPieChart = ChartFactory.createPieChart(title, dts, true, true, false);

        // PANEL
        chartPanel = new ChartPanel(jPieChart);
    }

    public void setValue(String text, int count){
        dts.setValue(text, count);
    }
}
