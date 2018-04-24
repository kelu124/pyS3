package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPCosPhase0Packet extends Packet {
    private final int cosPhase0;

    public SetBBPCosPhase0Packet(int cosPhase0) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_COS_PHASE0, 4));
        this.cosPhase0 = cosPhase0;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.cosPhase0);
        outputStream.flush();
    }
}
