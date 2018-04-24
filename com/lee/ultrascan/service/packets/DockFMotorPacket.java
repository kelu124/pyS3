package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class DockFMotorPacket extends Packet {
    public DockFMotorPacket() {
        super(new PacketHeader(ProbeMessageID.ID_DOCK_FMOTOR, 0));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.flush();
    }
}
