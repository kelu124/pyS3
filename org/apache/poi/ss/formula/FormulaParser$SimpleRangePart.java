package org.apache.poi.ss.formula;

import org.apache.poi.ss.util.CellReference;

final class FormulaParser$SimpleRangePart {
    private final String _rep;
    private final Type _type;

    private enum Type {
        CELL,
        ROW,
        COLUMN;

        public static Type get(boolean hasLetters, boolean hasDigits) {
            if (hasLetters) {
                return hasDigits ? CELL : COLUMN;
            } else {
                if (hasDigits) {
                    return ROW;
                }
                throw new IllegalArgumentException("must have either letters or numbers");
            }
        }
    }

    public FormulaParser$SimpleRangePart(String rep, boolean hasLetters, boolean hasNumbers) {
        this._rep = rep;
        this._type = Type.get(hasLetters, hasNumbers);
    }

    public boolean isCell() {
        return this._type == Type.CELL;
    }

    public boolean isRowOrColumn() {
        return this._type != Type.CELL;
    }

    public CellReference getCellReference() {
        if (this._type == Type.CELL) {
            return new CellReference(this._rep);
        }
        throw new IllegalStateException("Not applicable to this type");
    }

    public boolean isColumn() {
        return this._type == Type.COLUMN;
    }

    public boolean isRow() {
        return this._type == Type.ROW;
    }

    public String getRep() {
        return this._rep;
    }

    public boolean isCompatibleForArea(FormulaParser$SimpleRangePart part2) {
        return this._type == part2._type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(this._rep);
        sb.append("]");
        return sb.toString();
    }
}
