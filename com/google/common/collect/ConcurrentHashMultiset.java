package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtIncompatible
public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient ConcurrentMap<E, AtomicInteger> countMap;

    class C04912 extends AbstractIterator<Entry<E>> {
        private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();

        C04912() {
        }

        protected Entry<E> computeNext() {
            while (this.mapEntries.hasNext()) {
                Map.Entry<E, AtomicInteger> mapEntry = (Map.Entry) this.mapEntries.next();
                int count = ((AtomicInteger) mapEntry.getValue()).get();
                if (count != 0) {
                    return Multisets.immutableEntry(mapEntry.getKey(), count);
                }
            }
            return (Entry) endOfData();
        }
    }

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        public Object[] toArray() {
            return snapshot().toArray();
        }

        public <T> T[] toArray(T[] array) {
            return snapshot().toArray(array);
        }

        private List<Entry<E>> snapshot() {
            List<Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(list, iterator());
            return list;
        }
    }

    private static class FieldSettersHolder {
        static final FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    public /* bridge */ /* synthetic */ boolean add(Object x0) {
        return super.add(x0);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection x0) {
        return super.addAll(x0);
    }

    public /* bridge */ /* synthetic */ boolean contains(Object x0) {
        return super.contains(x0);
    }

    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public /* bridge */ /* synthetic */ boolean remove(Object x0) {
        return super.remove(x0);
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection x0) {
        return super.removeAll(x0);
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection x0) {
        return super.retainAll(x0);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset(new ConcurrentHashMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
        ConcurrentHashMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    @Deprecated
    @Beta
    public static <E> ConcurrentHashMultiset<E> create(MapMaker mapMaker) {
        return create(mapMaker.makeMap());
    }

    @Beta
    public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
        return new ConcurrentHashMultiset(countMap);
    }

    @VisibleForTesting
    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
        Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", (Object) countMap);
        this.countMap = countMap;
    }

    public int count(@Nullable Object element) {
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        return existingCounter == null ? 0 : existingCounter.get();
    }

    public int size() {
        long sum = 0;
        for (AtomicInteger value : this.countMap.values()) {
            sum += (long) value.get();
        }
        return Ints.saturatedCast(sum);
    }

    public Object[] toArray() {
        return snapshot().toArray();
    }

    public <T> T[] toArray(T[] array) {
        return snapshot().toArray(array);
    }

    private List<E> snapshot() {
        List<E> list = Lists.newArrayListWithExpectedSize(size());
        for (Entry<E> entry : entrySet()) {
            E element = entry.getElement();
            for (int i = entry.getCount(); i > 0; i--) {
                list.add(element);
            }
        }
        return list;
    }

    @CanIgnoreReturnValue
    public int add(E element, int occurrences) {
        Preconditions.checkNotNull(element);
        if (occurrences == 0) {
            return count(element);
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter;
        AtomicInteger newCounter;
        do {
            existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
            if (existingCounter == null) {
                existingCounter = (AtomicInteger) this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
                if (existingCounter == null) {
                    return 0;
                }
            }
            while (true) {
                int oldValue = existingCounter.get();
                if (oldValue == 0) {
                    break;
                }
                try {
                    if (existingCounter.compareAndSet(oldValue, IntMath.checkedAdd(oldValue, occurrences))) {
                        return oldValue;
                    }
                } catch (ArithmeticException e) {
                    throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
                }
            }
            newCounter = new AtomicInteger(occurrences);
            if (this.countMap.putIfAbsent(element, newCounter) == null) {
                break;
            }
        } while (!this.countMap.replace(element, existingCounter, newCounter));
        return 0;
    }

    @CanIgnoreReturnValue
    public int remove(@Nullable Object element, int occurrences) {
        if (occurrences == 0) {
            return count(element);
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        int oldValue;
        int newValue;
        do {
            oldValue = existingCounter.get();
            if (oldValue == 0) {
                return 0;
            }
            newValue = Math.max(0, oldValue - occurrences);
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue != 0) {
            return oldValue;
        }
        this.countMap.remove(element, existingCounter);
        return oldValue;
    }

    @CanIgnoreReturnValue
    public boolean removeExactly(@Nullable Object element, int occurrences) {
        if (occurrences == 0) {
            return true;
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return false;
        }
        int oldValue;
        int newValue;
        do {
            oldValue = existingCounter.get();
            if (oldValue < occurrences) {
                return false;
            }
            newValue = oldValue - occurrences;
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue != 0) {
            return true;
        }
        this.countMap.remove(element, existingCounter);
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    public int setCount(E r7, int r8) {
        /*
        r6 = this;
        r3 = 0;
        com.google.common.base.Preconditions.checkNotNull(r7);
        r4 = "count";
        com.google.common.collect.CollectPreconditions.checkNonnegative(r8, r4);
    L_0x0009:
        r4 = r6.countMap;
        r0 = com.google.common.collect.Maps.safeGet(r4, r7);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 != 0) goto L_0x0028;
    L_0x0013:
        if (r8 != 0) goto L_0x0017;
    L_0x0015:
        r2 = r3;
    L_0x0016:
        return r2;
    L_0x0017:
        r4 = r6.countMap;
        r5 = new java.util.concurrent.atomic.AtomicInteger;
        r5.<init>(r8);
        r0 = r4.putIfAbsent(r7, r5);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 != 0) goto L_0x0028;
    L_0x0026:
        r2 = r3;
        goto L_0x0016;
    L_0x0028:
        r2 = r0.get();
        if (r2 != 0) goto L_0x0049;
    L_0x002e:
        if (r8 != 0) goto L_0x0032;
    L_0x0030:
        r2 = r3;
        goto L_0x0016;
    L_0x0032:
        r1 = new java.util.concurrent.atomic.AtomicInteger;
        r1.<init>(r8);
        r4 = r6.countMap;
        r4 = r4.putIfAbsent(r7, r1);
        if (r4 == 0) goto L_0x0047;
    L_0x003f:
        r4 = r6.countMap;
        r4 = r4.replace(r7, r0, r1);
        if (r4 == 0) goto L_0x0009;
    L_0x0047:
        r2 = r3;
        goto L_0x0016;
    L_0x0049:
        r4 = r0.compareAndSet(r2, r8);
        if (r4 == 0) goto L_0x0028;
    L_0x004f:
        if (r8 != 0) goto L_0x0016;
    L_0x0051:
        r3 = r6.countMap;
        r3.remove(r7, r0);
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.setCount(java.lang.Object, int):int");
    }

    @CanIgnoreReturnValue
    public boolean setCount(E element, int expectedOldCount, int newCount) {
        boolean z = false;
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter != null) {
            int oldValue = existingCounter.get();
            if (oldValue == expectedOldCount) {
                if (oldValue == 0) {
                    if (newCount == 0) {
                        this.countMap.remove(element, existingCounter);
                        return true;
                    }
                    AtomicInteger newCounter = new AtomicInteger(newCount);
                    if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter)) {
                        z = true;
                    }
                    return z;
                } else if (existingCounter.compareAndSet(oldValue, newCount)) {
                    if (newCount != 0) {
                        return true;
                    }
                    this.countMap.remove(element, existingCounter);
                    return true;
                }
            }
            return false;
        } else if (expectedOldCount != 0) {
            return false;
        } else {
            if (newCount == 0 || this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null) {
                return true;
            }
            return false;
        }
    }

    Set<E> createElementSet() {
        final Set<E> delegate = this.countMap.keySet();
        return new ForwardingSet<E>() {
            protected Set<E> delegate() {
                return delegate;
            }

            public boolean contains(@Nullable Object object) {
                return object != null && Collections2.safeContains(delegate, object);
            }

            public boolean containsAll(Collection<?> collection) {
                return standardContainsAll(collection);
            }

            public boolean remove(Object object) {
                return object != null && Collections2.safeRemove(delegate, object);
            }

            public boolean removeAll(Collection<?> c) {
                return standardRemoveAll(c);
            }
        };
    }

    public Set<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    int distinctElements() {
        return this.countMap.size();
    }

    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    Iterator<Entry<E>> entryIterator() {
        final Iterator<Entry<E>> readOnlyIterator = new C04912();
        return new ForwardingIterator<Entry<E>>() {
            private Entry<E> last;

            protected Iterator<Entry<E>> delegate() {
                return readOnlyIterator;
            }

            public Entry<E> next() {
                this.last = (Entry) super.next();
                return this.last;
            }

            public void remove() {
                boolean z;
                if (this.last != null) {
                    z = true;
                } else {
                    z = false;
                }
                CollectPreconditions.checkRemove(z);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    public void clear() {
        this.countMap.clear();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.countMap);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set((Object) this, (ConcurrentMap) stream.readObject());
    }
}
