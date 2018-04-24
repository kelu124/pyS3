package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class ResetPacket extends Packet {
    public ResetPacket() {
        super(new PacketHeader(7, 0));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.flush();
    }
}
