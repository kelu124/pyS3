package org.apache.poi.ss.formula;

public interface IStabilityClassifier {
    public static final IStabilityClassifier TOTALLY_IMMUTABLE = new C11161();

    static class C11161 implements IStabilityClassifier {
        C11161() {
        }

        public boolean isCellFinal(int sheetIndex, int rowIndex, int columnIndex) {
            return true;
        }
    }

    boolean isCellFinal(int i, int i2, int i3);
}
