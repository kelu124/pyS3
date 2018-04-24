package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    @GwtIncompatible
    private static final long serialVersionUID = -2250766705698539974L;
    private transient Map<E, Count> backingMap;
    private transient long size = ((long) super.size());

    private class MapBasedMultisetIterator implements Iterator<E> {
        boolean canRemove;
        Entry<E, Count> currentEntry;
        final Iterator<Entry<E, Count>> entryIterator;
        int occurrencesLeft;

        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }

        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }

        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = (Entry) this.entryIterator.next();
                this.occurrencesLeft = ((Count) this.currentEntry.getValue()).get();
            }
            this.occurrencesLeft--;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (((Count) this.currentEntry.getValue()).get() <= 0) {
                throw new ConcurrentModificationException();
            }
            if (((Count) this.currentEntry.getValue()).addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - 1;
            this.canRemove = false;
        }
    }

    static /* synthetic */ long access$122(AbstractMapBasedMultiset x0, long x1) {
        long j = x0.size - x1;
        x0.size = j;
        return j;
    }

    protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
        this.backingMap = (Map) Preconditions.checkNotNull(backingMap);
    }

    void setBackingMap(Map<E, Count> backingMap) {
        this.backingMap = backingMap;
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }

    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
        return new Iterator<Multiset.Entry<E>>() {
            Entry<E, Count> toRemove;

            public boolean hasNext() {
                return backingEntries.hasNext();
            }

            public Multiset.Entry<E> next() {
                final Entry<E, Count> mapEntry = (Entry) backingEntries.next();
                this.toRemove = mapEntry;
                return new AbstractEntry<E>() {
                    public E getElement() {
                        return mapEntry.getKey();
                    }

                    public int getCount() {
                        Count count = (Count) mapEntry.getValue();
                        if (count == null || count.get() == 0) {
                            Count frequency = (Count) AbstractMapBasedMultiset.this.backingMap.get(getElement());
                            if (frequency != null) {
                                return frequency.get();
                            }
                        }
                        return count == null ? 0 : count.get();
                    }
                };
            }

            public void remove() {
                boolean z;
                if (this.toRemove != null) {
                    z = true;
                } else {
                    z = false;
                }
                CollectPreconditions.checkRemove(z);
                AbstractMapBasedMultiset.access$122(AbstractMapBasedMultiset.this, (long) ((Count) this.toRemove.getValue()).getAndSet(0));
                backingEntries.remove();
                this.toRemove = null;
            }
        };
    }

    public void clear() {
        for (Count frequency : this.backingMap.values()) {
            frequency.set(0);
        }
        this.backingMap.clear();
        this.size = 0;
    }

    int distinctElements() {
        return this.backingMap.size();
    }

    public int size() {
        return Ints.saturatedCast(this.size);
    }

    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    public int count(@Nullable Object element) {
        Count frequency = (Count) Maps.safeGet(this.backingMap, element);
        return frequency == null ? 0 : frequency.get();
    }

    @CanIgnoreReturnValue
    public int add(@Nullable E element, int occurrences) {
        boolean z = true;
        if (occurrences == 0) {
            return count(element);
        }
        boolean z2;
        int oldCount;
        if (occurrences > 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2, "occurrences cannot be negative: %s", occurrences);
        Count frequency = (Count) this.backingMap.get(element);
        if (frequency == null) {
            oldCount = 0;
            this.backingMap.put(element, new Count(occurrences));
        } else {
            oldCount = frequency.get();
            long newCount = ((long) oldCount) + ((long) occurrences);
            if (newCount > 2147483647L) {
                z = false;
            }
            Preconditions.checkArgument(z, "too many occurrences: %s", newCount);
            frequency.add(occurrences);
        }
        this.size += (long) occurrences;
        return oldCount;
    }

    @CanIgnoreReturnValue
    public int remove(@Nullable Object element, int occurrences) {
        if (occurrences == 0) {
            return count(element);
        }
        boolean z;
        if (occurrences > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "occurrences cannot be negative: %s", occurrences);
        Count frequency = (Count) this.backingMap.get(element);
        if (frequency == null) {
            return 0;
        }
        int numberRemoved;
        int oldCount = frequency.get();
        if (oldCount > occurrences) {
            numberRemoved = occurrences;
        } else {
            numberRemoved = oldCount;
            this.backingMap.remove(element);
        }
        frequency.add(-numberRemoved);
        this.size -= (long) numberRemoved;
        return oldCount;
    }

    @CanIgnoreReturnValue
    public int setCount(@Nullable E element, int count) {
        int oldCount;
        CollectPreconditions.checkNonnegative(count, "count");
        if (count == 0) {
            oldCount = getAndSet((Count) this.backingMap.remove(element), count);
        } else {
            Count existingCounter = (Count) this.backingMap.get(element);
            oldCount = getAndSet(existingCounter, count);
            if (existingCounter == null) {
                this.backingMap.put(element, new Count(count));
            }
        }
        this.size += (long) (count - oldCount);
        return oldCount;
    }

    private static int getAndSet(@Nullable Count i, int count) {
        if (i == null) {
            return 0;
        }
        return i.getAndSet(count);
    }

    @GwtIncompatible
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }
}
