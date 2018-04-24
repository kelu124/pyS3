package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class BatteryPacket extends Packet {
    public BatteryPacket() {
        super(new PacketHeader(2, 0));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.flush();
    }
}
