package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Map;

@GwtCompatible
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    final C singleColumnKey;
    final R singleRowKey;
    final V singleValue;

    SingletonImmutableTable(R rowKey, C columnKey, V value) {
        this.singleRowKey = Preconditions.checkNotNull(rowKey);
        this.singleColumnKey = Preconditions.checkNotNull(columnKey);
        this.singleValue = Preconditions.checkNotNull(value);
    }

    SingletonImmutableTable(Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }

    public ImmutableMap<R, V> column(C columnKey) {
        Preconditions.checkNotNull(columnKey);
        return containsColumn(columnKey) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
    }

    public int size() {
        return 1;
    }

    ImmutableSet<Cell<R, C, V>> createCellSet() {
        return ImmutableSet.of(ImmutableTable.cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }

    ImmutableCollection<V> createValues() {
        return ImmutableSet.of(this.singleValue);
    }

    SerializedForm createSerializedForm() {
        return SerializedForm.create(this, new int[]{0}, new int[]{0});
    }
}
