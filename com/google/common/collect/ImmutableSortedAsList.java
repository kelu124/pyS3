package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
    ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
        super((ImmutableCollection) backingSet, (ImmutableList) backingList);
    }

    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet) super.delegateCollection();
    }

    public Comparator<? super E> comparator() {
        return delegateCollection().comparator();
    }

    @GwtIncompatible
    public int indexOf(@Nullable Object target) {
        int index = delegateCollection().indexOf(target);
        return (index < 0 || !get(index).equals(target)) ? -1 : index;
    }

    @GwtIncompatible
    public int lastIndexOf(@Nullable Object target) {
        return indexOf(target);
    }

    public boolean contains(Object target) {
        return indexOf(target) >= 0;
    }

    @GwtIncompatible
    ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
        return new RegularImmutableSortedSet(super.subListUnchecked(fromIndex, toIndex), comparator()).asList();
    }
}
