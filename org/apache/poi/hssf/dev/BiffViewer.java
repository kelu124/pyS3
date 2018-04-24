package org.apache.poi.hssf.dev;

import com.itextpdf.text.Jpeg;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.lee.ultrascan.service.ProbeMessageID;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.ArrayRecord;
import org.apache.poi.hssf.record.AutoFilterInfoRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BackupRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BookBoolRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BottomMarginRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.CFHeader12Record;
import org.apache.poi.hssf.record.CFHeaderRecord;
import org.apache.poi.hssf.record.CFRule12Record;
import org.apache.poi.hssf.record.CFRuleRecord;
import org.apache.poi.hssf.record.CalcCountRecord;
import org.apache.poi.hssf.record.CalcModeRecord;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.ColumnInfoRecord;
import org.apache.poi.hssf.record.ContinueRecord;
import org.apache.poi.hssf.record.CountryRecord;
import org.apache.poi.hssf.record.DBCellRecord;
import org.apache.poi.hssf.record.DConRefRecord;
import org.apache.poi.hssf.record.DSFRecord;
import org.apache.poi.hssf.record.DVALRecord;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.DateWindow1904Record;
import org.apache.poi.hssf.record.DefaultColWidthRecord;
import org.apache.poi.hssf.record.DefaultRowHeightRecord;
import org.apache.poi.hssf.record.DeltaRecord;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.DrawingRecordForBiffViewer;
import org.apache.poi.hssf.record.DrawingSelectionRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.ExtSSTRecord;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.ExternSheetRecord;
import org.apache.poi.hssf.record.ExternalNameRecord;
import org.apache.poi.hssf.record.FeatHdrRecord;
import org.apache.poi.hssf.record.FeatRecord;
import org.apache.poi.hssf.record.FilePassRecord;
import org.apache.poi.hssf.record.FileSharingRecord;
import org.apache.poi.hssf.record.FnGroupCountRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.FooterRecord;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.GridsetRecord;
import org.apache.poi.hssf.record.GutsRecord;
import org.apache.poi.hssf.record.HCenterRecord;
import org.apache.poi.hssf.record.HeaderRecord;
import org.apache.poi.hssf.record.HideObjRecord;
import org.apache.poi.hssf.record.HorizontalPageBreakRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.IndexRecord;
import org.apache.poi.hssf.record.InterfaceEndRecord;
import org.apache.poi.hssf.record.InterfaceHdrRecord;
import org.apache.poi.hssf.record.IterationRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.LeftMarginRecord;
import org.apache.poi.hssf.record.MMSRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.MulRKRecord;
import org.apache.poi.hssf.record.NameCommentRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.record.PaneRecord;
import org.apache.poi.hssf.record.PasswordRecord;
import org.apache.poi.hssf.record.PasswordRev4Record;
import org.apache.poi.hssf.record.PrecisionRecord;
import org.apache.poi.hssf.record.PrintGridlinesRecord;
import org.apache.poi.hssf.record.PrintHeadersRecord;
import org.apache.poi.hssf.record.PrintSetupRecord;
import org.apache.poi.hssf.record.ProtectRecord;
import org.apache.poi.hssf.record.ProtectionRev4Record;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.RecalcIdRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.RecordInputStream$LeftoverDataException;
import org.apache.poi.hssf.record.RefModeRecord;
import org.apache.poi.hssf.record.RefreshAllRecord;
import org.apache.poi.hssf.record.RightMarginRecord;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SCLRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.SaveRecalcRecord;
import org.apache.poi.hssf.record.SelectionRecord;
import org.apache.poi.hssf.record.SharedFormulaRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.record.StyleRecord;
import org.apache.poi.hssf.record.SupBookRecord;
import org.apache.poi.hssf.record.TabIdRecord;
import org.apache.poi.hssf.record.TableRecord;
import org.apache.poi.hssf.record.TableStylesRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.hssf.record.TopMarginRecord;
import org.apache.poi.hssf.record.UncalcedRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.apache.poi.hssf.record.VCenterRecord;
import org.apache.poi.hssf.record.VerticalPageBreakRecord;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.record.WindowOneRecord;
import org.apache.poi.hssf.record.WindowProtectRecord;
import org.apache.poi.hssf.record.WindowTwoRecord;
import org.apache.poi.hssf.record.WriteAccessRecord;
import org.apache.poi.hssf.record.WriteProtectRecord;
import org.apache.poi.hssf.record.chart.AreaFormatRecord;
import org.apache.poi.hssf.record.chart.AreaRecord;
import org.apache.poi.hssf.record.chart.AxisLineFormatRecord;
import org.apache.poi.hssf.record.chart.AxisOptionsRecord;
import org.apache.poi.hssf.record.chart.AxisParentRecord;
import org.apache.poi.hssf.record.chart.AxisRecord;
import org.apache.poi.hssf.record.chart.AxisUsedRecord;
import org.apache.poi.hssf.record.chart.BarRecord;
import org.apache.poi.hssf.record.chart.BeginRecord;
import org.apache.poi.hssf.record.chart.CatLabRecord;
import org.apache.poi.hssf.record.chart.CategorySeriesAxisRecord;
import org.apache.poi.hssf.record.chart.ChartEndBlockRecord;
import org.apache.poi.hssf.record.chart.ChartEndObjectRecord;
import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;
import org.apache.poi.hssf.record.chart.ChartFormatRecord;
import org.apache.poi.hssf.record.chart.ChartRecord;
import org.apache.poi.hssf.record.chart.ChartStartBlockRecord;
import org.apache.poi.hssf.record.chart.ChartStartObjectRecord;
import org.apache.poi.hssf.record.chart.DatRecord;
import org.apache.poi.hssf.record.chart.DataFormatRecord;
import org.apache.poi.hssf.record.chart.DefaultDataLabelTextPropertiesRecord;
import org.apache.poi.hssf.record.chart.EndRecord;
import org.apache.poi.hssf.record.chart.FontBasisRecord;
import org.apache.poi.hssf.record.chart.FontIndexRecord;
import org.apache.poi.hssf.record.chart.FrameRecord;
import org.apache.poi.hssf.record.chart.LegendRecord;
import org.apache.poi.hssf.record.chart.LineFormatRecord;
import org.apache.poi.hssf.record.chart.LinkedDataRecord;
import org.apache.poi.hssf.record.chart.ObjectLinkRecord;
import org.apache.poi.hssf.record.chart.PlotAreaRecord;
import org.apache.poi.hssf.record.chart.PlotGrowthRecord;
import org.apache.poi.hssf.record.chart.SeriesIndexRecord;
import org.apache.poi.hssf.record.chart.SeriesListRecord;
import org.apache.poi.hssf.record.chart.SeriesRecord;
import org.apache.poi.hssf.record.chart.SeriesTextRecord;
import org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord;
import org.apache.poi.hssf.record.chart.SheetPropertiesRecord;
import org.apache.poi.hssf.record.chart.TextRecord;
import org.apache.poi.hssf.record.chart.TickRecord;
import org.apache.poi.hssf.record.chart.UnitsRecord;
import org.apache.poi.hssf.record.chart.ValueRangeRecord;
import org.apache.poi.hssf.record.pivottable.DataItemRecord;
import org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.PageItemRecord;
import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord;
import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
import org.apache.poi.hssf.record.pivottable.ViewSourceRecord;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

public final class BiffViewer {
    private static final char[] COLUMN_SEPARATOR = " | ".toCharArray();
    private static final int DUMP_LINE_LEN = 16;
    private static final char[] NEW_LINE_CHARS = System.getProperty("line.separator").toCharArray();
    private static final POILogger logger = POILogFactory.getLogger(BiffViewer.class);

    private static final class BiffDumpingStream extends InputStream {
        private int _currentPos = 0;
        private int _currentSize = 0;
        private final byte[] _data = new byte[8228];
        private boolean _innerHasReachedEOF;
        private final DataInputStream _is;
        private final IBiffRecordListener _listener;
        private int _overallStreamPos = 0;
        private int _recordCounter = 0;

        public BiffDumpingStream(InputStream is, IBiffRecordListener listener) {
            this._is = new DataInputStream(is);
            this._listener = listener;
        }

        public int read() throws IOException {
            if (this._currentPos >= this._currentSize) {
                fillNextBuffer();
            }
            if (this._currentPos >= this._currentSize) {
                return -1;
            }
            int result = this._data[this._currentPos] & 255;
            this._currentPos++;
            this._overallStreamPos++;
            formatBufferIfAtEndOfRec();
            return result;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (this._currentPos >= this._currentSize) {
                fillNextBuffer();
            }
            if (this._currentPos >= this._currentSize) {
                return -1;
            }
            int result;
            int availSize = this._currentSize - this._currentPos;
            if (len > availSize) {
                System.err.println("Unexpected request to read past end of current biff record");
                result = availSize;
            } else {
                result = len;
            }
            System.arraycopy(this._data, this._currentPos, b, off, result);
            this._currentPos += result;
            this._overallStreamPos += result;
            formatBufferIfAtEndOfRec();
            return result;
        }

        public int available() throws IOException {
            return (this._currentSize - this._currentPos) + this._is.available();
        }

        private void fillNextBuffer() throws IOException {
            if (!this._innerHasReachedEOF) {
                int b0 = this._is.read();
                if (b0 == -1) {
                    this._innerHasReachedEOF = true;
                    return;
                }
                this._data[0] = (byte) b0;
                this._is.readFully(this._data, 1, 3);
                int len = LittleEndian.getShort(this._data, 2);
                this._is.readFully(this._data, 4, len);
                this._currentPos = 0;
                this._currentSize = len + 4;
                this._recordCounter++;
            }
        }

        private void formatBufferIfAtEndOfRec() {
            if (this._currentPos == this._currentSize) {
                int dataSize = this._currentSize - 4;
                int globalOffset = this._overallStreamPos - this._currentSize;
                this._listener.processRecord(globalOffset, this._recordCounter, LittleEndian.getShort(this._data, 0), dataSize, this._data);
            }
        }

        public void close() throws IOException {
            this._is.close();
        }
    }

    private interface IBiffRecordListener {
        void processRecord(int i, int i2, int i3, int i4, byte[] bArr);
    }

    private static final class BiffRecordListener implements IBiffRecordListener {
        private List<String> _headers = new ArrayList();
        private final Writer _hexDumpWriter;
        private final boolean _noHeader;
        private final boolean _zeroAlignEachRecord;

        public BiffRecordListener(Writer hexDumpWriter, boolean zeroAlignEachRecord, boolean noHeader) {
            this._hexDumpWriter = hexDumpWriter;
            this._zeroAlignEachRecord = zeroAlignEachRecord;
            this._noHeader = noHeader;
        }

        public void processRecord(int globalOffset, int recordCounter, int sid, int dataSize, byte[] data) {
            String header = formatRecordDetails(globalOffset, sid, dataSize, recordCounter);
            if (!this._noHeader) {
                this._headers.add(header);
            }
            Writer w = this._hexDumpWriter;
            if (w != null) {
                try {
                    w.write(header);
                    w.write(BiffViewer.NEW_LINE_CHARS);
                    BiffViewer.hexDumpAligned(w, data, dataSize + 4, globalOffset, this._zeroAlignEachRecord);
                    w.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public List<String> getRecentHeaders() {
            List<String> result = this._headers;
            this._headers = new ArrayList();
            return result;
        }

        private static String formatRecordDetails(int globalOffset, int sid, int size, int recordCounter) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("Offset=").append(HexDump.intToHex(globalOffset)).append("(").append(globalOffset).append(")");
            sb.append(" recno=").append(recordCounter);
            sb.append(" sid=").append(HexDump.shortToHex(sid));
            sb.append(" size=").append(HexDump.shortToHex(size)).append("(").append(size).append(")");
            return sb.toString();
        }
    }

    private static final class CommandArgs {
        private final boolean _biffhex;
        private final File _file;
        private final boolean _noHeader;
        private final boolean _noint;
        private final boolean _out;
        private final boolean _rawhex;

        private CommandArgs(boolean biffhex, boolean noint, boolean out, boolean rawhex, boolean noHeader, File file) {
            this._biffhex = biffhex;
            this._noint = noint;
            this._out = out;
            this._rawhex = rawhex;
            this._file = file;
            this._noHeader = noHeader;
        }

        public static CommandArgs parse(String[] args) throws CommandParseException {
            int nArgs = args.length;
            boolean biffhex = false;
            boolean noint = false;
            boolean out = false;
            boolean rawhex = false;
            boolean noheader = false;
            File file = null;
            for (int i = 0; i < nArgs; i++) {
                String arg = args[i];
                if (!arg.startsWith("--")) {
                    file = new File(arg);
                    if (!file.exists()) {
                        throw new CommandParseException("Specified file '" + arg + "' does not exist");
                    } else if (i + 1 < nArgs) {
                        throw new CommandParseException("File name must be the last arg");
                    }
                } else if ("--biffhex".equals(arg)) {
                    biffhex = true;
                } else if ("--noint".equals(arg)) {
                    noint = true;
                } else if ("--out".equals(arg)) {
                    out = true;
                } else if ("--escher".equals(arg)) {
                    System.setProperty("poi.deserialize.escher", PdfBoolean.TRUE);
                } else if ("--rawhex".equals(arg)) {
                    rawhex = true;
                } else if ("--noheader".equals(arg)) {
                    noheader = true;
                } else {
                    throw new CommandParseException("Unexpected option '" + arg + "'");
                }
            }
            if (file != null) {
                return new CommandArgs(biffhex, noint, out, rawhex, noheader, file);
            }
            throw new CommandParseException("Biff viewer needs a filename");
        }

        public boolean shouldDumpBiffHex() {
            return this._biffhex;
        }

        public boolean shouldDumpRecordInterpretations() {
            return !this._noint;
        }

        public boolean shouldOutputToFile() {
            return this._out;
        }

        public boolean shouldOutputRawHexOnly() {
            return this._rawhex;
        }

        public boolean suppressHeader() {
            return this._noHeader;
        }

        public File getFile() {
            return this._file;
        }
    }

    private static final class CommandParseException extends Exception {
        public CommandParseException(String msg) {
            super(msg);
        }
    }

    private BiffViewer() {
    }

    public static Record[] createRecords(InputStream is, PrintWriter ps, BiffRecordListener recListener, boolean dumpInterpretedRecords) throws RecordFormatException {
        List<Record> temp = new ArrayList();
        RecordInputStream recStream = new RecordInputStream(is);
        while (true) {
            boolean hasNext;
            try {
                hasNext = recStream.hasNextRecord();
            } catch (RecordInputStream$LeftoverDataException e) {
                logger.log(7, new Object[]{"Discarding " + recStream.remaining() + " bytes and continuing", e});
                recStream.readRemainder();
                hasNext = recStream.hasNextRecord();
            }
            if (hasNext) {
                recStream.nextRecord();
                if (recStream.getSid() != (short) 0) {
                    if (dumpInterpretedRecords) {
                        Record record = createRecord(recStream);
                        if (record.getSid() != (short) 60) {
                            temp.add(record);
                            if (dumpInterpretedRecords) {
                                for (String header : recListener.getRecentHeaders()) {
                                    ps.println(header);
                                }
                                ps.print(record.toString());
                            }
                        }
                    } else {
                        recStream.readRemainder();
                    }
                    ps.println();
                }
            } else {
                Record[] result = new Record[temp.size()];
                temp.toArray(result);
                return result;
            }
        }
    }

    private static Record createRecord(RecordInputStream in) {
        switch (in.getSid()) {
            case (short) 6:
                return new FormulaRecord(in);
            case (short) 10:
                return new EOFRecord(in);
            case (short) 12:
                return new CalcCountRecord(in);
            case (short) 13:
                return new CalcModeRecord(in);
            case (short) 14:
                return new PrecisionRecord(in);
            case (short) 15:
                return new RefModeRecord(in);
            case (short) 16:
                return new DeltaRecord(in);
            case (short) 17:
                return new IterationRecord(in);
            case (short) 18:
                return new ProtectRecord(in);
            case (short) 19:
                return new PasswordRecord(in);
            case (short) 20:
                return new HeaderRecord(in);
            case (short) 21:
                return new FooterRecord(in);
            case (short) 23:
                return new ExternSheetRecord(in);
            case (short) 24:
                return new NameRecord(in);
            case (short) 25:
                return new WindowProtectRecord(in);
            case (short) 26:
                return new VerticalPageBreakRecord(in);
            case (short) 27:
                return new HorizontalPageBreakRecord(in);
            case (short) 28:
                return new NoteRecord(in);
            case (short) 29:
                return new SelectionRecord(in);
            case (short) 34:
                return new DateWindow1904Record(in);
            case (short) 35:
                return new ExternalNameRecord(in);
            case (short) 38:
                return new LeftMarginRecord(in);
            case (short) 39:
                return new RightMarginRecord(in);
            case (short) 40:
                return new TopMarginRecord(in);
            case (short) 41:
                return new BottomMarginRecord(in);
            case (short) 42:
                return new PrintHeadersRecord(in);
            case (short) 43:
                return new PrintGridlinesRecord(in);
            case (short) 47:
                return new FilePassRecord(in);
            case (short) 49:
                return new FontRecord(in);
            case (short) 60:
                return new ContinueRecord(in);
            case (short) 61:
                return new WindowOneRecord(in);
            case (short) 64:
                return new BackupRecord(in);
            case (short) 65:
                return new PaneRecord(in);
            case (short) 66:
                return new CodepageRecord(in);
            case (short) 81:
                return new DConRefRecord(in);
            case (short) 85:
                return new DefaultColWidthRecord(in);
            case (short) 91:
                return new FileSharingRecord(in);
            case (short) 92:
                return new WriteAccessRecord(in);
            case (short) 93:
                return new ObjRecord(in);
            case (short) 94:
                return new UncalcedRecord(in);
            case (short) 95:
                return new SaveRecalcRecord(in);
            case (short) 125:
                return new ColumnInfoRecord(in);
            case (short) 128:
                return new GutsRecord(in);
            case (short) 129:
                return new WSBoolRecord(in);
            case (short) 130:
                return new GridsetRecord(in);
            case (short) 131:
                return new HCenterRecord(in);
            case (short) 132:
                return new VCenterRecord(in);
            case (short) 133:
                return new BoundSheetRecord(in);
            case (short) 134:
                return new WriteProtectRecord(in);
            case (short) 140:
                return new CountryRecord(in);
            case (short) 141:
                return new HideObjRecord(in);
            case (short) 146:
                return new PaletteRecord(in);
            case (short) 156:
                return new FnGroupCountRecord(in);
            case (short) 157:
                return new AutoFilterInfoRecord(in);
            case (short) 160:
                return new SCLRecord(in);
            case (short) 161:
                return new PrintSetupRecord(in);
            case (short) 176:
                return new ViewDefinitionRecord(in);
            case (short) 177:
                return new ViewFieldsRecord(in);
            case (short) 182:
                return new PageItemRecord(in);
            case (short) 189:
                return new MulRKRecord(in);
            case (short) 190:
                return new MulBlankRecord(in);
            case HSSFShapeTypes.ActionButtonForwardNext /*193*/:
                return new MMSRecord(in);
            case HSSFShapeTypes.ActionButtonReturn /*197*/:
                return new DataItemRecord(in);
            case (short) 213:
                return new StreamIDRecord(in);
            case (short) 215:
                return new DBCellRecord(in);
            case (short) 218:
                return new BookBoolRecord(in);
            case (short) 224:
                return new ExtendedFormatRecord(in);
            case (short) 225:
                return new InterfaceHdrRecord(in);
            case Jpeg.M_APP2 /*226*/:
                return InterfaceEndRecord.create(in);
            case (short) 227:
                return new ViewSourceRecord(in);
            case (short) 229:
                return new MergeCellsRecord(in);
            case (short) 235:
                return new DrawingGroupRecord(in);
            case (short) 236:
                return new DrawingRecordForBiffViewer(in);
            case Jpeg.M_APPD /*237*/:
                return new DrawingSelectionRecord(in);
            case (short) 252:
                return new SSTRecord(in);
            case (short) 253:
                return new LabelSSTRecord(in);
            case (short) 255:
                return new ExtSSTRecord(in);
            case (short) 256:
                return new ExtendedPivotTableViewFieldsRecord(in);
            case (short) 317:
                return new TabIdRecord(in);
            case (short) 352:
                return new UseSelFSRecord(in);
            case dc1394.DC1394_COLOR_CODING_YUV411 /*353*/:
                return new DSFRecord(in);
            case (short) 430:
                return new SupBookRecord(in);
            case (short) 431:
                return new ProtectionRev4Record(in);
            case (short) 432:
                return new CFHeaderRecord(in);
            case (short) 433:
                return new CFRuleRecord(in);
            case (short) 434:
                return new DVALRecord(in);
            case (short) 438:
                return new TextObjectRecord(in);
            case opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_X /*439*/:
                return new RefreshAllRecord(in);
            case opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y /*440*/:
                return new HyperlinkRecord(in);
            case (short) 444:
                return new PasswordRev4Record(in);
            case (short) 446:
                return new DVRecord(in);
            case opencv_videoio.CV_CAP_PROP_XI_WB_KG /*449*/:
                return new RecalcIdRecord(in);
            case (short) 512:
                return new DimensionsRecord(in);
            case (short) 513:
                return new BlankRecord(in);
            case (short) 515:
                return new NumberRecord(in);
            case opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH /*516*/:
                return new LabelRecord(in);
            case (short) 517:
                return new BoolErrRecord(in);
            case TIFFConstants.TIFFTAG_JPEGQTABLES /*519*/:
                return new StringRecord(in);
            case TIFFConstants.TIFFTAG_JPEGDCTABLES /*520*/:
                return new RowRecord(in);
            case MetaDo.META_SETWINDOWORG /*523*/:
                return new IndexRecord(in);
            case (short) 545:
                return new ArrayRecord(in);
            case (short) 549:
                return new DefaultRowHeightRecord(in);
            case (short) 566:
                return new TableRecord(in);
            case (short) 574:
                return new WindowTwoRecord(in);
            case (short) 638:
                return new RKRecord(in);
            case (short) 659:
                return new StyleRecord(in);
            case (short) 1054:
                return new FormatRecord(in);
            case (short) 1212:
                return new SharedFormulaRecord(in);
            case (short) 2057:
                return new BOFRecord(in);
            case (short) 2128:
                return new ChartFRTInfoRecord(in);
            case (short) 2130:
                return new ChartStartBlockRecord(in);
            case (short) 2131:
                return new ChartEndBlockRecord(in);
            case (short) 2132:
                return new ChartStartObjectRecord(in);
            case (short) 2133:
                return new ChartEndObjectRecord(in);
            case (short) 2134:
                return new CatLabRecord(in);
            case UnknownRecord.SHEETPROTECTION_0867 /*2151*/:
                return new FeatHdrRecord(in);
            case (short) 2152:
                return new FeatRecord(in);
            case (short) 2169:
                return new CFHeader12Record(in);
            case AVCodecContext.FF_PROFILE_H264_HIGH_422_INTRA /*2170*/:
                return new CFRule12Record(in);
            case (short) 2190:
                return new TableStylesRecord(in);
            case (short) 2196:
                return new NameCommentRecord(in);
            case (short) 4097:
                return new UnitsRecord(in);
            case (short) 4098:
                return new ChartRecord(in);
            case (short) 4099:
                return new SeriesRecord(in);
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_SPEED /*4102*/:
                return new DataFormatRecord(in);
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_STEPS /*4103*/:
                return new LineFormatRecord(in);
            case ProbeMessageID.ID_SET_FMOTOR_MOVE_PDIR_SPEED /*4106*/:
                return new AreaFormatRecord(in);
            case ProbeMessageID.ID_MOVE_FMOTOR /*4109*/:
                return new SeriesTextRecord(in);
            case (short) 4116:
                return new ChartFormatRecord(in);
            case (short) 4117:
                return new LegendRecord(in);
            case (short) 4118:
                return new SeriesListRecord(in);
            case (short) 4119:
                return new BarRecord(in);
            case (short) 4122:
                return new AreaRecord(in);
            case (short) 4125:
                return new AxisRecord(in);
            case (short) 4126:
                return new TickRecord(in);
            case (short) 4127:
                return new ValueRangeRecord(in);
            case (short) 4128:
                return new CategorySeriesAxisRecord(in);
            case (short) 4129:
                return new AxisLineFormatRecord(in);
            case (short) 4132:
                return new DefaultDataLabelTextPropertiesRecord(in);
            case (short) 4133:
                return new TextRecord(in);
            case (short) 4134:
                return new FontIndexRecord(in);
            case (short) 4135:
                return new ObjectLinkRecord(in);
            case (short) 4146:
                return new FrameRecord(in);
            case (short) 4147:
                return new BeginRecord(in);
            case (short) 4148:
                return new EndRecord(in);
            case (short) 4149:
                return new PlotAreaRecord(in);
            case (short) 4161:
                return new AxisParentRecord(in);
            case (short) 4164:
                return new SheetPropertiesRecord(in);
            case (short) 4165:
                return new SeriesToChartGroupRecord(in);
            case (short) 4166:
                return new AxisUsedRecord(in);
            case (short) 4177:
                return new LinkedDataRecord(in);
            case (short) 4192:
                return new FontBasisRecord(in);
            case (short) 4194:
                return new AxisOptionsRecord(in);
            case (short) 4195:
                return new DatRecord(in);
            case (short) 4196:
                return new PlotGrowthRecord(in);
            case (short) 4197:
                return new SeriesIndexRecord(in);
            default:
                return new UnknownRecord(in);
        }
    }

    public static void main(String[] args) throws IOException, CommandParseException {
        PrintWriter pw;
        CommandArgs cmdArgs = CommandArgs.parse(args);
        if (cmdArgs.shouldOutputToFile()) {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(cmdArgs.getFile().getAbsolutePath() + ".out"), StringUtil.UTF8));
        } else {
            pw = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        }
        try {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(cmdArgs.getFile(), true);
            InputStream is;
            try {
                is = getPOIFSInputStream(fs);
                if (cmdArgs.shouldOutputRawHexOnly()) {
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    HexDump.dump(data, 0, System.out, 0);
                } else {
                    boolean dumpInterpretedRecords = cmdArgs.shouldDumpRecordInterpretations();
                    runBiffViewer(pw, is, dumpInterpretedRecords, cmdArgs.shouldDumpBiffHex(), dumpInterpretedRecords, cmdArgs.suppressHeader());
                }
                is.close();
                fs.close();
            } catch (Throwable th) {
                fs.close();
            }
        } finally {
            pw.close();
        }
    }

    protected static InputStream getPOIFSInputStream(NPOIFSFileSystem fs) throws IOException, FileNotFoundException {
        return fs.createDocumentInputStream(HSSFWorkbook.getWorkbookDirEntryName(fs.getRoot()));
    }

    protected static void runBiffViewer(PrintWriter pw, InputStream is, boolean dumpInterpretedRecords, boolean dumpHex, boolean zeroAlignHexDump, boolean suppressHeader) {
        BiffRecordListener recListener = new BiffRecordListener(dumpHex ? pw : null, zeroAlignHexDump, suppressHeader);
        createRecords(new BiffDumpingStream(is, recListener), pw, recListener, dumpInterpretedRecords);
    }

    static void hexDumpAligned(Writer w, byte[] data, int dumpLen, int globalOffset, boolean zeroAlignEachRecord) {
        int endLineAddr;
        int startLineAddr;
        int globalStart = globalOffset + 0;
        int globalEnd = (globalOffset + 0) + dumpLen;
        int startDelta = globalStart % 16;
        int endDelta = globalEnd % 16;
        if (zeroAlignEachRecord) {
            endDelta -= startDelta;
            if (endDelta < 0) {
                endDelta += 16;
            }
            startDelta = 0;
        }
        if (zeroAlignEachRecord) {
            endLineAddr = (globalEnd - endDelta) - (globalStart - startDelta);
            startLineAddr = 0;
        } else {
            startLineAddr = globalStart - startDelta;
            endLineAddr = globalEnd - endDelta;
        }
        int lineDataOffset = 0 - startDelta;
        int lineAddr = startLineAddr;
        if (startLineAddr == endLineAddr) {
            hexDumpLine(w, data, lineAddr, lineDataOffset, startDelta, endDelta);
            return;
        }
        hexDumpLine(w, data, lineAddr, lineDataOffset, startDelta, 16);
        while (true) {
            lineAddr += 16;
            lineDataOffset += 16;
            if (lineAddr >= endLineAddr) {
                break;
            }
            hexDumpLine(w, data, lineAddr, lineDataOffset, 0, 16);
        }
        if (endDelta != 0) {
            hexDumpLine(w, data, lineAddr, lineDataOffset, 0, endDelta);
        }
    }

    private static void hexDumpLine(Writer w, byte[] data, int lineStartAddress, int lineDataOffset, int startDelta, int endDelta) {
        IOException e;
        char[] buf = new char[((((((COLUMN_SEPARATOR.length * 2) + 8) + 48) - 1) + 16) + NEW_LINE_CHARS.length)];
        if (startDelta >= endDelta) {
            throw new IllegalArgumentException("Bad start/end delta");
        }
        try {
            int idx;
            writeHex(buf, 0, lineStartAddress, 8);
            int i = 0;
            int idx2 = arraycopy(COLUMN_SEPARATOR, buf, 8);
            while (i < 16) {
                if (i > 0) {
                    idx = idx2 + 1;
                    buf[idx2] = ' ';
                } else {
                    idx = idx2;
                }
                if (i < startDelta || i >= endDelta) {
                    buf[idx] = ' ';
                    buf[idx + 1] = ' ';
                } else {
                    writeHex(buf, idx, data[lineDataOffset + i], 2);
                }
                i++;
                idx2 = idx + 2;
            }
            try {
                i = 0;
                idx2 = arraycopy(COLUMN_SEPARATOR, buf, idx2);
                while (i < 16) {
                    char ch = ' ';
                    if (i >= startDelta && i < endDelta) {
                        ch = getPrintableChar(data[lineDataOffset + i]);
                    }
                    idx = idx2 + 1;
                    buf[idx2] = ch;
                    i++;
                    idx2 = idx;
                }
                w.write(buf, 0, arraycopy(NEW_LINE_CHARS, buf, idx2));
            } catch (IOException e2) {
                e = e2;
                idx = idx2;
                throw new RuntimeException(e);
            }
        } catch (IOException e3) {
            e = e3;
        }
    }

    private static int arraycopy(char[] in, char[] out, int pos) {
        int idx = pos;
        char[] arr$ = in;
        int len$ = arr$.length;
        int i$ = 0;
        int idx2 = idx;
        while (i$ < len$) {
            idx = idx2 + 1;
            out[idx2] = arr$[i$];
            i$++;
            idx2 = idx;
        }
        return idx2;
    }

    private static char getPrintableChar(byte b) {
        char ib = (char) (b & 255);
        if (ib < ' ' || ib > '~') {
            return '.';
        }
        return ib;
    }

    private static void writeHex(char[] buf, int startInBuf, int value, int nDigits) throws IOException {
        int acc = value;
        for (int i = nDigits - 1; i >= 0; i--) {
            int digit = acc & 15;
            buf[startInBuf + i] = (char) (digit < 10 ? digit + 48 : (digit + 65) - 10);
            acc >>>= 4;
        }
    }
}
