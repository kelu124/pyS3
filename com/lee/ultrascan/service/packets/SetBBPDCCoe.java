package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPDCCoe extends Packet {
    private final short bbpDCCoe;

    public SetBBPDCCoe(short bbpDCCoe) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_DC_COE, 2));
        this.bbpDCCoe = bbpDCCoe;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.bbpDCCoe);
        outputStream.flush();
    }
}
