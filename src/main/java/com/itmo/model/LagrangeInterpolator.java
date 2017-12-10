package com.itmo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleFunction;

public class LagrangeInterpolator extends Interpolator {

    private DoubleFunction<Double>[][] polynomElements;

    public LagrangeInterpolator(List<Double> x, List<Double> y) {
        super(x, y);
    }

    public LagrangeInterpolator(DataSet set){super(set);}

    @Override
    public Double apply(double x) {
        double result = 0;
        for(int i=0;i<points.size();i++){
            result+= points.get(i).y*Arrays.stream(polynomElements[i]).
                    mapToDouble(f->f.apply(x)).reduce(1, (p1,p2)->p1*p2);
        }
        return result;
    }

    @Override
    protected void init(){
        polynomElements = new DoubleFunction[points.size()][points.size()-1];
        for(int i=0;i<points.size();i++){
            for(int j=0, k=0;j<points.size();j++){
                if(j==i) continue;
                polynomElements[i][k] = buildElement(points.get(j).x, points.get(i).x);
                k++;
            }
        }
    }

    private DoubleFunction<Double> buildElement(double valueToSubtract, double valueToSubtractFrom){
        return new DoubleFunction<Double>() {
            @Override
            public Double apply(double value) {
                return (value-valueToSubtract)/(valueToSubtractFrom-valueToSubtract);
            }
        };
    }
}
