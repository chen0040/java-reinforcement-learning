package com.github.chen0040.rl.utils;

/**
 * Created by xschen on 10/11/2015 0011.
 */
public class TupleTwo<T1, T2> {
    private T1 item1;
    private T2 item2;

    public TupleTwo(T1 item1, T2 item2){
        this.item1 = item1;
        this.item2 = item2;
    }

    public T1 getItem1() {
        return item1;
    }

    public void setItem1(T1 item1) {
        this.item1 = item1;
    }

    public T2 getItem2() {
        return item2;
    }

    public void setItem2(T2 item2) {
        this.item2 = item2;
    }

    public static <U1, U2> TupleTwo<U1, U2> create(U1 item1, U2 item2){
        return new TupleTwo<U1, U2>(item1, item2);
    }


    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TupleTwo<?, ?> tupleTwo = (TupleTwo<?, ?>) o;

        if (item1 != null ? !item1.equals(tupleTwo.item1) : tupleTwo.item1 != null)
            return false;
        return item2 != null ? item2.equals(tupleTwo.item2) : tupleTwo.item2 == null;

    }


    @Override public int hashCode() {
        int result = item1 != null ? item1.hashCode() : 0;
        result = 31 * result + (item2 != null ? item2.hashCode() : 0);
        return result;
    }
}
