package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.util.NumberComparer;

public final class DStarRunner implements Function3Arg {
    private final DStarAlgorithmEnum algoType;

    public enum DStarAlgorithmEnum {
        DGET,
        DMIN
    }

    private enum operator {
        largerThan,
        largerEqualThan,
        smallerThan,
        smallerEqualThan,
        equal
    }

    public DStarRunner(DStarAlgorithmEnum algorithm) {
        this.algoType = algorithm;
    }

    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 3) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval database, ValueEval filterColumn, ValueEval conditionDatabase) {
        if (!(database instanceof AreaEval) || !(conditionDatabase instanceof AreaEval)) {
            return ErrorEval.VALUE_INVALID;
        }
        AreaEval db = (AreaEval) database;
        AreaEval cdb = (AreaEval) conditionDatabase;
        try {
            try {
                int fc = getColumnForName(OperandResolver.getSingleValue(filterColumn, srcRowIndex, srcColumnIndex), db);
                if (fc == -1) {
                    return ErrorEval.VALUE_INVALID;
                }
                IDStarAlgorithm algorithm;
                switch (this.algoType) {
                    case DGET:
                        algorithm = new DGet();
                        break;
                    case DMIN:
                        algorithm = new DMin();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected algorithm type " + this.algoType + " encountered.");
                }
                int height = db.getHeight();
                int row = 1;
                while (row < height) {
                    try {
                        if (fullfillsConditions(db, row, cdb) && !algorithm.processMatch(resolveReference(db, row, fc))) {
                            return algorithm.getResult();
                        }
                        row++;
                    } catch (EvaluationException e) {
                        return ErrorEval.VALUE_INVALID;
                    }
                }
                return algorithm.getResult();
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e3) {
            return e3.getErrorEval();
        }
    }

    private static int getColumnForName(ValueEval nameValueEval, AreaEval db) throws EvaluationException {
        return getColumnForString(db, OperandResolver.coerceValueToString(nameValueEval));
    }

    private static int getColumnForString(AreaEval db, String name) throws EvaluationException {
        int width = db.getWidth();
        for (int column = 0; column < width; column++) {
            ValueEval columnNameValueEval = resolveReference(db, 0, column);
            if (!(columnNameValueEval instanceof BlankEval) && !(columnNameValueEval instanceof ErrorEval) && name.equals(OperandResolver.coerceValueToString(columnNameValueEval))) {
                return column;
            }
        }
        return -1;
    }

    private static boolean fullfillsConditions(AreaEval db, int row, AreaEval cdb) throws EvaluationException {
        int height = cdb.getHeight();
        for (int conditionRow = 1; conditionRow < height; conditionRow++) {
            boolean matches = true;
            int width = cdb.getWidth();
            for (int column = 0; column < width; column++) {
                boolean columnCondition = true;
                ValueEval condition = resolveReference(cdb, conditionRow, column);
                if (!(condition instanceof BlankEval)) {
                    ValueEval targetHeader = resolveReference(cdb, 0, column);
                    if (targetHeader instanceof StringValueEval) {
                        if (getColumnForName(targetHeader, db) == -1) {
                            columnCondition = false;
                        }
                        if (columnCondition) {
                            if (!testNormalCondition(resolveReference(db, row, getColumnForName(targetHeader, db)), condition)) {
                                matches = false;
                                break;
                            }
                        } else if (OperandResolver.coerceValueToString(condition).isEmpty()) {
                            throw new EvaluationException(ErrorEval.VALUE_INVALID);
                        } else {
                            throw new NotImplementedException("D* function with formula conditions");
                        }
                    }
                    throw new EvaluationException(ErrorEval.VALUE_INVALID);
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private static boolean testNormalCondition(ValueEval value, ValueEval condition) throws EvaluationException {
        boolean z = true;
        if (condition instanceof StringEval) {
            String conditionString = ((StringEval) condition).getStringValue();
            String number;
            if (conditionString.startsWith("<")) {
                number = conditionString.substring(1);
                if (!number.startsWith("=")) {
                    return testNumericCondition(value, operator.smallerThan, number);
                }
                return testNumericCondition(value, operator.smallerEqualThan, number.substring(1));
            } else if (conditionString.startsWith(">")) {
                number = conditionString.substring(1);
                if (!number.startsWith("=")) {
                    return testNumericCondition(value, operator.largerThan, number);
                }
                return testNumericCondition(value, operator.largerEqualThan, number.substring(1));
            } else if (conditionString.startsWith("=")) {
                String stringOrNumber = conditionString.substring(1);
                if (stringOrNumber.isEmpty()) {
                    return value instanceof BlankEval;
                }
                boolean itsANumber;
                try {
                    Integer.parseInt(stringOrNumber);
                    itsANumber = true;
                } catch (NumberFormatException e) {
                    try {
                        Double.parseDouble(stringOrNumber);
                        itsANumber = true;
                    } catch (NumberFormatException e2) {
                        itsANumber = false;
                    }
                }
                if (itsANumber) {
                    return testNumericCondition(value, operator.equal, stringOrNumber);
                }
                return stringOrNumber.equals(value instanceof BlankEval ? "" : OperandResolver.coerceValueToString(value));
            } else if (conditionString.isEmpty()) {
                return value instanceof StringEval;
            } else {
                return (value instanceof BlankEval ? "" : OperandResolver.coerceValueToString(value)).startsWith(conditionString);
            }
        } else if (condition instanceof NumericValueEval) {
            double conditionNumber = ((NumericValueEval) condition).getNumberValue();
            Double valueNumber = getNumberFromValueEval(value);
            if (valueNumber == null) {
                return false;
            }
            if (conditionNumber != valueNumber.doubleValue()) {
                z = false;
            }
            return z;
        } else if (!(condition instanceof ErrorEval) || !(value instanceof ErrorEval)) {
            return false;
        } else {
            if (((ErrorEval) condition).getErrorCode() != ((ErrorEval) value).getErrorCode()) {
                z = false;
            }
            return z;
        }
    }

    private static boolean testNumericCondition(ValueEval valueEval, operator op, String condition) throws EvaluationException {
        if (!(valueEval instanceof NumericValueEval)) {
            return false;
        }
        double conditionValue;
        double value = ((NumericValueEval) valueEval).getNumberValue();
        try {
            conditionValue = (double) Integer.parseInt(condition);
        } catch (NumberFormatException e) {
            try {
                conditionValue = Double.parseDouble(condition);
            } catch (NumberFormatException e2) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
        }
        int result = NumberComparer.compare(value, conditionValue);
        switch (op) {
            case largerThan:
                if (result <= 0) {
                    return false;
                }
                return true;
            case largerEqualThan:
                if (result < 0) {
                    return false;
                }
                return true;
            case smallerThan:
                if (result >= 0) {
                    return false;
                }
                return true;
            case smallerEqualThan:
                if (result > 0) {
                    return false;
                }
                return true;
            case equal:
                if (result != 0) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    private static Double getNumberFromValueEval(ValueEval value) {
        Double d = null;
        if (value instanceof NumericValueEval) {
            return Double.valueOf(((NumericValueEval) value).getNumberValue());
        }
        if (!(value instanceof StringValueEval)) {
            return d;
        }
        try {
            return Double.valueOf(Double.parseDouble(((StringValueEval) value).getStringValue()));
        } catch (NumberFormatException e) {
            return d;
        }
    }

    private static ValueEval resolveReference(AreaEval db, int dbRow, int dbCol) {
        try {
            return OperandResolver.getSingleValue(db.getValue(dbRow, dbCol), db.getFirstRow() + dbRow, db.getFirstColumn() + dbCol);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
