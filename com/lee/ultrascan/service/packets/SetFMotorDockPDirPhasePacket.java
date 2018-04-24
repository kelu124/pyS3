package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorDockPDirPhasePacket extends Packet {
    private final short phase;

    public SetFMotorDockPDirPhasePacket(short phase) {
        super(new PacketHeader(4098, 2));
        this.phase = phase;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.phase);
        outputStream.flush();
    }
}
