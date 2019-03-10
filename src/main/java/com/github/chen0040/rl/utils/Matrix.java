package com.github.chen0040.rl.utils;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Matrix implements Serializable {
    private Map<Integer, Vec> rows = new HashMap<>();
    private int rowCount;
    private int columnCount;
    private double defaultValue;

    public Matrix() {

    }

    public Matrix(final double[][] A) {
        for (int i = 0; i < A.length; ++i) {
            final double[] B = A[i];
            for (int j = 0; j < B.length; ++j) {
                this.set(i, j, B[j]);
            }
        }
    }

    public Matrix(final int rowCount, final int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.defaultValue = 0;
    }

    public static Matrix identity(final int dimension) {
        final Matrix m = new Matrix(dimension, dimension);
        for (int i = 0; i < m.getRowCount(); ++i) {
            m.set(i, i, 1);
        }
        return m;
    }

    public void setRow(final int rowIndex, final Vec rowVector) {
        rowVector.setId(rowIndex);
        this.rows.put(rowIndex, rowVector);
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs != null && rhs instanceof Matrix) {
            final Matrix rhs2 = (Matrix) rhs;
            if (this.rowCount != rhs2.rowCount || this.columnCount != rhs2.columnCount) {
                return false;
            }

            if (this.defaultValue == rhs2.defaultValue) {
                for (final Integer index : this.rows.keySet()) {
                    if (!rhs2.rows.containsKey(index)) {
                        return false;
                    }
                    if (!this.rows.get(index).equals(rhs2.rows.get(index))) {
                        System.out.println("failed!");
                        return false;
                    }
                }

                for (final Integer index : rhs2.rows.keySet()) {
                    if (!this.rows.containsKey(index)) {
                        return false;
                    }
                    if (!rhs2.rows.get(index).equals(this.rows.get(index))) {
                        System.out.println("failed! 22");
                        return false;
                    }
                }
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

    public void copy(final Matrix rhs) {
        this.rowCount = rhs.rowCount;
        this.columnCount = rhs.columnCount;
        this.defaultValue = rhs.defaultValue;

        this.rows.clear();

        for (final Map.Entry<Integer, Vec> entry : rhs.rows.entrySet()) {
            this.rows.put(entry.getKey(), entry.getValue().makeCopy());
        }
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

    public List<Vec> columnVectors() {
        final Matrix A = this;
        final int n = A.getColumnCount();
        final int rowCount = A.getRowCount();

        final List<Vec> Acols = new ArrayList<Vec>();

        for (int c = 0; c < n; ++c) {
            final Vec Acol = new Vec(rowCount);
            Acol.setAll(this.defaultValue);
            Acol.setId(c);

            for (int r = 0; r < rowCount; ++r) {
                Acol.set(r, A.get(r, c));
            }
            Acols.add(Acol);
        }
        return Acols;
    }

    public Matrix multiply(final Matrix rhs) {
        if (this.getColumnCount() != rhs.getRowCount()) {
            System.err.println("A.columnCount must be equal to B.rowCount in multiplication");
            return null;
        }

        Vec row1;
        Vec col2;

        final Matrix result = new Matrix(this.getRowCount(), rhs.getColumnCount());
        result.setAll(this.defaultValue);

        final List<Vec> rhsColumns = rhs.columnVectors();

        for (final Map.Entry<Integer, Vec> entry : this.rows.entrySet()) {
            final int r1 = entry.getKey();
            row1 = entry.getValue();
            for (int c2 = 0; c2 < rhsColumns.size(); ++c2) {
                col2 = rhsColumns.get(c2);
                result.set(r1, c2, row1.multiply(col2));
            }
        }

        return result;
    }

    @JSONField(serialize = false)
    public boolean isSymmetric() {
        if (this.getRowCount() != this.getColumnCount()) {
            return false;
        }

        for (final Map.Entry<Integer, Vec> rowEntry : this.rows.entrySet()) {
            final int row = rowEntry.getKey();
            final Vec rowVec = rowEntry.getValue();

            for (final Integer col : rowVec.getData().keySet()) {
                if (row == col.intValue()) {
                    continue;
                }
                if (DoubleUtils.equals(rowVec.get(col), this.get(col, row))) {
                    return false;
                }
            }
        }

        return true;
    }

    public Vec multiply(final Vec rhs) {
        if (this.getColumnCount() != rhs.getDimension()) {
            System.err.println("columnCount must be equal to the size of the vector for multiplication");
        }

        Vec row1;
        final Vec result = new Vec(this.getRowCount());
        for (final Map.Entry<Integer, Vec> entry : this.rows.entrySet()) {
            row1 = entry.getValue();
            result.set(entry.getKey(), row1.multiply(rhs));
        }
        return result;
    }


    public Map<Integer, Vec> getRows() {
        return this.rows;
    }

    public void setRows(final Map<Integer, Vec> rows) {
        this.rows = rows;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(final int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(final int columnCount) {
        this.columnCount = columnCount;
    }

    public double getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(final double defaultValue) {
        this.defaultValue = defaultValue;
    }
}
