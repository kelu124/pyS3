package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketInputStream;
import com.lee.ultrascan.service.net.PacketOutputStream;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;

public abstract class Packet {
    protected static final int HEADER_FLAG = -1;
    protected PacketHeader packetHeader;

    public Packet(PacketHeader packetHeader) {
        this.packetHeader = packetHeader;
    }

    public int getId() {
        return this.packetHeader.id;
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        throw new IllegalAccessError("Packet not readable!");
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        throw new IllegalAccessError("Packet not writable!");
    }

    protected void writeHeader(PacketOutputStream outputStream) throws IOException {
        outputStream.write(-1);
        outputStream.write(this.packetHeader.id);
        outputStream.write(this.packetHeader.length);
        LogUtils.LOGI("socket", "write header id:" + this.packetHeader.id + " length:" + this.packetHeader.length);
    }
}
