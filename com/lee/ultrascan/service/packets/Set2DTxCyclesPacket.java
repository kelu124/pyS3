package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DTxCyclesPacket extends Packet {
    private final short cycles;

    public Set2DTxCyclesPacket(short cycles) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_TX_CYCLES, 2));
        this.cycles = cycles;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.cycles);
        outputStream.flush();
    }
}
