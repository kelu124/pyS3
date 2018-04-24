package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.net.PacketOutputStream;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;

public class ScanGetFramePacket extends Packet {
    private final short idleFrames;

    public ScanGetFramePacket(short idleFrames) {
        super(new PacketHeader(8194, 2));
        this.idleFrames = idleFrames;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        LogUtils.LOGI("probe", "writePacket:" + getClass().getSimpleName());
        writeHeader(outputStream);
        outputStream.write(this.idleFrames);
        outputStream.flush();
    }
}
