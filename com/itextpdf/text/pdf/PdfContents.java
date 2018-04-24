package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

class PdfContents extends PdfStream {
    static final byte[] RESTORESTATE = DocWriter.getISOBytes("Q\n");
    static final byte[] ROTATE180 = DocWriter.getISOBytes("-1 0 0 -1 ");
    static final byte[] ROTATE270 = DocWriter.getISOBytes("0 -1 1 0 ");
    static final byte[] ROTATE90 = DocWriter.getISOBytes("0 1 -1 0 ");
    static final byte[] ROTATEFINAL = DocWriter.getISOBytes(" cm\n");
    static final byte[] SAVESTATE = DocWriter.getISOBytes("q\n");

    PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent, Rectangle page) throws BadPdfFormatException {
        Exception e;
        Deflater deflater = null;
        try {
            OutputStream out;
            this.streamBytes = new ByteArrayOutputStream();
            if (Document.compress) {
                this.compressed = true;
                if (text != null) {
                    this.compressionLevel = text.getPdfWriter().getCompressionLevel();
                } else if (content != null) {
                    this.compressionLevel = content.getPdfWriter().getCompressionLevel();
                }
                Deflater deflater2 = new Deflater(this.compressionLevel);
                try {
                    deflater = deflater2;
                    out = new DeflaterOutputStream(this.streamBytes, deflater2);
                } catch (Exception e2) {
                    e = e2;
                    deflater = deflater2;
                    throw new BadPdfFormatException(e.getMessage());
                }
            }
            out = this.streamBytes;
            switch (page.getRotation()) {
                case 90:
                    out.write(ROTATE90);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble((double) page.getTop())));
                    out.write(32);
                    out.write(48);
                    out.write(ROTATEFINAL);
                    break;
                case 180:
                    out.write(ROTATE180);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble((double) page.getRight())));
                    out.write(32);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble((double) page.getTop())));
                    out.write(ROTATEFINAL);
                    break;
                case 270:
                    out.write(ROTATE270);
                    out.write(48);
                    out.write(32);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble((double) page.getRight())));
                    out.write(ROTATEFINAL);
                    break;
            }
            if (under.size() > 0) {
                out.write(SAVESTATE);
                under.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (content.size() > 0) {
                out.write(SAVESTATE);
                content.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (text != null) {
                out.write(SAVESTATE);
                text.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (secondContent.size() > 0) {
                secondContent.getInternalBuffer().writeTo(out);
            }
            out.close();
            if (deflater != null) {
                deflater.end();
            }
            put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
            if (this.compressed) {
                put(PdfName.FILTER, PdfName.FLATEDECODE);
            }
        } catch (Exception e3) {
            e = e3;
            throw new BadPdfFormatException(e.getMessage());
        }
    }
}
