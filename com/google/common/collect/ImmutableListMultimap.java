package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    @RetainedWith
    @LazyInit
    private transient ImmutableListMultimap<V, K> inverse;

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMultimap.Builder<K, V> {
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
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            super.putAll((Iterable) entries);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            super.putAll((Object) key, (Iterable) values);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K key, V... values) {
            super.putAll((Object) key, (Object[]) values);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            super.putAll((Multimap) multimap);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
            super.orderKeysBy(keyComparator);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }

        public ImmutableListMultimap<K, V> build() {
            return (ImmutableListMultimap) super.build();
        }
    }

    public static <K, V> ImmutableListMultimap<K, V> of() {
        return EmptyImmutableListMultimap.INSTANCE;
    }

    public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
        Builder<K, V> builder = builder();
        builder.put((Object) k1, (Object) v1);
        return builder.build();
    }

    public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
        Builder<K, V> builder = builder();
        builder.put((Object) k1, (Object) v1);
        builder.put((Object) k2, (Object) v2);
        return builder.build();
    }

    public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Builder<K, V> builder = builder();
        builder.put((Object) k1, (Object) v1);
        builder.put((Object) k2, (Object) v2);
        builder.put((Object) k3, (Object) v3);
        return builder.build();
    }

    public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Builder<K, V> builder = builder();
        builder.put((Object) k1, (Object) v1);
        builder.put((Object) k2, (Object) v2);
        builder.put((Object) k3, (Object) v3);
        builder.put((Object) k4, (Object) v4);
        return builder.build();
    }

    public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Builder<K, V> builder = builder();
        builder.put((Object) k1, (Object) v1);
        builder.put((Object) k2, (Object) v2);
        builder.put((Object) k3, (Object) v3);
        builder.put((Object) k4, (Object) v4);
        builder.put((Object) k5, (Object) v5);
        return builder.build();
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap.isEmpty()) {
            return of();
        }
        if (multimap instanceof ImmutableListMultimap) {
            ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap) multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        com.google.common.collect.ImmutableMap.Builder<K, ImmutableList<V>> builder = new com.google.common.collect.ImmutableMap.Builder(multimap.asMap().size());
        int size = 0;
        for (Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
            ImmutableList<V> list = ImmutableList.copyOf((Collection) entry.getValue());
            if (!list.isEmpty()) {
                builder.put(entry.getKey(), list);
                size += list.size();
            }
        }
        return new ImmutableListMultimap(builder.build(), size);
    }

    @Beta
    public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        return new Builder().putAll((Iterable) entries).build();
    }

    ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
        super(map, size);
    }

    public ImmutableList<V> get(@Nullable K key) {
        ImmutableList<V> list = (ImmutableList) this.map.get(key);
        return list == null ? ImmutableList.of() : list;
    }

    public ImmutableListMultimap<V, K> inverse() {
        ImmutableListMultimap<V, K> immutableListMultimap = this.inverse;
        if (immutableListMultimap != null) {
            return immutableListMultimap;
        }
        immutableListMultimap = invert();
        this.inverse = immutableListMultimap;
        return immutableListMultimap;
    }

    private ImmutableListMultimap<V, K> invert() {
        Builder<V, K> builder = builder();
        Iterator i$ = entries().iterator();
        while (i$.hasNext()) {
            Entry<K, V> entry = (Entry) i$.next();
            builder.put(entry.getValue(), entry.getKey());
        }
        ImmutableListMultimap<V, K> invertedMultimap = builder.build();
        invertedMultimap.inverse = this;
        return invertedMultimap;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableList<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableList<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultimap(this, stream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException("Invalid key count " + keyCount);
        }
        com.google.common.collect.ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
        int tmpSize = 0;
        for (int i = 0; i < keyCount; i++) {
            Object key = stream.readObject();
            int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException("Invalid value count " + valueCount);
            }
            com.google.common.collect.ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
            for (int j = 0; j < valueCount; j++) {
                valuesBuilder.add(stream.readObject());
            }
            builder.put(key, valuesBuilder.build());
            tmpSize += valueCount;
        }
        try {
            FieldSettersHolder.MAP_FIELD_SETTER.set((Object) this, builder.build());
            FieldSettersHolder.SIZE_FIELD_SETTER.set((Object) this, tmpSize);
        } catch (IllegalArgumentException e) {
            throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
        }
    }
}
