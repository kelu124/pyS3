package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.Weak;
import com.itextpdf.text.html.HtmlTags;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;

    public static class Builder<K, V> {
        Multimap<K, V> builderMultimap;
        Comparator<? super K> keyComparator;
        Comparator<? super V> valueComparator;

        public Builder() {
            this(MultimapBuilder.linkedHashKeys().arrayListValues().build());
        }

        Builder(Multimap<K, V> builderMultimap) {
            this.builderMultimap = builderMultimap;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K key, V value) {
            CollectPreconditions.checkEntryNotNull(key, value);
            this.builderMultimap.put(key, value);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            for (Entry<? extends K, ? extends V> entry : entries) {
                put(entry);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            if (key == null) {
                throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
            }
            Collection<V> valueList = this.builderMultimap.get(key);
            for (V value : values) {
                CollectPreconditions.checkEntryNotNull(key, value);
                valueList.add(value);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K key, V... values) {
            return putAll((Object) key, Arrays.asList(values));
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                putAll(entry.getKey(), (Iterable) entry.getValue());
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
            this.keyComparator = (Comparator) Preconditions.checkNotNull(keyComparator);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
            this.valueComparator = (Comparator) Preconditions.checkNotNull(valueComparator);
            return this;
        }

        public ImmutableMultimap<K, V> build() {
            if (this.valueComparator != null) {
                for (Collection<V> values : this.builderMultimap.asMap().values()) {
                    Collections.sort((List) values, this.valueComparator);
                }
            }
            if (this.keyComparator != null) {
                Multimap<K, V> sortedCopy = MultimapBuilder.linkedHashKeys().arrayListValues().build();
                for (Entry<K, Collection<V>> entry : Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(this.builderMultimap.asMap().entrySet())) {
                    sortedCopy.putAll(entry.getKey(), (Iterable) entry.getValue());
                }
                this.builderMultimap = sortedCopy;
            }
            return ImmutableMultimap.copyOf(this.builderMultimap);
        }
    }

    private abstract class Itr<T> extends UnmodifiableIterator<T> {
        K key;
        final Iterator<Entry<K, Collection<V>>> mapIterator;
        Iterator<V> valueIterator;

        abstract T output(K k, V v);

        private Itr() {
            this.mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
            this.key = null;
            this.valueIterator = Iterators.emptyIterator();
        }

        public boolean hasNext() {
            return this.mapIterator.hasNext() || this.valueIterator.hasNext();
        }

        public T next() {
            if (!this.valueIterator.hasNext()) {
                Entry<K, Collection<V>> mapEntry = (Entry) this.mapIterator.next();
                this.key = mapEntry.getKey();
                this.valueIterator = ((Collection) mapEntry.getValue()).iterator();
            }
            return output(this.key, this.valueIterator.next());
        }
    }

    class C05171 extends Itr<Entry<K, V>> {
        C05171() {
            super();
        }

        Entry<K, V> output(K key, V value) {
            return Maps.immutableEntry(key, value);
        }
    }

    class C05182 extends Itr<V> {
        C05182() {
            super();
        }

        V output(K k, V value) {
            return value;
        }
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
        private static final long serialVersionUID = 0;
        @Weak
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        boolean isPartialView() {
            return this.multimap.isPartialView();
        }

        public int size() {
            return this.multimap.size();
        }

        public boolean contains(Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry<?, ?> entry = (Entry) object;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }
    }

    @GwtIncompatible
    static class FieldSettersHolder {
        static final FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
        static final FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, HtmlTags.SIZE);

        FieldSettersHolder() {
        }
    }

    class Keys extends ImmutableMultiset<K> {
        Keys() {
        }

        public boolean contains(@Nullable Object object) {
            return ImmutableMultimap.this.containsKey(object);
        }

        public int count(@Nullable Object element) {
            Collection<V> values = (Collection) ImmutableMultimap.this.map.get(element);
            return values == null ? 0 : values.size();
        }

        public Set<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }

        public int size() {
            return ImmutableMultimap.this.size();
        }

        Multiset.Entry<K> getEntry(int index) {
            Entry<K, ? extends Collection<V>> entry = (Entry) ImmutableMultimap.this.map.entrySet().asList().get(index);
            return Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size());
        }

        boolean isPartialView() {
            return true;
        }
    }

    private static final class Values<K, V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        @Weak
        private final transient ImmutableMultimap<K, V> multimap;

        Values(ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }

        public boolean contains(@Nullable Object object) {
            return this.multimap.containsValue(object);
        }

        public UnmodifiableIterator<V> iterator() {
            return this.multimap.valueIterator();
        }

        @GwtIncompatible
        int copyIntoArray(Object[] dst, int offset) {
            Iterator i$ = this.multimap.map.values().iterator();
            while (i$.hasNext()) {
                offset = ((ImmutableCollection) i$.next()).copyIntoArray(dst, offset);
            }
            return offset;
        }

        public int size() {
            return this.multimap.size();
        }

        boolean isPartialView() {
            return true;
        }
    }

    public abstract ImmutableCollection<V> get(K k);

    public abstract ImmutableMultimap<V, K> inverse();

    public /* bridge */ /* synthetic */ boolean containsEntry(Object x0, Object x1) {
        return super.containsEntry(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> ImmutableMultimap<K, V> of() {
        return ImmutableListMultimap.of();
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
        return ImmutableListMultimap.of(k1, v1);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
        return ImmutableListMultimap.of(k1, v1, k2, v2);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap) multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        return ImmutableListMultimap.copyOf((Multimap) multimap);
    }

    @Beta
    public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        return ImmutableListMultimap.copyOf((Iterable) entries);
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
        this.map = map;
        this.size = size;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableCollection<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableCollection<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public boolean put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public boolean putAll(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public boolean containsKey(@Nullable Object key) {
        return this.map.containsKey(key);
    }

    public boolean containsValue(@Nullable Object value) {
        return value != null && super.containsValue(value);
    }

    public int size() {
        return this.size;
    }

    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError("should never be called");
    }

    public ImmutableCollection<Entry<K, V>> entries() {
        return (ImmutableCollection) super.entries();
    }

    ImmutableCollection<Entry<K, V>> createEntries() {
        return new EntryCollection(this);
    }

    UnmodifiableIterator<Entry<K, V>> entryIterator() {
        return new C05171();
    }

    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset) super.keys();
    }

    ImmutableMultiset<K> createKeys() {
        return new Keys();
    }

    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    ImmutableCollection<V> createValues() {
        return new Values(this);
    }

    UnmodifiableIterator<V> valueIterator() {
        return new C05182();
    }
}
