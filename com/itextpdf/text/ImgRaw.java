package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.net.URL;

public class ImgRaw extends Image {
    ImgRaw(Image image) {
        super(image);
    }

    public ImgRaw(int width, int height, int components, int bpc, byte[] data) throws BadElementException {
        super((URL) null);
        this.type = 34;
        this.scaledHeight = (float) height;
        setTop(this.scaledHeight);
        this.scaledWidth = (float) width;
        setRight(this.scaledWidth);
        if (components != 1 && components != 3 && components != 4) {
            throw new BadElementException(MessageLocalization.getComposedMessage("components.must.be.1.3.or.4", new Object[0]));
        } else if (bpc == 1 || bpc == 2 || bpc == 4 || bpc == 8) {
            this.colorspace = components;
            this.bpc = bpc;
            this.rawData = data;
            this.plainWidth = getWidth();
            this.plainHeight = getHeight();
        } else {
            throw new BadElementException(MessageLocalization.getComposedMessage("bits.per.component.must.be.1.2.4.or.8", new Object[0]));
        }
    }
}
