package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

public class BarcodeEANSUPP extends Barcode {
    protected Barcode ean;
    protected Barcode supp;

    public BarcodeEANSUPP(Barcode ean, Barcode supp) {
        this.n = 8.0f;
        this.ean = ean;
        this.supp = supp;
    }

    public Rectangle getBarcodeSize() {
        Rectangle rect = this.ean.getBarcodeSize();
        rect.setRight((rect.getWidth() + this.supp.getBarcodeSize().getWidth()) + this.n);
        return rect;
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        if (this.supp.getFont() != null) {
            this.supp.setBarHeight((this.ean.getBarHeight() + this.supp.getBaseline()) - this.supp.getFont().getFontDescriptor(2, this.supp.getSize()));
        } else {
            this.supp.setBarHeight(this.ean.getBarHeight());
        }
        Rectangle eanR = this.ean.getBarcodeSize();
        cb.saveState();
        this.ean.placeBarcode(cb, barColor, textColor);
        cb.restoreState();
        cb.saveState();
        cb.concatCTM(BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, eanR.getWidth() + this.n, eanR.getHeight() - this.ean.getBarHeight());
        this.supp.placeBarcode(cb, barColor, textColor);
        cb.restoreState();
        return getBarcodeSize();
    }
}
