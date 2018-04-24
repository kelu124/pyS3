package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    private transient Map<K, Collection<V>> asMap;
    private transient Collection<Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient Collection<V> values;

    private class Entries extends Entries<K, V> {
        private Entries() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

    private class EntrySet extends Entries implements Set<Entry<K, V>> {
        private EntrySet() {
            super();
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }

        public int size() {
            return AbstractMultimap.this.size();
        }

        public boolean contains(@Nullable Object o) {
            return AbstractMultimap.this.containsValue(o);
        }

        public void clear() {
            AbstractMultimap.this.clear();
        }
    }

    abstract Map<K, Collection<V>> createAsMap();

    abstract Iterator<Entry<K, V>> entryIterator();

    AbstractMultimap() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(@Nullable Object value) {
        for (Collection<V> collection : asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
        Collection<V> collection = (Collection) asMap().get(key);
        return collection != null && collection.contains(value);
    }

    @CanIgnoreReturnValue
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        Collection<V> collection = (Collection) asMap().get(key);
        return collection != null && collection.remove(value);
    }

    @CanIgnoreReturnValue
    public boolean put(@Nullable K key, @Nullable V value) {
        return get(key).add(value);
    }

    @CanIgnoreReturnValue
    public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof Collection) {
            Collection<? extends V> valueCollection = (Collection) values;
            if (valueCollection.isEmpty() || !get(key).addAll(valueCollection)) {
                return false;
            }
            return true;
        }
        Iterator<? extends V> valueItr = values.iterator();
        if (valueItr.hasNext() && Iterators.addAll(get(key), valueItr)) {
            return true;
        }
        return false;
    }

    @CanIgnoreReturnValue
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @CanIgnoreReturnValue
    public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        Collection<V> result = removeAll(key);
        putAll(key, values);
        return result;
    }

    public Collection<Entry<K, V>> entries() {
        Collection<Entry<K, V>> collection = this.entries;
        if (collection != null) {
            return collection;
        }
        collection = createEntries();
        this.entries = collection;
        return collection;
    }

    Collection<Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return new EntrySet();
        }
        return new Entries();
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = createKeySet();
        this.keySet = set;
        return set;
    }

    Set<K> createKeySet() {
        return new KeySet(asMap());
    }

    public Multiset<K> keys() {
        Multiset<K> multiset = this.keys;
        if (multiset != null) {
            return multiset;
        }
        multiset = createKeys();
        this.keys = multiset;
        return multiset;
    }

    Multiset<K> createKeys() {
        return new Keys(this);
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = createValues();
        this.values = collection;
        return collection;
    }

    Collection<V> createValues() {
        return new Values();
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(entries().iterator());
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    public boolean equals(@Nullable Object object) {
        return Multimaps.equalsImpl(this, object);
    }

    public int hashCode() {
        return asMap().hashCode();
    }

    public String toString() {
        return asMap().toString();
    }
}
