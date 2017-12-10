package com.itmo.model;

import java.util.List;

public class LowestSqrtInterpolation extends Interpolator {

    private double a;
    private double b;

    public LowestSqrtInterpolation(List<Double> x, List<Double> y) {
        super(x, y);
    }

    public LowestSqrtInterpolation(DataSet set){super(set);}

    @Override
    protected void init(){
        double denominator = ((points.size())*points.stream().mapToDouble(p->p.x*p.x).sum()-
                Math.pow(points.stream().mapToDouble(p->p.x).sum(), 2));
        a = ((points.size())*points.stream().mapToDouble(p->p.y*p.x).sum() -
                points.stream().mapToDouble(p->p.x).sum()*points.stream().mapToDouble(p->p.y).sum())
                /denominator;
        b = (points.stream().mapToDouble(p->p.y).sum() - a* points.stream().mapToDouble(p->p.x).sum())/points.size();
    }

    @Override
    public Double apply(double x) {
        return a*x+b;
    }
}
