package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxTgcBreakSizePacket extends Packet {
    private final short breakSize;

    public Set2DRxTgcBreakSizePacket(short breakSize) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_TGC_BREAK_SIZE, 2));
        this.breakSize = breakSize;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.breakSize);
        outputStream.flush();
    }
}
