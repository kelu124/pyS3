package org.apache.poi.hssf.extractor;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.OldFormulaRecord;
import org.apache.poi.hssf.record.OldLabelRecord;
import org.apache.poi.hssf.record.OldSheetRecord;
import org.apache.poi.hssf.record.OldStringRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.util.IOUtils;
import org.bytedeco.javacpp.opencv_videoio;

public class OldExcelExtractor implements Closeable {
    private int biffVersion;
    private int fileType;
    private RecordInputStream ris;
    private Closeable toClose;

    public OldExcelExtractor(InputStream input) throws IOException {
        open(input);
    }

    public OldExcelExtractor(File f) throws IOException {
        InputStream biffStream;
        NPOIFSFileSystem poifs = null;
        try {
            NPOIFSFileSystem poifs2 = new NPOIFSFileSystem(f);
            try {
                open(poifs2);
                this.toClose = poifs2;
                poifs = poifs2;
            } catch (OldExcelFormatException e) {
                poifs = poifs2;
                if (poifs != null) {
                    poifs.close();
                }
                biffStream = new FileInputStream(f);
                try {
                    open(biffStream);
                } catch (IOException e2) {
                    biffStream.close();
                    throw e2;
                } catch (RuntimeException e3) {
                    biffStream.close();
                    throw e3;
                }
            } catch (NotOLE2FileException e4) {
                poifs = poifs2;
                if (poifs != null) {
                    poifs.close();
                }
                biffStream = new FileInputStream(f);
                open(biffStream);
            }
        } catch (OldExcelFormatException e5) {
            if (poifs != null) {
                poifs.close();
            }
            biffStream = new FileInputStream(f);
            open(biffStream);
        } catch (NotOLE2FileException e6) {
            if (poifs != null) {
                poifs.close();
            }
            biffStream = new FileInputStream(f);
            open(biffStream);
        }
    }

    public OldExcelExtractor(NPOIFSFileSystem fs) throws IOException {
        open(fs);
    }

    public OldExcelExtractor(DirectoryNode directory) throws IOException {
        open(directory);
    }

    private void open(InputStream biffStream) throws IOException {
        BufferedInputStream bis = biffStream instanceof BufferedInputStream ? (BufferedInputStream) biffStream : new BufferedInputStream(biffStream, 8);
        if (NPOIFSFileSystem.hasPOIFSHeader(bis)) {
            NPOIFSFileSystem poifs = new NPOIFSFileSystem(bis);
            try {
                open(poifs);
            } finally {
                poifs.close();
            }
        } else {
            this.ris = new RecordInputStream(bis);
            this.toClose = bis;
            prepare();
        }
    }

    private void open(NPOIFSFileSystem fs) throws IOException {
        open(fs.getRoot());
    }

    private void open(DirectoryNode directory) throws IOException {
        try {
            Entry book = (DocumentNode) directory.getEntry(InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME);
        } catch (FileNotFoundException e) {
            DocumentNode book2 = (DocumentNode) directory.getEntry(InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES[0]);
        }
        if (book == null) {
            throw new IOException("No Excel 5/95 Book stream found");
        }
        this.ris = new RecordInputStream(directory.createDocumentInputStream(book));
        prepare();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Use:");
            System.err.println("   OldExcelExtractor <filename>");
            System.exit(1);
        }
        OldExcelExtractor extractor = new OldExcelExtractor(new File(args[0]));
        System.out.println(extractor.getText());
        extractor.close();
    }

    private void prepare() {
        if (this.ris.hasNextRecord()) {
            this.ris.nextRecord();
            int bofSid = this.ris.getSid();
            switch (bofSid) {
                case 9:
                    this.biffVersion = 2;
                    break;
                case 521:
                    this.biffVersion = 3;
                    break;
                case 1033:
                    this.biffVersion = 4;
                    break;
                case 2057:
                    this.biffVersion = 5;
                    break;
                default:
                    throw new IllegalArgumentException("File does not begin with a BOF, found sid of " + bofSid);
            }
            this.fileType = new BOFRecord(this.ris).getType();
            return;
        }
        throw new IllegalArgumentException("File contains no records!");
    }

    public int getBiffVersion() {
        return this.biffVersion;
    }

    public int getFileType() {
        return this.fileType;
    }

    public String getText() {
        StringBuffer text = new StringBuffer();
        CodepageRecord codepage = null;
        while (this.ris.hasNextRecord()) {
            int sid = this.ris.getNextSid();
            this.ris.nextRecord();
            switch (sid) {
                case 4:
                case opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH /*516*/:
                    OldLabelRecord lr = new OldLabelRecord(this.ris);
                    lr.setCodePage(codepage);
                    text.append(lr.getValue());
                    text.append('\n');
                    break;
                case 6:
                case 518:
                case 1030:
                    if (this.biffVersion != 5) {
                        OldFormulaRecord fr = new OldFormulaRecord(this.ris);
                        if (fr.getCachedResultType() != CellType.NUMERIC.getCode()) {
                            break;
                        }
                        handleNumericCell(text, fr.getValue());
                        break;
                    }
                    FormulaRecord fr2 = new FormulaRecord(this.ris);
                    if (fr2.getCachedResultType() != CellType.NUMERIC.getCode()) {
                        break;
                    }
                    handleNumericCell(text, fr2.getValue());
                    break;
                case 7:
                case TIFFConstants.TIFFTAG_JPEGQTABLES /*519*/:
                    OldStringRecord sr = new OldStringRecord(this.ris);
                    sr.setCodePage(codepage);
                    text.append(sr.getString());
                    text.append('\n');
                    break;
                case 66:
                    codepage = new CodepageRecord(this.ris);
                    break;
                case 133:
                    OldSheetRecord shr = new OldSheetRecord(this.ris);
                    shr.setCodePage(codepage);
                    text.append("Sheet: ");
                    text.append(shr.getSheetname());
                    text.append('\n');
                    break;
                case 515:
                    handleNumericCell(text, new NumberRecord(this.ris).getValue());
                    break;
                case 638:
                    handleNumericCell(text, new RKRecord(this.ris).getRKNumber());
                    break;
                default:
                    this.ris.readFully(new byte[this.ris.remaining()]);
                    break;
            }
        }
        close();
        this.ris = null;
        return text.toString();
    }

    public void close() {
        if (this.toClose != null) {
            IOUtils.closeQuietly(this.toClose);
            this.toClose = null;
        }
    }

    protected void handleNumericCell(StringBuffer text, double value) {
        text.append(value);
        text.append('\n');
    }
}
