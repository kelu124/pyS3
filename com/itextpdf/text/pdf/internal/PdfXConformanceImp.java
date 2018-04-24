package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ExtendedColor;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfXConformanceException;
import com.itextpdf.text.pdf.ShadingColor;
import com.itextpdf.text.pdf.SpotColor;
import com.itextpdf.text.pdf.interfaces.PdfXConformance;

public class PdfXConformanceImp implements PdfXConformance {
    protected int pdfxConformance = 0;
    protected PdfWriter writer;

    public PdfXConformanceImp(PdfWriter writer) {
        this.writer = writer;
    }

    public void setPDFXConformance(int pdfxConformance) {
        this.pdfxConformance = pdfxConformance;
    }

    public int getPDFXConformance() {
        return this.pdfxConformance;
    }

    public boolean isPdfIso() {
        return isPdfX();
    }

    public boolean isPdfX() {
        return this.pdfxConformance != 0;
    }

    public boolean isPdfX1A2001() {
        return this.pdfxConformance == 1;
    }

    public boolean isPdfX32002() {
        return this.pdfxConformance == 2;
    }

    public void checkPdfIsoConformance(int key, Object obj1) {
        if (this.writer != null && this.writer.isPdfX()) {
            int conf = this.writer.getPDFXConformance();
            switch (key) {
                case 1:
                    switch (conf) {
                        case 1:
                            if (obj1 instanceof ExtendedColor) {
                                ExtendedColor ec = (ExtendedColor) obj1;
                                switch (ec.getType()) {
                                    case 0:
                                        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
                                    case 1:
                                    case 2:
                                        return;
                                    case 3:
                                        checkPdfIsoConformance(1, ((SpotColor) ec).getPdfSpotColor().getAlternativeCS());
                                        return;
                                    case 4:
                                        checkPdfIsoConformance(1, ((PatternColor) ec).getPainter().getDefaultColor());
                                        return;
                                    case 5:
                                        checkPdfIsoConformance(1, ((ShadingColor) ec).getPdfShadingPattern().getShading().getColorSpace());
                                        return;
                                    default:
                                        return;
                                }
                            } else if (obj1 instanceof BaseColor) {
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
                            } else {
                                return;
                            }
                        default:
                            return;
                    }
                case 2:
                    return;
                case 3:
                    if (conf == 1) {
                        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
                    }
                    return;
                case 4:
                    if (!((BaseFont) obj1).isEmbedded()) {
                        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
                    }
                    return;
                case 5:
                    PdfImage image = (PdfImage) obj1;
                    if (image.get(PdfName.SMASK) != null) {
                        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images", new Object[0]));
                    }
                    switch (conf) {
                        case 1:
                            PdfObject cs = image.get(PdfName.COLORSPACE);
                            if (cs == null) {
                                return;
                            }
                            if (cs.isName()) {
                                if (PdfName.DEVICERGB.equals(cs)) {
                                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
                                }
                                return;
                            } else if (cs.isArray() && PdfName.CALRGB.equals(((PdfArray) cs).getPdfObject(0))) {
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.calrgb.is.not.allowed", new Object[0]));
                            } else {
                                return;
                            }
                        default:
                            return;
                    }
                case 6:
                    PdfDictionary gs = (PdfDictionary) obj1;
                    if (gs != null) {
                        PdfObject obj = gs.get(PdfName.BM);
                        if (obj == null || PdfGState.BM_NORMAL.equals(obj) || PdfGState.BM_COMPATIBLE.equals(obj)) {
                            obj = gs.get(PdfName.CA);
                            if (obj != null) {
                                if (((PdfNumber) obj).doubleValue() != 1.0d) {
                                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(((PdfNumber) obj).doubleValue())));
                                }
                            }
                            obj = gs.get(PdfName.ca);
                            if (obj != null) {
                                if (((PdfNumber) obj).doubleValue() != 1.0d) {
                                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(((PdfNumber) obj).doubleValue())));
                                }
                                return;
                            }
                            return;
                        }
                        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
                    }
                    return;
                case 7:
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed", new Object[0]));
                default:
                    return;
            }
        }
    }
}
