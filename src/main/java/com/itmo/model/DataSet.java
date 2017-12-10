package com.itmo.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataSet implements Cloneable{

    protected Set<Interpolator.Point> pointList;

    public DataSet(List<Double> xList, List<Double> yList){
        this();
        for(int i=0;i<Math.min(xList.size(), yList.size());i++){
            pointList.add(new Interpolator.Point(xList.get(i), yList.get(i)));
        }
    }

    public DataSet(Collection<Interpolator.Point> points){
        pointList = new TreeSet<>(points);
    }

    public DataSet(){
        pointList = new TreeSet<>();
    }

    public void addPoint(Interpolator.Point p){
        pointList.add(p);
    }

    public void removePoint(double x){
        pointList.remove(new Interpolator.Point(x, 0));
    }

    public int size(){
        return pointList.size();
    }

    public Interpolator.Point get(int index){
        if(index > pointList.size()) return new Interpolator.Point(0, 0);
        int i = 0;
        Iterator<Interpolator.Point> iterator = pointList.iterator();
        while (i<index){
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    public Stream<Interpolator.Point> stream(){
        return this.getPointList().stream();
    }

    public Set<Interpolator.Point> getPointList(){
        return this.pointList;
    }

    @Override
    public DataSet clone(){
        return new DataSet(pointList.stream().map(p->p.clone()).collect(Collectors.toList()));
    }

}
