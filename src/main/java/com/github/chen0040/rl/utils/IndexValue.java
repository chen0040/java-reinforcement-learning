package com.github.chen0040.rl.utils;

//import lombok.Getter;
//import lombok.Setter;

/**
 * Created by xschen on 6/5/2017.
 */
//@Getter
//@Setter
public class IndexValue {
	private int index;
	private double value;

	public IndexValue() {

	}

	public IndexValue(int index, double value) {
		this.index = index;
		this.value = value;
	}

	public IndexValue makeCopy() {
		IndexValue clone = new IndexValue();
		clone.setValue(value);
		clone.setIndex(index);
		return clone;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object rhs) {
		if (rhs != null && rhs instanceof IndexValue) {
			IndexValue rhs2 = (IndexValue) rhs;
			return index == rhs2.index && value == rhs2.value;
		}
		return false;
	}

	public boolean isValid() {
		return index != -1;
	}

	public void setValue(double v) {
		this.value = v;
	}

	public int getIndex() {
		return this.index;
	}

	public double getValue() {
		return this.value;
	}
}
