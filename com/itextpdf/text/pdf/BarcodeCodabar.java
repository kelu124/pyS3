package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;

public class BarcodeCodabar extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0}};
    private static final String CHARS = "0123456789-$:/.+ABCD";
    private static final int START_STOP_IDX = 16;

    public BarcodeCodabar() {
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
            this.startStopText = false;
            this.codeType = 12;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static byte[] getBarsCodabar(String text) {
        text = text.toUpperCase();
        int len = text.length();
        if (len < 2) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("codabar.must.have.at.least.a.start.and.stop.character", new Object[0]));
        } else if (CHARS.indexOf(text.charAt(0)) < 16 || CHARS.indexOf(text.charAt(len - 1)) < 16) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("codabar.must.have.one.of.abcd.as.start.stop.character", new Object[0]));
        } else {
            byte[] bars = new byte[((text.length() * 8) - 1)];
            int k = 0;
            while (k < len) {
                int idx = CHARS.indexOf(text.charAt(k));
                if (idx >= 16 && k > 0 && k < len - 1) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.codabar.start.stop.characters.are.only.allowed.at.the.extremes", new Object[0]));
                } else if (idx < 0) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.codabar", text.charAt(k)));
                } else {
                    System.arraycopy(BARS[idx], 0, bars, k * 8, 7);
                    k++;
                }
            }
            return bars;
        }
    }

    public static String calculateChecksum(String code) {
        if (code.length() < 2) {
            return code;
        }
        String text = code.toUpperCase();
        int sum = 0;
        int len = text.length();
        for (int k = 0; k < len; k++) {
            sum += CHARS.indexOf(text.charAt(k));
        }
        return code.substring(0, len - 1) + CHARS.charAt((((sum + 15) / 16) * 16) - sum) + code.substring(len - 1);
    }

    public Rectangle getBarcodeSize() {
        float fontX = 0.0f;
        float fontY = 0.0f;
        String text = this.code;
        if (this.generateChecksum && this.checksumText) {
            text = calculateChecksum(this.code);
        }
        if (!this.startStopText) {
            text = text.substring(1, text.length() - 1);
        }
        if (this.font != null) {
            if (this.baseline > 0.0f) {
                fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
            } else {
                fontY = (-this.baseline) + this.size;
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                text = this.altText;
            }
            fontX = baseFont.getWidthPoint(text, this.size);
        }
        text = this.code;
        if (this.generateChecksum) {
            text = calculateChecksum(this.code);
        }
        byte[] bars = getBarsCodabar(text);
        int wide = 0;
        for (byte b : bars) {
            wide += b;
        }
        return new Rectangle(Math.max(this.x * (((float) (bars.length - wide)) + (((float) wide) * this.n)), fontX), this.barHeight + fontY);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        String fullCode = this.code;
        if (this.generateChecksum && this.checksumText) {
            fullCode = calculateChecksum(this.code);
        }
        if (!this.startStopText) {
            fullCode = fullCode.substring(1, fullCode.length() - 1);
        }
        float fontX = 0.0f;
        if (this.font != null) {
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        byte[] bars = getBarsCodabar(this.generateChecksum ? calculateChecksum(this.code) : this.code);
        int wide = 0;
        for (byte b : bars) {
            wide += b;
        }
        float fullWidth = this.x * (((float) (bars.length - wide)) + (((float) wide) * this.n));
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
        boolean print = true;
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        for (byte b2 : bars) {
            float w = b2 == (byte) 0 ? this.x : this.x * this.n;
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
