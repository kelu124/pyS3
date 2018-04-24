package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxLinePtrPacket extends Packet {
    private final short ptr;

    public Set2DRxLinePtrPacket(short ptr) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_LINE_PRT, 2));
        this.ptr = ptr;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.ptr);
        outputStream.flush();
    }
}
