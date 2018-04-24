package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        Builder(int size) {
            super(size);
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K key, V value) {
            super.put(key, value);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll((Map) map);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            super.putAll((Iterable) entries);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
            super.orderEntriesByValue(valueComparator);
            return this;
        }

        public ImmutableBiMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableBiMap.of();
                case 1:
                    return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(this.entries, this.size);
                        }
                        Arrays.sort(this.entries, 0, this.size, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
                    }
                    this.entriesUsed = this.size == this.entries.length;
                    return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
            }
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableBiMap<?, ?> bimap) {
            super(bimap);
        }

        Object readResolve() {
            return createMap(new Builder());
        }
    }

    public abstract ImmutableBiMap<V, K> inverse();

    public static <K, V> ImmutableBiMap<K, V> of() {
        return RegularImmutableBiMap.EMPTY;
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
        return new SingletonImmutableBiMap(k1, v1);
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            ImmutableBiMap<K, V> bimap = (ImmutableBiMap) map;
            if (!bimap.isPartialView()) {
                return bimap;
            }
        }
        return copyOf(map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        Entry[] entryArray = (Entry[]) ((Entry[]) Iterables.toArray((Iterable) entries, EMPTY_ENTRY_ARRAY));
        switch (entryArray.length) {
            case 0:
                return of();
            case 1:
                Entry<K, V> entry = entryArray[0];
                return of(entry.getKey(), entry.getValue());
            default:
                return RegularImmutableBiMap.fromEntries(entryArray);
        }
    }

    ImmutableBiMap() {
    }

    public ImmutableSet<V> values() {
        return inverse().keySet();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public V forcePut(K k, V v) {
        throw new UnsupportedOperationException();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
