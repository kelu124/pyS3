package org.apache.poi.hssf.record;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherDgRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherRecordFactory;
import org.apache.poi.ddf.EscherSerializationListener;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherSpgrRecord;
import org.apache.poi.ddf.EscherTextboxRecord;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class EscherAggregate extends AbstractEscherHolderRecord {
    public static final short ST_ACCENTBORDERCALLOUT1 = (short) 50;
    public static final short ST_ACCENTBORDERCALLOUT2 = (short) 51;
    public static final short ST_ACCENTBORDERCALLOUT3 = (short) 52;
    public static final short ST_ACCENTBORDERCALLOUT90 = (short) 181;
    public static final short ST_ACCENTCALLOUT1 = (short) 44;
    public static final short ST_ACCENTCALLOUT2 = (short) 45;
    public static final short ST_ACCENTCALLOUT3 = (short) 46;
    public static final short ST_ACCENTCALLOUT90 = (short) 179;
    public static final short ST_ACTIONBUTTONBACKPREVIOUS = (short) 194;
    public static final short ST_ACTIONBUTTONBEGINNING = (short) 196;
    public static final short ST_ACTIONBUTTONBLANK = (short) 189;
    public static final short ST_ACTIONBUTTONDOCUMENT = (short) 198;
    public static final short ST_ACTIONBUTTONEND = (short) 195;
    public static final short ST_ACTIONBUTTONFORWARDNEXT = (short) 193;
    public static final short ST_ACTIONBUTTONHELP = (short) 191;
    public static final short ST_ACTIONBUTTONHOME = (short) 190;
    public static final short ST_ACTIONBUTTONINFORMATION = (short) 192;
    public static final short ST_ACTIONBUTTONMOVIE = (short) 200;
    public static final short ST_ACTIONBUTTONRETURN = (short) 197;
    public static final short ST_ACTIONBUTTONSOUND = (short) 199;
    public static final short ST_ARC = (short) 19;
    public static final short ST_ARROW = (short) 13;
    public static final short ST_BALLOON = (short) 17;
    public static final short ST_BENTARROW = (short) 91;
    public static final short ST_BENTCONNECTOR2 = (short) 33;
    public static final short ST_BENTCONNECTOR3 = (short) 34;
    public static final short ST_BENTCONNECTOR4 = (short) 35;
    public static final short ST_BENTCONNECTOR5 = (short) 36;
    public static final short ST_BENTUPARROW = (short) 90;
    public static final short ST_BEVEL = (short) 84;
    public static final short ST_BLOCKARC = (short) 95;
    public static final short ST_BORDERCALLOUT1 = (short) 47;
    public static final short ST_BORDERCALLOUT2 = (short) 48;
    public static final short ST_BORDERCALLOUT3 = (short) 49;
    public static final short ST_BORDERCALLOUT90 = (short) 180;
    public static final short ST_BRACEPAIR = (short) 186;
    public static final short ST_BRACKETPAIR = (short) 185;
    public static final short ST_CALLOUT1 = (short) 41;
    public static final short ST_CALLOUT2 = (short) 42;
    public static final short ST_CALLOUT3 = (short) 43;
    public static final short ST_CALLOUT90 = (short) 178;
    public static final short ST_CAN = (short) 22;
    public static final short ST_CHEVRON = (short) 55;
    public static final short ST_CIRCULARARROW = (short) 99;
    public static final short ST_CLOUDCALLOUT = (short) 106;
    public static final short ST_CUBE = (short) 16;
    public static final short ST_CURVEDCONNECTOR2 = (short) 37;
    public static final short ST_CURVEDCONNECTOR3 = (short) 38;
    public static final short ST_CURVEDCONNECTOR4 = (short) 39;
    public static final short ST_CURVEDCONNECTOR5 = (short) 40;
    public static final short ST_CURVEDDOWNARROW = (short) 105;
    public static final short ST_CURVEDLEFTARROW = (short) 103;
    public static final short ST_CURVEDRIGHTARROW = (short) 102;
    public static final short ST_CURVEDUPARROW = (short) 104;
    public static final short ST_DIAMOND = (short) 4;
    public static final short ST_DONUT = (short) 23;
    public static final short ST_DOUBLEWAVE = (short) 188;
    public static final short ST_DOWNARROW = (short) 67;
    public static final short ST_DOWNARROWCALLOUT = (short) 80;
    public static final short ST_ELLIPSE = (short) 3;
    public static final short ST_ELLIPSERIBBON = (short) 107;
    public static final short ST_ELLIPSERIBBON2 = (short) 108;
    public static final short ST_FLOWCHARTALTERNATEPROCESS = (short) 176;
    public static final short ST_FLOWCHARTCOLLATE = (short) 125;
    public static final short ST_FLOWCHARTCONNECTOR = (short) 120;
    public static final short ST_FLOWCHARTDECISION = (short) 110;
    public static final short ST_FLOWCHARTDELAY = (short) 135;
    public static final short ST_FLOWCHARTDISPLAY = (short) 134;
    public static final short ST_FLOWCHARTDOCUMENT = (short) 114;
    public static final short ST_FLOWCHARTEXTRACT = (short) 127;
    public static final short ST_FLOWCHARTINPUTOUTPUT = (short) 111;
    public static final short ST_FLOWCHARTINTERNALSTORAGE = (short) 113;
    public static final short ST_FLOWCHARTMAGNETICDISK = (short) 132;
    public static final short ST_FLOWCHARTMAGNETICDRUM = (short) 133;
    public static final short ST_FLOWCHARTMAGNETICTAPE = (short) 131;
    public static final short ST_FLOWCHARTMANUALINPUT = (short) 118;
    public static final short ST_FLOWCHARTMANUALOPERATION = (short) 119;
    public static final short ST_FLOWCHARTMERGE = (short) 128;
    public static final short ST_FLOWCHARTMULTIDOCUMENT = (short) 115;
    public static final short ST_FLOWCHARTOFFLINESTORAGE = (short) 129;
    public static final short ST_FLOWCHARTOFFPAGECONNECTOR = (short) 177;
    public static final short ST_FLOWCHARTONLINESTORAGE = (short) 130;
    public static final short ST_FLOWCHARTOR = (short) 124;
    public static final short ST_FLOWCHARTPREDEFINEDPROCESS = (short) 112;
    public static final short ST_FLOWCHARTPREPARATION = (short) 117;
    public static final short ST_FLOWCHARTPROCESS = (short) 109;
    public static final short ST_FLOWCHARTPUNCHEDCARD = (short) 121;
    public static final short ST_FLOWCHARTPUNCHEDTAPE = (short) 122;
    public static final short ST_FLOWCHARTSORT = (short) 126;
    public static final short ST_FLOWCHARTSUMMINGJUNCTION = (short) 123;
    public static final short ST_FLOWCHARTTERMINATOR = (short) 116;
    public static final short ST_FOLDEDCORNER = (short) 65;
    public static final short ST_HEART = (short) 74;
    public static final short ST_HEXAGON = (short) 9;
    public static final short ST_HOMEPLATE = (short) 15;
    public static final short ST_HORIZONTALSCROLL = (short) 98;
    public static final short ST_HOSTCONTROL = (short) 201;
    public static final short ST_IRREGULARSEAL1 = (short) 71;
    public static final short ST_IRREGULARSEAL2 = (short) 72;
    public static final short ST_ISOCELESTRIANGLE = (short) 5;
    public static final short ST_LEFTARROW = (short) 66;
    public static final short ST_LEFTARROWCALLOUT = (short) 77;
    public static final short ST_LEFTBRACE = (short) 87;
    public static final short ST_LEFTBRACKET = (short) 85;
    public static final short ST_LEFTRIGHTARROW = (short) 69;
    public static final short ST_LEFTRIGHTARROWCALLOUT = (short) 81;
    public static final short ST_LEFTRIGHTUPARROW = (short) 182;
    public static final short ST_LEFTUPARROW = (short) 89;
    public static final short ST_LIGHTNINGBOLT = (short) 73;
    public static final short ST_LINE = (short) 20;
    public static final short ST_MIN = (short) 0;
    public static final short ST_MOON = (short) 184;
    public static final short ST_NIL = (short) 4095;
    public static final short ST_NOSMOKING = (short) 57;
    public static final short ST_NOTCHEDCIRCULARARROW = (short) 100;
    public static final short ST_NOTCHEDRIGHTARROW = (short) 94;
    public static final short ST_NOT_PRIMATIVE = (short) 0;
    public static final short ST_OCTAGON = (short) 10;
    public static final short ST_PARALLELOGRAM = (short) 7;
    public static final short ST_PENTAGON = (short) 56;
    public static final short ST_PICTUREFRAME = (short) 75;
    public static final short ST_PLAQUE = (short) 21;
    public static final short ST_PLUS = (short) 11;
    public static final short ST_QUADARROW = (short) 76;
    public static final short ST_QUADARROWCALLOUT = (short) 83;
    public static final short ST_RECTANGLE = (short) 1;
    public static final short ST_RIBBON = (short) 53;
    public static final short ST_RIBBON2 = (short) 54;
    public static final short ST_RIGHTARROWCALLOUT = (short) 78;
    public static final short ST_RIGHTBRACE = (short) 88;
    public static final short ST_RIGHTBRACKET = (short) 86;
    public static final short ST_RIGHTTRIANGLE = (short) 6;
    public static final short ST_ROUNDRECTANGLE = (short) 2;
    public static final short ST_SEAL = (short) 18;
    public static final short ST_SEAL16 = (short) 59;
    public static final short ST_SEAL24 = (short) 92;
    public static final short ST_SEAL32 = (short) 60;
    public static final short ST_SEAL4 = (short) 187;
    public static final short ST_SEAL8 = (short) 58;
    public static final short ST_SMILEYFACE = (short) 96;
    public static final short ST_STAR = (short) 12;
    public static final short ST_STRAIGHTCONNECTOR1 = (short) 32;
    public static final short ST_STRIPEDRIGHTARROW = (short) 93;
    public static final short ST_SUN = (short) 183;
    public static final short ST_TEXTARCHDOWNCURVE = (short) 145;
    public static final short ST_TEXTARCHDOWNPOUR = (short) 149;
    public static final short ST_TEXTARCHUPCURVE = (short) 144;
    public static final short ST_TEXTARCHUPPOUR = (short) 148;
    public static final short ST_TEXTBOX = (short) 202;
    public static final short ST_TEXTBUTTONCURVE = (short) 147;
    public static final short ST_TEXTBUTTONPOUR = (short) 151;
    public static final short ST_TEXTCANDOWN = (short) 175;
    public static final short ST_TEXTCANUP = (short) 174;
    public static final short ST_TEXTCASCADEDOWN = (short) 155;
    public static final short ST_TEXTCASCADEUP = (short) 154;
    public static final short ST_TEXTCHEVRON = (short) 140;
    public static final short ST_TEXTCHEVRONINVERTED = (short) 141;
    public static final short ST_TEXTCIRCLECURVE = (short) 146;
    public static final short ST_TEXTCIRCLEPOUR = (short) 150;
    public static final short ST_TEXTCURVE = (short) 27;
    public static final short ST_TEXTCURVEDOWN = (short) 153;
    public static final short ST_TEXTCURVEUP = (short) 152;
    public static final short ST_TEXTDEFLATE = (short) 161;
    public static final short ST_TEXTDEFLATEBOTTOM = (short) 163;
    public static final short ST_TEXTDEFLATEINFLATE = (short) 166;
    public static final short ST_TEXTDEFLATEINFLATEDEFLATE = (short) 167;
    public static final short ST_TEXTDEFLATETOP = (short) 165;
    public static final short ST_TEXTFADEDOWN = (short) 171;
    public static final short ST_TEXTFADELEFT = (short) 169;
    public static final short ST_TEXTFADERIGHT = (short) 168;
    public static final short ST_TEXTFADEUP = (short) 170;
    public static final short ST_TEXTHEXAGON = (short) 26;
    public static final short ST_TEXTINFLATE = (short) 160;
    public static final short ST_TEXTINFLATEBOTTOM = (short) 162;
    public static final short ST_TEXTINFLATETOP = (short) 164;
    public static final short ST_TEXTOCTAGON = (short) 25;
    public static final short ST_TEXTONCURVE = (short) 30;
    public static final short ST_TEXTONRING = (short) 31;
    public static final short ST_TEXTPLAINTEXT = (short) 136;
    public static final short ST_TEXTRING = (short) 29;
    public static final short ST_TEXTRINGINSIDE = (short) 142;
    public static final short ST_TEXTRINGOUTSIDE = (short) 143;
    public static final short ST_TEXTSIMPLE = (short) 24;
    public static final short ST_TEXTSLANTDOWN = (short) 173;
    public static final short ST_TEXTSLANTUP = (short) 172;
    public static final short ST_TEXTSTOP = (short) 137;
    public static final short ST_TEXTTRIANGLE = (short) 138;
    public static final short ST_TEXTTRIANGLEINVERTED = (short) 139;
    public static final short ST_TEXTWAVE = (short) 28;
    public static final short ST_TEXTWAVE1 = (short) 156;
    public static final short ST_TEXTWAVE2 = (short) 157;
    public static final short ST_TEXTWAVE3 = (short) 158;
    public static final short ST_TEXTWAVE4 = (short) 159;
    public static final short ST_THICKARROW = (short) 14;
    public static final short ST_TRAPEZOID = (short) 8;
    public static final short ST_UPARROW = (short) 68;
    public static final short ST_UPARROWCALLOUT = (short) 79;
    public static final short ST_UPDOWNARROW = (short) 70;
    public static final short ST_UPDOWNARROWCALLOUT = (short) 82;
    public static final short ST_UTURNARROW = (short) 101;
    public static final short ST_VERTICALSCROLL = (short) 97;
    public static final short ST_WAVE = (short) 64;
    public static final short ST_WEDGEELLIPSECALLOUT = (short) 63;
    public static final short ST_WEDGERECTCALLOUT = (short) 61;
    public static final short ST_WEDGERRECTCALLOUT = (short) 62;
    private static POILogger log = POILogFactory.getLogger(EscherAggregate.class);
    public static final short sid = (short) 9876;
    private final Map<EscherRecord, Record> shapeToObj = new HashMap();
    private final Map<Integer, NoteRecord> tailRec = new LinkedHashMap();

    public EscherAggregate(boolean createDefaultTree) {
        if (createDefaultTree) {
            buildBaseTree();
        }
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        String nl = System.getProperty("line.separtor");
        StringBuilder result = new StringBuilder();
        result.append('[').append(getRecordName()).append(']').append(nl);
        for (EscherRecord escherRecord : getEscherRecords()) {
            result.append(escherRecord.toString());
        }
        result.append("[/").append(getRecordName()).append(']').append(nl);
        return result.toString();
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getRecordName()).append(">\n");
        for (EscherRecord escherRecord : getEscherRecords()) {
            builder.append(escherRecord.toXml(tab + "\t"));
        }
        builder.append(tab).append("</").append(getRecordName()).append(">\n");
        return builder.toString();
    }

    private static boolean isDrawingLayerRecord(short sid) {
        return sid == (short) 236 || sid == (short) 60 || sid == (short) 93 || sid == TextObjectRecord.sid;
    }

    public static EscherAggregate createAggregate(List<RecordBase> records, int locFirstDrawingRecord) {
        final List<EscherRecord> shapeRecords = new ArrayList();
        EscherRecordFactory recordFactory = new DefaultEscherRecordFactory() {
            public EscherRecord createRecord(byte[] data, int offset) {
                EscherRecord r = super.createRecord(data, offset);
                if (r.getRecordId() == EscherClientDataRecord.RECORD_ID || r.getRecordId() == EscherTextboxRecord.RECORD_ID) {
                    shapeRecords.add(r);
                }
                return r;
            }
        };
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        EscherAggregate agg = new EscherAggregate(false);
        int loc = locFirstDrawingRecord;
        while (loc + 1 < records.size() && isDrawingLayerRecord(sid(records, loc))) {
            try {
                if (sid(records, loc) == (short) 236 || sid(records, loc) == (short) 60) {
                    if (sid(records, loc) == (short) 236) {
                        buffer.write(((DrawingRecord) records.get(loc)).getRecordData());
                    } else {
                        buffer.write(((ContinueRecord) records.get(loc)).getData());
                    }
                    loc++;
                } else {
                    loc++;
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't get data from drawing/continue records", e);
            }
        }
        int pos = 0;
        while (pos < buffer.size()) {
            EscherRecord r = recordFactory.createRecord(buffer.toByteArray(), pos);
            int bytesRead = r.fillFields(buffer.toByteArray(), pos, recordFactory);
            agg.addEscherRecord(r);
            pos += bytesRead;
        }
        loc = locFirstDrawingRecord + 1;
        int shapeIndex = 0;
        while (loc < records.size() && isDrawingLayerRecord(sid(records, loc))) {
            if (isObjectRecord(records, loc)) {
                int shapeIndex2 = shapeIndex + 1;
                agg.shapeToObj.put(shapeRecords.get(shapeIndex), (Record) records.get(loc));
                loc++;
                shapeIndex = shapeIndex2;
            } else {
                loc++;
            }
        }
        while (loc < records.size() && sid(records, loc) == (short) 28) {
            NoteRecord r2 = (NoteRecord) records.get(loc);
            agg.tailRec.put(Integer.valueOf(r2.getShapeId()), r2);
            loc++;
        }
        records.subList(locFirstDrawingRecord, loc).clear();
        records.add(locFirstDrawingRecord, agg);
        return agg;
    }

    public int serialize(int offset, byte[] data) {
        List<EscherRecord> records = getEscherRecords();
        byte[] buffer = new byte[getEscherRecordSize(records)];
        List<Integer> spEndingOffsets = new ArrayList();
        List<EscherRecord> shapes = new ArrayList();
        int pos = 0;
        for (EscherRecord e : records) {
            final List<Integer> list = spEndingOffsets;
            final List<EscherRecord> list2 = shapes;
            pos += e.serialize(pos, buffer, new EscherSerializationListener() {
                public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {
                }

                public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record) {
                    if (recordId == EscherClientDataRecord.RECORD_ID || recordId == EscherTextboxRecord.RECORD_ID) {
                        list.add(Integer.valueOf(offset));
                        list2.add(record);
                    }
                }
            });
        }
        shapes.add(0, null);
        spEndingOffsets.add(0, Integer.valueOf(0));
        pos = offset;
        int writtenEscherBytes = 0;
        int i = 1;
        while (i < shapes.size()) {
            int startOffset;
            int endOffset = ((Integer) spEndingOffsets.get(i)).intValue() - 1;
            if (i == 1) {
                startOffset = 0;
            } else {
                startOffset = ((Integer) spEndingOffsets.get(i - 1)).intValue();
            }
            byte[] drawingData = new byte[((endOffset - startOffset) + 1)];
            System.arraycopy(buffer, startOffset, drawingData, 0, drawingData.length);
            pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
            writtenEscherBytes += drawingData.length;
            pos += ((Record) this.shapeToObj.get(shapes.get(i))).serialize(pos, data);
            if (i == shapes.size() - 1 && endOffset < buffer.length - 1) {
                drawingData = new byte[((buffer.length - endOffset) - 1)];
                System.arraycopy(buffer, endOffset + 1, drawingData, 0, drawingData.length);
                pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
            }
            i++;
        }
        if (pos - offset < buffer.length - 1) {
            drawingData = new byte[(buffer.length - (pos - offset))];
            System.arraycopy(buffer, pos - offset, drawingData, 0, drawingData.length);
            pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
        }
        for (Record noteRecord : this.tailRec.values()) {
            pos += noteRecord.serialize(pos, data);
        }
        int bytesWritten = pos - offset;
        if (bytesWritten == getRecordSize()) {
            return bytesWritten;
        }
        throw new RecordFormatException(bytesWritten + " bytes written but getRecordSize() reports " + getRecordSize());
    }

    private int writeDataIntoDrawingRecord(byte[] drawingData, int writtenEscherBytes, int pos, byte[] data, int i) {
        int temp = 0;
        int j;
        byte[] buf;
        if (drawingData.length + writtenEscherBytes <= 8224 || i == 1) {
            for (j = 0; j < drawingData.length; j += 8224) {
                if (j == 0) {
                    DrawingRecord drawing = new DrawingRecord();
                    buf = new byte[Math.min(8224, drawingData.length - j)];
                    System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
                    drawing.setData(buf);
                    temp += drawing.serialize(pos + temp, data);
                } else {
                    buf = new byte[Math.min(8224, drawingData.length - j)];
                    System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
                    temp += new ContinueRecord(buf).serialize(pos + temp, data);
                }
            }
        } else {
            for (j = 0; j < drawingData.length; j += 8224) {
                buf = new byte[Math.min(8224, drawingData.length - j)];
                System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
                temp += new ContinueRecord(buf).serialize(pos + temp, data);
            }
        }
        return temp;
    }

    private int getEscherRecordSize(List<EscherRecord> records) {
        int size = 0;
        for (EscherRecord record : records) {
            size += record.getRecordSize();
        }
        return size;
    }

    public int getRecordSize() {
        int continueRecordsHeadersSize = 0;
        List<EscherRecord> records = getEscherRecords();
        int rawEscherSize = getEscherRecordSize(records);
        byte[] buffer = new byte[rawEscherSize];
        final List<Integer> spEndingOffsets = new ArrayList();
        int pos = 0;
        for (EscherRecord e : records) {
            pos += e.serialize(pos, buffer, new EscherSerializationListener() {
                public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {
                }

                public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record) {
                    if (recordId == EscherClientDataRecord.RECORD_ID || recordId == EscherTextboxRecord.RECORD_ID) {
                        spEndingOffsets.add(Integer.valueOf(offset));
                    }
                }
            });
        }
        spEndingOffsets.add(0, Integer.valueOf(0));
        int i = 1;
        while (i < spEndingOffsets.size()) {
            if (i == spEndingOffsets.size() - 1 && ((Integer) spEndingOffsets.get(i)).intValue() < pos) {
                continueRecordsHeadersSize += 4;
            }
            if (((Integer) spEndingOffsets.get(i)).intValue() - ((Integer) spEndingOffsets.get(i - 1)).intValue() > 8224) {
                continueRecordsHeadersSize += ((((Integer) spEndingOffsets.get(i)).intValue() - ((Integer) spEndingOffsets.get(i - 1)).intValue()) / 8224) * 4;
            }
            i++;
        }
        int drawingRecordSize = rawEscherSize + (this.shapeToObj.size() * 4);
        if (rawEscherSize != 0 && spEndingOffsets.size() == 1) {
            continueRecordsHeadersSize += 4;
        }
        int objRecordSize = 0;
        for (Record r : this.shapeToObj.values()) {
            objRecordSize += r.getRecordSize();
        }
        int tailRecordSize = 0;
        for (NoteRecord noteRecord : this.tailRec.values()) {
            tailRecordSize += noteRecord.getRecordSize();
        }
        return ((drawingRecordSize + objRecordSize) + tailRecordSize) + continueRecordsHeadersSize;
    }

    public void associateShapeToObjRecord(EscherRecord r, Record objRecord) {
        this.shapeToObj.put(r, objRecord);
    }

    public void removeShapeToObjRecord(EscherRecord rec) {
        this.shapeToObj.remove(rec);
    }

    protected String getRecordName() {
        return "ESCHERAGGREGATE";
    }

    private static boolean isObjectRecord(List<RecordBase> records, int loc) {
        return sid(records, loc) == (short) 93 || sid(records, loc) == TextObjectRecord.sid;
    }

    private void buildBaseTree() {
        EscherContainerRecord dgContainer = new EscherContainerRecord();
        EscherContainerRecord spgrContainer = new EscherContainerRecord();
        EscherContainerRecord spContainer1 = new EscherContainerRecord();
        EscherSpgrRecord spgr = new EscherSpgrRecord();
        EscherSpRecord sp1 = new EscherSpRecord();
        dgContainer.setRecordId(EscherContainerRecord.DG_CONTAINER);
        dgContainer.setOptions((short) 15);
        EscherDgRecord dg = new EscherDgRecord();
        dg.setRecordId(EscherDgRecord.RECORD_ID);
        dg.setOptions((short) 16);
        dg.setNumShapes(0);
        dg.setLastMSOSPID(1024);
        spgrContainer.setRecordId(EscherContainerRecord.SPGR_CONTAINER);
        spgrContainer.setOptions((short) 15);
        spContainer1.setRecordId(EscherContainerRecord.SP_CONTAINER);
        spContainer1.setOptions((short) 15);
        spgr.setRecordId(EscherSpgrRecord.RECORD_ID);
        spgr.setOptions((short) 1);
        spgr.setRectX1(0);
        spgr.setRectY1(0);
        spgr.setRectX2(IEEEDouble.EXPONENT_BIAS);
        spgr.setRectY2(255);
        sp1.setRecordId(EscherSpRecord.RECORD_ID);
        sp1.setOptions((short) 2);
        sp1.setVersion((short) 2);
        sp1.setShapeId(-1);
        sp1.setFlags(5);
        dgContainer.addChildRecord(dg);
        dgContainer.addChildRecord(spgrContainer);
        spgrContainer.addChildRecord(spContainer1);
        spContainer1.addChildRecord(spgr);
        spContainer1.addChildRecord(sp1);
        addEscherRecord(dgContainer);
    }

    public void setDgId(short dgId) {
        ((EscherDgRecord) getEscherContainer().getChildById(EscherDgRecord.RECORD_ID)).setOptions((short) (dgId << 4));
    }

    public void setMainSpRecordId(int shapeId) {
        ((EscherSpRecord) ((EscherContainerRecord) ((EscherContainerRecord) getEscherContainer().getChildById(EscherContainerRecord.SPGR_CONTAINER)).getChild(0)).getChildById(EscherSpRecord.RECORD_ID)).setShapeId(shapeId);
    }

    private static short sid(List<RecordBase> records, int loc) {
        RecordBase record = (RecordBase) records.get(loc);
        if (record instanceof Record) {
            return ((Record) record).getSid();
        }
        return (short) -1;
    }

    public Map<EscherRecord, Record> getShapeToObjMapping() {
        return Collections.unmodifiableMap(this.shapeToObj);
    }

    public Map<Integer, NoteRecord> getTailRecords() {
        return Collections.unmodifiableMap(this.tailRec);
    }

    public NoteRecord getNoteRecordByObj(ObjRecord obj) {
        return (NoteRecord) this.tailRec.get(Integer.valueOf(((CommonObjectDataSubRecord) obj.getSubRecords().get(0)).getObjectId()));
    }

    public void addTailRecord(NoteRecord note) {
        this.tailRec.put(Integer.valueOf(note.getShapeId()), note);
    }

    public void removeTailRecord(NoteRecord note) {
        this.tailRec.remove(Integer.valueOf(note.getShapeId()));
    }
}
