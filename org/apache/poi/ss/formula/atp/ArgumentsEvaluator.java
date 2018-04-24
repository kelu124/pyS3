package org.apache.poi.ss.formula.atp;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.formula.eval.AreaEvalBase;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;

final class ArgumentsEvaluator {
    public static final ArgumentsEvaluator instance = new ArgumentsEvaluator();

    private ArgumentsEvaluator() {
    }

    public double evaluateDateArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short) srcCellCol);
        if (!(ve instanceof StringEval)) {
            return OperandResolver.coerceValueToDouble(ve);
        }
        String strVal = ((StringEval) ve).getStringValue();
        Double dVal = OperandResolver.parseDouble(strVal);
        if (dVal != null) {
            return dVal.doubleValue();
        }
        return DateUtil.getExcelDate(DateParser.parseDate(strVal), false);
    }

    public double[] evaluateDatesArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        if (arg == null) {
            return new double[0];
        }
        if (arg instanceof StringEval) {
            return new double[]{evaluateDateArg(arg, srcCellRow, srcCellCol)};
        } else if (arg instanceof AreaEvalBase) {
            int i;
            List<Double> valuesList = new ArrayList();
            AreaEvalBase area = (AreaEvalBase) arg;
            for (i = area.getFirstRow(); i <= area.getLastRow(); i++) {
                for (int j = area.getFirstColumn(); j <= area.getLastColumn(); j++) {
                    valuesList.add(Double.valueOf(evaluateDateArg(area.getAbsoluteValue(i, j), i, j)));
                }
            }
            double[] values = new double[valuesList.size()];
            for (i = 0; i < valuesList.size(); i++) {
                values[i] = ((Double) valuesList.get(i)).doubleValue();
            }
            return values;
        } else {
            return new double[]{OperandResolver.coerceValueToDouble(arg)};
        }
    }

    public double evaluateNumberArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        if (arg == null) {
            return 0.0d;
        }
        return OperandResolver.coerceValueToDouble(arg);
    }
}
