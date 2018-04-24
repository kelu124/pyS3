package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class ScanStartPacket extends Packet {
    private final short scanMode;

    public ScanStartPacket(short scanMode) {
        super(new PacketHeader(8192, 2));
        this.scanMode = scanMode;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.scanMode);
        outputStream.flush();
    }
}
