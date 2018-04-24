package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutputStream;
import org.apache.poi.util.StringUtil;

public class Ole10Native {
    static final /* synthetic */ boolean $assertionsDisabled = (!Ole10Native.class.desiredAssertionStatus());
    protected static final String ISO1 = "ISO-8859-1";
    public static final String OLE10_NATIVE = "\u0001Ole10Native";
    private String command;
    private byte[] dataBuffer;
    private String fileName;
    private short flags1 = (short) 2;
    private short flags2 = (short) 0;
    private short flags3 = (short) 0;
    private String label;
    private EncodingMode mode;
    private int totalSize;
    private short unknown1 = (short) 3;

    private enum EncodingMode {
        parsed,
        unparsed,
        compact
    }

    public static Ole10Native createFromEmbeddedOleObject(POIFSFileSystem poifs) throws IOException, Ole10NativeException {
        return createFromEmbeddedOleObject(poifs.getRoot());
    }

    public static Ole10Native createFromEmbeddedOleObject(DirectoryNode directory) throws IOException, Ole10NativeException {
        Entry nativeEntry = (DocumentEntry) directory.getEntry(OLE10_NATIVE);
        byte[] data = new byte[nativeEntry.getSize()];
        int readBytes = directory.createDocumentInputStream(nativeEntry).read(data);
        if ($assertionsDisabled || readBytes == data.length) {
            return new Ole10Native(data, 0);
        }
        throw new AssertionError();
    }

    public Ole10Native(String label, String filename, String command, byte[] data) {
        setLabel(label);
        setFileName(filename);
        setCommand(command);
        setDataBuffer(data);
        this.mode = EncodingMode.parsed;
    }

    public Ole10Native(byte[] data, int offset) throws Ole10NativeException {
        int ofs = offset;
        if (data.length < offset + 2) {
            throw new Ole10NativeException("data is too small");
        }
        int dataSize;
        this.totalSize = LittleEndian.getInt(data, ofs);
        ofs += 4;
        this.mode = EncodingMode.unparsed;
        if (LittleEndian.getShort(data, ofs) == (short) 2) {
            if (Character.isISOControl(data[ofs + 2])) {
                this.mode = EncodingMode.compact;
            } else {
                this.mode = EncodingMode.parsed;
            }
        }
        switch (this.mode) {
            case parsed:
                this.flags1 = LittleEndian.getShort(data, ofs);
                ofs += 2;
                int len = getStringLength(data, ofs);
                this.label = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
                ofs += len;
                len = getStringLength(data, ofs);
                this.fileName = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
                ofs += len;
                this.flags2 = LittleEndian.getShort(data, ofs);
                ofs += 2;
                this.unknown1 = LittleEndian.getShort(data, ofs);
                ofs += 2;
                len = LittleEndian.getInt(data, ofs);
                ofs += 4;
                this.command = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
                ofs += len;
                if (this.totalSize < ofs) {
                    throw new Ole10NativeException("Invalid Ole10Native");
                }
                dataSize = LittleEndian.getInt(data, ofs);
                ofs += 4;
                if (dataSize < 0 || this.totalSize - (ofs - 4) < dataSize) {
                    throw new Ole10NativeException("Invalid Ole10Native");
                }
            case compact:
                this.flags1 = LittleEndian.getShort(data, ofs);
                ofs += 2;
                dataSize = this.totalSize - 2;
                break;
            default:
                dataSize = this.totalSize;
                break;
        }
        this.dataBuffer = new byte[dataSize];
        System.arraycopy(data, ofs, this.dataBuffer, 0, dataSize);
        ofs += dataSize;
    }

    private static int getStringLength(byte[] data, int ofs) {
        int len = 0;
        while (len + ofs < data.length && data[ofs + len] != (byte) 0) {
            len++;
        }
        return len + 1;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public short getFlags1() {
        return this.flags1;
    }

    public String getLabel() {
        return this.label;
    }

    public String getFileName() {
        return this.fileName;
    }

    public short getFlags2() {
        return this.flags2;
    }

    public short getUnknown1() {
        return this.unknown1;
    }

    public String getCommand() {
        return this.command;
    }

    public int getDataSize() {
        return this.dataBuffer.length;
    }

    public byte[] getDataBuffer() {
        return this.dataBuffer;
    }

    public short getFlags3() {
        return this.flags3;
    }

    public void writeOut(OutputStream out) throws IOException {
        LittleEndianOutputStream leosOut = new LittleEndianOutputStream(out);
        switch (this.mode) {
            case parsed:
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                LittleEndianOutputStream leos = new LittleEndianOutputStream(bos);
                leos.writeShort(getFlags1());
                leos.write(getLabel().getBytes(ISO1));
                leos.write(0);
                leos.write(getFileName().getBytes(ISO1));
                leos.write(0);
                leos.writeShort(getFlags2());
                leos.writeShort(getUnknown1());
                leos.writeInt(getCommand().length() + 1);
                leos.write(getCommand().getBytes(ISO1));
                leos.write(0);
                leos.writeInt(getDataSize());
                leos.write(getDataBuffer());
                leos.writeShort(getFlags3());
                leos.close();
                leosOut.writeInt(bos.size());
                bos.writeTo(out);
                return;
            case compact:
                leosOut.writeInt(getDataSize() + 2);
                leosOut.writeShort(getFlags1());
                out.write(getDataBuffer());
                return;
            default:
                leosOut.writeInt(getDataSize());
                out.write(getDataBuffer());
                return;
        }
    }

    public void setFlags1(short flags1) {
        this.flags1 = flags1;
    }

    public void setFlags2(short flags2) {
        this.flags2 = flags2;
    }

    public void setFlags3(short flags3) {
        this.flags3 = flags3;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUnknown1(short unknown1) {
        this.unknown1 = unknown1;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = (byte[]) dataBuffer.clone();
    }
}
