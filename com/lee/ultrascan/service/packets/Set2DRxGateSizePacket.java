package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxGateSizePacket extends Packet {
    private final short gateSize;

    public Set2DRxGateSizePacket(short gateSize) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_GATE_SIZE, 2));
        this.gateSize = gateSize;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.gateSize);
        outputStream.flush();
    }
}
