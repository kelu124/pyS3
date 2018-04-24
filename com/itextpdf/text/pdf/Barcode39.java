package com.itextpdf.text.pdf;

import com.google.common.base.Ascii;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;

public class Barcode39 extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}};
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";
    private static final String EXTENDED = "%U$A$B$C$D$E$F$G$H$I$J$K$L$M$N$O$P$Q$R$S$T$U$V$W$X$Y$Z%A%B%C%D%E  /A/B/C/D/E/F/G/H/I/J/K/L - ./O 0 1 2 3 4 5 6 7 8 9/Z%F%G%H%I%J%V A B C D E F G H I J K L M N O P Q R S T U V W X Y Z%K%L%M%N%O%W+A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+Z%P%Q%R%S%T";

    public Barcode39() {
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
            this.startStopText = true;
            this.extended = false;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static byte[] getBarsCode39(String text) {
        text = "*" + text + "*";
        byte[] bars = new byte[((text.length() * 10) - 1)];
        for (int k = 0; k < text.length(); k++) {
            int idx = CHARS.indexOf(text.charAt(k));
            if (idx < 0) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39", text.charAt(k)));
            }
            System.arraycopy(BARS[idx], 0, bars, k * 10, 9);
        }
        return bars;
    }

    public static String getCode39Ex(String text) {
        StringBuilder out = new StringBuilder("");
        for (int k = 0; k < text.length(); k++) {
            int c = text.charAt(k);
            if (c > Ascii.MAX) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39.extended", c));
            }
            char c1 = EXTENDED.charAt(c * 2);
            char c2 = EXTENDED.charAt((c * 2) + 1);
            if (c1 != ' ') {
                out.append(c1);
            }
            out.append(c2);
        }
        return out.toString();
    }

    static char getChecksum(String text) {
        int chk = 0;
        for (int k = 0; k < text.length(); k++) {
            int idx = CHARS.indexOf(text.charAt(k));
            if (idx < 0) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39", text.charAt(k)));
            }
            chk += idx;
        }
        return CHARS.charAt(chk % 43);
    }

    public Rectangle getBarcodeSize() {
        float fontX = 0.0f;
        float fontY = 0.0f;
        String fCode = this.code;
        if (this.extended) {
            fCode = getCode39Ex(this.code);
        }
        if (this.font != null) {
            if (this.baseline > 0.0f) {
                fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
            } else {
                fontY = (-this.baseline) + this.size;
            }
            String fullCode = this.code;
            if (this.generateChecksum && this.checksumText) {
                fullCode = fullCode + getChecksum(fCode);
            }
            if (this.startStopText) {
                fullCode = "*" + fullCode + "*";
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        int len = fCode.length() + 2;
        if (this.generateChecksum) {
            len++;
        }
        return new Rectangle(Math.max((((float) len) * ((6.0f * this.x) + ((BaseField.BORDER_WIDTH_THICK * this.x) * this.n))) + (((float) (len - 1)) * this.x), fontX), this.barHeight + fontY);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        String fullCode = this.code;
        float fontX = 0.0f;
        String bCode = this.code;
        if (this.extended) {
            bCode = getCode39Ex(this.code);
        }
        if (this.font != null) {
            if (this.generateChecksum && this.checksumText) {
                fullCode = fullCode + getChecksum(bCode);
            }
            if (this.startStopText) {
                fullCode = "*" + fullCode + "*";
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        if (this.generateChecksum) {
            bCode = bCode + getChecksum(bCode);
        }
        int len = bCode.length() + 2;
        float fullWidth = (((float) len) * ((6.0f * this.x) + ((BaseField.BORDER_WIDTH_THICK * this.x) * this.n))) + (((float) (len - 1)) * this.x);
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
        byte[] bars = getBarsCode39(bCode);
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
