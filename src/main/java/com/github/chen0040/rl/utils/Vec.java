package com.github.chen0040.rl.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Vec implements Serializable {
    private final Map<Integer, Double> data = new HashMap<>();
    private int dimension;
    private double defaultValue;
    private int id = -1;

    public Vec() {

    }

    public Vec(final double[] v) {
        IntStream.range(0, v.length).forEach(i -> this.set(i, v[i]));
    }

    public Vec(final int dimension) {
        this.dimension = dimension;
        this.defaultValue = 0;
    }

    public Vec(final int dimension, final Map<Integer, Double> data) {
        this.dimension = dimension;
        this.defaultValue = 0;

        data.forEach(this::set);
    }

    private static boolean isZero(final double a) {
        return a < DoubleUtils.TOLERANCE;
    }

    public Vec makeCopy() {
        final Vec clone = new Vec(this.dimension);
        clone.copy(this);
        return clone;
    }

    public void copy(final Vec rhs) {
        this.defaultValue = rhs.defaultValue;
        this.dimension = rhs.dimension;
        this.id = rhs.id;

        this.data.clear();
        rhs.data.forEach(this.data::put);
    }

    public void set(final int i, final double value) {
        if (value == this.defaultValue) {
            return;
        }

        this.data.put(i, value);
        if (i >= this.dimension) {
            this.dimension = i + 1;
        }
    }


    public double get(final int i) {
        return this.data.getOrDefault(i, this.defaultValue);
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs instanceof Vec) {
            final Vec rhs2 = (Vec) rhs;
            if (this.dimension != rhs2.dimension || this.data.size() != rhs2.data.size()) {
                return false;
            }
            for (final Integer index : this.data.keySet()) {
                if (!rhs2.data.containsKey(index) || !DoubleUtils.equals(this.data.get(index), rhs2.data.get(index))) {
                    return false;
                }
            }
            if (this.defaultValue != rhs2.defaultValue) {
                return IntStream.range(0, this.dimension).noneMatch(this.data::containsKey);
            }
            return true;
        }

        return false;
    }

    public void setAll(final double value) {
        this.defaultValue = value;
        this.data.keySet().forEach(index -> this.data.put(index, this.defaultValue));
    }

    public IndexValue indexWithMaxValue(final Set<Integer> indices) {
        if (indices == null) {
            return this.indexWithMaxValue();
        } else {
            final IndexValue iv = new IndexValue();
            iv.setIndex(-1);
            iv.setValue(Double.NEGATIVE_INFINITY);
            for (final Integer index : indices) {
                final double value = this.data.getOrDefault(index, Double.NEGATIVE_INFINITY);
                if (value > iv.getValue()) {
                    iv.setIndex(index);
                    iv.setValue(value);
                }
            }
            return iv;
        }
    }

    private IndexValue indexWithMaxValue() {
        final IndexValue iv = new IndexValue();
        iv.setIndex(-1);
        iv.setValue(Double.NEGATIVE_INFINITY);


        for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
            if (entry.getKey() >= this.dimension) {
                continue;
            }

            final double value = entry.getValue();
            if (value > iv.getValue()) {
                iv.setValue(value);
                iv.setIndex(entry.getKey());
            }
        }

        if (!iv.isValid()) {
            iv.setValue(this.defaultValue);
        } else {
            if (iv.getValue() < this.defaultValue) {
                for (int i = 0; i < this.dimension; ++i) {
                    if (!this.data.containsKey(i)) {
                        iv.setValue(this.defaultValue);
                        iv.setIndex(i);
                        break;
                    }
                }
            }
        }

        return iv;
    }


    private double multiply(final Vec rhs) {

        return this.defaultValue == 0 ?
                this.data.entrySet().stream().mapToDouble(entry -> entry.getValue() * rhs.get(entry.getKey())).sum() :
                IntStream.range(0, this.dimension).mapToDouble(i -> this.get(i) * rhs.get(i)).sum();
    }

    private double sum() {
        return this.data.values().stream().mapToDouble(v -> v).sum() + this.defaultValue * (this.dimension - this.data.size());
    }

    boolean isZero() {
        return Vec.isZero(this.sum());
    }

    double norm() {
        double sum = this.multiply(this);
        if (!Vec.isZero(this.defaultValue)) {
            sum += (this.dimension - this.data.size()) * (this.defaultValue * this.defaultValue);
        }
        return Math.sqrt(sum);
    }

    Vec normalize() {
        final double norm = this.norm(); // L2 norm is the cartesian distance
        if (Vec.isZero(norm)) {
            return new Vec(this.dimension);
        }
        final Vec clone = new Vec(this.dimension);
        clone.setAll(this.defaultValue / norm);

        this.data.keySet().forEach(k -> clone.data.put(k, this.data.get(k) / norm));
        return clone;
    }

    void setId(final int id) {
        this.id = id;
    }
}
