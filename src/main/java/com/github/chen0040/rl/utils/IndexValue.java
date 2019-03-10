package com.github.chen0040.rl.utils;


/**
 * Created by xschen on 6/5/2017.
 */
public class IndexValue {
    private int index;
    private double value;

    public IndexValue() {

    }

    public IndexValue(final int index, final double value) {
        this.index = index;
        this.value = value;
    }

    public IndexValue makeCopy() {
        final IndexValue clone = new IndexValue();
        clone.setValue(this.value);
        clone.setIndex(this.index);
        return clone;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs != null && rhs instanceof IndexValue) {
            final IndexValue rhs2 = (IndexValue) rhs;
            return this.index == rhs2.index && this.value == rhs2.value;
        }
        return false;
    }

    public boolean isValid() {
        return this.index != -1;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(final double value) {
        this.value = value;
    }
}
