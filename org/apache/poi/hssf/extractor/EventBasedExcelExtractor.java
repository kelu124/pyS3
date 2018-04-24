package org.apache.poi.hssf.extractor;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.POIDocument;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.bytedeco.javacpp.opencv_videoio;

public class EventBasedExcelExtractor extends POIOLE2TextExtractor implements ExcelExtractor {
    private DirectoryNode _dir;
    boolean _formulasNotResults;
    boolean _includeSheetNames;

    private class TextListener implements HSSFListener {
        FormatTrackingHSSFListener _ft;
        final StringBuffer _text = new StringBuffer();
        private int nextRow = -1;
        private boolean outputNextStringValue = false;
        private int rowNum;
        private final List<String> sheetNames = new ArrayList();
        private int sheetNum = -1;
        private SSTRecord sstRecord;

        public void processRecord(Record record) {
            String thisText = null;
            int thisRow = -1;
            switch (record.getSid()) {
                case (short) 6:
                    FormulaRecord frec = (FormulaRecord) record;
                    thisRow = frec.getRow();
                    if (!EventBasedExcelExtractor.this._formulasNotResults) {
                        if (!frec.hasCachedResultString()) {
                            thisText = this._ft.formatNumberDateCell(frec);
                            break;
                        }
                        this.outputNextStringValue = true;
                        this.nextRow = frec.getRow();
                        break;
                    }
                    thisText = HSSFFormulaParser.toFormulaString((HSSFWorkbook) null, frec.getParsedExpression());
                    break;
                case (short) 28:
                    thisRow = ((NoteRecord) record).getRow();
                    break;
                case (short) 133:
                    this.sheetNames.add(((BoundSheetRecord) record).getSheetname());
                    break;
                case (short) 252:
                    this.sstRecord = (SSTRecord) record;
                    break;
                case (short) 253:
                    LabelSSTRecord lsrec = (LabelSSTRecord) record;
                    thisRow = lsrec.getRow();
                    if (this.sstRecord != null) {
                        thisText = this.sstRecord.getString(lsrec.getSSTIndex()).toString();
                        break;
                    }
                    throw new IllegalStateException("No SST record found");
                case (short) 515:
                    NumberRecord numrec = (NumberRecord) record;
                    thisRow = numrec.getRow();
                    thisText = this._ft.formatNumberDateCell(numrec);
                    break;
                case opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH /*516*/:
                    LabelRecord lrec = (LabelRecord) record;
                    thisRow = lrec.getRow();
                    thisText = lrec.getValue();
                    break;
                case TIFFConstants.TIFFTAG_JPEGQTABLES /*519*/:
                    if (this.outputNextStringValue) {
                        thisText = ((StringRecord) record).getString();
                        thisRow = this.nextRow;
                        this.outputNextStringValue = false;
                        break;
                    }
                    break;
                case (short) 2057:
                    if (((BOFRecord) record).getType() == 16) {
                        this.sheetNum++;
                        this.rowNum = -1;
                        if (EventBasedExcelExtractor.this._includeSheetNames) {
                            if (this._text.length() > 0) {
                                this._text.append("\n");
                            }
                            this._text.append((String) this.sheetNames.get(this.sheetNum));
                            break;
                        }
                    }
                    break;
            }
            if (thisText != null) {
                if (thisRow != this.rowNum) {
                    this.rowNum = thisRow;
                    if (this._text.length() > 0) {
                        this._text.append("\n");
                    }
                } else {
                    this._text.append("\t");
                }
                this._text.append(thisText);
            }
        }
    }

    public EventBasedExcelExtractor(DirectoryNode dir) {
        super((POIDocument) null);
        this._includeSheetNames = true;
        this._formulasNotResults = false;
        this._dir = dir;
    }

    public EventBasedExcelExtractor(POIFSFileSystem fs) {
        this(fs.getRoot());
        super.setFilesystem(fs);
    }

    public DocumentSummaryInformation getDocSummaryInformation() {
        throw new IllegalStateException("Metadata extraction not supported in streaming mode, please use ExcelExtractor");
    }

    public SummaryInformation getSummaryInformation() {
        throw new IllegalStateException("Metadata extraction not supported in streaming mode, please use ExcelExtractor");
    }

    public void setIncludeCellComments(boolean includeComments) {
        throw new IllegalStateException("Comment extraction not supported in streaming mode, please use ExcelExtractor");
    }

    public void setIncludeHeadersFooters(boolean includeHeadersFooters) {
        throw new IllegalStateException("Header/Footer extraction not supported in streaming mode, please use ExcelExtractor");
    }

    public void setIncludeSheetNames(boolean includeSheetNames) {
        this._includeSheetNames = includeSheetNames;
    }

    public void setFormulasNotResults(boolean formulasNotResults) {
        this._formulasNotResults = formulasNotResults;
    }

    public String getText() {
        try {
            String text = triggerExtraction()._text.toString();
            if (!text.endsWith("\n")) {
                text = text + "\n";
            }
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TextListener triggerExtraction() throws IOException {
        TextListener tl = new TextListener();
        FormatTrackingHSSFListener ft = new FormatTrackingHSSFListener(tl);
        tl._ft = ft;
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        request.addListenerForAllRecords(ft);
        factory.processWorkbookEvents(request, this._dir);
        return tl;
    }
}
