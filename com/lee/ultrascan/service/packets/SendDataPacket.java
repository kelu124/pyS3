package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SendDataPacket extends Packet {
    private final byte[] data;

    public SendDataPacket(byte[] data) {
        super(new PacketHeader(10, data.length));
        this.data = (byte[]) data.clone();
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.data);
        outputStream.flush();
    }
}
