package org.apache.poi.ss.usermodel;

public interface PrintSetup {
    public static final short A3_PAPERSIZE = (short) 8;
    public static final short A4_EXTRA_PAPERSIZE = (short) 53;
    public static final short A4_PAPERSIZE = (short) 9;
    public static final short A4_PLUS_PAPERSIZE = (short) 60;
    public static final short A4_ROTATED_PAPERSIZE = (short) 77;
    public static final short A4_SMALL_PAPERSIZE = (short) 10;
    public static final short A4_TRANSVERSE_PAPERSIZE = (short) 55;
    public static final short A5_PAPERSIZE = (short) 11;
    public static final short B4_PAPERSIZE = (short) 12;
    public static final short B5_PAPERSIZE = (short) 13;
    public static final short ELEVEN_BY_SEVENTEEN_PAPERSIZE = (short) 17;
    public static final short ENVELOPE_10_PAPERSIZE = (short) 20;
    public static final short ENVELOPE_9_PAPERSIZE = (short) 19;
    public static final short ENVELOPE_C3_PAPERSIZE = (short) 29;
    public static final short ENVELOPE_C4_PAPERSIZE = (short) 30;
    public static final short ENVELOPE_C5_PAPERSIZE = (short) 28;
    public static final short ENVELOPE_C6_PAPERSIZE = (short) 31;
    public static final short ENVELOPE_CS_PAPERSIZE = (short) 28;
    public static final short ENVELOPE_DL_PAPERSIZE = (short) 27;
    public static final short ENVELOPE_MONARCH_PAPERSIZE = (short) 37;
    public static final short EXECUTIVE_PAPERSIZE = (short) 7;
    public static final short FOLIO8_PAPERSIZE = (short) 14;
    public static final short LEDGER_PAPERSIZE = (short) 4;
    public static final short LEGAL_PAPERSIZE = (short) 5;
    public static final short LETTER_PAPERSIZE = (short) 1;
    public static final short LETTER_ROTATED_PAPERSIZE = (short) 75;
    public static final short LETTER_SMALL_PAGESIZE = (short) 2;
    public static final short NOTE8_PAPERSIZE = (short) 18;
    public static final short PRINTER_DEFAULT_PAPERSIZE = (short) 0;
    public static final short QUARTO_PAPERSIZE = (short) 15;
    public static final short STATEMENT_PAPERSIZE = (short) 6;
    public static final short TABLOID_PAPERSIZE = (short) 3;
    public static final short TEN_BY_FOURTEEN_PAPERSIZE = (short) 16;

    short getCopies();

    boolean getDraft();

    short getFitHeight();

    short getFitWidth();

    double getFooterMargin();

    short getHResolution();

    double getHeaderMargin();

    boolean getLandscape();

    boolean getLeftToRight();

    boolean getNoColor();

    boolean getNoOrientation();

    boolean getNotes();

    short getPageStart();

    short getPaperSize();

    short getScale();

    boolean getUsePage();

    short getVResolution();

    boolean getValidSettings();

    void setCopies(short s);

    void setDraft(boolean z);

    void setFitHeight(short s);

    void setFitWidth(short s);

    void setFooterMargin(double d);

    void setHResolution(short s);

    void setHeaderMargin(double d);

    void setLandscape(boolean z);

    void setLeftToRight(boolean z);

    void setNoColor(boolean z);

    void setNoOrientation(boolean z);

    void setNotes(boolean z);

    void setPageStart(short s);

    void setPaperSize(short s);

    void setScale(short s);

    void setUsePage(boolean z);

    void setVResolution(short s);

    void setValidSettings(boolean z);
}
