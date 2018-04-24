package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class MultiOperandNumericFunction implements Function {
    private static final int DEFAULT_MAX_NUM_OPERANDS = 30;
    static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    private final boolean _isBlankCounted;
    private final boolean _isReferenceBoolCounted;

    private static class DoubleList {
        private double[] _array = new double[8];
        private int _count = 0;

        public double[] toArray() {
            if (this._count < 1) {
                return MultiOperandNumericFunction.EMPTY_DOUBLE_ARRAY;
            }
            double[] result = new double[this._count];
            System.arraycopy(this._array, 0, result, 0, this._count);
            return result;
        }

        private void ensureCapacity(int reqSize) {
            if (reqSize > this._array.length) {
                double[] newArr = new double[((reqSize * 3) / 2)];
                System.arraycopy(this._array, 0, newArr, 0, this._count);
                this._array = newArr;
            }
        }

        public void add(double value) {
            ensureCapacity(this._count + 1);
            this._array[this._count] = value;
            this._count++;
        }
    }

    protected abstract double evaluate(double[] dArr) throws EvaluationException;

    protected MultiOperandNumericFunction(boolean isReferenceBoolCounted, boolean isBlankCounted) {
        this._isReferenceBoolCounted = isReferenceBoolCounted;
        this._isBlankCounted = isBlankCounted;
    }

    public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        try {
            double d = evaluate(getNumberArray(args));
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval(d);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    protected int getMaxNumOperands() {
        return 30;
    }

    protected final double[] getNumberArray(ValueEval[] operands) throws EvaluationException {
        if (operands.length > getMaxNumOperands()) {
            throw EvaluationException.invalidValue();
        }
        DoubleList retval = new DoubleList();
        for (ValueEval collectValues : operands) {
            collectValues(collectValues, retval);
        }
        return retval.toArray();
    }

    public boolean isSubtotalCounted() {
        return true;
    }

    private void collectValues(ValueEval operand, DoubleList temp) throws EvaluationException {
        int sIx;
        int width;
        int height;
        int rrIx;
        int rcIx;
        ValueEval ve;
        if (operand instanceof ThreeDEval) {
            ThreeDEval ae = (ThreeDEval) operand;
            for (sIx = ae.getFirstSheetIndex(); sIx <= ae.getLastSheetIndex(); sIx++) {
                width = ae.getWidth();
                height = ae.getHeight();
                rrIx = 0;
                while (rrIx < height) {
                    rcIx = 0;
                    while (rcIx < width) {
                        ve = ae.getValue(sIx, rrIx, rcIx);
                        if (isSubtotalCounted() || !ae.isSubTotal(rrIx, rcIx)) {
                            collectValue(ve, true, temp);
                        }
                        rcIx++;
                    }
                    rrIx++;
                }
            }
        } else if (operand instanceof TwoDEval) {
            TwoDEval ae2 = (TwoDEval) operand;
            width = ae2.getWidth();
            height = ae2.getHeight();
            rrIx = 0;
            while (rrIx < height) {
                rcIx = 0;
                while (rcIx < width) {
                    ve = ae2.getValue(rrIx, rcIx);
                    if (isSubtotalCounted() || !ae2.isSubTotal(rrIx, rcIx)) {
                        collectValue(ve, true, temp);
                    }
                    rcIx++;
                }
                rrIx++;
            }
        } else if (operand instanceof RefEval) {
            RefEval re = (RefEval) operand;
            for (sIx = re.getFirstSheetIndex(); sIx <= re.getLastSheetIndex(); sIx++) {
                collectValue(re.getInnerValueEval(sIx), true, temp);
            }
        } else {
            collectValue(operand, false, temp);
        }
    }

    private void collectValue(ValueEval ve, boolean isViaReference, DoubleList temp) throws EvaluationException {
        if (ve == null) {
            throw new IllegalArgumentException("ve must not be null");
        } else if (ve instanceof BoolEval) {
            if (!isViaReference || this._isReferenceBoolCounted) {
                temp.add(((BoolEval) ve).getNumberValue());
            }
        } else if (ve instanceof NumericValueEval) {
            temp.add(((NumericValueEval) ve).getNumberValue());
        } else if (ve instanceof StringValueEval) {
            if (!isViaReference) {
                Double d = OperandResolver.parseDouble(((StringValueEval) ve).getStringValue());
                if (d == null) {
                    throw new EvaluationException(ErrorEval.VALUE_INVALID);
                }
                temp.add(d.doubleValue());
            }
        } else if (ve instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) ve);
        } else if (ve != BlankEval.instance) {
            throw new RuntimeException("Invalid ValueEval type passed for conversion: (" + ve.getClass() + ")");
        } else if (this._isBlankCounted) {
            temp.add(0.0d);
        }
    }
}
