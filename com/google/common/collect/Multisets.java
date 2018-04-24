package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multiset.Entry;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets {
    private static final Ordering<Entry<?>> DECREASING_COUNT_ORDERING = new C06105();

    static abstract class AbstractEntry<E> implements Entry<E> {
        AbstractEntry() {
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry<?> that = (Entry) object;
            if (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            E e = getElement();
            return (e == null ? 0 : e.hashCode()) ^ getCount();
        }

        public String toString() {
            String text = String.valueOf(getElement());
            int n = getCount();
            return n == 1 ? text : text + " x " + n;
        }
    }

    static abstract class ElementSet<E> extends ImprovedAbstractSet<E> {
        abstract Multiset<E> multiset();

        ElementSet() {
        }

        public void clear() {
            multiset().clear();
        }

        public boolean contains(Object o) {
            return multiset().contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return multiset().containsAll(c);
        }

        public boolean isEmpty() {
            return multiset().isEmpty();
        }

        public Iterator<E> iterator() {
            return new TransformedIterator<Entry<E>, E>(multiset().entrySet().iterator()) {
                E transform(Entry<E> entry) {
                    return entry.getElement();
                }
            };
        }

        public boolean remove(Object o) {
            return multiset().remove(o, Integer.MAX_VALUE) > 0;
        }

        public int size() {
            return multiset().entrySet().size();
        }
    }

    static abstract class EntrySet<E> extends ImprovedAbstractSet<Entry<E>> {
        abstract Multiset<E> multiset();

        EntrySet() {
        }

        public boolean contains(@Nullable Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?> entry = (Entry) o;
            if (entry.getCount() > 0 && multiset().count(entry.getElement()) == entry.getCount()) {
                return true;
            }
            return false;
        }

        public boolean remove(Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry<?> entry = (Entry) object;
            Object element = entry.getElement();
            int entryCount = entry.getCount();
            if (entryCount != 0) {
                return multiset().setCount(element, entryCount, 0);
            }
            return false;
        }

        public void clear() {
            multiset().clear();
        }
    }

    static class C06105 extends Ordering<Entry<?>> {
        C06105() {
        }

        public int compare(Entry<?> entry1, Entry<?> entry2) {
            return Ints.compare(entry2.getCount(), entry1.getCount());
        }
    }

    private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
        final Predicate<? super E> predicate;
        final Multiset<E> unfiltered;

        class C06121 implements Predicate<Entry<E>> {
            C06121() {
            }

            public boolean apply(Entry<E> entry) {
                return FilteredMultiset.this.predicate.apply(entry.getElement());
            }
        }

        FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
            this.unfiltered = (Multiset) Preconditions.checkNotNull(unfiltered);
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        Set<E> createElementSet() {
            return Sets.filter(this.unfiltered.elementSet(), this.predicate);
        }

        Set<Entry<E>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), new C06121());
        }

        Iterator<Entry<E>> entryIterator() {
            throw new AssertionError("should never be called");
        }

        int distinctElements() {
            return elementSet().size();
        }

        public int count(@Nullable Object element) {
            int count = this.unfiltered.count(element);
            if (count <= 0) {
                return 0;
            }
            return this.predicate.apply(element) ? count : 0;
        }

        public int add(@Nullable E element, int occurrences) {
            Preconditions.checkArgument(this.predicate.apply(element), "Element %s does not match predicate %s", (Object) element, this.predicate);
            return this.unfiltered.add(element, occurrences);
        }

        public int remove(@Nullable Object element, int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return count(element);
            }
            return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
        }

        public void clear() {
            elementSet().clear();
        }
    }

    static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        private static final long serialVersionUID = 0;
        private final int count;
        @Nullable
        private final E element;

        ImmutableEntry(@Nullable E element, int count) {
            this.element = element;
            this.count = count;
            CollectPreconditions.checkNonnegative(count, "count");
        }

        @Nullable
        public final E getElement() {
            return this.element;
        }

        public final int getCount() {
            return this.count;
        }

        public ImmutableEntry<E> nextInBucket() {
            return null;
        }
    }

    static final class MultisetIteratorImpl<E> implements Iterator<E> {
        private boolean canRemove;
        private Entry<E> currentEntry;
        private final Iterator<Entry<E>> entryIterator;
        private int laterCount;
        private final Multiset<E> multiset;
        private int totalCount;

        MultisetIteratorImpl(Multiset<E> multiset, Iterator<Entry<E>> entryIterator) {
            this.multiset = multiset;
            this.entryIterator = entryIterator;
        }

        public boolean hasNext() {
            return this.laterCount > 0 || this.entryIterator.hasNext();
        }

        public E next() {
            if (hasNext()) {
                if (this.laterCount == 0) {
                    this.currentEntry = (Entry) this.entryIterator.next();
                    int count = this.currentEntry.getCount();
                    this.laterCount = count;
                    this.totalCount = count;
                }
                this.laterCount--;
                this.canRemove = true;
                return this.currentEntry.getElement();
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (this.totalCount == 1) {
                this.entryIterator.remove();
            } else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            this.totalCount--;
            this.canRemove = false;
        }
    }

    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Entry<E>> entrySet;

        UnmodifiableMultiset(Multiset<? extends E> delegate) {
            this.delegate = delegate;
        }

        protected Multiset<E> delegate() {
            return this.delegate;
        }

        Set<E> createElementSet() {
            return Collections.unmodifiableSet(this.delegate.elementSet());
        }

        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            set = createElementSet();
            this.elementSet = set;
            return set;
        }

        public Set<Entry<E>> entrySet() {
            Set<Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.entrySet());
            this.entrySet = set;
            return set;
        }

        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(this.delegate.iterator());
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public int add(E e, int occurences) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object element) {
            throw new UnsupportedOperationException();
        }

        public int remove(Object element, int occurrences) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public int setCount(E e, int count) {
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E e, int oldCount, int newCount) {
            throw new UnsupportedOperationException();
        }
    }

    private Multisets() {
    }

    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
        if ((multiset instanceof UnmodifiableMultiset) || (multiset instanceof ImmutableMultiset)) {
            return multiset;
        }
        return new UnmodifiableMultiset((Multiset) Preconditions.checkNotNull(multiset));
    }

    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
        return (Multiset) Preconditions.checkNotNull(multiset);
    }

    @Beta
    public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
        return new UnmodifiableSortedMultiset((SortedMultiset) Preconditions.checkNotNull(sortedMultiset));
    }

    public static <E> Entry<E> immutableEntry(@Nullable E e, int n) {
        return new ImmutableEntry(e, n);
    }

    @Beta
    public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
        if (!(unfiltered instanceof FilteredMultiset)) {
            return new FilteredMultiset(unfiltered, predicate);
        }
        FilteredMultiset<E> filtered = (FilteredMultiset) unfiltered;
        return new FilteredMultiset(filtered.unfiltered, Predicates.and(filtered.predicate, predicate));
    }

    static int inferDistinctElements(Iterable<?> elements) {
        if (elements instanceof Multiset) {
            return ((Multiset) elements).elementSet().size();
        }
        return 11;
    }

    @Beta
    public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
        Preconditions.checkNotNull(multiset1);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public boolean contains(@Nullable Object element) {
                return multiset1.contains(element) || multiset2.contains(element);
            }

            public boolean isEmpty() {
                return multiset1.isEmpty() && multiset2.isEmpty();
            }

            public int count(Object element) {
                return Math.max(multiset1.count(element), multiset2.count(element));
            }

            Set<E> createElementSet() {
                return Sets.union(multiset1.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator<? extends Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
                final Iterator<? extends Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        if (iterator1.hasNext()) {
                            Entry<? extends E> entry1 = (Entry) iterator1.next();
                            E element = entry1.getElement();
                            return Multisets.immutableEntry(element, Math.max(entry1.getCount(), multiset2.count(element)));
                        }
                        while (iterator2.hasNext()) {
                            Entry<? extends E> entry2 = (Entry) iterator2.next();
                            element = entry2.getElement();
                            if (!multiset1.contains(element)) {
                                return Multisets.immutableEntry(element, entry2.getCount());
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset1);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public int count(Object element) {
                int count1 = multiset1.count(element);
                return count1 == 0 ? 0 : Math.min(count1, multiset2.count(element));
            }

            Set<E> createElementSet() {
                return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator<Entry<E>> iterator1 = multiset1.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        while (iterator1.hasNext()) {
                            Entry<E> entry1 = (Entry) iterator1.next();
                            E element = entry1.getElement();
                            int count = Math.min(entry1.getCount(), multiset2.count(element));
                            if (count > 0) {
                                return Multisets.immutableEntry(element, count);
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    @Beta
    public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
        Preconditions.checkNotNull(multiset1);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public boolean contains(@Nullable Object element) {
                return multiset1.contains(element) || multiset2.contains(element);
            }

            public boolean isEmpty() {
                return multiset1.isEmpty() && multiset2.isEmpty();
            }

            public int size() {
                return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
            }

            public int count(Object element) {
                return multiset1.count(element) + multiset2.count(element);
            }

            Set<E> createElementSet() {
                return Sets.union(multiset1.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator<? extends Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
                final Iterator<? extends Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        if (iterator1.hasNext()) {
                            Entry<? extends E> entry1 = (Entry) iterator1.next();
                            E element = entry1.getElement();
                            return Multisets.immutableEntry(element, entry1.getCount() + multiset2.count(element));
                        }
                        while (iterator2.hasNext()) {
                            Entry<? extends E> entry2 = (Entry) iterator2.next();
                            element = entry2.getElement();
                            if (!multiset1.contains(element)) {
                                return Multisets.immutableEntry(element, entry2.getCount());
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    @Beta
    public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset1);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public int count(@Nullable Object element) {
                int count1 = multiset1.count(element);
                if (count1 == 0) {
                    return 0;
                }
                return Math.max(0, count1 - multiset2.count(element));
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator<Entry<E>> iterator1 = multiset1.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        while (iterator1.hasNext()) {
                            Entry<E> entry1 = (Entry) iterator1.next();
                            E element = entry1.getElement();
                            int count = entry1.getCount() - multiset2.count(element);
                            if (count > 0) {
                                return Multisets.immutableEntry(element, count);
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return Iterators.size(entryIterator());
            }
        };
    }

    @CanIgnoreReturnValue
    public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
        Preconditions.checkNotNull(superMultiset);
        Preconditions.checkNotNull(subMultiset);
        for (Entry<?> entry : subMultiset.entrySet()) {
            if (superMultiset.count(entry.getElement()) < entry.getCount()) {
                return false;
            }
        }
        return true;
    }

    @CanIgnoreReturnValue
    public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
        return retainOccurrencesImpl(multisetToModify, multisetToRetain);
    }

    private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
        Preconditions.checkNotNull(multisetToModify);
        Preconditions.checkNotNull(occurrencesToRetain);
        Iterator<Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            Entry<E> entry = (Entry) entryIterator.next();
            int retainCount = occurrencesToRetain.count(entry.getElement());
            if (retainCount == 0) {
                entryIterator.remove();
                changed = true;
            } else if (retainCount < entry.getCount()) {
                multisetToModify.setCount(entry.getElement(), retainCount);
                changed = true;
            }
        }
        return changed;
    }

    @CanIgnoreReturnValue
    public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
        if (occurrencesToRemove instanceof Multiset) {
            return removeOccurrences((Multiset) multisetToModify, (Multiset) occurrencesToRemove);
        }
        Preconditions.checkNotNull(multisetToModify);
        Preconditions.checkNotNull(occurrencesToRemove);
        boolean changed = false;
        for (Object o : occurrencesToRemove) {
            changed |= multisetToModify.remove(o);
        }
        return changed;
    }

    @CanIgnoreReturnValue
    public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
        Preconditions.checkNotNull(multisetToModify);
        Preconditions.checkNotNull(occurrencesToRemove);
        boolean changed = false;
        Iterator<? extends Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Entry<?> entry = (Entry) entryIterator.next();
            int removeCount = occurrencesToRemove.count(entry.getElement());
            if (removeCount >= entry.getCount()) {
                entryIterator.remove();
                changed = true;
            } else if (removeCount > 0) {
                multisetToModify.remove(entry.getElement(), removeCount);
                changed = true;
            }
        }
        return changed;
    }

    static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
        if (object == multiset) {
            return true;
        }
        if (!(object instanceof Multiset)) {
            return false;
        }
        Multiset<?> that = (Multiset) object;
        if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
            return false;
        }
        for (Entry<?> entry : that.entrySet()) {
            if (multiset.count(entry.getElement()) != entry.getCount()) {
                return false;
            }
        }
        return true;
    }

    static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
        if (elements.isEmpty()) {
            return false;
        }
        if (elements instanceof Multiset) {
            for (Entry<? extends E> entry : cast(elements).entrySet()) {
                self.add(entry.getElement(), entry.getCount());
            }
        } else {
            Iterators.addAll(self, elements.iterator());
        }
        return true;
    }

    static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
        Collection<?> collection;
        if (elementsToRemove instanceof Multiset) {
            collection = ((Multiset) elementsToRemove).elementSet();
        } else {
            collection = elementsToRemove;
        }
        return self.elementSet().removeAll(collection);
    }

    static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
        Collection<?> collection;
        Preconditions.checkNotNull(elementsToRetain);
        if (elementsToRetain instanceof Multiset) {
            collection = ((Multiset) elementsToRetain).elementSet();
        } else {
            collection = elementsToRetain;
        }
        return self.elementSet().retainAll(collection);
    }

    static <E> int setCountImpl(Multiset<E> self, E element, int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        int oldCount = self.count(element);
        int delta = count - oldCount;
        if (delta > 0) {
            self.add(element, delta);
        } else if (delta < 0) {
            self.remove(element, -delta);
        }
        return oldCount;
    }

    static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        if (self.count(element) != oldCount) {
            return false;
        }
        self.setCount(element, newCount);
        return true;
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
        return new MultisetIteratorImpl(multiset, multiset.entrySet().iterator());
    }

    static int sizeImpl(Multiset<?> multiset) {
        long size = 0;
        for (Entry<?> entry : multiset.entrySet()) {
            size += (long) entry.getCount();
        }
        return Ints.saturatedCast(size);
    }

    static <T> Multiset<T> cast(Iterable<T> iterable) {
        return (Multiset) iterable;
    }

    @Beta
    public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
        return ImmutableMultiset.copyFromEntries(DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet()));
    }
}
