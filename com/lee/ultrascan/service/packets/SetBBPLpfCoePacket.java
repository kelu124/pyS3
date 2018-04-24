package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class SetBBPLpfCoePacket extends Packet {
    private static final int LPF_COE_BYTES = 44;
    private static final int LPF_COE_SIZE = 22;
    private final short[] lpfCoes;

    public SetBBPLpfCoePacket(short[] lpfCoes) {
        super(new PacketHeader(ProbeMessageID.ID_SET_BBP_LPF_COE, 44));
        this.lpfCoes = (short[]) lpfCoes.clone();
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.lpfCoes);
        outputStream.flush();
    }
}
