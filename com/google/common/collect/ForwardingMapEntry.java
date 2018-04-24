package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject implements Entry<K, V> {
    protected abstract Entry<K, V> delegate();

    protected ForwardingMapEntry() {
    }

    public K getKey() {
        return delegate().getKey();
    }

    public V getValue() {
        return delegate().getValue();
    }

    public V setValue(V value) {
        return delegate().setValue(value);
    }

    public boolean equals(@Nullable Object object) {
        return delegate().equals(object);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    protected boolean standardEquals(@Nullable Object object) {
        if (!(object instanceof Entry)) {
            return false;
        }
        Entry<?, ?> that = (Entry) object;
        if (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue())) {
            return true;
        }
        return false;
    }

    protected int standardHashCode() {
        int i = 0;
        K k = getKey();
        V v = getValue();
        int hashCode = k == null ? 0 : k.hashCode();
        if (v != null) {
            i = v.hashCode();
        }
        return i ^ hashCode;
    }

    @Beta
    protected String standardToString() {
        return getKey() + "=" + getValue();
    }
}
