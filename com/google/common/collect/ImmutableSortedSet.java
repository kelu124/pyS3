package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E> {
    final transient Comparator<? super E> comparator;
    @GwtIncompatible
    @LazyInit
    transient ImmutableSortedSet<E> descendingSet;

    public static final class Builder<E> extends com.google.common.collect.ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E element) {
            super.add((Object) element);
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

        public ImmutableSortedSet<E> build() {
            ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, (Object[]) this.contents);
            this.size = result.size();
            return result;
        }
    }

    private static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super E> comparator;
        final Object[] elements;

        public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
            this.comparator = comparator;
            this.elements = elements;
        }

        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    @GwtIncompatible
    public abstract UnmodifiableIterator<E> descendingIterator();

    abstract ImmutableSortedSet<E> headSetImpl(E e, boolean z);

    abstract int indexOf(@Nullable Object obj);

    public abstract UnmodifiableIterator<E> iterator();

    abstract ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2);

    abstract ImmutableSortedSet<E> tailSetImpl(E e, boolean z);

    static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet(ImmutableList.of(), comparator);
    }

    public static <E> ImmutableSortedSet<E> of() {
        return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
        return new RegularImmutableSortedSet(ImmutableList.of(element), Ordering.natural());
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
        return construct(Ordering.natural(), 2, e1, e2);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
        return construct(Ordering.natural(), 3, e1, e2, e3);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
        return construct(Ordering.natural(), 4, e1, e2, e3, e4);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(Ordering.natural(), 5, e1, e2, e3, e4, e5);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
        Comparable[] contents = new Comparable[(remaining.length + 6)];
        contents[0] = e1;
        contents[1] = e2;
        contents[2] = e3;
        contents[3] = e4;
        contents[4] = e5;
        contents[5] = e6;
        System.arraycopy(remaining, 0, contents, 6, remaining.length);
        return construct(Ordering.natural(), contents.length, contents);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
        return construct(Ordering.natural(), elements.length, (Object[]) elements.clone());
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
        return copyOf(Ordering.natural(), (Iterable) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
        return copyOf(Ordering.natural(), (Collection) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
        return copyOf(Ordering.natural(), (Iterator) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        return new Builder(comparator).addAll((Iterator) elements).build();
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        if (SortedIterables.hasSameComparator(comparator, elements) && (elements instanceof ImmutableSortedSet)) {
            ImmutableSortedSet<E> original = (ImmutableSortedSet) elements;
            if (!original.isPartialView()) {
                return original;
            }
        }
        Object[] array = Iterables.toArray(elements);
        return construct(comparator, array.length, array);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
        return copyOf((Comparator) comparator, (Iterable) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
        Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
        ImmutableList<E> list = ImmutableList.copyOf((Collection) sortedSet);
        if (list.isEmpty()) {
            return emptySet(comparator);
        }
        return new RegularImmutableSortedSet(list, comparator);
    }

    static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
        if (n == 0) {
            return emptySet(comparator);
        }
        ObjectArrays.checkElementsNotNull(contents, n);
        Arrays.sort(contents, 0, n, comparator);
        int i = 1;
        int uniques = 1;
        while (i < n) {
            int uniques2;
            E cur = contents[i];
            if (comparator.compare(cur, contents[uniques - 1]) != 0) {
                uniques2 = uniques + 1;
                contents[uniques] = cur;
            } else {
                uniques2 = uniques;
            }
            i++;
            uniques = uniques2;
        }
        Arrays.fill(contents, uniques, n, null);
        return new RegularImmutableSortedSet(ImmutableList.asImmutableList(contents, uniques), comparator);
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

    int unsafeCompare(Object a, Object b) {
        return unsafeCompare(this.comparator, a, b);
    }

    static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
        return comparator.compare(a, b);
    }

    ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    public ImmutableSortedSet<E> headSet(E toElement) {
        return headSet((Object) toElement, false);
    }

    @GwtIncompatible
    public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
        return headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
    }

    public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
        return subSet((Object) fromElement, true, (Object) toElement, false);
    }

    @GwtIncompatible
    public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator.compare(fromElement, toElement) <= 0);
        return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }

    public ImmutableSortedSet<E> tailSet(E fromElement) {
        return tailSet((Object) fromElement, true);
    }

    @GwtIncompatible
    public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
        return tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
    }

    @GwtIncompatible
    public E lower(E e) {
        return Iterators.getNext(headSet((Object) e, false).descendingIterator(), null);
    }

    @GwtIncompatible
    public E floor(E e) {
        return Iterators.getNext(headSet((Object) e, true).descendingIterator(), null);
    }

    @GwtIncompatible
    public E ceiling(E e) {
        return Iterables.getFirst(tailSet((Object) e, true), null);
    }

    @GwtIncompatible
    public E higher(E e) {
        return Iterables.getFirst(tailSet((Object) e, false), null);
    }

    public E first() {
        return iterator().next();
    }

    public E last() {
        return descendingIterator().next();
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> result = this.descendingSet;
        if (result != null) {
            return result;
        }
        result = createDescendingSet();
        this.descendingSet = result;
        result.descendingSet = this;
        return result;
    }

    @GwtIncompatible
    ImmutableSortedSet<E> createDescendingSet() {
        return new DescendingImmutableSortedSet(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
