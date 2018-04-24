package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPDeciRatePacket extends Packet {
    private final short deciRate;

    public SetBBPDeciRatePacket(short deciRate) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_DECI_RATE, 2));
        this.deciRate = deciRate;
    }

    public short getDeciRate() {
        return this.deciRate;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.deciRate);
        outputStream.flush();
    }
}
