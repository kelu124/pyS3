package org.apache.poi.ss.usermodel;

public interface ConditionalFormattingThreshold {

    public enum RangeType {
        NUMBER(1, "num"),
        MIN(2, "min"),
        MAX(3, "max"),
        PERCENT(4, "percent"),
        PERCENTILE(5, "percentile"),
        UNALLOCATED(6, null),
        FORMULA(7, "formula");
        
        public final int id;
        public final String name;

        public String toString() {
            return this.id + " - " + this.name;
        }

        public static RangeType byId(int id) {
            return values()[id - 1];
        }

        public static RangeType byName(String name) {
            for (RangeType t : values()) {
                if (t.name.equals(name)) {
                    return t;
                }
            }
            return null;
        }

        private RangeType(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    String getFormula();

    RangeType getRangeType();

    Double getValue();

    void setFormula(String str);

    void setRangeType(RangeType rangeType);

    void setValue(Double d);
}
