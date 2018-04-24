package com.lee.ultrascan.service.packets;

import com.lee.ultrascan.service.ProbeMessageID;
import com.lee.ultrascan.service.net.PacketOutputStream;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;
import java.util.Arrays;

public class SettingsPacket extends Packet {
    private static final int BUFFER_SIZE = 32;
    private final byte[] pwd_bytes = new byte[32];
    private final byte[] ssid_bytes = new byte[32];

    public SettingsPacket(String ssid, String pwd) {
        int i = 0;
        super(new PacketHeader(ProbeMessageID.ID_SETTINGS, 64));
        Arrays.fill(this.ssid_bytes, (byte) 0);
        Arrays.fill(this.pwd_bytes, (byte) 0);
        byte[] ssid_str_bytes = ssid.getBytes();
        byte[] pwd_str_bytes = pwd.getBytes();
        System.arraycopy(ssid_str_bytes, 0, this.ssid_bytes, 0, min(this.ssid_bytes.length - 1, ssid_str_bytes.length));
        System.arraycopy(pwd_str_bytes, 0, this.pwd_bytes, 0, min(this.pwd_bytes.length - 1, pwd_str_bytes.length));
        StringBuffer ssidStringBuffer = new StringBuffer();
        for (byte b : this.ssid_bytes) {
            ssidStringBuffer.append(b);
        }
        LogUtils.LOGI("ssid_bytes", ssidStringBuffer.toString());
        StringBuffer pwdStringBuffer = new StringBuffer();
        byte[] bArr = this.pwd_bytes;
        int length = bArr.length;
        while (i < length) {
            pwdStringBuffer.append(bArr[i]);
            i++;
        }
        LogUtils.LOGI("pwd_bytes", pwdStringBuffer.toString());
    }

    protected int min(int a, int b) {
        return a < b ? a : b;
    }

    public void writePacket(PacketOutputStream outputStream) throws IOException {
        writeHeader(outputStream);
        outputStream.write(this.ssid_bytes);
        outputStream.write(this.pwd_bytes);
        outputStream.flush();
    }
}
