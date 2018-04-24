package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache.SimpleStatsCounter;
import com.google.common.cache.AbstractCache.StatsCounter;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.CacheLoader.UnsupportedLoadingOperationException;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.j2objc.annotations.Weak;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@GwtCompatible(emulated = true)
class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final Queue<? extends Object> DISCARDING_QUEUE = new C04392();
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final ValueReference<Object, Object> UNSET = new C04381();
    static final Logger logger = Logger.getLogger(LocalCache.class.getName());
    final int concurrencyLevel;
    @Nullable
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;
    Set<Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    Collection<V> values;
    final Weigher<K, V> weigher;

    interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @Nullable V v, ReferenceEntry<K, V> referenceEntry);

        @Nullable
        V get();

        @Nullable
        ReferenceEntry<K, V> getEntry();

        int getWeight();

        boolean isActive();

        boolean isLoading();

        void notifyNewValue(@Nullable V v);

        V waitForValue() throws ExecutionException;
    }

    static class C04381 implements ValueReference<Object, Object> {
        C04381() {
        }

        public Object get() {
            return null;
        }

        public int getWeight() {
            return 0;
        }

        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> referenceQueue, @Nullable Object value, ReferenceEntry<Object, Object> referenceEntry) {
            return this;
        }

        public boolean isLoading() {
            return false;
        }

        public boolean isActive() {
            return false;
        }

        public Object waitForValue() {
            return null;
        }

        public void notifyNewValue(Object newValue) {
        }
    }

    static class C04392 extends AbstractQueue<Object> {
        C04392() {
        }

        public boolean offer(Object o) {
            return true;
        }

        public Object peek() {
            return null;
        }

        public Object poll() {
            return null;
        }

        public int size() {
            return 0;
        }

        public Iterator<Object> iterator() {
            return ImmutableSet.of().iterator();
        }
    }

    abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        @Weak
        final ConcurrentMap<?, ?> map;

        AbstractCacheSet(ConcurrentMap<?, ?> map) {
            this.map = map;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void clear() {
            this.map.clear();
        }

        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        public <E> E[] toArray(E[] a) {
            return LocalCache.toArrayList(this).toArray(a);
        }
    }

    interface ReferenceEntry<K, V> {
        long getAccessTime();

        int getHash();

        @Nullable
        K getKey();

        @Nullable
        ReferenceEntry<K, V> getNext();

        ReferenceEntry<K, V> getNextInAccessQueue();

        ReferenceEntry<K, V> getNextInWriteQueue();

        ReferenceEntry<K, V> getPreviousInAccessQueue();

        ReferenceEntry<K, V> getPreviousInWriteQueue();

        ValueReference<K, V> getValueReference();

        long getWriteTime();

        void setAccessTime(long j);

        void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry);

        void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry);

        void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry);

        void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry);

        void setValueReference(ValueReference<K, V> valueReference);

        void setWriteTime(long j);
    }

    static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        public int getHash() {
            throw new UnsupportedOperationException();
        }

        public K getKey() {
            throw new UnsupportedOperationException();
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long time) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setWriteTime(long time) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
    }

    static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C04401();

        class C04401 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextAccess = this;
            ReferenceEntry<K, V> previousAccess = this;

            C04401() {
            }

            public long getAccessTime() {
                return Long.MAX_VALUE;
            }

            public void setAccessTime(long time) {
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return this.nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return this.previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }
        }

        AccessQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> entry) {
            LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
            LocalCache.connectAccessOrder(entry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            return next == this.head ? null : next;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            if (next == this.head) {
                return null;
            }
            remove(next);
            return next;
        }

        public boolean remove(Object o) {
            ReferenceEntry<K, V> e = (ReferenceEntry) o;
            ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
            ReferenceEntry<K, V> next = e.getNextInAccessQueue();
            LocalCache.connectAccessOrder(previous, next);
            LocalCache.nullifyAccessOrder(e);
            return next != NullEntry.INSTANCE;
        }

        public boolean contains(Object o) {
            return ((ReferenceEntry) o).getNextInAccessQueue() != NullEntry.INSTANCE;
        }

        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }

        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInAccessQueue(); e != this.head; e = e.getNextInAccessQueue()) {
                size++;
            }
            return size;
        }

        public void clear() {
            ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
            while (e != this.head) {
                ReferenceEntry<K, V> next = e.getNextInAccessQueue();
                LocalCache.nullifyAccessOrder(e);
                e = next;
            }
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
                    return next == AccessQueue.this.head ? null : next;
                }
            };
        }
    }

    enum EntryFactory {
        STRONG {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongEntry(key, hash, next);
            }
        },
        STRONG_ACCESS {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongAccessEntry(key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyAccessEntry(original, newEntry);
                return newEntry;
            }
        },
        STRONG_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongWriteEntry(key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyWriteEntry(original, newEntry);
                return newEntry;
            }
        },
        STRONG_ACCESS_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongAccessWriteEntry(key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyAccessEntry(original, newEntry);
                copyWriteEntry(original, newEntry);
                return newEntry;
            }
        },
        WEAK {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakEntry(segment.keyReferenceQueue, key, hash, next);
            }
        },
        WEAK_ACCESS {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakAccessEntry(segment.keyReferenceQueue, key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyAccessEntry(original, newEntry);
                return newEntry;
            }
        },
        WEAK_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakWriteEntry(segment.keyReferenceQueue, key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyWriteEntry(original, newEntry);
                return newEntry;
            }
        },
        WEAK_ACCESS_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakAccessWriteEntry(segment.keyReferenceQueue, key, hash, next);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                copyAccessEntry(original, newEntry);
                copyWriteEntry(original, newEntry);
                return newEntry;
            }
        };
        
        static final int ACCESS_MASK = 1;
        static final int WEAK_MASK = 4;
        static final int WRITE_MASK = 2;
        static final EntryFactory[] factories = null;

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry);

        static {
            factories = new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
        }

        static EntryFactory getFactory(Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
            int i;
            int i2 = 0;
            if (keyStrength == Strength.WEAK) {
                i = 4;
            } else {
                i = 0;
            }
            int i3 = (usesAccessQueue ? 1 : 0) | i;
            if (usesWriteQueue) {
                i2 = 2;
            }
            return factories[i3 | i2];
        }

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            return newEntry(segment, original.getKey(), original.getHash(), newNext);
        }

        <K, V> void copyAccessEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            newEntry.setAccessTime(original.getAccessTime());
            LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
            LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(original);
        }

        <K, V> void copyWriteEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            newEntry.setWriteTime(original.getWriteTime());
            LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
            LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(original);
        }
    }

    abstract class HashIterator<T> implements Iterator<T> {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public abstract T next();

        HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (!nextInChain() && !nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    Segment[] segmentArr = LocalCache.this.segments;
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
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i);
                this.nextEntry = referenceEntry;
                if (referenceEntry != null && (advanceTo(this.nextEntry) || nextInChain())) {
                    return true;
                }
            }
            return false;
        }

        boolean advanceTo(ReferenceEntry<K, V> entry) {
            try {
                long now = LocalCache.this.ticker.read();
                K key = entry.getKey();
                V value = LocalCache.this.getLiveValue(entry, now);
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
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
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

    final class EntrySet extends AbstractCacheSet<Entry<K, V>> {
        EntrySet(ConcurrentMap<?, ?> map) {
            super(map);
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
            V v = LocalCache.this.get(key);
            if (v == null || !LocalCache.this.valueEquivalence.equivalent(e.getValue(), v)) {
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
            if (key == null || !LocalCache.this.remove(key, e.getValue())) {
                return false;
            }
            return true;
        }
    }

    final class KeyIterator extends HashIterator<K> {
        KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    final class KeySet extends AbstractCacheSet<K> {
        KeySet(ConcurrentMap<?, ?> map) {
            super(map);
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public boolean contains(Object o) {
            return this.map.containsKey(o);
        }

        public boolean remove(Object o) {
            return this.map.remove(o) != null;
        }
    }

    static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        final int concurrencyLevel;
        transient Cache<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final CacheLoader<? super K, V> loader;
        final long maxWeight;
        final RemovalListener<? super K, ? super V> removalListener;
        final Ticker ticker;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;
        final Weigher<K, V> weigher;

        ManualSerializationProxy(LocalCache<K, V> cache) {
            this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
        }

        private ManualSerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.expireAfterWriteNanos = expireAfterWriteNanos;
            this.expireAfterAccessNanos = expireAfterAccessNanos;
            this.maxWeight = maxWeight;
            this.weigher = weigher;
            this.concurrencyLevel = concurrencyLevel;
            this.removalListener = removalListener;
            if (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) {
                ticker = null;
            }
            this.ticker = ticker;
            this.loader = loader;
        }

        CacheBuilder<K, V> recreateCacheBuilder() {
            CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
            builder.strictParsing = false;
            if (this.expireAfterWriteNanos > 0) {
                builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0) {
                builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.weigher != OneWeigher.INSTANCE) {
                builder.weigher(this.weigher);
                if (this.maxWeight != -1) {
                    builder.maximumWeight(this.maxWeight);
                }
            } else if (this.maxWeight != -1) {
                builder.maximumSize(this.maxWeight);
            }
            if (this.ticker != null) {
                builder.ticker(this.ticker);
            }
            return builder;
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.delegate = recreateCacheBuilder().build();
        }

        private Object readResolve() {
            return this.delegate;
        }

        protected Cache<K, V> delegate() {
            return this.delegate;
        }
    }

    static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        transient LoadingCache<K, V> autoDelegate;

        LoadingSerializationProxy(LocalCache<K, V> cache) {
            super(cache);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.autoDelegate = recreateCacheBuilder().build(this.loader);
        }

        public V get(K key) throws ExecutionException {
            return this.autoDelegate.get(key);
        }

        public V getUnchecked(K key) {
            return this.autoDelegate.getUnchecked(key);
        }

        public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
            return this.autoDelegate.getAll(keys);
        }

        public final V apply(K key) {
            return this.autoDelegate.apply(key);
        }

        public void refresh(K key) {
            this.autoDelegate.refresh(key);
        }

        private Object readResolve() {
            return this.autoDelegate;
        }
    }

    static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;

        class C04501 implements Function<V, V> {
            C04501() {
            }

            public V apply(V newValue) {
                LoadingValueReference.this.set(newValue);
                return newValue;
            }
        }

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        public LoadingValueReference(ValueReference<K, V> oldValue) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = oldValue;
        }

        public boolean isLoading() {
            return true;
        }

        public boolean isActive() {
            return this.oldValue.isActive();
        }

        public int getWeight() {
            return this.oldValue.getWeight();
        }

        public boolean set(@Nullable V newValue) {
            return this.futureValue.set(newValue);
        }

        public boolean setException(Throwable t) {
            return this.futureValue.setException(t);
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable t) {
            return Futures.immediateFailedFuture(t);
        }

        public void notifyNewValue(@Nullable V newValue) {
            if (newValue != null) {
                set(newValue);
            } else {
                this.oldValue = LocalCache.unset();
            }
        }

        public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
            try {
                this.stopwatch.start();
                V previousValue = this.oldValue.get();
                if (previousValue == null) {
                    V newValue = loader.load(key);
                    if (set(newValue)) {
                        return this.futureValue;
                    }
                    return Futures.immediateFuture(newValue);
                }
                ListenableFuture<V> newValue2 = loader.reload(key, previousValue);
                if (newValue2 == null) {
                    return Futures.immediateFuture(null);
                }
                return Futures.transform(newValue2, new C04501());
            } catch (Throwable t) {
                ListenableFuture<V> result = setException(t) ? this.futureValue : fullyFailedFuture(t);
                if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return result;
            }
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        public V waitForValue() throws ExecutionException {
            return Uninterruptibles.getUninterruptibly(this.futureValue);
        }

        public V get() {
            return this.oldValue.get();
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }

        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @Nullable V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
    }

    static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        final LocalCache<K, V> localCache;

        LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
            this(new LocalCache(builder, null));
        }

        private LocalManualCache(LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }

        @Nullable
        public V getIfPresent(Object key) {
            return this.localCache.getIfPresent(key);
        }

        public V get(K key, final Callable<? extends V> valueLoader) throws ExecutionException {
            Preconditions.checkNotNull(valueLoader);
            return this.localCache.get(key, new CacheLoader<Object, V>() {
                public V load(Object key) throws Exception {
                    return valueLoader.call();
                }
            });
        }

        public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
            return this.localCache.getAllPresent(keys);
        }

        public void put(K key, V value) {
            this.localCache.put(key, value);
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            this.localCache.putAll(m);
        }

        public void invalidate(Object key) {
            Preconditions.checkNotNull(key);
            this.localCache.remove(key);
        }

        public void invalidateAll(Iterable<?> keys) {
            this.localCache.invalidateAll(keys);
        }

        public void invalidateAll() {
            this.localCache.clear();
        }

        public long size() {
            return this.localCache.longSize();
        }

        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        public CacheStats stats() {
            SimpleStatsCounter aggregator = new SimpleStatsCounter();
            aggregator.incrementBy(this.localCache.globalStatsCounter);
            for (Segment<K, V> segment : this.localCache.segments) {
                aggregator.incrementBy(segment.statsCounter);
            }
            return aggregator.snapshot();
        }

        public void cleanUp() {
            this.localCache.cleanUp();
        }

        Object writeReplace() {
            return new ManualSerializationProxy(this.localCache);
        }
    }

    static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {
        private static final long serialVersionUID = 1;

        LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
            super();
        }

        public V get(K key) throws ExecutionException {
            return this.localCache.getOrLoad(key);
        }

        public V getUnchecked(K key) {
            try {
                return get(key);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e.getCause());
            }
        }

        public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
            return this.localCache.getAll(keys);
        }

        public void refresh(K key) {
            this.localCache.refresh(key);
        }

        public final V apply(K key) {
            return getUnchecked(key);
        }

        Object writeReplace() {
            return new LoadingSerializationProxy(this.localCache);
        }
    }

    private enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;

        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        public int getHash() {
            return 0;
        }

        public Object getKey() {
            return null;
        }

        public long getAccessTime() {
            return 0;
        }

        public void setAccessTime(long time) {
        }

        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        public void setNextInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public long getWriteTime() {
            return 0;
        }

        public void setWriteTime(long time) {
        }

        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        public void setNextInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }
    }

    static class Segment<K, V> extends ReentrantLock {
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;
        final ReferenceQueue<K> keyReferenceQueue;
        @Weak
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final StatsCounter statsCounter;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        @GuardedBy("this")
        long totalWeight;
        final ReferenceQueue<V> valueReferenceQueue;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> writeQueue;

        Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, StatsCounter statsCounter) {
            ReferenceQueue referenceQueue = null;
            this.map = map;
            this.maxSegmentWeight = maxSegmentWeight;
            this.statsCounter = (StatsCounter) Preconditions.checkNotNull(statsCounter);
            initTable(newEntryArray(initialCapacity));
            this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue() : null;
            if (map.usesValueReferences()) {
                referenceQueue = new ReferenceQueue();
            }
            this.valueReferenceQueue = referenceQueue;
            this.recencyQueue = map.usesAccessQueue() ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue();
            this.writeQueue = map.usesWriteQueue() ? new WriteQueue() : LocalCache.discardingQueue();
            this.accessQueue = map.usesAccessQueue() ? new AccessQueue() : LocalCache.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
            return new AtomicReferenceArray(size);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
            this.threshold = (newTable.length() * 3) / 4;
            if (!this.map.customWeigher() && ((long) this.threshold) == this.maxSegmentWeight) {
                this.threshold++;
            }
            this.table = newTable;
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(key), hash, next);
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            if (original.getKey() == null) {
                return null;
            }
            ValueReference<K, V> valueReference = original.getValueReference();
            V value = valueReference.get();
            if (value == null && valueReference.isActive()) {
                return null;
            }
            ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
            newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
            return newEntry;
        }

        @GuardedBy("this")
        void setValue(ReferenceEntry<K, V> entry, K key, V value, long now) {
            ValueReference<K, V> previous = entry.getValueReference();
            int weight = this.map.weigher.weigh(key, value);
            Preconditions.checkState(weight >= 0, "Weights must be non-negative");
            entry.setValueReference(this.map.valueStrength.referenceValue(this, entry, value, weight));
            recordWrite(entry, weight, now);
            previous.notifyNewValue(value);
        }

        V get(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(loader);
            try {
                V scheduleRefresh;
                if (this.count != 0) {
                    ReferenceEntry<K, V> e = getEntry(key, hash);
                    if (e != null) {
                        long now = this.map.ticker.read();
                        V value = getLiveValue(e, now);
                        if (value != null) {
                            recordRead(e, now);
                            this.statsCounter.recordHits(1);
                            scheduleRefresh = scheduleRefresh(e, key, hash, value, now, loader);
                            postReadCleanup();
                        } else {
                            ValueReference<K, V> valueReference = e.getValueReference();
                            if (valueReference.isLoading()) {
                                scheduleRefresh = waitForLoadingValue(e, key, valueReference);
                                postReadCleanup();
                            }
                        }
                        return scheduleRefresh;
                    }
                }
                scheduleRefresh = lockedGetOrLoad(key, hash, loader);
                postReadCleanup();
                return scheduleRefresh;
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof Error) {
                    throw new ExecutionError((Error) cause);
                } else if (cause instanceof RuntimeException) {
                    throw new UncheckedExecutionException(cause);
                } else {
                    throw ee;
                }
            } catch (Throwable th) {
                postReadCleanup();
            }
        }

        V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
            Throwable th;
            ValueReference<K, V> valueReference = null;
            LoadingValueReference<K, V> loadingValueReference = null;
            boolean createNewEntry = true;
            lock();
            try {
                V v;
                LoadingValueReference<K, V> loadingValueReference2;
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                ReferenceEntry<K, V> e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        valueReference = e.getValueReference();
                        if (valueReference.isLoading()) {
                            createNewEntry = false;
                        } else {
                            v = valueReference.get();
                            if (v == null) {
                                enqueueNotification(entryKey, hash, v, valueReference.getWeight(), RemovalCause.COLLECTED);
                            } else if (this.map.isExpired(e, now)) {
                                enqueueNotification(entryKey, hash, v, valueReference.getWeight(), RemovalCause.EXPIRED);
                            } else {
                                recordLockedRead(e, now);
                                this.statsCounter.recordHits(1);
                                unlock();
                                postWriteCleanup();
                                return v;
                            }
                            this.writeQueue.remove(e);
                            this.accessQueue.remove(e);
                            this.count = newCount;
                        }
                        if (createNewEntry) {
                            loadingValueReference2 = new LoadingValueReference();
                            if (e != null) {
                                try {
                                    e = newEntry(key, hash, first);
                                    e.setValueReference(loadingValueReference2);
                                    table.set(index, e);
                                    loadingValueReference = loadingValueReference2;
                                } catch (Throwable th2) {
                                    th = th2;
                                    loadingValueReference = loadingValueReference2;
                                    unlock();
                                    postWriteCleanup();
                                    throw th;
                                }
                            }
                            e.setValueReference(loadingValueReference2);
                            loadingValueReference = loadingValueReference2;
                        }
                        unlock();
                        postWriteCleanup();
                        if (createNewEntry) {
                            return waitForLoadingValue(e, key, valueReference);
                        }
                        try {
                            synchronized (e) {
                                v = loadSync(key, hash, loadingValueReference, loader);
                            }
                            return v;
                        } finally {
                            this.statsCounter.recordMisses(1);
                        }
                    } else {
                        e = e.getNext();
                    }
                }
                if (createNewEntry) {
                    loadingValueReference2 = new LoadingValueReference();
                    if (e != null) {
                        e.setValueReference(loadingValueReference2);
                        loadingValueReference = loadingValueReference2;
                    } else {
                        e = newEntry(key, hash, first);
                        e.setValueReference(loadingValueReference2);
                        table.set(index, e);
                        loadingValueReference = loadingValueReference2;
                    }
                }
                unlock();
                postWriteCleanup();
                if (createNewEntry) {
                    return waitForLoadingValue(e, key, valueReference);
                }
                synchronized (e) {
                    v = loadSync(key, hash, loadingValueReference, loader);
                }
                return v;
            } catch (Throwable th3) {
                th = th3;
            }
        }

        V waitForLoadingValue(ReferenceEntry<K, V> e, K key, ValueReference<K, V> valueReference) throws ExecutionException {
            if (valueReference.isLoading()) {
                Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", (Object) key);
                try {
                    V value = valueReference.waitForValue();
                    if (value == null) {
                        throw new InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
                    }
                    recordRead(e, this.map.ticker.read());
                    return value;
                } finally {
                    this.statsCounter.recordMisses(1);
                }
            } else {
                throw new AssertionError();
            }
        }

        V loadSync(K key, int hash, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) throws ExecutionException {
            return getAndRecordStats(key, hash, loadingValueReference, loadingValueReference.loadFuture(key, loader));
        }

        ListenableFuture<V> loadAsync(K key, int hash, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) {
            final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
            final K k = key;
            final int i = hash;
            final LoadingValueReference<K, V> loadingValueReference2 = loadingValueReference;
            loadingFuture.addListener(new Runnable() {
                public void run() {
                    try {
                        Segment.this.getAndRecordStats(k, i, loadingValueReference2, loadingFuture);
                    } catch (Throwable t) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
                        loadingValueReference2.setException(t);
                    }
                }
            }, MoreExecutors.directExecutor());
            return loadingFuture;
        }

        V getAndRecordStats(K key, int hash, LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue) throws ExecutionException {
            V v = null;
            try {
                v = Uninterruptibles.getUninterruptibly(newValue);
                if (v == null) {
                    throw new InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
                }
                this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                storeLoadedValue(key, hash, loadingValueReference, v);
                return v;
            } finally {
                if (v == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    removeLoadingValue(key, hash, loadingValueReference);
                }
            }
        }

        V scheduleRefresh(ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader) {
            if (this.map.refreshes() && now - entry.getWriteTime() > this.map.refreshNanos && !entry.getValueReference().isLoading()) {
                V newValue = refresh(key, hash, loader, true);
                if (newValue != null) {
                    return newValue;
                }
            }
            return oldValue;
        }

        @Nullable
        V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
            V v = null;
            LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
            if (loadingValueReference != null) {
                ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
                if (result.isDone()) {
                    try {
                        v = Uninterruptibles.getUninterruptibly(result);
                    } catch (Throwable th) {
                    }
                }
            }
            return v;
        }

        @Nullable
        LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime) {
            LoadingValueReference<K, V> loadingValueReference;
            lock();
            long now = this.map.ticker.read();
            preWriteCleanup(now);
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
            ReferenceEntry<K, V> e = first;
            while (e != null) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                    ValueReference<K, V> valueReference = e.getValueReference();
                    if (valueReference.isLoading() || (checkTime && now - e.getWriteTime() < this.map.refreshNanos)) {
                        unlock();
                        postWriteCleanup();
                        return null;
                    }
                    this.modCount++;
                    loadingValueReference = new LoadingValueReference(valueReference);
                    e.setValueReference(loadingValueReference);
                    unlock();
                    postWriteCleanup();
                    return loadingValueReference;
                }
                try {
                    e = e.getNext();
                } catch (Throwable th) {
                    unlock();
                    postWriteCleanup();
                }
            }
            this.modCount++;
            loadingValueReference = new LoadingValueReference();
            e = newEntry(key, hash, first);
            e.setValueReference(loadingValueReference);
            table.set(index, e);
            unlock();
            postWriteCleanup();
            return loadingValueReference;
        }

        void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                drainValueReferenceQueue();
            }
        }

        @GuardedBy("this")
        void drainKeyReferenceQueue() {
            int i = 0;
            do {
                Reference<? extends K> ref = this.keyReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimKey((ReferenceEntry) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        @GuardedBy("this")
        void drainValueReferenceQueue() {
            int i = 0;
            do {
                Reference<? extends V> ref = this.valueReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimValue((ValueReference) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                clearValueReferenceQueue();
            }
        }

        void clearKeyReferenceQueue() {
            do {
            } while (this.keyReferenceQueue.poll() != null);
        }

        void clearValueReferenceQueue() {
            do {
            } while (this.valueReferenceQueue.poll() != null);
        }

        void recordRead(ReferenceEntry<K, V> entry, long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.recencyQueue.add(entry);
        }

        @GuardedBy("this")
        void recordLockedRead(ReferenceEntry<K, V> entry, long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.accessQueue.add(entry);
        }

        @GuardedBy("this")
        void recordWrite(ReferenceEntry<K, V> entry, int weight, long now) {
            drainRecencyQueue();
            this.totalWeight += (long) weight;
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            if (this.map.recordsWrite()) {
                entry.setWriteTime(now);
            }
            this.accessQueue.add(entry);
            this.writeQueue.add(entry);
        }

        @GuardedBy("this")
        void drainRecencyQueue() {
            while (true) {
                ReferenceEntry<K, V> e = (ReferenceEntry) this.recencyQueue.poll();
                if (e == null) {
                    return;
                }
                if (this.accessQueue.contains(e)) {
                    this.accessQueue.add(e);
                }
            }
        }

        void tryExpireEntries(long now) {
            if (tryLock()) {
                try {
                    expireEntries(now);
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void expireEntries(long now) {
            drainRecencyQueue();
            ReferenceEntry<K, V> e;
            do {
                e = (ReferenceEntry) this.writeQueue.peek();
                if (e == null || !this.map.isExpired(e, now)) {
                    do {
                        e = (ReferenceEntry) this.accessQueue.peek();
                        if (e == null || !this.map.isExpired(e, now)) {
                            return;
                        }
                    } while (removeEntry(e, e.getHash(), RemovalCause.EXPIRED));
                    throw new AssertionError();
                }
            } while (removeEntry(e, e.getHash(), RemovalCause.EXPIRED));
            throw new AssertionError();
        }

        @GuardedBy("this")
        void enqueueNotification(@Nullable K key, int hash, @Nullable V value, int weight, RemovalCause cause) {
            this.totalWeight -= (long) weight;
            if (cause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(RemovalNotification.create(key, value, cause));
            }
        }

        @GuardedBy("this")
        void evictEntries(ReferenceEntry<K, V> newest) {
            if (this.map.evictsBySize()) {
                drainRecencyQueue();
                if (((long) newest.getValueReference().getWeight()) <= this.maxSegmentWeight || removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
                    while (this.totalWeight > this.maxSegmentWeight) {
                        ReferenceEntry<K, V> e = getNextEvictable();
                        if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
                            throw new AssertionError();
                        }
                    }
                    return;
                }
                throw new AssertionError();
            }
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> getNextEvictable() {
            for (ReferenceEntry<K, V> e : this.accessQueue) {
                if (e.getValueReference().getWeight() > 0) {
                    return e;
                }
            }
            throw new AssertionError();
        }

        ReferenceEntry<K, V> getFirst(int hash) {
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return (ReferenceEntry) table.get((table.length() - 1) & hash);
        }

        @Nullable
        ReferenceEntry<K, V> getEntry(Object key, int hash) {
            for (ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
                if (e.getHash() == hash) {
                    K entryKey = e.getKey();
                    if (entryKey == null) {
                        tryDrainReferenceQueues();
                    } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                        return e;
                    }
                }
            }
            return null;
        }

        @Nullable
        ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
            ReferenceEntry<K, V> e = getEntry(key, hash);
            if (e == null) {
                return null;
            }
            if (!this.map.isExpired(e, now)) {
                return e;
            }
            tryExpireEntries(now);
            return null;
        }

        V getLiveValue(ReferenceEntry<K, V> entry, long now) {
            if (entry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V value = entry.getValueReference().get();
            if (value == null) {
                tryDrainReferenceQueues();
                return null;
            } else if (!this.map.isExpired(entry, now)) {
                return value;
            } else {
                tryExpireEntries(now);
                return null;
            }
        }

        @Nullable
        V get(Object key, int hash) {
            V v = null;
            try {
                if (this.count != 0) {
                    long now = this.map.ticker.read();
                    ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
                    if (e != null) {
                        V value = e.getValueReference().get();
                        if (value != null) {
                            recordRead(e, now);
                            v = scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
                            postReadCleanup();
                        } else {
                            tryDrainReferenceQueues();
                        }
                    }
                    return v;
                }
                postReadCleanup();
                return v;
            } finally {
                postReadCleanup();
            }
        }

        boolean containsKey(Object key, int hash) {
            boolean z = false;
            try {
                if (this.count != 0) {
                    ReferenceEntry<K, V> e = getLiveEntry(key, hash, this.map.ticker.read());
                    if (e != null) {
                        if (e.getValueReference().get() != null) {
                            z = true;
                        }
                        postReadCleanup();
                    }
                } else {
                    postReadCleanup();
                }
                return z;
            } finally {
                postReadCleanup();
            }
        }

        @VisibleForTesting
        boolean containsValue(Object value) {
            try {
                if (this.count != 0) {
                    long now = this.map.ticker.read();
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int length = table.length();
                    for (int i = 0; i < length; i++) {
                        for (ReferenceEntry<K, V> e = (ReferenceEntry) table.get(i); e != null; e = e.getNext()) {
                            V entryValue = getLiveValue(e, now);
                            if (entryValue != null && this.map.valueEquivalence.equivalent(value, entryValue)) {
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

        @Nullable
        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            lock();
            try {
                int newCount;
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                if (this.count + 1 > this.threshold) {
                    expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V v = valueReference.get();
                        if (v == null) {
                            this.modCount++;
                            if (valueReference.isActive()) {
                                enqueueNotification(key, hash, v, valueReference.getWeight(), RemovalCause.COLLECTED);
                                setValue(e, key, value, now);
                                newCount = this.count;
                            } else {
                                setValue(e, key, value, now);
                                newCount = this.count + 1;
                            }
                            this.count = newCount;
                            evictEntries(e);
                            v = null;
                            return v;
                        } else if (onlyIfAbsent) {
                            recordLockedRead(e, now);
                            unlock();
                            postWriteCleanup();
                            return v;
                        } else {
                            this.modCount++;
                            enqueueNotification(key, hash, v, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(e, key, value, now);
                            evictEntries(e);
                            unlock();
                            postWriteCleanup();
                            return v;
                        }
                    }
                }
                this.modCount++;
                ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                setValue(newEntry, key, value, now);
                table.set(index, newEntry);
                this.count++;
                evictEntries(newEntry);
                unlock();
                postWriteCleanup();
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        @GuardedBy("this")
        void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
            int oldCapacity = oldTable.length();
            if (oldCapacity < 1073741824) {
                int newCount = this.count;
                AtomicReferenceArray<ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
                this.threshold = (newTable.length() * 3) / 4;
                int newMask = newTable.length() - 1;
                for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
                    ReferenceEntry<K, V> head = (ReferenceEntry) oldTable.get(oldIndex);
                    if (head != null) {
                        ReferenceEntry<K, V> next = head.getNext();
                        int headIndex = head.getHash() & newMask;
                        if (next == null) {
                            newTable.set(headIndex, head);
                        } else {
                            ReferenceEntry<K, V> e;
                            int newIndex;
                            ReferenceEntry<K, V> tail = head;
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
                                ReferenceEntry<K, V> newFirst = copyEntry(e, (ReferenceEntry) newTable.get(newIndex));
                                if (newFirst != null) {
                                    newTable.set(newIndex, newFirst);
                                } else {
                                    removeCollectedEntry(e);
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
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int newCount = this.count - 1;
                                this.modCount++;
                                newCount = this.count - 1;
                                table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED));
                                this.count = newCount;
                            }
                            unlock();
                            postWriteCleanup();
                            return false;
                        } else if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
                            this.modCount++;
                            enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(e, key, newValue, now);
                            evictEntries(e);
                            unlock();
                            postWriteCleanup();
                            return true;
                        } else {
                            recordLockedRead(e, now);
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        @Nullable
        V replace(K key, int hash, V newValue) {
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int newCount = this.count - 1;
                                this.modCount++;
                                newCount = this.count - 1;
                                table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED));
                                this.count = newCount;
                            }
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        this.modCount++;
                        enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                        setValue(e, key, newValue, now);
                        evictEntries(e);
                        unlock();
                        postWriteCleanup();
                        return entryValue;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        @Nullable
        V remove(Object key, int hash) {
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        RemovalCause cause;
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue != null) {
                            cause = RemovalCause.EXPLICIT;
                        } else if (valueReference.isActive()) {
                            cause = RemovalCause.COLLECTED;
                        } else {
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause));
                        this.count = newCount;
                        return entryValue;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean storeLoadedValue(K key, int hash, LoadingValueReference<K, V> oldValueReference, V newValue) {
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (oldValueReference == valueReference || (entryValue == null && valueReference != LocalCache.UNSET)) {
                            this.modCount++;
                            if (oldValueReference.isActive()) {
                                enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), entryValue == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED);
                                newCount--;
                            }
                            setValue(e, key, newValue, now);
                            this.count = newCount;
                            evictEntries(e);
                            return true;
                        }
                        enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
                        unlock();
                        postWriteCleanup();
                        return false;
                    }
                }
                this.modCount++;
                ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                setValue(newEntry, key, newValue, now);
                table.set(index, newEntry);
                this.count = newCount;
                evictEntries(newEntry);
                unlock();
                postWriteCleanup();
                return true;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean remove(Object key, int hash, Object value) {
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        RemovalCause cause;
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                            cause = RemovalCause.EXPLICIT;
                        } else {
                            if (entryValue == null) {
                                if (valueReference.isActive()) {
                                    cause = RemovalCause.COLLECTED;
                                }
                            }
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause));
                        this.count = newCount;
                        boolean z = cause == RemovalCause.EXPLICIT;
                        unlock();
                        postWriteCleanup();
                        return z;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        void clear() {
            if (this.count != 0) {
                lock();
                try {
                    int i;
                    preWriteCleanup(this.map.ticker.read());
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    for (i = 0; i < table.length(); i++) {
                        for (ReferenceEntry<K, V> e = (ReferenceEntry) table.get(i); e != null; e = e.getNext()) {
                            if (e.getValueReference().isActive()) {
                                K key = e.getKey();
                                V value = e.getValueReference().get();
                                RemovalCause cause = (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
                                enqueueNotification(key, e.getHash(), value, e.getValueReference().getWeight(), cause);
                            }
                        }
                    }
                    for (i = 0; i < table.length(); i++) {
                        table.set(i, null);
                    }
                    clearReferenceQueues();
                    this.writeQueue.clear();
                    this.accessQueue.clear();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }
        }

        @GuardedBy("this")
        @Nullable
        ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry, @Nullable K key, int hash, V value, ValueReference<K, V> valueReference, RemovalCause cause) {
            enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
            if (!valueReference.isLoading()) {
                return removeEntryFromChain(first, entry);
            }
            valueReference.notifyNewValue(null);
            return first;
        }

        @GuardedBy("this")
        @Nullable
        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
            int newCount = this.count;
            ReferenceEntry<K, V> newFirst = entry.getNext();
            for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
                ReferenceEntry<K, V> next = copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                } else {
                    removeCollectedEntry(e);
                    newCount--;
                }
            }
            this.count = newCount;
            return newFirst;
        }

        @GuardedBy("this")
        void removeCollectedEntry(ReferenceEntry<K, V> entry) {
            enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), entry.getValueReference().getWeight(), RemovalCause.COLLECTED);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
        }

        boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
            lock();
            try {
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    if (e == entry) {
                        this.modCount++;
                        newCount = this.count - 1;
                        table.set(index, removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), RemovalCause.COLLECTED));
                        this.count = newCount;
                        return true;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean reclaimValue(K key, int hash, ValueReference<K, V> valueReference) {
            lock();
            try {
                boolean z;
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        if (e.getValueReference() == valueReference) {
                            this.modCount++;
                            newCount = this.count - 1;
                            table.set(index, removeValueFromChain(first, e, entryKey, hash, valueReference.get(), valueReference, RemovalCause.COLLECTED));
                            this.count = newCount;
                            z = true;
                        } else {
                            z = false;
                            unlock();
                            if (!isHeldByCurrentThread()) {
                                postWriteCleanup();
                            }
                        }
                        return z;
                    }
                }
                z = false;
                unlock();
                if (!isHeldByCurrentThread()) {
                    postWriteCleanup();
                }
                return z;
            } finally {
                unlock();
                if (!isHeldByCurrentThread()) {
                    postWriteCleanup();
                }
            }
        }

        boolean removeLoadingValue(K key, int hash, LoadingValueReference<K, V> valueReference) {
            lock();
            try {
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
                ReferenceEntry<K, V> e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) {
                        e = e.getNext();
                    } else if (e.getValueReference() == valueReference) {
                        if (valueReference.isActive()) {
                            e.setValueReference(valueReference.getOldValue());
                        } else {
                            table.set(index, removeEntryFromChain(first, e));
                        }
                        unlock();
                        postWriteCleanup();
                        return true;
                    } else {
                        unlock();
                        postWriteCleanup();
                        return false;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        @GuardedBy("this")
        @VisibleForTesting
        boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
            int newCount = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = (ReferenceEntry) table.get(index);
            for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                if (e == entry) {
                    this.modCount++;
                    newCount = this.count - 1;
                    table.set(index, removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), cause));
                    this.count = newCount;
                    return true;
                }
            }
            return false;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                cleanUp();
            }
        }

        @GuardedBy("this")
        void preWriteCleanup(long now) {
            runLockedCleanup(now);
        }

        void postWriteCleanup() {
            runUnlockedCleanup();
        }

        void cleanUp() {
            runLockedCleanup(this.map.ticker.read());
            runUnlockedCleanup();
        }

        void runLockedCleanup(long now) {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                    expireEntries(now);
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        void runUnlockedCleanup() {
            if (!isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }

    static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        public int getWeight() {
            return 1;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public void notifyNewValue(V v) {
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new SoftValueReference(queue, value, entry);
        }

        public boolean isLoading() {
            return false;
        }

        public boolean isActive() {
            return true;
        }

        public V waitForValue() {
            return get();
        }
    }

    enum Strength {
        STRONG {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V value, int weight) {
                return weight == 1 ? new StrongValueReference(value) : new WeightedStrongValueReference(value, weight);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        SOFT {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight) {
                return weight == 1 ? new SoftValueReference(segment.valueReferenceQueue, value, entry) : new WeightedSoftValueReference(segment.valueReferenceQueue, value, entry, weight);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        },
        WEAK {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight) {
                return weight == 1 ? new WeakValueReference(segment.valueReferenceQueue, value, entry) : new WeightedWeakValueReference(segment.valueReferenceQueue, value, entry, weight);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        abstract Equivalence<Object> defaultEquivalence();

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i);
    }

    static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        StrongEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return this.key;
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        StrongAccessEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }

    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        StrongAccessWriteEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V referent) {
            this.referent = referent;
        }

        public V get() {
            return this.referent;
        }

        public int getWeight() {
            return 1;
        }

        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        public boolean isLoading() {
            return false;
        }

        public boolean isActive() {
            return true;
        }

        public V waitForValue() {
            return get();
        }

        public void notifyNewValue(V v) {
        }
    }

    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        StrongWriteEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
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
        private final ConcurrentMap<?, ?> map;

        Values(ConcurrentMap<?, ?> map) {
            this.map = map;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void clear() {
            this.map.clear();
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public boolean contains(Object o) {
            return this.map.containsValue(o);
        }

        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        public <E> E[] toArray(E[] a) {
            return LocalCache.toArrayList(this).toArray(a);
        }
    }

    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return get();
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long time) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setWriteTime(long time) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }

    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        public int getWeight() {
            return 1;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public void notifyNewValue(V v) {
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeakValueReference(queue, value, entry);
        }

        public boolean isLoading() {
            return false;
        }

        public boolean isActive() {
            return true;
        }

        public V waitForValue() {
            return get();
        }
    }

    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;

        WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeightedSoftValueReference(queue, value, entry, this.weight);
        }
    }

    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;

        WeightedStrongValueReference(V referent, int weight) {
            super(referent);
            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }
    }

    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;

        WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeightedWeakValueReference(queue, value, entry, this.weight);
        }
    }

    static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C04561();

        class C04561 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextWrite = this;
            ReferenceEntry<K, V> previousWrite = this;

            C04561() {
            }

            public long getWriteTime() {
                return Long.MAX_VALUE;
            }

            public void setWriteTime(long time) {
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return this.nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return this.previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }
        }

        WriteQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> entry) {
            LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
            LocalCache.connectWriteOrder(entry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            return next == this.head ? null : next;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            if (next == this.head) {
                return null;
            }
            remove(next);
            return next;
        }

        public boolean remove(Object o) {
            ReferenceEntry<K, V> e = (ReferenceEntry) o;
            ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
            ReferenceEntry<K, V> next = e.getNextInWriteQueue();
            LocalCache.connectWriteOrder(previous, next);
            LocalCache.nullifyWriteOrder(e);
            return next != NullEntry.INSTANCE;
        }

        public boolean contains(Object o) {
            return ((ReferenceEntry) o).getNextInWriteQueue() != NullEntry.INSTANCE;
        }

        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }

        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInWriteQueue(); e != this.head; e = e.getNextInWriteQueue()) {
                size++;
            }
            return size;
        }

        public void clear() {
            ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
            while (e != this.head) {
                ReferenceEntry<K, V> next = e.getNextInWriteQueue();
                LocalCache.nullifyWriteOrder(e);
                e = next;
            }
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
                    return next == WriteQueue.this.head ? null : next;
                }
            };
        }
    }

    final class WriteThroughEntry implements Entry<K, V> {
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
            V oldValue = LocalCache.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    LocalCache(CacheBuilder<? super K, ? super V> builder, @Nullable CacheLoader<? super K, V> loader) {
        Queue discardingQueue;
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyStrength = builder.getKeyStrength();
        this.valueStrength = builder.getValueStrength();
        this.keyEquivalence = builder.getKeyEquivalence();
        this.valueEquivalence = builder.getValueEquivalence();
        this.maxWeight = builder.getMaximumWeight();
        this.weigher = builder.getWeigher();
        this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
        this.refreshNanos = builder.getRefreshNanos();
        this.removalListener = builder.getRemovalListener();
        if (this.removalListener == NullListener.INSTANCE) {
            discardingQueue = discardingQueue();
        } else {
            discardingQueue = new ConcurrentLinkedQueue();
        }
        this.removalNotificationQueue = discardingQueue;
        this.ticker = builder.getTicker(recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
        this.globalStatsCounter = (StatsCounter) builder.getStatsCounterSupplier().get();
        this.defaultLoader = loader;
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        if (evictsBySize() && !customWeigher()) {
            initialCapacity = Math.min(initialCapacity, (int) this.maxWeight);
        }
        int segmentShift = 0;
        int segmentCount = 1;
        while (segmentCount < this.concurrencyLevel && (!evictsBySize() || ((long) (segmentCount * 20)) <= this.maxWeight)) {
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
        int i;
        if (evictsBySize()) {
            long maxSegmentWeight = (this.maxWeight / ((long) segmentCount)) + 1;
            long remainder = this.maxWeight % ((long) segmentCount);
            for (i = 0; i < this.segments.length; i++) {
                if (((long) i) == remainder) {
                    maxSegmentWeight--;
                }
                this.segments[i] = createSegment(segmentSize, maxSegmentWeight, (StatsCounter) builder.getStatsCounterSupplier().get());
            }
            return;
        }
        for (i = 0; i < this.segments.length; i++) {
            this.segments[i] = createSegment(segmentSize, -1, (StatsCounter) builder.getStatsCounterSupplier().get());
        }
    }

    boolean evictsBySize() {
        return this.maxWeight >= 0;
    }

    boolean customWeigher() {
        return this.weigher != OneWeigher.INSTANCE;
    }

    boolean expires() {
        return expiresAfterWrite() || expiresAfterAccess();
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    boolean refreshes() {
        return this.refreshNanos > 0;
    }

    boolean usesAccessQueue() {
        return expiresAfterAccess() || evictsBySize();
    }

    boolean usesWriteQueue() {
        return expiresAfterWrite();
    }

    boolean recordsWrite() {
        return expiresAfterWrite() || refreshes();
    }

    boolean recordsAccess() {
        return expiresAfterAccess();
    }

    boolean recordsTime() {
        return recordsWrite() || recordsAccess();
    }

    boolean usesWriteEntries() {
        return usesWriteQueue() || recordsWrite();
    }

    boolean usesAccessEntries() {
        return usesAccessQueue() || recordsAccess();
    }

    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    static <K, V> ValueReference<K, V> unset() {
        return UNSET;
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <E> Queue<E> discardingQueue() {
        return DISCARDING_QUEUE;
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
    ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
        Segment<K, V> segment = segmentFor(hash);
        segment.lock();
        try {
            ReferenceEntry<K, V> newEntry = segment.newEntry(key, hash, next);
            return newEntry;
        } finally {
            segment.unlock();
        }
    }

    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
        return segmentFor(original.getHash()).copyEntry(original, newNext);
    }

    @VisibleForTesting
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight) {
        return this.valueStrength.referenceValue(segmentFor(entry.getHash()), entry, Preconditions.checkNotNull(value), weight);
    }

    int hash(@Nullable Object key) {
        return rehash(this.keyEquivalence.hash(key));
    }

    void reclaimValue(ValueReference<K, V> valueReference) {
        ReferenceEntry<K, V> entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    void reclaimKey(ReferenceEntry<K, V> entry) {
        int hash = entry.getHash();
        segmentFor(hash).reclaimKey(entry, hash);
    }

    @VisibleForTesting
    boolean isLive(ReferenceEntry<K, V> entry, long now) {
        return segmentFor(entry.getHash()).getLiveValue(entry, now) != null;
    }

    Segment<K, V> segmentFor(int hash) {
        return this.segments[(hash >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, StatsCounter statsCounter) {
        return new Segment(this, initialCapacity, maxSegmentWeight, statsCounter);
    }

    @Nullable
    V getLiveValue(ReferenceEntry<K, V> entry, long now) {
        if (entry.getKey() == null) {
            return null;
        }
        V value = entry.getValueReference().get();
        if (value == null) {
            return null;
        }
        if (isExpired(entry, now)) {
            return null;
        }
        return value;
    }

    boolean isExpired(ReferenceEntry<K, V> entry, long now) {
        Preconditions.checkNotNull(entry);
        if (expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) {
            return true;
        }
        if (!expiresAfterWrite() || now - entry.getWriteTime() < this.expireAfterWriteNanos) {
            return false;
        }
        return true;
    }

    static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextInAccessQueue(next);
        next.setPreviousInAccessQueue(previous);
    }

    static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextInAccessQueue(nullEntry);
        nulled.setPreviousInAccessQueue(nullEntry);
    }

    static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextInWriteQueue(next);
        next.setPreviousInWriteQueue(previous);
    }

    static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextInWriteQueue(nullEntry);
        nulled.setPreviousInWriteQueue(nullEntry);
    }

    void processPendingNotifications() {
        while (true) {
            RemovalNotification<K, V> notification = (RemovalNotification) this.removalNotificationQueue.poll();
            if (notification != null) {
                try {
                    this.removalListener.onRemoval(notification);
                } catch (Throwable e) {
                    logger.log(Level.WARNING, "Exception thrown by removal listener", e);
                }
            } else {
                return;
            }
        }
    }

    final Segment<K, V>[] newSegmentArray(int ssize) {
        return new Segment[ssize];
    }

    public void cleanUp() {
        for (Segment<?, ?> segment : this.segments) {
            segment.cleanUp();
        }
    }

    public boolean isEmpty() {
        int i;
        long sum = 0;
        Segment<K, V>[] segments = this.segments;
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

    long longSize() {
        long sum = 0;
        for (Segment segment : this.segments) {
            sum += (long) Math.max(0, segment.count);
        }
        return sum;
    }

    public int size() {
        return Ints.saturatedCast(longSize());
    }

    @Nullable
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).get(key, hash);
    }

    @Nullable
    public V getIfPresent(Object key) {
        int hash = hash(Preconditions.checkNotNull(key));
        V value = segmentFor(hash).get(key, hash);
        if (value == null) {
            this.globalStatsCounter.recordMisses(1);
        } else {
            this.globalStatsCounter.recordHits(1);
        }
        return value;
    }

    @Nullable
    public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
        V result = get(key);
        return result != null ? result : defaultValue;
    }

    V get(K key, CacheLoader<? super K, V> loader) throws ExecutionException {
        int hash = hash(Preconditions.checkNotNull(key));
        return segmentFor(hash).get(key, hash, loader);
    }

    V getOrLoad(K key) throws ExecutionException {
        return get(key, this.defaultLoader);
    }

    ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
        int hits = 0;
        int misses = 0;
        Map result = Maps.newLinkedHashMap();
        for (K key : keys) {
            V value = get(key);
            if (value == null) {
                misses++;
            } else {
                result.put(key, value);
                hits++;
            }
        }
        this.globalStatsCounter.recordHits(hits);
        this.globalStatsCounter.recordMisses(misses);
        return ImmutableMap.copyOf(result);
    }

    ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
        int hits = 0;
        int misses = 0;
        Map result = Maps.newLinkedHashMap();
        Set<K> keysToLoad = Sets.newLinkedHashSet();
        for (K key : keys) {
            V value = get(key);
            if (!result.containsKey(key)) {
                result.put(key, value);
                if (value == null) {
                    misses++;
                    keysToLoad.add(key);
                } else {
                    hits++;
                }
            }
        }
        try {
            if (!keysToLoad.isEmpty()) {
                Map<K, V> newEntries = loadAll(keysToLoad, this.defaultLoader);
                for (K key2 : keysToLoad) {
                    value = newEntries.get(key2);
                    if (value == null) {
                        throw new InvalidCacheLoadException("loadAll failed to return a value for " + key2);
                    }
                    result.put(key2, value);
                }
            }
        } catch (UnsupportedLoadingOperationException e) {
            for (K key22 : keysToLoad) {
                misses--;
                result.put(key22, get(key22, this.defaultLoader));
            }
        } catch (Throwable th) {
            this.globalStatsCounter.recordHits(hits);
            this.globalStatsCounter.recordMisses(misses);
        }
        ImmutableMap<K, V> copyOf = ImmutableMap.copyOf(result);
        this.globalStatsCounter.recordHits(hits);
        this.globalStatsCounter.recordMisses(misses);
        return copyOf;
    }

    @Nullable
    Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader) throws ExecutionException {
        Preconditions.checkNotNull(loader);
        Preconditions.checkNotNull(keys);
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean success = false;
        try {
            Map<K, V> result = loader.loadAll(keys);
            if (!true) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            }
            if (result == null) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
                throw new InvalidCacheLoadException(loader + " returned null map from loadAll");
            }
            stopwatch.stop();
            boolean nullsPresent = false;
            for (Entry<K, V> entry : result.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (key == null || value == null) {
                    nullsPresent = true;
                } else {
                    put(key, value);
                }
            }
            if (nullsPresent) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
                throw new InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
            }
            this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            return result;
        } catch (UnsupportedLoadingOperationException e) {
            success = true;
            throw e;
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw new ExecutionException(e2);
        } catch (RuntimeException e3) {
            throw new UncheckedExecutionException(e3);
        } catch (Exception e4) {
            throw new ExecutionException(e4);
        } catch (Error e5) {
            throw new ExecutionError(e5);
        } catch (Throwable th) {
            if (!success) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            }
        }
    }

    ReferenceEntry<K, V> getEntry(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).getEntry(key, hash);
    }

    void refresh(K key) {
        int hash = hash(Preconditions.checkNotNull(key));
        segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
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
        long now = this.ticker.read();
        Segment<K, V>[] segments = this.segments;
        long last = -1;
        for (int i = 0; i < 3; i++) {
            long sum = 0;
            for (Segment<K, V> segment : segments) {
                int unused = segment.count;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                for (int j = 0; j < table.length(); j++) {
                    for (ReferenceEntry<K, V> e = (ReferenceEntry) table.get(j); e != null; e = e.getNext()) {
                        V v = segment.getLiveValue(e, now);
                        if (v != null && this.valueEquivalence.equivalent(value, v)) {
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

    public V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, false);
    }

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

    public V remove(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash);
    }

    public boolean remove(@Nullable Object key, @Nullable Object value) {
        if (key == null || value == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, value);
    }

    public boolean replace(K key, @Nullable V oldValue, V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    public V replace(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, value);
    }

    public void clear() {
        for (Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }

    void invalidateAll(Iterable<?> keys) {
        for (Object key : keys) {
            remove(key);
        }
    }

    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        ks = new KeySet(this);
        this.keySet = ks;
        return ks;
    }

    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        vs = new Values(this);
        this.values = vs;
        return vs;
    }

    @GwtIncompatible
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        es = new EntrySet(this);
        this.entrySet = es;
        return es;
    }

    private static <E> ArrayList<E> toArrayList(Collection<E> c) {
        ArrayList<E> result = new ArrayList(c.size());
        Iterators.addAll(result, c.iterator());
        return result;
    }
}
