package org.apache.poi.ss.formula;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.ptg.Area2DPtgBase;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.AreaPtgBase;
import org.apache.poi.ss.formula.ptg.Deleted3DPxg;
import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;

public final class FormulaShifter {
    private final int _amountToMove;
    private final int _dstSheetIndex;
    private final int _externSheetIndex;
    private final int _firstMovedIndex;
    private final int _lastMovedIndex;
    private final ShiftMode _mode;
    private final String _sheetName;
    private final int _srcSheetIndex;
    private final SpreadsheetVersion _version;

    private FormulaShifter(int externSheetIndex, String sheetName, int firstMovedIndex, int lastMovedIndex, int amountToMove, ShiftMode mode, SpreadsheetVersion version) {
        if (amountToMove == 0) {
            throw new IllegalArgumentException("amountToMove must not be zero");
        } else if (firstMovedIndex > lastMovedIndex) {
            throw new IllegalArgumentException("firstMovedIndex, lastMovedIndex out of order");
        } else {
            this._externSheetIndex = externSheetIndex;
            this._sheetName = sheetName;
            this._firstMovedIndex = firstMovedIndex;
            this._lastMovedIndex = lastMovedIndex;
            this._amountToMove = amountToMove;
            this._mode = mode;
            this._version = version;
            this._dstSheetIndex = -1;
            this._srcSheetIndex = -1;
        }
    }

    private FormulaShifter(int srcSheetIndex, int dstSheetIndex) {
        this._amountToMove = -1;
        this._lastMovedIndex = -1;
        this._firstMovedIndex = -1;
        this._externSheetIndex = -1;
        this._sheetName = null;
        this._version = null;
        this._srcSheetIndex = srcSheetIndex;
        this._dstSheetIndex = dstSheetIndex;
        this._mode = ShiftMode.SheetMove;
    }

    @Deprecated
    public static FormulaShifter createForRowShift(int externSheetIndex, String sheetName, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove) {
        return createForRowShift(externSheetIndex, sheetName, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove, SpreadsheetVersion.EXCEL97);
    }

    public static FormulaShifter createForRowShift(int externSheetIndex, String sheetName, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove, SpreadsheetVersion version) {
        return new FormulaShifter(externSheetIndex, sheetName, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove, ShiftMode.RowMove, version);
    }

    public static FormulaShifter createForRowCopy(int externSheetIndex, String sheetName, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove, SpreadsheetVersion version) {
        return new FormulaShifter(externSheetIndex, sheetName, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove, ShiftMode.RowCopy, version);
    }

    public static FormulaShifter createForSheetShift(int srcSheetIndex, int dstSheetIndex) {
        return new FormulaShifter(srcSheetIndex, dstSheetIndex);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(this._firstMovedIndex);
        sb.append(this._lastMovedIndex);
        sb.append(this._amountToMove);
        return sb.toString();
    }

    public boolean adjustFormula(Ptg[] ptgs, int currentExternSheetIx) {
        boolean refsWereChanged = false;
        for (int i = 0; i < ptgs.length; i++) {
            Ptg newPtg = adjustPtg(ptgs[i], currentExternSheetIx);
            if (newPtg != null) {
                refsWereChanged = true;
                ptgs[i] = newPtg;
            }
        }
        return refsWereChanged;
    }

    private Ptg adjustPtg(Ptg ptg, int currentExternSheetIx) {
        switch (1.$SwitchMap$org$apache$poi$ss$formula$FormulaShifter$ShiftMode[this._mode.ordinal()]) {
            case 1:
                return adjustPtgDueToRowMove(ptg, currentExternSheetIx);
            case 2:
                return adjustPtgDueToRowCopy(ptg);
            case 3:
                return adjustPtgDueToSheetMove(ptg);
            default:
                throw new IllegalStateException("Unsupported shift mode: " + this._mode);
        }
    }

    private Ptg adjustPtgDueToRowMove(Ptg ptg, int currentExternSheetIx) {
        if (ptg instanceof RefPtg) {
            if (currentExternSheetIx != this._externSheetIndex) {
                return null;
            }
            return rowMoveRefPtg((RefPtg) ptg);
        } else if (ptg instanceof Ref3DPtg) {
            Ref3DPtg rptg = (Ref3DPtg) ptg;
            if (this._externSheetIndex != rptg.getExternSheetIndex()) {
                return null;
            }
            return rowMoveRefPtg(rptg);
        } else if (ptg instanceof Ref3DPxg) {
            Ref3DPxg rpxg = (Ref3DPxg) ptg;
            if (rpxg.getExternalWorkbookNumber() > 0 || !this._sheetName.equals(rpxg.getSheetName())) {
                return null;
            }
            return rowMoveRefPtg(rpxg);
        } else if (ptg instanceof Area2DPtgBase) {
            if (currentExternSheetIx == this._externSheetIndex) {
                return rowMoveAreaPtg((Area2DPtgBase) ptg);
            }
            return ptg;
        } else if (ptg instanceof Area3DPtg) {
            Area3DPtg aptg = (Area3DPtg) ptg;
            if (this._externSheetIndex != aptg.getExternSheetIndex()) {
                return null;
            }
            return rowMoveAreaPtg(aptg);
        } else if (!(ptg instanceof Area3DPxg)) {
            return null;
        } else {
            Area3DPxg apxg = (Area3DPxg) ptg;
            if (apxg.getExternalWorkbookNumber() > 0 || !this._sheetName.equals(apxg.getSheetName())) {
                return null;
            }
            return rowMoveAreaPtg(apxg);
        }
    }

    private Ptg adjustPtgDueToRowCopy(Ptg ptg) {
        if (ptg instanceof RefPtg) {
            return rowCopyRefPtg((RefPtg) ptg);
        }
        if (ptg instanceof Ref3DPtg) {
            return rowCopyRefPtg((Ref3DPtg) ptg);
        }
        if (ptg instanceof Ref3DPxg) {
            return rowCopyRefPtg((Ref3DPxg) ptg);
        }
        if (ptg instanceof Area2DPtgBase) {
            return rowCopyAreaPtg((Area2DPtgBase) ptg);
        }
        if (ptg instanceof Area3DPtg) {
            return rowCopyAreaPtg((Area3DPtg) ptg);
        }
        if (ptg instanceof Area3DPxg) {
            return rowCopyAreaPtg((Area3DPxg) ptg);
        }
        return null;
    }

    private Ptg adjustPtgDueToSheetMove(Ptg ptg) {
        if (ptg instanceof Ref3DPtg) {
            Ref3DPtg ref = (Ref3DPtg) ptg;
            int oldSheetIndex = ref.getExternSheetIndex();
            if (oldSheetIndex < this._srcSheetIndex && oldSheetIndex < this._dstSheetIndex) {
                return null;
            }
            if (oldSheetIndex > this._srcSheetIndex && oldSheetIndex > this._dstSheetIndex) {
                return null;
            }
            if (oldSheetIndex == this._srcSheetIndex) {
                ref.setExternSheetIndex(this._dstSheetIndex);
                return ref;
            } else if (this._dstSheetIndex < this._srcSheetIndex) {
                ref.setExternSheetIndex(oldSheetIndex + 1);
                return ref;
            } else if (this._dstSheetIndex > this._srcSheetIndex) {
                ref.setExternSheetIndex(oldSheetIndex - 1);
                return ref;
            }
        }
        return null;
    }

    private Ptg rowMoveRefPtg(RefPtgBase rptg) {
        int refRow = rptg.getRow();
        if (this._firstMovedIndex > refRow || refRow > this._lastMovedIndex) {
            int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
            int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
            if (destLastRowIndex < refRow || refRow < destFirstRowIndex) {
                return null;
            }
            if (destFirstRowIndex <= refRow && refRow <= destLastRowIndex) {
                return createDeletedRef(rptg);
            }
            throw new IllegalStateException("Situation not covered: (" + this._firstMovedIndex + ", " + this._lastMovedIndex + ", " + this._amountToMove + ", " + refRow + ", " + refRow + ")");
        }
        rptg.setRow(this._amountToMove + refRow);
        return rptg;
    }

    private Ptg rowMoveAreaPtg(AreaPtgBase aptg) {
        int aFirstRow = aptg.getFirstRow();
        int aLastRow = aptg.getLastRow();
        if (this._firstMovedIndex > aFirstRow || aLastRow > this._lastMovedIndex) {
            int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
            int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
            if (aFirstRow >= this._firstMovedIndex || this._lastMovedIndex >= aLastRow) {
                if (this._firstMovedIndex > aFirstRow || aFirstRow > this._lastMovedIndex) {
                    if (this._firstMovedIndex > aLastRow || aLastRow > this._lastMovedIndex) {
                        if (destLastRowIndex < aFirstRow || aLastRow < destFirstRowIndex) {
                            return null;
                        }
                        if (destFirstRowIndex <= aFirstRow && aLastRow <= destLastRowIndex) {
                            return createDeletedRef(aptg);
                        }
                        if (aFirstRow <= destFirstRowIndex && destLastRowIndex <= aLastRow) {
                            return null;
                        }
                        if (destFirstRowIndex < aFirstRow && aFirstRow <= destLastRowIndex) {
                            aptg.setFirstRow(destLastRowIndex + 1);
                            return aptg;
                        } else if (destFirstRowIndex > aLastRow || aLastRow >= destLastRowIndex) {
                            throw new IllegalStateException("Situation not covered: (" + this._firstMovedIndex + ", " + this._lastMovedIndex + ", " + this._amountToMove + ", " + aFirstRow + ", " + aLastRow + ")");
                        } else {
                            aptg.setLastRow(destFirstRowIndex - 1);
                            return aptg;
                        }
                    } else if (this._amountToMove > 0) {
                        aptg.setLastRow(this._amountToMove + aLastRow);
                        return aptg;
                    } else if (destLastRowIndex < aFirstRow) {
                        return null;
                    } else {
                        int newLastRowIx = aLastRow + this._amountToMove;
                        if (destFirstRowIndex > aFirstRow) {
                            aptg.setLastRow(newLastRowIx);
                            return aptg;
                        }
                        int areaRemainingBottomRowIx = this._firstMovedIndex - 1;
                        if (destLastRowIndex < areaRemainingBottomRowIx) {
                            newLastRowIx = areaRemainingBottomRowIx;
                        }
                        aptg.setFirstRow(Math.min(aFirstRow, destFirstRowIndex));
                        aptg.setLastRow(newLastRowIx);
                        return aptg;
                    }
                } else if (this._amountToMove < 0) {
                    aptg.setFirstRow(this._amountToMove + aFirstRow);
                    return aptg;
                } else if (destFirstRowIndex > aLastRow) {
                    return null;
                } else {
                    int newFirstRowIx = aFirstRow + this._amountToMove;
                    if (destLastRowIndex < aLastRow) {
                        aptg.setFirstRow(newFirstRowIx);
                        return aptg;
                    }
                    int areaRemainingTopRowIx = this._lastMovedIndex + 1;
                    if (destFirstRowIndex > areaRemainingTopRowIx) {
                        newFirstRowIx = areaRemainingTopRowIx;
                    }
                    aptg.setFirstRow(newFirstRowIx);
                    aptg.setLastRow(Math.max(aLastRow, destLastRowIndex));
                    return aptg;
                }
            } else if (destFirstRowIndex < aFirstRow && aFirstRow <= destLastRowIndex) {
                aptg.setFirstRow(destLastRowIndex + 1);
                return aptg;
            } else if (destFirstRowIndex > aLastRow || aLastRow >= destLastRowIndex) {
                return null;
            } else {
                aptg.setLastRow(destFirstRowIndex - 1);
                return aptg;
            }
        }
        aptg.setFirstRow(this._amountToMove + aFirstRow);
        aptg.setLastRow(this._amountToMove + aLastRow);
        return aptg;
    }

    private Ptg rowCopyRefPtg(RefPtgBase rptg) {
        int refRow = rptg.getRow();
        if (!rptg.isRowRelative()) {
            return null;
        }
        int destRowIndex = this._firstMovedIndex + this._amountToMove;
        if (destRowIndex < 0 || this._version.getLastRowIndex() < destRowIndex) {
            return createDeletedRef(rptg);
        }
        rptg.setRow(this._amountToMove + refRow);
        return rptg;
    }

    private Ptg rowCopyAreaPtg(AreaPtgBase aptg) {
        boolean changed = false;
        int aFirstRow = aptg.getFirstRow();
        int aLastRow = aptg.getLastRow();
        if (aptg.isFirstRowRelative()) {
            int destFirstRowIndex = aFirstRow + this._amountToMove;
            if (destFirstRowIndex < 0 || this._version.getLastRowIndex() < destFirstRowIndex) {
                return createDeletedRef(aptg);
            }
            aptg.setFirstRow(destFirstRowIndex);
            changed = true;
        }
        if (aptg.isLastRowRelative()) {
            int destLastRowIndex = aLastRow + this._amountToMove;
            if (destLastRowIndex < 0 || this._version.getLastRowIndex() < destLastRowIndex) {
                return createDeletedRef(aptg);
            }
            aptg.setLastRow(destLastRowIndex);
            changed = true;
        }
        if (changed) {
            aptg.sortTopLeftToBottomRight();
        }
        return !changed ? null : aptg;
    }

    private static Ptg createDeletedRef(Ptg ptg) {
        if (ptg instanceof RefPtg) {
            return new RefErrorPtg();
        }
        if (ptg instanceof Ref3DPtg) {
            return new DeletedRef3DPtg(((Ref3DPtg) ptg).getExternSheetIndex());
        }
        if (ptg instanceof AreaPtg) {
            return new AreaErrPtg();
        }
        if (ptg instanceof Area3DPtg) {
            return new DeletedArea3DPtg(((Area3DPtg) ptg).getExternSheetIndex());
        }
        if (ptg instanceof Ref3DPxg) {
            Ref3DPxg pxg = (Ref3DPxg) ptg;
            return new Deleted3DPxg(pxg.getExternalWorkbookNumber(), pxg.getSheetName());
        } else if (ptg instanceof Area3DPxg) {
            Area3DPxg pxg2 = (Area3DPxg) ptg;
            return new Deleted3DPxg(pxg2.getExternalWorkbookNumber(), pxg2.getSheetName());
        } else {
            throw new IllegalArgumentException("Unexpected ref ptg class (" + ptg.getClass().getName() + ")");
        }
    }
}
