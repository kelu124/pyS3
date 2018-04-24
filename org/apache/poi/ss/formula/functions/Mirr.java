package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;

public class Mirr extends MultiOperandNumericFunction {
    public Mirr() {
        super(false, false);
    }

    protected int getMaxNumOperands() {
        return 3;
    }

    protected double evaluate(double[] values) throws EvaluationException {
        double financeRate = values[values.length - 1];
        double reinvestRate = values[values.length - 2];
        double[] mirrValues = new double[(values.length - 2)];
        System.arraycopy(values, 0, mirrValues, 0, mirrValues.length);
        boolean mirrValuesAreAllNegatives = true;
        for (double mirrValue : mirrValues) {
            mirrValuesAreAllNegatives &= mirrValue < 0.0d ? 1 : 0;
        }
        if (mirrValuesAreAllNegatives) {
            return -1.0d;
        }
        boolean mirrValuesAreAllPositives = true;
        for (double mirrValue2 : mirrValues) {
            mirrValuesAreAllPositives &= mirrValue2 > 0.0d ? 1 : 0;
        }
        if (!mirrValuesAreAllPositives) {
            return mirr(mirrValues, financeRate, reinvestRate);
        }
        throw new EvaluationException(ErrorEval.DIV_ZERO);
    }

    private static double mirr(double[] in, double financeRate, double reinvestRate) {
        int numOfYears = in.length - 1;
        double pv = 0.0d;
        double fv = 0.0d;
        double[] arr$ = in;
        int len$ = arr$.length;
        int i$ = 0;
        int indexN = 0;
        while (i$ < len$) {
            int indexN2;
            double anIn = arr$[i$];
            if (anIn < 0.0d) {
                indexN2 = indexN + 1;
                pv += anIn / Math.pow((1.0d + financeRate) + reinvestRate, (double) indexN);
            } else {
                indexN2 = indexN;
            }
            i$++;
            indexN = indexN2;
        }
        arr$ = in;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            anIn = arr$[i$];
            if (anIn > 0.0d) {
                indexN2 = indexN + 1;
                fv += Math.pow(1.0d + financeRate, (double) (numOfYears - indexN)) * anIn;
            } else {
                indexN2 = indexN;
            }
            i$++;
            indexN = indexN2;
        }
        if (fv == 0.0d || pv == 0.0d) {
            return 0.0d;
        }
        return Math.pow((-fv) / pv, 1.0d / ((double) numOfYears)) - 1.0d;
    }
}
