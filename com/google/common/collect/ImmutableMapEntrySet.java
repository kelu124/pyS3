package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet<Entry<K, V>> {

    @GwtIncompatible
    private static class EntrySetSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, V> map;

        EntrySetSerializedForm(ImmutableMap<K, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }

    static final class RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V> {
        private final transient Entry<K, V>[] entries;
        @Weak
        private final transient ImmutableMap<K, V> map;

        RegularEntrySet(ImmutableMap<K, V> map, Entry<K, V>[] entries) {
            this.map = map;
            this.entries = entries;
        }

        ImmutableMap<K, V> map() {
            return this.map;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return Iterators.forArray(this.entries);
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, this.entries);
        }
    }

    abstract ImmutableMap<K, V> map();

    ImmutableMapEntrySet() {
    }

    public int size() {
        return map().size();
    }

    public boolean contains(@Nullable Object object) {
        if (!(object instanceof Entry)) {
            return false;
        }
        Entry<?, ?> entry = (Entry) object;
        V value = map().get(entry.getKey());
        if (value == null || !value.equals(entry.getValue())) {
            return false;
        }
        return true;
    }

    boolean isPartialView() {
        return map().isPartialView();
    }

    @GwtIncompatible
    boolean isHashCodeFast() {
        return map().isHashCodeFast();
    }

    public int hashCode() {
        return map().hashCode();
    }

    @GwtIncompatible
    Object writeReplace() {
        return new EntrySetSerializedForm(map());
    }
}
