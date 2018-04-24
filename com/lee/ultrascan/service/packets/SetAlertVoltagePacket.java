package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetAlertVoltagePacket extends Packet {
    private final short alertVoltageValues;

    public SetAlertVoltagePacket(byte lowThresh, byte cancelThresh) {
        super(new PacketHeader(12, 2));
        this.alertVoltageValues = (short) ((cancelThresh << 8) | ((short) lowThresh));
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.alertVoltageValues);
        outputStream.flush();
    }
}
