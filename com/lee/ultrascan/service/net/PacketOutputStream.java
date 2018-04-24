package com.lee.ultrascan.service.net;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PacketOutputStream {
    private BufferedOutputStream outputStream;

    public PacketOutputStream(OutputStream outputStream) {
        this.outputStream = new BufferedOutputStream(outputStream);
    }

    public void close() throws IOException {
        this.outputStream.close();
    }

    public void write(byte value) throws IOException {
        this.outputStream.write(value);
    }

    public void write(byte[] buffer) throws IOException {
        if (buffer != null) {
            this.outputStream.write(buffer);
        }
    }

    public void write(short[] buffer) throws IOException {
        if (buffer != null) {
            for (short s : buffer) {
                write(s);
            }
        }
    }

    public void write(short value) throws IOException {
        write((byte) (value & 255));
        write((byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & value) >> 8));
    }

    public void write(int value) throws IOException {
        write((byte) (value & 255));
        write((byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & value) >> 8));
        write((byte) ((16711680 & value) >> 16));
        write((byte) ((ViewCompat.MEASURED_STATE_MASK & value) >> 24));
    }

    public void flush() throws IOException {
        this.outputStream.flush();
    }
}
