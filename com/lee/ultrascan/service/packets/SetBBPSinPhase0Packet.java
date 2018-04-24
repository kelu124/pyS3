package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPSinPhase0Packet extends Packet {
    private final int sinPhase0;

    public SetBBPSinPhase0Packet(int sinPhase0) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_SIN_PHASE0, 4));
        this.sinPhase0 = sinPhase0;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.sinPhase0);
        outputStream.flush();
    }
}
