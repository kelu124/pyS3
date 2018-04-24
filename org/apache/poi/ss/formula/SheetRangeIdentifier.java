package org.apache.poi.ss.formula;

public class SheetRangeIdentifier extends SheetIdentifier {
    public NameIdentifier _lastSheetIdentifier;

    public SheetRangeIdentifier(String bookName, NameIdentifier firstSheetIdentifier, NameIdentifier lastSheetIdentifier) {
        super(bookName, firstSheetIdentifier);
        this._lastSheetIdentifier = lastSheetIdentifier;
    }

    public NameIdentifier getFirstSheetIdentifier() {
        return super.getSheetIdentifier();
    }

    public NameIdentifier getLastSheetIdentifier() {
        return this._lastSheetIdentifier;
    }

    protected void asFormulaString(StringBuffer sb) {
        super.asFormulaString(sb);
        sb.append(':');
        if (this._lastSheetIdentifier.isQuoted()) {
            sb.append("'").append(this._lastSheetIdentifier.getName()).append("'");
        } else {
            sb.append(this._lastSheetIdentifier.getName());
        }
    }
}
