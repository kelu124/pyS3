package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class ConnectPacket extends Packet {
    private int version = 1;

    public ConnectPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        this.version = inputStream.readInt();
    }

    public int getProbeVersion() {
        return this.version;
    }
}
