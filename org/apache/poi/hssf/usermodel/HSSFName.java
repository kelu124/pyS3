package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.NameCommentRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Name;

public final class HSSFName implements Name {
    private HSSFWorkbook _book;
    private NameCommentRecord _commentRec;
    private NameRecord _definedNameRec;

    HSSFName(HSSFWorkbook book, NameRecord name) {
        this(book, name, null);
    }

    HSSFName(HSSFWorkbook book, NameRecord name, NameCommentRecord comment) {
        this._book = book;
        this._definedNameRec = name;
        this._commentRec = comment;
    }

    public String getSheetName() {
        return this._book.getWorkbook().findSheetFirstNameFromExternSheet(this._definedNameRec.getExternSheetNumber());
    }

    public String getNameName() {
        return this._definedNameRec.getNameText();
    }

    public void setNameName(String nameName) {
        validateName(nameName);
        InternalWorkbook wb = this._book.getWorkbook();
        this._definedNameRec.setNameText(nameName);
        int sheetNumber = this._definedNameRec.getSheetNumber();
        for (int i = wb.getNumNames() - 1; i >= 0; i--) {
            NameRecord rec = wb.getNameRecord(i);
            if (rec != this._definedNameRec && rec.getNameText().equalsIgnoreCase(nameName) && sheetNumber == rec.getSheetNumber()) {
                String msg = "The " + (sheetNumber == 0 ? "workbook" : "sheet") + " already contains this name: " + nameName;
                this._definedNameRec.setNameText(nameName + "(2)");
                throw new IllegalArgumentException(msg);
            }
        }
        if (this._commentRec != null) {
            this._commentRec.setNameText(nameName);
            this._book.getWorkbook().updateNameCommentRecordCache(this._commentRec);
        }
    }

    private static void validateName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        boolean characterIsValid;
        char c = name.charAt(0);
        String allowedSymbols = "_";
        if (Character.isLetter(c) || allowedSymbols.indexOf(c) != -1) {
            characterIsValid = true;
        } else {
            characterIsValid = false;
        }
        if (characterIsValid) {
            allowedSymbols = "_\\";
            char[] arr$ = name.toCharArray();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                char ch = arr$[i$];
                if (Character.isLetterOrDigit(ch) || allowedSymbols.indexOf(ch) != -1) {
                    characterIsValid = true;
                } else {
                    characterIsValid = false;
                }
                if (characterIsValid) {
                    i$++;
                } else {
                    throw new IllegalArgumentException("Invalid name: '" + name + "'");
                }
            }
            return;
        }
        throw new IllegalArgumentException("Invalid name: '" + name + "': first character must be underscore or a letter");
    }

    public void setRefersToFormula(String formulaText) {
        this._definedNameRec.setNameDefinition(HSSFFormulaParser.parse(formulaText, this._book, FormulaType.NAMEDRANGE, getSheetIndex()));
    }

    public String getRefersToFormula() {
        if (this._definedNameRec.isFunctionName()) {
            throw new IllegalStateException("Only applicable to named ranges");
        }
        Ptg[] ptgs = this._definedNameRec.getNameDefinition();
        if (ptgs.length < 1) {
            return null;
        }
        return HSSFFormulaParser.toFormulaString(this._book, ptgs);
    }

    void setNameDefinition(Ptg[] ptgs) {
        this._definedNameRec.setNameDefinition(ptgs);
    }

    public boolean isDeleted() {
        return Ptg.doesFormulaReferToDeletedCell(this._definedNameRec.getNameDefinition());
    }

    public boolean isFunctionName() {
        return this._definedNameRec.isFunctionName();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(this._definedNameRec.getNameText());
        sb.append("]");
        return sb.toString();
    }

    public void setSheetIndex(int index) {
        int lastSheetIx = this._book.getNumberOfSheets() - 1;
        if (index < -1 || index > lastSheetIx) {
            throw new IllegalArgumentException("Sheet index (" + index + ") is out of range" + (lastSheetIx == -1 ? "" : " (0.." + lastSheetIx + ")"));
        }
        this._definedNameRec.setSheetNumber(index + 1);
    }

    public int getSheetIndex() {
        return this._definedNameRec.getSheetNumber() - 1;
    }

    public String getComment() {
        if (this._commentRec == null || this._commentRec.getCommentText() == null || this._commentRec.getCommentText().length() <= 0) {
            return this._definedNameRec.getDescriptionText();
        }
        return this._commentRec.getCommentText();
    }

    public void setComment(String comment) {
        this._definedNameRec.setDescriptionText(comment);
        if (this._commentRec != null) {
            this._commentRec.setCommentText(comment);
        }
    }

    public void setFunction(boolean value) {
        this._definedNameRec.setFunction(value);
    }
}
