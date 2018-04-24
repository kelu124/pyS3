package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
    @Weak
    private final ImmutableMap<K, V> map;

    class C05151 extends UnmodifiableIterator<V> {
        final UnmodifiableIterator<Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();

        C05151() {
        }

        public boolean hasNext() {
            return this.entryItr.hasNext();
        }

        public V next() {
            return ((Entry) this.entryItr.next()).getValue();
        }
    }

    @GwtIncompatible
    private static class SerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<?, V> map;

        SerializedForm(ImmutableMap<?, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    ImmutableMapValues(ImmutableMap<K, V> map) {
        this.map = map;
    }

    public int size() {
        return this.map.size();
    }

    public UnmodifiableIterator<V> iterator() {
        return new C05151();
    }

    public boolean contains(@Nullable Object object) {
        return object != null && Iterators.contains(iterator(), object);
    }

    boolean isPartialView() {
        return true;
    }

    public ImmutableList<V> asList() {
        final ImmutableList<Entry<K, V>> entryList = this.map.entrySet().asList();
        return new ImmutableAsList<V>() {
            public V get(int index) {
                return ((Entry) entryList.get(index)).getValue();
            }

            ImmutableCollection<V> delegateCollection() {
                return ImmutableMapValues.this;
            }
        };
    }

    @GwtIncompatible
    Object writeReplace() {
        return new SerializedForm(this.map);
    }
}
