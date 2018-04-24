package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetFMotorDockNDirSpeedPacket extends Packet {
    private final short freqCoe;

    public SetFMotorDockNDirSpeedPacket(short freqCoe) {
        super(new PacketHeader(4099, 2));
        this.freqCoe = freqCoe;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.freqCoe);
        outputStream.flush();
    }
}
