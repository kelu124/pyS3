package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class BatteryAckPacket extends Packet {
    private short voltageData;

    public BatteryAckPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public short getVoltageData() {
        return this.voltageData;
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        this.voltageData = inputStream.readShort();
    }
}
