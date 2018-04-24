package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class Rate implements Function {
    private static final POILogger LOG = POILogFactory.getLogger(Rate.class);

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length < 3) {
            return ErrorEval.VALUE_INVALID;
        }
        double future_val = 0.0d;
        double type = 0.0d;
        double estimate = 0.1d;
        try {
            ValueEval v1 = OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex);
            ValueEval v2 = OperandResolver.getSingleValue(args[1], srcRowIndex, srcColumnIndex);
            ValueEval v3 = OperandResolver.getSingleValue(args[2], srcRowIndex, srcColumnIndex);
            ValueEval v4 = null;
            if (args.length >= 4) {
                v4 = OperandResolver.getSingleValue(args[3], srcRowIndex, srcColumnIndex);
            }
            ValueEval v5 = null;
            if (args.length >= 5) {
                v5 = OperandResolver.getSingleValue(args[4], srcRowIndex, srcColumnIndex);
            }
            ValueEval v6 = null;
            if (args.length >= 6) {
                v6 = OperandResolver.getSingleValue(args[5], srcRowIndex, srcColumnIndex);
            }
            double periods = OperandResolver.coerceValueToDouble(v1);
            double payment = OperandResolver.coerceValueToDouble(v2);
            double present_val = OperandResolver.coerceValueToDouble(v3);
            if (args.length >= 4) {
                future_val = OperandResolver.coerceValueToDouble(v4);
            }
            if (args.length >= 5) {
                type = OperandResolver.coerceValueToDouble(v5);
            }
            if (args.length >= 6) {
                estimate = OperandResolver.coerceValueToDouble(v6);
            }
            double rate = calculateRate(periods, payment, present_val, future_val, type, estimate);
            checkValue(rate);
            return new NumberEval(rate);
        } catch (EvaluationException e) {
            LOG.log(7, new Object[]{"Can't evaluate rate function", e});
            return e.getErrorEval();
        }
    }

    private double calculateRate(double nper, double pmt, double pv, double fv, double type, double guess) {
        double y;
        double f = 0.0d;
        double rate = guess;
        if (Math.abs(rate) < 1.0E-7d) {
            y = (((1.0d + (nper * rate)) * pv) + (((1.0d + (rate * type)) * pmt) * nper)) + fv;
        } else {
            f = Math.exp(Math.log(1.0d + rate) * nper);
            y = ((pv * f) + ((((1.0d / rate) + type) * pmt) * (f - 1.0d))) + fv;
        }
        double y0 = ((pmt * nper) + pv) + fv;
        double y1 = ((pv * f) + ((((1.0d / rate) + type) * pmt) * (f - 1.0d))) + fv;
        double x0 = 0.0d;
        double i = 0.0d;
        double x1 = rate;
        while (Math.abs(y0 - y1) > 1.0E-7d && i < ((double) 20)) {
            rate = ((y1 * x0) - (y0 * x1)) / (y1 - y0);
            x0 = x1;
            x1 = rate;
            if (Math.abs(rate) < 1.0E-7d) {
                y = (((1.0d + (nper * rate)) * pv) + (((1.0d + (rate * type)) * pmt) * nper)) + fv;
            } else {
                f = Math.exp(Math.log(1.0d + rate) * nper);
                y = ((pv * f) + ((((1.0d / rate) + type) * pmt) * (f - 1.0d))) + fv;
            }
            y0 = y1;
            y1 = y;
            i += 1.0d;
        }
        return rate;
    }

    static final void checkValue(double result) throws EvaluationException {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    }
}
