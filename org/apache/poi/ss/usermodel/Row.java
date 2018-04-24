package org.apache.poi.ss.usermodel;

import java.util.Iterator;
import org.apache.poi.util.Removal;

public interface Row extends Iterable<Cell> {
    @Deprecated
    @Removal(version = "3.17")
    public static final MissingCellPolicy CREATE_NULL_AS_BLANK = MissingCellPolicy.CREATE_NULL_AS_BLANK;
    @Deprecated
    @Removal(version = "3.17")
    public static final MissingCellPolicy RETURN_BLANK_AS_NULL = MissingCellPolicy.RETURN_BLANK_AS_NULL;
    @Deprecated
    @Removal(version = "3.17")
    public static final MissingCellPolicy RETURN_NULL_AND_BLANK = MissingCellPolicy.RETURN_NULL_AND_BLANK;

    public enum MissingCellPolicy {
        RETURN_NULL_AND_BLANK(1),
        RETURN_BLANK_AS_NULL(2),
        CREATE_NULL_AS_BLANK(3);
        
        @Deprecated
        @Removal(version = "3.17")
        public final int id;

        private MissingCellPolicy(int id) {
            this.id = id;
        }
    }

    Iterator<Cell> cellIterator();

    Cell createCell(int i);

    Cell createCell(int i, int i2);

    Cell createCell(int i, CellType cellType);

    Cell getCell(int i);

    Cell getCell(int i, MissingCellPolicy missingCellPolicy);

    short getFirstCellNum();

    short getHeight();

    float getHeightInPoints();

    short getLastCellNum();

    int getOutlineLevel();

    int getPhysicalNumberOfCells();

    int getRowNum();

    CellStyle getRowStyle();

    Sheet getSheet();

    boolean getZeroHeight();

    boolean isFormatted();

    void removeCell(Cell cell);

    void setHeight(short s);

    void setHeightInPoints(float f);

    void setRowNum(int i);

    void setRowStyle(CellStyle cellStyle);

    void setZeroHeight(boolean z);
}
