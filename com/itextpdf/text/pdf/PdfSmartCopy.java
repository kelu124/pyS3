package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

public class PdfSmartCopy extends PdfCopy {
    protected Counter COUNTER;
    private final HashMap<RefKey, Integer> serialized;
    private HashMap<ByteStore, PdfIndirectReference> streamMap;

    static class ByteStore {
        private final byte[] f140b;
        private final int hash;
        private MessageDigest md5;

        private void serObject(PdfObject obj, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            if (level > 0) {
                if (obj == null) {
                    bb.append("$Lnull");
                    return;
                }
                RefKey key;
                PdfIndirectReference ref = null;
                ByteBuffer savedBb = null;
                if (obj.isIndirect()) {
                    ref = (PdfIndirectReference) obj;
                    key = new RefKey(ref);
                    if (serialized.containsKey(key)) {
                        bb.append(((Integer) serialized.get(key)).intValue());
                        return;
                    } else {
                        savedBb = bb;
                        bb = new ByteBuffer();
                    }
                }
                obj = PdfReader.getPdfObject(obj);
                if (obj.isStream()) {
                    bb.append("$B");
                    serDic((PdfDictionary) obj, level - 1, bb, serialized);
                    if (level > 0) {
                        this.md5.reset();
                        bb.append(this.md5.digest(PdfReader.getStreamBytesRaw((PRStream) obj)));
                    }
                } else if (obj.isDictionary()) {
                    serDic((PdfDictionary) obj, level - 1, bb, serialized);
                } else if (obj.isArray()) {
                    serArray((PdfArray) obj, level - 1, bb, serialized);
                } else if (obj.isString()) {
                    bb.append("$S").append(obj.toString());
                } else if (obj.isName()) {
                    bb.append("$N").append(obj.toString());
                } else {
                    bb.append("$L").append(obj.toString());
                }
                if (savedBb != null) {
                    key = new RefKey(ref);
                    if (!serialized.containsKey(key)) {
                        serialized.put(key, Integer.valueOf(calculateHash(bb.getBuffer())));
                    }
                    savedBb.append(bb);
                }
            }
        }

        private void serDic(PdfDictionary dic, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            bb.append("$D");
            if (level > 0) {
                Object[] keys = dic.getKeys().toArray();
                Arrays.sort(keys);
                for (int k = 0; k < keys.length; k++) {
                    serObject((PdfObject) keys[k], level, bb, serialized);
                    serObject(dic.get((PdfName) keys[k]), level, bb, serialized);
                }
            }
        }

        private void serArray(PdfArray array, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            bb.append("$A");
            if (level > 0) {
                for (int k = 0; k < array.size(); k++) {
                    serObject(array.getPdfObject(k), level, bb, serialized);
                }
            }
        }

        ByteStore(PRStream str, HashMap<RefKey, Integer> serialized) throws IOException {
            try {
                this.md5 = MessageDigest.getInstance("MD5");
                ByteBuffer bb = new ByteBuffer();
                serObject(str, 100, bb, serialized);
                this.f140b = bb.toByteArray();
                this.hash = calculateHash(this.f140b);
                this.md5 = null;
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        ByteStore(PdfDictionary dict, HashMap<RefKey, Integer> serialized) throws IOException {
            try {
                this.md5 = MessageDigest.getInstance("MD5");
                ByteBuffer bb = new ByteBuffer();
                serObject(dict, 100, bb, serialized);
                this.f140b = bb.toByteArray();
                this.hash = calculateHash(this.f140b);
                this.md5 = null;
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        private static int calculateHash(byte[] b) {
            int hash = 0;
            for (byte b2 : b) {
                hash = (hash * 31) + (b2 & 255);
            }
            return hash;
        }

        public boolean equals(Object obj) {
            if ((obj instanceof ByteStore) && hashCode() == obj.hashCode()) {
                return Arrays.equals(this.f140b, ((ByteStore) obj).f140b);
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    protected Counter getCounter() {
        return this.COUNTER;
    }

    public PdfSmartCopy(Document document, OutputStream os) throws DocumentException {
        super(document, os);
        this.streamMap = null;
        this.serialized = new HashMap();
        this.COUNTER = CounterFactory.getCounter(PdfSmartCopy.class);
        this.streamMap = new HashMap();
    }

    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        PdfIndirectReference theRef;
        PdfObject srcObj = PdfReader.getPdfObjectRelease((PdfObject) in);
        ByteStore streamKey = null;
        boolean validStream = false;
        PdfIndirectReference streamRef;
        if (srcObj.isStream()) {
            streamKey = new ByteStore((PRStream) srcObj, this.serialized);
            validStream = true;
            streamRef = (PdfIndirectReference) this.streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        } else if (srcObj.isDictionary()) {
            streamKey = new ByteStore((PdfDictionary) srcObj, this.serialized);
            validStream = true;
            streamRef = (PdfIndirectReference) this.streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }
        RefKey key = new RefKey(in);
        IndirectReferences iRef = (IndirectReferences) this.indirects.get(key);
        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                return theRef;
            }
        }
        theRef = this.body.getPdfIndirectReference();
        iRef = new IndirectReferences(theRef);
        this.indirects.put(key, iRef);
        if (srcObj.isDictionary()) {
            PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary) srcObj).get(PdfName.TYPE));
            if (type != null && PdfName.PAGE.equals(type)) {
                return theRef;
            }
        }
        iRef.setCopied();
        if (validStream) {
            this.streamMap.put(streamKey, theRef);
        }
        addToBody(copyObject(srcObj), theRef);
        return theRef;
    }

    public void freeReader(PdfReader reader) throws IOException {
        this.serialized.clear();
        super.freeReader(reader);
    }

    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        if (this.currentPdfReaderInstance.getReader() != this.reader) {
            this.serialized.clear();
        }
        super.addPage(iPage);
    }
}
