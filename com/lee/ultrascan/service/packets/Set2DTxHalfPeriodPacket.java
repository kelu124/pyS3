package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DTxHalfPeriodPacket extends Packet {
    private final short period;

    public Set2DTxHalfPeriodPacket(short period) {
        super(new PacketHeader(16384, 2));
        this.period = period;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.period);
        outputStream.flush();
    }
}
