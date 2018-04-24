package org.apache.poi.ss.formula.ptg;

public abstract class OperandPtg extends Ptg implements Cloneable {
    public final boolean isBaseToken() {
        return false;
    }

    public final OperandPtg copy() {
        try {
            return (OperandPtg) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
