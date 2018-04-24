package com.itextpdf.testutils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Meta;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfAnnotation.PdfImportedLink;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RefKey;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TaggedPdfReaderTool;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.options.SerializeOptions;
import com.lee.ultrascan.utils.ReportHelper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CompareTool {
    private static final String cannotOpenTargetDirectory = "Cannot open target directory for <filename>.";
    private static final String differentPages = "File <filename> differs on page <pagenumber>.";
    private static final String gsFailed = "GhostScript failed for <filename>.";
    private static final String ignoredAreasPrefix = "ignored_areas_";
    private static final String undefinedGsPath = "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs9.14/bin/gswin32c.exe\")";
    private static final String unexpectedNumberOfPages = "Unexpected number of pages for <filename>.";
    private String cmpImage;
    List<PdfDictionary> cmpPages;
    List<RefKey> cmpPagesRef;
    private String cmpPdf;
    private String cmpPdfName;
    private String compareExec = System.getProperty("compareExec");
    private final String compareParams = " <image1> <image2> <difference>";
    private String gsExec = System.getProperty("gsExec");
    private final String gsParams = " -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>";
    private String outImage;
    List<PdfDictionary> outPages;
    List<RefKey> outPagesRef;
    private String outPdf;
    private String outPdfName;

    class CmpMarkedContentRenderFilter implements RenderListener {
        Map<Integer, TextExtractionStrategy> tagsByMcid = new HashMap();

        CmpMarkedContentRenderFilter() {
        }

        public Map<Integer, String> getParsedTagContent() {
            Map<Integer, String> content = new HashMap();
            for (Integer intValue : this.tagsByMcid.keySet()) {
                int id = intValue.intValue();
                content.put(Integer.valueOf(id), ((TextExtractionStrategy) this.tagsByMcid.get(Integer.valueOf(id))).getResultantText());
            }
            return content;
        }

        public void beginTextBlock() {
            for (Integer intValue : this.tagsByMcid.keySet()) {
                ((TextExtractionStrategy) this.tagsByMcid.get(Integer.valueOf(intValue.intValue()))).beginTextBlock();
            }
        }

        public void renderText(TextRenderInfo renderInfo) {
            Integer mcid = renderInfo.getMcid();
            if (mcid != null && this.tagsByMcid.containsKey(mcid)) {
                ((TextExtractionStrategy) this.tagsByMcid.get(mcid)).renderText(renderInfo);
            } else if (mcid != null) {
                this.tagsByMcid.put(mcid, new SimpleTextExtractionStrategy());
                ((TextExtractionStrategy) this.tagsByMcid.get(mcid)).renderText(renderInfo);
            }
        }

        public void endTextBlock() {
            for (Integer intValue : this.tagsByMcid.keySet()) {
                ((TextExtractionStrategy) this.tagsByMcid.get(Integer.valueOf(intValue.intValue()))).endTextBlock();
            }
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }
    }

    class CmpPngFileFilter implements FileFilter {
        CmpPngFileFilter() {
        }

        public boolean accept(File pathname) {
            String ap = pathname.getAbsolutePath();
            return ap.endsWith(".png") && ap.contains("cmp_") && ap.contains(CompareTool.this.cmpPdfName);
        }
    }

    class CmpTaggedPdfReaderTool extends TaggedPdfReaderTool {
        Map<PdfDictionary, Map<Integer, String>> parsedTags = new HashMap();

        CmpTaggedPdfReaderTool() {
        }

        public void parseTag(String tag, PdfObject object, PdfDictionary page) throws IOException {
            if (object instanceof PdfNumber) {
                if (!this.parsedTags.containsKey(page)) {
                    CmpMarkedContentRenderFilter listener = new CmpMarkedContentRenderFilter();
                    new PdfContentStreamProcessor(listener).processContent(PdfReader.getPageContent(page), page.getAsDict(PdfName.RESOURCES));
                    this.parsedTags.put(page, listener.getParsedTagContent());
                }
                String tagContent = "";
                if (((Map) this.parsedTags.get(page)).containsKey(Integer.valueOf(((PdfNumber) object).intValue()))) {
                    tagContent = (String) ((Map) this.parsedTags.get(page)).get(Integer.valueOf(((PdfNumber) object).intValue()));
                }
                this.out.print(XMLUtil.escapeXML(tagContent, true));
                return;
            }
            super.parseTag(tag, object, page);
        }

        public void inspectChildDictionary(PdfDictionary k) throws IOException {
            inspectChildDictionary(k, true);
        }
    }

    class ImageNameComparator implements Comparator<File> {
        ImageNameComparator() {
        }

        public int compare(File f1, File f2) {
            return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
        }
    }

    class PngFileFilter implements FileFilter {
        PngFileFilter() {
        }

        public boolean accept(File pathname) {
            String ap = pathname.getAbsolutePath();
            return ap.endsWith(".png") && !ap.contains("cmp_") && ap.contains(CompareTool.this.outPdfName);
        }
    }

    private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
        return compare(outPath, differenceImagePrefix, (Map) ignoredAreas, null);
    }

    private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas, List<Integer> equalPages) throws IOException, InterruptedException, DocumentException {
        if (this.gsExec == null) {
            return undefinedGsPath;
        }
        if (!new File(this.gsExec).exists()) {
            return new File(this.gsExec).getAbsolutePath() + " does not exist";
        }
        if (!outPath.endsWith("/")) {
            outPath = outPath + "/";
        }
        File file = new File(outPath);
        if (file.exists()) {
            for (File file2 : file.listFiles(new PngFileFilter())) {
                file2.delete();
            }
            for (File file22 : file.listFiles(new CmpPngFileFilter())) {
                file22.delete();
            }
        } else {
            file.mkdir();
        }
        File diffFile = new File(outPath + differenceImagePrefix);
        if (diffFile.exists()) {
            diffFile.delete();
        }
        if (!(ignoredAreas == null || ignoredAreas.isEmpty())) {
            PdfReader cmpReader = new PdfReader(this.cmpPdf);
            PdfReader pdfReader = new PdfReader(this.outPdf);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(outPath + ignoredAreasPrefix + this.outPdfName));
            PdfStamper cmpStamper = new PdfStamper(cmpReader, new FileOutputStream(outPath + ignoredAreasPrefix + this.cmpPdfName));
            for (Entry<Integer, List<Rectangle>> entry : ignoredAreas.entrySet()) {
                int pageNumber = ((Integer) entry.getKey()).intValue();
                List<Rectangle> rectangles = (List) entry.getValue();
                if (!(rectangles == null || rectangles.isEmpty())) {
                    PdfContentByte outCB = pdfStamper.getOverContent(pageNumber);
                    PdfContentByte cmpCB = cmpStamper.getOverContent(pageNumber);
                    for (Rectangle rect : rectangles) {
                        rect.setBackgroundColor(BaseColor.BLACK);
                        outCB.rectangle(rect);
                        cmpCB.rectangle(rect);
                    }
                }
            }
            pdfStamper.close();
            cmpStamper.close();
            pdfReader.close();
            cmpReader.close();
            init(outPath + ignoredAreasPrefix + this.outPdfName, outPath + ignoredAreasPrefix + this.cmpPdfName);
        }
        if (!file.exists()) {
            return cannotOpenTargetDirectory.replace("<filename>", this.outPdf);
        }
        getClass();
        Process p = Runtime.getRuntime().exec(this.gsExec + " -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>".replace("<outputfile>", outPath + this.cmpImage).replace("<inputfile>", this.cmpPdf));
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while (true) {
            String line = bri.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        bri.close();
        while (true) {
            line = bre.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        bre.close();
        if (p.waitFor() != 0) {
            return gsFailed.replace("<filename>", this.cmpPdf);
        }
        getClass();
        p = Runtime.getRuntime().exec(this.gsExec + " -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>".replace("<outputfile>", outPath + this.outImage).replace("<inputfile>", this.outPdf));
        bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while (true) {
            line = bri.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        bri.close();
        while (true) {
            line = bre.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        bre.close();
        if (p.waitFor() != 0) {
            return gsFailed.replace("<filename>", this.outPdf);
        }
        File[] imageFiles = file.listFiles(new PngFileFilter());
        File[] cmpImageFiles = file.listFiles(new CmpPngFileFilter());
        boolean bUnexpectedNumberOfPages = false;
        if (imageFiles.length != cmpImageFiles.length) {
            bUnexpectedNumberOfPages = true;
        }
        int cnt = Math.min(imageFiles.length, cmpImageFiles.length);
        if (cnt < 1) {
            return "No files for comparing!!!\nThe result or sample pdf file is not processed by GhostScript.";
        }
        Arrays.sort(imageFiles, new ImageNameComparator());
        Arrays.sort(cmpImageFiles, new ImageNameComparator());
        String differentPagesFail = null;
        int i = 0;
        while (i < cnt) {
            if (equalPages == null || !equalPages.contains(Integer.valueOf(i))) {
                System.out.print("Comparing page " + Integer.toString(i + 1) + " (" + imageFiles[i].getAbsolutePath() + ")...");
                InputStream fileInputStream = new FileInputStream(imageFiles[i]);
                fileInputStream = new FileInputStream(cmpImageFiles[i]);
                boolean cmpResult = compareStreams(fileInputStream, fileInputStream);
                fileInputStream.close();
                fileInputStream.close();
                if (cmpResult) {
                    System.out.println("done.");
                } else if (this.compareExec == null || !new File(this.compareExec).exists()) {
                    differentPagesFail = differentPages.replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1)) + "\nYou can optionally specify path to ImageMagick compare tool (e.g. -DcompareExec=\"C:/Program Files/ImageMagick-6.5.4-2/compare.exe\") to visualize differences.";
                    break;
                } else {
                    getClass();
                    p = Runtime.getRuntime().exec(this.compareExec + " <image1> <image2> <difference>".replace("<image1>", imageFiles[i].getAbsolutePath()).replace("<image2>", cmpImageFiles[i].getAbsolutePath()).replace("<difference>", outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png"));
                    bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    while (true) {
                        line = bre.readLine();
                        if (line == null) {
                            break;
                        }
                        System.out.println(line);
                    }
                    bre.close();
                    if (p.waitFor() != 0) {
                        differentPagesFail = differentPages.replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1));
                    } else if (differentPagesFail == null) {
                        differentPagesFail = differentPages.replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1)) + "\nPlease, examine " + outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png for more details.";
                    } else {
                        differentPagesFail = "File " + this.outPdf + " differs.\nPlease, examine difference images for more details.";
                    }
                    System.out.println(differentPagesFail);
                }
            }
            i++;
        }
        if (differentPagesFail != null) {
            return differentPagesFail;
        }
        if (bUnexpectedNumberOfPages) {
            return unexpectedNumberOfPages.replace("<filename>", this.outPdf);
        }
        return null;
    }

    public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
        init(outPdf, cmpPdf);
        return compare(outPath, differenceImagePrefix, ignoredAreas);
    }

    public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws IOException, InterruptedException, DocumentException {
        return compare(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
    }

    private String compareByContent(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
        System.out.print("[itext] INFO  Comparing by content..........");
        PdfReader outReader = new PdfReader(this.outPdf);
        this.outPages = new ArrayList();
        this.outPagesRef = new ArrayList();
        loadPagesFromReader(outReader, this.outPages, this.outPagesRef);
        PdfReader cmpReader = new PdfReader(this.cmpPdf);
        this.cmpPages = new ArrayList();
        this.cmpPagesRef = new ArrayList();
        loadPagesFromReader(cmpReader, this.cmpPages, this.cmpPagesRef);
        if (this.outPages.size() != this.cmpPages.size()) {
            return compare(outPath, differenceImagePrefix, ignoredAreas);
        }
        List equalPages = new ArrayList(this.cmpPages.size());
        for (int i = 0; i < this.cmpPages.size(); i++) {
            if (compareDictionaries((PdfDictionary) this.outPages.get(i), (PdfDictionary) this.cmpPages.get(i))) {
                equalPages.add(Integer.valueOf(i));
            }
        }
        outReader.close();
        cmpReader.close();
        if (equalPages.size() == this.cmpPages.size()) {
            System.out.println("OK");
            System.out.flush();
            return null;
        }
        System.out.println("Fail");
        System.out.flush();
        String message = compare(outPath, differenceImagePrefix, (Map) ignoredAreas, equalPages);
        if (message == null || message.length() == 0) {
            return "Compare by content fails. No visual differences";
        }
        return message;
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
        init(outPdf, cmpPdf);
        return compareByContent(outPath, differenceImagePrefix, ignoredAreas);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws DocumentException, InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
    }

    private void loadPagesFromReader(PdfReader reader, List<PdfDictionary> pages, List<RefKey> pagesRef) {
        addPagesFromDict(reader.getCatalog().get(PdfName.PAGES), pages, pagesRef);
    }

    private void addPagesFromDict(PdfObject dictRef, List<PdfDictionary> pages, List<RefKey> pagesRef) {
        PdfDictionary dict = (PdfDictionary) PdfReader.getPdfObject(dictRef);
        if (dict.isPages()) {
            PdfArray kids = dict.getAsArray(PdfName.KIDS);
            if (kids != null) {
                Iterator i$ = kids.iterator();
                while (i$.hasNext()) {
                    addPagesFromDict((PdfObject) i$.next(), pages, pagesRef);
                }
            }
        } else if (dict.isPage()) {
            pages.add(dict);
            pagesRef.add(new RefKey((PRIndirectReference) dictRef));
        }
    }

    private boolean compareObjects(PdfObject outObj, PdfObject cmpObj) throws IOException {
        PdfObject outDirectObj = PdfReader.getPdfObject(outObj);
        PdfObject cmpDirectObj = PdfReader.getPdfObject(cmpObj);
        if (outDirectObj == null || cmpDirectObj.type() != outDirectObj.type()) {
            return false;
        }
        if (cmpDirectObj.isDictionary()) {
            PdfDictionary cmpDict = (PdfDictionary) cmpDirectObj;
            PdfDictionary outDict = (PdfDictionary) outDirectObj;
            if (cmpDict.isPage()) {
                if (!outDict.isPage()) {
                    return false;
                }
                RefKey cmpRefKey = new RefKey((PRIndirectReference) cmpObj);
                RefKey outRefKey = new RefKey((PRIndirectReference) outObj);
                if (this.cmpPagesRef.contains(cmpRefKey) && this.cmpPagesRef.indexOf(cmpRefKey) == this.outPagesRef.indexOf(outRefKey)) {
                    return true;
                }
                return false;
            } else if (!compareDictionaries(outDict, cmpDict)) {
                return false;
            }
        } else if (cmpDirectObj.isStream()) {
            if (!compareStreams((PRStream) outDirectObj, (PRStream) cmpDirectObj)) {
                return false;
            }
        } else if (cmpDirectObj.isArray()) {
            if (!compareArrays((PdfArray) outDirectObj, (PdfArray) cmpDirectObj)) {
                return false;
            }
        } else if (cmpDirectObj.isName()) {
            if (!compareNames((PdfName) outDirectObj, (PdfName) cmpDirectObj)) {
                return false;
            }
        } else if (cmpDirectObj.isNumber()) {
            if (!compareNumbers((PdfNumber) outDirectObj, (PdfNumber) cmpDirectObj)) {
                return false;
            }
        } else if (cmpDirectObj.isString()) {
            if (!compareStrings((PdfString) outDirectObj, (PdfString) cmpDirectObj)) {
                return false;
            }
        } else if (cmpDirectObj.isBoolean()) {
            if (!compareBooleans((PdfBoolean) outDirectObj, (PdfBoolean) cmpDirectObj)) {
                return false;
            }
        } else if (!(outDirectObj.isNull() && cmpDirectObj.isNull())) {
            throw new UnsupportedOperationException();
        }
        return true;
    }

    public boolean compareDictionaries(PdfDictionary outDict, PdfDictionary cmpDict) throws IOException {
        for (PdfName key : cmpDict.getKeys()) {
            if (key.compareTo(PdfName.PARENT) != 0) {
                if (key.compareTo(PdfName.BASEFONT) == 0 || key.compareTo(PdfName.FONTNAME) == 0) {
                    PdfObject cmpObj = cmpDict.getDirectObject(key);
                    if (cmpObj.isName() && cmpObj.toString().indexOf(43) > 0) {
                        PdfObject outObj = outDict.getDirectObject(key);
                        if (!outObj.isName() || outObj.toString().indexOf(43) == -1) {
                            return false;
                        }
                        if (!cmpObj.toString().substring(cmpObj.toString().indexOf(43)).equals(outObj.toString().substring(outObj.toString().indexOf(43)))) {
                            return false;
                        }
                    }
                }
                if (!compareObjects(outDict.get(key), cmpDict.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean compareStreams(PRStream outStream, PRStream cmpStream) throws IOException {
        boolean decodeStreams = PdfName.FLATEDECODE.equals(outStream.get(PdfName.FILTER));
        byte[] outStreamBytes = PdfReader.getStreamBytesRaw(outStream);
        byte[] cmpStreamBytes = PdfReader.getStreamBytesRaw(cmpStream);
        if (decodeStreams) {
            outStreamBytes = PdfReader.decodeBytes(outStreamBytes, outStream);
            cmpStreamBytes = PdfReader.decodeBytes(cmpStreamBytes, cmpStream);
        }
        return Arrays.equals(outStreamBytes, cmpStreamBytes);
    }

    public boolean compareArrays(PdfArray outArray, PdfArray cmpArray) throws IOException {
        if (outArray == null || outArray.size() != cmpArray.size()) {
            return false;
        }
        for (int i = 0; i < cmpArray.size(); i++) {
            if (!compareObjects(outArray.getPdfObject(i), cmpArray.getPdfObject(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean compareNames(PdfName outName, PdfName cmpName) {
        return cmpName.compareTo(outName) == 0;
    }

    public boolean compareNumbers(PdfNumber outNumber, PdfNumber cmpNumber) {
        return cmpNumber.doubleValue() == outNumber.doubleValue();
    }

    public boolean compareStrings(PdfString outString, PdfString cmpString) {
        return Arrays.equals(cmpString.getBytes(), outString.getBytes());
    }

    public boolean compareBooleans(PdfBoolean outBoolean, PdfBoolean cmpBoolean) {
        return Arrays.equals(cmpBoolean.getBytes(), outBoolean.getBytes());
    }

    public String compareXmp(String outPdf, String cmpPdf) {
        return compareXmp(outPdf, cmpPdf, false);
    }

    public String compareXmp(String outPdf, String cmpPdf, boolean ignoreDateAndProducerProperties) {
        String str;
        Throwable th;
        init(outPdf, cmpPdf);
        PdfReader cmpReader = null;
        PdfReader outReader = null;
        try {
            PdfReader outReader2;
            PdfReader cmpReader2 = new PdfReader(this.cmpPdf);
            try {
                outReader2 = new PdfReader(this.outPdf);
            } catch (XMPException e) {
                cmpReader = cmpReader2;
                try {
                    str = "XMP parsing failure!";
                    if (cmpReader != null) {
                        cmpReader.close();
                    }
                    if (outReader != null) {
                        return str;
                    }
                    outReader.close();
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    if (cmpReader != null) {
                        cmpReader.close();
                    }
                    if (outReader != null) {
                        outReader.close();
                    }
                    throw th;
                }
            } catch (IOException e2) {
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (ParserConfigurationException e3) {
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (SAXException e4) {
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (Throwable th3) {
                th = th3;
                cmpReader = cmpReader2;
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    outReader.close();
                }
                throw th;
            }
            try {
                byte[] cmpBytes = cmpReader2.getMetadata();
                byte[] outBytes = outReader2.getMetadata();
                if (ignoreDateAndProducerProperties) {
                    XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(cmpBytes);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATEDATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.METADATADATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/pdf/1.3/", PdfProperties.PRODUCER, true, true);
                    cmpBytes = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(8192));
                    xmpMeta = XMPMetaFactory.parseFromBuffer(outBytes);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATEDATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.METADATADATE, true, true);
                    XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/pdf/1.3/", PdfProperties.PRODUCER, true, true);
                    outBytes = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(8192));
                }
                if (compareXmls(cmpBytes, outBytes)) {
                    if (cmpReader2 != null) {
                        cmpReader2.close();
                    }
                    if (outReader2 != null) {
                        outReader2.close();
                    }
                    outReader = outReader2;
                    cmpReader = cmpReader2;
                    return null;
                }
                str = "The XMP packages different!";
                if (cmpReader2 != null) {
                    cmpReader2.close();
                }
                if (outReader2 != null) {
                    outReader2.close();
                }
                outReader = outReader2;
                cmpReader = cmpReader2;
                return str;
            } catch (XMPException e5) {
                outReader = outReader2;
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (IOException e6) {
                outReader = outReader2;
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (ParserConfigurationException e7) {
                outReader = outReader2;
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (SAXException e8) {
                outReader = outReader2;
                cmpReader = cmpReader2;
                str = "XMP parsing failure!";
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    return str;
                }
                outReader.close();
                return str;
            } catch (Throwable th4) {
                th = th4;
                outReader = outReader2;
                cmpReader = cmpReader2;
                if (cmpReader != null) {
                    cmpReader.close();
                }
                if (outReader != null) {
                    outReader.close();
                }
                throw th;
            }
        } catch (XMPException e9) {
            str = "XMP parsing failure!";
            if (cmpReader != null) {
                cmpReader.close();
            }
            if (outReader != null) {
                return str;
            }
            outReader.close();
            return str;
        } catch (IOException e10) {
            str = "XMP parsing failure!";
            if (cmpReader != null) {
                cmpReader.close();
            }
            if (outReader != null) {
                return str;
            }
            outReader.close();
            return str;
        } catch (ParserConfigurationException e11) {
            str = "XMP parsing failure!";
            if (cmpReader != null) {
                cmpReader.close();
            }
            if (outReader != null) {
                return str;
            }
            outReader.close();
            return str;
        } catch (SAXException e12) {
            str = "XMP parsing failure!";
            if (cmpReader != null) {
                cmpReader.close();
            }
            if (outReader != null) {
                return str;
            }
            outReader.close();
            return str;
        }
    }

    public boolean compareXmls(byte[] xml1, byte[] xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc1 = db.parse(new ByteArrayInputStream(xml1));
        doc1.normalizeDocument();
        Document doc2 = db.parse(new ByteArrayInputStream(xml2));
        doc2.normalizeDocument();
        return doc2.isEqualNode(doc1);
    }

    public String compareDocumentInfo(String outPdf, String cmpPdf) throws IOException {
        System.out.print("[itext] INFO  Comparing document info.......");
        String message = null;
        PdfReader outReader = new PdfReader(outPdf);
        PdfReader cmpReader = new PdfReader(cmpPdf);
        String[] cmpInfo = convertInfo(cmpReader.getInfo());
        String[] outInfo = convertInfo(outReader.getInfo());
        for (int i = 0; i < cmpInfo.length; i++) {
            if (!cmpInfo[i].equals(outInfo[i])) {
                message = "Document info fail";
                break;
            }
        }
        outReader.close();
        cmpReader.close();
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    private boolean linksAreSame(PdfImportedLink cmpLink, PdfImportedLink outLink) {
        if (cmpLink.getDestinationPage() != outLink.getDestinationPage() || !cmpLink.getRect().toString().equals(outLink.getRect().toString())) {
            return false;
        }
        Map<PdfName, PdfObject> cmpParams = cmpLink.getParameters();
        Map<PdfName, PdfObject> outParams = outLink.getParameters();
        if (cmpParams.size() != outParams.size()) {
            return false;
        }
        for (Entry<PdfName, PdfObject> cmpEntry : cmpParams.entrySet()) {
            PdfObject cmpObj = (PdfObject) cmpEntry.getValue();
            if (!outParams.containsKey(cmpEntry.getKey())) {
                return false;
            }
            PdfObject outObj = (PdfObject) outParams.get(cmpEntry.getKey());
            if (cmpObj.type() == outObj.type()) {
                switch (cmpObj.type()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 8:
                        if (cmpObj.toString().equals(outObj.toString())) {
                            break;
                        }
                        return false;
                    default:
                        break;
                }
            }
            return false;
        }
        return true;
    }

    public String compareLinks(String outPdf, String cmpPdf) throws IOException {
        System.out.print("[itext] INFO  Comparing link annotations....");
        String message = null;
        PdfReader outReader = new PdfReader(outPdf);
        PdfReader cmpReader = new PdfReader(cmpPdf);
        int i = 0;
        while (i < outReader.getNumberOfPages() && i < cmpReader.getNumberOfPages()) {
            List<PdfImportedLink> outLinks = outReader.getLinks(i + 1);
            List<PdfImportedLink> cmpLinks = cmpReader.getLinks(i + 1);
            if (cmpLinks.size() != outLinks.size()) {
                message = String.format("Different number of links on page %d.", new Object[]{Integer.valueOf(i + 1)});
                break;
            }
            for (int j = 0; j < cmpLinks.size(); j++) {
                if (!linksAreSame((PdfImportedLink) cmpLinks.get(j), (PdfImportedLink) outLinks.get(j))) {
                    message = String.format("Different links on page %d.\n%s\n%s", new Object[]{Integer.valueOf(i + 1), ((PdfImportedLink) cmpLinks.get(j)).toString(), ((PdfImportedLink) outLinks.get(j)).toString()});
                    break;
                }
            }
            i++;
        }
        outReader.close();
        cmpReader.close();
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    public String compareTagStructures(String outPdf, String cmpPdf) throws IOException, ParserConfigurationException, SAXException {
        System.out.print("[itext] INFO  Comparing tag structures......");
        String outXml = outPdf.replace(ReportHelper.PDF_FILE_EXTENSION, ".xml");
        String cmpXml = outPdf.replace(ReportHelper.PDF_FILE_EXTENSION, ".cmp.xml");
        String message = null;
        PdfReader reader = new PdfReader(outPdf);
        FileOutputStream xmlOut1 = new FileOutputStream(outXml);
        new CmpTaggedPdfReaderTool().convertToXml(reader, xmlOut1);
        reader.close();
        reader = new PdfReader(cmpPdf);
        FileOutputStream xmlOut2 = new FileOutputStream(cmpXml);
        new CmpTaggedPdfReaderTool().convertToXml(reader, xmlOut2);
        reader.close();
        if (!compareXmls(outXml, cmpXml)) {
            message = "The tag structures are different.";
        }
        xmlOut1.close();
        xmlOut2.close();
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    private String[] convertInfo(HashMap<String, String> info) {
        String[] convertedInfo = new String[]{"", "", "", ""};
        for (Entry<String, String> entry : info.entrySet()) {
            if ("title".equalsIgnoreCase((String) entry.getKey())) {
                convertedInfo[0] = (String) entry.getValue();
            } else if (Meta.AUTHOR.equalsIgnoreCase((String) entry.getKey())) {
                convertedInfo[1] = (String) entry.getValue();
            } else if ("subject".equalsIgnoreCase((String) entry.getKey())) {
                convertedInfo[2] = (String) entry.getValue();
            } else if (Meta.KEYWORDS.equalsIgnoreCase((String) entry.getKey())) {
                convertedInfo[3] = (String) entry.getValue();
            }
        }
        return convertedInfo;
    }

    public boolean compareXmls(String xml1, String xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc1 = db.parse(new File(xml1));
        doc1.normalizeDocument();
        Document doc2 = db.parse(new File(xml2));
        doc2.normalizeDocument();
        return doc2.isEqualNode(doc1);
    }

    private void init(String outPdf, String cmpPdf) {
        this.outPdf = outPdf;
        this.cmpPdf = cmpPdf;
        this.outPdfName = new File(outPdf).getName();
        this.cmpPdfName = new File(cmpPdf).getName();
        this.outImage = this.outPdfName + "-%03d.png";
        if (this.cmpPdfName.startsWith("cmp_")) {
            this.cmpImage = this.cmpPdfName + "-%03d.png";
        } else {
            this.cmpImage = "cmp_" + this.cmpPdfName + "-%03d.png";
        }
    }

    private boolean compareStreams(InputStream is1, InputStream is2) throws IOException {
        byte[] buffer1 = new byte[65536];
        byte[] buffer2 = new byte[65536];
        int len1;
        do {
            len1 = is1.read(buffer1);
            if (len1 != is2.read(buffer2) || !Arrays.equals(buffer1, buffer2)) {
                return false;
            }
        } while (len1 != -1);
        return true;
    }
}
