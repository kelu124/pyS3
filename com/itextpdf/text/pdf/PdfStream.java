package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PdfStream extends PdfDictionary {
    public static final int BEST_COMPRESSION = 9;
    public static final int BEST_SPEED = 1;
    public static final int DEFAULT_COMPRESSION = -1;
    static final byte[] ENDSTREAM = DocWriter.getISOBytes("\nendstream");
    public static final int NO_COMPRESSION = 0;
    static final int SIZESTREAM = (STARTSTREAM.length + ENDSTREAM.length);
    static final byte[] STARTSTREAM = DocWriter.getISOBytes("stream\n");
    protected boolean compressed;
    protected int compressionLevel;
    protected InputStream inputStream;
    protected int inputStreamLength;
    protected int rawLength;
    protected PdfIndirectReference ref;
    protected ByteArrayOutputStream streamBytes;
    protected PdfWriter writer;

    public PdfStream(byte[] bytes) {
        this.compressed = false;
        this.compressionLevel = 0;
        this.streamBytes = null;
        this.inputStreamLength = -1;
        this.type = 7;
        this.bytes = bytes;
        this.rawLength = bytes.length;
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }

    public PdfStream(InputStream inputStream, PdfWriter writer) {
        this.compressed = false;
        this.compressionLevel = 0;
        this.streamBytes = null;
        this.inputStreamLength = -1;
        this.type = 7;
        this.inputStream = inputStream;
        this.writer = writer;
        this.ref = writer.getPdfIndirectReference();
        put(PdfName.LENGTH, this.ref);
    }

    protected PdfStream() {
        this.compressed = false;
        this.compressionLevel = 0;
        this.streamBytes = null;
        this.inputStreamLength = -1;
        this.type = 7;
    }

    public void writeLength() throws IOException {
        if (this.inputStream == null) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter", new Object[0]));
        } else if (this.inputStreamLength == -1) {
            throw new IOException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.after.output.of.the.stream.body", new Object[0]));
        } else {
            this.writer.addToBody(new PdfNumber(this.inputStreamLength), this.ref, false);
        }
    }

    public int getRawLength() {
        return this.rawLength;
    }

    public void flateCompress() {
        flateCompress(-1);
    }

    public void flateCompress(int compressionLevel) {
        if (Document.compress && !this.compressed) {
            this.compressionLevel = compressionLevel;
            if (this.inputStream != null) {
                this.compressed = true;
                return;
            }
            PdfObject filter = PdfReader.getPdfObject(get(PdfName.FILTER));
            if (filter != null) {
                if (filter.isName()) {
                    if (PdfName.FLATEDECODE.equals(filter)) {
                        return;
                    }
                } else if (!filter.isArray()) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("stream.could.not.be.compressed.filter.is.not.a.name.or.array", new Object[0]));
                } else if (((PdfArray) filter).contains(PdfName.FLATEDECODE)) {
                    return;
                }
            }
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Deflater deflater = new Deflater(compressionLevel);
                DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
                if (this.streamBytes != null) {
                    this.streamBytes.writeTo(zip);
                } else {
                    zip.write(this.bytes);
                }
                zip.close();
                deflater.end();
                this.streamBytes = stream;
                this.bytes = null;
                put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
                if (filter == null) {
                    put(PdfName.FILTER, PdfName.FLATEDECODE);
                } else {
                    PdfArray filters = new PdfArray(filter);
                    filters.add(0, PdfName.FLATEDECODE);
                    put(PdfName.FILTER, filters);
                }
                this.compressed = true;
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    }

    protected void superToPdf(PdfWriter writer, OutputStream os) throws IOException {
        super.toPdf(writer, os);
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (this.inputStream != null && this.compressed) {
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        }
        PdfEncryption crypto = null;
        if (writer != null) {
            crypto = writer.getEncryption();
        }
        if (crypto != null) {
            PdfObject filter = get(PdfName.FILTER);
            if (filter != null) {
                if (PdfName.CRYPT.equals(filter)) {
                    crypto = null;
                } else if (filter.isArray()) {
                    PdfArray a = (PdfArray) filter;
                    if (!a.isEmpty() && PdfName.CRYPT.equals(a.getPdfObject(0))) {
                        crypto = null;
                    }
                }
            }
        }
        PdfObject nn = get(PdfName.LENGTH);
        if (crypto == null || nn == null || !nn.isNumber()) {
            superToPdf(writer, os);
        } else {
            put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(((PdfNumber) nn).intValue())));
            superToPdf(writer, os);
            put(PdfName.LENGTH, nn);
        }
        PdfWriter.checkPdfIsoConformance(writer, 9, this);
        os.write(STARTSTREAM);
        if (this.inputStream != null) {
            this.rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStream osc = new OutputStreamCounter(os);
            OutputStreamEncryption ose = null;
            OutputStream fout = osc;
            if (!(crypto == null || crypto.isEmbeddedFilesOnly())) {
                ose = crypto.getEncryptionStream(fout);
                fout = ose;
            }
            Deflater deflater = null;
            if (this.compressed) {
                deflater = new Deflater(this.compressionLevel);
                def = new DeflaterOutputStream(fout, deflater, 32768);
                fout = def;
            }
            byte[] buf = new byte[4192];
            while (true) {
                int n = this.inputStream.read(buf);
                if (n <= 0) {
                    break;
                }
                fout.write(buf, 0, n);
                this.rawLength += n;
            }
            if (def != null) {
                def.finish();
                deflater.end();
            }
            if (ose != null) {
                ose.finish();
            }
            this.inputStreamLength = (int) osc.getCounter();
        } else if (crypto != null && !crypto.isEmbeddedFilesOnly()) {
            byte[] b;
            if (this.streamBytes != null) {
                b = crypto.encryptByteArray(this.streamBytes.toByteArray());
            } else {
                b = crypto.encryptByteArray(this.bytes);
            }
            os.write(b);
        } else if (this.streamBytes != null) {
            this.streamBytes.writeTo(os);
        } else {
            os.write(this.bytes);
        }
        os.write(ENDSTREAM);
    }

    public void writeContent(OutputStream os) throws IOException {
        if (this.streamBytes != null) {
            this.streamBytes.writeTo(os);
        } else if (this.bytes != null) {
            os.write(this.bytes);
        }
    }

    public String toString() {
        if (get(PdfName.TYPE) == null) {
            return "Stream";
        }
        return "Stream of type: " + get(PdfName.TYPE);
    }
}
