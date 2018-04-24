package com.lee.ultrascan.service.packets;

public class DisconnectedPacket extends Packet {
    public DisconnectedPacket() {
        super(new PacketHeader(-1, 0));
    }
}
