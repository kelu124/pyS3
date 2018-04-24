package org.apache.poi.hssf.usermodel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.util.Internal;

public class HSSFHyperlink implements Hyperlink {
    protected final HyperlinkType link_type;
    protected final HyperlinkRecord record;

    @Internal(since = "3.15 beta 3")
    protected HSSFHyperlink(int type) {
        this(HyperlinkType.forInt(type));
    }

    @Internal(since = "3.15 beta 3")
    protected HSSFHyperlink(HyperlinkType type) {
        this.link_type = type;
        this.record = new HyperlinkRecord();
        switch (1.$SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[type.ordinal()]) {
            case 1:
            case 2:
                this.record.newUrlLink();
                return;
            case 3:
                this.record.newFileLink();
                return;
            case 4:
                this.record.newDocumentLink();
                return;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    protected HSSFHyperlink(HyperlinkRecord record) {
        this.record = record;
        this.link_type = getType(record);
    }

    private static HyperlinkType getType(HyperlinkRecord record) {
        if (record.isFileLink()) {
            return HyperlinkType.FILE;
        }
        if (record.isDocumentLink()) {
            return HyperlinkType.DOCUMENT;
        }
        if (record.getAddress() == null || !record.getAddress().startsWith("mailto:")) {
            return HyperlinkType.URL;
        }
        return HyperlinkType.EMAIL;
    }

    protected HSSFHyperlink(Hyperlink other) {
        if (other instanceof HSSFHyperlink) {
            this.record = ((HSSFHyperlink) other).record.clone();
            this.link_type = getType(this.record);
            return;
        }
        this.link_type = other.getTypeEnum();
        this.record = new HyperlinkRecord();
        setFirstRow(other.getFirstRow());
        setFirstColumn(other.getFirstColumn());
        setLastRow(other.getLastRow());
        setLastColumn(other.getLastColumn());
    }

    public int getFirstRow() {
        return this.record.getFirstRow();
    }

    public void setFirstRow(int row) {
        this.record.setFirstRow(row);
    }

    public int getLastRow() {
        return this.record.getLastRow();
    }

    public void setLastRow(int row) {
        this.record.setLastRow(row);
    }

    public int getFirstColumn() {
        return this.record.getFirstColumn();
    }

    public void setFirstColumn(int col) {
        this.record.setFirstColumn((short) col);
    }

    public int getLastColumn() {
        return this.record.getLastColumn();
    }

    public void setLastColumn(int col) {
        this.record.setLastColumn((short) col);
    }

    public String getAddress() {
        return this.record.getAddress();
    }

    public String getTextMark() {
        return this.record.getTextMark();
    }

    public void setTextMark(String textMark) {
        this.record.setTextMark(textMark);
    }

    public String getShortFilename() {
        return this.record.getShortFilename();
    }

    public void setShortFilename(String shortFilename) {
        this.record.setShortFilename(shortFilename);
    }

    public void setAddress(String address) {
        this.record.setAddress(address);
    }

    public String getLabel() {
        return this.record.getLabel();
    }

    public void setLabel(String label) {
        this.record.setLabel(label);
    }

    public int getType() {
        return this.link_type.getCode();
    }

    public HyperlinkType getTypeEnum() {
        return this.link_type;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HSSFHyperlink)) {
            return false;
        }
        if (this.record != ((HSSFHyperlink) other).record) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.record.hashCode();
    }
}
