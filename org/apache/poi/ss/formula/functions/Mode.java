package org.apache.poi.ss.formula.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Mode implements Function {
    public static double evaluate(double[] v) throws EvaluationException {
        if (v.length < 2) {
            throw new EvaluationException(ErrorEval.NA);
        }
        int i;
        int[] counts = new int[v.length];
        Arrays.fill(counts, 1);
        int iSize = v.length;
        for (i = 0; i < iSize; i++) {
            int jSize = v.length;
            for (int j = i + 1; j < jSize; j++) {
                if (v[i] == v[j]) {
                    counts[i] = counts[i] + 1;
                }
            }
        }
        double maxv = 0.0d;
        int maxc = 0;
        iSize = counts.length;
        for (i = 0; i < iSize; i++) {
            if (counts[i] > maxc) {
                maxv = v[i];
                maxc = counts[i];
            }
        }
        if (maxc > 1) {
            return maxv;
        }
        throw new EvaluationException(ErrorEval.NA);
    }

    public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        try {
            int i;
            List<Double> temp = new ArrayList();
            for (ValueEval collectValues : args) {
                collectValues(collectValues, temp);
            }
            double[] values = new double[temp.size()];
            for (i = 0; i < values.length; i++) {
                values[i] = ((Double) temp.get(i)).doubleValue();
            }
            return new NumberEval(evaluate(values));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static void collectValues(ValueEval arg, List<Double> temp) throws EvaluationException {
        if (arg instanceof TwoDEval) {
            TwoDEval ae = (TwoDEval) arg;
            int width = ae.getWidth();
            int height = ae.getHeight();
            for (int rrIx = 0; rrIx < height; rrIx++) {
                for (int rcIx = 0; rcIx < width; rcIx++) {
                    collectValue(ae.getValue(rrIx, rcIx), temp, false);
                }
            }
        } else if (arg instanceof RefEval) {
            RefEval re = (RefEval) arg;
            int firstSheetIndex = re.getFirstSheetIndex();
            int lastSheetIndex = re.getLastSheetIndex();
            for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
                collectValue(re.getInnerValueEval(sIx), temp, true);
            }
        } else {
            collectValue(arg, temp, true);
        }
    }

    private static void collectValue(ValueEval arg, List<Double> temp, boolean mustBeNumber) throws EvaluationException {
        if (arg instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) arg);
        } else if (arg == BlankEval.instance || (arg instanceof BoolEval) || (arg instanceof StringEval)) {
            if (mustBeNumber) {
                throw EvaluationException.invalidValue();
            }
        } else if (arg instanceof NumberEval) {
            temp.add(new Double(((NumberEval) arg).getNumberValue()));
        } else {
            throw new RuntimeException("Unexpected value type (" + arg.getClass().getName() + ")");
        }
    }
}
