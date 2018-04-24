package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxLinePerFramePacket extends Packet {
    private final short linesPerFrame;

    public Set2DRxLinePerFramePacket(short linesPerFrame) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_LINE_PER_FRAME, 2));
        this.linesPerFrame = linesPerFrame;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.linesPerFrame);
        outputStream.flush();
    }
}
