package com.itextpdf.text.pdf.languages;

public class GujaratiLigaturizer extends IndicLigaturizer {
    public static final char GUJR_HALANTA = '્';
    public static final char GUJR_LETTER_A = 'અ';
    public static final char GUJR_LETTER_AU = 'ઔ';
    public static final char GUJR_LETTER_HA = 'હ';
    public static final char GUJR_LETTER_KA = 'ક';
    public static final char GUJR_MATRA_AA = 'ા';
    public static final char GUJR_MATRA_AI = 'ૈ';
    public static final char GUJR_MATRA_E = 'ે';
    public static final char GUJR_MATRA_HLR = 'ૢ';
    public static final char GUJR_MATRA_HLRR = 'ૣ';
    public static final char GUJR_MATRA_I = 'િ';

    public GujaratiLigaturizer() {
        this.langTable = new char[11];
        this.langTable[0] = GUJR_MATRA_AA;
        this.langTable[1] = GUJR_MATRA_I;
        this.langTable[2] = GUJR_MATRA_E;
        this.langTable[3] = GUJR_MATRA_AI;
        this.langTable[4] = GUJR_MATRA_HLR;
        this.langTable[5] = GUJR_MATRA_HLRR;
        this.langTable[6] = GUJR_LETTER_A;
        this.langTable[7] = GUJR_LETTER_AU;
        this.langTable[8] = GUJR_LETTER_KA;
        this.langTable[9] = GUJR_LETTER_HA;
        this.langTable[10] = GUJR_HALANTA;
    }
}
