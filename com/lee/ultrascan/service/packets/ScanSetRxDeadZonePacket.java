package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class ScanSetRxDeadZonePacket extends Packet {
    private final short points;

    public ScanSetRxDeadZonePacket(short points) {
        super(new PacketHeader(8196, 2));
        this.points = points;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.points);
        outputStream.flush();
    }
}
