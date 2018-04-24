package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxBothDirectionsPacket extends Packet {
    private final short useBidirectionScan;

    public Set2DRxBothDirectionsPacket(short useBidirectionScan) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_BOTH_DIRECTIONS, 2));
        this.useBidirectionScan = useBidirectionScan;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.useBidirectionScan);
        outputStream.flush();
    }
}
