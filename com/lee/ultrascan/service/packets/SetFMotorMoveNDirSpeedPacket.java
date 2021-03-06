package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorMoveNDirSpeedPacket extends Packet {
    private final short freqCoe;

    public SetFMotorMoveNDirSpeedPacket(short freqCoe) {
        super(new PacketHeader(ProbeMessageID.ID_SET_FMOTOR_MOVE_NDIR_SPEED, 2));
        this.freqCoe = freqCoe;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.freqCoe);
        outputStream.flush();
    }
}
