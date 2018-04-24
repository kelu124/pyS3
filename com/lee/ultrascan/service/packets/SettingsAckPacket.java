package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class SettingsAckPacket extends Packet {
    private int retCode = -1;

    public SettingsAckPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        this.retCode = inputStream.readInt();
    }

    public int getRetCode() {
        return this.retCode;
    }
}
