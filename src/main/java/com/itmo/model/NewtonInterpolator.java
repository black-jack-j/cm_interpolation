package com.itmo.model;

import java.util.ArrayList;
import java.util.List;

public class NewtonInterpolator extends Interpolator {

    private List<List<Double>> diffs;

    public NewtonInterpolator(List<Double> x, List<Double> y) {
        super(x, y);
    }

    public NewtonInterpolator(DataSet set){super(set);}

    @Override
    protected void init(){
        diffs = new ArrayList<>();
        List<Double> firstDiff = new ArrayList<>();
        for(int i=0;i<points.size()-1;i++){
            Point next = points.get(i+1);
            Point curr = points.get(i);
            firstDiff.add((next.y-curr.y)/(next.x-curr.x));
        }
        diffs.add(firstDiff);
        for(int i=0;i<points.size()-2;i++){
            List<Double> next = new ArrayList<>();
            List<Double> curr = diffs.get(i);
            for(int j=0;j<curr.size()-1;j++){
                double nextX = points.get(j+i+2).x;
                double currX = points.get(j).x;
                next.add((curr.get(j+1)-curr.get(j))/(nextX-currX));
            }
            diffs.add(next);
        }
    }

    @Override
    protected void pointAdded(int index){
        init();
    }

    @Override
    protected void pointDeleted(int index){
        init();
    }

    @Override
    public Double apply(double x) {
        double result = points.get(0).y;
        double xDiff = 1;
        for(int i=0;i<diffs.size();i++){
            xDiff = xDiff * (x-points.get(i).x);
            result = result + xDiff * diffs.get(i).get(0);
        }
        return result;
    }
}
