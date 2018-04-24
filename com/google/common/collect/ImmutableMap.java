package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
    @LazyInit
    private transient ImmutableSet<Entry<K, V>> entrySet;
    @LazyInit
    private transient ImmutableSet<K> keySet;
    @LazyInit
    private transient ImmutableSetMultimap<K, V> multimapView;
    @LazyInit
    private transient ImmutableCollection<V> values;

    static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        abstract UnmodifiableIterator<Entry<K, V>> entryIterator();

        IteratorBasedImmutableMap() {
        }

        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        ImmutableSet<Entry<K, V>> createEntrySet() {
            return new ImmutableMapEntrySet<K, V>() {
                ImmutableMap<K, V> map() {
                    return IteratorBasedImmutableMap.this;
                }

                public UnmodifiableIterator<Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            };
        }
    }

    public static class Builder<K, V> {
        ImmutableMapEntry<K, V>[] entries;
        boolean entriesUsed;
        int size;
        Comparator<? super V> valueComparator;

        public Builder() {
            this(4);
        }

        Builder(int initialCapacity) {
            this.entries = new ImmutableMapEntry[initialCapacity];
            this.size = 0;
            this.entriesUsed = false;
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > this.entries.length) {
                this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(this.entries, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
                this.entriesUsed = false;
            }
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K key, V value) {
            ensureCapacity(this.size + 1);
            ImmutableMapEntry<K, V> entry = ImmutableMap.entryOf(key, value);
            ImmutableMapEntry[] immutableMapEntryArr = this.entries;
            int i = this.size;
            this.size = i + 1;
            immutableMapEntryArr[i] = entry;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return putAll(map.entrySet());
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            if (entries instanceof Collection) {
                ensureCapacity(((Collection) entries).size() + this.size);
            }
            for (Entry<? extends K, ? extends V> entry : entries) {
                put(entry);
            }
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
            Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
            this.valueComparator = (Comparator) Preconditions.checkNotNull(valueComparator, "valueComparator");
            return this;
        }

        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableMap.of();
                case 1:
                    return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(this.entries, this.size);
                        }
                        Arrays.sort(this.entries, 0, this.size, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
                    }
                    this.entriesUsed = this.size == this.entries.length;
                    return RegularImmutableMap.fromEntryArray(this.size, this.entries);
            }
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> map) {
            this.keys = new Object[map.size()];
            this.values = new Object[map.size()];
            int i = 0;
            Iterator i$ = map.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<?, ?> entry = (Entry) i$.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        Object readResolve() {
            return createMap(new Builder(this.keys.length));
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; i++) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }

    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        private MapViewOfValuesAsSingletonSets() {
        }

        public int size() {
            return ImmutableMap.this.size();
        }

        public ImmutableSet<K> keySet() {
            return ImmutableMap.this.keySet();
        }

        public boolean containsKey(@Nullable Object key) {
            return ImmutableMap.this.containsKey(key);
        }

        public ImmutableSet<V> get(@Nullable Object key) {
            V outerValue = ImmutableMap.this.get(key);
            return outerValue == null ? null : ImmutableSet.of(outerValue);
        }

        boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }

        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }

        boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }

        UnmodifiableIterator<Entry<K, ImmutableSet<V>>> entryIterator() {
            final Iterator<Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }

                public Entry<K, ImmutableSet<V>> next() {
                    final Entry<K, V> backingEntry = (Entry) backingIterator.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>() {
                        public K getKey() {
                            return backingEntry.getKey();
                        }

                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.of(backingEntry.getValue());
                        }
                    };
                }
            };
        }
    }

    abstract ImmutableSet<Entry<K, V>> createEntrySet();

    public abstract V get(@Nullable Object obj);

    abstract boolean isPartialView();

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
        return ImmutableBiMap.of(k1, v1);
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    static <K, V> ImmutableMapEntry<K, V> entryOf(K key, V value) {
        return new ImmutableMapEntry(key, value);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    static void checkNoConflict(boolean safe, String conflictDescription, Entry<?, ?> entry1, Entry<?, ?> entry2) {
        if (!safe) {
            throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap<K, V> kvMap = (ImmutableMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        } else if (map instanceof EnumMap) {
            return copyOfEnumMap((EnumMap) map);
        }
        return copyOf(map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        Entry[] entryArray = (Entry[]) ((Entry[]) Iterables.toArray((Iterable) entries, EMPTY_ENTRY_ARRAY));
        switch (entryArray.length) {
            case 0:
                return of();
            case 1:
                Entry<K, V> onlyEntry = entryArray[0];
                return of(onlyEntry.getKey(), onlyEntry.getValue());
            default:
                return RegularImmutableMap.fromEntries(entryArray);
        }
    }

    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> original) {
        EnumMap<K, V> copy = new EnumMap(original);
        for (Entry<?, ?> entry : copy.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(copy);
    }

    ImmutableMap() {
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final V remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(@Nullable Object key) {
        return get(key) != null;
    }

    public boolean containsValue(@Nullable Object value) {
        return values().contains(value);
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createKeySet();
        this.keySet = immutableSet;
        return immutableSet;
    }

    ImmutableSet<K> createKeySet() {
        return isEmpty() ? ImmutableSet.of() : new ImmutableMapKeySet(this);
    }

    UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Entry<K, V>> entryIterator = entrySet().iterator();
        return new UnmodifiableIterator<K>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public K next() {
                return ((Entry) entryIterator.next()).getKey();
            }
        };
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = createValues();
        this.values = immutableCollection;
        return immutableCollection;
    }

    ImmutableCollection<V> createValues() {
        return new ImmutableMapValues(this);
    }

    public ImmutableSetMultimap<K, V> asMultimap() {
        if (isEmpty()) {
            return ImmutableSetMultimap.of();
        }
        ImmutableSetMultimap<K, V> result = this.multimapView;
        if (result != null) {
            return result;
        }
        result = new ImmutableSetMultimap(new MapViewOfValuesAsSingletonSets(), size(), null);
        this.multimapView = result;
        return result;
    }

    public boolean equals(@Nullable Object object) {
        return Maps.equalsImpl(this, object);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    boolean isHashCodeFast() {
        return false;
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
