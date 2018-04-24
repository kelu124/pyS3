package com.itextpdf.text.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
import com.itextpdf.text.pdf.qrcode.ByteMatrix;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.qrcode.WriterException;
import java.util.Map;

public class BarcodeQRCode {
    ByteMatrix bm;

    public BarcodeQRCode(String content, int width, int height, Map<EncodeHintType, Object> hints) {
        try {
            this.bm = new QRCodeWriter().encode(content, width, height, hints);
        } catch (WriterException ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private byte[] getBitMatrix() {
        int width = this.bm.getWidth();
        int height = this.bm.getHeight();
        int stride = (width + 7) / 8;
        byte[] b = new byte[(stride * height)];
        byte[][] mt = this.bm.getArray();
        for (int y = 0; y < height; y++) {
            byte[] line = mt[y];
            for (int x = 0; x < width; x++) {
                if (line[x] != (byte) 0) {
                    int offset = (stride * y) + (x / 8);
                    b[offset] = (byte) (b[offset] | ((byte) (128 >> (x % 8))));
                }
            }
        }
        return b;
    }

    public Image getImage() throws BadElementException {
        return Image.getInstance(this.bm.getWidth(), this.bm.getHeight(), false, 256, 1, CCITTG4Encoder.compress(getBitMatrix(), this.bm.getWidth(), this.bm.getHeight()), null);
    }
}
