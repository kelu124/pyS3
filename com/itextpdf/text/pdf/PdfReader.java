package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.io.WindowRandomAccessSource;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.FilterHandlers.FilterHandler;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import com.itextpdf.text.pdf.PdfAnnotation.PdfImportedLink;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
import com.itextpdf.text.pdf.internal.PdfViewerPreferencesImp;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDecryptionProcess;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.zip.InflaterInputStream;
import org.bytedeco.javacpp.dc1394;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cms.CMSEnvelopedData;
import org.spongycastle.cms.RecipientInformation;

public class PdfReader implements PdfViewerPreferences {
    protected static Counter COUNTER = CounterFactory.getCounter(PdfReader.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfReader.class);
    public static boolean debugmode = false;
    static final byte[] endobj = PdfEncodings.convertToBytes("endobj", null);
    static final byte[] endstream = PdfEncodings.convertToBytes("endstream", null);
    static final PdfName[] pageInhCandidates = new PdfName[]{PdfName.MEDIABOX, PdfName.ROTATE, PdfName.RESOURCES, PdfName.CROPBOX};
    public static boolean unethicalreading = false;
    protected PRAcroForm acroForm;
    protected boolean acroFormParsed;
    private boolean appendable;
    protected PdfDictionary catalog;
    protected Certificate certificate;
    protected Key certificateKey;
    protected String certificateKeyProvider;
    protected boolean consolidateNamedDestinations;
    private PRIndirectReference cryptoRef;
    protected PdfEncryption decrypt;
    protected boolean encrypted;
    private boolean encryptionError;
    protected long eofPos;
    protected ExternalDecryptionProcess externalDecryptionProcess;
    private long fileLength;
    protected int freeXref;
    private boolean hybridXref;
    protected long lastXref;
    private int lastXrefPartial;
    protected boolean newXrefType;
    private int objGen;
    private int objNum;
    protected HashMap<Integer, IntHashtable> objStmMark;
    protected LongHashtable objStmToOffset;
    private boolean ownerPasswordUsed;
    protected long pValue;
    protected PageRefs pageRefs;
    private boolean partial;
    protected byte[] password;
    protected char pdfVersion;
    protected int rValue;
    private int readDepth;
    protected boolean rebuilt;
    protected boolean remoteToLocalNamedDestinations;
    PdfDictionary rootPages;
    protected boolean sharedStreams;
    protected ArrayList<PdfString> strings;
    protected boolean tampered;
    protected PRTokeniser tokens;
    protected PdfDictionary trailer;
    private final PdfViewerPreferencesImp viewerPreferences;
    protected long[] xref;
    protected ArrayList<PdfObject> xrefObj;

    static class PageRefs {
        private boolean keepPages;
        private int lastPageRead;
        private ArrayList<PdfDictionary> pageInh;
        private final PdfReader reader;
        private ArrayList<PRIndirectReference> refsn;
        private IntHashtable refsp;
        private int sizep;

        private PageRefs(PdfReader reader) throws IOException {
            this.lastPageRead = -1;
            this.reader = reader;
            if (reader.partial) {
                this.refsp = new IntHashtable();
                this.sizep = ((PdfNumber) PdfReader.getPdfObjectRelease(reader.rootPages.get(PdfName.COUNT))).intValue();
                return;
            }
            readPages();
        }

        PageRefs(PageRefs other, PdfReader reader) {
            this.lastPageRead = -1;
            this.reader = reader;
            this.sizep = other.sizep;
            if (other.refsn != null) {
                this.refsn = new ArrayList(other.refsn);
                for (int k = 0; k < this.refsn.size(); k++) {
                    this.refsn.set(k, (PRIndirectReference) PdfReader.duplicatePdfObject((PdfObject) this.refsn.get(k), reader));
                }
                return;
            }
            this.refsp = (IntHashtable) other.refsp.clone();
        }

        int size() {
            if (this.refsn != null) {
                return this.refsn.size();
            }
            return this.sizep;
        }

        void readPages() throws IOException {
            if (this.refsn == null) {
                this.refsp = null;
                this.refsn = new ArrayList();
                this.pageInh = new ArrayList();
                iteratePages((PRIndirectReference) this.reader.catalog.get(PdfName.PAGES));
                this.pageInh = null;
                this.reader.rootPages.put(PdfName.COUNT, new PdfNumber(this.refsn.size()));
            }
        }

        void reReadPages() throws IOException {
            this.refsn = null;
            readPages();
        }

        public PdfDictionary getPageN(int pageNum) {
            return (PdfDictionary) PdfReader.getPdfObject(getPageOrigRef(pageNum));
        }

        public PdfDictionary getPageNRelease(int pageNum) {
            PdfDictionary page = getPageN(pageNum);
            releasePage(pageNum);
            return page;
        }

        public PRIndirectReference getPageOrigRefRelease(int pageNum) {
            PRIndirectReference ref = getPageOrigRef(pageNum);
            releasePage(pageNum);
            return ref;
        }

        public PRIndirectReference getPageOrigRef(int pageNum) {
            pageNum--;
            if (pageNum >= 0) {
                try {
                    if (pageNum < size()) {
                        if (this.refsn != null) {
                            return (PRIndirectReference) this.refsn.get(pageNum);
                        }
                        int n = this.refsp.get(pageNum);
                        if (n == 0) {
                            PRIndirectReference ref = getSinglePage(pageNum);
                            if (this.reader.lastXrefPartial == -1) {
                                this.lastPageRead = -1;
                            } else {
                                this.lastPageRead = pageNum;
                            }
                            this.reader.lastXrefPartial = -1;
                            this.refsp.put(pageNum, ref.getNumber());
                            if (this.keepPages) {
                                this.lastPageRead = -1;
                            }
                            return ref;
                        }
                        if (this.lastPageRead != pageNum) {
                            this.lastPageRead = -1;
                        }
                        if (this.keepPages) {
                            this.lastPageRead = -1;
                        }
                        return new PRIndirectReference(this.reader, n);
                    }
                } catch (Exception e) {
                    throw new ExceptionConverter(e);
                }
            }
            return null;
        }

        void keepPages() {
            if (this.refsp != null && !this.keepPages) {
                this.keepPages = true;
                this.refsp.clear();
            }
        }

        public void releasePage(int pageNum) {
            if (this.refsp != null) {
                pageNum--;
                if (pageNum >= 0 && pageNum < size() && pageNum == this.lastPageRead) {
                    this.lastPageRead = -1;
                    this.reader.lastXrefPartial = this.refsp.get(pageNum);
                    this.reader.releaseLastXrefPartial();
                    this.refsp.remove(pageNum);
                }
            }
        }

        public void resetReleasePage() {
            if (this.refsp != null) {
                this.lastPageRead = -1;
            }
        }

        void insertPage(int pageNum, PRIndirectReference ref) {
            pageNum--;
            if (this.refsn == null) {
                this.sizep++;
                this.lastPageRead = -1;
                if (pageNum >= size()) {
                    this.refsp.put(size(), ref.getNumber());
                    return;
                }
                IntHashtable refs2 = new IntHashtable((this.refsp.size() + 1) * 2);
                Iterator<Entry> it = this.refsp.getEntryIterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    int p = entry.getKey();
                    if (p >= pageNum) {
                        p++;
                    }
                    refs2.put(p, entry.getValue());
                }
                refs2.put(pageNum, ref.getNumber());
                this.refsp = refs2;
            } else if (pageNum >= this.refsn.size()) {
                this.refsn.add(ref);
            } else {
                this.refsn.add(pageNum, ref);
            }
        }

        private void pushPageAttributes(PdfDictionary nodePages) {
            PdfDictionary dic = new PdfDictionary();
            if (!this.pageInh.isEmpty()) {
                dic.putAll((PdfDictionary) this.pageInh.get(this.pageInh.size() - 1));
            }
            for (int k = 0; k < PdfReader.pageInhCandidates.length; k++) {
                PdfObject obj = nodePages.get(PdfReader.pageInhCandidates[k]);
                if (obj != null) {
                    dic.put(PdfReader.pageInhCandidates[k], obj);
                }
            }
            this.pageInh.add(dic);
        }

        private void popPageAttributes() {
            this.pageInh.remove(this.pageInh.size() - 1);
        }

        private void iteratePages(PRIndirectReference rpage) throws IOException {
            PdfDictionary page = (PdfDictionary) PdfReader.getPdfObject((PdfObject) rpage);
            if (page != null) {
                PdfArray kidsPR = page.getAsArray(PdfName.KIDS);
                if (kidsPR == null) {
                    page.put(PdfName.TYPE, PdfName.PAGE);
                    PdfDictionary dic = (PdfDictionary) this.pageInh.get(this.pageInh.size() - 1);
                    for (PdfName key : dic.getKeys()) {
                        if (page.get(key) == null) {
                            page.put(key, dic.get(key));
                        }
                    }
                    if (page.get(PdfName.MEDIABOX) == null) {
                        page.put(PdfName.MEDIABOX, new PdfArray(new float[]{0.0f, 0.0f, PageSize.LETTER.getRight(), PageSize.LETTER.getTop()}));
                    }
                    this.refsn.add(rpage);
                    return;
                }
                page.put(PdfName.TYPE, PdfName.PAGES);
                pushPageAttributes(page);
                int k = 0;
                while (k < kidsPR.size()) {
                    PdfObject obj = kidsPR.getPdfObject(k);
                    if (obj.isIndirect()) {
                        iteratePages((PRIndirectReference) obj);
                        k++;
                    } else {
                        while (k < kidsPR.size()) {
                            kidsPR.remove(k);
                        }
                        popPageAttributes();
                    }
                }
                popPageAttributes();
            }
        }

        protected PRIndirectReference getSinglePage(int n) {
            PdfDictionary acc = new PdfDictionary();
            PdfDictionary top = this.reader.rootPages;
            int base = 0;
            while (true) {
                for (int k = 0; k < PdfReader.pageInhCandidates.length; k++) {
                    PdfObject obj = top.get(PdfReader.pageInhCandidates[k]);
                    if (obj != null) {
                        acc.put(PdfReader.pageInhCandidates[k], obj);
                    }
                }
                Iterator<PdfObject> it = ((PdfArray) PdfReader.getPdfObjectRelease(top.get(PdfName.KIDS))).listIterator();
                while (it.hasNext()) {
                    PdfObject ref = (PRIndirectReference) it.next();
                    PdfDictionary dic = (PdfDictionary) PdfReader.getPdfObject(ref);
                    int last = this.reader.lastXrefPartial;
                    PdfObject count = PdfReader.getPdfObjectRelease(dic.get(PdfName.COUNT));
                    this.reader.lastXrefPartial = last;
                    int acn = 1;
                    if (count != null && count.type() == 2) {
                        acn = ((PdfNumber) count).intValue();
                    }
                    if (n >= base + acn) {
                        this.reader.releaseLastXrefPartial();
                        base += acn;
                    } else if (count == null) {
                        dic.mergeDifferent(acc);
                        return ref;
                    } else {
                        this.reader.releaseLastXrefPartial();
                        top = dic;
                    }
                }
            }
        }

        private void selectPages(List<Integer> pagesToKeep) {
            int k;
            IntHashtable pg = new IntHashtable();
            ArrayList<Integer> finalPages = new ArrayList();
            int psize = size();
            for (Integer pi : pagesToKeep) {
                int p = pi.intValue();
                if (p >= 1 && p <= psize && pg.put(p, 1) == 0) {
                    finalPages.add(pi);
                }
            }
            if (this.reader.partial) {
                for (k = 1; k <= psize; k++) {
                    getPageOrigRef(k);
                    resetReleasePage();
                }
            }
            PdfObject parent = (PRIndirectReference) this.reader.catalog.get(PdfName.PAGES);
            PdfDictionary topPages = (PdfDictionary) PdfReader.getPdfObject(parent);
            ArrayList<PRIndirectReference> newPageRefs = new ArrayList(finalPages.size());
            PdfArray kids = new PdfArray();
            for (k = 0; k < finalPages.size(); k++) {
                p = ((Integer) finalPages.get(k)).intValue();
                PRIndirectReference pref = getPageOrigRef(p);
                resetReleasePage();
                kids.add(pref);
                newPageRefs.add(pref);
                getPageN(p).put(PdfName.PARENT, parent);
            }
            AcroFields af = this.reader.getAcroFields();
            boolean removeFields = af.getFields().size() > 0;
            for (k = 1; k <= psize; k++) {
                if (!pg.containsKey(k)) {
                    if (removeFields) {
                        af.removeFieldsFromPage(k);
                    }
                    int nref = getPageOrigRef(k).getNumber();
                    this.reader.xrefObj.set(nref, null);
                    if (this.reader.partial) {
                        this.reader.xref[nref * 2] = -1;
                        this.reader.xref[(nref * 2) + 1] = 0;
                    }
                }
            }
            topPages.put(PdfName.COUNT, new PdfNumber(finalPages.size()));
            topPages.put(PdfName.KIDS, kids);
            this.refsp = null;
            this.refsn = newPageRefs;
        }
    }

    protected Counter getCounter() {
        return COUNTER;
    }

    private PdfReader(RandomAccessSource byteSource, boolean partialRead, byte[] ownerPassword, Certificate certificate, Key certificateKey, String certificateKeyProvider, ExternalDecryptionProcess externalDecryptionProcess, boolean closeSourceOnConstructorError) throws IOException {
        this.acroForm = null;
        this.acroFormParsed = false;
        this.encrypted = false;
        this.rebuilt = false;
        this.tampered = false;
        this.password = null;
        this.certificateKey = null;
        this.certificate = null;
        this.certificateKeyProvider = null;
        this.externalDecryptionProcess = null;
        this.strings = new ArrayList();
        this.sharedStreams = true;
        this.consolidateNamedDestinations = false;
        this.remoteToLocalNamedDestinations = false;
        this.lastXrefPartial = -1;
        this.viewerPreferences = new PdfViewerPreferencesImp();
        this.readDepth = 0;
        this.certificate = certificate;
        this.certificateKey = certificateKey;
        this.certificateKeyProvider = certificateKeyProvider;
        this.externalDecryptionProcess = externalDecryptionProcess;
        this.password = ownerPassword;
        this.partial = partialRead;
        try {
            this.tokens = getOffsetTokeniser(byteSource);
            if (partialRead) {
                readPdfPartial();
            } else {
                readPdf();
            }
            getCounter().read(this.fileLength);
        } catch (IOException e) {
            if (closeSourceOnConstructorError) {
                byteSource.close();
            }
            throw e;
        }
    }

    public PdfReader(String filename) throws IOException {
        this(filename, (byte[]) null);
    }

    public PdfReader(String filename, byte[] ownerPassword) throws IOException {
        this(filename, ownerPassword, false);
    }

    public PdfReader(String filename, byte[] ownerPassword, boolean partial) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(false).setUsePlainRandomAccess(Document.plainRandomAccess).createBestSource(filename), partial, ownerPassword, null, null, null, null, true);
    }

    public PdfReader(byte[] pdfIn) throws IOException {
        this(pdfIn, null);
    }

    public PdfReader(byte[] pdfIn, byte[] ownerPassword) throws IOException {
        this(new RandomAccessSourceFactory().createSource(pdfIn), false, ownerPassword, null, null, null, null, true);
    }

    public PdfReader(String filename, Certificate certificate, Key certificateKey, String certificateKeyProvider) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(false).setUsePlainRandomAccess(Document.plainRandomAccess).createBestSource(filename), false, null, certificate, certificateKey, certificateKeyProvider, null, true);
    }

    public PdfReader(String filename, Certificate certificate, ExternalDecryptionProcess externalDecryptionProcess) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(false).setUsePlainRandomAccess(Document.plainRandomAccess).createBestSource(filename), false, null, certificate, null, null, externalDecryptionProcess, true);
    }

    public PdfReader(URL url) throws IOException {
        this(url, null);
    }

    public PdfReader(URL url, byte[] ownerPassword) throws IOException {
        this(new RandomAccessSourceFactory().createSource(url), false, ownerPassword, null, null, null, null, true);
    }

    public PdfReader(InputStream is, byte[] ownerPassword) throws IOException {
        this(new RandomAccessSourceFactory().createSource(is), false, ownerPassword, null, null, null, null, false);
    }

    public PdfReader(InputStream is) throws IOException {
        this(is, null);
    }

    public PdfReader(RandomAccessFileOrArray raf, byte[] ownerPassword) throws IOException {
        this(raf.getByteSource(), true, ownerPassword, null, null, null, null, false);
    }

    public PdfReader(PdfReader reader) {
        this.acroForm = null;
        this.acroFormParsed = false;
        this.encrypted = false;
        this.rebuilt = false;
        this.tampered = false;
        this.password = null;
        this.certificateKey = null;
        this.certificate = null;
        this.certificateKeyProvider = null;
        this.externalDecryptionProcess = null;
        this.strings = new ArrayList();
        this.sharedStreams = true;
        this.consolidateNamedDestinations = false;
        this.remoteToLocalNamedDestinations = false;
        this.lastXrefPartial = -1;
        this.viewerPreferences = new PdfViewerPreferencesImp();
        this.readDepth = 0;
        this.appendable = reader.appendable;
        this.consolidateNamedDestinations = reader.consolidateNamedDestinations;
        this.encrypted = reader.encrypted;
        this.rebuilt = reader.rebuilt;
        this.sharedStreams = reader.sharedStreams;
        this.tampered = reader.tampered;
        this.password = reader.password;
        this.pdfVersion = reader.pdfVersion;
        this.eofPos = reader.eofPos;
        this.freeXref = reader.freeXref;
        this.lastXref = reader.lastXref;
        this.newXrefType = reader.newXrefType;
        this.tokens = new PRTokeniser(reader.tokens.getSafeFile());
        if (reader.decrypt != null) {
            this.decrypt = new PdfEncryption(reader.decrypt);
        }
        this.pValue = reader.pValue;
        this.rValue = reader.rValue;
        this.xrefObj = new ArrayList(reader.xrefObj);
        for (int k = 0; k < reader.xrefObj.size(); k++) {
            this.xrefObj.set(k, duplicatePdfObject((PdfObject) reader.xrefObj.get(k), this));
        }
        this.pageRefs = new PageRefs(reader.pageRefs, this);
        this.trailer = (PdfDictionary) duplicatePdfObject(reader.trailer, this);
        this.catalog = this.trailer.getAsDict(PdfName.ROOT);
        this.rootPages = this.catalog.getAsDict(PdfName.PAGES);
        this.fileLength = reader.fileLength;
        this.partial = reader.partial;
        this.hybridXref = reader.hybridXref;
        this.objStmToOffset = reader.objStmToOffset;
        this.xref = reader.xref;
        this.cryptoRef = (PRIndirectReference) duplicatePdfObject(reader.cryptoRef, this);
        this.ownerPasswordUsed = reader.ownerPasswordUsed;
    }

    private static PRTokeniser getOffsetTokeniser(RandomAccessSource byteSource) throws IOException {
        PRTokeniser tok = new PRTokeniser(new RandomAccessFileOrArray(byteSource));
        int offset = tok.getHeaderOffset();
        if (offset != 0) {
            return new PRTokeniser(new RandomAccessFileOrArray(new WindowRandomAccessSource(byteSource, (long) offset)));
        }
        return tok;
    }

    public RandomAccessFileOrArray getSafeFile() {
        return this.tokens.getSafeFile();
    }

    protected PdfReaderInstance getPdfReaderInstance(PdfWriter writer) {
        return new PdfReaderInstance(this, writer);
    }

    public int getNumberOfPages() {
        return this.pageRefs.size();
    }

    public PdfDictionary getCatalog() {
        return this.catalog;
    }

    public PRAcroForm getAcroForm() {
        if (!this.acroFormParsed) {
            this.acroFormParsed = true;
            PdfObject form = this.catalog.get(PdfName.ACROFORM);
            if (form != null) {
                try {
                    this.acroForm = new PRAcroForm(this);
                    this.acroForm.readAcroForm((PdfDictionary) getPdfObject(form));
                } catch (Exception e) {
                    this.acroForm = null;
                }
            }
        }
        return this.acroForm;
    }

    public int getPageRotation(int index) {
        return getPageRotation(this.pageRefs.getPageNRelease(index));
    }

    int getPageRotation(PdfDictionary page) {
        PdfNumber rotate = page.getAsNumber(PdfName.ROTATE);
        if (rotate == null) {
            return 0;
        }
        int n = rotate.intValue() % dc1394.DC1394_COLOR_CODING_RGB16S;
        return n < 0 ? n + dc1394.DC1394_COLOR_CODING_RGB16S : n;
    }

    public Rectangle getPageSizeWithRotation(int index) {
        return getPageSizeWithRotation(this.pageRefs.getPageNRelease(index));
    }

    public Rectangle getPageSizeWithRotation(PdfDictionary page) {
        Rectangle rect = getPageSize(page);
        for (int rotation = getPageRotation(page); rotation > 0; rotation -= 90) {
            rect = rect.rotate();
        }
        return rect;
    }

    public Rectangle getPageSize(int index) {
        return getPageSize(this.pageRefs.getPageNRelease(index));
    }

    public Rectangle getPageSize(PdfDictionary page) {
        return getNormalizedRectangle(page.getAsArray(PdfName.MEDIABOX));
    }

    public Rectangle getCropBox(int index) {
        PdfDictionary page = this.pageRefs.getPageNRelease(index);
        PdfArray cropBox = (PdfArray) getPdfObjectRelease(page.get(PdfName.CROPBOX));
        if (cropBox == null) {
            return getPageSize(page);
        }
        return getNormalizedRectangle(cropBox);
    }

    public Rectangle getBoxSize(int index, String boxName) {
        PdfDictionary page = this.pageRefs.getPageNRelease(index);
        PdfArray box = null;
        if (boxName.equals("trim")) {
            box = (PdfArray) getPdfObjectRelease(page.get(PdfName.TRIMBOX));
        } else if (boxName.equals("art")) {
            box = (PdfArray) getPdfObjectRelease(page.get(PdfName.ARTBOX));
        } else if (boxName.equals("bleed")) {
            box = (PdfArray) getPdfObjectRelease(page.get(PdfName.BLEEDBOX));
        } else if (boxName.equals("crop")) {
            box = (PdfArray) getPdfObjectRelease(page.get(PdfName.CROPBOX));
        } else if (boxName.equals("media")) {
            box = (PdfArray) getPdfObjectRelease(page.get(PdfName.MEDIABOX));
        }
        if (box == null) {
            return null;
        }
        return getNormalizedRectangle(box);
    }

    public HashMap<String, String> getInfo() {
        HashMap<String, String> map = new HashMap();
        PdfDictionary info = this.trailer.getAsDict(PdfName.INFO);
        if (info != null) {
            for (PdfName key : info.getKeys()) {
                PdfObject obj = getPdfObject(info.get(key));
                if (obj != null) {
                    String value = obj.toString();
                    switch (obj.type()) {
                        case 3:
                            value = ((PdfString) obj).toUnicodeString();
                            break;
                        case 4:
                            value = PdfName.decodeName(value);
                            break;
                    }
                    map.put(PdfName.decodeName(key.toString()), value);
                }
            }
        }
        return map;
    }

    public static Rectangle getNormalizedRectangle(PdfArray box) {
        float llx = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(0))).floatValue();
        float lly = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(1))).floatValue();
        float urx = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(2))).floatValue();
        float ury = ((PdfNumber) getPdfObjectRelease(box.getPdfObject(3))).floatValue();
        return new Rectangle(Math.min(llx, urx), Math.min(lly, ury), Math.max(llx, urx), Math.max(lly, ury));
    }

    public boolean isTagged() {
        PdfDictionary markInfo = this.catalog.getAsDict(PdfName.MARKINFO);
        if (markInfo == null || !PdfBoolean.PDFTRUE.equals(markInfo.getAsBoolean(PdfName.MARKED)) || this.catalog.getAsDict(PdfName.STRUCTTREEROOT) == null) {
            return false;
        }
        return true;
    }

    protected void readPdf() throws IOException {
        this.fileLength = this.tokens.getFile().length();
        this.pdfVersion = this.tokens.checkPdfHeader();
        try {
            readXref();
        } catch (Exception e) {
            try {
                this.rebuilt = true;
                rebuildXref();
                this.lastXref = -1;
            } catch (Exception ne) {
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", ne.getMessage(), e.getMessage()));
            }
        }
        try {
            readDocObj();
        } catch (Exception e2) {
            if (e2 instanceof BadPasswordException) {
                throw new BadPasswordException(e2.getMessage());
            } else if (this.rebuilt || this.encryptionError) {
                throw new InvalidPdfException(e2.getMessage());
            } else {
                this.rebuilt = true;
                this.encrypted = false;
                try {
                    rebuildXref();
                    this.lastXref = -1;
                    readDocObj();
                } catch (Exception ne2) {
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", ne2.getMessage(), e2.getMessage()));
                }
            }
        }
        this.strings.clear();
        readPages();
        removeUnusedObjects();
    }

    protected void readPdfPartial() throws IOException {
        this.fileLength = this.tokens.getFile().length();
        this.pdfVersion = this.tokens.checkPdfHeader();
        try {
            readXref();
        } catch (Exception e) {
            try {
                this.rebuilt = true;
                rebuildXref();
                this.lastXref = -1;
            } catch (Exception ne) {
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", ne.getMessage(), e.getMessage()), ne);
            }
        }
        readDocObjPartial();
        readPages();
    }

    private boolean equalsArray(byte[] ar1, byte[] ar2, int size) {
        for (int k = 0; k < size; k++) {
            if (ar1[k] != ar2[k]) {
                return false;
            }
        }
        return true;
    }

    private void readDecryptedDocObj() throws IOException {
        CMSEnvelopedData cMSEnvelopedData;
        Exception f;
        CMSEnvelopedData cMSEnvelopedData2;
        if (!this.encrypted) {
            PdfObject encDic = this.trailer.get(PdfName.ENCRYPT);
            if (encDic != null && !encDic.toString().equals("null")) {
                PdfObject o;
                this.encryptionError = true;
                byte[] encryptionKey = null;
                this.encrypted = true;
                PdfDictionary enc = (PdfDictionary) getPdfObject(encDic);
                PdfArray documentIDs = this.trailer.getAsArray(PdfName.ID);
                byte[] documentID = null;
                if (documentIDs != null) {
                    o = documentIDs.getPdfObject(0);
                    this.strings.remove(o);
                    documentID = DocWriter.getISOBytes(o.toString());
                    if (documentIDs.size() > 1) {
                        this.strings.remove(documentIDs.getPdfObject(1));
                    }
                }
                if (documentID == null) {
                    documentID = new byte[0];
                }
                byte[] uValue = null;
                byte[] oValue = null;
                int cryptoMode = 0;
                int lengthValue = 0;
                PdfObject filter = getPdfObjectRelease(enc.get(PdfName.FILTER));
                PdfDictionary dic;
                PdfObject em;
                if (filter.equals(PdfName.STANDARD)) {
                    String s = enc.get(PdfName.f135U).toString();
                    this.strings.remove(enc.get(PdfName.f135U));
                    uValue = DocWriter.getISOBytes(s);
                    s = enc.get(PdfName.f129O).toString();
                    this.strings.remove(enc.get(PdfName.f129O));
                    oValue = DocWriter.getISOBytes(s);
                    if (enc.contains(PdfName.OE)) {
                        this.strings.remove(enc.get(PdfName.OE));
                    }
                    if (enc.contains(PdfName.UE)) {
                        this.strings.remove(enc.get(PdfName.UE));
                    }
                    if (enc.contains(PdfName.PERMS)) {
                        this.strings.remove(enc.get(PdfName.PERMS));
                    }
                    o = enc.get(PdfName.f130P);
                    if (o.isNumber()) {
                        this.pValue = ((PdfNumber) o).longValue();
                        o = enc.get(PdfName.f132R);
                        if (o.isNumber()) {
                            this.rValue = ((PdfNumber) o).intValue();
                            switch (this.rValue) {
                                case 2:
                                    cryptoMode = 0;
                                    break;
                                case 3:
                                    o = enc.get(PdfName.LENGTH);
                                    if (o.isNumber()) {
                                        lengthValue = ((PdfNumber) o).intValue();
                                        if (lengthValue <= 128 && lengthValue >= 40 && lengthValue % 8 == 0) {
                                            cryptoMode = 1;
                                            break;
                                        }
                                        throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0]));
                                    }
                                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0]));
                                case 4:
                                    dic = (PdfDictionary) enc.get(PdfName.CF);
                                    if (dic != null) {
                                        dic = (PdfDictionary) dic.get(PdfName.STDCF);
                                        if (dic != null) {
                                            if (PdfName.V2.equals(dic.get(PdfName.CFM))) {
                                                cryptoMode = 1;
                                            } else {
                                                if (PdfName.AESV2.equals(dic.get(PdfName.CFM))) {
                                                    cryptoMode = 2;
                                                } else {
                                                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found", new Object[0]));
                                                }
                                            }
                                            em = enc.get(PdfName.ENCRYPTMETADATA);
                                            if (em != null && em.toString().equals(PdfBoolean.FALSE)) {
                                                cryptoMode |= 8;
                                                break;
                                            }
                                        }
                                        throw new InvalidPdfException(MessageLocalization.getComposedMessage("stdcf.not.found.encryption", new Object[0]));
                                    }
                                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption", new Object[0]));
                                case 5:
                                    cryptoMode = 3;
                                    PdfObject em5 = enc.get(PdfName.ENCRYPTMETADATA);
                                    if (em5 != null && em5.toString().equals(PdfBoolean.FALSE)) {
                                        cryptoMode = 3 | 8;
                                        break;
                                    }
                                default:
                                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.r.eq.1", this.rValue));
                            }
                        }
                        throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.r.value", new Object[0]));
                    }
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.p.value", new Object[0]));
                }
                if (filter.equals(PdfName.PUBSEC)) {
                    boolean foundRecipient = false;
                    byte[] envelopedData = null;
                    o = enc.get(PdfName.f136V);
                    if (o.isNumber()) {
                        PdfArray recipients;
                        int vValue = ((PdfNumber) o).intValue();
                        switch (vValue) {
                            case 1:
                                cryptoMode = 0;
                                lengthValue = 40;
                                recipients = (PdfArray) enc.get(PdfName.RECIPIENTS);
                                break;
                            case 2:
                                o = enc.get(PdfName.LENGTH);
                                if (o.isNumber()) {
                                    lengthValue = ((PdfNumber) o).intValue();
                                    if (lengthValue <= 128 && lengthValue >= 40 && lengthValue % 8 == 0) {
                                        cryptoMode = 1;
                                        recipients = (PdfArray) enc.get(PdfName.RECIPIENTS);
                                        break;
                                    }
                                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0]));
                                }
                                throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0]));
                                break;
                            case 4:
                            case 5:
                                dic = (PdfDictionary) enc.get(PdfName.CF);
                                if (dic != null) {
                                    dic = (PdfDictionary) dic.get(PdfName.DEFAULTCRYPTFILTER);
                                    if (dic != null) {
                                        if (PdfName.V2.equals(dic.get(PdfName.CFM))) {
                                            cryptoMode = 1;
                                            lengthValue = 128;
                                        } else {
                                            if (PdfName.AESV2.equals(dic.get(PdfName.CFM))) {
                                                cryptoMode = 2;
                                                lengthValue = 128;
                                            } else {
                                                if (PdfName.AESV3.equals(dic.get(PdfName.CFM))) {
                                                    cryptoMode = 3;
                                                    lengthValue = 256;
                                                } else {
                                                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found", new Object[0]));
                                                }
                                            }
                                        }
                                        em = dic.get(PdfName.ENCRYPTMETADATA);
                                        if (em != null && em.toString().equals(PdfBoolean.FALSE)) {
                                            cryptoMode |= 8;
                                        }
                                        recipients = (PdfArray) dic.get(PdfName.RECIPIENTS);
                                        break;
                                    }
                                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("defaultcryptfilter.not.found.encryption", new Object[0]));
                                }
                                throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption", new Object[0]));
                            default:
                                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.v.eq.1", vValue));
                        }
                        try {
                            int i;
                            X509CertificateHolder certHolder = new X509CertificateHolder(this.certificate.getEncoded());
                            PdfObject recipient;
                            RecipientInformation recipientInfo;
                            if (this.externalDecryptionProcess == null) {
                                i = 0;
                                while (i < recipients.size()) {
                                    recipient = recipients.getPdfObject(i);
                                    this.strings.remove(recipient);
                                    try {
                                        cMSEnvelopedData = new CMSEnvelopedData(recipient.getBytes());
                                        try {
                                            for (RecipientInformation recipientInfo2 : cMSEnvelopedData.getRecipientInfos().getRecipients()) {
                                                if (recipientInfo2.getRID().match(certHolder) && !foundRecipient) {
                                                    envelopedData = PdfEncryptor.getContent(recipientInfo2, (PrivateKey) this.certificateKey, this.certificateKeyProvider);
                                                    foundRecipient = true;
                                                }
                                            }
                                            i++;
                                        } catch (Exception e) {
                                            f = e;
                                            cMSEnvelopedData2 = cMSEnvelopedData;
                                        }
                                    } catch (Exception e2) {
                                        f = e2;
                                    }
                                }
                            } else {
                                i = 0;
                                while (i < recipients.size()) {
                                    recipient = recipients.getPdfObject(i);
                                    this.strings.remove(recipient);
                                    try {
                                        cMSEnvelopedData = new CMSEnvelopedData(recipient.getBytes());
                                        try {
                                            recipientInfo2 = cMSEnvelopedData.getRecipientInfos().get(this.externalDecryptionProcess.getCmsRecipientId());
                                            if (recipientInfo2 != null) {
                                                envelopedData = recipientInfo2.getContent(this.externalDecryptionProcess.getCmsRecipient());
                                                foundRecipient = true;
                                            }
                                            i++;
                                        } catch (Exception e3) {
                                            f = e3;
                                            cMSEnvelopedData2 = cMSEnvelopedData;
                                        }
                                    } catch (Exception e4) {
                                        f = e4;
                                    }
                                }
                            }
                            if (!foundRecipient || envelopedData == null) {
                                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("bad.certificate.and.key", new Object[0]));
                            }
                            MessageDigest md;
                            if ((cryptoMode & 7) == 3) {
                                try {
                                    md = MessageDigest.getInstance("SHA-256");
                                } catch (Exception f2) {
                                    throw new ExceptionConverter(f2);
                                }
                            }
                            md = MessageDigest.getInstance(DigestAlgorithms.SHA1);
                            md.update(envelopedData, 0, 20);
                            for (i = 0; i < recipients.size(); i++) {
                                md.update(recipients.getPdfObject(i).getBytes());
                            }
                            if ((cryptoMode & 8) != 0) {
                                md.update(new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1});
                            }
                            encryptionKey = md.digest();
                        } catch (Exception f22) {
                            throw new ExceptionConverter(f22);
                        }
                    }
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.v.value", new Object[0]));
                }
                this.decrypt = new PdfEncryption();
                this.decrypt.setCryptoMode(cryptoMode, lengthValue);
                if (!filter.equals(PdfName.STANDARD)) {
                    if (filter.equals(PdfName.PUBSEC)) {
                        if ((cryptoMode & 7) == 3) {
                            this.decrypt.setKey(encryptionKey);
                        } else {
                            this.decrypt.setupByEncryptionKey(encryptionKey, lengthValue);
                        }
                        this.ownerPasswordUsed = true;
                    }
                } else if (this.rValue == 5) {
                    this.ownerPasswordUsed = this.decrypt.readKey(enc, this.password);
                    this.pValue = this.decrypt.getPermissions();
                } else {
                    this.decrypt.setupByOwnerPassword(documentID, this.password, uValue, oValue, this.pValue);
                    byte[] bArr = this.decrypt.userKey;
                    int i2 = (this.rValue == 3 || this.rValue == 4) ? 16 : 32;
                    if (equalsArray(uValue, bArr, i2)) {
                        this.ownerPasswordUsed = true;
                    } else {
                        this.decrypt.setupByUserPassword(documentID, this.password, oValue, this.pValue);
                        bArr = this.decrypt.userKey;
                        i2 = (this.rValue == 3 || this.rValue == 4) ? 16 : 32;
                        if (!equalsArray(uValue, bArr, i2)) {
                            throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password", new Object[0]));
                        }
                    }
                }
                for (int k = 0; k < this.strings.size(); k++) {
                    ((PdfString) this.strings.get(k)).decrypt(this);
                }
                if (encDic.isIndirect()) {
                    this.cryptoRef = (PRIndirectReference) encDic;
                    this.xrefObj.set(this.cryptoRef.getNumber(), null);
                }
                this.encryptionError = false;
                return;
            }
            return;
        }
        return;
        throw new ExceptionConverter(f22);
        throw new ExceptionConverter(f22);
    }

    public static PdfObject getPdfObjectRelease(PdfObject obj) {
        PdfObject obj2 = getPdfObject(obj);
        releaseLastXrefPartial(obj);
        return obj2;
    }

    public static PdfObject getPdfObject(PdfObject obj) {
        if (obj == null) {
            return null;
        }
        if (!obj.isIndirect()) {
            return obj;
        }
        try {
            PRIndirectReference ref = (PRIndirectReference) obj;
            int idx = ref.getNumber();
            boolean appendable = ref.getReader().appendable;
            obj = ref.getReader().getPdfObject(idx);
            if (obj == null) {
                return null;
            }
            if (!appendable) {
                return obj;
            }
            switch (obj.type()) {
                case 1:
                    obj = new PdfBoolean(((PdfBoolean) obj).booleanValue());
                    break;
                case 4:
                    obj = new PdfName(obj.getBytes());
                    break;
                case 8:
                    obj = new PdfNull();
                    break;
            }
            obj.setIndRef(ref);
            return obj;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static PdfObject getPdfObjectRelease(PdfObject obj, PdfObject parent) {
        PdfObject obj2 = getPdfObject(obj, parent);
        releaseLastXrefPartial(obj);
        return obj2;
    }

    public static PdfObject getPdfObject(PdfObject obj, PdfObject parent) {
        if (obj == null) {
            return null;
        }
        if (obj.isIndirect()) {
            return getPdfObject(obj);
        }
        if (parent == null) {
            return obj;
        }
        PRIndirectReference ref = parent.getIndRef();
        if (ref == null || !ref.getReader().isAppendable()) {
            return obj;
        }
        switch (obj.type()) {
            case 1:
                obj = new PdfBoolean(((PdfBoolean) obj).booleanValue());
                break;
            case 4:
                obj = new PdfName(obj.getBytes());
                break;
            case 8:
                obj = new PdfNull();
                break;
        }
        obj.setIndRef(ref);
        return obj;
    }

    public PdfObject getPdfObjectRelease(int idx) {
        PdfObject obj = getPdfObject(idx);
        releaseLastXrefPartial();
        return obj;
    }

    public PdfObject getPdfObject(int idx) {
        try {
            this.lastXrefPartial = -1;
            if (idx < 0 || idx >= this.xrefObj.size()) {
                return null;
            }
            PdfObject obj = (PdfObject) this.xrefObj.get(idx);
            if (!this.partial || obj != null) {
                return obj;
            }
            if (idx * 2 >= this.xref.length) {
                return null;
            }
            obj = readSingleObject(idx);
            this.lastXrefPartial = -1;
            if (obj == null) {
                return obj;
            }
            this.lastXrefPartial = idx;
            return obj;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void resetLastXrefPartial() {
        this.lastXrefPartial = -1;
    }

    public void releaseLastXrefPartial() {
        if (this.partial && this.lastXrefPartial != -1) {
            this.xrefObj.set(this.lastXrefPartial, null);
            this.lastXrefPartial = -1;
        }
    }

    public static void releaseLastXrefPartial(PdfObject obj) {
        if (obj != null && obj.isIndirect() && (obj instanceof PRIndirectReference)) {
            PRIndirectReference ref = (PRIndirectReference) obj;
            PdfReader reader = ref.getReader();
            if (reader.partial && reader.lastXrefPartial != -1 && reader.lastXrefPartial == ref.getNumber()) {
                reader.xrefObj.set(reader.lastXrefPartial, null);
            }
            reader.lastXrefPartial = -1;
        }
    }

    private void setXrefPartialObject(int idx, PdfObject obj) {
        if (this.partial && idx >= 0) {
            this.xrefObj.set(idx, obj);
        }
    }

    public PRIndirectReference addPdfObject(PdfObject obj) {
        this.xrefObj.add(obj);
        return new PRIndirectReference(this, this.xrefObj.size() - 1);
    }

    protected void readPages() throws IOException {
        this.catalog = this.trailer.getAsDict(PdfName.ROOT);
        if (this.catalog == null) {
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("the.document.has.no.catalog.object", new Object[0]));
        }
        this.rootPages = this.catalog.getAsDict(PdfName.PAGES);
        if (this.rootPages == null) {
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("the.document.has.no.page.root", new Object[0]));
        }
        this.pageRefs = new PageRefs();
    }

    protected void readDocObjPartial() throws IOException {
        this.xrefObj = new ArrayList(this.xref.length / 2);
        this.xrefObj.addAll(Collections.nCopies(this.xref.length / 2, null));
        readDecryptedDocObj();
        if (this.objStmToOffset != null) {
            long[] keys = this.objStmToOffset.getKeys();
            for (long n : keys) {
                this.objStmToOffset.put(n, this.xref[(int) (n * 2)]);
                this.xref[(int) (n * 2)] = -1;
            }
        }
    }

    protected PdfObject readSingleObject(int k) throws IOException {
        PdfObject pdfObject = null;
        this.strings.clear();
        int k2 = k * 2;
        long pos = this.xref[k2];
        if (pos >= 0) {
            if (this.xref[k2 + 1] > 0) {
                pos = this.objStmToOffset.get(this.xref[k2 + 1]);
            }
            if (pos != 0) {
                this.tokens.seek(pos);
                this.tokens.nextValidToken();
                if (this.tokens.getTokenType() != TokenType.NUMBER) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.object.number", new Object[0]));
                }
                this.objNum = this.tokens.intValue();
                this.tokens.nextValidToken();
                if (this.tokens.getTokenType() != TokenType.NUMBER) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.generation.number", new Object[0]));
                }
                this.objGen = this.tokens.intValue();
                this.tokens.nextValidToken();
                if (!this.tokens.getStringValue().equals("obj")) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("token.obj.expected", new Object[0]));
                }
                try {
                    pdfObject = readPRObject();
                    for (int j = 0; j < this.strings.size(); j++) {
                        ((PdfString) this.strings.get(j)).decrypt(this);
                    }
                    if (pdfObject.isStream()) {
                        checkPRStreamLength((PRStream) pdfObject);
                    }
                } catch (IOException e) {
                    if (debugmode) {
                        if (LOGGER.isLogging(Level.ERROR)) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        pdfObject = null;
                    } else {
                        throw e;
                    }
                }
                if (this.xref[k2 + 1] > 0) {
                    pdfObject = readOneObjStm((PRStream) pdfObject, (int) this.xref[k2]);
                }
                this.xrefObj.set(k, pdfObject);
            }
        }
        return pdfObject;
    }

    protected PdfObject readOneObjStm(PRStream stream, int idx) throws IOException {
        int first = stream.getAsNumber(PdfName.FIRST).intValue();
        byte[] b = getStreamBytes(stream, this.tokens.getFile());
        PRTokeniser saveTokens = this.tokens;
        this.tokens = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(b)));
        int address = 0;
        boolean ok = true;
        idx++;
        int k = 0;
        while (k < idx) {
            try {
                ok = this.tokens.nextToken();
                if (!ok) {
                    break;
                } else if (this.tokens.getTokenType() != TokenType.NUMBER) {
                    ok = false;
                    break;
                } else {
                    ok = this.tokens.nextToken();
                    if (!ok) {
                        break;
                    } else if (this.tokens.getTokenType() != TokenType.NUMBER) {
                        ok = false;
                        break;
                    } else {
                        address = this.tokens.intValue() + first;
                        k++;
                    }
                }
            } catch (Throwable th) {
                this.tokens = saveTokens;
            }
        }
        if (ok) {
            PdfObject obj;
            this.tokens.seek((long) address);
            this.tokens.nextToken();
            if (this.tokens.getTokenType() == TokenType.NUMBER) {
                obj = new PdfNumber(this.tokens.getStringValue());
            } else {
                this.tokens.seek((long) address);
                obj = readPRObject();
            }
            this.tokens = saveTokens;
            return obj;
        }
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm", new Object[0]));
    }

    public double dumpPerc() {
        int total = 0;
        for (int k = 0; k < this.xrefObj.size(); k++) {
            if (this.xrefObj.get(k) != null) {
                total++;
            }
        }
        return (((double) total) * 100.0d) / ((double) this.xrefObj.size());
    }

    protected void readDocObj() throws IOException {
        ArrayList<PRStream> streams = new ArrayList();
        this.xrefObj = new ArrayList(this.xref.length / 2);
        this.xrefObj.addAll(Collections.nCopies(this.xref.length / 2, null));
        int k = 2;
        while (k < this.xref.length) {
            long pos = this.xref[k];
            if (pos > 0 && this.xref[k + 1] <= 0) {
                PdfObject obj;
                this.tokens.seek(pos);
                this.tokens.nextValidToken();
                if (this.tokens.getTokenType() != TokenType.NUMBER) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.object.number", new Object[0]));
                }
                this.objNum = this.tokens.intValue();
                this.tokens.nextValidToken();
                if (this.tokens.getTokenType() != TokenType.NUMBER) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.generation.number", new Object[0]));
                }
                this.objGen = this.tokens.intValue();
                this.tokens.nextValidToken();
                if (!this.tokens.getStringValue().equals("obj")) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("token.obj.expected", new Object[0]));
                }
                try {
                    obj = readPRObject();
                    if (obj.isStream()) {
                        streams.add((PRStream) obj);
                    }
                } catch (IOException e) {
                    if (debugmode) {
                        if (LOGGER.isLogging(Level.ERROR)) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        obj = null;
                    } else {
                        throw e;
                    }
                }
                this.xrefObj.set(k / 2, obj);
            }
            k += 2;
        }
        for (k = 0; k < streams.size(); k++) {
            checkPRStreamLength((PRStream) streams.get(k));
        }
        readDecryptedDocObj();
        if (this.objStmMark != null) {
            for (Entry<Integer, IntHashtable> entry : this.objStmMark.entrySet()) {
                int n = ((Integer) entry.getKey()).intValue();
                readObjStm((PRStream) this.xrefObj.get(n), (IntHashtable) entry.getValue());
                this.xrefObj.set(n, null);
            }
            this.objStmMark = null;
        }
        this.xref = null;
    }

    private void checkPRStreamLength(PRStream stream) throws IOException {
        long fileLength = this.tokens.length();
        long start = stream.getOffset();
        boolean calc = false;
        long streamLength = 0;
        PdfObject obj = getPdfObjectRelease(stream.get(PdfName.LENGTH));
        if (obj == null || obj.type() != 2) {
            calc = true;
        } else {
            streamLength = (long) ((PdfNumber) obj).intValue();
            if (streamLength + start > fileLength - 20) {
                calc = true;
            } else {
                this.tokens.seek(start + streamLength);
                String line = this.tokens.readString(20);
                if (!(line.startsWith("\nendstream") || line.startsWith("\r\nendstream") || line.startsWith("\rendstream") || line.startsWith("endstream"))) {
                    calc = true;
                }
            }
        }
        if (calc) {
            long pos;
            byte[] tline = new byte[16];
            this.tokens.seek(start);
            do {
                pos = this.tokens.getFilePointer();
                if (!this.tokens.readLineSegment(tline, false)) {
                    break;
                } else if (equalsn(tline, endstream)) {
                    streamLength = pos - start;
                    break;
                }
            } while (!equalsn(tline, endobj));
            this.tokens.seek(pos - 16);
            int index = this.tokens.readString(16).indexOf("endstream");
            if (index >= 0) {
                pos = (pos - 16) + ((long) index);
            }
            streamLength = pos - start;
            this.tokens.seek(pos - 2);
            if (this.tokens.read() == 13) {
                streamLength--;
            }
            this.tokens.seek(pos - 1);
            if (this.tokens.read() == 10) {
                streamLength--;
            }
            if (streamLength < 0) {
                streamLength = 0;
            }
        }
        stream.setLength((int) streamLength);
    }

    protected void readObjStm(PRStream stream, IntHashtable map) throws IOException {
        if (stream != null) {
            int first = stream.getAsNumber(PdfName.FIRST).intValue();
            int n = stream.getAsNumber(PdfName.f128N).intValue();
            byte[] b = getStreamBytes(stream, this.tokens.getFile());
            PRTokeniser saveTokens = this.tokens;
            this.tokens = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(b)));
            try {
                int[] address = new int[n];
                int[] objNumber = new int[n];
                boolean ok = true;
                int k = 0;
                while (k < n) {
                    ok = this.tokens.nextToken();
                    if (!ok) {
                        break;
                    } else if (this.tokens.getTokenType() != TokenType.NUMBER) {
                        ok = false;
                        break;
                    } else {
                        objNumber[k] = this.tokens.intValue();
                        ok = this.tokens.nextToken();
                        if (!ok) {
                            break;
                        } else if (this.tokens.getTokenType() != TokenType.NUMBER) {
                            ok = false;
                            break;
                        } else {
                            address[k] = this.tokens.intValue() + first;
                            k++;
                        }
                    }
                }
                if (ok) {
                    for (k = 0; k < n; k++) {
                        if (map.containsKey(k)) {
                            PdfObject obj;
                            this.tokens.seek((long) address[k]);
                            this.tokens.nextToken();
                            if (this.tokens.getTokenType() == TokenType.NUMBER) {
                                obj = new PdfNumber(this.tokens.getStringValue());
                            } else {
                                this.tokens.seek((long) address[k]);
                                obj = readPRObject();
                            }
                            this.xrefObj.set(objNumber[k], obj);
                        }
                    }
                    return;
                }
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm", new Object[0]));
            } finally {
                this.tokens = saveTokens;
            }
        }
    }

    public static PdfObject killIndirect(PdfObject obj) {
        if (obj == null || obj.isNull()) {
            return null;
        }
        PdfObject ret = getPdfObjectRelease(obj);
        if (!obj.isIndirect()) {
            return ret;
        }
        PRIndirectReference ref = (PRIndirectReference) obj;
        PdfReader reader = ref.getReader();
        int n = ref.getNumber();
        reader.xrefObj.set(n, null);
        if (!reader.partial) {
            return ret;
        }
        reader.xref[n * 2] = -1;
        return ret;
    }

    private void ensureXrefSize(int size) {
        if (size != 0) {
            if (this.xref == null) {
                this.xref = new long[size];
            } else if (this.xref.length < size) {
                long[] xref2 = new long[size];
                System.arraycopy(this.xref, 0, xref2, 0, this.xref.length);
                this.xref = xref2;
            }
        }
    }

    protected void readXref() throws IOException {
        this.hybridXref = false;
        this.newXrefType = false;
        this.tokens.seek(this.tokens.getStartxref());
        this.tokens.nextToken();
        if (this.tokens.getStringValue().equals("startxref")) {
            this.tokens.nextToken();
            if (this.tokens.getTokenType() != TokenType.NUMBER) {
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.is.not.followed.by.a.number", new Object[0]));
            }
            long startxref = this.tokens.longValue();
            this.lastXref = startxref;
            this.eofPos = this.tokens.getFilePointer();
            try {
                if (readXRefStream(startxref)) {
                    this.newXrefType = true;
                    return;
                }
            } catch (Exception e) {
            }
            this.xref = null;
            this.tokens.seek(startxref);
            this.trailer = readXrefSection();
            PdfDictionary trailer2 = this.trailer;
            while (true) {
                PdfNumber prev = (PdfNumber) trailer2.get(PdfName.PREV);
                if (prev != null) {
                    this.tokens.seek(prev.longValue());
                    trailer2 = readXrefSection();
                } else {
                    return;
                }
            }
        }
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.not.found", new Object[0]));
    }

    protected PdfDictionary readXrefSection() throws IOException {
        this.tokens.nextValidToken();
        if (!this.tokens.getStringValue().equals("xref")) {
            this.tokens.throwError(MessageLocalization.getComposedMessage("xref.subsection.not.found", new Object[0]));
        }
        while (true) {
            this.tokens.nextValidToken();
            if (this.tokens.getStringValue().equals("trailer")) {
                break;
            }
            long pos;
            if (this.tokens.getTokenType() != TokenType.NUMBER) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("object.number.of.the.first.object.in.this.xref.subsection.not.found", new Object[0]));
            }
            int start = this.tokens.intValue();
            this.tokens.nextValidToken();
            if (this.tokens.getTokenType() != TokenType.NUMBER) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("number.of.entries.in.this.xref.subsection.not.found", new Object[0]));
            }
            int end = this.tokens.intValue() + start;
            if (start == 1) {
                long back = this.tokens.getFilePointer();
                this.tokens.nextValidToken();
                pos = this.tokens.longValue();
                this.tokens.nextValidToken();
                int gen = this.tokens.intValue();
                if (pos == 0 && gen == 65535) {
                    start--;
                    end--;
                }
                this.tokens.seek(back);
            }
            ensureXrefSize(end * 2);
            for (int k = start; k < end; k++) {
                this.tokens.nextValidToken();
                pos = this.tokens.longValue();
                this.tokens.nextValidToken();
                gen = this.tokens.intValue();
                this.tokens.nextValidToken();
                int p = k * 2;
                if (this.tokens.getStringValue().equals("n")) {
                    if (this.xref[p] == 0 && this.xref[p + 1] == 0) {
                        this.xref[p] = pos;
                    }
                } else if (!this.tokens.getStringValue().equals("f")) {
                    this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.cross.reference.entry.in.this.xref.subsection", new Object[0]));
                } else if (this.xref[p] == 0 && this.xref[p + 1] == 0) {
                    this.xref[p] = -1;
                }
            }
        }
        PdfDictionary trailer = (PdfDictionary) readPRObject();
        ensureXrefSize(((PdfNumber) trailer.get(PdfName.SIZE)).intValue() * 2);
        PdfObject xrs = trailer.get(PdfName.XREFSTM);
        if (xrs != null && xrs.isNumber()) {
            try {
                readXRefStream((long) ((PdfNumber) xrs).intValue());
                this.newXrefType = true;
                this.hybridXref = true;
            } catch (IOException e) {
                this.xref = null;
                throw e;
            }
        }
        return trailer;
    }

    protected boolean readXRefStream(long ptr) throws IOException {
        this.tokens.seek(ptr);
        if (!this.tokens.nextToken()) {
            return false;
        }
        if (this.tokens.getTokenType() != TokenType.NUMBER) {
            return false;
        }
        int thisStream = this.tokens.intValue();
        if (!this.tokens.nextToken() || this.tokens.getTokenType() != TokenType.NUMBER) {
            return false;
        }
        if (!this.tokens.nextToken() || !this.tokens.getStringValue().equals("obj")) {
            return false;
        }
        PdfObject object = readPRObject();
        if (!object.isStream()) {
            return false;
        }
        PdfDictionary stm = (PRStream) object;
        if (!PdfName.XREF.equals(stm.get(PdfName.TYPE))) {
            return false;
        }
        PdfArray index;
        int k;
        if (this.trailer == null) {
            this.trailer = new PdfDictionary();
            this.trailer.putAll(stm);
        }
        stm.setLength(((PdfNumber) stm.get(PdfName.LENGTH)).intValue());
        int size = ((PdfNumber) stm.get(PdfName.SIZE)).intValue();
        PdfObject obj = stm.get(PdfName.INDEX);
        if (obj == null) {
            index = new PdfArray();
            index.add(new int[]{0, size});
        } else {
            index = (PdfArray) obj;
        }
        PdfArray w = (PdfArray) stm.get(PdfName.f137W);
        long prev = -1;
        obj = stm.get(PdfName.PREV);
        if (obj != null) {
            prev = ((PdfNumber) obj).longValue();
        }
        ensureXrefSize(size * 2);
        if (this.objStmMark == null && !this.partial) {
            this.objStmMark = new HashMap();
        }
        if (this.objStmToOffset == null && this.partial) {
            this.objStmToOffset = new LongHashtable();
        }
        byte[] b = getStreamBytes(stm, this.tokens.getFile());
        int i = 0;
        int[] wc = new int[3];
        for (k = 0; k < 3; k++) {
            wc[k] = w.getAsNumber(k).intValue();
        }
        int idx = 0;
        while (idx < index.size()) {
            int start = index.getAsNumber(idx).intValue();
            int length = index.getAsNumber(idx + 1).intValue();
            ensureXrefSize((start + length) * 2);
            int length2 = length;
            while (true) {
                length = length2 - 1;
                if (length2 > 0) {
                    int type = 1;
                    if (wc[0] > 0) {
                        type = 0;
                        k = 0;
                        while (k < wc[0]) {
                            type = (type << 8) + (b[i] & 255);
                            k++;
                            i++;
                        }
                    }
                    long field2 = 0;
                    k = 0;
                    while (k < wc[1]) {
                        field2 = (field2 << 8) + ((long) (b[i] & 255));
                        k++;
                        i++;
                    }
                    int field3 = 0;
                    k = 0;
                    while (k < wc[2]) {
                        field3 = (field3 << 8) + (b[i] & 255);
                        k++;
                        i++;
                    }
                    int base = start * 2;
                    if (this.xref[base] == 0 && this.xref[base + 1] == 0) {
                        switch (type) {
                            case 0:
                                this.xref[base] = -1;
                                break;
                            case 1:
                                this.xref[base] = field2;
                                break;
                            case 2:
                                this.xref[base] = (long) field3;
                                this.xref[base + 1] = field2;
                                if (!this.partial) {
                                    Integer on = Integer.valueOf((int) field2);
                                    IntHashtable seq = (IntHashtable) this.objStmMark.get(on);
                                    if (seq != null) {
                                        seq.put(field3, 1);
                                        break;
                                    }
                                    seq = new IntHashtable();
                                    seq.put(field3, 1);
                                    this.objStmMark.put(on, seq);
                                    break;
                                }
                                this.objStmToOffset.put(field2, 0);
                                break;
                            default:
                                break;
                        }
                    }
                    start++;
                    length2 = length;
                } else {
                    idx += 2;
                }
            }
        }
        thisStream *= 2;
        if (thisStream + 1 < this.xref.length && this.xref[thisStream] == 0 && this.xref[thisStream + 1] == 0) {
            this.xref[thisStream] = -1;
        }
        if (prev == -1) {
            return true;
        }
        return readXRefStream(prev);
    }

    protected void rebuildXref() throws IOException {
        this.hybridXref = false;
        this.newXrefType = false;
        this.tokens.seek(0);
        long[][] xr = new long[1024][];
        long top = 0;
        this.trailer = null;
        byte[] line = new byte[64];
        while (true) {
            long[] obj;
            long pos = this.tokens.getFilePointer();
            if (!this.tokens.readLineSegment(line, true)) {
                break;
            } else if (line[0] == (byte) 116) {
                if (PdfEncodings.convertToString(line, null).startsWith("trailer")) {
                    this.tokens.seek(pos);
                    this.tokens.nextToken();
                    pos = this.tokens.getFilePointer();
                    try {
                        PdfDictionary dic = (PdfDictionary) readPRObject();
                        if (dic.get(PdfName.ROOT) != null) {
                            this.trailer = dic;
                        } else {
                            this.tokens.seek(pos);
                        }
                    } catch (Exception e) {
                        this.tokens.seek(pos);
                    }
                }
            } else if (line[0] >= (byte) 48 && line[0] <= (byte) 57) {
                obj = PRTokeniser.checkObjectStart(line);
                if (obj != null) {
                    long num = obj[0];
                    long gen = obj[1];
                    if (num >= ((long) xr.length)) {
                        Object xr2 = new long[((int) (num * 2))][];
                        System.arraycopy(xr, 0, xr2, 0, (int) top);
                        xr = xr2;
                    }
                    if (num >= top) {
                        top = num + 1;
                    }
                    if (xr[(int) num] == null || gen >= xr[(int) num][1]) {
                        obj[0] = pos;
                        xr[(int) num] = obj;
                    }
                }
            }
        }
        if (this.trailer == null) {
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("trailer.not.found", new Object[0]));
        }
        this.xref = new long[((int) (2 * top))];
        for (int k = 0; ((long) k) < top; k++) {
            obj = xr[k];
            if (obj != null) {
                this.xref[k * 2] = obj[0];
            }
        }
    }

    protected PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            this.tokens.nextValidToken();
            if (this.tokens.getTokenType() == TokenType.END_DIC) {
                return dic;
            }
            if (this.tokens.getTokenType() != TokenType.NAME) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", this.tokens.getStringValue()));
            }
            PdfName name = new PdfName(this.tokens.getStringValue(), false);
            PdfObject obj = readPRObject();
            int type = obj.type();
            if ((-type) == TokenType.END_DIC.ordinal()) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0]));
            }
            if ((-type) == TokenType.END_ARRAY.ordinal()) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.close.bracket", new Object[0]));
            }
            dic.put(name, obj);
        }
    }

    protected PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if ((-type) == TokenType.END_ARRAY.ordinal()) {
                return array;
            }
            if ((-type) == TokenType.END_DIC.ordinal()) {
                this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0]));
            }
            array.add(obj);
        }
    }

    protected PdfObject readPRObject() throws IOException {
        this.tokens.nextValidToken();
        TokenType type = this.tokens.getTokenType();
        switch (type) {
            case START_DIC:
                boolean hasNext;
                this.readDepth++;
                PdfObject dic = readDictionary();
                this.readDepth--;
                long pos = this.tokens.getFilePointer();
                do {
                    hasNext = this.tokens.nextToken();
                    if (hasNext) {
                    }
                    if (hasNext || !this.tokens.getStringValue().equals("stream")) {
                        this.tokens.seek(pos);
                        return dic;
                    }
                    while (true) {
                        int ch = this.tokens.read();
                        if (ch != 32 && ch != 9 && ch != 0 && ch != 12) {
                            if (ch != 10) {
                                ch = this.tokens.read();
                            }
                            if (ch != 10) {
                                this.tokens.backOnePosition(ch);
                            }
                            PdfObject stream = new PRStream(this, this.tokens.getFilePointer());
                            stream.putAll(dic);
                            stream.setObjNum(this.objNum, this.objGen);
                            return stream;
                        }
                    }
                } while (this.tokens.getTokenType() == TokenType.COMMENT);
                if (hasNext) {
                    break;
                }
                this.tokens.seek(pos);
                return dic;
            case START_ARRAY:
                this.readDepth++;
                PdfObject arr = readArray();
                this.readDepth--;
                return arr;
            case NUMBER:
                return new PdfNumber(this.tokens.getStringValue());
            case STRING:
                PdfObject str = new PdfString(this.tokens.getStringValue(), null).setHexWriting(this.tokens.isHexString());
                str.setObjNum(this.objNum, this.objGen);
                if (this.strings != null) {
                    this.strings.add(str);
                }
                return str;
            case NAME:
                PdfObject cachedName = (PdfName) PdfName.staticNames.get(this.tokens.getStringValue());
                if (this.readDepth <= 0 || cachedName == null) {
                    return new PdfName(this.tokens.getStringValue(), false);
                }
                return cachedName;
            case REF:
                return new PRIndirectReference(this, this.tokens.getReference(), this.tokens.getGeneration());
            case ENDOFFILE:
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file", new Object[0]));
            default:
                String sv = this.tokens.getStringValue();
                if ("null".equals(sv)) {
                    if (this.readDepth == 0) {
                        return new PdfNull();
                    }
                    return PdfNull.PDFNULL;
                } else if (PdfBoolean.TRUE.equals(sv)) {
                    if (this.readDepth == 0) {
                        return new PdfBoolean(true);
                    }
                    return PdfBoolean.PDFTRUE;
                } else if (!PdfBoolean.FALSE.equals(sv)) {
                    return new PdfLiteral(-type.ordinal(), this.tokens.getStringValue());
                } else if (this.readDepth == 0) {
                    return new PdfBoolean(false);
                } else {
                    return PdfBoolean.PDFFALSE;
                }
        }
    }

    public static byte[] FlateDecode(byte[] in) {
        byte[] b = FlateDecode(in, true);
        if (b == null) {
            return FlateDecode(in, false);
        }
        return b;
    }

    public static byte[] decodePredictor(byte[] in, PdfObject dicPar) {
        if (dicPar == null || !dicPar.isDictionary()) {
            return in;
        }
        PdfDictionary dic = (PdfDictionary) dicPar;
        PdfObject obj = getPdfObject(dic.get(PdfName.PREDICTOR));
        if (obj == null || !obj.isNumber()) {
            return in;
        }
        int predictor = ((PdfNumber) obj).intValue();
        if (predictor < 10 && predictor != 2) {
            return in;
        }
        int width = 1;
        obj = getPdfObject(dic.get(PdfName.COLUMNS));
        if (obj != null && obj.isNumber()) {
            width = ((PdfNumber) obj).intValue();
        }
        int colors = 1;
        obj = getPdfObject(dic.get(PdfName.COLORS));
        if (obj != null && obj.isNumber()) {
            colors = ((PdfNumber) obj).intValue();
        }
        int bpc = 8;
        obj = getPdfObject(dic.get(PdfName.BITSPERCOMPONENT));
        if (obj != null && obj.isNumber()) {
            bpc = ((PdfNumber) obj).intValue();
        }
        DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(in));
        ByteArrayOutputStream fout = new ByteArrayOutputStream(in.length);
        int bytesPerPixel = (colors * bpc) / 8;
        int bytesPerRow = (((colors * width) * bpc) + 7) / 8;
        byte[] curr = new byte[bytesPerRow];
        byte[] prior = new byte[bytesPerRow];
        if (predictor != 2) {
            while (true) {
                try {
                    int filter = dataStream.read();
                    if (filter < 0) {
                        return fout.toByteArray();
                    }
                    dataStream.readFully(curr, 0, bytesPerRow);
                    int i;
                    switch (filter) {
                        case 0:
                            break;
                        case 1:
                            for (i = bytesPerPixel; i < bytesPerRow; i++) {
                                curr[i] = (byte) (curr[i] + curr[i - bytesPerPixel]);
                            }
                            break;
                        case 2:
                            for (i = 0; i < bytesPerRow; i++) {
                                curr[i] = (byte) (curr[i] + prior[i]);
                            }
                            break;
                        case 3:
                            for (i = 0; i < bytesPerPixel; i++) {
                                curr[i] = (byte) (curr[i] + (prior[i] / 2));
                            }
                            for (i = bytesPerPixel; i < bytesPerRow; i++) {
                                curr[i] = (byte) (curr[i] + (((curr[i - bytesPerPixel] & 255) + (prior[i] & 255)) / 2));
                            }
                            break;
                        case 4:
                            for (i = 0; i < bytesPerPixel; i++) {
                                curr[i] = (byte) (curr[i] + prior[i]);
                            }
                            for (i = bytesPerPixel; i < bytesPerRow; i++) {
                                int ret;
                                int a = curr[i - bytesPerPixel] & 255;
                                int b = prior[i] & 255;
                                int c = prior[i - bytesPerPixel] & 255;
                                int p = (a + b) - c;
                                int pa = Math.abs(p - a);
                                int pb = Math.abs(p - b);
                                int pc = Math.abs(p - c);
                                if (pa <= pb && pa <= pc) {
                                    ret = a;
                                } else if (pb <= pc) {
                                    ret = b;
                                } else {
                                    ret = c;
                                }
                                curr[i] = (byte) (curr[i] + ((byte) ret));
                            }
                            break;
                        default:
                            throw new RuntimeException(MessageLocalization.getComposedMessage("png.filter.unknown", new Object[0]));
                    }
                    try {
                        fout.write(curr);
                    } catch (IOException e) {
                    }
                    byte[] tmp = prior;
                    prior = curr;
                    curr = tmp;
                } catch (Exception e2) {
                    return fout.toByteArray();
                }
            }
        } else if (bpc != 8) {
            return in;
        } else {
            int numRows = in.length / bytesPerRow;
            for (int row = 0; row < numRows; row++) {
                int rowStart = row * bytesPerRow;
                for (int col = bytesPerPixel + 0; col < bytesPerRow; col++) {
                    in[rowStart + col] = (byte) (in[rowStart + col] + in[(rowStart + col) - bytesPerPixel]);
                }
            }
            return in;
        }
    }

    public static byte[] FlateDecode(byte[] in, boolean strict) {
        InflaterInputStream zip = new InflaterInputStream(new ByteArrayInputStream(in));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[(strict ? 4092 : 1)];
        while (true) {
            try {
                int n = zip.read(b);
                if (n >= 0) {
                    out.write(b, 0, n);
                } else {
                    zip.close();
                    out.close();
                    return out.toByteArray();
                }
            } catch (Exception e) {
                if (strict) {
                    return null;
                }
                return out.toByteArray();
            }
        }
    }

    public static byte[] ASCIIHexDecode(byte[] in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean first = true;
        int n1 = 0;
        for (byte b : in) {
            int ch = b & 255;
            if (ch == 62) {
                break;
            }
            if (!PRTokeniser.isWhitespace(ch)) {
                int n = PRTokeniser.getHex(ch);
                if (n == -1) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.asciihexdecode", new Object[0]));
                }
                if (first) {
                    n1 = n;
                } else {
                    out.write((byte) ((n1 << 4) + n));
                }
                if (first) {
                    first = false;
                } else {
                    first = true;
                }
            }
        }
        if (!first) {
            out.write((byte) (n1 << 4));
        }
        return out.toByteArray();
    }

    public static byte[] ASCII85Decode(byte[] in) {
        int r;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int state = 0;
        int[] chn = new int[5];
        for (byte b : in) {
            int ch = b & 255;
            if (ch == 126) {
                break;
            }
            if (!PRTokeniser.isWhitespace(ch)) {
                if (ch == 122 && state == 0) {
                    out.write(0);
                    out.write(0);
                    out.write(0);
                    out.write(0);
                } else if (ch < 33 || ch > 117) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.ascii85decode", new Object[0]));
                } else {
                    chn[state] = ch - 33;
                    state++;
                    if (state == 5) {
                        state = 0;
                        r = 0;
                        for (int j = 0; j < 5; j++) {
                            r = (r * 85) + chn[j];
                        }
                        out.write((byte) (r >> 24));
                        out.write((byte) (r >> 16));
                        out.write((byte) (r >> 8));
                        out.write((byte) r);
                    }
                }
            }
        }
        if (state == 2) {
            out.write((byte) (((((((((chn[0] * 85) * 85) * 85) * 85) + (((chn[1] * 85) * 85) * 85)) + 614125) + 7225) + 85) >> 24));
        } else if (state == 3) {
            r = (((((((chn[0] * 85) * 85) * 85) * 85) + (((chn[1] * 85) * 85) * 85)) + ((chn[2] * 85) * 85)) + 7225) + 85;
            out.write((byte) (r >> 24));
            out.write((byte) (r >> 16));
        } else if (state == 4) {
            r = (((((((chn[0] * 85) * 85) * 85) * 85) + (((chn[1] * 85) * 85) * 85)) + ((chn[2] * 85) * 85)) + (chn[3] * 85)) + 85;
            out.write((byte) (r >> 24));
            out.write((byte) (r >> 16));
            out.write((byte) (r >> 8));
        }
        return out.toByteArray();
    }

    public static byte[] LZWDecode(byte[] in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new LZWDecoder().decode(in, out);
        return out.toByteArray();
    }

    public boolean isRebuilt() {
        return this.rebuilt;
    }

    public PdfDictionary getPageN(int pageNum) {
        PdfDictionary dic = this.pageRefs.getPageN(pageNum);
        if (dic == null) {
            return null;
        }
        if (!this.appendable) {
            return dic;
        }
        dic.setIndRef(this.pageRefs.getPageOrigRef(pageNum));
        return dic;
    }

    public PdfDictionary getPageNRelease(int pageNum) {
        PdfDictionary dic = getPageN(pageNum);
        this.pageRefs.releasePage(pageNum);
        return dic;
    }

    public void releasePage(int pageNum) {
        this.pageRefs.releasePage(pageNum);
    }

    public void resetReleasePage() {
        this.pageRefs.resetReleasePage();
    }

    public PRIndirectReference getPageOrigRef(int pageNum) {
        return this.pageRefs.getPageOrigRef(pageNum);
    }

    public byte[] getPageContent(int pageNum, RandomAccessFileOrArray file) throws IOException {
        PdfDictionary page = getPageNRelease(pageNum);
        if (page == null) {
            return null;
        }
        PdfObject contents = getPdfObjectRelease(page.get(PdfName.CONTENTS));
        if (contents == null) {
            return new byte[0];
        }
        if (contents.isStream()) {
            return getStreamBytes((PRStream) contents, file);
        }
        if (!contents.isArray()) {
            return new byte[0];
        }
        PdfArray array = (PdfArray) contents;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for (int k = 0; k < array.size(); k++) {
            PdfObject item = getPdfObjectRelease(array.getPdfObject(k));
            if (item != null && item.isStream()) {
                bout.write(getStreamBytes((PRStream) item, file));
                if (k != array.size() - 1) {
                    bout.write(10);
                }
            }
        }
        return bout.toByteArray();
    }

    public static byte[] getPageContent(PdfDictionary page) throws IOException {
        byte[] bArr;
        if (page == null) {
            return null;
        }
        RandomAccessFileOrArray rf = null;
        try {
            PdfObject contents = getPdfObjectRelease(page.get(PdfName.CONTENTS));
            if (contents == null) {
                bArr = new byte[0];
                if (rf == null) {
                    return bArr;
                }
                try {
                    rf.close();
                    return bArr;
                } catch (Exception e) {
                    return bArr;
                }
            } else if (contents.isStream()) {
                if (rf == null) {
                    rf = ((PRStream) contents).getReader().getSafeFile();
                    rf.reOpen();
                }
                bArr = getStreamBytes((PRStream) contents, rf);
                if (rf == null) {
                    return bArr;
                }
                try {
                    rf.close();
                    return bArr;
                } catch (Exception e2) {
                    return bArr;
                }
            } else if (contents.isArray()) {
                PdfArray array = (PdfArray) contents;
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                for (int k = 0; k < array.size(); k++) {
                    PdfObject item = getPdfObjectRelease(array.getPdfObject(k));
                    if (item != null && item.isStream()) {
                        if (rf == null) {
                            rf = ((PRStream) item).getReader().getSafeFile();
                            rf.reOpen();
                        }
                        bout.write(getStreamBytes((PRStream) item, rf));
                        if (k != array.size() - 1) {
                            bout.write(10);
                        }
                    }
                }
                bArr = bout.toByteArray();
                if (rf == null) {
                    return bArr;
                }
                try {
                    rf.close();
                    return bArr;
                } catch (Exception e3) {
                    return bArr;
                }
            } else {
                bArr = new byte[0];
                if (rf == null) {
                    return bArr;
                }
                try {
                    rf.close();
                    return bArr;
                } catch (Exception e4) {
                    return bArr;
                }
            }
        } catch (Throwable th) {
            if (rf != null) {
                try {
                    rf.close();
                } catch (Exception e5) {
                }
            }
        }
    }

    public PdfDictionary getPageResources(int pageNum) {
        return getPageResources(getPageN(pageNum));
    }

    public PdfDictionary getPageResources(PdfDictionary pageDict) {
        return pageDict.getAsDict(PdfName.RESOURCES);
    }

    public byte[] getPageContent(int pageNum) throws IOException {
        RandomAccessFileOrArray rf = getSafeFile();
        try {
            rf.reOpen();
            byte[] pageContent = getPageContent(pageNum, rf);
            return pageContent;
        } finally {
            try {
                rf.close();
            } catch (Exception e) {
            }
        }
    }

    protected void killXref(PdfObject obj) {
        if (obj != null) {
            if (!(obj instanceof PdfIndirectReference) || obj.isIndirect()) {
                switch (obj.type()) {
                    case 5:
                        PdfArray t = (PdfArray) obj;
                        for (int i = 0; i < t.size(); i++) {
                            killXref(t.getPdfObject(i));
                        }
                        return;
                    case 6:
                    case 7:
                        PdfDictionary dic = (PdfDictionary) obj;
                        for (PdfName element : dic.getKeys()) {
                            killXref(dic.get(element));
                        }
                        return;
                    case 10:
                        int xr = ((PRIndirectReference) obj).getNumber();
                        obj = (PdfObject) this.xrefObj.get(xr);
                        this.xrefObj.set(xr, null);
                        this.freeXref = xr;
                        killXref(obj);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void setPageContent(int pageNum, byte[] content) {
        setPageContent(pageNum, content, -1);
    }

    public void setPageContent(int pageNum, byte[] content, int compressionLevel) {
        PdfDictionary page = getPageN(pageNum);
        if (page != null) {
            PdfObject contents = page.get(PdfName.CONTENTS);
            this.freeXref = -1;
            killXref(contents);
            if (this.freeXref == -1) {
                this.xrefObj.add(null);
                this.freeXref = this.xrefObj.size() - 1;
            }
            page.put(PdfName.CONTENTS, new PRIndirectReference(this, this.freeXref));
            this.xrefObj.set(this.freeXref, new PRStream(this, content, compressionLevel));
        }
    }

    public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary) throws IOException {
        return decodeBytes(b, streamDictionary, FilterHandlers.getDefaultFilterHandlers());
    }

    public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary, Map<PdfName, FilterHandler> filterHandlers) throws IOException {
        PdfObject filter = getPdfObjectRelease(streamDictionary.get(PdfName.FILTER));
        ArrayList<PdfObject> filters = new ArrayList();
        if (filter != null) {
            if (filter.isName()) {
                filters.add(filter);
            } else if (filter.isArray()) {
                filters = ((PdfArray) filter).getArrayList();
            }
        }
        ArrayList<PdfObject> dp = new ArrayList();
        PdfObject dpo = getPdfObjectRelease(streamDictionary.get(PdfName.DECODEPARMS));
        if (dpo == null || !(dpo.isDictionary() || dpo.isArray())) {
            dpo = getPdfObjectRelease(streamDictionary.get(PdfName.DP));
        }
        if (dpo != null) {
            if (dpo.isDictionary()) {
                dp.add(dpo);
            } else if (dpo.isArray()) {
                dp = ((PdfArray) dpo).getArrayList();
            }
        }
        for (int j = 0; j < filters.size(); j++) {
            PdfName filterName = (PdfName) filters.get(j);
            FilterHandler filterHandler = (FilterHandler) filterHandlers.get(filterName);
            if (filterHandler == null) {
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.filter.1.is.not.supported", filterName));
            }
            PdfDictionary decodeParams;
            if (j < dp.size()) {
                PdfObject dpEntry = getPdfObject((PdfObject) dp.get(j));
                if (dpEntry instanceof PdfDictionary) {
                    decodeParams = (PdfDictionary) dpEntry;
                } else if (dpEntry == null || (dpEntry instanceof PdfNull)) {
                    decodeParams = null;
                } else {
                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.decode.parameter.type.1.is.not.supported", dpEntry.getClass().toString()));
                }
            }
            decodeParams = null;
            b = filterHandler.decode(b, filterName, decodeParams, streamDictionary);
        }
        return b;
    }

    public static byte[] getStreamBytes(PRStream stream, RandomAccessFileOrArray file) throws IOException {
        return decodeBytes(getStreamBytesRaw(stream, file), stream);
    }

    public static byte[] getStreamBytes(PRStream stream) throws IOException {
        RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
        try {
            rf.reOpen();
            byte[] streamBytes = getStreamBytes(stream, rf);
            return streamBytes;
        } finally {
            try {
                rf.close();
            } catch (Exception e) {
            }
        }
    }

    public static byte[] getStreamBytesRaw(PRStream stream, RandomAccessFileOrArray file) throws IOException {
        PdfReader reader = stream.getReader();
        if (stream.getOffset() < 0) {
            return stream.getBytes();
        }
        byte[] b = new byte[stream.getLength()];
        file.seek(stream.getOffset());
        file.readFully(b);
        PdfEncryption decrypt = reader.getDecrypt();
        if (decrypt == null) {
            return b;
        }
        PdfObject filter = getPdfObjectRelease(stream.get(PdfName.FILTER));
        ArrayList<PdfObject> filters = new ArrayList();
        if (filter != null) {
            if (filter.isName()) {
                filters.add(filter);
            } else if (filter.isArray()) {
                filters = ((PdfArray) filter).getArrayList();
            }
        }
        boolean skip = false;
        for (int k = 0; k < filters.size(); k++) {
            PdfObject obj = getPdfObjectRelease((PdfObject) filters.get(k));
            if (obj != null && obj.toString().equals("/Crypt")) {
                skip = true;
                break;
            }
        }
        if (skip) {
            return b;
        }
        decrypt.setHashKey(stream.getObjNum(), stream.getObjGen());
        return decrypt.decryptByteArray(b);
    }

    public static byte[] getStreamBytesRaw(PRStream stream) throws IOException {
        RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
        try {
            rf.reOpen();
            byte[] streamBytesRaw = getStreamBytesRaw(stream, rf);
            return streamBytesRaw;
        } finally {
            try {
                rf.close();
            } catch (Exception e) {
            }
        }
    }

    public void eliminateSharedStreams() {
        if (this.sharedStreams) {
            this.sharedStreams = false;
            if (this.pageRefs.size() != 1) {
                int k;
                ArrayList<PRIndirectReference> newRefs = new ArrayList();
                ArrayList<PRStream> newStreams = new ArrayList();
                IntHashtable visited = new IntHashtable();
                for (k = 1; k <= this.pageRefs.size(); k++) {
                    PdfDictionary page = this.pageRefs.getPageN(k);
                    if (page != null) {
                        PdfObject contents = getPdfObject(page.get(PdfName.CONTENTS));
                        if (contents != null) {
                            if (contents.isStream()) {
                                PRIndirectReference ref = (PRIndirectReference) page.get(PdfName.CONTENTS);
                                if (visited.containsKey(ref.getNumber())) {
                                    newRefs.add(ref);
                                    newStreams.add(new PRStream((PRStream) contents, null));
                                } else {
                                    visited.put(ref.getNumber(), 1);
                                }
                            } else if (contents.isArray()) {
                                PdfArray array = (PdfArray) contents;
                                for (int j = 0; j < array.size(); j++) {
                                    PdfObject ref2 = (PRIndirectReference) array.getPdfObject(j);
                                    if (visited.containsKey(ref2.getNumber())) {
                                        newRefs.add(ref2);
                                        newStreams.add(new PRStream((PRStream) getPdfObject(ref2), null));
                                    } else {
                                        visited.put(ref2.getNumber(), 1);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!newStreams.isEmpty()) {
                    for (k = 0; k < newStreams.size(); k++) {
                        this.xrefObj.add(newStreams.get(k));
                        ((PRIndirectReference) newRefs.get(k)).setNumber(this.xrefObj.size() - 1, 0);
                    }
                }
            }
        }
    }

    public boolean isTampered() {
        return this.tampered;
    }

    public void setTampered(boolean tampered) {
        this.tampered = tampered;
        this.pageRefs.keepPages();
    }

    public byte[] getMetadata() throws IOException {
        PdfObject obj = getPdfObject(this.catalog.get(PdfName.METADATA));
        if (!(obj instanceof PRStream)) {
            return null;
        }
        RandomAccessFileOrArray rf = getSafeFile();
        byte[] b = null;
        try {
            rf.reOpen();
            b = getStreamBytes((PRStream) obj, rf);
            return b;
        } finally {
            try {
                rf.close();
            } catch (Exception e) {
            }
        }
    }

    public long getLastXref() {
        return this.lastXref;
    }

    public int getXrefSize() {
        return this.xrefObj.size();
    }

    public long getEofPos() {
        return this.eofPos;
    }

    public char getPdfVersion() {
        return this.pdfVersion;
    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    public long getPermissions() {
        return this.pValue;
    }

    public boolean is128Key() {
        return this.rValue == 3;
    }

    public PdfDictionary getTrailer() {
        return this.trailer;
    }

    PdfEncryption getDecrypt() {
        return this.decrypt;
    }

    static boolean equalsn(byte[] a1, byte[] a2) {
        int length = a2.length;
        for (int k = 0; k < length; k++) {
            if (a1[k] != a2[k]) {
                return false;
            }
        }
        return true;
    }

    static boolean existsName(PdfDictionary dic, PdfName key, PdfName value) {
        PdfObject type = getPdfObjectRelease(dic.get(key));
        if (type == null || !type.isName()) {
            return false;
        }
        return ((PdfName) type).equals(value);
    }

    static String getFontName(PdfDictionary dic) {
        if (dic == null) {
            return null;
        }
        PdfObject type = getPdfObjectRelease(dic.get(PdfName.BASEFONT));
        if (type == null || !type.isName()) {
            return null;
        }
        return PdfName.decodeName(type.toString());
    }

    static String getSubsetPrefix(PdfDictionary dic) {
        if (dic == null) {
            return null;
        }
        String s = getFontName(dic);
        if (s == null) {
            return null;
        }
        if (s.length() < 8 || s.charAt(6) != '+') {
            return null;
        }
        for (int k = 0; k < 6; k++) {
            char c = s.charAt(k);
            if (c < 'A' || c > 'Z') {
                return null;
            }
        }
        return s;
    }

    public int shuffleSubsetNames() {
        int total = 0;
        for (int k = 1; k < this.xrefObj.size(); k++) {
            PdfObject obj = getPdfObjectRelease(k);
            if (obj != null && obj.isDictionary()) {
                PdfDictionary dic = (PdfDictionary) obj;
                if (existsName(dic, PdfName.TYPE, PdfName.FONT)) {
                    String s;
                    PdfName newName;
                    PdfDictionary fd;
                    if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.MMTYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.TRUETYPE)) {
                        s = getSubsetPrefix(dic);
                        if (s != null) {
                            newName = new PdfName(BaseFont.createSubsetPrefix() + s.substring(7));
                            dic.put(PdfName.BASEFONT, newName);
                            setXrefPartialObject(k, dic);
                            total++;
                            fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
                            if (fd != null) {
                                fd.put(PdfName.FONTNAME, newName);
                            }
                        }
                    } else if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE0)) {
                        s = getSubsetPrefix(dic);
                        PdfArray arr = dic.getAsArray(PdfName.DESCENDANTFONTS);
                        if (!(arr == null || arr.isEmpty())) {
                            PdfDictionary desc = arr.getAsDict(0);
                            String sde = getSubsetPrefix(desc);
                            if (sde != null) {
                                String ns = BaseFont.createSubsetPrefix();
                                if (s != null) {
                                    dic.put(PdfName.BASEFONT, new PdfName(ns + s.substring(7)));
                                }
                                setXrefPartialObject(k, dic);
                                newName = new PdfName(ns + sde.substring(7));
                                desc.put(PdfName.BASEFONT, newName);
                                total++;
                                fd = desc.getAsDict(PdfName.FONTDESCRIPTOR);
                                if (fd != null) {
                                    fd.put(PdfName.FONTNAME, newName);
                                }
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    public int createFakeFontSubsets() {
        int total = 0;
        for (int k = 1; k < this.xrefObj.size(); k++) {
            PdfObject obj = getPdfObjectRelease(k);
            if (obj != null && obj.isDictionary()) {
                PdfDictionary dic = (PdfDictionary) obj;
                if (existsName(dic, PdfName.TYPE, PdfName.FONT) && ((existsName(dic, PdfName.SUBTYPE, PdfName.TYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.MMTYPE1) || existsName(dic, PdfName.SUBTYPE, PdfName.TRUETYPE)) && getSubsetPrefix(dic) == null)) {
                    String s = getFontName(dic);
                    if (s != null) {
                        String ns = BaseFont.createSubsetPrefix() + s;
                        PdfDictionary fd = (PdfDictionary) getPdfObjectRelease(dic.get(PdfName.FONTDESCRIPTOR));
                        if (!(fd == null || (fd.get(PdfName.FONTFILE) == null && fd.get(PdfName.FONTFILE2) == null && fd.get(PdfName.FONTFILE3) == null))) {
                            fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
                            PdfName newName = new PdfName(ns);
                            dic.put(PdfName.BASEFONT, newName);
                            fd.put(PdfName.FONTNAME, newName);
                            setXrefPartialObject(k, dic);
                            total++;
                        }
                    }
                }
            }
        }
        return total;
    }

    private static PdfArray getNameArray(PdfObject obj) {
        if (obj == null) {
            return null;
        }
        obj = getPdfObjectRelease(obj);
        if (obj == null) {
            return null;
        }
        if (obj.isArray()) {
            return (PdfArray) obj;
        }
        if (obj.isDictionary()) {
            PdfObject arr2 = getPdfObjectRelease(((PdfDictionary) obj).get(PdfName.f120D));
            if (arr2 != null && arr2.isArray()) {
                return (PdfArray) arr2;
            }
        }
        return null;
    }

    public HashMap<Object, PdfObject> getNamedDestination() {
        return getNamedDestination(false);
    }

    public HashMap<Object, PdfObject> getNamedDestination(boolean keepNames) {
        HashMap<Object, PdfObject> names = getNamedDestinationFromNames(keepNames);
        names.putAll(getNamedDestinationFromStrings());
        return names;
    }

    public HashMap<String, PdfObject> getNamedDestinationFromNames() {
        return new HashMap(getNamedDestinationFromNames(false));
    }

    public HashMap<Object, PdfObject> getNamedDestinationFromNames(boolean keepNames) {
        HashMap<Object, PdfObject> names = new HashMap();
        if (this.catalog.get(PdfName.DESTS) != null) {
            PdfDictionary dic = (PdfDictionary) getPdfObjectRelease(this.catalog.get(PdfName.DESTS));
            if (dic != null) {
                for (PdfName key : dic.getKeys()) {
                    PdfArray arr = getNameArray(dic.get(key));
                    if (arr != null) {
                        if (keepNames) {
                            names.put(key, arr);
                        } else {
                            names.put(PdfName.decodeName(key.toString()), arr);
                        }
                    }
                }
            }
        }
        return names;
    }

    public HashMap<String, PdfObject> getNamedDestinationFromStrings() {
        if (this.catalog.get(PdfName.NAMES) != null) {
            PdfDictionary dic = (PdfDictionary) getPdfObjectRelease(this.catalog.get(PdfName.NAMES));
            if (dic != null) {
                dic = (PdfDictionary) getPdfObjectRelease(dic.get(PdfName.DESTS));
                if (dic != null) {
                    HashMap<String, PdfObject> readTree = PdfNameTree.readTree(dic);
                    Iterator<Entry<String, PdfObject>> it = readTree.entrySet().iterator();
                    while (it.hasNext()) {
                        Entry<String, PdfObject> entry = (Entry) it.next();
                        PdfArray arr = getNameArray((PdfObject) entry.getValue());
                        if (arr != null) {
                            entry.setValue(arr);
                        } else {
                            it.remove();
                        }
                    }
                    return readTree;
                }
            }
        }
        return new HashMap();
    }

    public void removeFields() {
        this.pageRefs.resetReleasePage();
        for (int k = 1; k <= this.pageRefs.size(); k++) {
            PdfDictionary page = this.pageRefs.getPageN(k);
            PdfArray annots = page.getAsArray(PdfName.ANNOTS);
            if (annots == null) {
                this.pageRefs.releasePage(k);
            } else {
                int j = 0;
                while (j < annots.size()) {
                    PdfObject obj = getPdfObjectRelease(annots.getPdfObject(j));
                    if (obj != null && obj.isDictionary()) {
                        if (PdfName.WIDGET.equals(((PdfDictionary) obj).get(PdfName.SUBTYPE))) {
                            int j2 = j - 1;
                            annots.remove(j);
                            j = j2;
                        }
                    }
                    j++;
                }
                if (annots.isEmpty()) {
                    page.remove(PdfName.ANNOTS);
                } else {
                    this.pageRefs.releasePage(k);
                }
            }
        }
        this.catalog.remove(PdfName.ACROFORM);
        this.pageRefs.resetReleasePage();
    }

    public void removeAnnotations() {
        this.pageRefs.resetReleasePage();
        for (int k = 1; k <= this.pageRefs.size(); k++) {
            PdfDictionary page = this.pageRefs.getPageN(k);
            if (page.get(PdfName.ANNOTS) == null) {
                this.pageRefs.releasePage(k);
            } else {
                page.remove(PdfName.ANNOTS);
            }
        }
        this.catalog.remove(PdfName.ACROFORM);
        this.pageRefs.resetReleasePage();
    }

    public ArrayList<PdfImportedLink> getLinks(int page) {
        this.pageRefs.resetReleasePage();
        ArrayList<PdfImportedLink> result = new ArrayList();
        PdfDictionary pageDic = this.pageRefs.getPageN(page);
        if (pageDic.get(PdfName.ANNOTS) != null) {
            PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
            for (int j = 0; j < annots.size(); j++) {
                PdfDictionary annot = (PdfDictionary) getPdfObjectRelease(annots.getPdfObject(j));
                if (PdfName.LINK.equals(annot.get(PdfName.SUBTYPE))) {
                    result.add(new PdfImportedLink(annot));
                }
            }
        }
        this.pageRefs.releasePage(page);
        this.pageRefs.resetReleasePage();
        return result;
    }

    private void iterateBookmarks(PdfObject outlineRef, HashMap<Object, PdfObject> names) {
        while (outlineRef != null) {
            replaceNamedDestination(outlineRef, names);
            PdfDictionary outline = (PdfDictionary) getPdfObjectRelease(outlineRef);
            PdfObject first = outline.get(PdfName.FIRST);
            if (first != null) {
                iterateBookmarks(first, names);
            }
            outlineRef = outline.get(PdfName.NEXT);
        }
    }

    public void makeRemoteNamedDestinationsLocal() {
        if (!this.remoteToLocalNamedDestinations) {
            this.remoteToLocalNamedDestinations = true;
            HashMap<Object, PdfObject> names = getNamedDestination(true);
            if (!names.isEmpty()) {
                for (int k = 1; k <= this.pageRefs.size(); k++) {
                    PdfObject annotsRef = this.pageRefs.getPageN(k).get(PdfName.ANNOTS);
                    PdfArray annots = (PdfArray) getPdfObject(annotsRef);
                    int annotIdx = this.lastXrefPartial;
                    releaseLastXrefPartial();
                    if (annots == null) {
                        this.pageRefs.releasePage(k);
                    } else {
                        boolean commitAnnots = false;
                        for (int an = 0; an < annots.size(); an++) {
                            PdfObject objRef = annots.getPdfObject(an);
                            if (convertNamedDestination(objRef, names) && !objRef.isIndirect()) {
                                commitAnnots = true;
                            }
                        }
                        if (commitAnnots) {
                            setXrefPartialObject(annotIdx, annots);
                        }
                        if (!commitAnnots || annotsRef.isIndirect()) {
                            this.pageRefs.releasePage(k);
                        }
                    }
                }
            }
        }
    }

    private boolean convertNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
        obj = getPdfObject(obj);
        int objIdx = this.lastXrefPartial;
        releaseLastXrefPartial();
        if (obj != null && obj.isDictionary()) {
            PdfObject ob2 = getPdfObject(((PdfDictionary) obj).get(PdfName.f117A));
            if (ob2 != null) {
                int obj2Idx = this.lastXrefPartial;
                releaseLastXrefPartial();
                PdfDictionary dic = (PdfDictionary) ob2;
                if (PdfName.GOTOR.equals((PdfName) getPdfObjectRelease(dic.get(PdfName.f133S)))) {
                    PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.f120D));
                    Object name = null;
                    if (ob3 != null) {
                        if (ob3.isName()) {
                            name = ob3;
                        } else if (ob3.isString()) {
                            name = ob3.toString();
                        }
                        if (((PdfArray) names.get(name)) != null) {
                            dic.remove(PdfName.f122F);
                            dic.remove(PdfName.NEWWINDOW);
                            dic.put(PdfName.f133S, PdfName.GOTO);
                            setXrefPartialObject(obj2Idx, ob2);
                            setXrefPartialObject(objIdx, obj);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void consolidateNamedDestinations() {
        if (!this.consolidateNamedDestinations) {
            this.consolidateNamedDestinations = true;
            HashMap<Object, PdfObject> names = getNamedDestination(true);
            if (!names.isEmpty()) {
                for (int k = 1; k <= this.pageRefs.size(); k++) {
                    PdfObject annotsRef = this.pageRefs.getPageN(k).get(PdfName.ANNOTS);
                    PdfArray annots = (PdfArray) getPdfObject(annotsRef);
                    int annotIdx = this.lastXrefPartial;
                    releaseLastXrefPartial();
                    if (annots == null) {
                        this.pageRefs.releasePage(k);
                    } else {
                        boolean commitAnnots = false;
                        for (int an = 0; an < annots.size(); an++) {
                            PdfObject objRef = annots.getPdfObject(an);
                            if (replaceNamedDestination(objRef, names) && !objRef.isIndirect()) {
                                commitAnnots = true;
                            }
                        }
                        if (commitAnnots) {
                            setXrefPartialObject(annotIdx, annots);
                        }
                        if (!commitAnnots || annotsRef.isIndirect()) {
                            this.pageRefs.releasePage(k);
                        }
                    }
                }
                PdfDictionary outlines = (PdfDictionary) getPdfObjectRelease(this.catalog.get(PdfName.OUTLINES));
                if (outlines != null) {
                    iterateBookmarks(outlines.get(PdfName.FIRST), names);
                }
            }
        }
    }

    private boolean replaceNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
        obj = getPdfObject(obj);
        int objIdx = this.lastXrefPartial;
        releaseLastXrefPartial();
        if (obj != null && obj.isDictionary()) {
            PdfObject ob2 = getPdfObjectRelease(((PdfDictionary) obj).get(PdfName.DEST));
            Object name = null;
            PdfArray dest;
            if (ob2 != null) {
                if (ob2.isName()) {
                    name = ob2;
                } else if (ob2.isString()) {
                    name = ob2.toString();
                }
                dest = (PdfArray) names.get(name);
                if (dest != null) {
                    ((PdfDictionary) obj).put(PdfName.DEST, dest);
                    setXrefPartialObject(objIdx, obj);
                    return true;
                }
            }
            ob2 = getPdfObject(((PdfDictionary) obj).get(PdfName.f117A));
            if (ob2 != null) {
                int obj2Idx = this.lastXrefPartial;
                releaseLastXrefPartial();
                PdfDictionary dic = (PdfDictionary) ob2;
                if (PdfName.GOTO.equals((PdfName) getPdfObjectRelease(dic.get(PdfName.f133S)))) {
                    PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.f120D));
                    if (ob3 != null) {
                        if (ob3.isName()) {
                            name = ob3;
                        } else if (ob3.isString()) {
                            name = ob3.toString();
                        }
                    }
                    dest = (PdfArray) names.get(name);
                    if (dest != null) {
                        dic.put(PdfName.f120D, dest);
                        setXrefPartialObject(obj2Idx, ob2);
                        setXrefPartialObject(objIdx, obj);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected static PdfDictionary duplicatePdfDictionary(PdfDictionary original, PdfDictionary copy, PdfReader newReader) {
        if (copy == null) {
            copy = new PdfDictionary();
        }
        for (PdfName key : original.getKeys()) {
            copy.put(key, duplicatePdfObject(original.get(key), newReader));
        }
        return copy;
    }

    protected static PdfObject duplicatePdfObject(PdfObject original, PdfReader newReader) {
        if (original == null) {
            return null;
        }
        switch (original.type()) {
            case 5:
                PdfObject arr = new PdfArray();
                Iterator<PdfObject> it = ((PdfArray) original).listIterator();
                while (it.hasNext()) {
                    arr.add(duplicatePdfObject((PdfObject) it.next(), newReader));
                }
                return arr;
            case 6:
                return duplicatePdfDictionary((PdfDictionary) original, null, newReader);
            case 7:
                PRStream org = (PRStream) original;
                PdfObject stream = new PRStream(org, null, newReader);
                duplicatePdfDictionary(org, stream, newReader);
                return stream;
            case 10:
                PRIndirectReference org2 = (PRIndirectReference) original;
                return new PRIndirectReference(newReader, org2.getNumber(), org2.getGeneration());
            default:
                return original;
        }
    }

    public void close() {
        try {
            this.tokens.close();
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected void removeUnusedNode(PdfObject obj, boolean[] hits) {
        Stack<Object> state = new Stack();
        state.push(obj);
        while (!state.empty()) {
            PdfObject current = state.pop();
            if (current != null) {
                int num;
                ArrayList<PdfObject> ar = null;
                PdfDictionary dic = null;
                PdfName[] keys = null;
                Object[] objs = null;
                int idx = 0;
                if (current instanceof PdfObject) {
                    obj = current;
                    switch (obj.type()) {
                        case 5:
                            ar = ((PdfArray) obj).getArrayList();
                            break;
                        case 6:
                        case 7:
                            dic = (PdfDictionary) obj;
                            keys = new PdfName[dic.size()];
                            dic.getKeys().toArray(keys);
                            break;
                        case 10:
                            PdfObject ref = (PRIndirectReference) obj;
                            num = ref.getNumber();
                            if (!hits[num]) {
                                hits[num] = true;
                                state.push(getPdfObjectRelease(ref));
                                break;
                            }
                            continue;
                        default:
                            continue;
                    }
                } else {
                    objs = (Object[]) current;
                    if (objs[0] instanceof ArrayList) {
                        ar = objs[0];
                        idx = ((Integer) objs[1]).intValue();
                    } else {
                        keys = (PdfName[]) objs[0];
                        dic = objs[1];
                        idx = ((Integer) objs[2]).intValue();
                    }
                }
                int k;
                PdfObject v;
                if (ar != null) {
                    k = idx;
                    while (k < ar.size()) {
                        v = (PdfObject) ar.get(k);
                        if (v.isIndirect()) {
                            num = ((PRIndirectReference) v).getNumber();
                            if (num >= this.xrefObj.size() || (!this.partial && this.xrefObj.get(num) == null)) {
                                ar.set(k, PdfNull.PDFNULL);
                                k++;
                            }
                        }
                        if (objs == null) {
                            state.push(new Object[]{ar, Integer.valueOf(k + 1)});
                        } else {
                            objs[1] = Integer.valueOf(k + 1);
                            state.push(objs);
                        }
                        state.push(v);
                    }
                } else {
                    k = idx;
                    while (k < keys.length) {
                        PdfName key = keys[k];
                        v = dic.get(key);
                        if (v.isIndirect()) {
                            num = ((PRIndirectReference) v).getNumber();
                            if (num < 0 || num >= this.xrefObj.size() || (!this.partial && this.xrefObj.get(num) == null)) {
                                dic.put(key, PdfNull.PDFNULL);
                                k++;
                            }
                        }
                        if (objs == null) {
                            state.push(new Object[]{keys, dic, Integer.valueOf(k + 1)});
                        } else {
                            objs[2] = Integer.valueOf(k + 1);
                            state.push(objs);
                        }
                        state.push(v);
                    }
                }
            }
        }
    }

    public int removeUnusedObjects() {
        boolean[] hits = new boolean[this.xrefObj.size()];
        removeUnusedNode(this.trailer, hits);
        int total = 0;
        int k;
        if (this.partial) {
            for (k = 1; k < hits.length; k++) {
                if (!hits[k]) {
                    this.xref[k * 2] = -1;
                    this.xref[(k * 2) + 1] = 0;
                    this.xrefObj.set(k, null);
                    total++;
                }
            }
        } else {
            for (k = 1; k < hits.length; k++) {
                if (!hits[k]) {
                    this.xrefObj.set(k, null);
                    total++;
                }
            }
        }
        return total;
    }

    public AcroFields getAcroFields() {
        return new AcroFields(this, null);
    }

    public String getJavaScript(RandomAccessFileOrArray file) throws IOException {
        PdfDictionary names = (PdfDictionary) getPdfObjectRelease(this.catalog.get(PdfName.NAMES));
        if (names == null) {
            return null;
        }
        PdfDictionary js = (PdfDictionary) getPdfObjectRelease(names.get(PdfName.JAVASCRIPT));
        if (js == null) {
            return null;
        }
        HashMap<String, PdfObject> jscript = PdfNameTree.readTree(js);
        String[] sortedNames = (String[]) jscript.keySet().toArray(new String[jscript.size()]);
        Arrays.sort(sortedNames);
        StringBuffer buf = new StringBuffer();
        for (Object obj : sortedNames) {
            PdfDictionary j = (PdfDictionary) getPdfObjectRelease((PdfObject) jscript.get(obj));
            if (j != null) {
                PdfObject obj2 = getPdfObjectRelease(j.get(PdfName.JS));
                if (obj2 != null) {
                    if (obj2.isString()) {
                        buf.append(((PdfString) obj2).toUnicodeString()).append('\n');
                    } else if (obj2.isStream()) {
                        byte[] bytes = getStreamBytes((PRStream) obj2, file);
                        if (bytes.length >= 2 && bytes[0] == (byte) -2 && bytes[1] == (byte) -1) {
                            buf.append(PdfEncodings.convertToString(bytes, PdfObject.TEXT_UNICODE));
                        } else {
                            buf.append(PdfEncodings.convertToString(bytes, PdfObject.TEXT_PDFDOCENCODING));
                        }
                        buf.append('\n');
                    }
                }
            }
        }
        return buf.toString();
    }

    public String getJavaScript() throws IOException {
        RandomAccessFileOrArray rf = getSafeFile();
        try {
            rf.reOpen();
            String javaScript = getJavaScript(rf);
            return javaScript;
        } finally {
            try {
                rf.close();
            } catch (Exception e) {
            }
        }
    }

    public void selectPages(String ranges) {
        selectPages(SequenceList.expand(ranges, getNumberOfPages()));
    }

    public void selectPages(List<Integer> pagesToKeep) {
        selectPages(pagesToKeep, true);
    }

    protected void selectPages(List<Integer> pagesToKeep, boolean removeUnused) {
        this.pageRefs.selectPages(pagesToKeep);
        if (removeUnused) {
            removeUnusedObjects();
        }
    }

    public void setViewerPreferences(int preferences) {
        this.viewerPreferences.setViewerPreferences(preferences);
        setViewerPreferences(this.viewerPreferences);
    }

    public void addViewerPreference(PdfName key, PdfObject value) {
        this.viewerPreferences.addViewerPreference(key, value);
        setViewerPreferences(this.viewerPreferences);
    }

    public void setViewerPreferences(PdfViewerPreferencesImp vp) {
        vp.addToCatalog(this.catalog);
    }

    public int getSimpleViewerPreferences() {
        return PdfViewerPreferencesImp.getViewerPreferences(this.catalog).getPageLayoutAndMode();
    }

    public boolean isAppendable() {
        return this.appendable;
    }

    public void setAppendable(boolean appendable) {
        this.appendable = appendable;
        if (appendable) {
            getPdfObject(this.trailer.get(PdfName.ROOT));
        }
    }

    public boolean isNewXrefType() {
        return this.newXrefType;
    }

    public long getFileLength() {
        return this.fileLength;
    }

    public boolean isHybridXref() {
        return this.hybridXref;
    }

    PdfIndirectReference getCryptoRef() {
        if (this.cryptoRef == null) {
            return null;
        }
        return new PdfIndirectReference(0, this.cryptoRef.getNumber(), this.cryptoRef.getGeneration());
    }

    public boolean hasUsageRights() {
        PdfDictionary perms = this.catalog.getAsDict(PdfName.PERMS);
        if (perms == null) {
            return false;
        }
        if (perms.contains(PdfName.UR) || perms.contains(PdfName.UR3)) {
            return true;
        }
        return false;
    }

    public void removeUsageRights() {
        PdfDictionary perms = this.catalog.getAsDict(PdfName.PERMS);
        if (perms != null) {
            perms.remove(PdfName.UR);
            perms.remove(PdfName.UR3);
            if (perms.size() == 0) {
                this.catalog.remove(PdfName.PERMS);
            }
        }
    }

    public int getCertificationLevel() {
        PdfDictionary dic = this.catalog.getAsDict(PdfName.PERMS);
        if (dic == null) {
            return 0;
        }
        dic = dic.getAsDict(PdfName.DOCMDP);
        if (dic == null) {
            return 0;
        }
        PdfArray arr = dic.getAsArray(PdfName.REFERENCE);
        if (arr == null || arr.size() == 0) {
            return 0;
        }
        dic = arr.getAsDict(0);
        if (dic == null) {
            return 0;
        }
        dic = dic.getAsDict(PdfName.TRANSFORMPARAMS);
        if (dic == null) {
            return 0;
        }
        PdfNumber p = dic.getAsNumber(PdfName.f130P);
        if (p != null) {
            return p.intValue();
        }
        return 0;
    }

    public final boolean isOpenedWithFullPermissions() {
        return !this.encrypted || this.ownerPasswordUsed || unethicalreading;
    }

    public int getCryptoMode() {
        if (this.decrypt == null) {
            return -1;
        }
        return this.decrypt.getCryptoMode();
    }

    public boolean isMetadataEncrypted() {
        if (this.decrypt == null) {
            return false;
        }
        return this.decrypt.isMetadataEncrypted();
    }

    public byte[] computeUserPassword() {
        if (this.encrypted && this.ownerPasswordUsed) {
            return this.decrypt.computeUserPassword(this.password);
        }
        return null;
    }
}
