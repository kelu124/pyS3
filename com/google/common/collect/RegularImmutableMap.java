package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    private final transient Entry<K, V>[] entries;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] table;

    @GwtCompatible(emulated = true)
    private static final class KeySet<K, V> extends Indexed<K> {
        @Weak
        private final RegularImmutableMap<K, V> map;

        @GwtIncompatible
        private static class SerializedForm<K> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<K, ?> map;

            SerializedForm(ImmutableMap<K, ?> map) {
                this.map = map;
            }

            Object readResolve() {
                return this.map.keySet();
            }
        }

        KeySet(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        K get(int index) {
            return this.map.entries[index].getKey();
        }

        public boolean contains(Object object) {
            return this.map.containsKey(object);
        }

        boolean isPartialView() {
            return true;
        }

        public int size() {
            return this.map.size();
        }

        @GwtIncompatible
        Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    @GwtCompatible(emulated = true)
    private static final class Values<K, V> extends ImmutableList<V> {
        @Weak
        final RegularImmutableMap<K, V> map;

        @GwtIncompatible
        private static class SerializedForm<V> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<?, V> map;

            SerializedForm(ImmutableMap<?, V> map) {
                this.map = map;
            }

            Object readResolve() {
                return this.map.values();
            }
        }

        Values(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        public V get(int index) {
            return this.map.entries[index].getValue();
        }

        public int size() {
            return this.map.size();
        }

        boolean isPartialView() {
            return true;
        }

        @GwtIncompatible
        Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    static <K, V> RegularImmutableMap<K, V> fromEntries(Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }

    static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n, Entry<K, V>[] entryArray) {
        Entry<K, V>[] entries;
        Preconditions.checkPositionIndex(n, entryArray.length);
        if (n == entryArray.length) {
            entries = entryArray;
        } else {
            entries = ImmutableMapEntry.createEntryArray(n);
        }
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
        int mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < n; entryIndex++) {
            ImmutableMapEntry<K, V> newEntry;
            Entry<K, V> entry = entryArray[entryIndex];
            K key = entry.getKey();
            V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int tableIndex = Hashing.smear(key.hashCode()) & mask;
            ImmutableMapEntry<K, V> existing = table[tableIndex];
            if (existing == null) {
                boolean reusable = (entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable();
                newEntry = reusable ? (ImmutableMapEntry) entry : new ImmutableMapEntry(key, value);
            } else {
                newEntry = new NonTerminalImmutableMapEntry(key, value, existing);
            }
            table[tableIndex] = newEntry;
            entries[entryIndex] = newEntry;
            checkNoConflictInKeyBucket(key, newEntry, existing);
        }
        return new RegularImmutableMap(entries, table, mask);
    }

    private RegularImmutableMap(Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
        this.entries = entries;
        this.table = table;
        this.mask = mask;
    }

    static void checkNoConflictInKeyBucket(Object key, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead) {
        while (keyBucketHead != null) {
            ImmutableMap.checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
            keyBucketHead = keyBucketHead.getNextInKeyBucket();
        }
    }

    public V get(@Nullable Object key) {
        return get(key, this.table, this.mask);
    }

    @Nullable
    static <V> V get(@Nullable Object key, ImmutableMapEntry<?, V>[] keyTable, int mask) {
        if (key == null) {
            return null;
        }
        for (ImmutableMapEntry<?, V> entry = keyTable[Hashing.smear(key.hashCode()) & mask]; entry != null; entry = entry.getNextInKeyBucket()) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    boolean isPartialView() {
        return false;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new RegularEntrySet(this, this.entries);
    }

    ImmutableSet<K> createKeySet() {
        return new KeySet(this);
    }

    ImmutableCollection<V> createValues() {
        return new Values(this);
    }
}
