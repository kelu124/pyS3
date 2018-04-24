package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    static final RegularImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableList.of());
    @LazyInit
    private transient ImmutableSet<E> elementSet;
    private final transient ImmutableEntry<E>[] entries;
    private final transient int hashCode;
    private final transient ImmutableEntry<E>[] hashTable;
    private final transient int size;

    private final class ElementSet extends Indexed<E> {
        private ElementSet() {
        }

        E get(int index) {
            return RegularImmutableMultiset.this.entries[index].getElement();
        }

        public boolean contains(@Nullable Object object) {
            return RegularImmutableMultiset.this.contains(object);
        }

        boolean isPartialView() {
            return true;
        }

        public int size() {
            return RegularImmutableMultiset.this.entries.length;
        }
    }

    private static final class NonTerminalEntry<E> extends ImmutableEntry<E> {
        private final ImmutableEntry<E> nextInBucket;

        NonTerminalEntry(E element, int count, ImmutableEntry<E> nextInBucket) {
            super(element, count);
            this.nextInBucket = nextInBucket;
        }

        public ImmutableEntry<E> nextInBucket() {
            return this.nextInBucket;
        }
    }

    RegularImmutableMultiset(Collection<? extends Entry<? extends E>> entries) {
        int distinct = entries.size();
        ImmutableEntry<E>[] entryArray = new ImmutableEntry[distinct];
        if (distinct == 0) {
            this.entries = entryArray;
            this.hashTable = null;
            this.size = 0;
            this.hashCode = 0;
            this.elementSet = ImmutableSet.of();
            return;
        }
        int tableSize = Hashing.closedTableSize(distinct, 1.0d);
        int mask = tableSize - 1;
        ImmutableEntry<E>[] hashTable = new ImmutableEntry[tableSize];
        int index = 0;
        int hashCode = 0;
        long size = 0;
        for (Entry<? extends E> entry : entries) {
            ImmutableEntry<E> newEntry;
            E element = Preconditions.checkNotNull(entry.getElement());
            int count = entry.getCount();
            int hash = element.hashCode();
            int bucket = Hashing.smear(hash) & mask;
            ImmutableEntry<E> bucketHead = hashTable[bucket];
            ImmutableEntry<E> immutableEntry;
            if (bucketHead == null) {
                boolean canReuseEntry = (entry instanceof ImmutableEntry) && !(entry instanceof NonTerminalEntry);
                if (canReuseEntry) {
                    newEntry = (ImmutableEntry) entry;
                } else {
                    immutableEntry = new ImmutableEntry(element, count);
                }
            } else {
                immutableEntry = new NonTerminalEntry(element, count, bucketHead);
            }
            hashCode += hash ^ count;
            int index2 = index + 1;
            entryArray[index] = newEntry;
            hashTable[bucket] = newEntry;
            size += (long) count;
            index = index2;
        }
        this.entries = entryArray;
        this.hashTable = hashTable;
        this.size = Ints.saturatedCast(size);
        this.hashCode = hashCode;
    }

    boolean isPartialView() {
        return false;
    }

    public int count(@Nullable Object element) {
        ImmutableEntry<E>[] hashTable = this.hashTable;
        if (element == null || hashTable == null) {
            return 0;
        }
        for (ImmutableEntry<E> entry = hashTable[Hashing.smearedHash(element) & (hashTable.length - 1)]; entry != null; entry = entry.nextInBucket()) {
            if (Objects.equal(element, entry.getElement())) {
                return entry.getCount();
            }
        }
        return 0;
    }

    public int size() {
        return this.size;
    }

    public ImmutableSet<E> elementSet() {
        ImmutableSet<E> immutableSet = this.elementSet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = new ElementSet();
        this.elementSet = immutableSet;
        return immutableSet;
    }

    Entry<E> getEntry(int index) {
        return this.entries[index];
    }

    public int hashCode() {
        return this.hashCode;
    }
}
