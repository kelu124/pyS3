package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.internal.PdfViewerPreferencesImp;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;
import com.itextpdf.xmp.options.SerializeOptions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bytedeco.javacpp.dc1394;

class PdfStamperImp extends PdfWriter {
    protected Counter COUNTER = CounterFactory.getCounter(PdfStamper.class);
    protected AcroFields acroFields;
    protected boolean append;
    protected boolean closed = false;
    protected HashSet<PdfTemplate> fieldTemplates = new HashSet();
    protected boolean fieldsAdded = false;
    protected RandomAccessFileOrArray file;
    protected boolean flat = false;
    protected boolean flatFreeText = false;
    protected boolean flatannotations = false;
    protected int initialXrefSize;
    protected IntHashtable marked;
    IntHashtable myXref = new IntHashtable();
    protected int[] namePtr = new int[]{0};
    protected PdfAction openAction;
    HashMap<PdfDictionary, PageStamp> pagesToContent = new HashMap();
    protected HashSet<String> partialFlattening = new HashSet();
    PdfReader reader;
    HashMap<PdfReader, RandomAccessFileOrArray> readers2file = new HashMap();
    HashMap<PdfReader, IntHashtable> readers2intrefs = new HashMap();
    private boolean rotateContents = true;
    protected int sigFlags = 0;
    protected boolean useVp = false;
    protected PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();

    static class PageStamp {
        StampContent over;
        PdfDictionary pageN;
        PageResources pageResources;
        int replacePoint = 0;
        StampContent under;

        PageStamp(PdfStamperImp stamper, PdfReader reader, PdfDictionary pageN) {
            this.pageN = pageN;
            this.pageResources = new PageResources();
            this.pageResources.setOriginalResources(pageN.getAsDict(PdfName.RESOURCES), stamper.namePtr);
        }
    }

    protected Counter getCounter() {
        return this.COUNTER;
    }

    protected PdfStamperImp(PdfReader reader, OutputStream os, char pdfVersion, boolean append) throws DocumentException, IOException {
        super(new PdfDocument(), os);
        if (!reader.isOpenedWithFullPermissions()) {
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
        } else if (reader.isTampered()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.original.document.was.reused.read.it.again.from.file", new Object[0]));
        } else {
            reader.setTampered(true);
            this.reader = reader;
            this.file = reader.getSafeFile();
            this.append = append;
            if (reader.isEncrypted() && (append || PdfReader.unethicalreading)) {
                this.crypto = new PdfEncryption(reader.getDecrypt());
            }
            if (append) {
                if (reader.isRebuilt()) {
                    throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.requires.a.document.without.errors.even.if.recovery.was.possible", new Object[0]));
                }
                this.pdf_version.setAppendmode(true);
                byte[] buf = new byte[8192];
                while (true) {
                    int n = this.file.read(buf);
                    if (n <= 0) {
                        break;
                    }
                    this.os.write(buf, 0, n);
                }
                this.prevxref = reader.getLastXref();
                reader.setAppendable(true);
            } else if (pdfVersion == '\u0000') {
                super.setPdfVersion(reader.getPdfVersion());
            } else {
                super.setPdfVersion(pdfVersion);
            }
            if (reader.isTagged()) {
                setTagged();
            }
            super.open();
            this.pdf.addWriter(this);
            if (append) {
                this.body.setRefnum(reader.getXrefSize());
                this.marked = new IntHashtable();
                if (reader.isNewXrefType()) {
                    this.fullCompression = true;
                }
                if (reader.isHybridXref()) {
                    this.fullCompression = false;
                }
            }
            this.initialXrefSize = reader.getXrefSize();
            readColorProfile();
        }
    }

    protected void readColorProfile() {
        PdfObject outputIntents = this.reader.getCatalog().getAsArray(PdfName.OUTPUTINTENTS);
        if (outputIntents != null && ((PdfArray) outputIntents).size() > 0) {
            PdfStream iccProfileStream = null;
            for (int i = 0; i < ((PdfArray) outputIntents).size(); i++) {
                PdfDictionary outputIntentDictionary = ((PdfArray) outputIntents).getAsDict(i);
                if (outputIntentDictionary != null) {
                    iccProfileStream = outputIntentDictionary.getAsStream(PdfName.DESTOUTPUTPROFILE);
                    if (iccProfileStream != null) {
                        break;
                    }
                }
            }
            if (iccProfileStream instanceof PRStream) {
                try {
                    this.colorProfile = ICC_Profile.getInstance(PdfReader.getStreamBytes((PRStream) iccProfileStream));
                } catch (IOException exc) {
                    throw new ExceptionConverter(exc);
                }
            }
        }
    }

    protected void setViewerPreferences() {
        this.reader.setViewerPreferences(this.viewerPreferences);
        markUsed(this.reader.getTrailer().get(PdfName.ROOT));
    }

    protected void close(Map<String, String> moreInfo) throws IOException {
        if (!this.closed) {
            PdfIndirectReference info;
            ByteArrayOutputStream baos;
            if (this.useVp) {
                setViewerPreferences();
            }
            if (this.flat) {
                flatFields();
            }
            if (this.flatFreeText) {
                flatFreeTextFields();
            }
            if (this.flatannotations) {
                flattenAnnotations();
            }
            addFieldResources();
            PdfObject catalog = this.reader.getCatalog();
            getPdfVersion().addToCatalog(catalog);
            PdfObject acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), this.reader.getCatalog());
            if (this.acroFields != null && this.acroFields.getXfa().isChanged()) {
                markUsed(acroForm);
                if (!this.flat) {
                    this.acroFields.getXfa().setXfa(this);
                }
            }
            if (!(this.sigFlags == 0 || acroForm == null)) {
                acroForm.put(PdfName.SIGFLAGS, new PdfNumber(this.sigFlags));
                markUsed(acroForm);
                markUsed(catalog);
            }
            this.closed = true;
            addSharedObjectsToBody();
            setOutlines();
            setJavaScript();
            addFileAttachments();
            if (this.extraCatalog != null) {
                catalog.mergeDifferent(this.extraCatalog);
            }
            if (this.openAction != null) {
                catalog.put(PdfName.OPENACTION, this.openAction);
            }
            if (this.pdf.pageLabels != null) {
                catalog.put(PdfName.PAGELABELS, this.pdf.pageLabels.getDictionary(this));
            }
            if (!this.documentOCG.isEmpty()) {
                fillOCProperties(false);
                PdfDictionary ocdict = catalog.getAsDict(PdfName.OCPROPERTIES);
                if (ocdict == null) {
                    this.reader.getCatalog().put(PdfName.OCPROPERTIES, this.OCProperties);
                } else {
                    ocdict.put(PdfName.OCGS, this.OCProperties.get(PdfName.OCGS));
                    PdfDictionary ddict = ocdict.getAsDict(PdfName.f120D);
                    if (ddict == null) {
                        ddict = new PdfDictionary();
                        ocdict.put(PdfName.f120D, ddict);
                    }
                    ddict.put(PdfName.ORDER, this.OCProperties.getAsDict(PdfName.f120D).get(PdfName.ORDER));
                    ddict.put(PdfName.RBGROUPS, this.OCProperties.getAsDict(PdfName.f120D).get(PdfName.RBGROUPS));
                    ddict.put(PdfName.OFF, this.OCProperties.getAsDict(PdfName.f120D).get(PdfName.OFF));
                    ddict.put(PdfName.AS, this.OCProperties.getAsDict(PdfName.f120D).get(PdfName.AS));
                }
                PdfWriter.checkPdfIsoConformance(this, 7, this.OCProperties);
            }
            int skipInfo = -1;
            PdfIndirectReference iInfo = this.reader.getTrailer().getAsIndirectObject(PdfName.INFO);
            if (iInfo != null) {
                skipInfo = iInfo.getNumber();
            }
            PdfDictionary oldInfo = this.reader.getTrailer().getAsDict(PdfName.INFO);
            String producer = null;
            if (!(oldInfo == null || oldInfo.get(PdfName.PRODUCER) == null)) {
                producer = oldInfo.getAsString(PdfName.PRODUCER).toUnicodeString();
            }
            Version version = Version.getInstance();
            if (producer == null || version.getVersion().indexOf(version.getProduct()) == -1) {
                producer = version.getVersion();
            } else {
                StringBuffer buf;
                int idx = producer.indexOf("; modified using");
                if (idx == -1) {
                    buf = new StringBuffer(producer);
                } else {
                    buf = new StringBuffer(producer.substring(0, idx));
                }
                buf.append("; modified using ");
                buf.append(version.getVersion());
                producer = buf.toString();
            }
            PdfObject newInfo = new PdfDictionary();
            if (oldInfo != null) {
                for (PdfName key : oldInfo.getKeys()) {
                    newInfo.put(key, PdfReader.getPdfObject(oldInfo.get(key)));
                }
            }
            if (moreInfo != null) {
                for (Entry<String, String> entry : moreInfo.entrySet()) {
                    PdfName pdfName = new PdfName((String) entry.getKey());
                    String value = (String) entry.getValue();
                    if (value == null) {
                        newInfo.remove(pdfName);
                    } else {
                        newInfo.put(pdfName, new PdfString(value, PdfObject.TEXT_UNICODE));
                    }
                }
            }
            PdfDate date = new PdfDate();
            newInfo.put(PdfName.MODDATE, date);
            newInfo.put(PdfName.PRODUCER, new PdfString(producer, PdfObject.TEXT_UNICODE));
            if (!this.append) {
                info = addToBody(newInfo, false).getIndirectReference();
            } else if (iInfo == null) {
                info = addToBody(newInfo, false).getIndirectReference();
            } else {
                info = addToBody(newInfo, iInfo.getNumber(), false).getIndirectReference();
            }
            byte[] altMetadata = null;
            PdfObject xmpo = PdfReader.getPdfObject(catalog.get(PdfName.METADATA));
            if (xmpo != null && xmpo.isStream()) {
                altMetadata = PdfReader.getStreamBytesRaw((PRStream) xmpo);
                PdfReader.killIndirect(catalog.get(PdfName.METADATA));
            }
            PdfObject xmp = null;
            if (this.xmpMetadata != null) {
                altMetadata = this.xmpMetadata;
            } else if (this.xmpWriter != null) {
                try {
                    baos = new ByteArrayOutputStream();
                    PdfProperties.setProducer(this.xmpWriter.getXmpMeta(), producer);
                    XmpBasicProperties.setModDate(this.xmpWriter.getXmpMeta(), date.getW3CDate());
                    XmpBasicProperties.setMetaDataDate(this.xmpWriter.getXmpMeta(), date.getW3CDate());
                    this.xmpWriter.serialize(baos);
                    this.xmpWriter.close();
                    xmp = new PdfStream(baos.toByteArray());
                } catch (XMPException e) {
                    this.xmpWriter = null;
                }
            }
            if (xmp == null && altMetadata != null) {
                PdfObject pdfStream;
                try {
                    baos = new ByteArrayOutputStream();
                    if (moreInfo == null || this.xmpMetadata != null) {
                        XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(altMetadata);
                        PdfProperties.setProducer(xmpMeta, producer);
                        XmpBasicProperties.setModDate(xmpMeta, date.getW3CDate());
                        XmpBasicProperties.setMetaDataDate(xmpMeta, date.getW3CDate());
                        SerializeOptions serializeOptions = new SerializeOptions();
                        serializeOptions.setPadding(2000);
                        XMPMetaFactory.serialize(xmpMeta, baos, serializeOptions);
                    } else {
                        createXmpWriter(baos, newInfo).close();
                    }
                    pdfStream = new PdfStream(baos.toByteArray());
                } catch (XMPException e2) {
                    pdfStream = new PdfStream(altMetadata);
                } catch (IOException e3) {
                    pdfStream = new PdfStream(altMetadata);
                }
            }
            if (xmp != null) {
                xmp.put(PdfName.TYPE, PdfName.METADATA);
                xmp.put(PdfName.SUBTYPE, PdfName.XML);
                if (!(this.crypto == null || this.crypto.isMetadataEncrypted())) {
                    PdfArray ar = new PdfArray();
                    ar.add(PdfName.CRYPT);
                    xmp.put(PdfName.FILTER, ar);
                }
                if (!this.append || xmpo == null) {
                    catalog.put(PdfName.METADATA, this.body.add(xmp).getIndirectReference());
                    markUsed(catalog);
                } else {
                    this.body.add(xmp, xmpo.getIndRef());
                }
            }
            close(info, skipInfo);
        }
    }

    protected void close(PdfIndirectReference info, int skipInfo) throws IOException {
        PdfObject fileID;
        alterContents();
        int rootN = ((PRIndirectReference) this.reader.trailer.get(PdfName.ROOT)).getNumber();
        int k;
        PdfObject obj;
        if (this.append) {
            int[] keys = this.marked.getKeys();
            for (int j : keys) {
                obj = this.reader.getPdfObjectRelease(j);
                if (!(obj == null || skipInfo == j || j >= this.initialXrefSize)) {
                    addToBody(obj, obj.getIndRef(), j != rootN);
                }
            }
            for (k = this.initialXrefSize; k < this.reader.getXrefSize(); k++) {
                obj = this.reader.getPdfObject(k);
                if (obj != null) {
                    addToBody(obj, getNewObjectNumber(this.reader, k, 0));
                }
            }
        } else {
            k = 1;
            while (k < this.reader.getXrefSize()) {
                obj = this.reader.getPdfObjectRelease(k);
                if (!(obj == null || skipInfo == k)) {
                    addToBody(obj, getNewObjectNumber(this.reader, k, 0), k != rootN);
                }
                k++;
            }
        }
        PdfIndirectReference encryption = null;
        if (this.crypto != null) {
            if (this.append) {
                encryption = this.reader.getCryptoRef();
            } else {
                encryption = addToBody(this.crypto.getEncryptionDictionary(), false).getIndirectReference();
            }
            fileID = this.crypto.getFileID(true);
        } else {
            PdfArray IDs = this.reader.trailer.getAsArray(PdfName.ID);
            if (IDs == null || IDs.getAsString(0) == null) {
                fileID = PdfEncryption.createInfoId(PdfEncryption.createDocumentId(), true);
            } else {
                fileID = PdfEncryption.createInfoId(IDs.getAsString(0).getBytes(), true);
            }
        }
        PdfIndirectReference root = new PdfIndirectReference(0, getNewObjectNumber(this.reader, ((PRIndirectReference) this.reader.trailer.get(PdfName.ROOT)).getNumber(), 0));
        this.body.writeCrossReferenceTable(this.os, root, info, encryption, fileID, this.prevxref);
        if (this.fullCompression) {
            writeKeyInfo(this.os);
            this.os.write(getISOBytes("startxref\n"));
            this.os.write(getISOBytes(String.valueOf(this.body.offset())));
            this.os.write(getISOBytes("\n%%EOF\n"));
        } else {
            new PdfWriter$PdfTrailer(this.body.size(), this.body.offset(), root, info, encryption, fileID, this.prevxref).toPdf(this, this.os);
        }
        this.os.flush();
        if (isCloseStream()) {
            this.os.close();
        }
        getCounter().written(this.os.getCounter());
    }

    void applyRotation(PdfDictionary pageN, ByteBuffer out) {
        if (this.rotateContents) {
            Rectangle page = this.reader.getPageSizeWithRotation(pageN);
            switch (page.getRotation()) {
                case 90:
                    out.append(PdfContents.ROTATE90);
                    out.append(page.getTop());
                    out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
                    return;
                case 180:
                    out.append(PdfContents.ROTATE180);
                    out.append(page.getRight());
                    out.append(' ');
                    out.append(page.getTop());
                    out.append(PdfContents.ROTATEFINAL);
                    return;
                case 270:
                    out.append(PdfContents.ROTATE270);
                    out.append('0').append(' ');
                    out.append(page.getRight());
                    out.append(PdfContents.ROTATEFINAL);
                    return;
                default:
                    return;
            }
        }
    }

    protected void alterContents() throws IOException {
        for (PageStamp ps : this.pagesToContent.values()) {
            PdfArray ar;
            PdfObject pageN = ps.pageN;
            markUsed(pageN);
            PdfObject content = PdfReader.getPdfObject(pageN.get(PdfName.CONTENTS), pageN);
            if (content == null) {
                ar = new PdfArray();
                pageN.put(PdfName.CONTENTS, ar);
            } else if (content.isArray()) {
                PdfObject ar2 = (PdfArray) content;
                markUsed(ar2);
            } else if (content.isStream()) {
                ar = new PdfArray();
                ar.add(pageN.get(PdfName.CONTENTS));
                pageN.put(PdfName.CONTENTS, ar);
            } else {
                ar = new PdfArray();
                pageN.put(PdfName.CONTENTS, ar);
            }
            ByteBuffer out = new ByteBuffer();
            if (ps.under != null) {
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageN, out);
                out.append(ps.under.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
            }
            if (ps.over != null) {
                out.append(PdfContents.SAVESTATE);
            }
            PdfStream stream = new PdfStream(out.toByteArray());
            stream.flateCompress(this.compressionLevel);
            ar.addFirst(addToBody(stream).getIndirectReference());
            out.reset();
            if (ps.over != null) {
                out.append(' ');
                out.append(PdfContents.RESTORESTATE);
                ByteBuffer buf = ps.over.getInternalBuffer();
                out.append(buf.getBuffer(), 0, ps.replacePoint);
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageN, out);
                out.append(buf.getBuffer(), ps.replacePoint, buf.size() - ps.replacePoint);
                out.append(PdfContents.RESTORESTATE);
                stream = new PdfStream(out.toByteArray());
                stream.flateCompress(this.compressionLevel);
                ar.add(addToBody(stream).getIndirectReference());
            }
            alterResources(ps);
        }
    }

    void alterResources(PageStamp ps) {
        ps.pageN.put(PdfName.RESOURCES, ps.pageResources.getResources());
    }

    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable ref = (IntHashtable) this.readers2intrefs.get(reader);
        int n;
        if (ref != null) {
            n = ref.get(number);
            if (n != 0) {
                return n;
            }
            n = getIndirectReferenceNumber();
            ref.put(number, n);
            return n;
        } else if (this.currentPdfReaderInstance != null) {
            return this.currentPdfReaderInstance.getNewObjectNumber(number, generation);
        } else {
            if (this.append && number < this.initialXrefSize) {
                return number;
            }
            n = this.myXref.get(number);
            if (n != 0) {
                return n;
            }
            n = getIndirectReferenceNumber();
            this.myXref.put(number, n);
            return n;
        }
    }

    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        if (this.readers2intrefs.containsKey(reader)) {
            RandomAccessFileOrArray raf = (RandomAccessFileOrArray) this.readers2file.get(reader);
            if (raf != null) {
                return raf;
            }
            return reader.getSafeFile();
        } else if (this.currentPdfReaderInstance == null) {
            return this.file;
        } else {
            return this.currentPdfReaderInstance.getReaderFile();
        }
    }

    public void registerReader(PdfReader reader, boolean openFile) throws IOException {
        if (!this.readers2intrefs.containsKey(reader)) {
            this.readers2intrefs.put(reader, new IntHashtable());
            if (openFile) {
                RandomAccessFileOrArray raf = reader.getSafeFile();
                this.readers2file.put(reader, raf);
                raf.reOpen();
            }
        }
    }

    public void unRegisterReader(PdfReader reader) {
        if (this.readers2intrefs.containsKey(reader)) {
            this.readers2intrefs.remove(reader);
            RandomAccessFileOrArray raf = (RandomAccessFileOrArray) this.readers2file.get(reader);
            if (raf != null) {
                this.readers2file.remove(reader);
                try {
                    raf.close();
                } catch (Exception e) {
                }
            }
        }
    }

    static void findAllObjects(PdfReader reader, PdfObject obj, IntHashtable hits) {
        if (obj != null) {
            switch (obj.type()) {
                case 5:
                    PdfArray a = (PdfArray) obj;
                    for (int k = 0; k < a.size(); k++) {
                        findAllObjects(reader, a.getPdfObject(k), hits);
                    }
                    return;
                case 6:
                case 7:
                    PdfDictionary dic = (PdfDictionary) obj;
                    for (PdfName name : dic.getKeys()) {
                        findAllObjects(reader, dic.get(name), hits);
                    }
                    return;
                case 10:
                    PRIndirectReference iref = (PRIndirectReference) obj;
                    if (reader == iref.getReader() && !hits.containsKey(iref.getNumber())) {
                        hits.put(iref.getNumber(), 1);
                        findAllObjects(reader, PdfReader.getPdfObject(obj), hits);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void addComments(FdfReader fdf) throws IOException {
        if (!this.readers2intrefs.containsKey(fdf)) {
            PdfDictionary catalog = fdf.getCatalog().getAsDict(PdfName.FDF);
            if (catalog != null) {
                PdfArray annots = catalog.getAsArray(PdfName.ANNOTS);
                if (annots != null && annots.size() != 0) {
                    int k;
                    PdfObject obj;
                    registerReader(fdf, false);
                    IntHashtable hits = new IntHashtable();
                    HashMap<String, PdfObject> irt = new HashMap();
                    ArrayList<PdfObject> an = new ArrayList();
                    for (k = 0; k < annots.size(); k++) {
                        obj = annots.getPdfObject(k);
                        PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject(obj);
                        PdfNumber page = annot.getAsNumber(PdfName.PAGE);
                        if (page != null && page.intValue() < this.reader.getNumberOfPages()) {
                            findAllObjects(fdf, obj, hits);
                            an.add(obj);
                            if (obj.type() == 10) {
                                PdfObject nm = PdfReader.getPdfObject(annot.get(PdfName.NM));
                                if (nm != null && nm.type() == 3) {
                                    irt.put(nm.toString(), obj);
                                }
                            }
                        }
                    }
                    int[] arhits = hits.getKeys();
                    for (int n : arhits) {
                        obj = fdf.getPdfObject(n);
                        if (obj.type() == 6) {
                            PdfObject str = PdfReader.getPdfObject(((PdfDictionary) obj).get(PdfName.IRT));
                            if (str != null && str.type() == 3) {
                                PdfObject i = (PdfObject) irt.get(str.toString());
                                if (i != null) {
                                    PdfObject dic2 = new PdfDictionary();
                                    dic2.merge((PdfDictionary) obj);
                                    dic2.put(PdfName.IRT, i);
                                    obj = dic2;
                                }
                            }
                        }
                        addToBody(obj, getNewObjectNumber(fdf, n, 0));
                    }
                    for (k = 0; k < an.size(); k++) {
                        obj = (PdfObject) an.get(k);
                        PdfObject dic = this.reader.getPageN(((PdfDictionary) PdfReader.getPdfObject(obj)).getAsNumber(PdfName.PAGE).intValue() + 1);
                        PdfObject annotsp = (PdfArray) PdfReader.getPdfObject(dic.get(PdfName.ANNOTS), dic);
                        if (annotsp == null) {
                            annotsp = new PdfArray();
                            dic.put(PdfName.ANNOTS, annotsp);
                            markUsed(dic);
                        }
                        markUsed(annotsp);
                        annotsp.add(obj);
                    }
                }
            }
        }
    }

    PageStamp getPageStamp(int pageNum) {
        PdfDictionary pageN = this.reader.getPageN(pageNum);
        PageStamp ps = (PageStamp) this.pagesToContent.get(pageN);
        if (ps != null) {
            return ps;
        }
        ps = new PageStamp(this, this.reader, pageN);
        this.pagesToContent.put(pageN, ps);
        return ps;
    }

    PdfContentByte getUnderContent(int pageNum) {
        if (pageNum < 1 || pageNum > this.reader.getNumberOfPages()) {
            return null;
        }
        PageStamp ps = getPageStamp(pageNum);
        if (ps.under == null) {
            ps.under = new StampContent(this, ps);
        }
        return ps.under;
    }

    PdfContentByte getOverContent(int pageNum) {
        if (pageNum < 1 || pageNum > this.reader.getNumberOfPages()) {
            return null;
        }
        PageStamp ps = getPageStamp(pageNum);
        if (ps.over == null) {
            ps.over = new StampContent(this, ps);
        }
        return ps.over;
    }

    void correctAcroFieldPages(int page) {
        if (this.acroFields != null && page <= this.reader.getNumberOfPages()) {
            for (Item item : this.acroFields.getFields().values()) {
                for (int k = 0; k < item.size(); k++) {
                    int p = item.getPage(k).intValue();
                    if (p >= page) {
                        item.forcePage(k, p + 1);
                    }
                }
            }
        }
    }

    private static void moveRectangle(PdfDictionary dic2, PdfReader r, int pageImported, PdfName key, String name) {
        Rectangle m = r.getBoxSize(pageImported, name);
        if (m == null) {
            dic2.remove(key);
        } else {
            dic2.put(key, new PdfRectangle(m));
        }
    }

    void replacePage(PdfReader r, int pageImported, int pageReplaced) {
        PdfDictionary pageN = this.reader.getPageN(pageReplaced);
        if (this.pagesToContent.containsKey(pageN)) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("this.page.cannot.be.replaced.new.content.was.already.added", new Object[0]));
        }
        PdfTemplate p = getImportedPage(r, pageImported);
        PdfDictionary dic2 = this.reader.getPageNRelease(pageReplaced);
        dic2.remove(PdfName.RESOURCES);
        dic2.remove(PdfName.CONTENTS);
        moveRectangle(dic2, r, pageImported, PdfName.MEDIABOX, "media");
        moveRectangle(dic2, r, pageImported, PdfName.CROPBOX, "crop");
        moveRectangle(dic2, r, pageImported, PdfName.TRIMBOX, "trim");
        moveRectangle(dic2, r, pageImported, PdfName.ARTBOX, "art");
        moveRectangle(dic2, r, pageImported, PdfName.BLEEDBOX, "bleed");
        dic2.put(PdfName.ROTATE, new PdfNumber(r.getPageRotation(pageImported)));
        getOverContent(pageReplaced).addTemplate(p, 0.0f, 0.0f);
        PageStamp ps = (PageStamp) this.pagesToContent.get(pageN);
        ps.replacePoint = ps.over.getInternalBuffer().size();
    }

    void insertPage(int pageNumber, Rectangle mediabox) {
        PRIndirectReference parentRef;
        Rectangle media = new Rectangle(mediabox);
        int rotation = media.getRotation() % dc1394.DC1394_COLOR_CODING_RGB16S;
        PdfDictionary page = new PdfDictionary(PdfName.PAGE);
        page.put(PdfName.RESOURCES, new PdfDictionary());
        page.put(PdfName.ROTATE, new PdfNumber(rotation));
        page.put(PdfName.MEDIABOX, new PdfRectangle(media, rotation));
        PdfObject pref = this.reader.addPdfObject(page);
        PdfObject pRIndirectReference;
        PdfObject kids;
        if (pageNumber > this.reader.getNumberOfPages()) {
            parentRef = (PRIndirectReference) this.reader.getPageNRelease(this.reader.getNumberOfPages()).get(PdfName.PARENT);
            pRIndirectReference = new PRIndirectReference(this.reader, parentRef.getNumber());
            PdfObject parent = (PdfDictionary) PdfReader.getPdfObject(pRIndirectReference);
            kids = (PdfArray) PdfReader.getPdfObject(parent.get(PdfName.KIDS), parent);
            kids.add(pref);
            markUsed(kids);
            this.reader.pageRefs.insertPage(pageNumber, pref);
            parentRef = pRIndirectReference;
        } else {
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            PdfDictionary firstPage = this.reader.getPageN(pageNumber);
            PRIndirectReference firstPageRef = this.reader.getPageOrigRef(pageNumber);
            this.reader.releasePage(pageNumber);
            parentRef = (PRIndirectReference) firstPage.get(PdfName.PARENT);
            pRIndirectReference = new PRIndirectReference(this.reader, parentRef.getNumber());
            PdfDictionary parent2 = (PdfDictionary) PdfReader.getPdfObject(pRIndirectReference);
            kids = (PdfArray) PdfReader.getPdfObject(parent2.get(PdfName.KIDS), parent2);
            int len = kids.size();
            int num = firstPageRef.getNumber();
            for (int k = 0; k < len; k++) {
                if (num == ((PRIndirectReference) kids.getPdfObject(k)).getNumber()) {
                    kids.add(k, pref);
                    break;
                }
            }
            if (len == kids.size()) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("internal.inconsistence", new Object[0]));
            }
            markUsed(kids);
            this.reader.pageRefs.insertPage(pageNumber, pref);
            correctAcroFieldPages(pageNumber);
            PdfObject parentRef2 = pRIndirectReference;
        }
        page.put(PdfName.PARENT, parentRef);
        for (parent = 
/*
Method generation error in method: com.itextpdf.text.pdf.PdfStamperImp.insertPage(int, com.itextpdf.text.Rectangle):void
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r14_2 'parent' com.itextpdf.text.pdf.PdfObject) = (r14_1 'parent' com.itextpdf.text.pdf.PdfObject), (r14_6 'parent2' com.itextpdf.text.pdf.PdfDictionary) binds: {(r14_1 'parent' com.itextpdf.text.pdf.PdfObject)=B:2:0x0068, (r14_6 'parent2' com.itextpdf.text.pdf.PdfDictionary)=B:19:0x01b0} in method: com.itextpdf.text.pdf.PdfStamperImp.insertPage(int, com.itextpdf.text.Rectangle):void
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:328)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:265)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:228)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:83)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:530)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:514)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 15 more

*/

        boolean isRotateContents() {
            return this.rotateContents;
        }

        void setRotateContents(boolean rotateContents) {
            this.rotateContents = rotateContents;
        }

        boolean isContentWritten() {
            return this.body.size() > 1;
        }

        AcroFields getAcroFields() {
            if (this.acroFields == null) {
                this.acroFields = new AcroFields(this.reader, this);
            }
            return this.acroFields;
        }

        void setFormFlattening(boolean flat) {
            this.flat = flat;
        }

        void setFreeTextFlattening(boolean flat) {
            this.flatFreeText = flat;
        }

        boolean partialFormFlattening(String name) {
            getAcroFields();
            if (this.acroFields.getXfa().isXfaPresent()) {
                throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("partial.form.flattening.is.not.supported.with.xfa.forms", new Object[0]));
            } else if (!this.acroFields.getFields().containsKey(name)) {
                return false;
            } else {
                this.partialFlattening.add(name);
                return true;
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void flatFields() {
            /*
            r54 = this;
            r0 = r54;
            r0 = r0.append;
            r51 = r0;
            if (r51 == 0) goto L_0x001c;
        L_0x0008:
            r51 = new java.lang.IllegalArgumentException;
            r52 = "field.flattening.is.not.supported.in.append.mode";
            r53 = 0;
            r0 = r53;
            r0 = new java.lang.Object[r0];
            r53 = r0;
            r52 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r52, r53);
            r51.<init>(r52);
            throw r51;
        L_0x001c:
            r54.getAcroFields();
            r0 = r54;
            r0 = r0.acroFields;
            r51 = r0;
            r19 = r51.getFields();
            r0 = r54;
            r0 = r0.fieldsAdded;
            r51 = r0;
            if (r51 == 0) goto L_0x005f;
        L_0x0031:
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r51 = r51.isEmpty();
            if (r51 == 0) goto L_0x005f;
        L_0x003d:
            r51 = r19.keySet();
            r25 = r51.iterator();
        L_0x0045:
            r51 = r25.hasNext();
            if (r51 == 0) goto L_0x005f;
        L_0x004b:
            r45 = r25.next();
            r45 = (java.lang.String) r45;
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r0 = r51;
            r1 = r45;
            r0.add(r1);
            goto L_0x0045;
        L_0x005f:
            r0 = r54;
            r0 = r0.reader;
            r51 = r0;
            r51 = r51.getCatalog();
            r52 = com.itextpdf.text.pdf.PdfName.ACROFORM;
            r4 = r51.getAsDict(r52);
            r3 = 0;
            if (r4 == 0) goto L_0x0082;
        L_0x0072:
            r51 = com.itextpdf.text.pdf.PdfName.FIELDS;
            r0 = r51;
            r51 = r4.get(r0);
            r0 = r51;
            r3 = com.itextpdf.text.pdf.PdfReader.getPdfObject(r0, r4);
            r3 = (com.itextpdf.text.pdf.PdfArray) r3;
        L_0x0082:
            r51 = r19.entrySet();
            r25 = r51.iterator();
        L_0x008a:
            r51 = r25.hasNext();
            if (r51 == 0) goto L_0x0527;
        L_0x0090:
            r17 = r25.next();
            r17 = (java.util.Map.Entry) r17;
            r33 = r17.getKey();
            r33 = (java.lang.String) r33;
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r51 = r51.isEmpty();
            if (r51 != 0) goto L_0x00b8;
        L_0x00a8:
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r0 = r51;
            r1 = r33;
            r51 = r0.contains(r1);
            if (r51 == 0) goto L_0x008a;
        L_0x00b8:
            r29 = r17.getValue();
            r29 = (com.itextpdf.text.pdf.AcroFields.Item) r29;
            r30 = 0;
        L_0x00c0:
            r51 = r29.size();
            r0 = r30;
            r1 = r51;
            if (r0 >= r1) goto L_0x008a;
        L_0x00ca:
            r32 = r29.getMerged(r30);
            r51 = com.itextpdf.text.pdf.PdfName.f122F;
            r0 = r32;
            r1 = r51;
            r18 = r0.getAsNumber(r1);
            r20 = 0;
            if (r18 == 0) goto L_0x00e0;
        L_0x00dc:
            r20 = r18.intValue();
        L_0x00e0:
            r51 = r29.getPage(r30);
            r36 = r51.intValue();
            r51 = 1;
            r0 = r36;
            r1 = r51;
            if (r0 >= r1) goto L_0x00f3;
        L_0x00f0:
            r30 = r30 + 1;
            goto L_0x00c0;
        L_0x00f3:
            r51 = com.itextpdf.text.pdf.PdfName.AP;
            r0 = r32;
            r1 = r51;
            r8 = r0.getAsDict(r1);
            r11 = 0;
            if (r8 == 0) goto L_0x0112;
        L_0x0100:
            r51 = com.itextpdf.text.pdf.PdfName.f128N;
            r0 = r51;
            r11 = r8.getAsStream(r0);
            if (r11 != 0) goto L_0x0112;
        L_0x010a:
            r51 = com.itextpdf.text.pdf.PdfName.f128N;
            r0 = r51;
            r11 = r8.getAsDict(r0);
        L_0x0112:
            r0 = r54;
            r0 = r0.acroFields;
            r51 = r0;
            r51 = r51.isGenerateAppearances();
            if (r51 == 0) goto L_0x02f6;
        L_0x011e:
            if (r8 == 0) goto L_0x0122;
        L_0x0120:
            if (r11 != 0) goto L_0x01f7;
        L_0x0122:
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r51 = r0;
            r0 = r51;
            r1 = r33;
            r0.regenerateField(r1);	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r51 = r0;
            r0 = r51;
            r1 = r33;
            r51 = r0.getFieldItem(r1);	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r0 = r51;
            r1 = r30;
            r51 = r0.getMerged(r1);	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r52 = com.itextpdf.text.pdf.PdfName.AP;	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
            r8 = r51.getAsDict(r52);	 Catch:{ IOException -> 0x05d5, DocumentException -> 0x05d2 }
        L_0x014b:
            if (r8 == 0) goto L_0x01b6;
        L_0x014d:
            r51 = r20 & 4;
            if (r51 == 0) goto L_0x01b6;
        L_0x0151:
            r51 = r20 & 2;
            if (r51 != 0) goto L_0x01b6;
        L_0x0155:
            r51 = com.itextpdf.text.pdf.PdfName.f128N;
            r0 = r51;
            r34 = r8.get(r0);
            r7 = 0;
            if (r34 == 0) goto L_0x017b;
        L_0x0160:
            r35 = com.itextpdf.text.pdf.PdfReader.getPdfObject(r34);
            r0 = r34;
            r0 = r0 instanceof com.itextpdf.text.pdf.PdfIndirectReference;
            r51 = r0;
            if (r51 == 0) goto L_0x03db;
        L_0x016c:
            r51 = r34.isIndirect();
            if (r51 != 0) goto L_0x03db;
        L_0x0172:
            r7 = new com.itextpdf.text.pdf.PdfAppearance;
            r34 = (com.itextpdf.text.pdf.PdfIndirectReference) r34;
            r0 = r34;
            r7.<init>(r0);
        L_0x017b:
            if (r7 == 0) goto L_0x01b6;
        L_0x017d:
            r51 = com.itextpdf.text.pdf.PdfName.RECT;
            r0 = r32;
            r1 = r51;
            r51 = r0.getAsArray(r1);
            r15 = com.itextpdf.text.pdf.PdfReader.getNormalizedRectangle(r51);
            r0 = r54;
            r1 = r36;
            r16 = r0.getOverContent(r1);
            r51 = "Q ";
            r0 = r16;
            r1 = r51;
            r0.setLiteral(r1);
            r51 = r15.getLeft();
            r52 = r15.getBottom();
            r0 = r16;
            r1 = r51;
            r2 = r52;
            r0.addTemplate(r7, r1, r2);
            r51 = "q ";
            r0 = r16;
            r1 = r51;
            r0.setLiteral(r1);
        L_0x01b6:
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r51 = r51.isEmpty();
            if (r51 != 0) goto L_0x00f0;
        L_0x01c2:
            r0 = r54;
            r0 = r0.reader;
            r51 = r0;
            r0 = r51;
            r1 = r36;
            r37 = r0.getPageN(r1);
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r6 = r0.getAsArray(r1);
            if (r6 == 0) goto L_0x00f0;
        L_0x01dc:
            r26 = 0;
        L_0x01de:
            r51 = r6.size();
            r0 = r26;
            r1 = r51;
            if (r0 >= r1) goto L_0x0509;
        L_0x01e8:
            r0 = r26;
            r40 = r6.getPdfObject(r0);
            r51 = r40.isIndirect();
            if (r51 != 0) goto L_0x043f;
        L_0x01f4:
            r26 = r26 + 1;
            goto L_0x01de;
        L_0x01f7:
            r51 = r11.isStream();
            if (r51 == 0) goto L_0x014b;
        L_0x01fd:
            r46 = r11;
            r46 = (com.itextpdf.text.pdf.PdfStream) r46;
            r51 = com.itextpdf.text.pdf.PdfName.BBOX;
            r0 = r46;
            r1 = r51;
            r12 = r0.getAsArray(r1);
            r51 = com.itextpdf.text.pdf.PdfName.RECT;
            r0 = r32;
            r1 = r51;
            r42 = r0.getAsArray(r1);
            if (r12 == 0) goto L_0x014b;
        L_0x0217:
            if (r42 == 0) goto L_0x014b;
        L_0x0219:
            r51 = 2;
            r0 = r42;
            r1 = r51;
            r51 = r0.getAsNumber(r1);
            r51 = r51.floatValue();
            r52 = 0;
            r0 = r42;
            r1 = r52;
            r52 = r0.getAsNumber(r1);
            r52 = r52.floatValue();
            r44 = r51 - r52;
            r51 = 2;
            r0 = r51;
            r51 = r12.getAsNumber(r0);
            r51 = r51.floatValue();
            r52 = 0;
            r0 = r52;
            r52 = r12.getAsNumber(r0);
            r52 = r52.floatValue();
            r14 = r51 - r52;
            r51 = 3;
            r0 = r42;
            r1 = r51;
            r51 = r0.getAsNumber(r1);
            r51 = r51.floatValue();
            r52 = 1;
            r0 = r42;
            r1 = r52;
            r52 = r0.getAsNumber(r1);
            r52 = r52.floatValue();
            r43 = r51 - r52;
            r51 = 3;
            r0 = r51;
            r51 = r12.getAsNumber(r0);
            r51 = r51.floatValue();
            r52 = 1;
            r0 = r52;
            r52 = r12.getAsNumber(r0);
            r52 = r52.floatValue();
            r13 = r51 - r52;
            r51 = 0;
            r51 = (r14 > r51 ? 1 : (r14 == r51 ? 0 : -1));
            if (r51 == 0) goto L_0x02ee;
        L_0x028f:
            r51 = r44 / r14;
        L_0x0291:
            r49 = java.lang.Math.abs(r51);
            r51 = 0;
            r51 = (r13 > r51 ? 1 : (r13 == r51 ? 0 : -1));
            if (r51 == 0) goto L_0x02f2;
        L_0x029b:
            r51 = r43 / r13;
        L_0x029d:
            r23 = java.lang.Math.abs(r51);
            r51 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r51 = (r49 > r51 ? 1 : (r49 == r51 ? 0 : -1));
            if (r51 != 0) goto L_0x02ad;
        L_0x02a7:
            r51 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r51 = (r23 > r51 ? 1 : (r23 == r51 ? 0 : -1));
            if (r51 == 0) goto L_0x014b;
        L_0x02ad:
            r9 = new com.itextpdf.text.pdf.NumberArray;
            r51 = 6;
            r0 = r51;
            r0 = new float[r0];
            r51 = r0;
            r52 = 0;
            r51[r52] = r49;
            r52 = 1;
            r53 = 0;
            r51[r52] = r53;
            r52 = 2;
            r53 = 0;
            r51[r52] = r53;
            r52 = 3;
            r51[r52] = r23;
            r52 = 4;
            r53 = 0;
            r51[r52] = r53;
            r52 = 5;
            r53 = 0;
            r51[r52] = r53;
            r0 = r51;
            r9.<init>(r0);
            r51 = com.itextpdf.text.pdf.PdfName.MATRIX;
            r0 = r46;
            r1 = r51;
            r0.put(r1, r9);
            r0 = r54;
            r1 = r46;
            r0.markUsed(r1);
            goto L_0x014b;
        L_0x02ee:
            r51 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
            goto L_0x0291;
        L_0x02f2:
            r51 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
            goto L_0x029d;
        L_0x02f6:
            if (r8 == 0) goto L_0x014b;
        L_0x02f8:
            if (r11 == 0) goto L_0x014b;
        L_0x02fa:
            r11 = (com.itextpdf.text.pdf.PdfDictionary) r11;
            r51 = com.itextpdf.text.pdf.PdfName.BBOX;
            r0 = r51;
            r12 = r11.getAsArray(r0);
            r51 = com.itextpdf.text.pdf.PdfName.RECT;
            r0 = r32;
            r1 = r51;
            r42 = r0.getAsArray(r1);
            if (r12 == 0) goto L_0x014b;
        L_0x0310:
            if (r42 == 0) goto L_0x014b;
        L_0x0312:
            r51 = 2;
            r0 = r51;
            r51 = r12.getAsNumber(r0);
            r51 = r51.floatValue();
            r52 = 0;
            r0 = r52;
            r52 = r12.getAsNumber(r0);
            r52 = r52.floatValue();
            r51 = r51 - r52;
            r52 = 2;
            r0 = r42;
            r1 = r52;
            r52 = r0.getAsNumber(r1);
            r52 = r52.floatValue();
            r53 = 0;
            r0 = r42;
            r1 = r53;
            r53 = r0.getAsNumber(r1);
            r53 = r53.floatValue();
            r52 = r52 - r53;
            r50 = r51 - r52;
            r51 = 3;
            r0 = r51;
            r51 = r12.getAsNumber(r0);
            r51 = r51.floatValue();
            r52 = 1;
            r0 = r52;
            r52 = r12.getAsNumber(r0);
            r52 = r52.floatValue();
            r51 = r51 - r52;
            r52 = 3;
            r0 = r42;
            r1 = r52;
            r52 = r0.getAsNumber(r1);
            r52 = r52.floatValue();
            r53 = 1;
            r0 = r42;
            r1 = r53;
            r53 = r0.getAsNumber(r1);
            r53 = r53.floatValue();
            r52 = r52 - r53;
            r24 = r51 - r52;
            r51 = java.lang.Math.abs(r50);
            r52 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r51 = (r51 > r52 ? 1 : (r51 == r52 ? 0 : -1));
            if (r51 > 0) goto L_0x039a;
        L_0x0390:
            r51 = java.lang.Math.abs(r24);
            r52 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r51 = (r51 > r52 ? 1 : (r51 == r52 ? 0 : -1));
            if (r51 <= 0) goto L_0x014b;
        L_0x039a:
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r51 = r0;
            r52 = 1;
            r51.setGenerateAppearances(r52);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r51 = r0;
            r0 = r51;
            r1 = r33;
            r0.regenerateField(r1);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r51 = r0;
            r52 = 0;
            r51.setGenerateAppearances(r52);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r0 = r54;
            r0 = r0.acroFields;	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r51 = r0;
            r0 = r51;
            r1 = r33;
            r51 = r0.getFieldItem(r1);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r0 = r51;
            r1 = r30;
            r51 = r0.getMerged(r1);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r52 = com.itextpdf.text.pdf.PdfName.AP;	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            r8 = r51.getAsDict(r52);	 Catch:{ IOException -> 0x05cf, DocumentException -> 0x05cc }
            goto L_0x014b;
        L_0x03db:
            r0 = r35;
            r0 = r0 instanceof com.itextpdf.text.pdf.PdfStream;
            r51 = r0;
            if (r51 == 0) goto L_0x03fd;
        L_0x03e3:
            r35 = (com.itextpdf.text.pdf.PdfDictionary) r35;
            r51 = com.itextpdf.text.pdf.PdfName.SUBTYPE;
            r52 = com.itextpdf.text.pdf.PdfName.FORM;
            r0 = r35;
            r1 = r51;
            r2 = r52;
            r0.put(r1, r2);
            r7 = new com.itextpdf.text.pdf.PdfAppearance;
            r34 = (com.itextpdf.text.pdf.PdfIndirectReference) r34;
            r0 = r34;
            r7.<init>(r0);
            goto L_0x017b;
        L_0x03fd:
            if (r35 == 0) goto L_0x017b;
        L_0x03ff:
            r51 = r35.isDictionary();
            if (r51 == 0) goto L_0x017b;
        L_0x0405:
            r51 = com.itextpdf.text.pdf.PdfName.AS;
            r0 = r32;
            r1 = r51;
            r10 = r0.getAsName(r1);
            if (r10 == 0) goto L_0x017b;
        L_0x0411:
            r35 = (com.itextpdf.text.pdf.PdfDictionary) r35;
            r0 = r35;
            r28 = r0.get(r10);
            r28 = (com.itextpdf.text.pdf.PdfIndirectReference) r28;
            if (r28 == 0) goto L_0x017b;
        L_0x041d:
            r7 = new com.itextpdf.text.pdf.PdfAppearance;
            r0 = r28;
            r7.<init>(r0);
            r51 = r28.isIndirect();
            if (r51 == 0) goto L_0x017b;
        L_0x042a:
            r35 = com.itextpdf.text.pdf.PdfReader.getPdfObject(r28);
            r35 = (com.itextpdf.text.pdf.PdfDictionary) r35;
            r51 = com.itextpdf.text.pdf.PdfName.SUBTYPE;
            r52 = com.itextpdf.text.pdf.PdfName.FORM;
            r0 = r35;
            r1 = r51;
            r2 = r52;
            r0.put(r1, r2);
            goto L_0x017b;
        L_0x043f:
            r41 = r29.getWidgetRef(r30);
            r51 = r41.isIndirect();
            if (r51 == 0) goto L_0x01f4;
        L_0x0449:
            r40 = (com.itextpdf.text.pdf.PRIndirectReference) r40;
            r52 = r40.getNumber();
            r51 = r41;
            r51 = (com.itextpdf.text.pdf.PRIndirectReference) r51;
            r51 = r51.getNumber();
            r0 = r52;
            r1 = r51;
            if (r0 != r1) goto L_0x01f4;
        L_0x045d:
            r27 = r26 + -1;
            r0 = r26;
            r6.remove(r0);
            r48 = r41;
            r48 = (com.itextpdf.text.pdf.PRIndirectReference) r48;
        L_0x0468:
            r47 = com.itextpdf.text.pdf.PdfReader.getPdfObject(r48);
            r47 = (com.itextpdf.text.pdf.PdfDictionary) r47;
            r51 = com.itextpdf.text.pdf.PdfName.PARENT;
            r0 = r47;
            r1 = r51;
            r39 = r0.get(r1);
            r39 = (com.itextpdf.text.pdf.PRIndirectReference) r39;
            com.itextpdf.text.pdf.PdfReader.killIndirect(r48);
            if (r39 != 0) goto L_0x04b5;
        L_0x047f:
            r21 = 0;
        L_0x0481:
            r51 = r3.size();
            r0 = r21;
            r1 = r51;
            if (r0 >= r1) goto L_0x04b1;
        L_0x048b:
            r0 = r21;
            r22 = r3.getPdfObject(r0);
            r51 = r22.isIndirect();
            if (r51 == 0) goto L_0x04ae;
        L_0x0497:
            r22 = (com.itextpdf.text.pdf.PRIndirectReference) r22;
            r51 = r22.getNumber();
            r52 = r48.getNumber();
            r0 = r51;
            r1 = r52;
            if (r0 != r1) goto L_0x04ae;
        L_0x04a7:
            r0 = r21;
            r3.remove(r0);
            r21 = r21 + -1;
        L_0x04ae:
            r21 = r21 + 1;
            goto L_0x0481;
        L_0x04b1:
            r26 = r27;
            goto L_0x01f4;
        L_0x04b5:
            r38 = com.itextpdf.text.pdf.PdfReader.getPdfObject(r39);
            r38 = (com.itextpdf.text.pdf.PdfDictionary) r38;
            r51 = com.itextpdf.text.pdf.PdfName.KIDS;
            r0 = r38;
            r1 = r51;
            r31 = r0.getAsArray(r1);
            r21 = 0;
        L_0x04c7:
            r51 = r31.size();
            r0 = r21;
            r1 = r51;
            if (r0 >= r1) goto L_0x04fb;
        L_0x04d1:
            r0 = r31;
            r1 = r21;
            r22 = r0.getPdfObject(r1);
            r51 = r22.isIndirect();
            if (r51 == 0) goto L_0x04f8;
        L_0x04df:
            r22 = (com.itextpdf.text.pdf.PRIndirectReference) r22;
            r51 = r22.getNumber();
            r52 = r48.getNumber();
            r0 = r51;
            r1 = r52;
            if (r0 != r1) goto L_0x04f8;
        L_0x04ef:
            r0 = r31;
            r1 = r21;
            r0.remove(r1);
            r21 = r21 + -1;
        L_0x04f8:
            r21 = r21 + 1;
            goto L_0x04c7;
        L_0x04fb:
            r51 = r31.isEmpty();
            if (r51 != 0) goto L_0x0505;
        L_0x0501:
            r26 = r27;
            goto L_0x01f4;
        L_0x0505:
            r48 = r39;
            goto L_0x0468;
        L_0x0509:
            r51 = r6.isEmpty();
            if (r51 == 0) goto L_0x00f0;
        L_0x050f:
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r51 = r0.get(r1);
            com.itextpdf.text.pdf.PdfReader.killIndirect(r51);
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r0.remove(r1);
            goto L_0x00f0;
        L_0x0527:
            r0 = r54;
            r0 = r0.fieldsAdded;
            r51 = r0;
            if (r51 != 0) goto L_0x05cb;
        L_0x052f:
            r0 = r54;
            r0 = r0.partialFlattening;
            r51 = r0;
            r51 = r51.isEmpty();
            if (r51 == 0) goto L_0x05cb;
        L_0x053b:
            r36 = 1;
        L_0x053d:
            r0 = r54;
            r0 = r0.reader;
            r51 = r0;
            r51 = r51.getNumberOfPages();
            r0 = r36;
            r1 = r51;
            if (r0 > r1) goto L_0x05c8;
        L_0x054d:
            r0 = r54;
            r0 = r0.reader;
            r51 = r0;
            r0 = r51;
            r1 = r36;
            r37 = r0.getPageN(r1);
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r6 = r0.getAsArray(r1);
            if (r6 != 0) goto L_0x056a;
        L_0x0567:
            r36 = r36 + 1;
            goto L_0x053d;
        L_0x056a:
            r26 = 0;
        L_0x056c:
            r51 = r6.size();
            r0 = r26;
            r1 = r51;
            if (r0 >= r1) goto L_0x05ab;
        L_0x0576:
            r0 = r26;
            r5 = r6.getDirectObject(r0);
            r0 = r5 instanceof com.itextpdf.text.pdf.PdfIndirectReference;
            r51 = r0;
            if (r51 == 0) goto L_0x058b;
        L_0x0582:
            r51 = r5.isIndirect();
            if (r51 != 0) goto L_0x058b;
        L_0x0588:
            r26 = r26 + 1;
            goto L_0x056c;
        L_0x058b:
            r51 = r5.isDictionary();
            if (r51 == 0) goto L_0x05a3;
        L_0x0591:
            r51 = com.itextpdf.text.pdf.PdfName.WIDGET;
            r5 = (com.itextpdf.text.pdf.PdfDictionary) r5;
            r52 = com.itextpdf.text.pdf.PdfName.SUBTYPE;
            r0 = r52;
            r52 = r5.get(r0);
            r51 = r51.equals(r52);
            if (r51 == 0) goto L_0x0588;
        L_0x05a3:
            r0 = r26;
            r6.remove(r0);
            r26 = r26 + -1;
            goto L_0x0588;
        L_0x05ab:
            r51 = r6.isEmpty();
            if (r51 == 0) goto L_0x0567;
        L_0x05b1:
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r51 = r0.get(r1);
            com.itextpdf.text.pdf.PdfReader.killIndirect(r51);
            r51 = com.itextpdf.text.pdf.PdfName.ANNOTS;
            r0 = r37;
            r1 = r51;
            r0.remove(r1);
            goto L_0x0567;
        L_0x05c8:
            r54.eliminateAcroformObjects();
        L_0x05cb:
            return;
        L_0x05cc:
            r51 = move-exception;
            goto L_0x014b;
        L_0x05cf:
            r51 = move-exception;
            goto L_0x014b;
        L_0x05d2:
            r51 = move-exception;
            goto L_0x014b;
        L_0x05d5:
            r51 = move-exception;
            goto L_0x014b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.PdfStamperImp.flatFields():void");
        }

        void eliminateAcroformObjects() {
            PdfObject acro = this.reader.getCatalog().get(PdfName.ACROFORM);
            if (acro != null) {
                PdfDictionary acrodic = (PdfDictionary) PdfReader.getPdfObject(acro);
                this.reader.killXref(acrodic.get(PdfName.XFA));
                acrodic.remove(PdfName.XFA);
                PdfObject iFields = acrodic.get(PdfName.FIELDS);
                if (iFields != null) {
                    PdfDictionary kids = new PdfDictionary();
                    kids.put(PdfName.KIDS, iFields);
                    sweepKids(kids);
                    PdfReader.killIndirect(iFields);
                    acrodic.put(PdfName.FIELDS, new PdfArray());
                }
                acrodic.remove(PdfName.SIGFLAGS);
                acrodic.remove(PdfName.NEEDAPPEARANCES);
                acrodic.remove(PdfName.DR);
            }
        }

        void sweepKids(PdfObject obj) {
            PdfObject oo = PdfReader.killIndirect(obj);
            if (oo != null && oo.isDictionary()) {
                PdfArray kids = (PdfArray) PdfReader.killIndirect(((PdfDictionary) oo).get(PdfName.KIDS));
                if (kids != null) {
                    for (int k = 0; k < kids.size(); k++) {
                        sweepKids(kids.getPdfObject(k));
                    }
                }
            }
        }

        public void setFlatAnnotations(boolean flatAnnotations) {
            this.flatannotations = flatAnnotations;
        }

        protected void flattenAnnotations() {
            flattenAnnotations(false);
        }

        private void flattenAnnotations(boolean flattenFreeTextAnnotations) {
            if (!this.append) {
                for (int page = 1; page <= this.reader.getNumberOfPages(); page++) {
                    PdfDictionary pageDic = this.reader.getPageN(page);
                    PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
                    if (annots != null) {
                        int idx = 0;
                        while (idx < annots.size()) {
                            PdfObject annoto = annots.getDirectObject(idx);
                            if ((!(annoto instanceof PdfIndirectReference) || annoto.isIndirect()) && (annoto instanceof PdfDictionary)) {
                                PdfDictionary annDic = (PdfDictionary) annoto;
                                if (flattenFreeTextAnnotations) {
                                    if (!annDic.get(PdfName.SUBTYPE).equals(PdfName.FREETEXT)) {
                                    }
                                } else if (annDic.get(PdfName.SUBTYPE).equals(PdfName.WIDGET)) {
                                }
                                PdfNumber ff = annDic.getAsNumber(PdfName.f122F);
                                int flags = ff != null ? ff.intValue() : 0;
                                if ((flags & 4) != 0 && (flags & 2) == 0) {
                                    PdfObject obj1 = annDic.get(PdfName.AP);
                                    if (obj1 != null) {
                                        PdfDictionary appDic = obj1 instanceof PdfIndirectReference ? (PdfDictionary) PdfReader.getPdfObject(obj1) : (PdfDictionary) obj1;
                                        PdfObject obj = appDic.get(PdfName.f128N);
                                        PdfTemplate app = null;
                                        PdfObject objReal = PdfReader.getPdfObject(obj);
                                        if ((obj instanceof PdfIndirectReference) && !obj.isIndirect()) {
                                            app = new PdfAppearance((PdfIndirectReference) obj);
                                        } else if (objReal instanceof PdfStream) {
                                            ((PdfDictionary) objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                                            app = new PdfAppearance((PdfIndirectReference) obj);
                                        } else if (objReal.isDictionary()) {
                                            PdfName as_p = appDic.getAsName(PdfName.AS);
                                            if (as_p != null) {
                                                PdfObject iref = (PdfIndirectReference) ((PdfDictionary) objReal).get(as_p);
                                                if (iref != null) {
                                                    app = new PdfAppearance((PdfIndirectReference) iref);
                                                    if (iref.isIndirect()) {
                                                        ((PdfDictionary) PdfReader.getPdfObject(iref)).put(PdfName.SUBTYPE, PdfName.FORM);
                                                    }
                                                }
                                            }
                                        }
                                        if (app != null) {
                                            Rectangle box = PdfReader.getNormalizedRectangle(annDic.getAsArray(PdfName.RECT));
                                            PdfContentByte cb = getOverContent(page);
                                            cb.setLiteral("Q ");
                                            cb.addTemplate(app, box.getLeft(), box.getBottom());
                                            cb.setLiteral("q ");
                                            annots.remove(idx);
                                            idx--;
                                        }
                                    }
                                }
                            }
                            idx++;
                        }
                        if (annots.isEmpty()) {
                            PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
                            pageDic.remove(PdfName.ANNOTS);
                        }
                    }
                }
            } else if (flattenFreeTextAnnotations) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("freetext.flattening.is.not.supported.in.append.mode", new Object[0]));
            } else {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("annotation.flattening.is.not.supported.in.append.mode", new Object[0]));
            }
        }

        protected void flatFreeTextFields() {
            flattenAnnotations(true);
        }

        public PdfIndirectReference getPageReference(int page) {
            PdfIndirectReference ref = this.reader.getPageOrigRef(page);
            if (ref != null) {
                return ref;
            }
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));
        }

        public void addAnnotation(PdfAnnotation annot) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("unsupported.in.this.context.use.pdfstamper.addannotation", new Object[0]));
        }

        void addDocumentField(PdfIndirectReference ref) {
            PdfObject catalog = this.reader.getCatalog();
            PdfObject acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
            if (acroForm == null) {
                acroForm = new PdfDictionary();
                catalog.put(PdfName.ACROFORM, acroForm);
                markUsed(catalog);
            }
            PdfObject fields = (PdfArray) PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
            if (fields == null) {
                fields = new PdfArray();
                acroForm.put(PdfName.FIELDS, fields);
                markUsed(acroForm);
            }
            if (!acroForm.contains(PdfName.DA)) {
                acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
                markUsed(acroForm);
            }
            fields.add(ref);
            markUsed(fields);
        }

        protected void addFieldResources() throws IOException {
            if (!this.fieldTemplates.isEmpty()) {
                PdfDictionary dic;
                PdfObject catalog = this.reader.getCatalog();
                PdfObject acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
                if (acroForm == null) {
                    acroForm = new PdfDictionary();
                    catalog.put(PdfName.ACROFORM, acroForm);
                    markUsed(catalog);
                }
                PdfObject dr = (PdfDictionary) PdfReader.getPdfObject(acroForm.get(PdfName.DR), acroForm);
                if (dr == null) {
                    dr = new PdfDictionary();
                    acroForm.put(PdfName.DR, dr);
                    markUsed(acroForm);
                }
                markUsed(dr);
                Iterator i$ = this.fieldTemplates.iterator();
                while (i$.hasNext()) {
                    PdfFormField.mergeResources(dr, (PdfDictionary) ((PdfTemplate) i$.next()).getResources(), this);
                }
                PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
                if (fonts == null) {
                    fonts = new PdfDictionary();
                    dr.put(PdfName.FONT, fonts);
                }
                if (!fonts.contains(PdfName.HELV)) {
                    dic = new PdfDictionary(PdfName.FONT);
                    dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
                    dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
                    dic.put(PdfName.NAME, PdfName.HELV);
                    dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
                    fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
                }
                if (!fonts.contains(PdfName.ZADB)) {
                    dic = new PdfDictionary(PdfName.FONT);
                    dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
                    dic.put(PdfName.NAME, PdfName.ZADB);
                    dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
                    fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
                }
                if (acroForm.get(PdfName.DA) == null) {
                    acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
                    markUsed(acroForm);
                }
            }
        }

        void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
            allAnnots.add(field);
            ArrayList<PdfFormField> kids = field.getKids();
            if (kids != null) {
                for (int k = 0; k < kids.size(); k++) {
                    expandFields((PdfFormField) kids.get(k), allAnnots);
                }
            }
        }

        void addAnnotation(PdfAnnotation annot, PdfDictionary pageN) {
            try {
                PdfFormField field;
                ArrayList<PdfAnnotation> allAnnots = new ArrayList();
                if (annot.isForm()) {
                    this.fieldsAdded = true;
                    getAcroFields();
                    field = (PdfFormField) annot;
                    if (field.getParent() == null) {
                        expandFields(field, allAnnots);
                    } else {
                        return;
                    }
                }
                allAnnots.add(annot);
                for (int k = 0; k < allAnnots.size(); k++) {
                    PdfObject annot2 = (PdfAnnotation) allAnnots.get(k);
                    if (annot2.getPlaceInPage() > 0) {
                        pageN = this.reader.getPageN(annot2.getPlaceInPage());
                    }
                    if (annot2.isForm()) {
                        if (!annot2.isUsed()) {
                            HashSet<PdfTemplate> templates = annot2.getTemplates();
                            if (templates != null) {
                                this.fieldTemplates.addAll(templates);
                            }
                        }
                        field = (PdfFormField) annot2;
                        if (field.getParent() == null) {
                            addDocumentField(field.getIndirectReference());
                        }
                    }
                    if (annot2.isAnnotation()) {
                        PdfObject annots;
                        PdfObject pdfobj = PdfReader.getPdfObject(pageN.get(PdfName.ANNOTS), pageN);
                        if (pdfobj == null || !pdfobj.isArray()) {
                            annots = new PdfArray();
                            pageN.put(PdfName.ANNOTS, annots);
                            markUsed(pageN);
                        } else {
                            annots = (PdfArray) pdfobj;
                        }
                        annots.add(annot2.getIndirectReference());
                        markUsed(annots);
                        if (!annot2.isUsed()) {
                            PdfRectangle rect = (PdfRectangle) annot2.get(PdfName.RECT);
                            if (!(rect == null || (rect.left() == 0.0f && rect.right() == 0.0f && rect.top() == 0.0f && rect.bottom() == 0.0f))) {
                                int rotation = this.reader.getPageRotation((PdfDictionary) pageN);
                                Rectangle pageSize = this.reader.getPageSizeWithRotation((PdfDictionary) pageN);
                                switch (rotation) {
                                    case 90:
                                        annot2.put(PdfName.RECT, new PdfRectangle(pageSize.getTop() - rect.top(), rect.right(), pageSize.getTop() - rect.bottom(), rect.left()));
                                        break;
                                    case 180:
                                        annot2.put(PdfName.RECT, new PdfRectangle(pageSize.getRight() - rect.left(), pageSize.getTop() - rect.bottom(), pageSize.getRight() - rect.right(), pageSize.getTop() - rect.top()));
                                        break;
                                    case 270:
                                        annot2.put(PdfName.RECT, new PdfRectangle(rect.bottom(), pageSize.getRight() - rect.left(), rect.top(), pageSize.getRight() - rect.right()));
                                        break;
                                }
                            }
                        }
                    }
                    if (!annot2.isUsed()) {
                        annot2.setUsed();
                        addToBody(annot2, annot2.getIndirectReference());
                    }
                }
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }

        void addAnnotation(PdfAnnotation annot, int page) {
            annot.setPage(page);
            addAnnotation(annot, this.reader.getPageN(page));
        }

        private void outlineTravel(PRIndirectReference outline) {
            while (outline != null) {
                PdfDictionary outlineR = (PdfDictionary) PdfReader.getPdfObjectRelease(outline);
                PRIndirectReference first = (PRIndirectReference) outlineR.get(PdfName.FIRST);
                if (first != null) {
                    outlineTravel(first);
                }
                PdfReader.killIndirect(outlineR.get(PdfName.DEST));
                PdfReader.killIndirect(outlineR.get(PdfName.f117A));
                PdfReader.killIndirect(outline);
                outline = (PRIndirectReference) outlineR.get(PdfName.NEXT);
            }
        }

        void deleteOutlines() {
            PdfObject catalog = this.reader.getCatalog();
            PdfObject obj = catalog.get(PdfName.OUTLINES);
            if (obj != null) {
                if (obj instanceof PRIndirectReference) {
                    PRIndirectReference outlines = (PRIndirectReference) obj;
                    outlineTravel(outlines);
                    PdfReader.killIndirect(outlines);
                }
                catalog.remove(PdfName.OUTLINES);
                markUsed(catalog);
            }
        }

        protected void setJavaScript() throws IOException {
            HashMap<String, PdfObject> djs = this.pdf.getDocumentLevelJS();
            if (!djs.isEmpty()) {
                PdfObject catalog = this.reader.getCatalog();
                PdfObject names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
                if (names == null) {
                    names = new PdfDictionary();
                    catalog.put(PdfName.NAMES, names);
                    markUsed(catalog);
                }
                markUsed(names);
                names.put(PdfName.JAVASCRIPT, addToBody(PdfNameTree.writeTree(djs, this)).getIndirectReference());
            }
        }

        protected void addFileAttachments() throws IOException {
            HashMap<String, PdfObject> fs = this.pdf.getDocumentFileAttachment();
            if (!fs.isEmpty()) {
                PdfObject catalog = this.reader.getCatalog();
                PdfObject names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
                if (names == null) {
                    names = new PdfDictionary();
                    catalog.put(PdfName.NAMES, names);
                    markUsed(catalog);
                }
                markUsed(names);
                HashMap<String, PdfObject> old = PdfNameTree.readTree((PdfDictionary) PdfReader.getPdfObjectRelease(names.get(PdfName.EMBEDDEDFILES)));
                for (Entry<String, PdfObject> entry : fs.entrySet()) {
                    int k = 0;
                    StringBuilder nn = new StringBuilder((String) entry.getKey());
                    while (old.containsKey(nn.toString())) {
                        k++;
                        nn.append(" ").append(k);
                    }
                    old.put(nn.toString(), entry.getValue());
                }
                PdfDictionary tree = PdfNameTree.writeTree(old, this);
                PdfObject oldEmbeddedFiles = names.get(PdfName.EMBEDDEDFILES);
                if (oldEmbeddedFiles != null) {
                    PdfReader.killIndirect(oldEmbeddedFiles);
                }
                names.put(PdfName.EMBEDDEDFILES, addToBody(tree).getIndirectReference());
            }
        }

        void makePackage(PdfCollection collection) {
            this.reader.getCatalog().put(PdfName.COLLECTION, collection);
        }

        protected void setOutlines() throws IOException {
            if (this.newBookmarks != null) {
                deleteOutlines();
                if (!this.newBookmarks.isEmpty()) {
                    PdfObject catalog = this.reader.getCatalog();
                    writeOutlines(catalog, catalog.get(PdfName.DESTS) != null);
                    markUsed(catalog);
                }
            }
        }

        public void setViewerPreferences(int preferences) {
            this.useVp = true;
            this.viewerPreferences.setViewerPreferences(preferences);
        }

        public void addViewerPreference(PdfName key, PdfObject value) {
            this.useVp = true;
            this.viewerPreferences.addViewerPreference(key, value);
        }

        public void setSigFlags(int f) {
            this.sigFlags |= f;
        }

        public void setPageAction(PdfName actionType, PdfAction action) throws PdfException {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
        }

        void setPageAction(PdfName actionType, PdfAction action, int page) throws PdfException {
            if (actionType.equals(PAGE_OPEN) || actionType.equals(PAGE_CLOSE)) {
                PdfObject pg = this.reader.getPageN(page);
                PdfObject aa = (PdfDictionary) PdfReader.getPdfObject(pg.get(PdfName.AA), pg);
                if (aa == null) {
                    aa = new PdfDictionary();
                    pg.put(PdfName.AA, aa);
                    markUsed(pg);
                }
                aa.put(actionType, action);
                markUsed(aa);
                return;
            }
            throw new PdfException(MessageLocalization.getComposedMessage("invalid.page.additional.action.type.1", actionType.toString()));
        }

        public void setDuration(int seconds) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
        }

        public void setTransition(PdfTransition transition) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
        }

        void setDuration(int seconds, int page) {
            PdfObject pg = this.reader.getPageN(page);
            if (seconds < 0) {
                pg.remove(PdfName.DUR);
            } else {
                pg.put(PdfName.DUR, new PdfNumber(seconds));
            }
            markUsed(pg);
        }

        void setTransition(PdfTransition transition, int page) {
            PdfObject pg = this.reader.getPageN(page);
            if (transition == null) {
                pg.remove(PdfName.TRANS);
            } else {
                pg.put(PdfName.TRANS, transition.getTransitionDictionary());
            }
            markUsed(pg);
        }

        protected void markUsed(PdfObject obj) {
            if (this.append && obj != null) {
                PRIndirectReference ref;
                if (obj.type() == 10) {
                    ref = (PRIndirectReference) obj;
                } else {
                    ref = obj.getIndRef();
                }
                if (ref != null) {
                    this.marked.put(ref.getNumber(), 1);
                }
            }
        }

        protected void markUsed(int num) {
            if (this.append) {
                this.marked.put(num, 1);
            }
        }

        boolean isAppend() {
            return this.append;
        }

        public void setAdditionalAction(PdfName actionType, PdfAction action) throws PdfException {
            if (actionType.equals(DOCUMENT_CLOSE) || actionType.equals(WILL_SAVE) || actionType.equals(DID_SAVE) || actionType.equals(WILL_PRINT) || actionType.equals(DID_PRINT)) {
                PdfObject aa = this.reader.getCatalog().getAsDict(PdfName.AA);
                if (aa == null) {
                    if (action != null) {
                        aa = new PdfDictionary();
                        this.reader.getCatalog().put(PdfName.AA, aa);
                    } else {
                        return;
                    }
                }
                markUsed(aa);
                if (action == null) {
                    aa.remove(actionType);
                    return;
                } else {
                    aa.put(actionType, action);
                    return;
                }
            }
            throw new PdfException(MessageLocalization.getComposedMessage("invalid.additional.action.type.1", actionType.toString()));
        }

        public void setOpenAction(PdfAction action) {
            this.openAction = action;
        }

        public void setOpenAction(String name) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("open.actions.by.name.are.not.supported", new Object[0]));
        }

        public void setThumbnail(Image image) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.setthumbnail", new Object[0]));
        }

        void setThumbnail(Image image, int page) throws PdfException, DocumentException {
            PdfIndirectReference thumb = getImageReference(addDirectImageSimple(image));
            this.reader.resetReleasePage();
            this.reader.getPageN(page).put(PdfName.THUMB, thumb);
            this.reader.resetReleasePage();
        }

        public PdfContentByte getDirectContentUnder() {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.getundercontent.or.pdfstamper.getovercontent", new Object[0]));
        }

        public PdfContentByte getDirectContent() {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.getundercontent.or.pdfstamper.getovercontent", new Object[0]));
        }

        protected void readOCProperties() {
            if (this.documentOCG.isEmpty()) {
                PdfDictionary dict = this.reader.getCatalog().getAsDict(PdfName.OCPROPERTIES);
                if (dict != null) {
                    PdfArray ocgs = dict.getAsArray(PdfName.OCGS);
                    HashMap<String, PdfLayer> ocgmap = new HashMap();
                    Iterator<PdfObject> i = ocgs.listIterator();
                    while (i.hasNext()) {
                        PdfObject ref = (PdfIndirectReference) i.next();
                        PdfLayer layer = new PdfLayer(null);
                        layer.setRef(ref);
                        layer.setOnPanel(false);
                        layer.merge((PdfDictionary) PdfReader.getPdfObject(ref));
                        ocgmap.put(ref.toString(), layer);
                    }
                    PdfDictionary d = dict.getAsDict(PdfName.f120D);
                    PdfArray off = d.getAsArray(PdfName.OFF);
                    if (off != null) {
                        i = off.listIterator();
                        while (i.hasNext()) {
                            ((PdfLayer) ocgmap.get(((PdfIndirectReference) i.next()).toString())).setOn(false);
                        }
                    }
                    PdfArray order = d.getAsArray(PdfName.ORDER);
                    if (order != null) {
                        addOrder(null, order, ocgmap);
                    }
                    this.documentOCG.addAll(ocgmap.values());
                    this.OCGRadioGroup = d.getAsArray(PdfName.RBGROUPS);
                    if (this.OCGRadioGroup == null) {
                        this.OCGRadioGroup = new PdfArray();
                    }
                    this.OCGLocked = d.getAsArray(PdfName.LOCKED);
                    if (this.OCGLocked == null) {
                        this.OCGLocked = new PdfArray();
                    }
                }
            }
        }

        private void addOrder(PdfLayer parent, PdfArray arr, Map<String, PdfLayer> ocgmap) {
            int i = 0;
            while (i < arr.size()) {
                PdfObject obj = arr.getPdfObject(i);
                PdfLayer layer;
                if (obj.isIndirect()) {
                    layer = (PdfLayer) ocgmap.get(obj.toString());
                    if (layer != null) {
                        layer.setOnPanel(true);
                        registerLayer(layer);
                        if (parent != null) {
                            parent.addChild(layer);
                        }
                        if (arr.size() > i + 1 && arr.getPdfObject(i + 1).isArray()) {
                            i++;
                            addOrder(layer, (PdfArray) arr.getPdfObject(i), ocgmap);
                        }
                    }
                } else if (obj.isArray()) {
                    PdfArray sub = (PdfArray) obj;
                    if (!sub.isEmpty()) {
                        obj = sub.getPdfObject(0);
                        if (obj.isString()) {
                            layer = new PdfLayer(obj.toString());
                            layer.setOnPanel(true);
                            registerLayer(layer);
                            if (parent != null) {
                                parent.addChild(layer);
                            }
                            PdfArray array = new PdfArray();
                            Iterator<PdfObject> j = sub.listIterator();
                            while (j.hasNext()) {
                                array.add((PdfObject) j.next());
                            }
                            addOrder(layer, array, ocgmap);
                        } else {
                            addOrder(parent, (PdfArray) obj, ocgmap);
                        }
                    } else {
                        return;
                    }
                } else {
                    continue;
                }
                i++;
            }
        }

        public Map<String, PdfLayer> getPdfLayers() {
            if (this.documentOCG.isEmpty()) {
                readOCProperties();
            }
            HashMap<String, PdfLayer> map = new HashMap();
            Iterator i$ = this.documentOCG.iterator();
            while (i$.hasNext()) {
                String key;
                PdfLayer layer = (PdfLayer) ((PdfOCG) i$.next());
                if (layer.getTitle() == null) {
                    key = layer.getAsString(PdfName.NAME).toString();
                } else {
                    key = layer.getTitle();
                }
                if (map.containsKey(key)) {
                    int seq = 2;
                    String tmp = key + "(" + 2 + ")";
                    while (map.containsKey(tmp)) {
                        seq++;
                        tmp = key + "(" + seq + ")";
                    }
                    key = tmp;
                }
                map.put(key, layer);
            }
            return map;
        }

        public void createXmpMetadata() {
            try {
                this.xmpWriter = createXmpWriter(null, this.reader.getInfo());
                this.xmpMetadata = null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
