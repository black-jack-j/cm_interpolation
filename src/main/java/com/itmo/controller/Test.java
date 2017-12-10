package com.itmo.controller;

import com.itmo.model.*;
import static java.lang.Math.*;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.None;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.DoubleFunction;

public class Test {
    public static void main(String[] args){
        Double[] x1 = new Double[]{PI/2, PI, 3*PI/4, 2*PI};
        Double[] y1 = new Double[x1.length];
        for(int i=0;i<x1.length;i++) y1[i] = sin(x1[i]);
        Double[] x2 = new Double[]{0.0, PI/4, PI/2, 3*PI/4, PI, 5*PI/4, 3*PI/2, 7*PI/4, 2*PI};
        Double[] y2 = new Double[x2.length];
        for(int i=0;i<x2.length;i++) y2[i] = sin(x2[i]);
        Double[] y3 = Arrays.copyOf(y2, y2.length);
        y3[4] = -2.0;
        Double[] x3 = new Double[]{9*PI/2, 19*PI/2, 29*PI/2, 39*PI/2, 49*PI/2, 59*PI/2, 69*PI/2, 79*PI/2, 89*PI, 99*PI/2};
        Double[] y4 = new Double[x3.length];
        for(int i=0;i<x3.length;i++) y4[i] = sin(x3[i]);
        Class<? extends Interpolator>[] iClasses = new Class[]{LagrangeInterpolator.class,
                                                                NewtonInterpolator.class,
                                                                BiCubeInterpolator.class};
        for(int j=0;j<iClasses.length;j++){
            int size = 500;
            double[][] dots = getDots(0, PI * 50, size, new DoubleFunction<Double>() {
                @Override
                public Double apply(double value) {
                    return Math.sin(value);
                }
            });
            XYChart chart = QuickChart.getChart(iClasses[j].getName(), "x", "y", "sin(x)", dots[0], dots[1]);
            chart.getStyler().setSeriesMarkers(new Marker[]{new None()});
            drawChart(x1, y1, chart, iClasses[j], size, "rough");
            drawChart(x2, y2, chart, iClasses[j], size, "precise");
            drawChart(x2, y3, chart, iClasses[j], size, "mistake");
            drawChart(x3, y4, chart, iClasses[j], size, "long rough");
            new SwingWrapper(chart).displayChart();
        }
        x1 = new Double[]{0.0, 1.0, 2.0, 3.0, 5.0, 10.0};
        y1 = new Double[]{-1.0, 1.2, 12.9, 5.03, 9.0, 19.9};
        double[][] dots = getDots(0, 10, 500, new DoubleFunction<Double>() {
            @Override
            public Double apply(double value) {
                return 2*value-1;
            }
        });
        XYChart chart = QuickChart.getChart(LowestSqrtInterpolation.class.getName(), "x", "y", "y=2x-1", dots[0], dots[1]);
        Interpolator interpolator = new LowestSqrtInterpolation(new DataSet(Arrays.asList(x1), Arrays.asList(y1)));
        dots = getDots(0, 10, 500, interpolator);
        chart.addSeries("custom", dots[0], dots[1]);
        int max = 0;
        double diff = 0;
        for(int i=0; i<x1.length;i++){
            if(Math.abs(interpolator.apply(x1[i])-y1[i]) > diff){
                max = i;
                diff = Math.abs(interpolator.apply(x1[i])-y1[i]);
            }
        }
        List<Double> x = new ArrayList<>(Arrays.asList(x1));
        List<Double> y = new ArrayList<>(Arrays.asList(y1));
        x.remove(max);
        y.remove(max);
        interpolator = new LowestSqrtInterpolation(new DataSet(x, y));
        dots = getDots(0, 10, 500, interpolator);
        chart.addSeries("precise", dots[0], dots[1]);
        chart.getStyler().setSeriesMarkers(new Marker[]{new None()});
        SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
        wrapper.displayChart();
        chart.getSeriesMap().forEach((s, xy)->{
            wrapper.getXChartPanel().getActionMap().put(xy.getName(), new ViewAction(xy));
        });
    }

    private static double[][] getDots(double start, double stop, int size, DoubleFunction<Double> function){
        double reply[][] = new double[2][size];
        double diff = (stop-start)/size;
        for(int i=0;i < size;i++){
            reply[0][i] = start;
            reply[1][i] = function.apply(start);
            start+=diff;
        }
        return reply;
    }

    private static void drawChart(Double[] x, Double[] y, XYChart chart, Class<? extends Interpolator> iClass, int size, String name){
        double xP[] = new double[size];
        double yP[] = new double[size];
        DataSet set = new DataSet(Arrays.asList(x), Arrays.asList(y));
        Interpolator interpolator = null;
        try {
            interpolator = iClass.getConstructor(DataSet.class).newInstance(set);
            double start = x[0];
            double stop = x[x.length-1];
            double diff = (stop-start)/size;
            for(int i=0;i<xP.length;i++){
                xP[i]=start;
                yP[i]=interpolator.apply(start);
                start+=diff;
            }
            chart.addSeries(name, xP, yP);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static class ViewAction extends AbstractAction{

        private XYSeries series;

        public ViewAction(XYSeries series){
            super(series.getName());
            this.series = series;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            series.setEnabled(!series.isEnabled());
        }
    }

}
