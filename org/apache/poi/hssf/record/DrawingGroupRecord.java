package org.apache.poi.hssf.record;

import java.util.List;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.NullEscherSerializationListener;
import org.apache.poi.util.LittleEndian;

public final class DrawingGroupRecord extends AbstractEscherHolderRecord {
    private static final int MAX_DATA_SIZE = 8224;
    static final int MAX_RECORD_SIZE = 8228;
    public static final short sid = (short) 235;

    public DrawingGroupRecord(RecordInputStream in) {
        super(in);
    }

    protected String getRecordName() {
        return "MSODRAWINGGROUP";
    }

    public short getSid() {
        return sid;
    }

    public int serialize(int offset, byte[] data) {
        byte[] rawData = getRawData();
        if (getEscherRecords().size() == 0 && rawData != null) {
            return writeData(offset, data, rawData);
        }
        byte[] buffer = new byte[getRawDataSize()];
        int pos = 0;
        for (EscherRecord r : getEscherRecords()) {
            pos += r.serialize(pos, buffer, new NullEscherSerializationListener());
        }
        return writeData(offset, data, buffer);
    }

    public void processChildRecords() {
        convertRawBytesToEscherRecords();
    }

    public int getRecordSize() {
        return grossSizeFromDataSize(getRawDataSize());
    }

    private int getRawDataSize() {
        List<EscherRecord> escherRecords = getEscherRecords();
        byte[] rawData = getRawData();
        if (escherRecords.size() == 0 && rawData != null) {
            return rawData.length;
        }
        int size = 0;
        for (EscherRecord r : escherRecords) {
            size += r.getRecordSize();
        }
        return size;
    }

    static int grossSizeFromDataSize(int dataSize) {
        return ((((dataSize - 1) / MAX_DATA_SIZE) + 1) * 4) + dataSize;
    }

    private int writeData(int offset, byte[] data, byte[] rawData) {
        int writtenActualData = 0;
        int writtenRawData = 0;
        while (writtenRawData < rawData.length) {
            int segmentLength = Math.min(rawData.length - writtenRawData, MAX_DATA_SIZE);
            if (writtenRawData / MAX_DATA_SIZE >= 2) {
                writeContinueHeader(data, offset, segmentLength);
            } else {
                writeHeader(data, offset, segmentLength);
            }
            writtenActualData += 4;
            offset += 4;
            System.arraycopy(rawData, writtenRawData, data, offset, segmentLength);
            offset += segmentLength;
            writtenRawData += segmentLength;
            writtenActualData += segmentLength;
        }
        return writtenActualData;
    }

    private void writeHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, offset + 0, getSid());
        LittleEndian.putShort(data, offset + 2, (short) sizeExcludingHeader);
    }

    private void writeContinueHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, offset + 0, (short) 60);
        LittleEndian.putShort(data, offset + 2, (short) sizeExcludingHeader);
    }
}
