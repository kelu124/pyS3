package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class HashBiMap<K, V> extends IteratorBasedAbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final double LOAD_FACTOR = 1.0d;
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    private transient BiEntry<K, V> firstInKeyInsertionOrder;
    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    @RetainedWith
    private transient BiMap<V, K> inverse;
    private transient BiEntry<K, V> lastInKeyInsertionOrder;
    private transient int mask;
    private transient int modCount;
    private transient int size;

    abstract class Itr<T> implements Iterator<T> {
        int expectedModCount = HashBiMap.this.modCount;
        BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;
        BiEntry<K, V> toRemove = null;

        abstract T output(BiEntry<K, V> biEntry);

        Itr() {
        }

        public boolean hasNext() {
            if (HashBiMap.this.modCount == this.expectedModCount) {
                return this.next != null;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public T next() {
            if (hasNext()) {
                BiEntry<K, V> entry = this.next;
                this.next = entry.nextInKeyInsertionOrder;
                this.toRemove = entry;
                return output(entry);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (HashBiMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            CollectPreconditions.checkRemove(this.toRemove != null);
            HashBiMap.this.delete(this.toRemove);
            this.expectedModCount = HashBiMap.this.modCount;
            this.toRemove = null;
        }
    }

    class C05031 extends Itr<Entry<K, V>> {

        class MapEntry extends AbstractMapEntry<K, V> {
            BiEntry<K, V> delegate;

            MapEntry(BiEntry<K, V> entry) {
                this.delegate = entry;
            }

            public K getKey() {
                return this.delegate.key;
            }

            public V getValue() {
                return this.delegate.value;
            }

            public V setValue(V value) {
                V oldValue = this.delegate.value;
                int valueHash = Hashing.smearedHash(value);
                if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
                    return value;
                }
                Preconditions.checkArgument(HashBiMap.this.seekByValue(value, valueHash) == null, "value already present: %s", (Object) value);
                HashBiMap.this.delete(this.delegate);
                BiEntry<K, V> newEntry = new BiEntry(this.delegate.key, this.delegate.keyHash, value, valueHash);
                HashBiMap.this.insert(newEntry, this.delegate);
                this.delegate.prevInKeyInsertionOrder = null;
                this.delegate.nextInKeyInsertionOrder = null;
                C05031.this.expectedModCount = HashBiMap.this.modCount;
                if (C05031.this.toRemove == this.delegate) {
                    C05031.this.toRemove = newEntry;
                }
                this.delegate = newEntry;
                return oldValue;
            }
        }

        C05031() {
            super();
        }

        Entry<K, V> output(BiEntry<K, V> entry) {
            return new MapEntry(entry);
        }
    }

    private static final class BiEntry<K, V> extends ImmutableEntry<K, V> {
        final int keyHash;
        @Nullable
        BiEntry<K, V> nextInKToVBucket;
        @Nullable
        BiEntry<K, V> nextInKeyInsertionOrder;
        @Nullable
        BiEntry<K, V> nextInVToKBucket;
        @Nullable
        BiEntry<K, V> prevInKeyInsertionOrder;
        final int valueHash;

        BiEntry(K key, int keyHash, V value, int valueHash) {
            super(key, value);
            this.keyHash = keyHash;
            this.valueHash = valueHash;
        }
    }

    private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {

        class C05051 extends EntrySet<V, K> {

            class C05041 extends Itr<Entry<V, K>> {

                class InverseEntry extends AbstractMapEntry<V, K> {
                    BiEntry<K, V> delegate;

                    InverseEntry(BiEntry<K, V> entry) {
                        this.delegate = entry;
                    }

                    public V getKey() {
                        return this.delegate.value;
                    }

                    public K getValue() {
                        return this.delegate.key;
                    }

                    public K setValue(K key) {
                        K oldKey = this.delegate.key;
                        int keyHash = Hashing.smearedHash(key);
                        if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
                            return key;
                        }
                        Preconditions.checkArgument(HashBiMap.this.seekByKey(key, keyHash) == null, "value already present: %s", (Object) key);
                        HashBiMap.this.delete(this.delegate);
                        BiEntry<K, V> newEntry = new BiEntry(key, keyHash, this.delegate.value, this.delegate.valueHash);
                        this.delegate = newEntry;
                        HashBiMap.this.insert(newEntry, null);
                        C05041.this.expectedModCount = HashBiMap.this.modCount;
                        return oldKey;
                    }
                }

                C05041() {
                    super();
                }

                Entry<V, K> output(BiEntry<K, V> entry) {
                    return new InverseEntry(entry);
                }
            }

            C05051() {
            }

            Map<V, K> map() {
                return Inverse.this;
            }

            public Iterator<Entry<V, K>> iterator() {
                return new C05041();
            }
        }

        private final class InverseKeySet extends KeySet<V, K> {

            class C05061 extends Itr<V> {
                C05061() {
                    super();
                }

                V output(BiEntry<K, V> entry) {
                    return entry.value;
                }
            }

            InverseKeySet() {
                super(Inverse.this);
            }

            public boolean remove(@Nullable Object o) {
                BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
                if (entry == null) {
                    return false;
                }
                HashBiMap.this.delete(entry);
                return true;
            }

            public Iterator<V> iterator() {
                return new C05061();
            }
        }

        private Inverse() {
        }

        BiMap<K, V> forward() {
            return HashBiMap.this;
        }

        public int size() {
            return HashBiMap.this.size;
        }

        public void clear() {
            forward().clear();
        }

        public boolean containsKey(@Nullable Object value) {
            return forward().containsValue(value);
        }

        public K get(@Nullable Object value) {
            return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
        }

        public K put(@Nullable V value, @Nullable K key) {
            return HashBiMap.this.putInverse(value, key, false);
        }

        public K forcePut(@Nullable V value, @Nullable K key) {
            return HashBiMap.this.putInverse(value, key, true);
        }

        public K remove(@Nullable Object value) {
            BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
            if (entry == null) {
                return null;
            }
            HashBiMap.this.delete(entry);
            entry.prevInKeyInsertionOrder = null;
            entry.nextInKeyInsertionOrder = null;
            return entry.key;
        }

        public BiMap<K, V> inverse() {
            return forward();
        }

        public Set<V> keySet() {
            return new InverseKeySet();
        }

        public Set<K> values() {
            return forward().keySet();
        }

        public Set<Entry<V, K>> entrySet() {
            return new C05051();
        }

        Object writeReplace() {
            return new InverseSerializedForm(HashBiMap.this);
        }
    }

    private static final class InverseSerializedForm<K, V> implements Serializable {
        private final HashBiMap<K, V> bimap;

        InverseSerializedForm(HashBiMap<K, V> bimap) {
            this.bimap = bimap;
        }

        Object readResolve() {
            return this.bimap.inverse();
        }
    }

    private final class KeySet extends KeySet<K, V> {

        class C05071 extends Itr<K> {
            C05071() {
                super();
            }

            K output(BiEntry<K, V> entry) {
                return entry.key;
            }
        }

        KeySet() {
            super(HashBiMap.this);
        }

        public Iterator<K> iterator() {
            return new C05071();
        }

        public boolean remove(@Nullable Object o) {
            BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
            if (entry == null) {
                return false;
            }
            HashBiMap.this.delete(entry);
            entry.prevInKeyInsertionOrder = null;
            entry.nextInKeyInsertionOrder = null;
            return true;
        }
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap(expectedSize);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    private HashBiMap(int expectedSize) {
        init(expectedSize);
    }

    private void init(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        int tableSize = Hashing.closedTableSize(expectedSize, 1.0d);
        this.hashTableKToV = createTable(tableSize);
        this.hashTableVToK = createTable(tableSize);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.size = 0;
        this.mask = tableSize - 1;
        this.modCount = 0;
    }

    private void delete(BiEntry<K, V> entry) {
        BiEntry<K, V> bucketEntry;
        int keyBucket = entry.keyHash & this.mask;
        BiEntry<K, V> prevBucketEntry = null;
        for (bucketEntry = this.hashTableKToV[keyBucket]; bucketEntry != entry; bucketEntry = bucketEntry.nextInKToVBucket) {
            prevBucketEntry = bucketEntry;
        }
        if (prevBucketEntry == null) {
            this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
        } else {
            prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
        }
        int valueBucket = entry.valueHash & this.mask;
        prevBucketEntry = null;
        for (bucketEntry = this.hashTableVToK[valueBucket]; bucketEntry != entry; bucketEntry = bucketEntry.nextInVToKBucket) {
            prevBucketEntry = bucketEntry;
        }
        if (prevBucketEntry == null) {
            this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
        } else {
            prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
        }
        if (entry.prevInKeyInsertionOrder == null) {
            this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        } else {
            entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        }
        if (entry.nextInKeyInsertionOrder == null) {
            this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        } else {
            entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        }
        this.size--;
        this.modCount++;
    }

    private void insert(BiEntry<K, V> entry, @Nullable BiEntry<K, V> oldEntryForKey) {
        int keyBucket = entry.keyHash & this.mask;
        entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
        this.hashTableKToV[keyBucket] = entry;
        int valueBucket = entry.valueHash & this.mask;
        entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
        this.hashTableVToK[valueBucket] = entry;
        if (oldEntryForKey == null) {
            entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
            entry.nextInKeyInsertionOrder = null;
            if (this.lastInKeyInsertionOrder == null) {
                this.firstInKeyInsertionOrder = entry;
            } else {
                this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            this.lastInKeyInsertionOrder = entry;
        } else {
            entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
            if (entry.prevInKeyInsertionOrder == null) {
                this.firstInKeyInsertionOrder = entry;
            } else {
                entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
            if (entry.nextInKeyInsertionOrder == null) {
                this.lastInKeyInsertionOrder = entry;
            } else {
                entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
            }
        }
        this.size++;
        this.modCount++;
    }

    private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
        BiEntry<K, V> entry = this.hashTableKToV[this.mask & keyHash];
        while (entry != null) {
            if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
                return entry;
            }
            entry = entry.nextInKToVBucket;
        }
        return null;
    }

    private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
        BiEntry<K, V> entry = this.hashTableVToK[this.mask & valueHash];
        while (entry != null) {
            if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
                return entry;
            }
            entry = entry.nextInVToKBucket;
        }
        return null;
    }

    public boolean containsKey(@Nullable Object key) {
        return seekByKey(key, Hashing.smearedHash(key)) != null;
    }

    public boolean containsValue(@Nullable Object value) {
        return seekByValue(value, Hashing.smearedHash(value)) != null;
    }

    @Nullable
    public V get(@Nullable Object key) {
        return Maps.valueOrNull(seekByKey(key, Hashing.smearedHash(key)));
    }

    @CanIgnoreReturnValue
    public V put(@Nullable K key, @Nullable V value) {
        return put(key, value, false);
    }

    @CanIgnoreReturnValue
    public V forcePut(@Nullable K key, @Nullable V value) {
        return put(key, value, true);
    }

    private V put(@Nullable K key, @Nullable V value, boolean force) {
        int keyHash = Hashing.smearedHash(key);
        int valueHash = Hashing.smearedHash(value);
        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value)) {
            return value;
        }
        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        if (oldEntryForValue != null) {
            if (force) {
                delete(oldEntryForValue);
            } else {
                throw new IllegalArgumentException("value already present: " + value);
            }
        }
        BiEntry<K, V> newEntry = new BiEntry(key, keyHash, value, valueHash);
        if (oldEntryForKey != null) {
            delete(oldEntryForKey);
            insert(newEntry, oldEntryForKey);
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
            rehashIfNecessary();
            return oldEntryForKey.value;
        }
        insert(newEntry, null);
        rehashIfNecessary();
        return null;
    }

    @Nullable
    private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
        int valueHash = Hashing.smearedHash(value);
        int keyHash = Hashing.smearedHash(key);
        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key)) {
            return key;
        }
        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForKey != null) {
            if (force) {
                delete(oldEntryForKey);
            } else {
                throw new IllegalArgumentException("value already present: " + key);
            }
        }
        if (oldEntryForValue != null) {
            delete(oldEntryForValue);
        }
        insert(new BiEntry(key, keyHash, value, valueHash), oldEntryForKey);
        if (oldEntryForKey != null) {
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
        }
        rehashIfNecessary();
        return Maps.keyOrNull(oldEntryForValue);
    }

    private void rehashIfNecessary() {
        BiEntry<K, V>[] oldKToV = this.hashTableKToV;
        if (Hashing.needsResizing(this.size, oldKToV.length, 1.0d)) {
            int newTableSize = oldKToV.length * 2;
            this.hashTableKToV = createTable(newTableSize);
            this.hashTableVToK = createTable(newTableSize);
            this.mask = newTableSize - 1;
            this.size = 0;
            for (BiEntry<K, V> entry = this.firstInKeyInsertionOrder; entry != null; entry = entry.nextInKeyInsertionOrder) {
                insert(entry, entry);
            }
            this.modCount++;
        }
    }

    private BiEntry<K, V>[] createTable(int length) {
        return new BiEntry[length];
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object key) {
        BiEntry<K, V> entry = seekByKey(key, Hashing.smearedHash(key));
        if (entry == null) {
            return null;
        }
        delete(entry);
        entry.prevInKeyInsertionOrder = null;
        entry.nextInKeyInsertionOrder = null;
        return entry.value;
    }

    public void clear() {
        this.size = 0;
        Arrays.fill(this.hashTableKToV, null);
        Arrays.fill(this.hashTableVToK, null);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.modCount++;
    }

    public int size() {
        return this.size;
    }

    public Set<K> keySet() {
        return new KeySet();
    }

    public Set<V> values() {
        return inverse().keySet();
    }

    Iterator<Entry<K, V>> entryIterator() {
        return new C05031();
    }

    public BiMap<V, K> inverse() {
        if (this.inverse != null) {
            return this.inverse;
        }
        BiMap<V, K> inverse = new Inverse();
        this.inverse = inverse;
        return inverse;
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap(this, stream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init(16);
        Serialization.populateMap(this, stream, Serialization.readCount(stream));
    }
}
