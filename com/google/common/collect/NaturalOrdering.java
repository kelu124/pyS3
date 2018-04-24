package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();
    private static final long serialVersionUID = 0;
    private transient Ordering<Comparable> nullsFirst;
    private transient Ordering<Comparable> nullsLast;

    public int compare(Comparable left, Comparable right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        return left.compareTo(right);
    }

    public <S extends Comparable> Ordering<S> nullsFirst() {
        Ordering<Comparable> result = this.nullsFirst;
        if (result != null) {
            return result;
        }
        result = super.nullsFirst();
        this.nullsFirst = result;
        return result;
    }

    public <S extends Comparable> Ordering<S> nullsLast() {
        Ordering<Comparable> result = this.nullsLast;
        if (result != null) {
            return result;
        }
        result = super.nullsLast();
        this.nullsLast = result;
        return result;
    }

    public <S extends Comparable> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "Ordering.natural()";
    }

    private NaturalOrdering() {
    }
}
