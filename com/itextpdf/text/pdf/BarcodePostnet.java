package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

public class BarcodePostnet extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1}, new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0}, new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0}};

    public BarcodePostnet() {
        this.n = 3.2727273f;
        this.x = 1.4399999f;
        this.barHeight = 9.0f;
        this.size = 3.6000001f;
        this.codeType = 7;
    }

    public static byte[] getBarsPostnet(String text) {
        int k;
        int total = 0;
        for (k = text.length() - 1; k >= 0; k--) {
            total += text.charAt(k) - 48;
        }
        text = text + ((char) (((10 - (total % 10)) % 10) + 48));
        byte[] bars = new byte[((text.length() * 5) + 2)];
        bars[0] = (byte) 1;
        bars[bars.length - 1] = (byte) 1;
        for (k = 0; k < text.length(); k++) {
            System.arraycopy(BARS[text.charAt(k) - 48], 0, bars, (k * 5) + 1, 5);
        }
        return bars;
    }

    public Rectangle getBarcodeSize() {
        return new Rectangle((((float) (((this.code.length() + 1) * 5) + 1)) * this.n) + this.x, this.barHeight);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        byte[] bars = getBarsPostnet(this.code);
        byte flip = (byte) 1;
        if (this.codeType == 8) {
            flip = (byte) 0;
            bars[0] = (byte) 0;
            bars[bars.length - 1] = (byte) 0;
        }
        float startX = 0.0f;
        for (byte b : bars) {
            cb.rectangle(startX, 0.0f, this.x - this.inkSpreading, b == flip ? this.barHeight : this.size);
            startX += this.n;
        }
        cb.fill();
        return getBarcodeSize();
    }
}
