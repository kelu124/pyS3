package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorScanPDirSpeedPacket extends Packet {
    private final short freqCoe;

    public SetFMotorScanPDirSpeedPacket(short freqCoe) {
        super(new PacketHeader(ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_SPEED, 2));
        this.freqCoe = freqCoe;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.freqCoe);
        outputStream.flush();
    }
}
