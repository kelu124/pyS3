package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Offset implements Function {
    private static final int LAST_VALID_COLUMN_INDEX = 255;
    private static final int LAST_VALID_ROW_INDEX = 65535;

    private static final class BaseRef {
        private final AreaEval _areaEval;
        private final int _firstColumnIndex;
        private final int _firstRowIndex;
        private final int _height;
        private final RefEval _refEval;
        private final int _width;

        public BaseRef(RefEval re) {
            this._refEval = re;
            this._areaEval = null;
            this._firstRowIndex = re.getRow();
            this._firstColumnIndex = re.getColumn();
            this._height = 1;
            this._width = 1;
        }

        public BaseRef(AreaEval ae) {
            this._refEval = null;
            this._areaEval = ae;
            this._firstRowIndex = ae.getFirstRow();
            this._firstColumnIndex = ae.getFirstColumn();
            this._height = (ae.getLastRow() - ae.getFirstRow()) + 1;
            this._width = (ae.getLastColumn() - ae.getFirstColumn()) + 1;
        }

        public int getWidth() {
            return this._width;
        }

        public int getHeight() {
            return this._height;
        }

        public int getFirstRowIndex() {
            return this._firstRowIndex;
        }

        public int getFirstColumnIndex() {
            return this._firstColumnIndex;
        }

        public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
            if (this._refEval == null) {
                return this._areaEval.offset(relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
            }
            return this._refEval.offset(relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
        }
    }

    static final class LinearOffsetRange {
        private final int _length;
        private final int _offset;

        public LinearOffsetRange(int offset, int length) {
            if (length == 0) {
                throw new RuntimeException("length may not be zero");
            }
            this._offset = offset;
            this._length = length;
        }

        public short getFirstIndex() {
            return (short) this._offset;
        }

        public short getLastIndex() {
            return (short) ((this._offset + this._length) - 1);
        }

        public LinearOffsetRange normaliseAndTranslate(int translationAmount) {
            if (this._length <= 0) {
                return new LinearOffsetRange(((this._offset + translationAmount) + this._length) + 1, -this._length);
            }
            if (translationAmount == 0) {
                return this;
            }
            return new LinearOffsetRange(this._offset + translationAmount, this._length);
        }

        public boolean isOutOfBounds(int lowValidIx, int highValidIx) {
            if (this._offset >= lowValidIx && getLastIndex() <= highValidIx) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName()).append(" [");
            sb.append(this._offset).append("...").append(getLastIndex());
            sb.append("]");
            return sb.toString();
        }
    }

    public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        if (args.length < 3 || args.length > 5) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            BaseRef baseRef = evaluateBaseRef(args[0]);
            int rowOffset = evaluateIntArg(args[1], srcCellRow, srcCellCol);
            int columnOffset = evaluateIntArg(args[2], srcCellRow, srcCellCol);
            int height = baseRef.getHeight();
            int width = baseRef.getWidth();
            switch (args.length) {
                case 4:
                    break;
                case 5:
                    if (!(args[4] instanceof MissingArgEval)) {
                        width = evaluateIntArg(args[4], srcCellRow, srcCellCol);
                        break;
                    }
                    break;
            }
            if (!(args[3] instanceof MissingArgEval)) {
                height = evaluateIntArg(args[3], srcCellRow, srcCellCol);
            }
            if (height == 0 || width == 0) {
                return ErrorEval.REF_INVALID;
            }
            return createOffset(baseRef, new LinearOffsetRange(rowOffset, height), new LinearOffsetRange(columnOffset, width));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static AreaEval createOffset(BaseRef baseRef, LinearOffsetRange orRow, LinearOffsetRange orCol) throws EvaluationException {
        LinearOffsetRange absRows = orRow.normaliseAndTranslate(baseRef.getFirstRowIndex());
        LinearOffsetRange absCols = orCol.normaliseAndTranslate(baseRef.getFirstColumnIndex());
        if (absRows.isOutOfBounds(0, 65535)) {
            throw new EvaluationException(ErrorEval.REF_INVALID);
        } else if (!absCols.isOutOfBounds(0, 255)) {
            return baseRef.offset(orRow.getFirstIndex(), orRow.getLastIndex(), orCol.getFirstIndex(), orCol.getLastIndex());
        } else {
            throw new EvaluationException(ErrorEval.REF_INVALID);
        }
    }

    private static BaseRef evaluateBaseRef(ValueEval eval) throws EvaluationException {
        if (eval instanceof RefEval) {
            return new BaseRef((RefEval) eval);
        }
        if (eval instanceof AreaEval) {
            return new BaseRef((AreaEval) eval);
        }
        if (eval instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) eval);
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }

    static int evaluateIntArg(ValueEval eval, int srcCellRow, int srcCellCol) throws EvaluationException {
        return OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(eval, srcCellRow, srcCellCol));
    }
}
