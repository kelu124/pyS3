package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {

    public static final class Builder<E> extends ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }

        Builder(int capacity) {
            super(capacity);
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E element) {
            super.add((Object) element);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... elements) {
            super.add((Object[]) elements);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        public ImmutableList<E> build() {
            return ImmutableList.asImmutableList(this.contents, this.size);
        }
    }

    private static class ReverseImmutableList<E> extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator(int x0) {
            return super.listIterator(x0);
        }

        ReverseImmutableList(ImmutableList<E> backingList) {
            this.forwardList = backingList;
        }

        private int reverseIndex(int index) {
            return (size() - 1) - index;
        }

        private int reversePosition(int index) {
            return size() - index;
        }

        public ImmutableList<E> reverse() {
            return this.forwardList;
        }

        public boolean contains(@Nullable Object object) {
            return this.forwardList.contains(object);
        }

        public int indexOf(@Nullable Object object) {
            int index = this.forwardList.lastIndexOf(object);
            return index >= 0 ? reverseIndex(index) : -1;
        }

        public int lastIndexOf(@Nullable Object object) {
            int index = this.forwardList.indexOf(object);
            return index >= 0 ? reverseIndex(index) : -1;
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
        }

        public E get(int index) {
            Preconditions.checkElementIndex(index, size());
            return this.forwardList.get(reverseIndex(index));
        }

        public int size() {
            return this.forwardList.size();
        }

        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }

    class SubList extends ImmutableList<E> {
        final transient int length;
        final transient int offset;

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator(int x0) {
            return super.listIterator(x0);
        }

        SubList(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }

        public int size() {
            return this.length;
        }

        public E get(int index) {
            Preconditions.checkElementIndex(index, this.length);
            return ImmutableList.this.get(this.offset + index);
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
            return ImmutableList.this.subList(this.offset + fromIndex, this.offset + toIndex);
        }

        boolean isPartialView() {
            return true;
        }
    }

    public static <E> ImmutableList<E> of() {
        return RegularImmutableList.EMPTY;
    }

    public static <E> ImmutableList<E> of(E element) {
        return new SingletonImmutableList(element);
    }

    public static <E> ImmutableList<E> of(E e1, E e2) {
        return construct(e1, e2);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
        return construct(e1, e2, e3);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
        return construct(e1, e2, e3, e4);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return construct(e1, e2, e3, e4, e5, e6);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(e1, e2, e3, e4, e5, e6, e7);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
    }

    @SafeVarargs
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
        Object[] array = new Object[(others.length + 12)];
        array[0] = e1;
        array[1] = e2;
        array[2] = e3;
        array[3] = e4;
        array[4] = e5;
        array[5] = e6;
        array[6] = e7;
        array[7] = e8;
        array[8] = e9;
        array[9] = e10;
        array[10] = e11;
        array[11] = e12;
        System.arraycopy(others, 0, array, 12, others.length);
        return construct(array);
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        return elements instanceof Collection ? copyOf((Collection) elements) : copyOf(elements.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
        if (!(elements instanceof ImmutableCollection)) {
            return construct(elements.toArray());
        }
        ImmutableList<E> list = ((ImmutableCollection) elements).asList();
        if (list.isPartialView()) {
            return asImmutableList(list.toArray());
        }
        return list;
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        Object first = elements.next();
        if (elements.hasNext()) {
            return new Builder().add(first).addAll((Iterator) elements).build();
        }
        return of(first);
    }

    public static <E> ImmutableList<E> copyOf(E[] elements) {
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(elements[0]);
            default:
                return new RegularImmutableList(ObjectArrays.checkElementsNotNull((Object[]) elements.clone()));
        }
    }

    private static <E> ImmutableList<E> construct(Object... elements) {
        return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
    }

    static <E> ImmutableList<E> asImmutableList(Object[] elements) {
        return asImmutableList(elements, elements.length);
    }

    static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
        switch (length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(elements[0]);
            default:
                if (length < elements.length) {
                    elements = ObjectArrays.arraysCopyOf(elements, length);
                }
                return new RegularImmutableList(elements);
        }
    }

    ImmutableList() {
    }

    public UnmodifiableIterator<E> iterator() {
        return listIterator();
    }

    public UnmodifiableListIterator<E> listIterator() {
        return listIterator(0);
    }

    public UnmodifiableListIterator<E> listIterator(int index) {
        return new AbstractIndexedListIterator<E>(size(), index) {
            protected E get(int index) {
                return ImmutableList.this.get(index);
            }
        };
    }

    public int indexOf(@Nullable Object object) {
        return object == null ? -1 : Lists.indexOfImpl(this, object);
    }

    public int lastIndexOf(@Nullable Object object) {
        return object == null ? -1 : Lists.lastIndexOfImpl(this, object);
    }

    public boolean contains(@Nullable Object object) {
        return indexOf(object) >= 0;
    }

    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
        int length = toIndex - fromIndex;
        if (length == size()) {
            return this;
        }
        switch (length) {
            case 0:
                return of();
            case 1:
                return of(get(fromIndex));
            default:
                return subListUnchecked(fromIndex, toIndex);
        }
    }

    ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
        return new SubList(fromIndex, toIndex - fromIndex);
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean addAll(int index, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final E set(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void add(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    public final ImmutableList<E> asList() {
        return this;
    }

    int copyIntoArray(Object[] dst, int offset) {
        int size = size();
        for (int i = 0; i < size; i++) {
            dst[offset + i] = get(i);
        }
        return offset + size;
    }

    public ImmutableList<E> reverse() {
        return size() <= 1 ? this : new ReverseImmutableList(this);
    }

    public boolean equals(@Nullable Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    public int hashCode() {
        int hashCode = 1;
        for (int i = 0; i < size(); i++) {
            hashCode = (((hashCode * 31) + get(i).hashCode()) ^ -1) ^ -1;
        }
        return hashCode;
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }
}
