package org.apache.poi.hssf.record.cont;

import org.apache.poi.util.DelayableLittleEndianOutput;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianOutput;

final class UnknownLengthRecordOutput implements LittleEndianOutput {
    private static final int MAX_DATA_SIZE = 8224;
    private final byte[] _byteBuffer;
    private final LittleEndianOutput _dataSizeOutput;
    private final LittleEndianOutput _originalOut;
    private LittleEndianOutput _out;
    private int _size;

    public UnknownLengthRecordOutput(LittleEndianOutput out, int sid) {
        this._originalOut = out;
        out.writeShort(sid);
        if (out instanceof DelayableLittleEndianOutput) {
            this._dataSizeOutput = ((DelayableLittleEndianOutput) out).createDelayedOutput(2);
            this._byteBuffer = null;
            this._out = out;
            return;
        }
        this._dataSizeOutput = out;
        this._byteBuffer = new byte[MAX_DATA_SIZE];
        this._out = new LittleEndianByteArrayOutputStream(this._byteBuffer, 0);
    }

    public int getTotalSize() {
        return this._size + 4;
    }

    public int getAvailableSpace() {
        if (this._out != null) {
            return 8224 - this._size;
        }
        throw new IllegalStateException("Record already terminated");
    }

    public void terminate() {
        if (this._out == null) {
            throw new IllegalStateException("Record already terminated");
        }
        this._dataSizeOutput.writeShort(this._size);
        if (this._byteBuffer != null) {
            this._originalOut.write(this._byteBuffer, 0, this._size);
            this._out = null;
            return;
        }
        this._out = null;
    }

    public void write(byte[] b) {
        this._out.write(b);
        this._size += b.length;
    }

    public void write(byte[] b, int offset, int len) {
        this._out.write(b, offset, len);
        this._size += len;
    }

    public void writeByte(int v) {
        this._out.writeByte(v);
        this._size++;
    }

    public void writeDouble(double v) {
        this._out.writeDouble(v);
        this._size += 8;
    }

    public void writeInt(int v) {
        this._out.writeInt(v);
        this._size += 4;
    }

    public void writeLong(long v) {
        this._out.writeLong(v);
        this._size += 8;
    }

    public void writeShort(int v) {
        this._out.writeShort(v);
        this._size += 2;
    }
}
