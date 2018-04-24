package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class ConnectAckPacket extends Packet {
    private int version = 2;

    public ConnectAckPacket() {
        super(new PacketHeader(1, 4));
    }

    public int getVersion() {
        return this.version;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.version);
        outputStream.flush();
    }
}
