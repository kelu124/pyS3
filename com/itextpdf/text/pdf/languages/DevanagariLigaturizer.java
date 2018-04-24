package com.itextpdf.text.pdf.languages;

public class DevanagariLigaturizer extends IndicLigaturizer {
    public static final char DEVA_HALANTA = '्';
    public static final char DEVA_LETTER_A = 'अ';
    public static final char DEVA_LETTER_AU = 'औ';
    public static final char DEVA_LETTER_HA = 'ह';
    public static final char DEVA_LETTER_KA = 'क';
    public static final char DEVA_MATRA_AA = 'ा';
    public static final char DEVA_MATRA_AI = 'ै';
    public static final char DEVA_MATRA_E = 'े';
    public static final char DEVA_MATRA_HLR = 'ॢ';
    public static final char DEVA_MATRA_HLRR = 'ॣ';
    public static final char DEVA_MATRA_I = 'ि';

    public DevanagariLigaturizer() {
        this.langTable = new char[11];
        this.langTable[0] = DEVA_MATRA_AA;
        this.langTable[1] = DEVA_MATRA_I;
        this.langTable[2] = DEVA_MATRA_E;
        this.langTable[3] = DEVA_MATRA_AI;
        this.langTable[4] = DEVA_MATRA_HLR;
        this.langTable[5] = DEVA_MATRA_HLRR;
        this.langTable[6] = DEVA_LETTER_A;
        this.langTable[7] = DEVA_LETTER_AU;
        this.langTable[8] = DEVA_LETTER_KA;
        this.langTable[9] = DEVA_LETTER_HA;
        this.langTable[10] = DEVA_HALANTA;
    }
}
