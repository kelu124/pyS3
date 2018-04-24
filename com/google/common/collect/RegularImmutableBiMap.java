package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
    static final double MAX_LOAD_FACTOR = 1.2d;
    private final transient Entry<K, V>[] entries;
    private final transient int hashCode;
    @RetainedWith
    @LazyInit
    private transient ImmutableBiMap<V, K> inverse;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] valueTable;

    private final class Inverse extends ImmutableBiMap<V, K> {

        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {

            class C06201 extends ImmutableAsList<Entry<V, K>> {
                C06201() {
                }

                public Entry<V, K> get(int index) {
                    Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                    return Maps.immutableEntry(entry.getValue(), entry.getKey());
                }

                ImmutableCollection<Entry<V, K>> delegateCollection() {
                    return InverseEntrySet.this;
                }
            }

            InverseEntrySet() {
            }

            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            boolean isHashCodeFast() {
                return true;
            }

            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            public UnmodifiableIterator<Entry<V, K>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<V, K>> createAsList() {
                return new C06201();
            }
        }

        private Inverse() {
        }

        public int size() {
            return inverse().size();
        }

        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        public K get(@Nullable Object value) {
            if (value == null || RegularImmutableBiMap.this.valueTable == null) {
                return null;
            }
            for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask]; entry != null; entry = entry.getNextInValueBucket()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
            return null;
        }

        ImmutableSet<Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        boolean isPartialView() {
            return false;
        }

        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    private static class InverseSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        private final ImmutableBiMap<K, V> forward;

        InverseSerializedForm(ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }

    static <K, V> RegularImmutableBiMap<K, V> fromEntries(Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }

    static <K, V> RegularImmutableBiMap<K, V> fromEntryArray(int n, Entry<K, V>[] entryArray) {
        Entry<K, V>[] entries;
        Preconditions.checkPositionIndex(n, entryArray.length);
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        int mask = tableSize - 1;
        ImmutableMapEntry<K, V>[] keyTable = ImmutableMapEntry.createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] valueTable = ImmutableMapEntry.createEntryArray(tableSize);
        if (n == entryArray.length) {
            entries = entryArray;
        } else {
            entries = ImmutableMapEntry.createEntryArray(n);
        }
        int hashCode = 0;
        for (int i = 0; i < n; i++) {
            ImmutableMapEntry<K, V> newEntry;
            Entry<K, V> entry = entryArray[i];
            K key = entry.getKey();
            V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & mask;
            int valueBucket = Hashing.smear(valueHash) & mask;
            ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, nextInKeyBucket);
            ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
            checkNoConflictInValueBucket(value, entry, nextInValueBucket);
            if (nextInValueBucket == null && nextInKeyBucket == null) {
                boolean reusable = (entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable();
                newEntry = reusable ? (ImmutableMapEntry) entry : new ImmutableMapEntry(key, value);
            } else {
                newEntry = new NonTerminalImmutableBiMapEntry(key, value, nextInKeyBucket, nextInValueBucket);
            }
            keyTable[keyBucket] = newEntry;
            valueTable[valueBucket] = newEntry;
            entries[i] = newEntry;
            hashCode += keyHash ^ valueHash;
        }
        return new RegularImmutableBiMap(keyTable, valueTable, entries, mask, hashCode);
    }

    private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] keyTable, ImmutableMapEntry<K, V>[] valueTable, Entry<K, V>[] entries, int mask, int hashCode) {
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.mask = mask;
        this.hashCode = hashCode;
    }

    private static void checkNoConflictInValueBucket(Object value, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> valueBucketHead) {
        while (valueBucketHead != null) {
            ImmutableMap.checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
            valueBucketHead = valueBucketHead.getNextInValueBucket();
        }
    }

    @Nullable
    public V get(@Nullable Object key) {
        return this.keyTable == null ? null : RegularImmutableMap.get(key, this.keyTable, this.mask);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new RegularEntrySet(this, this.entries);
    }

    boolean isHashCodeFast() {
        return true;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return this.entries.length;
    }

    public ImmutableBiMap<V, K> inverse() {
        if (isEmpty()) {
            return ImmutableBiMap.of();
        }
        ImmutableBiMap<V, K> result = this.inverse;
        if (result != null) {
            return result;
        }
        result = new Inverse();
        this.inverse = result;
        return result;
    }
}
