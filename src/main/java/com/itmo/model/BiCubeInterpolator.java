package com.itmo.model;

import java.util.List;
import java.util.function.DoubleFunction;

public class BiCubeInterpolator extends Interpolator {

    private double[] subFactors;

    public BiCubeInterpolator(List<Double> x, List<Double> y) {
        super(x, y);
    }

    public BiCubeInterpolator(DataSet set){super(set);}

    @Override
    protected void init(){
        subFactors = new double[points.size()];
        double[] diffs = new double[points.size()-1];

        for(int i=0;i<points.size()-1;i++){
            double h = points.get(i+1).x-points.get(i).x;
            diffs[i] = (points.get(i+1).y-points.get(i).y)/h;
        }

        subFactors[0] = diffs[0];
        for (int i=1;i<points.size()-1;i++){
            subFactors[i] = (diffs[i-1]+diffs[i])/2;
        }
        subFactors[points.size()-1] = diffs[points.size()-2];

        for(int i=0; i<points.size()-1;i++){
            if(diffs[i]==0){
                subFactors[i] = 0;
                subFactors[i+1] = 0;
            }else {
                double a = subFactors[i]/diffs[i];
                double b = subFactors[i+1]/diffs[i];
                double h = Math.hypot(a, b);
                if(h > 9){
                    double t = 3 / h;
                    subFactors[i] = t * a * diffs[i];
                    subFactors[i+1] = t * b * diffs[i];
                }
            }
        }
    }

    private double[] tomasAlg(){
        double reply[] = new double[points.size()];

        return reply;
    }

    @Override
    public Double apply(double x) {
        if(x <= points.get(0).x) return points.get(0).y;
        if(x >= points.get(points.size()-1).x) return points.get(points.size()-1).y;

        int i = 0;
        while (x >= points.get(i+1).x){
            i++;
            if(x == points.get(i).x) return points.get(i).y;
        }

        double h = points.get(i+1).x - points.get(i).x;
        double t = (x-points.get(i).x) / h;
        return (points.get(i).y *(1+2*t) + h*subFactors[i]*t)*(1-t)*(1-t)
                + (points.get(i+1).y * (3-2*t) + h*subFactors[i+1]*(t-1))*t*t;
    }
}
