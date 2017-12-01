package com.github.chen0040.rl.utils;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xschen on 9/27/2015 0027.
 */
@Getter
@Setter
public class Matrix implements Serializable {
    private Map<Integer, Vec> rows = new HashMap<>();
    private int rowCount;
    private int columnCount;
    private double defaultValue;

    public Matrix(){

    }

    public Matrix(double[][] A){
        for(int i = 0; i < A.length; ++i){
            double[] B = A[i];
            for(int j=0; j < B.length; ++j){
                set(i, j, B[j]);
            }
        }
    }

    public void setRow(int rowIndex, Vec rowVector){
        rowVector.setId(rowIndex);
        rows.put(rowIndex, rowVector);
    }


    public static Matrix identity(int dimension){
        Matrix m = new Matrix(dimension, dimension);
        for(int i=0; i < m.getRowCount(); ++i){
            m.set(i, i, 1);
        }
        return m;
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs != null && rhs instanceof Matrix){
            Matrix rhs2 = (Matrix)rhs;
            if(rowCount != rhs2.rowCount || columnCount != rhs2.columnCount){
                return false;
            }

            if(defaultValue == rhs2.defaultValue) {
                for (Integer index : rows.keySet()) {
                    if (!rhs2.rows.containsKey(index)) return false;
                    if (!rows.get(index).equals(rhs2.rows.get(index))) {
                        System.out.println("failed!");
                        return false;
                    }
                }

                for (Integer index : rhs2.rows.keySet()) {
                    if (!rows.containsKey(index)) return false;
                    if (!rhs2.rows.get(index).equals(rows.get(index))) {
                        System.out.println("failed! 22");
                        return false;
                    }
                }
            } else {

                for(int i=0; i < rowCount; ++i) {
                    for(int j=0; j < columnCount; ++j) {
                        if(this.get(i, j) != rhs2.get(i, j)){
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Matrix makeCopy(){
        Matrix clone = new Matrix(rowCount, columnCount);
        clone.copy(this);
        return clone;
    }

    public void copy(Matrix rhs){
        rowCount = rhs.rowCount;
        columnCount = rhs.columnCount;
        defaultValue = rhs.defaultValue;

        rows.clear();

        for(Map.Entry<Integer, Vec> entry : rhs.rows.entrySet()){
          rows.put(entry.getKey(), entry.getValue().makeCopy());
        }
    }



    public void set(int rowIndex, int columnIndex, double value){
        Vec row = rowAt(rowIndex);
        row.set(columnIndex, value);
        if(rowIndex >= rowCount) { rowCount = rowIndex+1; }
        if(columnIndex >= columnCount) { columnCount = columnIndex + 1; }
    }



    public Matrix(int rowCount, int columnCount){
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.defaultValue = 0;
    }

    public Vec rowAt(int rowIndex){
        Vec row = rows.get(rowIndex);
        if(row == null){
            row = new Vec(columnCount);
            row.setAll(defaultValue);
            row.setId(rowIndex);
            rows.put(rowIndex, row);
        }
        return row;
    }

    public void setAll(double value){
        defaultValue = value;
        for(Vec row : rows.values()){
            row.setAll(value);
        }
    }

    public double get(int rowIndex, int columnIndex) {
        Vec row= rowAt(rowIndex);
        return row.get(columnIndex);
    }

    public List<Vec> columnVectors()
    {
        Matrix A = this;
        int n = A.getColumnCount();
        int rowCount = A.getRowCount();

        List<Vec> Acols = new ArrayList<Vec>();

        for (int c = 0; c < n; ++c)
        {
            Vec Acol = new Vec(rowCount);
            Acol.setAll(defaultValue);
            Acol.setId(c);

            for (int r = 0; r < rowCount; ++r)
            {
                Acol.set(r, A.get(r, c));
            }
            Acols.add(Acol);
        }
        return Acols;
    }

    public Matrix multiply(Matrix rhs)
    {
        if(this.getColumnCount() != rhs.getRowCount()){
            System.err.println("A.columnCount must be equal to B.rowCount in multiplication");
            return null;
        }

        Vec row1;
        Vec col2;

        Matrix result = new Matrix(getRowCount(), rhs.getColumnCount());
        result.setAll(defaultValue);

        List<Vec> rhsColumns = rhs.columnVectors();

        for (Map.Entry<Integer, Vec> entry : rows.entrySet())
        {
            int r1 = entry.getKey();
            row1 = entry.getValue();
            for (int c2 = 0; c2 < rhsColumns.size(); ++c2)
            {
                col2 = rhsColumns.get(c2);
                result.set(r1, c2, row1.multiply(col2));
            }
        }

        return result;
    }

    @JSONField(serialize = false)
    public boolean isSymmetric(){
        if (getRowCount() != getColumnCount()) return false;

        for (Map.Entry<Integer, Vec> rowEntry : rows.entrySet())
        {
            int row = rowEntry.getKey();
            Vec rowVec = rowEntry.getValue();

            for (Integer col : rowVec.getData().keySet())
            {
                if (row == col.intValue()) continue;
                if(DoubleUtils.equals(rowVec.get(col), this.get(col, row))){
                    return false;
                }
            }
        }

        return true;
    }

    public Vec multiply(Vec rhs)
    {
        if(this.getColumnCount() != rhs.getDimension()){
            System.err.println("columnCount must be equal to the size of the vector for multiplication");
        }

        Vec row1;
        Vec result = new Vec(getRowCount());
        for (Map.Entry<Integer, Vec> entry : rows.entrySet())
        {
            row1 = entry.getValue();
            result.set(entry.getKey(), row1.multiply(rhs));
        }
        return result;
    }




}
