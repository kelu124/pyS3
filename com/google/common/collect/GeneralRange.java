package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class GeneralRange<T> implements Serializable {
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;
    private final BoundType lowerBoundType;
    @Nullable
    private final T lowerEndpoint;
    private transient GeneralRange<T> reverse;
    private final BoundType upperBoundType;
    @Nullable
    private final T upperEndpoint;

    static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
        T lowerEndpoint;
        T upperEndpoint;
        if (range.hasLowerBound()) {
            lowerEndpoint = range.lowerEndpoint();
        } else {
            lowerEndpoint = null;
        }
        BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
        if (range.hasUpperBound()) {
            upperEndpoint = range.upperEndpoint();
        } else {
            upperEndpoint = null;
        }
        return new GeneralRange(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), upperEndpoint, range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN);
    }

    static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
        return new GeneralRange(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
    }

    static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType) {
        return new GeneralRange(comparator, true, lower, lowerType, true, upper, upperType);
    }

    private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType) {
        int i = 1;
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        this.hasLowerBound = hasLowerBound;
        this.hasUpperBound = hasUpperBound;
        this.lowerEndpoint = lowerEndpoint;
        this.lowerBoundType = (BoundType) Preconditions.checkNotNull(lowerBoundType);
        this.upperEndpoint = upperEndpoint;
        this.upperBoundType = (BoundType) Preconditions.checkNotNull(upperBoundType);
        if (hasLowerBound) {
            comparator.compare(lowerEndpoint, lowerEndpoint);
        }
        if (hasUpperBound) {
            comparator.compare(upperEndpoint, upperEndpoint);
        }
        if (hasLowerBound && hasUpperBound) {
            boolean z;
            int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
            if (cmp <= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "lowerEndpoint (%s) > upperEndpoint (%s)", (Object) lowerEndpoint, (Object) upperEndpoint);
            if (cmp == 0) {
                int i2;
                if (lowerBoundType != BoundType.OPEN) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (upperBoundType == BoundType.OPEN) {
                    i = 0;
                }
                Preconditions.checkArgument(i2 | i);
            }
        }
    }

    Comparator<? super T> comparator() {
        return this.comparator;
    }

    boolean hasLowerBound() {
        return this.hasLowerBound;
    }

    boolean hasUpperBound() {
        return this.hasUpperBound;
    }

    boolean isEmpty() {
        return (hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint()));
    }

    boolean tooLow(@Nullable T t) {
        int i = 1;
        if (!hasLowerBound()) {
            return false;
        }
        int cmp = this.comparator.compare(t, getLowerEndpoint());
        int i2 = cmp < 0 ? 1 : 0;
        int i3 = cmp == 0 ? 1 : 0;
        if (getLowerBoundType() != BoundType.OPEN) {
            i = 0;
        }
        return i2 | (i3 & i);
    }

    boolean tooHigh(@Nullable T t) {
        int i = 1;
        if (!hasUpperBound()) {
            return false;
        }
        int cmp = this.comparator.compare(t, getUpperEndpoint());
        int i2 = cmp > 0 ? 1 : 0;
        int i3 = cmp == 0 ? 1 : 0;
        if (getUpperBoundType() != BoundType.OPEN) {
            i = 0;
        }
        return i2 | (i3 & i);
    }

    boolean contains(@Nullable T t) {
        return (tooLow(t) || tooHigh(t)) ? false : true;
    }

    GeneralRange<T> intersect(GeneralRange<T> other) {
        int cmp;
        Preconditions.checkNotNull(other);
        Preconditions.checkArgument(this.comparator.equals(other.comparator));
        boolean hasLowBound = this.hasLowerBound;
        T lowEnd = getLowerEndpoint();
        BoundType lowType = getLowerBoundType();
        if (!hasLowerBound()) {
            hasLowBound = other.hasLowerBound;
            lowEnd = other.getLowerEndpoint();
            lowType = other.getLowerBoundType();
        } else if (other.hasLowerBound()) {
            cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
            if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
                lowEnd = other.getLowerEndpoint();
                lowType = other.getLowerBoundType();
            }
        }
        boolean hasUpBound = this.hasUpperBound;
        T upEnd = getUpperEndpoint();
        BoundType upType = getUpperBoundType();
        if (!hasUpperBound()) {
            hasUpBound = other.hasUpperBound;
            upEnd = other.getUpperEndpoint();
            upType = other.getUpperBoundType();
        } else if (other.hasUpperBound()) {
            cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
            if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
                upEnd = other.getUpperEndpoint();
                upType = other.getUpperBoundType();
            }
        }
        if (hasLowBound && hasUpBound) {
            cmp = this.comparator.compare(lowEnd, upEnd);
            if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
                lowEnd = upEnd;
                lowType = BoundType.OPEN;
                upType = BoundType.CLOSED;
            }
        }
        return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof GeneralRange)) {
            return false;
        }
        GeneralRange<?> r = (GeneralRange) obj;
        if (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && getLowerBoundType().equals(r.getLowerBoundType()) && getUpperBoundType().equals(r.getUpperBoundType()) && Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && Objects.equal(getUpperEndpoint(), r.getUpperEndpoint())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType());
    }

    GeneralRange<T> reverse() {
        GeneralRange<T> result = this.reverse;
        if (result != null) {
            return result;
        }
        result = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
        result.reverse = this;
        this.reverse = result;
        return result;
    }

    public String toString() {
        return this.comparator + ":" + (this.lowerBoundType == BoundType.CLOSED ? '[' : '(') + (this.hasLowerBound ? this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? this.upperEndpoint : "∞") + (this.upperBoundType == BoundType.CLOSED ? ']' : ')');
    }

    T getLowerEndpoint() {
        return this.lowerEndpoint;
    }

    BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }

    T getUpperEndpoint() {
        return this.upperEndpoint;
    }

    BoundType getUpperBoundType() {
        return this.upperBoundType;
    }
}
