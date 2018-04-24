package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Table.Cell;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractTable<R, C, V> implements Table<R, C, V> {
    private transient Set<Cell<R, C, V>> cellSet;
    private transient Collection<V> values;

    class CellSet extends AbstractSet<Cell<R, C, V>> {
        CellSet() {
        }

        public boolean contains(Object o) {
            if (!(o instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) o;
            Map<C, V> row = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (row == null || !Collections2.safeContains(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()))) {
                return false;
            }
            return true;
        }

        public boolean remove(@Nullable Object o) {
            if (!(o instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) o;
            Map<C, V> row = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (row == null || !Collections2.safeRemove(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()))) {
                return false;
            }
            return true;
        }

        public void clear() {
            AbstractTable.this.clear();
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return AbstractTable.this.cellIterator();
        }

        public int size() {
            return AbstractTable.this.size();
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return AbstractTable.this.valuesIterator();
        }

        public boolean contains(Object o) {
            return AbstractTable.this.containsValue(o);
        }

        public void clear() {
            AbstractTable.this.clear();
        }

        public int size() {
            return AbstractTable.this.size();
        }
    }

    abstract Iterator<Cell<R, C, V>> cellIterator();

    AbstractTable() {
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return Maps.safeContainsKey(rowMap(), rowKey);
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        return Maps.safeContainsKey(columnMap(), columnKey);
    }

    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    public Set<C> columnKeySet() {
        return columnMap().keySet();
    }

    public boolean containsValue(@Nullable Object value) {
        for (Map<C, V> row : rowMap().values()) {
            if (row.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        return row != null && Maps.safeContainsKey(row, columnKey);
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        return row == null ? null : Maps.safeGet(row, columnKey);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        Iterators.clear(cellSet().iterator());
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        return row == null ? null : Maps.safeRemove(row, columnKey);
    }

    @CanIgnoreReturnValue
    public V put(R rowKey, C columnKey, V value) {
        return row(rowKey).put(columnKey, value);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    public Set<Cell<R, C, V>> cellSet() {
        Set<Cell<R, C, V>> set = this.cellSet;
        if (set != null) {
            return set;
        }
        set = createCellSet();
        this.cellSet = set;
        return set;
    }

    Set<Cell<R, C, V>> createCellSet() {
        return new CellSet();
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = createValues();
        this.values = collection;
        return collection;
    }

    Collection<V> createValues() {
        return new Values();
    }

    Iterator<V> valuesIterator() {
        return new TransformedIterator<Cell<R, C, V>, V>(cellSet().iterator()) {
            V transform(Cell<R, C, V> cell) {
                return cell.getValue();
            }
        };
    }

    public boolean equals(@Nullable Object obj) {
        return Tables.equalsImpl(this, obj);
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public String toString() {
        return rowMap().toString();
    }
}
