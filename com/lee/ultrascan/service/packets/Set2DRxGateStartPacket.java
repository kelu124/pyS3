package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxGateStartPacket extends Packet {
    private final short gateStart;

    public Set2DRxGateStartPacket(short gateStart) {
        super(new PacketHeader(16386, 2));
        this.gateStart = gateStart;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.gateStart);
        outputStream.flush();
    }
}
