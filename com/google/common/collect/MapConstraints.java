package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
@Deprecated
@Beta
public final class MapConstraints {

    static class ConstrainedAsMapEntries<K, V> extends ForwardingSet<Entry<K, Collection<V>>> {
        private final MapConstraint<? super K, ? super V> constraint;
        private final Set<Entry<K, Collection<V>>> entries;

        ConstrainedAsMapEntries(Set<Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        protected Set<Entry<K, Collection<V>>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, Collection<V>>> iterator() {
            return new TransformedIterator<Entry<K, Collection<V>>, Entry<K, Collection<V>>>(this.entries.iterator()) {
                Entry<K, Collection<V>> transform(Entry<K, Collection<V>> from) {
                    return MapConstraints.constrainedAsMapEntry(from, ConstrainedAsMapEntries.this.constraint);
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        public boolean equals(@Nullable Object object) {
            return standardEquals(object);
        }

        public int hashCode() {
            return standardHashCode();
        }

        public boolean remove(Object o) {
            return Maps.removeEntryImpl(delegate(), o);
        }

        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    private static class ConstrainedAsMapValues<K, V> extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;
        final Set<Entry<K, Collection<V>>> entrySet;

        ConstrainedAsMapValues(Collection<Collection<V>> delegate, Set<Entry<K, Collection<V>>> entrySet) {
            this.delegate = delegate;
            this.entrySet = entrySet;
        }

        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator<Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
            return new Iterator<Collection<V>>() {
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Collection<V> next() {
                    return (Collection) ((Entry) iterator.next()).getValue();
                }

                public void remove() {
                    iterator.remove();
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public boolean contains(Object o) {
            return standardContains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        public boolean remove(Object o) {
            return standardRemove(o);
        }

        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    private static class ConstrainedEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
        final MapConstraint<? super K, ? super V> constraint;
        final Collection<Entry<K, V>> entries;

        ConstrainedEntries(Collection<Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        protected Collection<Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, V>> iterator() {
            return new TransformedIterator<Entry<K, V>, Entry<K, V>>(this.entries.iterator()) {
                Entry<K, V> transform(Entry<K, V> from) {
                    return MapConstraints.constrainedEntry(from, ConstrainedEntries.this.constraint);
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        public boolean remove(Object o) {
            return Maps.removeEntryImpl(delegate(), o);
        }

        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    static class ConstrainedEntrySet<K, V> extends ConstrainedEntries<K, V> implements Set<Entry<K, V>> {
        ConstrainedEntrySet(Set<Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            super(entries, constraint);
        }

        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class ConstrainedMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        transient Map<K, Collection<V>> asMap;
        final MapConstraint<? super K, ? super V> constraint;
        final Multimap<K, V> delegate;
        transient Collection<Entry<K, V>> entries;

        public ConstrainedMultimap(Multimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = (Multimap) Preconditions.checkNotNull(delegate);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(constraint);
        }

        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.asMap;
            if (result != null) {
                return result;
            }
            final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
            result = new ForwardingMap<K, Collection<V>>() {
                Set<Entry<K, Collection<V>>> entrySet;
                Collection<Collection<V>> values;

                protected Map<K, Collection<V>> delegate() {
                    return asMapDelegate;
                }

                public Set<Entry<K, Collection<V>>> entrySet() {
                    Set<Entry<K, Collection<V>>> result = this.entrySet;
                    if (result != null) {
                        return result;
                    }
                    result = MapConstraints.constrainedAsMapEntries(asMapDelegate.entrySet(), ConstrainedMultimap.this.constraint);
                    this.entrySet = result;
                    return result;
                }

                public Collection<V> get(Object key) {
                    try {
                        Collection<V> collection = ConstrainedMultimap.this.get(key);
                        if (collection.isEmpty()) {
                            return null;
                        }
                        return collection;
                    } catch (ClassCastException e) {
                        return null;
                    }
                }

                public Collection<Collection<V>> values() {
                    Collection<Collection<V>> result = this.values;
                    if (result != null) {
                        return result;
                    }
                    result = new ConstrainedAsMapValues(delegate().values(), entrySet());
                    this.values = result;
                    return result;
                }

                public boolean containsValue(Object o) {
                    return values().contains(o);
                }
            };
            this.asMap = result;
            return result;
        }

        public Collection<Entry<K, V>> entries() {
            Collection<Entry<K, V>> result = this.entries;
            if (result != null) {
                return result;
            }
            result = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
            this.entries = result;
            return result;
        }

        public Collection<V> get(final K key) {
            return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>() {
                public V checkElement(V value) {
                    ConstrainedMultimap.this.constraint.checkKeyValue(key, value);
                    return value;
                }
            });
        }

        public boolean put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        public boolean putAll(K key, Iterable<? extends V> values) {
            return this.delegate.putAll(key, MapConstraints.checkValues(key, values, this.constraint));
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            boolean changed = false;
            for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
                changed |= put(entry.getKey(), entry.getValue());
            }
            return changed;
        }

        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            return this.delegate.replaceValues(key, MapConstraints.checkValues(key, values, this.constraint));
        }
    }

    private static class ConstrainedListMultimap<K, V> extends ConstrainedMultimap<K, V> implements ListMultimap<K, V> {
        ConstrainedListMultimap(ListMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        public List<V> get(K key) {
            return (List) super.get(key);
        }

        public List<V> removeAll(Object key) {
            return (List) super.removeAll(key);
        }

        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            return (List) super.replaceValues(key, values);
        }
    }

    static class ConstrainedMap<K, V> extends ForwardingMap<K, V> {
        final MapConstraint<? super K, ? super V> constraint;
        private final Map<K, V> delegate;
        private transient Set<Entry<K, V>> entrySet;

        ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = (Map) Preconditions.checkNotNull(delegate);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(constraint);
        }

        protected Map<K, V> delegate() {
            return this.delegate;
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> result = this.entrySet;
            if (result != null) {
                return result;
            }
            result = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
            this.entrySet = result;
            return result;
        }

        @CanIgnoreReturnValue
        public V put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
        }
    }

    private MapConstraints() {
    }

    public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMap(map, constraint);
    }

    public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedListMultimap(multimap, constraint);
    }

    private static <K, V> Entry<K, V> constrainedEntry(final Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, V>() {
            protected Entry<K, V> delegate() {
                return entry;
            }

            public V setValue(V value) {
                constraint.checkKeyValue(getKey(), value);
                return entry.setValue(value);
            }
        };
    }

    private static <K, V> Entry<K, Collection<V>> constrainedAsMapEntry(final Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, Collection<V>>() {

            class C05591 implements Constraint<V> {
                C05591() {
                }

                public V checkElement(V value) {
                    constraint.checkKeyValue(C05602.this.getKey(), value);
                    return value;
                }
            }

            protected Entry<K, Collection<V>> delegate() {
                return entry;
            }

            public Collection<V> getValue() {
                return Constraints.constrainedTypePreservingCollection((Collection) entry.getValue(), new C05591());
            }
        };
    }

    private static <K, V> Set<Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedAsMapEntries(entries, constraint);
    }

    private static <K, V> Collection<Entry<K, V>> constrainedEntries(Collection<Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        if (entries instanceof Set) {
            return constrainedEntrySet((Set) entries, constraint);
        }
        return new ConstrainedEntries(entries, constraint);
    }

    private static <K, V> Set<Entry<K, V>> constrainedEntrySet(Set<Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedEntrySet(entries, constraint);
    }

    private static <K, V> Collection<V> checkValues(K key, Iterable<? extends V> values, MapConstraint<? super K, ? super V> constraint) {
        Collection<V> copy = Lists.newArrayList((Iterable) values);
        for (V value : copy) {
            constraint.checkKeyValue(key, value);
        }
        return copy;
    }

    private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> constraint) {
        Map<K, V> copy = new LinkedHashMap(map);
        for (Entry<K, V> entry : copy.entrySet()) {
            constraint.checkKeyValue(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
