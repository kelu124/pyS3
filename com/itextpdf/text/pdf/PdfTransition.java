package com.itextpdf.text.pdf;

public class PdfTransition {
    public static final int BLINDH = 6;
    public static final int BLINDV = 5;
    public static final int BTWIPE = 11;
    public static final int DGLITTER = 16;
    public static final int DISSOLVE = 13;
    public static final int INBOX = 7;
    public static final int LRGLITTER = 14;
    public static final int LRWIPE = 9;
    public static final int OUTBOX = 8;
    public static final int RLWIPE = 10;
    public static final int SPLITHIN = 4;
    public static final int SPLITHOUT = 2;
    public static final int SPLITVIN = 3;
    public static final int SPLITVOUT = 1;
    public static final int TBGLITTER = 15;
    public static final int TBWIPE = 12;
    protected int duration;
    protected int type;

    public PdfTransition() {
        this(6);
    }

    public PdfTransition(int type) {
        this(type, 1);
    }

    public PdfTransition(int type, int duration) {
        this.duration = duration;
        this.type = type;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getType() {
        return this.type;
    }

    public PdfDictionary getTransitionDictionary() {
        PdfDictionary trans = new PdfDictionary(PdfName.TRANS);
        switch (this.type) {
            case 1:
                trans.put(PdfName.f133S, PdfName.SPLIT);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f136V);
                trans.put(PdfName.f127M, PdfName.f129O);
                break;
            case 2:
                trans.put(PdfName.f133S, PdfName.SPLIT);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f123H);
                trans.put(PdfName.f127M, PdfName.f129O);
                break;
            case 3:
                trans.put(PdfName.f133S, PdfName.SPLIT);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f136V);
                trans.put(PdfName.f127M, PdfName.f124I);
                break;
            case 4:
                trans.put(PdfName.f133S, PdfName.SPLIT);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f123H);
                trans.put(PdfName.f127M, PdfName.f124I);
                break;
            case 5:
                trans.put(PdfName.f133S, PdfName.BLINDS);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f136V);
                break;
            case 6:
                trans.put(PdfName.f133S, PdfName.BLINDS);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DM, PdfName.f123H);
                break;
            case 7:
                trans.put(PdfName.f133S, PdfName.BOX);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.f127M, PdfName.f124I);
                break;
            case 8:
                trans.put(PdfName.f133S, PdfName.BOX);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.f127M, PdfName.f129O);
                break;
            case 9:
                trans.put(PdfName.f133S, PdfName.WIPE);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(0));
                break;
            case 10:
                trans.put(PdfName.f133S, PdfName.WIPE);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(180));
                break;
            case 11:
                trans.put(PdfName.f133S, PdfName.WIPE);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(90));
                break;
            case 12:
                trans.put(PdfName.f133S, PdfName.WIPE);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(270));
                break;
            case 13:
                trans.put(PdfName.f133S, PdfName.DISSOLVE);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                break;
            case 14:
                trans.put(PdfName.f133S, PdfName.GLITTER);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(0));
                break;
            case 15:
                trans.put(PdfName.f133S, PdfName.GLITTER);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(270));
                break;
            case 16:
                trans.put(PdfName.f133S, PdfName.GLITTER);
                trans.put(PdfName.f120D, new PdfNumber(this.duration));
                trans.put(PdfName.DI, new PdfNumber(315));
                break;
        }
        return trans;
    }
}
