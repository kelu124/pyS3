package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSet<E> extends Indexed<E> {
    static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
    private final transient Object[] elements;
    private final transient int hashCode;
    private final transient int mask;
    @VisibleForTesting
    final transient Object[] table;

    RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
        this.elements = elements;
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }

    public boolean contains(@Nullable Object target) {
        Object[] table = this.table;
        if (target == null || table == null) {
            return false;
        }
        int i = Hashing.smearedHash(target);
        while (true) {
            i &= this.mask;
            Object candidate = table[i];
            if (candidate == null) {
                return false;
            }
            if (candidate.equals(target)) {
                return true;
            }
            i++;
        }
    }

    public int size() {
        return this.elements.length;
    }

    E get(int i) {
        return this.elements[i];
    }

    int copyIntoArray(Object[] dst, int offset) {
        System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
        return this.elements.length + offset;
    }

    ImmutableList<E> createAsList() {
        return this.table == null ? ImmutableList.of() : new RegularImmutableAsList((ImmutableCollection) this, this.elements);
    }

    boolean isPartialView() {
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}
