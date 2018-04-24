package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap<K, V> descendingMap;
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    public static class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K key, V value) {
            super.put(key, value);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll((Map) map);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            super.putAll((Iterable) entries);
            return this;
        }

        @CanIgnoreReturnValue
        @Deprecated
        @Beta
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> comparator) {
            throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
        }

        public ImmutableSortedMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableSortedMap.emptyMap(this.comparator);
                case 1:
                    return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
            }
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = sortedMap.comparator();
        }

        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
        return of(Ordering.natural(), k1, v1);
    }

    private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
        return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.of(k1), (Comparator) Preconditions.checkNotNull(comparator)), ImmutableList.of(v1));
    }

    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(ImmutableMapEntry<K, V>... entries) {
        return fromEntries(Ordering.natural(), false, entries, entries.length);
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
        return ofEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ofEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ofEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ofEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return copyOfInternal(map, NATURAL_ORDER);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        return copyOf((Iterable) entries, (Comparator) NATURAL_ORDER);
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
        return fromEntries((Comparator) Preconditions.checkNotNull(comparator), false, entries);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        if (map instanceof ImmutableSortedMap) {
            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, true, map.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            Comparator<?> comparator2 = ((SortedMap) map).comparator();
            sameComparator = comparator2 == null ? comparator == NATURAL_ORDER : comparator.equals(comparator2);
        }
        if (sameComparator && (map instanceof ImmutableSortedMap)) {
            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return fromEntries(comparator, sameComparator, map.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        Entry[] entryArray = (Entry[]) ((Entry[]) Iterables.toArray((Iterable) entries, EMPTY_ENTRY_ARRAY));
        return fromEntries(comparator, sameComparator, entryArray, entryArray.length);
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Entry<K, V>[] entryArray, int size) {
        switch (size) {
            case 0:
                return emptyMap(comparator);
            case 1:
                return of(comparator, entryArray[0].getKey(), entryArray[0].getValue());
            default:
                Object[] keys = new Object[size];
                Object[] values = new Object[size];
                int i;
                if (sameComparator) {
                    for (i = 0; i < size; i++) {
                        Object key = entryArray[i].getKey();
                        Object value = entryArray[i].getValue();
                        CollectPreconditions.checkEntryNotNull(key, value);
                        keys[i] = key;
                        values[i] = value;
                    }
                } else {
                    Arrays.sort(entryArray, 0, size, Ordering.from((Comparator) comparator).onKeys());
                    K prevKey = entryArray[0].getKey();
                    keys[0] = prevKey;
                    values[0] = entryArray[0].getValue();
                    for (i = 1; i < size; i++) {
                        K key2 = entryArray[i].getKey();
                        V value2 = entryArray[i].getValue();
                        CollectPreconditions.checkEntryNotNull(key2, value2);
                        keys[i] = key2;
                        values[i] = value2;
                        ImmutableMap.checkNoConflict(comparator.compare(prevKey, key2) != 0, "key", entryArray[i - 1], entryArray[i]);
                        prevKey = key2;
                    }
                }
                return new ImmutableSortedMap(new RegularImmutableSortedSet(new RegularImmutableList(keys), comparator), new RegularImmutableList(values));
        }
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder(comparator);
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
        this(keySet, valueList, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
        this.keySet = keySet;
        this.valueList = valueList;
        this.descendingMap = descendingMap;
    }

    public int size() {
        return this.valueList.size();
    }

    public V get(@Nullable Object key) {
        int index = this.keySet.indexOf(key);
        return index == -1 ? null : this.valueList.get(index);
    }

    boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet<K, V>() {

            class C05261 extends ImmutableAsList<Entry<K, V>> {
                C05261() {
                }

                public Entry<K, V> get(int index) {
                    return Maps.immutableEntry(ImmutableSortedMap.this.keySet.asList().get(index), ImmutableSortedMap.this.valueList.get(index));
                }

                ImmutableCollection<Entry<K, V>> delegateCollection() {
                    return AnonymousClass1EntrySet.this;
                }
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<K, V>> createAsList() {
                return new C05261();
            }

            ImmutableMap<K, V> map() {
                return ImmutableSortedMap.this;
            }
        };
    }

    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    public K firstKey() {
        return keySet().first();
    }

    public K lastKey() {
        return keySet().last();
    }

    private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
        if (fromIndex == 0 && toIndex == size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return emptyMap(comparator());
        }
        return new ImmutableSortedMap(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }

    public ImmutableSortedMap<K, V> headMap(K toKey) {
        return headMap((Object) toKey, false);
    }

    public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
        return getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
    }

    public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap((Object) fromKey, true, (Object) toKey, false);
    }

    public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument(comparator().compare(fromKey, toKey) <= 0, "expected fromKey <= toKey but %s > %s", (Object) fromKey, (Object) toKey);
        return headMap((Object) toKey, toInclusive).tailMap((Object) fromKey, fromInclusive);
    }

    public ImmutableSortedMap<K, V> tailMap(K fromKey) {
        return tailMap((Object) fromKey, true);
    }

    public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), size());
    }

    public Entry<K, V> lowerEntry(K key) {
        return headMap((Object) key, false).lastEntry();
    }

    public K lowerKey(K key) {
        return Maps.keyOrNull(lowerEntry(key));
    }

    public Entry<K, V> floorEntry(K key) {
        return headMap((Object) key, true).lastEntry();
    }

    public K floorKey(K key) {
        return Maps.keyOrNull(floorEntry(key));
    }

    public Entry<K, V> ceilingEntry(K key) {
        return tailMap((Object) key, true).firstEntry();
    }

    public K ceilingKey(K key) {
        return Maps.keyOrNull(ceilingEntry(key));
    }

    public Entry<K, V> higherEntry(K key) {
        return tailMap((Object) key, false).firstEntry();
    }

    public K higherKey(K key) {
        return Maps.keyOrNull(higherEntry(key));
    }

    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(0);
    }

    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(size() - 1);
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result != null) {
            return result;
        }
        if (isEmpty()) {
            return emptyMap(Ordering.from(comparator()).reverse());
        }
        return new ImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }

    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
