package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetPowerOffSecondsPacket extends Packet {
    private final short seconds;

    public SetPowerOffSecondsPacket(short seconds) {
        super(new PacketHeader(11, 2));
        this.seconds = seconds;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.seconds);
        outputStream.flush();
    }
}
