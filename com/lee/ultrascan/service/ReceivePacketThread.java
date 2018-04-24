package com.lee.ultrascan.service;

import android.os.Handler;
import android.os.Message;
import com.lee.ultrascan.service.net.PacketInputStream;
import com.lee.ultrascan.service.packets.Packet;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;

public class ReceivePacketThread extends Thread {
    private PacketInputStream inputStream;
    private volatile boolean isRunning = false;
    private Handler packetHandler;

    public ReceivePacketThread(PacketInputStream inputStream, Handler packetHandler) {
        this.inputStream = inputStream;
        this.packetHandler = packetHandler;
    }

    public void run() {
        this.isRunning = true;
        while (this.isRunning) {
            LogUtils.LOGI("CONNECTION", "start receving packets.");
            try {
                Packet packet = ProbeMessageID.createReadPacketByHeader(this.inputStream.readPacketHeader());
                packet.readPacketData(this.inputStream);
                LogUtils.LOGE("CONNECTION", "packet received:" + packet.getClass().getSimpleName());
                Message message = this.packetHandler.obtainMessage();
                message.obj = packet;
                this.packetHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.LOGE("CONNECTION", "receving ioexception:" + e);
                this.isRunning = false;
            }
        }
        LogUtils.LOGI("CONNECTION", "stop receving packets.");
    }

    public void stopRunning() throws InterruptedException {
        this.isRunning = false;
        try {
            this.inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
