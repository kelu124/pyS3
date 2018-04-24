package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtIncompatible
final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(Ordering.natural());
    private static final long[] ZERO_CUMULATIVE_COUNTS = new long[]{0};
    private final transient long[] cumulativeCounts;
    private final transient RegularImmutableSortedSet<E> elementSet;
    private final transient int length;
    private final transient int offset;

    RegularImmutableSortedMultiset(Comparator<? super E> comparator) {
        this.elementSet = ImmutableSortedSet.emptySet(comparator);
        this.cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
        this.offset = 0;
        this.length = 0;
    }

    RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length) {
        this.elementSet = elementSet;
        this.cumulativeCounts = cumulativeCounts;
        this.offset = offset;
        this.length = length;
    }

    private int getCount(int index) {
        return (int) (this.cumulativeCounts[(this.offset + index) + 1] - this.cumulativeCounts[this.offset + index]);
    }

    Entry<E> getEntry(int index) {
        return Multisets.immutableEntry(this.elementSet.asList().get(index), getCount(index));
    }

    public Entry<E> firstEntry() {
        return isEmpty() ? null : getEntry(0);
    }

    public Entry<E> lastEntry() {
        return isEmpty() ? null : getEntry(this.length - 1);
    }

    public int count(@Nullable Object element) {
        int index = this.elementSet.indexOf(element);
        return index >= 0 ? getCount(index) : 0;
    }

    public int size() {
        return Ints.saturatedCast(this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset]);
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }

    public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
        return getSubMultiset(0, this.elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
    }

    public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
        return getSubMultiset(this.elementSet.tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), this.length);
    }

    ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, this.length);
        if (from == to) {
            return ImmutableSortedMultiset.emptyMultiset(comparator());
        }
        return (from == 0 && to == this.length) ? this : new RegularImmutableSortedMultiset(this.elementSet.getSubSet(from, to), this.cumulativeCounts, this.offset + from, to - from);
    }

    boolean isPartialView() {
        return this.offset > 0 || this.length < this.cumulativeCounts.length - 1;
    }
}
