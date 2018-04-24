package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

    private final class CellSet extends Indexed<Cell<R, C, V>> {
        private CellSet() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        Cell<R, C, V> get(int index) {
            return RegularImmutableTable.this.getCell(index);
        }

        public boolean contains(@Nullable Object object) {
            if (!(object instanceof Cell)) {
                return false;
            }
            Cell<?, ?, ?> cell = (Cell) object;
            Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            if (value == null || !value.equals(cell.getValue())) {
                return false;
            }
            return true;
        }

        boolean isPartialView() {
            return false;
        }
    }

    private final class Values extends ImmutableList<V> {
        private Values() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        public V get(int index) {
            return RegularImmutableTable.this.getValue(index);
        }

        boolean isPartialView() {
            return true;
        }
    }

    abstract Cell<R, C, V> getCell(int i);

    abstract V getValue(int i);

    RegularImmutableTable() {
    }

    final ImmutableSet<Cell<R, C, V>> createCellSet() {
        return isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    final ImmutableCollection<V> createValues() {
        return isEmpty() ? ImmutableList.of() : new Values();
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (!(rowComparator == null && columnComparator == null)) {
            Collections.sort(cells, new Comparator<Cell<R, C, V>>() {
                public int compare(Cell<R, C, V> cell1, Cell<R, C, V> cell2) {
                    int i = 0;
                    int rowCompare = rowComparator == null ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    if (columnComparator != null) {
                        i = columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                    }
                    return i;
                }
            });
        }
        return forCellsInternal(cells, rowComparator, columnComparator);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Cell<R, C, V>> cells) {
        return forCellsInternal(cells, null, null);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
        Collection rowSpaceBuilder = new LinkedHashSet();
        Collection columnSpaceBuilder = new LinkedHashSet();
        ImmutableList<Cell<R, C, V>> cellList = ImmutableList.copyOf((Iterable) cells);
        for (Cell<R, C, V> cell : cells) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        return forOrderedComponents(cellList, rowComparator == null ? ImmutableSet.copyOf(rowSpaceBuilder) : ImmutableSet.copyOf(Ordering.from((Comparator) rowComparator).immutableSortedCopy(rowSpaceBuilder)), columnComparator == null ? ImmutableSet.copyOf(columnSpaceBuilder) : ImmutableSet.copyOf(Ordering.from((Comparator) columnComparator).immutableSortedCopy(columnSpaceBuilder)));
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        return ((long) cellList.size()) > (((long) rowSpace.size()) * ((long) columnSpace.size())) / 2 ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
    }
}
