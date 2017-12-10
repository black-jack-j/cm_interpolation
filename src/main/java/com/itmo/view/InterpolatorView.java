package com.itmo.view;

import com.itmo.model.Interpolator;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;

import javax.swing.*;
import java.util.function.DoubleFunction;

public class InterpolatorView{

    private Interpolator interpolator;
    private int elementNum;

    public JFrame getChartView() {
        return new SwingWrapper<XYChart>(chart).displayChart();
    }

    private XYChart chart;

    public InterpolatorView(Interpolator interpolator, int elementNum){
        this.elementNum = elementNum;
        setInterpolator(interpolator);
    }


    public XYChart getChart(){
        return this.chart;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        double[][] dots = getDots(interpolator.getPoints().get(0).getX(),
                interpolator.getPoints().get(interpolator.getPoints().size()-1).getX(),
                this.elementNum, interpolator);
        chart = QuickChart.getChart(interpolator.getClass().getSimpleName(), "x", "y",
                "myChart", dots[0], dots[1]);
    }

    private double[][] getDots(double start, double stop, int size, DoubleFunction<Double> function){
        double reply[][] = new double[2][size];
        double diff = (stop-start)/size;
        for(int i=0;i < size;i++){
            reply[0][i] = start;
            reply[1][i] = function.apply(start);
            start+=diff;
        }
        return reply;
    }

    public int getElementNum() {
        return elementNum;
    }

    public void setElementNum(int elementNum) {
        this.elementNum = elementNum;
    }
}
