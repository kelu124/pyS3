package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPControlWord extends Packet {
    private final short controlWord;

    public SetBBPControlWord(short controlWord) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_CONTROL_WORD, 2));
        this.controlWord = controlWord;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.controlWord);
        outputStream.flush();
    }
}
