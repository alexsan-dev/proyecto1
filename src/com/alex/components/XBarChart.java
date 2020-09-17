package com.alex.components;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class XBarChart {
    public DefaultCategoryDataset dts;
    public JPanel chartPanel;

    public XBarChart(String title, String xLabel, String yLabel){
        // DATASET
        dts = new DefaultCategoryDataset();

        // PIE
        JFreeChart jBarChart = ChartFactory.createBarChart(title, xLabel, yLabel, dts, PlotOrientation.VERTICAL, true, true, false);

        // PANEL
        chartPanel = new ChartPanel(jBarChart);
    }

    public void setValue(int age, int count){
        dts.addValue(count, Integer.toString(age), Integer.toString(age));
    }
}
