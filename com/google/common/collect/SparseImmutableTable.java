package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
    private final int[] cellColumnInRowIndices;
    private final int[] cellRowIndices;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final ImmutableMap<R, Map<C, V>> rowMap;

    SparseImmutableTable(ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
        Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
        Iterator i$ = rowSpace.iterator();
        while (i$.hasNext()) {
            rows.put(i$.next(), new LinkedHashMap());
        }
        Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
        i$ = columnSpace.iterator();
        while (i$.hasNext()) {
            columns.put(i$.next(), new LinkedHashMap());
        }
        int[] cellRowIndices = new int[cellList.size()];
        int[] cellColumnInRowIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); i++) {
            Cell<R, C, V> cell = (Cell) cellList.get(i);
            R rowKey = cell.getRowKey();
            C columnKey = cell.getColumnKey();
            V value = cell.getValue();
            cellRowIndices[i] = ((Integer) rowIndex.get(rowKey)).intValue();
            Map<C, V> thisRow = (Map) rows.get(rowKey);
            cellColumnInRowIndices[i] = thisRow.size();
            V oldValue = thisRow.put(columnKey, value);
            if (oldValue != null) {
                throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
            }
            ((Map) columns.get(columnKey)).put(rowKey, value);
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnInRowIndices = cellColumnInRowIndices;
        Builder<R, Map<C, V>> builder = new Builder(rows.size());
        for (Entry<R, Map<C, V>> row : rows.entrySet()) {
            builder.put(row.getKey(), ImmutableMap.copyOf((Map) row.getValue()));
        }
        this.rowMap = builder.build();
        Builder<C, Map<R, V>> columnBuilder = new Builder(columns.size());
        for (Entry<C, Map<R, V>> col : columns.entrySet()) {
            columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map) col.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public int size() {
        return this.cellRowIndices.length;
    }

    Cell<R, C, V> getCell(int index) {
        Entry<R, Map<C, V>> rowEntry = (Entry) this.rowMap.entrySet().asList().get(this.cellRowIndices[index]);
        ImmutableMap<C, V> row = (ImmutableMap) rowEntry.getValue();
        Entry<C, V> colEntry = (Entry) row.entrySet().asList().get(this.cellColumnInRowIndices[index]);
        return ImmutableTable.cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }

    V getValue(int index) {
        ImmutableMap<C, V> row = (ImmutableMap) this.rowMap.values().asList().get(this.cellRowIndices[index]);
        return row.values().asList().get(this.cellColumnInRowIndices[index]);
    }

    SerializedForm createSerializedForm() {
        Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
        int[] cellColumnIndices = new int[cellSet().size()];
        int i = 0;
        Iterator i$ = cellSet().iterator();
        while (i$.hasNext()) {
            int i2 = i + 1;
            cellColumnIndices[i] = ((Integer) columnKeyToIndex.get(((Cell) i$.next()).getColumnKey())).intValue();
            i = i2;
        }
        return SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
    }
}
