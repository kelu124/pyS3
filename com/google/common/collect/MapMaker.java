package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible(emulated = true)
public final class MapMaker {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int UNSET_INT = -1;
    int concurrencyLevel = -1;
    int initialCapacity = -1;
    Equivalence<Object> keyEquivalence;
    Strength keyStrength;
    boolean useCustomMap;
    Strength valueStrength;

    @GwtIncompatible
    @CanIgnoreReturnValue
    MapMaker keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }

    Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    @CanIgnoreReturnValue
    public MapMaker initialCapacity(int initialCapacity) {
        boolean z = true;
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        if (initialCapacity < 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.initialCapacity = initialCapacity;
        return this;
    }

    int getInitialCapacity() {
        return this.initialCapacity == -1 ? 16 : this.initialCapacity;
    }

    @CanIgnoreReturnValue
    public MapMaker concurrencyLevel(int concurrencyLevel) {
        boolean z = true;
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        if (concurrencyLevel <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    int getConcurrencyLevel() {
        return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public MapMaker weakKeys() {
        return setKeyStrength(Strength.WEAK);
    }

    MapMaker setKeyStrength(Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = (Strength) Preconditions.checkNotNull(strength);
        if (strength != Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    Strength getKeyStrength() {
        return (Strength) MoreObjects.firstNonNull(this.keyStrength, Strength.STRONG);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public MapMaker weakValues() {
        return setValueStrength(Strength.WEAK);
    }

    MapMaker setValueStrength(Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = (Strength) Preconditions.checkNotNull(strength);
        if (strength != Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    Strength getValueStrength() {
        return (Strength) MoreObjects.firstNonNull(this.valueStrength, Strength.STRONG);
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (this.useCustomMap) {
            return MapMakerInternalMap.create(this);
        }
        return new ConcurrentHashMap(getInitialCapacity(), 0.75f, getConcurrencyLevel());
    }

    @GwtIncompatible
    <K, V> MapMakerInternalMap<K, V, ?, ?> makeCustomMap() {
        return MapMakerInternalMap.create(this);
    }

    public String toString() {
        ToStringHelper s = MoreObjects.toStringHelper((Object) this);
        if (this.initialCapacity != -1) {
            s.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            s.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.keyStrength != null) {
            s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue((Object) "keyEquivalence");
        }
        return s.toString();
    }
}
