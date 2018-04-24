package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPDeltaPhasePacket extends Packet {
    private final int deltaPhase;

    public SetBBPDeltaPhasePacket(float deltaPhase) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_DELTA_PHASE, 4));
        this.deltaPhase = (int) (16384.0f * deltaPhase);
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.deltaPhase);
        outputStream.flush();
    }
}
