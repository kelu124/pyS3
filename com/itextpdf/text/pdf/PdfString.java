package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class PdfString extends PdfObject {
    protected String encoding = PdfObject.TEXT_PDFDOCENCODING;
    protected boolean hexWriting = false;
    protected int objGen = 0;
    protected int objNum = 0;
    protected String originalValue = null;
    protected String value = "";

    public PdfString() {
        super(3);
    }

    public PdfString(String value) {
        super(3);
        this.value = value;
    }

    public PdfString(String value, String encoding) {
        super(3);
        this.value = value;
        this.encoding = encoding;
    }

    public PdfString(byte[] bytes) {
        super(3);
        this.value = PdfEncodings.convertToString(bytes, null);
        this.encoding = "";
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, 11, this);
        byte[] b = getBytes();
        PdfEncryption crypto = null;
        if (writer != null) {
            crypto = writer.getEncryption();
        }
        if (!(crypto == null || crypto.isEmbeddedFilesOnly())) {
            b = crypto.encryptByteArray(b);
        }
        if (this.hexWriting) {
            ByteBuffer buf = new ByteBuffer();
            buf.append('<');
            for (byte appendHex : b) {
                buf.appendHex(appendHex);
            }
            buf.append('>');
            os.write(buf.toByteArray());
            return;
        }
        os.write(StringUtils.escapeString(b));
    }

    public String toString() {
        return this.value;
    }

    public byte[] getBytes() {
        if (this.bytes == null) {
            if (this.encoding != null && this.encoding.equals(PdfObject.TEXT_UNICODE) && PdfEncodings.isPdfDocEncoding(this.value)) {
                this.bytes = PdfEncodings.convertToBytes(this.value, PdfObject.TEXT_PDFDOCENCODING);
            } else {
                this.bytes = PdfEncodings.convertToBytes(this.value, this.encoding);
            }
        }
        return this.bytes;
    }

    public String toUnicodeString() {
        if (this.encoding != null && this.encoding.length() != 0) {
            return this.value;
        }
        getBytes();
        if (this.bytes.length >= 2 && this.bytes[0] == (byte) -2 && this.bytes[1] == (byte) -1) {
            return PdfEncodings.convertToString(this.bytes, PdfObject.TEXT_UNICODE);
        }
        return PdfEncodings.convertToString(this.bytes, PdfObject.TEXT_PDFDOCENCODING);
    }

    public String getEncoding() {
        return this.encoding;
    }

    void setObjNum(int objNum, int objGen) {
        this.objNum = objNum;
        this.objGen = objGen;
    }

    void decrypt(PdfReader reader) {
        PdfEncryption decrypt = reader.getDecrypt();
        if (decrypt != null) {
            this.originalValue = this.value;
            decrypt.setHashKey(this.objNum, this.objGen);
            this.bytes = PdfEncodings.convertToBytes(this.value, null);
            this.bytes = decrypt.decryptByteArray(this.bytes);
            this.value = PdfEncodings.convertToString(this.bytes, null);
        }
    }

    public byte[] getOriginalBytes() {
        if (this.originalValue == null) {
            return getBytes();
        }
        return PdfEncodings.convertToBytes(this.originalValue, null);
    }

    public PdfString setHexWriting(boolean hexWriting) {
        this.hexWriting = hexWriting;
        return this;
    }

    public boolean isHexWriting() {
        return this.hexWriting;
    }
}
