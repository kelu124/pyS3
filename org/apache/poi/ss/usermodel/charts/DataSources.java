package org.apache.poi.ss.usermodel.charts;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class DataSources {

    private static abstract class AbstractCellRangeDataSource<T> implements ChartDataSource<T> {
        private final CellRangeAddress cellRangeAddress;
        private FormulaEvaluator evaluator;
        private final int numOfCells = this.cellRangeAddress.getNumberOfCells();
        private final Sheet sheet;

        protected AbstractCellRangeDataSource(Sheet sheet, CellRangeAddress cellRangeAddress) {
            this.sheet = sheet;
            this.cellRangeAddress = cellRangeAddress.copy();
            this.evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        }

        public int getPointCount() {
            return this.numOfCells;
        }

        public boolean isReference() {
            return true;
        }

        public String getFormulaString() {
            return this.cellRangeAddress.formatAsString(this.sheet.getSheetName(), true);
        }

        protected CellValue getCellValueAt(int index) {
            if (index < 0 || index >= this.numOfCells) {
                throw new IndexOutOfBoundsException("Index must be between 0 and " + (this.numOfCells - 1) + " (inclusive), given: " + index);
            }
            int firstRow = this.cellRangeAddress.getFirstRow();
            int firstCol = this.cellRangeAddress.getFirstColumn();
            int width = (this.cellRangeAddress.getLastColumn() - firstCol) + 1;
            int cellIndex = firstCol + (index % width);
            Row row = this.sheet.getRow(firstRow + (index / width));
            return row == null ? null : this.evaluator.evaluate(row.getCell(cellIndex));
        }
    }

    private static class ArrayDataSource<T> implements ChartDataSource<T> {
        private final T[] elements;

        public ArrayDataSource(T[] elements) {
            this.elements = (Object[]) elements.clone();
        }

        public int getPointCount() {
            return this.elements.length;
        }

        public T getPointAt(int index) {
            return this.elements[index];
        }

        public boolean isReference() {
            return false;
        }

        public boolean isNumeric() {
            return Number.class.isAssignableFrom(this.elements.getClass().getComponentType());
        }

        public String getFormulaString() {
            throw new UnsupportedOperationException("Literal data source can not be expressed by reference.");
        }
    }

    private DataSources() {
    }

    public static <T> ChartDataSource<T> fromArray(T[] elements) {
        return new ArrayDataSource(elements);
    }

    public static ChartDataSource<Number> fromNumericCellRange(Sheet sheet, CellRangeAddress cellRangeAddress) {
        return new AbstractCellRangeDataSource<Number>(sheet, cellRangeAddress) {
            public Number getPointAt(int index) {
                CellValue cellValue = getCellValueAt(index);
                if (cellValue == null || cellValue.getCellTypeEnum() != CellType.NUMERIC) {
                    return null;
                }
                return Double.valueOf(cellValue.getNumberValue());
            }

            public boolean isNumeric() {
                return true;
            }
        };
    }

    public static ChartDataSource<String> fromStringCellRange(Sheet sheet, CellRangeAddress cellRangeAddress) {
        return new AbstractCellRangeDataSource<String>(sheet, cellRangeAddress) {
            public String getPointAt(int index) {
                CellValue cellValue = getCellValueAt(index);
                if (cellValue == null || cellValue.getCellTypeEnum() != CellType.STRING) {
                    return null;
                }
                return cellValue.getStringValue();
            }

            public boolean isNumeric() {
                return false;
            }
        };
    }
}
