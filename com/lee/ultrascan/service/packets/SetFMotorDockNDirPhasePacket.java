package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorDockNDirPhasePacket extends Packet {
    private final short phase;

    public SetFMotorDockNDirPhasePacket(short phase) {
        super(new PacketHeader(ProbeMessageID.ID_SET_FMOTOR_DOCK_NDIR_PHASE, 2));
        this.phase = phase;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.phase);
        outputStream.flush();
    }
}
