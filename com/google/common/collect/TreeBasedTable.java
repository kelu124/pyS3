package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0;
    private final Comparator<? super C> columnComparator;

    class C06561 implements Function<Map<C, V>, Iterator<C>> {
        C06561() {
        }

        public Iterator<C> apply(Map<C, V> input) {
            return input.keySet().iterator();
        }
    }

    private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super C> comparator;

        Factory(Comparator<? super C> comparator) {
            this.comparator = comparator;
        }

        public TreeMap<C, V> get() {
            return new TreeMap(this.comparator);
        }
    }

    private class TreeRow extends Row implements SortedMap<C, V> {
        @Nullable
        final C lowerBound;
        @Nullable
        final C upperBound;
        transient SortedMap<C, V> wholeRow;

        TreeRow(TreeBasedTable treeBasedTable, R rowKey) {
            this(rowKey, null, null);
        }

        TreeRow(R rowKey, @Nullable C lowerBound, @Nullable C upperBound) {
            super(rowKey);
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            boolean z = lowerBound == null || upperBound == null || compare(lowerBound, upperBound) <= 0;
            Preconditions.checkArgument(z);
        }

        public SortedSet<C> keySet() {
            return new SortedKeySet(this);
        }

        public Comparator<? super C> comparator() {
            return TreeBasedTable.this.columnComparator();
        }

        int compare(Object a, Object b) {
            return comparator().compare(a, b);
        }

        boolean rangeContains(@Nullable Object o) {
            return o != null && ((this.lowerBound == null || compare(this.lowerBound, o) <= 0) && (this.upperBound == null || compare(this.upperBound, o) > 0));
        }

        public SortedMap<C, V> subMap(C fromKey, C toKey) {
            boolean z = rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey));
            Preconditions.checkArgument(z);
            return new TreeRow(this.rowKey, fromKey, toKey);
        }

        public SortedMap<C, V> headMap(C toKey) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
            return new TreeRow(this.rowKey, this.lowerBound, toKey);
        }

        public SortedMap<C, V> tailMap(C fromKey) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
            return new TreeRow(this.rowKey, fromKey, this.upperBound);
        }

        public C firstKey() {
            if (backingRowMap() != null) {
                return backingRowMap().firstKey();
            }
            throw new NoSuchElementException();
        }

        public C lastKey() {
            if (backingRowMap() != null) {
                return backingRowMap().lastKey();
            }
            throw new NoSuchElementException();
        }

        SortedMap<C, V> wholeRow() {
            if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
                this.wholeRow = (SortedMap) TreeBasedTable.this.backingMap.get(this.rowKey);
            }
            return this.wholeRow;
        }

        SortedMap<C, V> backingRowMap() {
            return (SortedMap) super.backingRowMap();
        }

        SortedMap<C, V> computeBackingRowMap() {
            SortedMap<C, V> map = wholeRow();
            if (map == null) {
                return null;
            }
            if (this.lowerBound != null) {
                map = map.tailMap(this.lowerBound);
            }
            if (this.upperBound != null) {
                map = map.headMap(this.upperBound);
            }
            return map;
        }

        void maintainEmptyInvariant() {
            if (wholeRow() != null && this.wholeRow.isEmpty()) {
                TreeBasedTable.this.backingMap.remove(this.rowKey);
                this.wholeRow = null;
                this.backingRowMap = null;
            }
        }

        public boolean containsKey(Object key) {
            return rangeContains(key) && super.containsKey(key);
        }

        public V put(C key, V value) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
            return super.put(key, value);
        }
    }

    public /* bridge */ /* synthetic */ Set cellSet() {
        return super.cellSet();
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ Map column(Object x0) {
        return super.column(x0);
    }

    public /* bridge */ /* synthetic */ Set columnKeySet() {
        return super.columnKeySet();
    }

    public /* bridge */ /* synthetic */ Map columnMap() {
        return super.columnMap();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object x0, Object x1) {
        return super.contains(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean containsColumn(Object x0) {
        return super.containsColumn(x0);
    }

    public /* bridge */ /* synthetic */ boolean containsRow(Object x0) {
        return super.containsRow(x0);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object x0) {
        return super.containsValue(x0);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ Object get(Object x0, Object x1) {
        return super.get(x0, x1);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Object put(Object x0, Object x1, Object x2) {
        return super.put(x0, x1, x2);
    }

    public /* bridge */ /* synthetic */ void putAll(Table x0) {
        super.putAll(x0);
    }

    public /* bridge */ /* synthetic */ Object remove(Object x0, Object x1) {
        return super.remove(x0, x1);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ Collection values() {
        return super.values();
    }

    public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
        return new TreeBasedTable(Ordering.natural(), Ordering.natural());
    }

    public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(rowComparator);
        Preconditions.checkNotNull(columnComparator);
        return new TreeBasedTable(rowComparator, columnComparator);
    }

    public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
        TreeBasedTable<R, C, V> result = new TreeBasedTable(table.rowComparator(), table.columnComparator());
        result.putAll(table);
        return result;
    }

    TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
        super(new TreeMap(rowComparator), new Factory(columnComparator));
        this.columnComparator = columnComparator;
    }

    public Comparator<? super R> rowComparator() {
        return rowKeySet().comparator();
    }

    public Comparator<? super C> columnComparator() {
        return this.columnComparator;
    }

    public SortedMap<C, V> row(R rowKey) {
        return new TreeRow(this, rowKey);
    }

    public SortedSet<R> rowKeySet() {
        return super.rowKeySet();
    }

    public SortedMap<R, Map<C, V>> rowMap() {
        return super.rowMap();
    }

    Iterator<C> createColumnKeyIterator() {
        final Comparator<? super C> comparator = columnComparator();
        final Iterator<C> merged = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new C06561()), comparator);
        return new AbstractIterator<C>() {
            C lastValue;

            protected C computeNext() {
                while (merged.hasNext()) {
                    boolean duplicate;
                    C next = merged.next();
                    if (this.lastValue == null || comparator.compare(next, this.lastValue) != 0) {
                        duplicate = false;
                        continue;
                    } else {
                        duplicate = true;
                        continue;
                    }
                    if (!duplicate) {
                        this.lastValue = next;
                        return this.lastValue;
                    }
                }
                this.lastValue = null;
                return endOfData();
            }
        };
    }
}
