package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class ScanStopPacket extends Packet {
    public ScanStopPacket() {
        super(new PacketHeader(ProbeMessageID.ID_SCAN_STOP, 0));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.flush();
    }
}
