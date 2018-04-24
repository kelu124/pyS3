package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PdfEFStream extends PdfStream {
    public PdfEFStream(InputStream in, PdfWriter writer) {
        super(in, writer);
    }

    public PdfEFStream(byte[] fileStore) {
        super(fileStore);
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfObject filter;
        if (this.inputStream != null && this.compressed) {
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        }
        PdfEncryption crypto = null;
        if (writer != null) {
            crypto = writer.getEncryption();
        }
        if (crypto != null) {
            filter = get(PdfName.FILTER);
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
        if (crypto != null && crypto.isEmbeddedFilesOnly()) {
            filter = new PdfArray();
            PdfArray decodeparms = new PdfArray();
            PdfDictionary crypt = new PdfDictionary();
            crypt.put(PdfName.NAME, PdfName.STDCF);
            filter.add(PdfName.CRYPT);
            decodeparms.add(crypt);
            if (this.compressed) {
                filter.add(PdfName.FLATEDECODE);
                decodeparms.add(new PdfNull());
            }
            put(PdfName.FILTER, filter);
            put(PdfName.DECODEPARMS, decodeparms);
        }
        PdfObject nn = get(PdfName.LENGTH);
        if (crypto == null || nn == null || !nn.isNumber()) {
            superToPdf(writer, os);
        } else {
            int sz = ((PdfNumber) nn).intValue();
            put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
            superToPdf(writer, os);
            put(PdfName.LENGTH, nn);
        }
        os.write(STARTSTREAM);
        if (this.inputStream != null) {
            this.rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStream outputStreamCounter = new OutputStreamCounter(os);
            OutputStreamEncryption ose = null;
            OutputStream fout = outputStreamCounter;
            if (crypto != null) {
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
            this.inputStreamLength = (int) outputStreamCounter.getCounter();
        } else if (crypto != null) {
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
}
