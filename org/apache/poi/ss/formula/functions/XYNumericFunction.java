package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LookupUtils.ValueVector;

public abstract class XYNumericFunction extends Fixed2ArgFunction {

    protected interface Accumulator {
        double accumulate(double d, double d2);
    }

    private static abstract class ValueArray implements ValueVector {
        private final int _size;

        protected abstract ValueEval getItemInternal(int i);

        protected ValueArray(int size) {
            this._size = size;
        }

        public ValueEval getItem(int index) {
            if (index >= 0 && index <= this._size) {
                return getItemInternal(index);
            }
            throw new IllegalArgumentException("Specified index " + index + " is outside range (0.." + (this._size - 1) + ")");
        }

        public final int getSize() {
            return this._size;
        }
    }

    private static final class AreaValueArray extends ValueArray {
        private final TwoDEval _ae;
        private final int _width;

        public AreaValueArray(TwoDEval ae) {
            super(ae.getWidth() * ae.getHeight());
            this._ae = ae;
            this._width = ae.getWidth();
        }

        protected ValueEval getItemInternal(int index) {
            return this._ae.getValue(index / this._width, index % this._width);
        }
    }

    private static final class RefValueArray extends ValueArray {
        private final RefEval _ref;
        private final int _width;

        public RefValueArray(RefEval ref) {
            super(ref.getNumberOfSheets());
            this._ref = ref;
            this._width = ref.getNumberOfSheets();
        }

        protected ValueEval getItemInternal(int index) {
            return this._ref.getInnerValueEval((index % this._width) + this._ref.getFirstSheetIndex());
        }
    }

    private static final class SingleCellValueArray extends ValueArray {
        private final ValueEval _value;

        public SingleCellValueArray(ValueEval value) {
            super(1);
            this._value = value;
        }

        protected ValueEval getItemInternal(int index) {
            return this._value;
        }
    }

    protected abstract Accumulator createAccumulator();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            ValueVector vvX = createValueVector(arg0);
            ValueVector vvY = createValueVector(arg1);
            int size = vvX.getSize();
            if (size == 0 || vvY.getSize() != size) {
                return ErrorEval.NA;
            }
            double result = evaluateInternal(vvX, vvY, size);
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private double evaluateInternal(ValueVector x, ValueVector y, int size) throws EvaluationException {
        Accumulator acc = createAccumulator();
        ErrorEval firstXerr = null;
        ErrorEval firstYerr = null;
        boolean accumlatedSome = false;
        double result = 0.0d;
        for (int i = 0; i < size; i++) {
            ValueEval vx = x.getItem(i);
            ValueEval vy = y.getItem(i);
            if ((vx instanceof ErrorEval) && firstXerr == null) {
                firstXerr = (ErrorEval) vx;
            } else if ((vy instanceof ErrorEval) && firstYerr == null) {
                firstYerr = (ErrorEval) vy;
            } else if ((vx instanceof NumberEval) && (vy instanceof NumberEval)) {
                accumlatedSome = true;
                result += acc.accumulate(((NumberEval) vx).getNumberValue(), ((NumberEval) vy).getNumberValue());
            }
        }
        if (firstXerr != null) {
            throw new EvaluationException(firstXerr);
        } else if (firstYerr != null) {
            throw new EvaluationException(firstYerr);
        } else if (accumlatedSome) {
            return result;
        } else {
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    }

    private static ValueVector createValueVector(ValueEval arg) throws EvaluationException {
        if (arg instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) arg);
        } else if (arg instanceof TwoDEval) {
            return new AreaValueArray((TwoDEval) arg);
        } else {
            if (arg instanceof RefEval) {
                return new RefValueArray((RefEval) arg);
            }
            return new SingleCellValueArray(arg);
        }
    }
}
