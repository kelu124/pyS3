package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.model.FrameData;
import com.lee.ultrascan.service.net.PacketInputStream;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;

public class ScanGetFrameAckPacket extends Packet {
    private FrameData frameData;

    public ScanGetFrameAckPacket(PacketHeader packetHeader) {
        super(packetHeader);
    }

    public FrameData getFrameData() {
        return this.frameData;
    }

    public void readPacketData(PacketInputStream inputStream) throws IOException {
        LogUtils.LOGI("probe", "readPacketData:" + getClass().getSimpleName());
        int length = this.packetHeader.length;
        int attribute = inputStream.readInt();
        int frameIndex = inputStream.readInt();
        int linesHaveScanned = inputStream.readInt();
        LogUtils.LOGI("probe", "length:" + length + " attribute:" + attribute + " frameIndex:" + frameIndex + " linesHaveScanned:" + linesHaveScanned);
        byte[] data = new byte[(length - 12)];
        inputStream.read(data);
        LogUtils.LOGI("probe", "frame data read complete.");
        this.frameData = new FrameData(attribute, frameIndex, linesHaveScanned, data);
    }
}
