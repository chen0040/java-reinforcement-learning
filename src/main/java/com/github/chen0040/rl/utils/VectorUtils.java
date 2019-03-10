package com.github.chen0040.rl.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 10/11/2015 0011.
 */
public class VectorUtils {
    public static List<Vec> removeZeroVectors(final Iterable<Vec> vlist) {
        final List<Vec> vstarlist = new ArrayList<Vec>();
        for (final Vec v : vlist) {
            if (!v.isZero()) {
                vstarlist.add(v);
            }
        }

        return vstarlist;
    }

    public static TupleTwo<List<Vec>, List<Double>> normalize(final Iterable<Vec> vlist) {
        final List<Double> norms = new ArrayList<Double>();
        final List<Vec> vstarlist = new ArrayList<Vec>();
        for (final Vec v : vlist) {
            norms.add(v.norm());
            vstarlist.add(v.normalize());
        }

        return TupleTwo.create(vstarlist, norms);
    }


}
