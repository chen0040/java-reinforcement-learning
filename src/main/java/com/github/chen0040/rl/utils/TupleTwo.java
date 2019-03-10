package com.github.chen0040.rl.utils;

import java.util.Objects;

/**
 * Created by xschen on 10/11/2015 0011.
 */
public class TupleTwo<T1, T2> {
    private final T1 item1;
    private final T2 item2;

    private TupleTwo(final T1 item1, final T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    static <U1, U2> TupleTwo<U1, U2> create(final U1 item1, final U2 item2) {
        return new TupleTwo<>(item1, item2);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final TupleTwo<?, ?> tupleTwo = (TupleTwo<?, ?>) o;

        return Objects.equals(this.item1, tupleTwo.item1) && Objects.equals(this.item2, tupleTwo.item2);

    }


    @Override
    public int hashCode() {
        int result = this.item1 != null ? this.item1.hashCode() : 0;
        result = 31 * result + (this.item2 != null ? this.item2.hashCode() : 0);
        return result;
    }
}
