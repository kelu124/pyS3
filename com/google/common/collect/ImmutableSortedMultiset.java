package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@GwtIncompatible
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
    @LazyInit
    transient ImmutableSortedMultiset<E> descendingMultiset;

    public static class Builder<E> extends com.google.common.collect.ImmutableMultiset.Builder<E> {
        public Builder(Comparator<? super E> comparator) {
            super(TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator)));
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E element) {
            super.add((Object) element);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addCopies(E element, int occurrences) {
            super.addCopies(element, occurrences);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> setCount(E element, int count) {
            super.setCount(element, count);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... elements) {
            super.add((Object[]) elements);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll((Iterable) elements);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.copyOfSorted((SortedMultiset) this.contents);
        }
    }

    private static final class SerializedForm<E> implements Serializable {
        final Comparator<? super E> comparator;
        final int[] counts;
        final E[] elements;

        SerializedForm(SortedMultiset<E> multiset) {
            this.comparator = multiset.comparator();
            int n = multiset.entrySet().size();
            this.elements = new Object[n];
            this.counts = new int[n];
            int i = 0;
            for (Entry<E> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        Object readResolve() {
            int n = this.elements.length;
            Builder<E> builder = new Builder(this.comparator);
            for (int i = 0; i < n; i++) {
                builder.addCopies(this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }

    public abstract ImmutableSortedSet<E> elementSet();

    public abstract ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType);

    public abstract ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType);

    public static <E> ImmutableSortedMultiset<E> of() {
        return RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) ImmutableSortedSet.of(element), new long[]{0, 1}, 0, 1);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e1, e2}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e1, e2, e3}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e1, e2, e3, e4}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e1, e2, e3, e4, e5}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
        Iterable all = Lists.newArrayListWithCapacity(remaining.length + 6);
        Collections.addAll(all, new Comparable[]{e1, e2, e3, e4, e5, e6});
        Collections.addAll(all, remaining);
        return copyOf(Ordering.natural(), all);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
        return copyOf(Ordering.natural(), Arrays.asList(elements));
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
        return copyOf(Ordering.natural(), (Iterable) elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
        return copyOf(Ordering.natural(), (Iterator) elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        return new Builder(comparator).addAll((Iterator) elements).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        if (elements instanceof ImmutableSortedMultiset) {
            ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset) elements;
            if (comparator.equals(multiset.comparator())) {
                if (multiset.isPartialView()) {
                    return copyOfSortedEntries(comparator, multiset.entrySet().asList());
                }
                return multiset;
            }
        }
        elements = Lists.newArrayList((Iterable) elements);
        TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator));
        Iterables.addAll(sortedCopy, elements);
        return copyOfSortedEntries(comparator, sortedCopy.entrySet());
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
        return copyOfSortedEntries(sortedMultiset.comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
    }

    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Entry<E>> entries) {
        if (entries.isEmpty()) {
            return emptyMultiset(comparator);
        }
        com.google.common.collect.ImmutableList.Builder<E> elementsBuilder = new com.google.common.collect.ImmutableList.Builder(entries.size());
        long[] cumulativeCounts = new long[(entries.size() + 1)];
        int i = 0;
        for (Entry<E> entry : entries) {
            elementsBuilder.add(entry.getElement());
            cumulativeCounts[i + 1] = cumulativeCounts[i] + ((long) entry.getCount());
            i++;
        }
        return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(elementsBuilder.build(), comparator), cumulativeCounts, 0, entries.size());
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
        }
        return new RegularImmutableSortedMultiset(comparator);
    }

    ImmutableSortedMultiset() {
    }

    public final Comparator<? super E> comparator() {
        return elementSet().comparator();
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> result = this.descendingMultiset;
        if (result != null) {
            return result;
        }
        ImmutableSortedMultiset<E> emptyMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset(this);
        this.descendingMultiset = emptyMultiset;
        return emptyMultiset;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
        Preconditions.checkArgument(comparator().compare(lowerBound, upperBound) <= 0, "Expected lowerBound <= upperBound but %s > %s", (Object) lowerBound, (Object) upperBound);
        return tailMultiset((Object) lowerBound, lowerBoundType).headMultiset((Object) upperBound, upperBoundType);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder(comparator);
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
