package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
import java.net.URL;

public class ImgCCITT extends Image {
    ImgCCITT(Image image) {
        super(image);
    }

    public ImgCCITT(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException {
        super((URL) null);
        if (typeCCITT == 256 || typeCCITT == 257 || typeCCITT == 258) {
            if (reverseBits) {
                TIFFFaxDecoder.reverseBits(data);
            }
            this.type = 34;
            this.scaledHeight = (float) height;
            setTop(this.scaledHeight);
            this.scaledWidth = (float) width;
            setRight(this.scaledWidth);
            this.colorspace = parameters;
            this.bpc = typeCCITT;
            this.rawData = data;
            this.plainWidth = getWidth();
            this.plainHeight = getHeight();
            return;
        }
        throw new BadElementException(MessageLocalization.getComposedMessage("the.ccitt.compression.type.must.be.ccittg4.ccittg3.1d.or.ccittg3.2d", new Object[0]));
    }
}
