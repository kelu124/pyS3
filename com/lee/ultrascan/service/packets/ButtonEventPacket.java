package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class ButtonEventPacket extends Packet {
    public ButtonEventPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
    }
}
