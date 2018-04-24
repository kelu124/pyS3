package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import java.io.IOException;

public class Set2DRxTgcProfilePacket extends Packet {
    static final /* synthetic */ boolean $assertionsDisabled = (!Set2DRxTgcProfilePacket.class.desiredAssertionStatus());
    private static final int TGC_CURVE_BYTES = 1870;
    private static final int TGC_CURVE_SIZE = 935;
    private final short offset;
    private final short[] tgcCurve;

    public Set2DRxTgcProfilePacket(short[] tgcCurve, short offset) {
        super(new PacketHeader(ProbeMessageID.ID_SET2D_RX_TGC_PROFILE, tgcCurve.length * 2));
        if ($assertionsDisabled || tgcCurve.length == 935) {
            this.tgcCurve = tgcCurve;
            this.offset = offset;
            return;
        }
        throw new AssertionError();
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.tgcCurve);
        outputStream.flush();
    }

    public short getOffset() {
        return this.offset;
    }
}
