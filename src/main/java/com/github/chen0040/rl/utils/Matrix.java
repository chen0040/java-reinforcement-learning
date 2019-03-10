package com.github.chen0040.rl.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Matrix implements Serializable {
    private final Map<Integer, Vec> rows = new HashMap<>();
    private int rowCount;
    private int columnCount;
    private double defaultValue;

    public Matrix(final int rowCount, final int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.defaultValue = 0;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs instanceof Matrix) {
            final Matrix rhs2 = (Matrix) rhs;
            if (this.rowCount != rhs2.rowCount || this.columnCount != rhs2.columnCount) {
                return false;
            }

            if (this.defaultValue == rhs2.defaultValue) {
                return this.rows.keySet().stream().noneMatch(index -> !rhs2.rows.containsKey(index) || !this.rows.get(index).equals(rhs2.rows.get(index))) &&
                        rhs2.rows.keySet().stream().noneMatch(index -> !this.rows.containsKey(index) || !rhs2.rows.get(index).equals(this.rows.get(index)));
            } else {

                for (int i = 0; i < this.rowCount; ++i) {
                    for (int j = 0; j < this.columnCount; ++j) {
                        if (this.get(i, j) != rhs2.get(i, j)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Matrix makeCopy() {
        final Matrix clone = new Matrix(this.rowCount, this.columnCount);
        clone.copy(this);
        return clone;
    }

    private void copy(final Matrix rhs) {
        this.rowCount = rhs.rowCount;
        this.columnCount = rhs.columnCount;
        this.defaultValue = rhs.defaultValue;

        this.rows.clear();

        rhs.rows.forEach((key, value) -> this.rows.put(key, value.makeCopy()));
    }

    public void set(final int rowIndex, final int columnIndex, final double value) {
        final Vec row = this.rowAt(rowIndex);
        row.set(columnIndex, value);
        if (rowIndex >= this.rowCount) {
            this.rowCount = rowIndex + 1;
        }
        if (columnIndex >= this.columnCount) {
            this.columnCount = columnIndex + 1;
        }
    }

    public Vec rowAt(final int rowIndex) {
        Vec row = this.rows.get(rowIndex);
        if (row == null) {
            row = new Vec(this.columnCount);
            row.setAll(this.defaultValue);
            row.setId(rowIndex);
            this.rows.put(rowIndex, row);
        }
        return row;
    }

    public void setAll(final double value) {
        this.defaultValue = value;
        for (final Vec row : this.rows.values()) {
            row.setAll(value);
        }
    }

    public double get(final int rowIndex, final int columnIndex) {
        final Vec row = this.rowAt(rowIndex);
        return row.get(columnIndex);
    }


    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

}
