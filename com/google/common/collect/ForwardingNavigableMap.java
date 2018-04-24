package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;

@GwtIncompatible
public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V> {

    @Beta
    protected class StandardDescendingMap extends DescendingMap<K, V> {

        class C05021 implements Iterator<Entry<K, V>> {
            private Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();
            private Entry<K, V> toRemove = null;

            C05021() {
            }

            public boolean hasNext() {
                return this.nextOrNull != null;
            }

            public Entry<K, V> next() {
                if (hasNext()) {
                    try {
                        Entry<K, V> entry = this.nextOrNull;
                        return entry;
                    } finally {
                        this.toRemove = this.nextOrNull;
                        this.nextOrNull = StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                this.toRemove = null;
            }
        }

        NavigableMap<K, V> forward() {
            return ForwardingNavigableMap.this;
        }

        protected Iterator<Entry<K, V>> entryIterator() {
            return new C05021();
        }
    }

    @Beta
    protected class StandardNavigableKeySet extends NavigableKeySet<K, V> {
        public StandardNavigableKeySet() {
            super(ForwardingNavigableMap.this);
        }
    }

    protected abstract NavigableMap<K, V> delegate();

    protected ForwardingNavigableMap() {
    }

    public Entry<K, V> lowerEntry(K key) {
        return delegate().lowerEntry(key);
    }

    protected Entry<K, V> standardLowerEntry(K key) {
        return headMap(key, false).lastEntry();
    }

    public K lowerKey(K key) {
        return delegate().lowerKey(key);
    }

    protected K standardLowerKey(K key) {
        return Maps.keyOrNull(lowerEntry(key));
    }

    public Entry<K, V> floorEntry(K key) {
        return delegate().floorEntry(key);
    }

    protected Entry<K, V> standardFloorEntry(K key) {
        return headMap(key, true).lastEntry();
    }

    public K floorKey(K key) {
        return delegate().floorKey(key);
    }

    protected K standardFloorKey(K key) {
        return Maps.keyOrNull(floorEntry(key));
    }

    public Entry<K, V> ceilingEntry(K key) {
        return delegate().ceilingEntry(key);
    }

    protected Entry<K, V> standardCeilingEntry(K key) {
        return tailMap(key, true).firstEntry();
    }

    public K ceilingKey(K key) {
        return delegate().ceilingKey(key);
    }

    protected K standardCeilingKey(K key) {
        return Maps.keyOrNull(ceilingEntry(key));
    }

    public Entry<K, V> higherEntry(K key) {
        return delegate().higherEntry(key);
    }

    protected Entry<K, V> standardHigherEntry(K key) {
        return tailMap(key, false).firstEntry();
    }

    public K higherKey(K key) {
        return delegate().higherKey(key);
    }

    protected K standardHigherKey(K key) {
        return Maps.keyOrNull(higherEntry(key));
    }

    public Entry<K, V> firstEntry() {
        return delegate().firstEntry();
    }

    protected Entry<K, V> standardFirstEntry() {
        return (Entry) Iterables.getFirst(entrySet(), null);
    }

    protected K standardFirstKey() {
        Entry<K, V> entry = firstEntry();
        if (entry != null) {
            return entry.getKey();
        }
        throw new NoSuchElementException();
    }

    public Entry<K, V> lastEntry() {
        return delegate().lastEntry();
    }

    protected Entry<K, V> standardLastEntry() {
        return (Entry) Iterables.getFirst(descendingMap().entrySet(), null);
    }

    protected K standardLastKey() {
        Entry<K, V> entry = lastEntry();
        if (entry != null) {
            return entry.getKey();
        }
        throw new NoSuchElementException();
    }

    public Entry<K, V> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    protected Entry<K, V> standardPollFirstEntry() {
        return (Entry) Iterators.pollNext(entrySet().iterator());
    }

    public Entry<K, V> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    protected Entry<K, V> standardPollLastEntry() {
        return (Entry) Iterators.pollNext(descendingMap().entrySet().iterator());
    }

    public NavigableMap<K, V> descendingMap() {
        return delegate().descendingMap();
    }

    public NavigableSet<K> navigableKeySet() {
        return delegate().navigableKeySet();
    }

    public NavigableSet<K> descendingKeySet() {
        return delegate().descendingKeySet();
    }

    @Beta
    protected NavigableSet<K> standardDescendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return delegate().headMap(toKey, inclusive);
    }

    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return delegate().tailMap(fromKey, inclusive);
    }

    protected SortedMap<K, V> standardHeadMap(K toKey) {
        return headMap(toKey, false);
    }

    protected SortedMap<K, V> standardTailMap(K fromKey) {
        return tailMap(fromKey, true);
    }
}
