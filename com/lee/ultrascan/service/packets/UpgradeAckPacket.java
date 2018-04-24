package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class UpgradeAckPacket extends Packet {
    public UpgradeAckPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
    }
}
