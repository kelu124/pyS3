package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import java.io.IOException;

public class CmdRfidPacket extends Packet {
    private String rfid = "";

    public CmdRfidPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        byte[] data = new byte[this.packetHeader.length];
        inputStream.read(data);
        this.rfid = new String(data);
    }

    public String getRfid() {
        return this.rfid;
    }
}
