package com.lee.ultrascan.service.net;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.lee.ultrascan.service.packets.PacketHeader;
import com.lee.ultrascan.utils.LogUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PacketInputStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!PacketInputStream.class.desiredAssertionStatus());
    protected static final int HEADER_FLAG = -1;
    private BufferedInputStream inputStream;

    public PacketInputStream(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
    }

    public void close() throws IOException {
        this.inputStream.close();
    }

    public byte readByte() throws IOException {
        int i = this.inputStream.read();
        if (i != -1) {
            return (byte) i;
        }
        throw new RuntimeException("End of inputstream when readByte.");
    }

    public short readShort() throws IOException {
        return (short) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & (readByte() << 8)) | (readByte() & 255));
    }

    public int readInt() throws IOException {
        byte a = readByte();
        byte b = readByte();
        return (((ViewCompat.MEASURED_STATE_MASK & (readByte() << 24)) | (16711680 & (readByte() << 16))) | (MotionEventCompat.ACTION_POINTER_INDEX_MASK & (b << 8))) | (a & 255);
    }

    public void read(byte[] buffer) throws IOException {
        if (buffer != null) {
            if ($assertionsDisabled || buffer.length > 0) {
                int bytesRead = 0;
                while (bytesRead < buffer.length) {
                    bytesRead += this.inputStream.read(buffer, bytesRead, buffer.length - bytesRead);
                }
                return;
            }
            throw new AssertionError();
        }
    }

    public PacketHeader readPacketHeader() throws IOException {
        if (readInt() != -1) {
            throw new IOException("unexpected flag when reading from inputstream.");
        }
        PacketHeader packetHeader = new PacketHeader(readInt(), readInt());
        LogUtils.LOGI("PacketInputStream", packetHeader.toString());
        return packetHeader;
    }
}
