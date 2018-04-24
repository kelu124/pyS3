package com.lee.ultrascan.service.packets;

public class PacketHeader {
    public final int id;
    public final int length;

    public PacketHeader(int id, int length) {
        this.id = id;
        this.length = length;
    }

    public String toString() {
        return "PacketHeader Id:" + this.id + "  length:" + this.length;
    }
}
