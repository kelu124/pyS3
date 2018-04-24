package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPLogTablePacket extends Packet {
    private static final int LOG_TABLE_BYTES = 2048;
    private static final int LOG_TABLE_SIZE = 2048;
    private final byte[] logTable;

    public SetBBPLogTablePacket(byte[] logTable) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_LOG_TABLE, 2048));
        this.logTable = (byte[]) logTable.clone();
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.logTable);
        outputStream.flush();
    }
}
