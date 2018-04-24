package com.itextpdf.text;

import com.google.zxing.pdf417.PDF417Common;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.Barcode128;
import org.apache.poi.util.CodePageUtil;

public class SpecialSymbol {
    public static int index(String string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (getCorrespondingSymbol(string.charAt(i)) != ' ') {
                return i;
            }
        }
        return -1;
    }

    public static Chunk get(char c, Font font) {
        char greek = getCorrespondingSymbol(c);
        if (greek == ' ') {
            return new Chunk(String.valueOf(c), font);
        }
        return new Chunk(String.valueOf(greek), new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor()));
    }

    public static char getCorrespondingSymbol(char c) {
        switch (c) {
            case 'Α':
                return 'A';
            case 'Β':
                return 'B';
            case 'Γ':
                return 'G';
            case 'Δ':
                return 'D';
            case 'Ε':
                return 'E';
            case 'Ζ':
                return 'Z';
            case 'Η':
                return 'H';
            case 'Θ':
                return 'Q';
            case 'Ι':
                return 'I';
            case 'Κ':
                return 'K';
            case 'Λ':
                return 'L';
            case 'Μ':
                return 'M';
            case 'Ν':
                return 'N';
            case 'Ξ':
                return 'X';
            case 'Ο':
                return 'O';
            case PDF417Common.MAX_CODEWORDS_IN_BARCODE /*928*/:
                return 'P';
            case PDF417Common.NUMBER_OF_CODEWORDS /*929*/:
                return 'R';
            case 'Σ':
                return 'S';
            case CodePageUtil.CP_SJIS /*932*/:
                return 'T';
            case 'Υ':
                return 'U';
            case 'Φ':
                return 'F';
            case TGC_CURVE_SIZE:
                return 'C';
            case CodePageUtil.CP_GBK /*936*/:
                return 'Y';
            case 'Ω':
                return 'W';
            case 'α':
                return 'a';
            case 'β':
                return 'b';
            case 'γ':
                return Barcode128.START_A;
            case 'δ':
                return Barcode128.CODE_AC_TO_B;
            case CodePageUtil.CP_MS949 /*949*/:
                return Barcode128.CODE_BC_TO_A;
            case 'ζ':
                return 'z';
            case 'η':
                return Barcode128.START_B;
            case 'θ':
                return 'q';
            case 'ι':
                return Barcode128.START_C;
            case 'κ':
                return 'k';
            case 'λ':
                return 'l';
            case 'μ':
                return 'm';
            case 'ν':
                return 'n';
            case 'ξ':
                return 'x';
            case 'ο':
                return 'o';
            case 'π':
                return 'p';
            case 'ρ':
                return 'r';
            case 'ς':
                return 'V';
            case 'σ':
                return 's';
            case 'τ':
                return 't';
            case 'υ':
                return 'u';
            case 'φ':
                return Barcode128.FNC1_INDEX;
            case 'χ':
                return Barcode128.CODE_AB_TO_C;
            case 'ψ':
                return 'y';
            case 'ω':
                return 'w';
            default:
                return ' ';
        }
    }
}
