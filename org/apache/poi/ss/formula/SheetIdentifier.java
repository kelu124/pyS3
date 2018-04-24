package org.apache.poi.ss.formula;

public class SheetIdentifier {
    public String _bookName;
    public NameIdentifier _sheetIdentifier;

    public SheetIdentifier(String bookName, NameIdentifier sheetIdentifier) {
        this._bookName = bookName;
        this._sheetIdentifier = sheetIdentifier;
    }

    public String getBookName() {
        return this._bookName;
    }

    public NameIdentifier getSheetIdentifier() {
        return this._sheetIdentifier;
    }

    protected void asFormulaString(StringBuffer sb) {
        if (this._bookName != null) {
            sb.append(" [").append(this._sheetIdentifier.getName()).append("]");
        }
        if (this._sheetIdentifier.isQuoted()) {
            sb.append("'").append(this._sheetIdentifier.getName()).append("'");
        } else {
            sb.append(this._sheetIdentifier.getName());
        }
    }

    public String asFormulaString() {
        StringBuffer sb = new StringBuffer(32);
        asFormulaString(sb);
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        asFormulaString(sb);
        sb.append("]");
        return sb.toString();
    }
}
