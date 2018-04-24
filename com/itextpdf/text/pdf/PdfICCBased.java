package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfICCBased extends PdfStream {
    public PdfICCBased(ICC_Profile profile) {
        this(profile, -1);
    }

    public PdfICCBased(ICC_Profile profile, int compressionLevel) {
        try {
            int numberOfComponents = profile.getNumComponents();
            switch (numberOfComponents) {
                case 1:
                    put(PdfName.ALTERNATE, PdfName.DEVICEGRAY);
                    break;
                case 3:
                    put(PdfName.ALTERNATE, PdfName.DEVICERGB);
                    break;
                case 4:
                    put(PdfName.ALTERNATE, PdfName.DEVICECMYK);
                    break;
                default:
                    throw new PdfException(MessageLocalization.getComposedMessage("1.component.s.is.not.supported", numberOfComponents));
            }
            put(PdfName.f128N, new PdfNumber(numberOfComponents));
            this.bytes = profile.getData();
            put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
            flateCompress(compressionLevel);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
