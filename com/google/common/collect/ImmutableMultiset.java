package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
    @LazyInit
    private transient ImmutableList<E> asList;
    @LazyInit
    private transient ImmutableSet<Entry<E>> entrySet;

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        final Multiset<E> contents;

        public Builder() {
            this(LinkedHashMultiset.create());
        }

        Builder(Multiset<E> contents) {
            this.contents = contents;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E element) {
            this.contents.add(Preconditions.checkNotNull(element));
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addCopies(E element, int occurrences) {
            this.contents.add(Preconditions.checkNotNull(element), occurrences);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> setCount(E element, int count) {
            this.contents.setCount(Preconditions.checkNotNull(element), count);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... elements) {
            super.add((Object[]) elements);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Multiset) {
                for (Entry<? extends E> entry : Multisets.cast(elements).entrySet()) {
                    addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll((Iterable) elements);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf(this.contents);
        }
    }

    private final class EntrySet extends Indexed<Entry<E>> {
        private static final long serialVersionUID = 0;

        private EntrySet() {
        }

        boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }

        Entry<E> get(int index) {
            return ImmutableMultiset.this.getEntry(index);
        }

        public int size() {
            return ImmutableMultiset.this.elementSet().size();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?> entry = (Entry) o;
            if (entry.getCount() > 0 && ImmutableMultiset.this.count(entry.getElement()) == entry.getCount()) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }

    static class EntrySetSerializedForm<E> implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
            this.multiset = multiset;
        }

        Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final int[] counts;
        final Object[] elements;

        SerializedForm(Multiset<?> multiset) {
            int distinct = multiset.entrySet().size();
            this.elements = new Object[distinct];
            this.counts = new int[distinct];
            int i = 0;
            for (Entry<?> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        Object readResolve() {
            Iterable multiset = LinkedHashMultiset.create(this.elements.length);
            for (int i = 0; i < this.elements.length; i++) {
                multiset.add(this.elements[i], this.counts[i]);
            }
            return ImmutableMultiset.copyOf(multiset);
        }
    }

    abstract Entry<E> getEntry(int i);

    public static <E> ImmutableMultiset<E> of() {
        return RegularImmutableMultiset.EMPTY;
    }

    public static <E> ImmutableMultiset<E> of(E element) {
        return copyFromElements(element);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2) {
        return copyFromElements(e1, e2);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
        return copyFromElements(e1, e2, e3);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
        return copyFromElements(e1, e2, e3, e4);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
        return copyFromElements(e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
        return new Builder().add((Object) e1).add((Object) e2).add((Object) e3).add((Object) e4).add((Object) e5).add((Object) e6).add((Object[]) others).build();
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
        return copyFromElements(elements);
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
        if (elements instanceof ImmutableMultiset) {
            ImmutableMultiset<E> result = (ImmutableMultiset) elements;
            if (!result.isPartialView()) {
                return result;
            }
        }
        return copyFromEntries((elements instanceof Multiset ? Multisets.cast(elements) : LinkedHashMultiset.create((Iterable) elements)).entrySet());
    }

    private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
        Multiset<E> multiset = LinkedHashMultiset.create();
        Collections.addAll(multiset, elements);
        return copyFromEntries(multiset.entrySet());
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Entry<? extends E>> entries) {
        if (entries.isEmpty()) {
            return of();
        }
        return new RegularImmutableMultiset(entries);
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
        Multiset<E> multiset = LinkedHashMultiset.create();
        Iterators.addAll(multiset, elements);
        return copyFromEntries(multiset.entrySet());
    }

    ImmutableMultiset() {
    }

    public UnmodifiableIterator<E> iterator() {
        final Iterator<Entry<E>> entryIterator = entrySet().iterator();
        return new UnmodifiableIterator<E>() {
            E element;
            int remaining;

            public boolean hasNext() {
                return this.remaining > 0 || entryIterator.hasNext();
            }

            public E next() {
                if (this.remaining <= 0) {
                    Entry<E> entry = (Entry) entryIterator.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                this.remaining--;
                return this.element;
            }
        };
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        immutableList = createAsList();
        this.asList = immutableList;
        return immutableList;
    }

    ImmutableList<E> createAsList() {
        if (isEmpty()) {
            return ImmutableList.of();
        }
        return new RegularImmutableAsList((ImmutableCollection) this, toArray());
    }

    public boolean contains(@Nullable Object object) {
        return count(object) > 0;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int add(E e, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int remove(Object element, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int setCount(E e, int count) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean setCount(E e, int oldCount, int newCount) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible
    int copyIntoArray(Object[] dst, int offset) {
        Iterator i$ = entrySet().iterator();
        while (i$.hasNext()) {
            Entry<E> entry = (Entry) i$.next();
            Arrays.fill(dst, offset, entry.getCount() + offset, entry.getElement());
            offset += entry.getCount();
        }
        return offset;
    }

    public boolean equals(@Nullable Object object) {
        return Multisets.equalsImpl(this, object);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public String toString() {
        return entrySet().toString();
    }

    public ImmutableSet<Entry<E>> entrySet() {
        ImmutableSet<Entry<E>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    private final ImmutableSet<Entry<E>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new EntrySet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }
}
