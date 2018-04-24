package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    @LazyInit
    private transient int cachedHashCode;
    final transient E element;

    SingletonImmutableSet(E element) {
        this.element = Preconditions.checkNotNull(element);
    }

    SingletonImmutableSet(E element, int hashCode) {
        this.element = element;
        this.cachedHashCode = hashCode;
    }

    public int size() {
        return 1;
    }

    public boolean contains(Object target) {
        return this.element.equals(target);
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    ImmutableList<E> createAsList() {
        return ImmutableList.of(this.element);
    }

    boolean isPartialView() {
        return false;
    }

    int copyIntoArray(Object[] dst, int offset) {
        dst[offset] = this.element;
        return offset + 1;
    }

    public final int hashCode() {
        int code = this.cachedHashCode;
        if (code != 0) {
            return code;
        }
        code = this.element.hashCode();
        this.cachedHashCode = code;
        return code;
    }

    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    public String toString() {
        return '[' + this.element.toString() + ']';
    }
}
