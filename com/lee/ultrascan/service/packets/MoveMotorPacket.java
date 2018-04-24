package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class MoveMotorPacket extends Packet {
    private final short steps;

    public MoveMotorPacket(short steps) {
        super(new PacketHeader(ProbeMessageID.ID_MOVE_FMOTOR, 2));
        this.steps = steps;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.steps);
        outputStream.flush();
    }
}
