package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class PdfWriter$PdfBody {
    private static final int OBJSINSTREAM = 200;
    protected int currentObjNum;
    protected ByteBuffer index;
    protected int numObj = 0;
    protected long position;
    protected int refnum;
    protected ByteBuffer streamObjects;
    protected final PdfWriter writer;
    protected final TreeSet<PdfCrossReference> xrefs = new TreeSet();

    public static class PdfCrossReference implements Comparable<PdfCrossReference> {
        private final int generation;
        private final long offset;
        private final int refnum;
        private final int type;

        public PdfCrossReference(int refnum, long offset, int generation) {
            this.type = 0;
            this.offset = offset;
            this.refnum = refnum;
            this.generation = generation;
        }

        public PdfCrossReference(int refnum, long offset) {
            this.type = 1;
            this.offset = offset;
            this.refnum = refnum;
            this.generation = 0;
        }

        public PdfCrossReference(int type, int refnum, long offset, int generation) {
            this.type = type;
            this.offset = offset;
            this.refnum = refnum;
            this.generation = generation;
        }

        public int getRefnum() {
            return this.refnum;
        }

        public void toPdf(OutputStream os) throws IOException {
            StringBuffer off = new StringBuffer("0000000000").append(this.offset);
            off.delete(0, off.length() - 10);
            StringBuffer gen = new StringBuffer("00000").append(this.generation);
            gen.delete(0, gen.length() - 5);
            off.append(' ').append(gen).append(this.generation == 65535 ? " f \n" : " n \n");
            os.write(DocWriter.getISOBytes(off.toString()));
        }

        public void toPdf(int midSize, OutputStream os) throws IOException {
            os.write((byte) this.type);
            while (true) {
                midSize--;
                if (midSize >= 0) {
                    os.write((byte) ((int) ((this.offset >>> (midSize * 8)) & 255)));
                } else {
                    os.write((byte) ((this.generation >>> 8) & 255));
                    os.write((byte) (this.generation & 255));
                    return;
                }
            }
        }

        public int compareTo(PdfCrossReference other) {
            if (this.refnum < other.refnum) {
                return -1;
            }
            return this.refnum == other.refnum ? 0 : 1;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PdfCrossReference)) {
                return false;
            }
            if (this.refnum == ((PdfCrossReference) obj).refnum) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.refnum;
        }
    }

    protected PdfWriter$PdfBody(PdfWriter writer) {
        this.xrefs.add(new PdfCrossReference(0, 0, 65535));
        this.position = writer.getOs().getCounter();
        this.refnum = 1;
        this.writer = writer;
    }

    void setRefnum(int refnum) {
        this.refnum = refnum;
    }

    protected PdfCrossReference addToObjStm(PdfObject obj, int nObj) throws IOException {
        if (this.numObj >= 200) {
            flushObjStm();
        }
        if (this.index == null) {
            this.index = new ByteBuffer();
            this.streamObjects = new ByteBuffer();
            this.currentObjNum = getIndirectReferenceNumber();
            this.numObj = 0;
        }
        int p = this.streamObjects.size();
        int idx = this.numObj;
        this.numObj = idx + 1;
        PdfEncryption enc = this.writer.crypto;
        this.writer.crypto = null;
        obj.toPdf(this.writer, this.streamObjects);
        this.writer.crypto = enc;
        this.streamObjects.append(' ');
        this.index.append(nObj).append(' ').append(p).append(' ');
        return new PdfCrossReference(2, nObj, (long) this.currentObjNum, idx);
    }

    public void flushObjStm() throws IOException {
        if (this.numObj != 0) {
            int first = this.index.size();
            this.index.append(this.streamObjects);
            PdfObject stream = new PdfStream(this.index.toByteArray());
            stream.flateCompress(this.writer.getCompressionLevel());
            stream.put(PdfName.TYPE, PdfName.OBJSTM);
            stream.put(PdfName.f128N, new PdfNumber(this.numObj));
            stream.put(PdfName.FIRST, new PdfNumber(first));
            add(stream, this.currentObjNum);
            this.index = null;
            this.streamObjects = null;
            this.numObj = 0;
        }
    }

    PdfIndirectObject add(PdfObject object) throws IOException {
        return add(object, getIndirectReferenceNumber());
    }

    PdfIndirectObject add(PdfObject object, boolean inObjStm) throws IOException {
        return add(object, getIndirectReferenceNumber(), 0, inObjStm);
    }

    public PdfIndirectReference getPdfIndirectReference() {
        return new PdfIndirectReference(0, getIndirectReferenceNumber());
    }

    protected int getIndirectReferenceNumber() {
        int n = this.refnum;
        this.refnum = n + 1;
        this.xrefs.add(new PdfCrossReference(n, 0, 65535));
        return n;
    }

    PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) throws IOException {
        return add(object, ref, true);
    }

    PdfIndirectObject add(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
        return add(object, ref.getNumber(), ref.getGeneration(), inObjStm);
    }

    PdfIndirectObject add(PdfObject object, int refNumber) throws IOException {
        return add(object, refNumber, 0, true);
    }

    protected PdfIndirectObject add(PdfObject object, int refNumber, int generation, boolean inObjStm) throws IOException {
        PdfIndirectObject indirect;
        if (inObjStm && object.canBeInObjStm() && this.writer.isFullCompression()) {
            PdfCrossReference pxref = addToObjStm(object, refNumber);
            indirect = new PdfIndirectObject(refNumber, object, this.writer);
            if (this.xrefs.add(pxref)) {
                return indirect;
            }
            this.xrefs.remove(pxref);
            this.xrefs.add(pxref);
            return indirect;
        } else if (this.writer.isFullCompression()) {
            indirect = new PdfIndirectObject(refNumber, object, this.writer);
            write(indirect, refNumber);
            return indirect;
        } else {
            indirect = new PdfIndirectObject(refNumber, generation, object, this.writer);
            write(indirect, refNumber, generation);
            return indirect;
        }
    }

    protected void write(PdfIndirectObject indirect, int refNumber) throws IOException {
        PdfCrossReference pxref = new PdfCrossReference(refNumber, this.position);
        if (!this.xrefs.add(pxref)) {
            this.xrefs.remove(pxref);
            this.xrefs.add(pxref);
        }
        indirect.writeTo(this.writer.getOs());
        this.position = this.writer.getOs().getCounter();
    }

    protected void write(PdfIndirectObject indirect, int refNumber, int generation) throws IOException {
        PdfCrossReference pxref = new PdfCrossReference(refNumber, this.position, generation);
        if (!this.xrefs.add(pxref)) {
            this.xrefs.remove(pxref);
            this.xrefs.add(pxref);
        }
        indirect.writeTo(this.writer.getOs());
        this.position = this.writer.getOs().getCounter();
    }

    public long offset() {
        return this.position;
    }

    public int size() {
        return Math.max(((PdfCrossReference) this.xrefs.last()).getRefnum() + 1, this.refnum);
    }

    public void writeCrossReferenceTable(OutputStream os, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, long prevxref) throws IOException {
        int refNumber = 0;
        if (this.writer.isFullCompression()) {
            flushObjStm();
            refNumber = getIndirectReferenceNumber();
            this.xrefs.add(new PdfCrossReference(refNumber, this.position));
        }
        int first = ((PdfCrossReference) this.xrefs.first()).getRefnum();
        int len = 0;
        ArrayList<Integer> sections = new ArrayList();
        Iterator i$ = this.xrefs.iterator();
        while (i$.hasNext()) {
            PdfCrossReference entry = (PdfCrossReference) i$.next();
            if (first + len == entry.getRefnum()) {
                len++;
            } else {
                sections.add(Integer.valueOf(first));
                sections.add(Integer.valueOf(len));
                first = entry.getRefnum();
                len = 1;
            }
        }
        sections.add(Integer.valueOf(first));
        sections.add(Integer.valueOf(len));
        int k;
        if (this.writer.isFullCompression()) {
            int mid = 5;
            long mask = 1095216660480L;
            while (mid > 1 && (this.position & mask) == 0) {
                mask >>>= 8;
                mid--;
            }
            ByteBuffer buf = new ByteBuffer();
            i$ = this.xrefs.iterator();
            while (i$.hasNext()) {
                ((PdfCrossReference) i$.next()).toPdf(mid, buf);
            }
            PdfObject xr = new PdfStream(buf.toByteArray());
            xr.flateCompress(this.writer.getCompressionLevel());
            xr.put(PdfName.SIZE, new PdfNumber(size()));
            xr.put(PdfName.ROOT, root);
            if (info != null) {
                xr.put(PdfName.INFO, info);
            }
            if (encryption != null) {
                xr.put(PdfName.ENCRYPT, encryption);
            }
            if (fileID != null) {
                xr.put(PdfName.ID, fileID);
            }
            xr.put(PdfName.f137W, new PdfArray(new int[]{1, mid, 2}));
            xr.put(PdfName.TYPE, PdfName.XREF);
            PdfArray idx = new PdfArray();
            for (k = 0; k < sections.size(); k++) {
                idx.add(new PdfNumber(((Integer) sections.get(k)).intValue()));
            }
            xr.put(PdfName.INDEX, idx);
            if (prevxref > 0) {
                xr.put(PdfName.PREV, new PdfNumber(prevxref));
            }
            PdfEncryption enc = this.writer.crypto;
            this.writer.crypto = null;
            new PdfIndirectObject(refNumber, xr, this.writer).writeTo(this.writer.getOs());
            this.writer.crypto = enc;
            return;
        }
        os.write(DocWriter.getISOBytes("xref\n"));
        Iterator<PdfCrossReference> i = this.xrefs.iterator();
        for (k = 0; k < sections.size(); k += 2) {
            first = ((Integer) sections.get(k)).intValue();
            len = ((Integer) sections.get(k + 1)).intValue();
            os.write(DocWriter.getISOBytes(String.valueOf(first)));
            os.write(DocWriter.getISOBytes(" "));
            os.write(DocWriter.getISOBytes(String.valueOf(len)));
            os.write(10);
            int len2 = len;
            while (true) {
                len = len2 - 1;
                if (len2 <= 0) {
                    break;
                }
                ((PdfCrossReference) i.next()).toPdf(os);
                len2 = len;
            }
        }
    }
}
