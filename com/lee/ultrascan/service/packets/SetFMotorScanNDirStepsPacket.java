package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorScanNDirStepsPacket extends Packet {
    private final short steps;

    public SetFMotorScanNDirStepsPacket(short steps) {
        super(new PacketHeader(ProbeMessageID.ID_SET_FMOTOR_SCAN_NDIR_STEPS, 2));
        this.steps = steps;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.steps);
        outputStream.flush();
    }
}
