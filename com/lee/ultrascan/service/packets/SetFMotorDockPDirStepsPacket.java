package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorDockPDirStepsPacket extends Packet {
    private final short steps;

    public SetFMotorDockPDirStepsPacket(short steps) {
        super(new PacketHeader(4097, 2));
        this.steps = steps;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.steps);
        outputStream.flush();
    }
}
