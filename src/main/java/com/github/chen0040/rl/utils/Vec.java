package com.github.chen0040.rl.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Vec implements Serializable {
    private Map<Integer, Double> data = new HashMap<Integer, Double>();
    private int dimension;
    private double defaultValue;
    private int id = -1;

    public Vec() {

    }

    public Vec(final double[] v) {
        for (int i = 0; i < v.length; ++i) {
            this.set(i, v[i]);
        }
    }

    public Vec(final int dimension) {
        this.dimension = dimension;
        this.defaultValue = 0;
    }

    public Vec(final int dimension, final Map<Integer, Double> data) {
        this.dimension = dimension;
        this.defaultValue = 0;

        for (final Map.Entry<Integer, Double> entry : data.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }
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
        for (final Map.Entry<Integer, Double> entry : rhs.data.entrySet()) {
            this.data.put(entry.getKey(), entry.getValue());
        }
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
        if (rhs != null && rhs instanceof Vec) {
            final Vec rhs2 = (Vec) rhs;
            if (this.dimension != rhs2.dimension) {
                return false;
            }

            if (this.data.size() != rhs2.data.size()) {
                return false;
            }

            for (final Integer index : this.data.keySet()) {
                if (!rhs2.data.containsKey(index)) {
                    return false;
                }
                if (!DoubleUtils.equals(this.data.get(index), rhs2.data.get(index))) {
                    return false;
                }
            }

            if (this.defaultValue != rhs2.defaultValue) {
                for (int i = 0; i < this.dimension; ++i) {
                    if (this.data.containsKey(i)) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    public void setAll(final double value) {
        this.defaultValue = value;
        for (final Integer index : this.data.keySet()) {
            this.data.put(index, this.defaultValue);
        }
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

    public IndexValue indexWithMaxValue() {
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


    public Vec projectOrthogonal(final Iterable<Vec> vlist) {
        Vec b = this;
        for (final Vec v : vlist) {
            b = b.minus(b.projectAlong(v));
        }

        return b;
    }

    public Vec projectOrthogonal(final List<Vec> vlist, final Map<Integer, Double> alpha) {
        Vec b = this;
        for (int i = 0; i < vlist.size(); ++i) {
            final Vec v = vlist.get(i);
            final double norm_a = v.multiply(v);

            if (DoubleUtils.isZero(norm_a)) {
                return new Vec(this.dimension);
            }
            final double sigma = this.multiply(v) / norm_a;
            final Vec v_parallel = v.multiply(sigma);

            alpha.put(i, sigma);

            b = b.minus(v_parallel);
        }

        return b;
    }

    public Vec projectAlong(final Vec rhs) {
        final double norm_a = rhs.multiply(rhs);

        if (DoubleUtils.isZero(norm_a)) {
            return new Vec(this.dimension);
        }
        final double sigma = this.multiply(rhs) / norm_a;
        return rhs.multiply(sigma);
    }

    public Vec multiply(final double rhs) {
        final Vec clone = this.makeCopy();
        for (final Integer i : this.data.keySet()) {
            clone.data.put(i, rhs * this.data.get(i));
        }
        return clone;
    }

    public double multiply(final Vec rhs) {
        double productSum = 0;
        if (this.defaultValue == 0) {
            for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
                productSum += entry.getValue() * rhs.get(entry.getKey());
            }
        } else {
            for (int i = 0; i < this.dimension; ++i) {
                productSum += this.get(i) * rhs.get(i);
            }
        }

        return productSum;
    }

    public Vec pow(final double scalar) {
        final Vec result = new Vec(this.dimension);
        for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
            result.data.put(entry.getKey(), Math.pow(entry.getValue(), scalar));
        }
        return result;
    }

    public Vec add(final Vec rhs) {
        final Vec result = new Vec(this.dimension);
        int index;
        for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() + rhs.data.get(index));
        }
        for (final Map.Entry<Integer, Double> entry : rhs.data.entrySet()) {
            index = entry.getKey();
            if (result.data.containsKey(index)) {
                continue;
            }
            result.data.put(index, entry.getValue() + this.data.get(index));
        }

        return result;
    }

    public Vec minus(final Vec rhs) {
        final Vec result = new Vec(this.dimension);
        int index;
        for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() - rhs.data.get(index));
        }
        for (final Map.Entry<Integer, Double> entry : rhs.data.entrySet()) {
            index = entry.getKey();
            if (result.data.containsKey(index)) {
                continue;
            }
            result.data.put(index, this.data.get(index) - entry.getValue());
        }

        return result;
    }

    public double sum() {
        double sum = 0;

        for (final Map.Entry<Integer, Double> entry : this.data.entrySet()) {
            sum += entry.getValue();
        }
        sum += this.defaultValue * (this.dimension - this.data.size());

        return sum;
    }

    public boolean isZero() {
        return DoubleUtils.isZero(this.sum());
    }

    public double norm(final int level) {
        if (level == 1) {
            double sum = 0;
            for (final Double val : this.data.values()) {
                sum += Math.abs(val);
            }
            if (!DoubleUtils.isZero(this.defaultValue)) {
                sum += Math.abs(this.defaultValue) * (this.dimension - this.data.size());
            }
            return sum;
        } else if (level == 2) {
            double sum = this.multiply(this);
            if (!DoubleUtils.isZero(this.defaultValue)) {
                sum += (this.dimension - this.data.size()) * (this.defaultValue * this.defaultValue);
            }
            return Math.sqrt(sum);
        } else {
            double sum = 0;
            for (final Double val : this.data.values()) {
                sum += Math.pow(Math.abs(val), level);
            }
            if (!DoubleUtils.isZero(this.defaultValue)) {
                sum += Math.pow(Math.abs(this.defaultValue), level) * (this.dimension - this.data.size());
            }
            return Math.pow(sum, 1.0 / level);
        }
    }

    public Vec normalize() {
        final double norm = this.norm(2); // L2 norm is the cartesian distance
        if (DoubleUtils.isZero(norm)) {
            return new Vec(this.dimension);
        }
        final Vec clone = new Vec(this.dimension);
        clone.setAll(this.defaultValue / norm);

        for (final Integer k : this.data.keySet()) {
            clone.data.put(k, this.data.get(k) / norm);
        }
        return clone;
    }

    public Map<Integer, Double> getData() {
        return this.data;
    }

    public void setData(final Map<Integer, Double> data) {
        this.data = data;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(final int dimension) {
        this.dimension = dimension;
    }

    public double getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(final double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
