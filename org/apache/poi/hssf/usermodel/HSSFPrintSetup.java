package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.PrintSetupRecord;
import org.apache.poi.ss.usermodel.PrintSetup;

public class HSSFPrintSetup implements PrintSetup {
    PrintSetupRecord printSetupRecord;

    protected HSSFPrintSetup(PrintSetupRecord printSetupRecord) {
        this.printSetupRecord = printSetupRecord;
    }

    public void setPaperSize(short size) {
        this.printSetupRecord.setPaperSize(size);
    }

    public void setScale(short scale) {
        this.printSetupRecord.setScale(scale);
    }

    public void setPageStart(short start) {
        this.printSetupRecord.setPageStart(start);
    }

    public void setFitWidth(short width) {
        this.printSetupRecord.setFitWidth(width);
    }

    public void setFitHeight(short height) {
        this.printSetupRecord.setFitHeight(height);
    }

    public void setOptions(short options) {
        this.printSetupRecord.setOptions(options);
    }

    public void setLeftToRight(boolean ltor) {
        this.printSetupRecord.setLeftToRight(ltor);
    }

    public void setLandscape(boolean ls) {
        this.printSetupRecord.setLandscape(!ls);
    }

    public void setValidSettings(boolean valid) {
        this.printSetupRecord.setValidSettings(valid);
    }

    public void setNoColor(boolean mono) {
        this.printSetupRecord.setNoColor(mono);
    }

    public void setDraft(boolean d) {
        this.printSetupRecord.setDraft(d);
    }

    public void setNotes(boolean printnotes) {
        this.printSetupRecord.setNotes(printnotes);
    }

    public void setNoOrientation(boolean orientation) {
        this.printSetupRecord.setNoOrientation(orientation);
    }

    public void setUsePage(boolean page) {
        this.printSetupRecord.setUsePage(page);
    }

    public void setHResolution(short resolution) {
        this.printSetupRecord.setHResolution(resolution);
    }

    public void setVResolution(short resolution) {
        this.printSetupRecord.setVResolution(resolution);
    }

    public void setHeaderMargin(double headermargin) {
        this.printSetupRecord.setHeaderMargin(headermargin);
    }

    public void setFooterMargin(double footermargin) {
        this.printSetupRecord.setFooterMargin(footermargin);
    }

    public void setCopies(short copies) {
        this.printSetupRecord.setCopies(copies);
    }

    public short getPaperSize() {
        return this.printSetupRecord.getPaperSize();
    }

    public short getScale() {
        return this.printSetupRecord.getScale();
    }

    public short getPageStart() {
        return this.printSetupRecord.getPageStart();
    }

    public short getFitWidth() {
        return this.printSetupRecord.getFitWidth();
    }

    public short getFitHeight() {
        return this.printSetupRecord.getFitHeight();
    }

    public short getOptions() {
        return this.printSetupRecord.getOptions();
    }

    public boolean getLeftToRight() {
        return this.printSetupRecord.getLeftToRight();
    }

    public boolean getLandscape() {
        return !this.printSetupRecord.getLandscape();
    }

    public boolean getValidSettings() {
        return this.printSetupRecord.getValidSettings();
    }

    public boolean getNoColor() {
        return this.printSetupRecord.getNoColor();
    }

    public boolean getDraft() {
        return this.printSetupRecord.getDraft();
    }

    public boolean getNotes() {
        return this.printSetupRecord.getNotes();
    }

    public boolean getNoOrientation() {
        return this.printSetupRecord.getNoOrientation();
    }

    public boolean getUsePage() {
        return this.printSetupRecord.getUsePage();
    }

    public short getHResolution() {
        return this.printSetupRecord.getHResolution();
    }

    public short getVResolution() {
        return this.printSetupRecord.getVResolution();
    }

    public double getHeaderMargin() {
        return this.printSetupRecord.getHeaderMargin();
    }

    public double getFooterMargin() {
        return this.printSetupRecord.getFooterMargin();
    }

    public short getCopies() {
        return this.printSetupRecord.getCopies();
    }
}
