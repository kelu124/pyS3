package org.apache.poi.ss.format;

import java.util.HashMap;
import java.util.Map;

public abstract class CellFormatCondition {
    private static final int EQ = 4;
    private static final int GE = 3;
    private static final int GT = 2;
    private static final int LE = 1;
    private static final int LT = 0;
    private static final int NE = 5;
    private static final Map<String, Integer> TESTS = new HashMap();

    public abstract boolean pass(double d);

    static {
        TESTS.put("<", Integer.valueOf(0));
        TESTS.put("<=", Integer.valueOf(1));
        TESTS.put(">", Integer.valueOf(2));
        TESTS.put(">=", Integer.valueOf(3));
        TESTS.put("=", Integer.valueOf(4));
        TESTS.put("==", Integer.valueOf(4));
        TESTS.put("!=", Integer.valueOf(5));
        TESTS.put("<>", Integer.valueOf(5));
    }

    public static CellFormatCondition getInstance(String opString, String constStr) {
        if (TESTS.containsKey(opString)) {
            int test = ((Integer) TESTS.get(opString)).intValue();
            final double c = Double.parseDouble(constStr);
            switch (test) {
                case 0:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value < c;
                        }
                    };
                case 1:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value <= c;
                        }
                    };
                case 2:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value > c;
                        }
                    };
                case 3:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value >= c;
                        }
                    };
                case 4:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value == c;
                        }
                    };
                case 5:
                    return new CellFormatCondition() {
                        public boolean pass(double value) {
                            return value != c;
                        }
                    };
                default:
                    throw new IllegalArgumentException("Cannot create for test number " + test + "(\"" + opString + "\")");
            }
        }
        throw new IllegalArgumentException("Unknown test: " + opString);
    }
}
