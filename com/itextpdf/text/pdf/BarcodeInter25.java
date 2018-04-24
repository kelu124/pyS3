package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;

public class BarcodeInter25 extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}};

    public BarcodeInter25() {
        try {
            this.x = 0.8f;
            this.n = BaseField.BORDER_WIDTH_MEDIUM;
            this.font = BaseFont.createFont("Helvetica", "winansi", false);
            this.size = 8.0f;
            this.baseline = this.size;
            this.barHeight = this.size * BaseField.BORDER_WIDTH_THICK;
            this.textAlignment = 1;
            this.generateChecksum = false;
            this.checksumText = false;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static String keepNumbers(String text) {
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < text.length(); k++) {
            char c = text.charAt(k);
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static char getChecksum(String text) {
        int mul = 3;
        int total = 0;
        for (int k = text.length() - 1; k >= 0; k--) {
            total += mul * (text.charAt(k) - 48);
            mul ^= 2;
        }
        return (char) (((10 - (total % 10)) % 10) + 48);
    }

    public static byte[] getBarsInter25(String text) {
        text = keepNumbers(text);
        if ((text.length() & 1) != 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.text.length.must.be.even", new Object[0]));
        }
        byte[] bars = new byte[((text.length() * 5) + 7)];
        int pb = 0 + 1;
        bars[0] = (byte) 0;
        int pb2 = pb + 1;
        bars[pb] = (byte) 0;
        pb = pb2 + 1;
        bars[pb2] = (byte) 0;
        pb2 = pb + 1;
        bars[pb] = (byte) 0;
        int len = text.length() / 2;
        pb = pb2;
        for (int k = 0; k < len; k++) {
            int c2 = text.charAt((k * 2) + 1) - 48;
            byte[] b1 = BARS[text.charAt(k * 2) - 48];
            byte[] b2 = BARS[c2];
            for (int j = 0; j < 5; j++) {
                pb2 = pb + 1;
                bars[pb] = b1[j];
                pb = pb2 + 1;
                bars[pb2] = b2[j];
            }
        }
        pb2 = pb + 1;
        bars[pb] = (byte) 1;
        pb = pb2 + 1;
        bars[pb2] = (byte) 0;
        pb2 = pb + 1;
        bars[pb] = (byte) 0;
        return bars;
    }

    public Rectangle getBarcodeSize() {
        float fontX = 0.0f;
        float fontY = 0.0f;
        if (this.font != null) {
            if (this.baseline > 0.0f) {
                fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
            } else {
                fontY = (-this.baseline) + this.size;
            }
            String fullCode = this.code;
            if (this.generateChecksum && this.checksumText) {
                fullCode = fullCode + getChecksum(fullCode);
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        int len = keepNumbers(this.code).length();
        if (this.generateChecksum) {
            len++;
        }
        return new Rectangle(Math.max((((float) len) * ((BaseField.BORDER_WIDTH_THICK * this.x) + ((BaseField.BORDER_WIDTH_MEDIUM * this.x) * this.n))) + ((6.0f + this.n) * this.x), fontX), this.barHeight + fontY);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        String fullCode = this.code;
        float fontX = 0.0f;
        if (this.font != null) {
            if (this.generateChecksum && this.checksumText) {
                fullCode = fullCode + getChecksum(fullCode);
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        String bCode = keepNumbers(this.code);
        if (this.generateChecksum) {
            bCode = bCode + getChecksum(bCode);
        }
        float fullWidth = (((float) bCode.length()) * ((BaseField.BORDER_WIDTH_THICK * this.x) + ((BaseField.BORDER_WIDTH_MEDIUM * this.x) * this.n))) + ((6.0f + this.n) * this.x);
        float barStartX = 0.0f;
        float textStartX = 0.0f;
        switch (this.textAlignment) {
            case 0:
                break;
            case 2:
                if (fontX <= fullWidth) {
                    textStartX = fullWidth - fontX;
                    break;
                }
                barStartX = fontX - fullWidth;
                break;
            default:
                if (fontX <= fullWidth) {
                    textStartX = (fullWidth - fontX) / BaseField.BORDER_WIDTH_MEDIUM;
                    break;
                }
                barStartX = (fontX - fullWidth) / BaseField.BORDER_WIDTH_MEDIUM;
                break;
        }
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
        byte[] bars = getBarsInter25(bCode);
        boolean print = true;
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        for (byte b : bars) {
            float w = b == (byte) 0 ? this.x : this.x * this.n;
            if (print) {
                cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight);
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
            cb.setTextMatrix(textStartX, textStartY);
            cb.showText(fullCode);
            cb.endText();
        }
        return getBarcodeSize();
    }
}
