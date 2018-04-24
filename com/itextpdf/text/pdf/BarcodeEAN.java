package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.Arrays;

public class BarcodeEAN extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 3, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 2, (byte) 2, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 4, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 4}, new byte[]{(byte) 1, (byte) 3, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 3}, new byte[]{(byte) 3, (byte) 1, (byte) 1, (byte) 2}};
    private static final int EVEN = 1;
    private static final int[] GUARD_EAN13 = new int[]{0, 2, 28, 30, 56, 58};
    private static final int[] GUARD_EAN8 = new int[]{0, 2, 20, 22, 40, 42};
    private static final int[] GUARD_EMPTY = new int[0];
    private static final int[] GUARD_UPCA = new int[]{0, 2, 4, 6, 28, 30, 52, 54, 56, 58};
    private static final int[] GUARD_UPCE = new int[]{0, 2, 28, 30, 32};
    private static final int ODD = 0;
    private static final byte[][] PARITY13 = new byte[][]{new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0}};
    private static final byte[][] PARITY2 = new byte[][]{new byte[]{(byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 1}};
    private static final byte[][] PARITY5 = new byte[][]{new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}};
    private static final byte[][] PARITYE = new byte[][]{new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}};
    private static final float[] TEXTPOS_EAN13 = new float[]{6.5f, 13.5f, 20.5f, 27.5f, 34.5f, 41.5f, 53.5f, 60.5f, 67.5f, 74.5f, 81.5f, 88.5f};
    private static final float[] TEXTPOS_EAN8 = new float[]{6.5f, 13.5f, 20.5f, 27.5f, 39.5f, 46.5f, 53.5f, 60.5f};
    private static final int TOTALBARS_EAN13 = 59;
    private static final int TOTALBARS_EAN8 = 43;
    private static final int TOTALBARS_SUPP2 = 13;
    private static final int TOTALBARS_SUPP5 = 31;
    private static final int TOTALBARS_UPCE = 33;

    public BarcodeEAN() {
        try {
            this.x = 0.8f;
            this.font = BaseFont.createFont("Helvetica", "winansi", false);
            this.size = 8.0f;
            this.baseline = this.size;
            this.barHeight = this.size * BaseField.BORDER_WIDTH_THICK;
            this.guardBars = true;
            this.codeType = 1;
            this.code = "";
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static int calculateEANParity(String code) {
        int mul = 3;
        int total = 0;
        for (int k = code.length() - 1; k >= 0; k--) {
            total += mul * (code.charAt(k) - 48);
            mul ^= 2;
        }
        return (10 - (total % 10)) % 10;
    }

    public static String convertUPCAtoUPCE(String text) {
        if (text.length() != 12 || (!text.startsWith("0") && !text.startsWith("1"))) {
            return null;
        }
        if (text.substring(3, 6).equals("000") || text.substring(3, 6).equals("100") || text.substring(3, 6).equals("200")) {
            if (text.substring(6, 8).equals("00")) {
                return text.substring(0, 1) + text.substring(1, 3) + text.substring(8, 11) + text.substring(3, 4) + text.substring(11);
            }
        } else if (text.substring(4, 6).equals("00")) {
            if (text.substring(6, 9).equals("000")) {
                return text.substring(0, 1) + text.substring(1, 4) + text.substring(9, 11) + "3" + text.substring(11);
            }
        } else if (text.substring(5, 6).equals("0")) {
            if (text.substring(6, 10).equals("0000")) {
                return text.substring(0, 1) + text.substring(1, 5) + text.substring(10, 11) + "4" + text.substring(11);
            }
        } else if (text.charAt(10) >= PdfWriter.VERSION_1_5 && text.substring(6, 10).equals("0000")) {
            return text.substring(0, 1) + text.substring(1, 6) + text.substring(10, 11) + text.substring(11);
        }
        return null;
    }

    public static byte[] getBarsEAN13(String _code) {
        int k;
        int[] code = new int[_code.length()];
        for (k = 0; k < code.length; k++) {
            code[k] = _code.charAt(k) - 48;
        }
        byte[] bars = new byte[59];
        int pb = 0 + 1;
        bars[0] = (byte) 1;
        int pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        byte[] sequence = PARITY13[code[0]];
        pb2 = pb;
        for (k = 0; k < sequence.length; k++) {
            byte[] stripes = BARS[code[k + 1]];
            if (sequence[k] == (byte) 0) {
                pb = pb2 + 1;
                bars[pb2] = stripes[0];
                pb2 = pb + 1;
                bars[pb] = stripes[1];
                pb = pb2 + 1;
                bars[pb2] = stripes[2];
                pb2 = pb + 1;
                bars[pb] = stripes[3];
            } else {
                pb = pb2 + 1;
                bars[pb2] = stripes[3];
                pb2 = pb + 1;
                bars[pb] = stripes[2];
                pb = pb2 + 1;
                bars[pb2] = stripes[1];
                pb2 = pb + 1;
                bars[pb] = stripes[0];
            }
        }
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        for (k = 7; k < 13; k++) {
            stripes = BARS[code[k]];
            pb2 = pb + 1;
            bars[pb] = stripes[0];
            pb = pb2 + 1;
            bars[pb2] = stripes[1];
            pb2 = pb + 1;
            bars[pb] = stripes[2];
            pb = pb2 + 1;
            bars[pb2] = stripes[3];
        }
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        return bars;
    }

    public static byte[] getBarsEAN8(String _code) {
        int k;
        int[] code = new int[_code.length()];
        for (k = 0; k < code.length; k++) {
            code[k] = _code.charAt(k) - 48;
        }
        byte[] bars = new byte[43];
        int pb = 0 + 1;
        bars[0] = (byte) 1;
        int pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        for (k = 0; k < 4; k++) {
            byte[] stripes = BARS[code[k]];
            pb2 = pb + 1;
            bars[pb] = stripes[0];
            pb = pb2 + 1;
            bars[pb2] = stripes[1];
            pb2 = pb + 1;
            bars[pb] = stripes[2];
            pb = pb2 + 1;
            bars[pb2] = stripes[3];
        }
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2;
        for (k = 4; k < 8; k++) {
            stripes = BARS[code[k]];
            pb2 = pb + 1;
            bars[pb] = stripes[0];
            pb = pb2 + 1;
            bars[pb2] = stripes[1];
            pb2 = pb + 1;
            bars[pb] = stripes[2];
            pb = pb2 + 1;
            bars[pb2] = stripes[3];
        }
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        return bars;
    }

    public static byte[] getBarsUPCE(String _code) {
        int k;
        boolean flip;
        int[] code = new int[_code.length()];
        for (k = 0; k < code.length; k++) {
            code[k] = _code.charAt(k) - 48;
        }
        byte[] bars = new byte[33];
        if (code[0] != 0) {
            flip = true;
        } else {
            flip = false;
        }
        int pb = 0 + 1;
        bars[0] = (byte) 1;
        int pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        byte[] sequence = PARITYE[code[code.length - 1]];
        pb2 = pb;
        for (k = 1; k < code.length - 1; k++) {
            byte b;
            byte[] stripes = BARS[code[k]];
            byte b2 = sequence[k - 1];
            if (flip) {
                b = (byte) 1;
            } else {
                b = (byte) 0;
            }
            if (b2 == b) {
                pb = pb2 + 1;
                bars[pb2] = stripes[0];
                pb2 = pb + 1;
                bars[pb] = stripes[1];
                pb = pb2 + 1;
                bars[pb2] = stripes[2];
                pb2 = pb + 1;
                bars[pb] = stripes[3];
            } else {
                pb = pb2 + 1;
                bars[pb2] = stripes[3];
                pb2 = pb + 1;
                bars[pb] = stripes[2];
                pb = pb2 + 1;
                bars[pb2] = stripes[1];
                pb2 = pb + 1;
                bars[pb] = stripes[0];
            }
        }
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 1;
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        return bars;
    }

    public static byte[] getBarsSupplemental2(String _code) {
        int k;
        int[] code = new int[2];
        for (k = 0; k < code.length; k++) {
            code[k] = _code.charAt(k) - 48;
        }
        byte[] bars = new byte[13];
        int parity = ((code[0] * 10) + code[1]) % 4;
        int pb = 0 + 1;
        bars[0] = (byte) 1;
        int pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 2;
        byte[] sequence = PARITY2[parity];
        pb2 = pb;
        for (k = 0; k < sequence.length; k++) {
            if (k == 1) {
                pb = pb2 + 1;
                bars[pb2] = (byte) 1;
                pb2 = pb + 1;
                bars[pb] = (byte) 1;
            }
            byte[] stripes = BARS[code[k]];
            if (sequence[k] == (byte) 0) {
                pb = pb2 + 1;
                bars[pb2] = stripes[0];
                pb2 = pb + 1;
                bars[pb] = stripes[1];
                pb = pb2 + 1;
                bars[pb2] = stripes[2];
                pb2 = pb + 1;
                bars[pb] = stripes[3];
            } else {
                pb = pb2 + 1;
                bars[pb2] = stripes[3];
                pb2 = pb + 1;
                bars[pb] = stripes[2];
                pb = pb2 + 1;
                bars[pb2] = stripes[1];
                pb2 = pb + 1;
                bars[pb] = stripes[0];
            }
        }
        return bars;
    }

    public static byte[] getBarsSupplemental5(String _code) {
        int k;
        int[] code = new int[5];
        for (k = 0; k < code.length; k++) {
            code[k] = _code.charAt(k) - 48;
        }
        byte[] bars = new byte[31];
        int parity = ((((code[0] + code[2]) + code[4]) * 3) + ((code[1] + code[3]) * 9)) % 10;
        int i = 0 + 1;
        bars[0] = (byte) 1;
        int i2 = i + 1;
        bars[i] = (byte) 1;
        i = i2 + 1;
        bars[i2] = (byte) 2;
        byte[] sequence = PARITY5[parity];
        i2 = i;
        for (k = 0; k < sequence.length; k++) {
            if (k != 0) {
                i = i2 + 1;
                bars[i2] = (byte) 1;
                i2 = i + 1;
                bars[i] = (byte) 1;
            }
            byte[] stripes = BARS[code[k]];
            if (sequence[k] == (byte) 0) {
                i = i2 + 1;
                bars[i2] = stripes[0];
                i2 = i + 1;
                bars[i] = stripes[1];
                i = i2 + 1;
                bars[i2] = stripes[2];
                i2 = i + 1;
                bars[i] = stripes[3];
            } else {
                i = i2 + 1;
                bars[i2] = stripes[3];
                i2 = i + 1;
                bars[i] = stripes[2];
                i = i2 + 1;
                bars[i2] = stripes[1];
                i2 = i + 1;
                bars[i] = stripes[0];
            }
        }
        return bars;
    }

    public Rectangle getBarcodeSize() {
        float width;
        float height = this.barHeight;
        if (this.font != null) {
            if (this.baseline <= 0.0f) {
                height += (-this.baseline) + this.size;
            } else {
                height += this.baseline - this.font.getFontDescriptor(3, this.size);
            }
        }
        switch (this.codeType) {
            case 1:
                width = this.x * 95.0f;
                if (this.font != null) {
                    width += this.font.getWidthPoint(this.code.charAt(0), this.size);
                    break;
                }
                break;
            case 2:
                width = this.x * 67.0f;
                break;
            case 3:
                width = this.x * 95.0f;
                if (this.font != null) {
                    width += this.font.getWidthPoint(this.code.charAt(0), this.size) + this.font.getWidthPoint(this.code.charAt(11), this.size);
                    break;
                }
                break;
            case 4:
                width = this.x * 51.0f;
                if (this.font != null) {
                    width += this.font.getWidthPoint(this.code.charAt(0), this.size) + this.font.getWidthPoint(this.code.charAt(7), this.size);
                    break;
                }
                break;
            case 5:
                width = this.x * 20.0f;
                break;
            case 6:
                width = this.x * 47.0f;
                break;
            default:
                throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.type", new Object[0]));
        }
        return new Rectangle(width, height);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        int k;
        Rectangle rect = getBarcodeSize();
        float barStartX = 0.0f;
        float barStartY = 0.0f;
        float textStartY = 0.0f;
        if (this.font != null) {
            if (this.baseline <= 0.0f) {
                textStartY = this.barHeight - this.baseline;
            } else {
                textStartY = -this.font.getFontDescriptor(3, this.size);
                barStartY = textStartY + this.baseline;
            }
        }
        switch (this.codeType) {
            case 1:
            case 3:
            case 4:
                if (this.font != null) {
                    barStartX = 0.0f + this.font.getWidthPoint(this.code.charAt(0), this.size);
                    break;
                }
                break;
        }
        byte[] bars = null;
        int[] guard = GUARD_EMPTY;
        switch (this.codeType) {
            case 1:
                bars = getBarsEAN13(this.code);
                guard = GUARD_EAN13;
                break;
            case 2:
                bars = getBarsEAN8(this.code);
                guard = GUARD_EAN8;
                break;
            case 3:
                bars = getBarsEAN13("0" + this.code);
                guard = GUARD_UPCA;
                break;
            case 4:
                bars = getBarsUPCE(this.code);
                guard = GUARD_UPCE;
                break;
            case 5:
                bars = getBarsSupplemental2(this.code);
                break;
            case 6:
                bars = getBarsSupplemental5(this.code);
                break;
        }
        float keepBarX = barStartX;
        boolean print = true;
        float gd = 0.0f;
        if (this.font != null && this.baseline > 0.0f && this.guardBars) {
            gd = this.baseline / BaseField.BORDER_WIDTH_MEDIUM;
        }
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        for (k = 0; k < bars.length; k++) {
            float w = ((float) bars[k]) * this.x;
            if (print) {
                if (Arrays.binarySearch(guard, k) >= 0) {
                    cb.rectangle(barStartX, barStartY - gd, w - this.inkSpreading, this.barHeight + gd);
                } else {
                    cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight);
                }
            }
            print = !print;
            barStartX += w;
        }
        cb.fill();
        if (this.font != null) {
            if (textColor != null) {
                cb.setColorFill(textColor);
            }
            cb.beginText();
            cb.setFontAndSize(this.font, this.size);
            String c;
            switch (this.codeType) {
                case 1:
                    cb.setTextMatrix(0.0f, textStartY);
                    cb.showText(this.code.substring(0, 1));
                    for (k = 1; k < 13; k++) {
                        c = this.code.substring(k, k + 1);
                        cb.setTextMatrix(((TEXTPOS_EAN13[k - 1] * this.x) + keepBarX) - (this.font.getWidthPoint(c, this.size) / BaseField.BORDER_WIDTH_MEDIUM), textStartY);
                        cb.showText(c);
                    }
                    break;
                case 2:
                    for (k = 0; k < 8; k++) {
                        c = this.code.substring(k, k + 1);
                        cb.setTextMatrix((TEXTPOS_EAN8[k] * this.x) - (this.font.getWidthPoint(c, this.size) / BaseField.BORDER_WIDTH_MEDIUM), textStartY);
                        cb.showText(c);
                    }
                    break;
                case 3:
                    cb.setTextMatrix(0.0f, textStartY);
                    cb.showText(this.code.substring(0, 1));
                    for (k = 1; k < 11; k++) {
                        c = this.code.substring(k, k + 1);
                        cb.setTextMatrix(((TEXTPOS_EAN13[k] * this.x) + keepBarX) - (this.font.getWidthPoint(c, this.size) / BaseField.BORDER_WIDTH_MEDIUM), textStartY);
                        cb.showText(c);
                    }
                    cb.setTextMatrix((this.x * 95.0f) + keepBarX, textStartY);
                    cb.showText(this.code.substring(11, 12));
                    break;
                case 4:
                    cb.setTextMatrix(0.0f, textStartY);
                    cb.showText(this.code.substring(0, 1));
                    for (k = 1; k < 7; k++) {
                        c = this.code.substring(k, k + 1);
                        cb.setTextMatrix(((TEXTPOS_EAN13[k - 1] * this.x) + keepBarX) - (this.font.getWidthPoint(c, this.size) / BaseField.BORDER_WIDTH_MEDIUM), textStartY);
                        cb.showText(c);
                    }
                    cb.setTextMatrix((this.x * 51.0f) + keepBarX, textStartY);
                    cb.showText(this.code.substring(7, 8));
                    break;
                case 5:
                case 6:
                    for (k = 0; k < this.code.length(); k++) {
                        c = this.code.substring(k, k + 1);
                        cb.setTextMatrix(((7.5f + ((float) (k * 9))) * this.x) - (this.font.getWidthPoint(c, this.size) / BaseField.BORDER_WIDTH_MEDIUM), textStartY);
                        cb.showText(c);
                    }
                    break;
            }
            cb.endText();
        }
        return rect;
    }
}
