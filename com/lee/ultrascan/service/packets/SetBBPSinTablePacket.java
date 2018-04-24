package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPSinTablePacket extends Packet {
    static final /* synthetic */ boolean $assertionsDisabled = (!SetBBPSinTablePacket.class.desiredAssertionStatus());
    private static final int TABLE_BYTES = 1024;
    private static final int TABLE_SIZE = 512;
    private final short[] sinTable;

    public SetBBPSinTablePacket(short[] sinTable) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_SIN_TABLE, 1024));
        if ($assertionsDisabled || sinTable.length == 512) {
            this.sinTable = (short[]) sinTable.clone();
            return;
        }
        throw new AssertionError();
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.sinTable);
        outputStream.flush();
    }
}
