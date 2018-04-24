package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.Weak;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new C05651();
    private static final long serialVersionUID = 5;
    final int concurrencyLevel;
    final transient InternalEntryHelper<K, V, E, S> entryHelper;
    transient Set<Entry<K, V>> entrySet;
    final Equivalence<Object> keyEquivalence;
    transient Set<K> keySet;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V, E, S>[] segments;
    transient Collection<V> values;

    interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
        void clear();

        WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> referenceQueue, E e);

        @Nullable
        V get();

        E getEntry();
    }

    static class C05651 implements WeakValueReference<Object, Object, DummyInternalEntry> {
        C05651() {
        }

        public DummyInternalEntry getEntry() {
            return null;
        }

        public void clear() {
        }

        public Object get() {
            return null;
        }

        public WeakValueReference<Object, Object, DummyInternalEntry> copyFor(ReferenceQueue<Object> referenceQueue, DummyInternalEntry entry) {
            return this;
        }
    }

    static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;

        AbstractSerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.concurrencyLevel = concurrencyLevel;
            this.delegate = delegate;
        }

        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        void writeMapTo(ObjectOutputStream out) throws IOException {
            out.writeInt(this.delegate.size());
            for (Entry<K, V> entry : this.delegate.entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }

        MapMaker readMapMaker(ObjectInputStream in) throws IOException {
            return new MapMaker().initialCapacity(in.readInt()).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
        }

        void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
            while (true) {
                K key = in.readObject();
                if (key != null) {
                    this.delegate.put(key, in.readObject());
                } else {
                    return;
                }
            }
        }
    }

    interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
        int getHash();

        K getKey();

        E getNext();

        V getValue();
    }

    static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E> {
        final int hash;
        final K key;
        final E next;

        AbstractStrongKeyEntry(K key, int hash, @Nullable E next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return this.key;
        }

        public int getHash() {
            return this.hash;
        }

        public E getNext() {
            return this.next;
        }
    }

    static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> {
        final int hash;
        final E next;

        AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable E next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return get();
        }

        public int getHash() {
            return this.hash;
        }

        public E getNext() {
            return this.next;
        }
    }

    static final class CleanupMapTask implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
            this.mapReference = new WeakReference(map);
        }

        public void run() {
            MapMakerInternalMap<?, ?, ?, ?> map = (MapMakerInternalMap) this.mapReference.get();
            if (map == null) {
                throw new CancellationException();
            }
            for (Segment<?, ?, ?, ?> segment : map.segments) {
                segment.runCleanup();
            }
        }
    }

    static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry> {
        private DummyInternalEntry() {
            throw new AssertionError();
        }

        public DummyInternalEntry getNext() {
            throw new AssertionError();
        }

        public int getHash() {
            throw new AssertionError();
        }

        public Object getKey() {
            throw new AssertionError();
        }

        public Object getValue() {
            throw new AssertionError();
        }
    }

    abstract class HashIterator<T> implements Iterator<T> {
        Segment<K, V, E, S> currentSegment;
        AtomicReferenceArray<E> currentTable;
        WriteThroughEntry lastReturned;
        E nextEntry;
        WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public abstract T next();

        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (!nextInChain() && !nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    Segment[] segmentArr = MapMakerInternalMap.this.segments;
                    int i = this.nextSegmentIndex;
                    this.nextSegmentIndex = i - 1;
                    this.currentSegment = segmentArr[i];
                    if (this.currentSegment.count != 0) {
                        this.currentTable = this.currentSegment.table;
                        this.nextTableIndex = this.currentTable.length() - 1;
                        if (nextInTable()) {
                            return;
                        }
                    }
                }
            }
        }

        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }

        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                AtomicReferenceArray atomicReferenceArray = this.currentTable;
                int i = this.nextTableIndex;
                this.nextTableIndex = i - 1;
                InternalEntry internalEntry = (InternalEntry) atomicReferenceArray.get(i);
                this.nextEntry = internalEntry;
                if (internalEntry != null && (advanceTo(this.nextEntry) || nextInChain())) {
                    return true;
                }
            }
            return false;
        }

        boolean advanceTo(E entry) {
            try {
                K key = entry.getKey();
                V value = MapMakerInternalMap.this.getLiveValue(entry);
                if (value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    return true;
                }
                this.currentSegment.postReadCleanup();
                return false;
            } finally {
                this.currentSegment.postReadCleanup();
            }
        }

        public boolean hasNext() {
            return this.nextExternal != null;
        }

        WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            advance();
            return this.lastReturned;
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    final class EntryIterator extends HashIterator<Entry<K, V>> {
        EntryIterator() {
            super();
        }

        public Entry<K, V> next() {
            return nextEntry();
        }
    }

    private static abstract class SafeToArraySet<E> extends AbstractSet<E> {
        private SafeToArraySet() {
        }

        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        public <E> E[] toArray(E[] a) {
            return MapMakerInternalMap.toArrayList(this).toArray(a);
        }
    }

    final class EntrySet extends SafeToArraySet<Entry<K, V>> {
        EntrySet() {
            super();
        }

        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> e = (Entry) o;
            Object key = e.getKey();
            if (key == null) {
                return false;
            }
            V v = MapMakerInternalMap.this.get(key);
            if (v == null || !MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v)) {
                return false;
            }
            return true;
        }

        public boolean remove(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?, ?> e = (Entry) o;
            Object key = e.getKey();
            if (key == null || !MapMakerInternalMap.this.remove(key, e.getValue())) {
                return false;
            }
            return true;
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
        E copy(S s, E e, @Nullable E e2);

        Strength keyStrength();

        E newEntry(S s, K k, int i, @Nullable E e);

        S newSegment(MapMakerInternalMap<K, V, E, S> mapMakerInternalMap, int i, int i2);

        void setValue(S s, E e, V v);

        Strength valueStrength();
    }

    final class KeyIterator extends HashIterator<K> {
        KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    final class KeySet extends SafeToArraySet<K> {
        KeySet() {
            super();
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public boolean contains(Object o) {
            return MapMakerInternalMap.this.containsKey(o);
        }

        public boolean remove(Object o) {
            return MapMakerInternalMap.this.remove(o) != null;
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock {
        volatile int count;
        @Weak
        final MapMakerInternalMap<K, V, E, S> map;
        final int maxSegmentSize;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        volatile AtomicReferenceArray<E> table;
        int threshold;

        abstract E castForTesting(InternalEntry<K, V, ?> internalEntry);

        abstract S self();

        Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            initTable(newEntryArray(initialCapacity));
        }

        @GuardedBy("this")
        void maybeDrainReferenceQueues() {
        }

        void maybeClearReferenceQueues() {
        }

        void setValue(E entry, V value) {
            this.map.entryHelper.setValue(self(), entry, value);
        }

        E copyEntry(E original, E newNext) {
            return this.map.entryHelper.copy(self(), original, newNext);
        }

        AtomicReferenceArray<E> newEntryArray(int size) {
            return new AtomicReferenceArray(size);
        }

        void initTable(AtomicReferenceArray<E> newTable) {
            this.threshold = (newTable.length() * 3) / 4;
            if (this.threshold == this.maxSegmentSize) {
                this.threshold++;
            }
            this.table = newTable;
        }

        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            throw new AssertionError();
        }

        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            throw new AssertionError();
        }

        WeakValueReference<K, V, E> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry) {
            throw new AssertionError();
        }

        WeakValueReference<K, V, E> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, V v) {
            throw new AssertionError();
        }

        void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> weakValueReference) {
            throw new AssertionError();
        }

        void setTableEntryForTesting(int i, InternalEntry<K, V, ?> entry) {
            this.table.set(i, castForTesting(entry));
        }

        E copyForTesting(InternalEntry<K, V, ?> entry, @Nullable InternalEntry<K, V, ?> newNext) {
            return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
        }

        void setValueForTesting(InternalEntry<K, V, ?> entry, V value) {
            this.map.entryHelper.setValue(self(), castForTesting(entry), value);
        }

        E newEntryForTesting(K key, int hash, @Nullable InternalEntry<K, V, ?> next) {
            return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
        }

        @CanIgnoreReturnValue
        boolean removeTableEntryForTesting(InternalEntry<K, V, ?> entry) {
            return removeEntryForTesting(castForTesting(entry));
        }

        E removeFromChainForTesting(InternalEntry<K, V, ?> first, InternalEntry<K, V, ?> entry) {
            return removeFromChain(castForTesting(first), castForTesting(entry));
        }

        @Nullable
        V getLiveValueForTesting(InternalEntry<K, V, ?> entry) {
            return getLiveValue(castForTesting(entry));
        }

        void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
            int i = 0;
            do {
                E ref = keyReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimKey((InternalEntry) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        @GuardedBy("this")
        void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
            int i = 0;
            do {
                Reference<? extends V> ref = valueReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimValue((WeakValueReference) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
            do {
            } while (referenceQueue.poll() != null);
        }

        E getFirst(int hash) {
            AtomicReferenceArray<E> table = this.table;
            return (InternalEntry) table.get((table.length() - 1) & hash);
        }

        E getEntry(Object key, int hash) {
            if (this.count != 0) {
                for (E e = getFirst(hash); e != null; e = e.getNext()) {
                    if (e.getHash() == hash) {
                        K entryKey = e.getKey();
                        if (entryKey == null) {
                            tryDrainReferenceQueues();
                        } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                            return e;
                        }
                    }
                }
            }
            return null;
        }

        E getLiveEntry(Object key, int hash) {
            return getEntry(key, hash);
        }

        V get(Object key, int hash) {
            try {
                E e = getLiveEntry(key, hash);
                if (e == null) {
                    return null;
                }
                V value = e.getValue();
                if (value == null) {
                    tryDrainReferenceQueues();
                }
                postReadCleanup();
                return value;
            } finally {
                postReadCleanup();
            }
        }

        boolean containsKey(Object key, int hash) {
            boolean z = false;
            try {
                if (this.count != 0) {
                    E e = getLiveEntry(key, hash);
                    if (!(e == null || e.getValue() == null)) {
                        z = true;
                    }
                    postReadCleanup();
                } else {
                    postReadCleanup();
                }
                return z;
            } catch (Throwable th) {
                postReadCleanup();
            }
        }

        @VisibleForTesting
        boolean containsValue(Object value) {
            try {
                if (this.count != 0) {
                    AtomicReferenceArray<E> table = this.table;
                    int length = table.length();
                    for (int i = 0; i < length; i++) {
                        for (E e = (InternalEntry) table.get(i); e != null; e = e.getNext()) {
                            V entryValue = getLiveValue(e);
                            if (entryValue != null && this.map.valueEquivalence().equivalent(value, entryValue)) {
                                return true;
                            }
                        }
                    }
                }
                postReadCleanup();
                return false;
            } finally {
                postReadCleanup();
            }
        }

        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            lock();
            try {
                preWriteCleanup();
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        V entryValue = e.getValue();
                        if (entryValue == null) {
                            this.modCount++;
                            setValue(e, value);
                            this.count = this.count;
                            return null;
                        } else if (onlyIfAbsent) {
                            unlock();
                            return entryValue;
                        } else {
                            this.modCount++;
                            setValue(e, value);
                            unlock();
                            return entryValue;
                        }
                    }
                }
                this.modCount++;
                E newEntry = this.map.entryHelper.newEntry(self(), key, hash, first);
                setValue(newEntry, value);
                table.set(index, newEntry);
                this.count = newCount;
                unlock();
                return null;
            } finally {
                unlock();
            }
        }

        @GuardedBy("this")
        void expand() {
            AtomicReferenceArray<E> oldTable = this.table;
            int oldCapacity = oldTable.length();
            if (oldCapacity < 1073741824) {
                int newCount = this.count;
                AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
                this.threshold = (newTable.length() * 3) / 4;
                int newMask = newTable.length() - 1;
                for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
                    E head = (InternalEntry) oldTable.get(oldIndex);
                    if (head != null) {
                        E next = head.getNext();
                        int headIndex = head.getHash() & newMask;
                        if (next == null) {
                            newTable.set(headIndex, head);
                        } else {
                            E e;
                            int newIndex;
                            E tail = head;
                            int tailIndex = headIndex;
                            for (e = next; e != null; e = e.getNext()) {
                                newIndex = e.getHash() & newMask;
                                if (newIndex != tailIndex) {
                                    tailIndex = newIndex;
                                    tail = e;
                                }
                            }
                            newTable.set(tailIndex, tail);
                            for (e = head; e != tail; e = e.getNext()) {
                                newIndex = e.getHash() & newMask;
                                E newFirst = copyEntry(e, (InternalEntry) newTable.get(newIndex));
                                if (newFirst != null) {
                                    newTable.set(newIndex, newFirst);
                                } else {
                                    newCount--;
                                }
                            }
                        }
                    }
                }
                this.table = newTable;
                this.count = newCount;
            }
        }

        boolean replace(K key, int hash, V oldValue, V newValue) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        V entryValue = e.getValue();
                        if (entryValue == null) {
                            if (isCollected(e)) {
                                int newCount = this.count - 1;
                                this.modCount++;
                                newCount = this.count - 1;
                                table.set(index, removeFromChain(first, e));
                                this.count = newCount;
                            }
                            unlock();
                            return false;
                        } else if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
                            this.modCount++;
                            setValue(e, newValue);
                            unlock();
                            return true;
                        } else {
                            unlock();
                            return false;
                        }
                    }
                }
                unlock();
                return false;
            } catch (Throwable th) {
                unlock();
            }
        }

        V replace(K key, int hash, V newValue) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        V entryValue = e.getValue();
                        if (entryValue == null) {
                            if (isCollected(e)) {
                                int newCount = this.count - 1;
                                this.modCount++;
                                newCount = this.count - 1;
                                table.set(index, removeFromChain(first, e));
                                this.count = newCount;
                            }
                            unlock();
                            return null;
                        }
                        this.modCount++;
                        setValue(e, newValue);
                        unlock();
                        return entryValue;
                    }
                }
                unlock();
                return null;
            } catch (Throwable th) {
                unlock();
            }
        }

        @CanIgnoreReturnValue
        V remove(Object key, int hash) {
            lock();
            try {
                preWriteCleanup();
                int newCount = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        V entryValue = e.getValue();
                        if (entryValue == null) {
                            if (!isCollected(e)) {
                                unlock();
                                return null;
                            }
                        }
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeFromChain(first, e));
                        this.count = newCount;
                        return entryValue;
                    }
                }
                unlock();
                return null;
            } finally {
                unlock();
            }
        }

        boolean remove(Object key, int hash, Object value) {
            lock();
            try {
                preWriteCleanup();
                int newCount = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        boolean z = false;
                        if (this.map.valueEquivalence().equivalent(value, e.getValue())) {
                            z = true;
                        } else if (!isCollected(e)) {
                            unlock();
                            return false;
                        }
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeFromChain(first, e));
                        this.count = newCount;
                        return z;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        void clear() {
            if (this.count != 0) {
                lock();
                try {
                    AtomicReferenceArray<E> table = this.table;
                    for (int i = 0; i < table.length(); i++) {
                        table.set(i, null);
                    }
                    maybeClearReferenceQueues();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        E removeFromChain(E first, E entry) {
            int newCount = this.count;
            E newFirst = entry.getNext();
            for (E e = first; e != entry; e = e.getNext()) {
                E next = copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                } else {
                    newCount--;
                }
            }
            this.count = newCount;
            return newFirst;
        }

        @CanIgnoreReturnValue
        boolean reclaimKey(E entry, int hash) {
            lock();
            try {
                int newCount = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                for (E e = first; e != null; e = e.getNext()) {
                    if (e == entry) {
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeFromChain(first, e));
                        this.count = newCount;
                        return true;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        @CanIgnoreReturnValue
        boolean reclaimValue(K key, int hash, WeakValueReference<K, V, E> valueReference) {
            lock();
            try {
                int newCount = this.count - 1;
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                E e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) {
                        e = e.getNext();
                    } else if (((WeakValueEntry) e).getValueReference() == valueReference) {
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeFromChain(first, e));
                        this.count = newCount;
                        return true;
                    } else {
                        unlock();
                        return false;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        @CanIgnoreReturnValue
        boolean clearValueForTesting(K key, int hash, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            lock();
            try {
                AtomicReferenceArray<E> table = this.table;
                int index = hash & (table.length() - 1);
                E first = (InternalEntry) table.get(index);
                E e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) {
                        e = e.getNext();
                    } else if (((WeakValueEntry) e).getValueReference() == valueReference) {
                        table.set(index, removeFromChain(first, e));
                        return true;
                    } else {
                        unlock();
                        return false;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        @GuardedBy("this")
        boolean removeEntryForTesting(E entry) {
            int hash = entry.getHash();
            int newCount = this.count - 1;
            AtomicReferenceArray<E> table = this.table;
            int index = hash & (table.length() - 1);
            E first = (InternalEntry) table.get(index);
            for (E e = first; e != null; e = e.getNext()) {
                if (e == entry) {
                    this.modCount++;
                    newCount = this.count - 1;
                    table.set(index, removeFromChain(first, e));
                    this.count = newCount;
                    return true;
                }
            }
            return false;
        }

        static <K, V, E extends InternalEntry<K, V, E>> boolean isCollected(E entry) {
            return entry.getValue() == null;
        }

        @Nullable
        V getLiveValue(E entry) {
            if (entry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V value = entry.getValue();
            if (value != null) {
                return value;
            }
            tryDrainReferenceQueues();
            return null;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                runCleanup();
            }
        }

        @GuardedBy("this")
        void preWriteCleanup() {
            runLockedCleanup();
        }

        void runCleanup() {
            runLockedCleanup();
        }

        void runLockedCleanup() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }
    }

    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        SerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            writeMapTo(out);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.delegate = readMapMaker(in).makeMap();
            readEntries(in);
        }

        private Object readResolve() {
            return this.delegate;
        }
    }

    enum Strength {
        STRONG {
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        WEAK {
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        abstract Equivalence<Object> defaultEquivalence();
    }

    interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
    }

    static final class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
        @Nullable
        private volatile V value = null;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper();

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return INSTANCE;
            }

            public Strength keyStrength() {
                return Strength.STRONG;
            }

            public Strength valueStrength() {
                return Strength.STRONG;
            }

            public StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new StrongKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
            }

            public StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, StrongKeyStrongValueEntry<K, V> entry, @Nullable StrongKeyStrongValueEntry<K, V> newNext) {
                return entry.copy(newNext);
            }

            public void setValue(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, StrongKeyStrongValueEntry<K, V> entry, V value) {
                entry.setValue(value);
            }

            public StrongKeyStrongValueEntry<K, V> newEntry(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, K key, int hash, @Nullable StrongKeyStrongValueEntry<K, V> next) {
                return new StrongKeyStrongValueEntry(key, hash, next);
            }
        }

        StrongKeyStrongValueEntry(K key, int hash, @Nullable StrongKeyStrongValueEntry<K, V> next) {
            super(key, hash, next);
        }

        @Nullable
        public V getValue() {
            return this.value;
        }

        void setValue(V value) {
            this.value = value;
        }

        StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
            StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(this.key, this.hash, newNext);
            newEntry.value = this.value;
            return newEntry;
        }
    }

    static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
        StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        StrongKeyStrongValueSegment<K, V> self() {
            return this;
        }

        public StrongKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (StrongKeyStrongValueEntry) entry;
        }
    }

    interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
        void clearValue();

        WeakValueReference<K, V, E> getValueReference();
    }

    static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();

        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper();

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return INSTANCE;
            }

            public Strength keyStrength() {
                return Strength.STRONG;
            }

            public Strength valueStrength() {
                return Strength.WEAK;
            }

            public StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new StrongKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
            }

            public StrongKeyWeakValueEntry<K, V> copy(StrongKeyWeakValueSegment<K, V> segment, StrongKeyWeakValueEntry<K, V> entry, @Nullable StrongKeyWeakValueEntry<K, V> newNext) {
                if (Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(segment.queueForValues, newNext);
            }

            public void setValue(StrongKeyWeakValueSegment<K, V> segment, StrongKeyWeakValueEntry<K, V> entry, V value) {
                entry.setValue(value, segment.queueForValues);
            }

            public StrongKeyWeakValueEntry<K, V> newEntry(StrongKeyWeakValueSegment<K, V> strongKeyWeakValueSegment, K key, int hash, @Nullable StrongKeyWeakValueEntry<K, V> next) {
                return new StrongKeyWeakValueEntry(key, hash, next);
            }
        }

        StrongKeyWeakValueEntry(K key, int hash, @Nullable StrongKeyWeakValueEntry<K, V> next) {
            super(key, hash, next);
        }

        public V getValue() {
            return this.valueReference.get();
        }

        public void clearValue() {
            this.valueReference.clear();
        }

        void setValue(V value, ReferenceQueue<V> queueForValues) {
            WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
            this.valueReference = new WeakValueReferenceImpl(queueForValues, value, this);
            previous.clear();
        }

        StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext) {
            StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(this.key, this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }

        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<V> queueForValues = new ReferenceQueue();

        StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        StrongKeyWeakValueSegment<K, V> self() {
            return this;
        }

        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        public StrongKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (StrongKeyWeakValueEntry) entry;
        }

        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> e) {
            return castForTesting((InternalEntry) e).getValueReference();
        }

        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, V value) {
            return new WeakValueReferenceImpl(this.queueForValues, value, castForTesting((InternalEntry) e));
        }

        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            StrongKeyWeakValueEntry<K, V> entry = castForTesting((InternalEntry) e);
            WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newValueReference = valueReference;
            WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
            entry.valueReference = newValueReference;
            previous.clear();
        }

        void maybeDrainReferenceQueues() {
            drainValueReferenceQueue(this.queueForValues);
        }

        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForValues);
        }
    }

    final class ValueIterator extends HashIterator<V> {
        ValueIterator() {
            super();
        }

        public V next() {
            return nextEntry().getValue();
        }
    }

    final class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public boolean contains(Object o) {
            return MapMakerInternalMap.this.containsValue(o);
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        public Object[] toArray() {
            return MapMakerInternalMap.toArrayList(this).toArray();
        }

        public <E> E[] toArray(E[] a) {
            return MapMakerInternalMap.toArrayList(this).toArray(a);
        }
    }

    static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> {
        @Nullable
        private volatile V value = null;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper();

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return INSTANCE;
            }

            public Strength keyStrength() {
                return Strength.WEAK;
            }

            public Strength valueStrength() {
                return Strength.STRONG;
            }

            public WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new WeakKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
            }

            public WeakKeyStrongValueEntry<K, V> copy(WeakKeyStrongValueSegment<K, V> segment, WeakKeyStrongValueEntry<K, V> entry, @Nullable WeakKeyStrongValueEntry<K, V> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.copy(segment.queueForKeys, newNext);
            }

            public void setValue(WeakKeyStrongValueSegment<K, V> weakKeyStrongValueSegment, WeakKeyStrongValueEntry<K, V> entry, V value) {
                entry.setValue(value);
            }

            public WeakKeyStrongValueEntry<K, V> newEntry(WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable WeakKeyStrongValueEntry<K, V> next) {
                return new WeakKeyStrongValueEntry(segment.queueForKeys, key, hash, next);
            }
        }

        WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyStrongValueEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Nullable
        public V getValue() {
            return this.value;
        }

        void setValue(V value) {
            this.value = value;
        }

        WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext) {
            WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), this.hash, newNext);
            newEntry.setValue(this.value);
            return newEntry;
        }
    }

    static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();

        WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        WeakKeyStrongValueSegment<K, V> self() {
            return this;
        }

        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        public WeakKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (WeakKeyStrongValueEntry) entry;
        }

        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }
    }

    static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();

        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper();

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return INSTANCE;
            }

            public Strength keyStrength() {
                return Strength.WEAK;
            }

            public Strength valueStrength() {
                return Strength.WEAK;
            }

            public WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
                return new WeakKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
            }

            public WeakKeyWeakValueEntry<K, V> copy(WeakKeyWeakValueSegment<K, V> segment, WeakKeyWeakValueEntry<K, V> entry, @Nullable WeakKeyWeakValueEntry<K, V> newNext) {
                if (entry.getKey() == null || Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
            }

            public void setValue(WeakKeyWeakValueSegment<K, V> segment, WeakKeyWeakValueEntry<K, V> entry, V value) {
                entry.setValue(value, segment.queueForValues);
            }

            public WeakKeyWeakValueEntry<K, V> newEntry(WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable WeakKeyWeakValueEntry<K, V> next) {
                return new WeakKeyWeakValueEntry(segment.queueForKeys, key, hash, next);
            }
        }

        WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyWeakValueEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        public V getValue() {
            return this.valueReference.get();
        }

        WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext) {
            WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }

        public void clearValue() {
            this.valueReference.clear();
        }

        void setValue(V value, ReferenceQueue<V> queueForValues) {
            WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
            this.valueReference = new WeakValueReferenceImpl(queueForValues, value, this);
            previous.clear();
        }

        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
        private final ReferenceQueue<V> queueForValues = new ReferenceQueue();

        WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        WeakKeyWeakValueSegment<K, V> self() {
            return this;
        }

        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        public WeakKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> entry) {
            return (WeakKeyWeakValueEntry) entry;
        }

        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> e) {
            return castForTesting((InternalEntry) e).getValueReference();
        }

        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, V value) {
            return new WeakValueReferenceImpl(this.queueForValues, value, castForTesting((InternalEntry) e));
        }

        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> e, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            WeakKeyWeakValueEntry<K, V> entry = castForTesting((InternalEntry) e);
            WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newValueReference = valueReference;
            WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
            entry.valueReference = newValueReference;
            previous.clear();
        }

        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
            drainValueReferenceQueue(this.queueForValues);
        }

        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }
    }

    static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> {
        final E entry;

        WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
            super(referent, queue);
            this.entry = entry;
        }

        public E getEntry() {
            return this.entry;
        }

        public WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
            return new WeakValueReferenceImpl(queue, get(), entry);
        }
    }

    final class WriteThroughEntry extends AbstractMapEntry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry<?, ?> that = (Entry) object;
            if (this.key.equals(that.getKey()) && this.value.equals(that.getValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        public V setValue(V newValue) {
            V oldValue = MapMakerInternalMap.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
    }

    private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyEquivalence = builder.getKeyEquivalence();
        this.entryHelper = entryHelper;
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        int segmentShift = 0;
        int segmentCount = 1;
        while (segmentCount < this.concurrencyLevel) {
            segmentShift++;
            segmentCount <<= 1;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            segmentCapacity++;
        }
        int segmentSize = 1;
        while (segmentSize < segmentCapacity) {
            segmentSize <<= 1;
        }
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = createSegment(segmentSize, -1);
        }
    }

    static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap(builder, Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap(builder, Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap(builder, Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap(builder, Helper.instance());
        }
        throw new AssertionError();
    }

    static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
        return UNSET_WEAK_VALUE_REFERENCE;
    }

    static int rehash(int h) {
        h += (h << 15) ^ -12931;
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return (h >>> 16) ^ h;
    }

    @VisibleForTesting
    E copyEntry(E original, E newNext) {
        return segmentFor(original.getHash()).copyEntry(original, newNext);
    }

    int hash(Object key) {
        return rehash(this.keyEquivalence.hash(key));
    }

    void reclaimValue(WeakValueReference<K, V, E> valueReference) {
        E entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    void reclaimKey(E entry) {
        int hash = entry.getHash();
        segmentFor(hash).reclaimKey(entry, hash);
    }

    @VisibleForTesting
    boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
        return segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null;
    }

    Segment<K, V, E, S> segmentFor(int hash) {
        return this.segments[(hash >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
        return this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
    }

    V getLiveValue(E entry) {
        if (entry.getKey() == null) {
            return null;
        }
        V value = entry.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
        return new Segment[ssize];
    }

    @VisibleForTesting
    Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }

    @VisibleForTesting
    Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }

    @VisibleForTesting
    Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }

    public boolean isEmpty() {
        int i;
        long sum = 0;
        Segment<K, V, E, S>[] segments = this.segments;
        for (i = 0; i < segments.length; i++) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += (long) segments[i].modCount;
        }
        if (sum != 0) {
            for (i = 0; i < segments.length; i++) {
                if (segments[i].count != 0) {
                    return false;
                }
                sum -= (long) segments[i].modCount;
            }
            if (sum != 0) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        long sum = 0;
        for (Segment segment : this.segments) {
            sum += (long) segment.count;
        }
        return Ints.saturatedCast(sum);
    }

    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).get(key, hash);
    }

    E getEntry(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).getEntry(key, hash);
    }

    public boolean containsKey(@Nullable Object key) {
        if (key == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).containsKey(key, hash);
    }

    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        }
        Segment<K, V, E, S>[] segments = this.segments;
        long last = -1;
        for (int i = 0; i < 3; i++) {
            long sum = 0;
            for (Segment<K, V, E, S> segment : segments) {
                int unused = segment.count;
                AtomicReferenceArray<E> table = segment.table;
                for (int j = 0; j < table.length(); j++) {
                    for (E e = (InternalEntry) table.get(j); e != null; e = e.getNext()) {
                        V v = segment.getLiveValue(e);
                        if (v != null && valueEquivalence().equivalent(value, v)) {
                            return true;
                        }
                    }
                }
                sum += (long) segment.modCount;
            }
            if (sum == last) {
                break;
            }
            last = sum;
        }
        return false;
    }

    @CanIgnoreReturnValue
    public V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, false);
    }

    @CanIgnoreReturnValue
    public V putIfAbsent(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, true);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash);
    }

    @CanIgnoreReturnValue
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        if (key == null || value == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, value);
    }

    @CanIgnoreReturnValue
    public boolean replace(K key, @Nullable V oldValue, V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    @CanIgnoreReturnValue
    public V replace(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, value);
    }

    public void clear() {
        for (Segment<K, V, E, S> segment : this.segments) {
            segment.clear();
        }
    }

    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        ks = new KeySet();
        this.keySet = ks;
        return ks;
    }

    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        vs = new Values();
        this.values = vs;
        return vs;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        es = new EntrySet();
        this.entrySet = es;
        return es;
    }

    private static <E> ArrayList<E> toArrayList(Collection<E> c) {
        ArrayList<E> result = new ArrayList(c.size());
        Iterators.addAll(result, c.iterator());
        return result;
    }

    Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
    }
}
