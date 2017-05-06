package com.github.chen0040.rl.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Vec implements Serializable, Cloneable {
    private HashMap<Integer, Double> data = new HashMap<Integer, Double>();
    private int dimension;
    private double defaultValue;
    private int id = -1;

    public Vec(){

    }

    public Vec(double[] v){
        for(int i=0; i < v.length; ++i){
            set(i, v[i]);
        }
    }

    public Vec(int dimension){
        this.dimension = dimension;
        defaultValue = 0;
    }

    public Vec(int dimension, Map<Integer, Double> data){
        this.dimension = dimension;
        defaultValue = 0;

        for(Map.Entry<Integer, Double> entry : data.entrySet()){
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object clone(){
        Vec clone = new Vec(dimension);
        clone.copy(this);
        return clone;
    }

    public void copy(Vec rhs){
        defaultValue = rhs.defaultValue;
        dimension = rhs.dimension;
        id = rhs.id;

        data.clear();
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            data.put(entry.getKey(), entry.getValue());
        }
    }

    public void set(int i, double value){
        if(value == defaultValue) return;

        data.put(i, value);
        if(i >= dimension){
            dimension = i+1;
        }
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double get(int i){
        return data.getOrDefault(i, defaultValue);
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs != null && rhs instanceof Vec){
            Vec rhs2 = (Vec)rhs;
            if(dimension != rhs2.dimension){
                return false;
            }

            if(data.size() != rhs2.data.size()){
                return false;
            }

            for(Integer index : data.keySet()){
                if(!rhs2.data.containsKey(index)) return false;
                if(!DoubleUtils.equals(data.get(index), rhs2.data.get(index))){
                    return false;
                }
            }

            if(defaultValue != rhs2.defaultValue){
                for(int i=0; i < dimension; ++i){
                    if(data.containsKey(i)){
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    public void setAll(double value){
        defaultValue = value;
        for(Integer index : data.keySet()){
            data.put(index, defaultValue);
        }
    }

    public IndexValue indexWithMaxValue(Set<Integer> indices){
        if(indices == null){
            return indexWithMaxValue();
        }else{
            IndexValue iv = new IndexValue();
            iv.setIndex(-1);
            iv.setValue(Double.NEGATIVE_INFINITY);
            for(Integer index : indices){
                double value = data.getOrDefault(index, Double.NEGATIVE_INFINITY);
                if(value > iv.getValue()){
                    iv.setIndex(index);
                    iv.setValue(value);
                }
            }
            return iv;
        }
    }

    public IndexValue indexWithMaxValue(){
        IndexValue iv = new IndexValue();
        iv.setIndex(-1);
        iv.setValue(Double.NEGATIVE_INFINITY);


        for(Map.Entry<Integer, Double> entry : data.entrySet()){
            if(entry.getKey() >= dimension) continue;

            double value = entry.getValue();
            if(value > iv.getValue()){
                iv.setValue(value);
                iv.setIndex(entry.getKey());
            }
        }

        if(!iv.isValid()){
            iv.setValue(defaultValue);
        } else{
            if(iv.getValue() < defaultValue){
                for(int i=0; i < dimension; ++i){
                    if(!data.containsKey(i)){
                        iv.setValue(defaultValue);
                        iv.setIndex(i);
                        break;
                    }
                }
            }
        }

        return iv;
    }

    public int getDimension(){
        return dimension;
    }

    public double getDefaultValue() { return defaultValue;}

    public HashMap<Integer, Double> getData(){
        return data;
    }

    public Vec projectOrthogonal(Iterable<Vec> vlist) {
        Vec b = this;
        for(Vec v : vlist)
        {
            b = b.minus(b.projectAlong(v));
        }

        return b;
    }

    public Vec projectOrthogonal(List<Vec> vlist, Map<Integer, Double> alpha) {
        Vec b = this;
        for(int i = 0; i < vlist.size(); ++i)
        {
            Vec v = vlist.get(i);
            double norm_a = v.multiply(v);

            if (DoubleUtils.isZero(norm_a)) {
                return new Vec(dimension);
            }
            double sigma = multiply(v) / norm_a;
            Vec v_parallel = v.multiply(sigma);

            alpha.put(i, sigma);

            b = b.minus(v_parallel);
        }

        return b;
    }

    public Vec projectAlong(Vec rhs)
    {
        double norm_a = rhs.multiply(rhs);

        if (DoubleUtils.isZero(norm_a)) {
            return new Vec(dimension);
        }
        double sigma = multiply(rhs) / norm_a;
        return rhs.multiply(sigma);
    }

    public Vec multiply(double rhs){
        Vec clone = (Vec)this.clone();
        for(Integer i : data.keySet()){
            clone.data.put(i, rhs * data.get(i));
        }
        return clone;
    }

    public double multiply(Vec rhs)
    {
        double productSum = 0;
        if(defaultValue == 0) {
            for (Map.Entry<Integer, Double> entry : data.entrySet()) {
                productSum += entry.getValue() * rhs.get(entry.getKey());
            }
        } else {
            for(int i=0; i < dimension; ++i){
                productSum += get(i) * rhs.get(i);
            }
        }

        return productSum;
    }

    public Vec pow(double scalar)
    {
        Vec result = new Vec(dimension);
        for (Map.Entry<Integer, Double> entry : data.entrySet())
        {
            result.data.put(entry.getKey(), Math.pow(entry.getValue(), scalar));
        }
        return result;
    }

    public Vec add(Vec rhs)
    {
        Vec result = new Vec(dimension);
        int index;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() + rhs.data.get(index));
        }
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            index = entry.getKey();
            if(result.data.containsKey(index)) continue;
            result.data.put(index, entry.getValue() + data.get(index));
        }

        return result;
    }

    public Vec minus(Vec rhs)
    {
        Vec result = new Vec(dimension);
        int index;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() - rhs.data.get(index));
        }
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            index = entry.getKey();
            if(result.data.containsKey(index)) continue;
            result.data.put(index, data.get(index) - entry.getValue());
        }

        return result;
    }

    public double sum(){
        double sum = 0;

        for(Map.Entry<Integer, Double> entry : data.entrySet()){
            sum += entry.getValue();
        }
        sum += defaultValue * (dimension - data.size());

        return sum;
    }

    public boolean isZero(){
        return DoubleUtils.isZero(sum());
    }

    public double norm(int level)
    {
        if (level == 1)
        {
            double sum = 0;
            for (Double val : data.values())
            {
                sum += Math.abs(val);
            }
            if(!DoubleUtils.isZero(defaultValue)) {
                sum += Math.abs(defaultValue) * (dimension - data.size());
            }
            return sum;
        }
        else if (level == 2)
        {
            double sum = multiply(this);
            if(!DoubleUtils.isZero(defaultValue)){
                sum += (dimension - data.size()) * (defaultValue * defaultValue);
            }
            return Math.sqrt(sum);
        }
        else
        {
            double sum = 0;
            for (Double val : this.data.values())
            {
                sum += Math.pow(Math.abs(val), level);
            }
            if(!DoubleUtils.isZero(defaultValue)) {
                sum += Math.pow(Math.abs(defaultValue), level) * (dimension - data.size());
            }
            return Math.pow(sum, 1.0 / level);
        }
    }

    public Vec normalize()
    {
        double norm = norm(2); // L2 norm is the cartesian distance
        if (DoubleUtils.isZero(norm))
        {
            return new Vec(dimension);
        }
        Vec clone = new Vec(dimension);
        clone.setAll(defaultValue / norm);

        for (Integer k : data.keySet())
        {
            clone.data.put(k, data.get(k) / norm);
        }
        return clone;
    }
}
