package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;

@GwtCompatible(emulated = true, serializable = true)
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;
    @LazyInit
    private transient int hashCode;

    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumSet<E> delegate;

        EnumSerializedForm(EnumSet<E> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone());
        }
    }

    static ImmutableSet asImmutable(EnumSet set) {
        switch (set.size()) {
            case 0:
                return ImmutableSet.of();
            case 1:
                return ImmutableSet.of(Iterables.getOnlyElement(set));
            default:
                return new ImmutableEnumSet(set);
        }
    }

    private ImmutableEnumSet(EnumSet<E> delegate) {
        this.delegate = delegate;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean contains(Object object) {
        return this.delegate.contains(object);
    }

    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof ImmutableEnumSet) {
            collection = ((ImmutableEnumSet) collection).delegate;
        }
        return this.delegate.containsAll(collection);
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumSet) {
            object = ((ImmutableEnumSet) object).delegate;
        }
        return this.delegate.equals(object);
    }

    boolean isHashCodeFast() {
        return true;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        i = this.delegate.hashCode();
        this.hashCode = i;
        return i;
    }

    public String toString() {
        return this.delegate.toString();
    }

    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
