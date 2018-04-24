package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final int[] cellColumnIndices;
    private final int[] cellRowIndices;
    private final int[] columnCounts = new int[this.columnKeyToIndex.size()];
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] rowCounts = new int[this.rowKeyToIndex.size()];
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final V[][] values;

    private static abstract class ImmutableArrayMap<K, V> extends IteratorBasedImmutableMap<K, V> {
        private final int size;

        class C04951 extends AbstractIterator<Entry<K, V>> {
            private int index = -1;
            private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

            C04951() {
            }

            protected Entry<K, V> computeNext() {
                this.index++;
                while (this.index < this.maxIndex) {
                    V value = ImmutableArrayMap.this.getValue(this.index);
                    if (value != null) {
                        return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                    }
                    this.index++;
                }
                return (Entry) endOfData();
            }
        }

        @Nullable
        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        ImmutableArrayMap(int size) {
            this.size = size;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        K getKey(int index) {
            return keyToIndex().keySet().asList().get(index);
        }

        ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        public int size() {
            return this.size;
        }

        public V get(@Nullable Object key) {
            Integer keyIndex = (Integer) keyToIndex().get(key);
            return keyIndex == null ? null : getValue(keyIndex.intValue());
        }

        UnmodifiableIterator<Entry<K, V>> entryIterator() {
            return new C04951();
        }
    }

    private final class Column extends ImmutableArrayMap<R, V> {
        private final int columnIndex;

        Column(int columnIndex) {
            super(DenseImmutableTable.this.columnCounts[columnIndex]);
            this.columnIndex = columnIndex;
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        V getValue(int keyIndex) {
            return DenseImmutableTable.this.values[keyIndex][this.columnIndex];
        }

        boolean isPartialView() {
            return true;
        }
    }

    private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(DenseImmutableTable.this.columnCounts.length);
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        Map<R, V> getValue(int keyIndex) {
            return new Column(keyIndex);
        }

        boolean isPartialView() {
            return false;
        }
    }

    private final class Row extends ImmutableArrayMap<C, V> {
        private final int rowIndex;

        Row(int rowIndex) {
            super(DenseImmutableTable.this.rowCounts[rowIndex]);
            this.rowIndex = rowIndex;
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        V getValue(int keyIndex) {
            return DenseImmutableTable.this.values[this.rowIndex][keyIndex];
        }

        boolean isPartialView() {
            return true;
        }
    }

    private final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(DenseImmutableTable.this.rowCounts.length);
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        Map<C, V> getValue(int keyIndex) {
            return new Row(keyIndex);
        }

        boolean isPartialView() {
            return false;
        }
    }

    DenseImmutableTable(ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        this.values = (Object[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{rowSpace.size(), columnSpace.size()}));
        this.rowKeyToIndex = Maps.indexMap(rowSpace);
        this.columnKeyToIndex = Maps.indexMap(columnSpace);
        int[] cellRowIndices = new int[cellList.size()];
        int[] cellColumnIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); i++) {
            Cell<R, C, V> cell = (Cell) cellList.get(i);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            int rowIndex = ((Integer) this.rowKeyToIndex.get(rowKey)).intValue();
            int columnIndex = ((Integer) this.columnKeyToIndex.get(columnKey)).intValue();
            Preconditions.checkArgument(this.values[rowIndex][columnIndex] == null, "duplicate key: (%s, %s)", rowKey, columnKey);
            this.values[rowIndex][columnIndex] = cell.getValue();
            int[] iArr = this.rowCounts;
            iArr[rowIndex] = iArr[rowIndex] + 1;
            iArr = this.columnCounts;
            iArr[columnIndex] = iArr[columnIndex] + 1;
            cellRowIndices[i] = rowIndex;
            cellColumnIndices[i] = columnIndex;
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnIndices = cellColumnIndices;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = (Integer) this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = (Integer) this.columnKeyToIndex.get(columnKey);
        return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
    }

    public int size() {
        return this.cellRowIndices.length;
    }

    Cell<R, C, V> getCell(int index) {
        int rowIndex = this.cellRowIndices[index];
        int columnIndex = this.cellColumnIndices[index];
        return ImmutableTable.cellOf(rowKeySet().asList().get(rowIndex), columnKeySet().asList().get(columnIndex), this.values[rowIndex][columnIndex]);
    }

    V getValue(int index) {
        return this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]];
    }

    SerializedForm createSerializedForm() {
        return SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
    }
}
