package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;
    @Nullable
    private transient Entry<K, V> entrySetCache;

    class C07171 extends AbstractSet<K> {
        C07171() {
        }

        public UnmodifiableIterator<K> iterator() {
            final Iterator<Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
            return new UnmodifiableIterator<K>() {
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }

                public K next() {
                    Entry<K, V> entry = (Entry) entryIterator.next();
                    MapIteratorCache.this.entrySetCache = entry;
                    return entry.getKey();
                }
            };
        }

        public int size() {
            return MapIteratorCache.this.backingMap.size();
        }

        public boolean contains(@Nullable Object key) {
            return MapIteratorCache.this.containsKey(key);
        }
    }

    MapIteratorCache(Map<K, V> backingMap) {
        this.backingMap = (Map) Preconditions.checkNotNull(backingMap);
    }

    @CanIgnoreReturnValue
    public V put(@Nullable K key, @Nullable V value) {
        clearCache();
        return this.backingMap.put(key, value);
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object key) {
        clearCache();
        return this.backingMap.remove(key);
    }

    public void clear() {
        clearCache();
        this.backingMap.clear();
    }

    public V get(@Nullable Object key) {
        V value = getIfCached(key);
        return value != null ? value : getWithoutCaching(key);
    }

    public final V getWithoutCaching(@Nullable Object key) {
        return this.backingMap.get(key);
    }

    public final boolean containsKey(@Nullable Object key) {
        return getIfCached(key) != null || this.backingMap.containsKey(key);
    }

    public final Set<K> unmodifiableKeySet() {
        return new C07171();
    }

    protected V getIfCached(@Nullable Object key) {
        Entry<K, V> entry = this.entrySetCache;
        if (entry == null || entry.getKey() != key) {
            return null;
        }
        return entry.getValue();
    }

    protected void clearCache() {
        this.entrySetCache = null;
    }
}
