package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SleepPacket extends Packet {
    public SleepPacket() {
        super(new PacketHeader(4, 0));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.flush();
    }
}
