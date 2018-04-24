package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {

    public static abstract class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;

        @CanIgnoreReturnValue
        public abstract Builder<E> add(E e);

        public abstract ImmutableCollection<E> build();

        static int expandedCapacity(int oldCapacity, int minCapacity) {
            if (minCapacity < 0) {
                throw new AssertionError("cannot store more than MAX_VALUE elements");
            }
            int newCapacity = ((oldCapacity >> 1) + oldCapacity) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }
            if (newCapacity < 0) {
                return Integer.MAX_VALUE;
            }
            return newCapacity;
        }

        Builder() {
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... elements) {
            for (Object element : elements) {
                add(element);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> elements) {
            for (Object element : elements) {
                add(element);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                add(elements.next());
            }
            return this;
        }
    }

    static abstract class ArrayBasedBuilder<E> extends Builder<E> {
        Object[] contents;
        int size = 0;

        ArrayBasedBuilder(int initialCapacity) {
            CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
            this.contents = new Object[initialCapacity];
        }

        private void ensureCapacity(int minCapacity) {
            if (this.contents.length < minCapacity) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, Builder.expandedCapacity(this.contents.length, minCapacity));
            }
        }

        @CanIgnoreReturnValue
        public ArrayBasedBuilder<E> add(E element) {
            Preconditions.checkNotNull(element);
            ensureCapacity(this.size + 1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = element;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... elements) {
            ObjectArrays.checkElementsNotNull(elements);
            ensureCapacity(this.size + elements.length);
            System.arraycopy(elements, 0, this.contents, this.size, elements.length);
            this.size += elements.length;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                ensureCapacity(this.size + ((Collection) elements).size());
            }
            super.addAll((Iterable) elements);
            return this;
        }
    }

    public abstract boolean contains(@Nullable Object obj);

    abstract boolean isPartialView();

    public abstract UnmodifiableIterator<E> iterator();

    ImmutableCollection() {
    }

    public final Object[] toArray() {
        int size = size();
        if (size == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        Object[] result = new Object[size];
        copyIntoArray(result, 0);
        return result;
    }

    @CanIgnoreReturnValue
    public final <T> T[] toArray(T[] other) {
        Preconditions.checkNotNull(other);
        int size = size();
        if (other.length < size) {
            other = ObjectArrays.newArray((Object[]) other, size);
        } else if (other.length > size) {
            other[size] = null;
        }
        copyIntoArray(other, 0);
        return other;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        switch (size()) {
            case 0:
                return ImmutableList.of();
            case 1:
                return ImmutableList.of(iterator().next());
            default:
                return new RegularImmutableAsList(this, toArray());
        }
    }

    @CanIgnoreReturnValue
    int copyIntoArray(Object[] dst, int offset) {
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            int offset2 = offset + 1;
            dst[offset] = i$.next();
            offset = offset2;
        }
        return offset;
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
