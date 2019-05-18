package com.github.chen0040.rl.utils;

import java.util.List;

/**
 * Created by xschen on 10/11/2015 0011.
 */
public class MatrixUtils {
	/**
	 * Convert a list of column vectors into a matrix
	 */
	public static Matrix matrixFromColumnVectors(List<Vec> R) {
		int n = R.size();
		int m = R.get(0).getDimension();

		Matrix T = new Matrix(m, n);
		for (int c = 0; c < n; ++c) {
			Vec Rcol = R.get(c);
			for (int r : Rcol.getData().keySet()) {
				T.set(r, c, Rcol.get(r));
			}
		}
		return T;
	}
}
