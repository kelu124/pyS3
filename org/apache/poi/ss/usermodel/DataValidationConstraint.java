package org.apache.poi.ss.usermodel;

public interface DataValidationConstraint {

    public static final class OperatorType {
        public static final int BETWEEN = 0;
        public static final int EQUAL = 2;
        public static final int GREATER_OR_EQUAL = 6;
        public static final int GREATER_THAN = 4;
        public static final int IGNORED = 0;
        public static final int LESS_OR_EQUAL = 7;
        public static final int LESS_THAN = 5;
        public static final int NOT_BETWEEN = 1;
        public static final int NOT_EQUAL = 3;

        private OperatorType() {
        }

        public static void validateSecondArg(int comparisonOperator, String paramValue) {
            switch (comparisonOperator) {
                case 0:
                case 1:
                    if (paramValue == null) {
                        throw new IllegalArgumentException("expr2 must be supplied for 'between' comparisons");
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static final class ValidationType {
        public static final int ANY = 0;
        public static final int DATE = 4;
        public static final int DECIMAL = 2;
        public static final int FORMULA = 7;
        public static final int INTEGER = 1;
        public static final int LIST = 3;
        public static final int TEXT_LENGTH = 6;
        public static final int TIME = 5;

        private ValidationType() {
        }
    }

    String[] getExplicitListValues();

    String getFormula1();

    String getFormula2();

    int getOperator();

    int getValidationType();

    void setExplicitListValues(String[] strArr);

    void setFormula1(String str);

    void setFormula2(String str);

    void setOperator(int i);
}
