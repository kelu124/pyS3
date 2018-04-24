package org.apache.poi.ss.formula.eval.forked;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

final class ForkedEvaluationCell implements EvaluationCell {
    private boolean _booleanValue;
    private CellType _cellType;
    private int _errorValue;
    private final EvaluationCell _masterCell;
    private double _numberValue;
    private final EvaluationSheet _sheet;
    private String _stringValue;

    static /* synthetic */ class C11291 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BLANK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ForkedEvaluationCell(ForkedEvaluationSheet sheet, EvaluationCell masterCell) {
        this._sheet = sheet;
        this._masterCell = masterCell;
        setValue(BlankEval.instance);
    }

    public Object getIdentityKey() {
        return this._masterCell.getIdentityKey();
    }

    public void setValue(ValueEval value) {
        Class<? extends ValueEval> cls = value.getClass();
        if (cls == NumberEval.class) {
            this._cellType = CellType.NUMERIC;
            this._numberValue = ((NumberEval) value).getNumberValue();
        } else if (cls == StringEval.class) {
            this._cellType = CellType.STRING;
            this._stringValue = ((StringEval) value).getStringValue();
        } else if (cls == BoolEval.class) {
            this._cellType = CellType.BOOLEAN;
            this._booleanValue = ((BoolEval) value).getBooleanValue();
        } else if (cls == ErrorEval.class) {
            this._cellType = CellType.ERROR;
            this._errorValue = ((ErrorEval) value).getErrorCode();
        } else if (cls == BlankEval.class) {
            this._cellType = CellType.BLANK;
        } else {
            throw new IllegalArgumentException("Unexpected value class (" + cls.getName() + ")");
        }
    }

    public void copyValue(Cell destCell) {
        switch (C11291.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                destCell.setCellType(CellType.BLANK);
                return;
            case 2:
                destCell.setCellValue(this._numberValue);
                return;
            case 3:
                destCell.setCellValue(this._booleanValue);
                return;
            case 4:
                destCell.setCellValue(this._stringValue);
                return;
            case 5:
                destCell.setCellErrorValue((byte) this._errorValue);
                return;
            default:
                throw new IllegalStateException("Unexpected data type (" + this._cellType + ")");
        }
    }

    private void checkCellType(CellType expectedCellType) {
        if (this._cellType != expectedCellType) {
            throw new RuntimeException("Wrong data type (" + this._cellType + ")");
        }
    }

    public int getCellType() {
        return this._cellType.getCode();
    }

    public CellType getCellTypeEnum() {
        return this._cellType;
    }

    public boolean getBooleanCellValue() {
        checkCellType(CellType.BOOLEAN);
        return this._booleanValue;
    }

    public int getErrorCellValue() {
        checkCellType(CellType.ERROR);
        return this._errorValue;
    }

    public double getNumericCellValue() {
        checkCellType(CellType.NUMERIC);
        return this._numberValue;
    }

    public String getStringCellValue() {
        checkCellType(CellType.STRING);
        return this._stringValue;
    }

    public EvaluationSheet getSheet() {
        return this._sheet;
    }

    public int getRowIndex() {
        return this._masterCell.getRowIndex();
    }

    public int getColumnIndex() {
        return this._masterCell.getColumnIndex();
    }

    public int getCachedFormulaResultType() {
        return this._masterCell.getCachedFormulaResultType();
    }

    public CellType getCachedFormulaResultTypeEnum() {
        return this._masterCell.getCachedFormulaResultTypeEnum();
    }
}
