package org.apache.poi.hssf.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.record.Record;

public final class WorkbookRecordList implements Iterable<Record> {
    private int backuppos = 0;
    private int bspos = 0;
    private int externsheetPos = 0;
    private int fontpos = 0;
    private int namepos = 0;
    private int palettepos = -1;
    private int protpos = 0;
    private List<Record> records = new ArrayList();
    private int supbookpos = 0;
    private int tabpos = 0;
    private int xfpos = 0;

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public int size() {
        return this.records.size();
    }

    public Record get(int i) {
        return (Record) this.records.get(i);
    }

    public void add(int pos, Record r) {
        this.records.add(pos, r);
        if (getProtpos() >= pos) {
            setProtpos(this.protpos + 1);
        }
        if (getBspos() >= pos) {
            setBspos(this.bspos + 1);
        }
        if (getTabpos() >= pos) {
            setTabpos(this.tabpos + 1);
        }
        if (getFontpos() >= pos) {
            setFontpos(this.fontpos + 1);
        }
        if (getXfpos() >= pos) {
            setXfpos(this.xfpos + 1);
        }
        if (getBackuppos() >= pos) {
            setBackuppos(this.backuppos + 1);
        }
        if (getNamepos() >= pos) {
            setNamepos(this.namepos + 1);
        }
        if (getSupbookpos() >= pos) {
            setSupbookpos(this.supbookpos + 1);
        }
        if (getPalettepos() != -1 && getPalettepos() >= pos) {
            setPalettepos(this.palettepos + 1);
        }
        if (getExternsheetPos() >= pos) {
            setExternsheetPos(getExternsheetPos() + 1);
        }
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public Iterator<Record> iterator() {
        return this.records.iterator();
    }

    public void remove(Object record) {
        int i = 0;
        for (Record r : this.records) {
            if (r == record) {
                remove(i);
                return;
            }
            i++;
        }
    }

    public void remove(int pos) {
        this.records.remove(pos);
        if (getProtpos() >= pos) {
            setProtpos(this.protpos - 1);
        }
        if (getBspos() >= pos) {
            setBspos(this.bspos - 1);
        }
        if (getTabpos() >= pos) {
            setTabpos(this.tabpos - 1);
        }
        if (getFontpos() >= pos) {
            setFontpos(this.fontpos - 1);
        }
        if (getXfpos() >= pos) {
            setXfpos(this.xfpos - 1);
        }
        if (getBackuppos() >= pos) {
            setBackuppos(this.backuppos - 1);
        }
        if (getNamepos() >= pos) {
            setNamepos(getNamepos() - 1);
        }
        if (getSupbookpos() >= pos) {
            setSupbookpos(getSupbookpos() - 1);
        }
        if (getPalettepos() != -1 && getPalettepos() >= pos) {
            setPalettepos(this.palettepos - 1);
        }
        if (getExternsheetPos() >= pos) {
            setExternsheetPos(getExternsheetPos() - 1);
        }
    }

    public int getProtpos() {
        return this.protpos;
    }

    public void setProtpos(int protpos) {
        this.protpos = protpos;
    }

    public int getBspos() {
        return this.bspos;
    }

    public void setBspos(int bspos) {
        this.bspos = bspos;
    }

    public int getTabpos() {
        return this.tabpos;
    }

    public void setTabpos(int tabpos) {
        this.tabpos = tabpos;
    }

    public int getFontpos() {
        return this.fontpos;
    }

    public void setFontpos(int fontpos) {
        this.fontpos = fontpos;
    }

    public int getXfpos() {
        return this.xfpos;
    }

    public void setXfpos(int xfpos) {
        this.xfpos = xfpos;
    }

    public int getBackuppos() {
        return this.backuppos;
    }

    public void setBackuppos(int backuppos) {
        this.backuppos = backuppos;
    }

    public int getPalettepos() {
        return this.palettepos;
    }

    public void setPalettepos(int palettepos) {
        this.palettepos = palettepos;
    }

    public int getNamepos() {
        return this.namepos;
    }

    public int getSupbookpos() {
        return this.supbookpos;
    }

    public void setNamepos(int namepos) {
        this.namepos = namepos;
    }

    public void setSupbookpos(int supbookpos) {
        this.supbookpos = supbookpos;
    }

    public int getExternsheetPos() {
        return this.externsheetPos;
    }

    public void setExternsheetPos(int externsheetPos) {
        this.externsheetPos = externsheetPos;
    }
}
