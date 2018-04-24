package org.apache.poi.ss.usermodel.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Internal;

public abstract class RowShifter {
    protected final Sheet sheet;

    public abstract void updateConditionalFormatting(FormulaShifter formulaShifter);

    public abstract void updateFormulas(FormulaShifter formulaShifter);

    public abstract void updateHyperlinks(FormulaShifter formulaShifter);

    public abstract void updateNamedRanges(FormulaShifter formulaShifter);

    @Internal
    public abstract void updateRowFormulas(Row row, FormulaShifter formulaShifter);

    public RowShifter(Sheet sh) {
        this.sheet = sh;
    }

    public List<CellRangeAddress> shiftMergedRegions(int startRow, int endRow, int n) {
        List<CellRangeAddress> shiftedRegions = new ArrayList();
        Set<Integer> removedIndices = new HashSet();
        int size = this.sheet.getNumMergedRegions();
        for (int i = 0; i < size; i++) {
            CellRangeAddress merged = this.sheet.getMergedRegion(i);
            if (startRow + n > merged.getFirstRow() || endRow + n < merged.getLastRow()) {
                boolean inStart = merged.getFirstRow() >= startRow || merged.getLastRow() >= startRow;
                boolean inEnd = merged.getFirstRow() <= endRow || merged.getLastRow() <= endRow;
                if (inStart && inEnd && !merged.containsRow(startRow - 1) && !merged.containsRow(endRow + 1)) {
                    merged.setFirstRow(merged.getFirstRow() + n);
                    merged.setLastRow(merged.getLastRow() + n);
                    shiftedRegions.add(merged);
                    removedIndices.add(Integer.valueOf(i));
                }
            } else {
                removedIndices.add(Integer.valueOf(i));
            }
        }
        if (!removedIndices.isEmpty()) {
            this.sheet.removeMergedRegions(removedIndices);
        }
        for (CellRangeAddress region : shiftedRegions) {
            this.sheet.addMergedRegion(region);
        }
        return shiftedRegions;
    }
}
