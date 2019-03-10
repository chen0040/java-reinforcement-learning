package com.github.chen0040.rl.utils;

/**
 * Created by xschen on 10/11/2015 0011.
 */
public enum DoubleUtils {
    ;

    public static final double TOLERANCE = 1e-10;

    public static boolean equals(final double a1, final double a2) {
        return Math.abs(a1 - a2) < DoubleUtils.TOLERANCE;
    }

}
