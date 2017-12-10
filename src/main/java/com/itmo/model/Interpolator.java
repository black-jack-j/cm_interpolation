package com.itmo.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

public abstract class Interpolator implements DoubleFunction<Double>{

    protected DataSet points;

    public Interpolator(List<Double> x, List<Double> y){
        points = new DataSet(x, y);
        init();
    }

    public Interpolator(DataSet dataSet){
        this.points = dataSet;
        init();
    }

    public void addPoint(Double x, Double y){
        points.addPoint(new Point(x, y));
    }

    public void removePoint(Double x){
        points.removePoint(x);
    }

    public void setDataSet(DataSet set){
        this.points = set;
        init();
    }

    public DataSet getPoints() {
        return points;
    }

    protected void pointAdded(int index){
        init();
    }

    protected void pointDeleted(int index){
        init();
    }

    protected void init(){

    }

    public abstract Double apply(double x);


    public static class Point implements Comparable<Point>, Cloneable{
        protected double x;
        protected double y;

        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }

        public Point(){

        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y){
            this.y = y;
        }

        @Override
        public int compareTo(Point that){
            return (int)Math.signum(this.x-that.x);
        }

        @Override
        public boolean equals(Object o){
            return this.x == ((Point)o).x;
        }

        @Override
        public Point clone(){
            return new Point(this.x, this.y);
        }
        public static class PointComparator implements Comparator<Point>{

            @Override
            public int compare(Point point1, Point point2){
                return (int)Math.signum(point1.x - point2.x);
            }

        }
    }
}
