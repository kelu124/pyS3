package com.lee.ultrascan.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import com.lee.ultrascan.library.DepthType;
import com.lee.ultrascan.library.FrequencyType;
import com.lee.ultrascan.library.ProbeParams;
import com.lee.ultrascan.library.ProbeStatus;
import com.lee.ultrascan.library.ScanConversionParams;
import com.lee.ultrascan.model.RFFrame;
import com.lee.ultrascan.model.TgcCurve;
import com.lee.ultrascan.service.net.PacketInputStream;
import com.lee.ultrascan.service.net.PacketOutputStream;
import com.lee.ultrascan.service.net.WifiHelper;
import com.lee.ultrascan.service.packets.BatteryAckPacket;
import com.lee.ultrascan.service.packets.BatteryPacket;
import com.lee.ultrascan.service.packets.CmdRfidPacket;
import com.lee.ultrascan.service.packets.DisconnectedPacket;
import com.lee.ultrascan.service.packets.DockFMotorPacket;
import com.lee.ultrascan.service.packets.Packet;
import com.lee.ultrascan.service.packets.ScanGetFrameAckPacket;
import com.lee.ultrascan.service.packets.ScanGetFramePacket;
import com.lee.ultrascan.service.packets.ScanSetRxDeadZonePacket;
import com.lee.ultrascan.service.packets.ScanStartPacket;
import com.lee.ultrascan.service.packets.ScanStopPacket;
import com.lee.ultrascan.service.packets.Set2DRxBothDirectionsPacket;
import com.lee.ultrascan.service.packets.Set2DRxGateSizePacket;
import com.lee.ultrascan.service.packets.Set2DRxGateStartPacket;
import com.lee.ultrascan.service.packets.Set2DRxLinePerFramePacket;
import com.lee.ultrascan.service.packets.Set2DRxLinePtrPacket;
import com.lee.ultrascan.service.packets.Set2DRxTgcBreakSizePacket;
import com.lee.ultrascan.service.packets.Set2DRxTgcProfilePacket;
import com.lee.ultrascan.service.packets.Set2DTxCyclesPacket;
import com.lee.ultrascan.service.packets.Set2DTxHalfPeriodPacket;
import com.lee.ultrascan.service.packets.SetAlertVoltagePacket;
import com.lee.ultrascan.service.packets.SetBBPControlWord;
import com.lee.ultrascan.service.packets.SetBBPCosPhase0Packet;
import com.lee.ultrascan.service.packets.SetBBPDCCoe;
import com.lee.ultrascan.service.packets.SetBBPDeciRatePacket;
import com.lee.ultrascan.service.packets.SetBBPDeltaPhasePacket;
import com.lee.ultrascan.service.packets.SetBBPLogTablePacket;
import com.lee.ultrascan.service.packets.SetBBPLpfCoePacket;
import com.lee.ultrascan.service.packets.SetBBPSinPhase0Packet;
import com.lee.ultrascan.service.packets.SetBBPSinTablePacket;
import com.lee.ultrascan.service.packets.SetFMotorDockNDirPhasePacket;
import com.lee.ultrascan.service.packets.SetFMotorDockNDirSpeedPacket;
import com.lee.ultrascan.service.packets.SetFMotorDockNDirStepsPacket;
import com.lee.ultrascan.service.packets.SetFMotorDockPDirPhasePacket;
import com.lee.ultrascan.service.packets.SetFMotorDockPDirSpeedPacket;
import com.lee.ultrascan.service.packets.SetFMotorDockPDirStepsPacket;
import com.lee.ultrascan.service.packets.SetFMotorScanNDirSpeedPacket;
import com.lee.ultrascan.service.packets.SetFMotorScanNDirStepsPacket;
import com.lee.ultrascan.service.packets.SetFMotorScanPDirSpeedPacket;
import com.lee.ultrascan.service.packets.SetFMotorScanPDirStepsPacket;
import com.lee.ultrascan.service.packets.SetPowerOffSecondsPacket;
import com.lee.ultrascan.utils.LogUtils;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProbeService extends Service {
    private static final String PROBE_IP = "192.168.0.8";
    private static final int PROBE_PORT = 8888;
    private static final short SCAN_MODE_2D = (short) 2;
    private final IBinder binder = new ProbeServiceBinder();
    private String newApName = "";
    private String newPwd = "";
    private PacketHandler packetHandler;
    private ProbeSocketThread probeSocketThread;
    private ProbeStateListener probeStateListener;
    private ProbeStatus probeStatus;
    private AtomicBoolean skipFrame = new AtomicBoolean(false);
    private Runnable timeoutRunnable = new C10091();
    private WifiHelper wifiHelper;

    class C10091 implements Runnable {
        C10091() {
        }

        public void run() {
            ProbeService.this.probeSocketThread.disconnectProbe();
        }
    }

    private class PacketHandler extends Handler {
        public static final int BATTERY_INTERVAL_MS = 2000;
        private static final short IDLE_FRAMES = (short) 0;
        public static final int TIMEOUT_MS = 5000;
        private volatile boolean isScanning = false;

        public boolean isScanning() {
            return this.isScanning;
        }

        public void setScanning(boolean isScanning) {
            if (isScanning) {
                ProbeService.this.probeSocketThread.sendPacket(new ScanStartPacket((short) 2));
                ProbeService.this.probeSocketThread.sendPacket(new ScanGetFramePacket((short) 0));
            } else {
                ProbeService.this.probeSocketThread.sendPacket(new ScanStopPacket());
            }
            this.isScanning = isScanning;
        }

        public PacketHandler(Looper looper) {
            super(looper);
        }

        public void postBatteryPacket(int delayMs) {
            Message message = obtainMessage();
            message.obj = new BatteryPacket();
            sendMessageDelayed(message, (long) delayMs);
        }

        public void postDisconnected() {
            Message message = obtainMessage();
            message.obj = new DisconnectedPacket();
            sendMessageAtFrontOfQueue(message);
        }

        public void handleMessage(Message msg) {
            LogUtils.LOGI("CONNECTION", "start of handle message.");
            removeCallbacks(ProbeService.this.timeoutRunnable);
            postDelayed(ProbeService.this.timeoutRunnable, 5000);
            Packet packet = msg.obj;
            LogUtils.LOGI("HANDLE_MESSAGE", "handle msg:" + packet.getClass().getSimpleName());
            switch (packet.getId()) {
                case -1:
                    if (ProbeService.this.probeStateListener != null) {
                        ProbeService.this.packetHandler.setScanning(false);
                        ProbeService.this.probeStateListener.onDisconnected();
                        break;
                    }
                    break;
                case 0:
                    ProbeService.this.sendInitParams();
                    ProbeService.this.sendSettingsParams();
                    LogUtils.LOGE("CONNECTION", "after send setting params.");
                    if (ProbeService.this.probeStateListener != null) {
                        ProbeService.this.probeStateListener.onConnected();
                        break;
                    }
                    break;
                case 2:
                    if (!isScanning()) {
                        ProbeService.this.probeSocketThread.sendPacket(packet);
                        break;
                    } else {
                        postBatteryPacket(2000);
                        break;
                    }
                case 3:
                    if (ProbeService.this.probeStateListener != null) {
                        ProbeService.this.probeStateListener.onBattery(((BatteryAckPacket) packet).getVoltageData());
                    }
                    postBatteryPacket(2000);
                    break;
                case 13:
                    if (ProbeService.this.probeStateListener != null) {
                        ProbeService.this.probeStateListener.onProbeButtonClicked();
                        break;
                    }
                    break;
                case 14:
                    String rfid = ((CmdRfidPacket) packet).getRfid();
                    if (ProbeService.this.probeStateListener != null) {
                        ProbeService.this.probeStateListener.onRfid(rfid);
                        break;
                    }
                    break;
                case ProbeMessageID.ID_SCAN_GET_FRAME_ACK /*8195*/:
                    LogUtils.LOGI("changeTgc", "frame ack. isScanning:" + this.isScanning);
                    if (this.isScanning) {
                        LogUtils.LOGI("changeTgc", "send get frame. isScanning:" + this.isScanning);
                        ProbeService.this.probeSocketThread.sendPacket(new ScanGetFramePacket((short) 0));
                        if (!ProbeService.this.skipFrame.getAndSet(false)) {
                            ScanConversionParams scanConversionParams = ProbeService.this.probeStatus.getScanConversionParams();
                            RFFrame rfFrame = new RFFrame(scanConversionParams.getLines(), scanConversionParams.getSamplesPerLine(), ((ScanGetFrameAckPacket) packet).getFrameData().getData());
                            if (ProbeService.this.probeStateListener != null) {
                                ProbeService.this.probeStateListener.onGetFrame(rfFrame, scanConversionParams);
                                break;
                            }
                        }
                    }
                    break;
            }
            LogUtils.LOGI("CONNECTION", "end of handle message.");
            super.handleMessage(msg);
        }
    }

    public class ProbeServiceBinder extends Binder {
        public ProbeService getService() {
            return ProbeService.this;
        }
    }

    private class ProbeSocketThread extends Thread {
        PacketInputStream inputStream;
        private volatile boolean isRunning;
        PacketOutputStream outputStream;
        private Socket probeSocket;
        private ReceivePacketThread receivePacketThread;
        private final Handler sendPacketHandler;
        private final HandlerThread sendPacketThread;

        private ProbeSocketThread() {
            this.isRunning = true;
            this.sendPacketThread = new HandlerThread("SendPacketHandlerThread");
            this.sendPacketThread.start();
            this.sendPacketHandler = new Handler(this.sendPacketThread.getLooper());
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r10 = this;
        L_0x0000:
            r5 = r10.isRunning;
            if (r5 == 0) goto L_0x0131;
        L_0x0004:
            r6 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
            java.lang.Thread.sleep(r6);	 Catch:{ InterruptedException -> 0x00cc }
        L_0x0009:
            r5 = new java.net.Socket;
            r5.<init>();
            r10.probeSocket = r5;
            r5 = r10.probeSocket;	 Catch:{ IOException -> 0x00d2 }
            r6 = new java.net.InetSocketAddress;	 Catch:{ IOException -> 0x00d2 }
            r7 = "192.168.0.8";
            r8 = 8888; // 0x22b8 float:1.2455E-41 double:4.3913E-320;
            r6.<init>(r7, r8);	 Catch:{ IOException -> 0x00d2 }
            r5.connect(r6);	 Catch:{ IOException -> 0x00d2 }
            r5 = "CONNECTION";
            r6 = "probe socket connected.";
            com.lee.ultrascan.utils.LogUtils.LOGI(r5, r6);	 Catch:{ IOException -> 0x00d2 }
            r5 = new com.lee.ultrascan.service.net.PacketInputStream;	 Catch:{ IOException -> 0x00dd }
            r6 = r10.probeSocket;	 Catch:{ IOException -> 0x00dd }
            r6 = r6.getInputStream();	 Catch:{ IOException -> 0x00dd }
            r5.<init>(r6);	 Catch:{ IOException -> 0x00dd }
            r10.inputStream = r5;	 Catch:{ IOException -> 0x00dd }
            r5 = new com.lee.ultrascan.service.net.PacketOutputStream;	 Catch:{ IOException -> 0x00dd }
            r6 = r10.probeSocket;	 Catch:{ IOException -> 0x00dd }
            r6 = r6.getOutputStream();	 Catch:{ IOException -> 0x00dd }
            r5.<init>(r6);	 Catch:{ IOException -> 0x00dd }
            r10.outputStream = r5;	 Catch:{ IOException -> 0x00dd }
            r5 = r10.inputStream;	 Catch:{ IOException -> 0x00dd }
            r2 = r5.readPacketHeader();	 Catch:{ IOException -> 0x00dd }
            r1 = new com.lee.ultrascan.service.packets.ConnectPacket;	 Catch:{ IOException -> 0x00dd }
            r1.<init>(r2);	 Catch:{ IOException -> 0x00dd }
            r5 = r10.inputStream;	 Catch:{ IOException -> 0x00dd }
            r1.readPacketData(r5);	 Catch:{ IOException -> 0x00dd }
            r0 = new com.lee.ultrascan.service.packets.ConnectAckPacket;	 Catch:{ IOException -> 0x00dd }
            r0.<init>();	 Catch:{ IOException -> 0x00dd }
            r5 = r10.outputStream;	 Catch:{ IOException -> 0x00dd }
            r0.writePacket(r5);	 Catch:{ IOException -> 0x00dd }
            r5 = com.lee.ultrascan.service.ProbeService.this;	 Catch:{ IOException -> 0x00dd }
            r5 = r5.packetHandler;	 Catch:{ IOException -> 0x00dd }
            r3 = r5.obtainMessage();	 Catch:{ IOException -> 0x00dd }
            r3.obj = r1;	 Catch:{ IOException -> 0x00dd }
            r5 = com.lee.ultrascan.service.ProbeService.this;	 Catch:{ IOException -> 0x00dd }
            r5 = r5.packetHandler;	 Catch:{ IOException -> 0x00dd }
            r5.sendMessage(r3);	 Catch:{ IOException -> 0x00dd }
            r5 = new com.lee.ultrascan.service.ReceivePacketThread;
            r6 = r10.inputStream;
            r7 = com.lee.ultrascan.service.ProbeService.this;
            r7 = r7.packetHandler;
            r5.<init>(r6, r7);
            r10.receivePacketThread = r5;
            r5 = r10.receivePacketThread;
            r5.start();
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r6 = com.lee.ultrascan.service.ProbeService.this;
            r6 = r6.timeoutRunnable;
            r8 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r5.postDelayed(r6, r8);
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r6 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
            r5.postBatteryPacket(r6);
            r5 = "CONNECTION";
            r6 = "before receive packet thread join.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            r5 = r10.receivePacketThread;	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            r5.join();	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            r5 = r10.probeSocket;	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            if (r5 == 0) goto L_0x00ba;
        L_0x00ae:
            r5 = r10.probeSocket;	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            r5.close();	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
            r5 = "CONNECTION";
            r6 = "probe socket closed.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);	 Catch:{ InterruptedException -> 0x00f3, IOException -> 0x0109 }
        L_0x00ba:
            r5 = "CONNECTION";
            r6 = "post disconnected LINE 527.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r5.postDisconnected();
            goto L_0x0000;
        L_0x00cc:
            r4 = move-exception;
            r4.printStackTrace();
            goto L_0x0009;
        L_0x00d2:
            r4 = move-exception;
            r5 = r10.probeSocket;	 Catch:{ IOException -> 0x0139 }
            r5.close();	 Catch:{ IOException -> 0x0139 }
        L_0x00d8:
            r4.printStackTrace();
            goto L_0x0000;
        L_0x00dd:
            r4 = move-exception;
            r4.printStackTrace();
            r5 = "CONNECTION";
            r6 = "post disconnected LINE507.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r5.postDisconnected();
            goto L_0x0000;
        L_0x00f3:
            r4 = move-exception;
            r4.printStackTrace();	 Catch:{ all -> 0x011f }
            r5 = "CONNECTION";
            r6 = "post disconnected LINE 527.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r5.postDisconnected();
            goto L_0x0000;
        L_0x0109:
            r4 = move-exception;
            r4.printStackTrace();	 Catch:{ all -> 0x011f }
            r5 = "CONNECTION";
            r6 = "post disconnected LINE 527.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r5, r6);
            r5 = com.lee.ultrascan.service.ProbeService.this;
            r5 = r5.packetHandler;
            r5.postDisconnected();
            goto L_0x0000;
        L_0x011f:
            r5 = move-exception;
            r6 = "CONNECTION";
            r7 = "post disconnected LINE 527.";
            com.lee.ultrascan.utils.LogUtils.LOGE(r6, r7);
            r6 = com.lee.ultrascan.service.ProbeService.this;
            r6 = r6.packetHandler;
            r6.postDisconnected();
            throw r5;
        L_0x0131:
            r5 = "Service Thread";
            r6 = "ConnectingThread stopped.";
            com.lee.ultrascan.utils.LogUtils.LOGI(r5, r6);
            return;
        L_0x0139:
            r5 = move-exception;
            goto L_0x00d8;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.lee.ultrascan.service.ProbeService.ProbeSocketThread.run():void");
        }

        public void stopRunning() throws InterruptedException {
            if (currentThread().getId() == getId()) {
                throw new RuntimeException("can not call stopRunning in ProbeSocketThread.");
            }
            this.isRunning = false;
            if (this.receivePacketThread != null && this.receivePacketThread.isAlive()) {
                this.receivePacketThread.stopRunning();
            }
            join();
        }

        public void sendPacket(final Packet packet) {
            if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
                throw new RuntimeException("can only sendPacket in mainthread.");
            }
            switch (packet.getId()) {
                case ProbeMessageID.ID_SET2D_RX_TGC_PROFILE /*16390*/:
                    ProbeService.this.probeStateListener.onUpdateTgc(((Set2DRxTgcProfilePacket) packet).getOffset());
                    break;
            }
            this.sendPacketHandler.post(new Runnable() {
                public void run() {
                    if (ProbeSocketThread.this.outputStream != null) {
                        try {
                            packet.writePacket(ProbeSocketThread.this.outputStream);
                        } catch (IOException e) {
                            LogUtils.LOGE("CONNECTION", "send packet failed.");
                            e.printStackTrace();
                            try {
                                if (ProbeSocketThread.this.receivePacketThread != null) {
                                    ProbeSocketThread.this.receivePacketThread.stopRunning();
                                }
                            } catch (InterruptedException e2) {
                            }
                        }
                    }
                }
            });
        }

        public void disconnectProbe() {
            try {
                LogUtils.LOGI("CONNECTION", "before receive packet thread stopRunning.");
                if (this.receivePacketThread != null) {
                    this.receivePacketThread.stopRunning();
                }
                LogUtils.LOGI("CONNECTION", "after receive packet thread stopRunning.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ProbeStateListener {
        void onBattery(short s);

        void onConnected();

        void onDisconnected();

        void onGetFrame(RFFrame rFFrame, ScanConversionParams scanConversionParams);

        void onProbeButtonClicked();

        void onReset();

        void onRfid(String str);

        void onUpdateDepth(DepthType depthType);

        void onUpdateFreq(FrequencyType frequencyType);

        void onUpdateTgc(short s);
    }

    public void setProbeWifi(String newApName, String newPwd) {
        this.newApName = newApName;
        this.newPwd = newPwd;
    }

    public void setProbeStateListener(ProbeStateListener probeStateListener) {
        this.probeStateListener = probeStateListener;
    }

    public void setScanConversionImageSize(int width, int height) {
        this.probeStatus.setScanConversionImageSize(width, height);
    }

    public final ProbeStatus getProbeStatus() {
        return this.probeStatus;
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void onCreate() {
        super.onCreate();
        this.wifiHelper = new WifiHelper(this);
        this.packetHandler = new PacketHandler(getMainLooper());
        this.probeStatus = new ProbeStatus(PreferenceManager.getDefaultSharedPreferences(this).getInt(ProbeParams.RX_LINE_PER_FRAME_KEY, 109));
        this.probeSocketThread = new ProbeSocketThread();
        this.probeSocketThread.start();
        LogUtils.LOGI("probe", "probe service onCreate");
    }

    public void onDestroy() {
        if (this.probeSocketThread != null) {
            try {
                this.probeSocketThread.stopRunning();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LogUtils.LOGI("probe", "probe service onDestroy");
        super.onDestroy();
    }

    protected void sendInitParams() {
        int i = 0;
        Packet[] initParamPackets = new Packet[]{new SetFMotorDockPDirSpeedPacket((short) 0), new SetFMotorDockPDirStepsPacket((short) 0), new SetFMotorDockPDirPhasePacket((short) 1), new SetFMotorDockNDirSpeedPacket((short) 28), new SetFMotorDockNDirStepsPacket((short) 30), new SetFMotorDockNDirPhasePacket((short) 2), new SetFMotorScanPDirSpeedPacket((short) 14), new SetFMotorScanPDirStepsPacket((short) 30), new SetFMotorScanNDirSpeedPacket((short) 14), new SetFMotorScanNDirStepsPacket((short) 30), new ScanSetRxDeadZonePacket((short) 125), new Set2DTxHalfPeriodPacket(this.probeStatus.getFrequencyType().getTxHalfPeriod()), new Set2DTxCyclesPacket((short) 2), new Set2DRxGateStartPacket((short) 0), new Set2DRxGateSizePacket(this.probeStatus.getDepthType().getGateSize()), new Set2DRxLinePtrPacket(ProbeParams.RX_LINE_PRT), new Set2DRxLinePerFramePacket((short) 109), new Set2DRxTgcBreakSizePacket((short) 5), new Set2DRxBothDirectionsPacket((short) 0), new SetBBPSinPhase0Packet(0), new SetBBPCosPhase0Packet(4194304), new SetBBPDeltaPhasePacket(ProbeParams.BBP_DELTA_PHASE), new SetBBPDeciRatePacket(this.probeStatus.getDepthType().getDeciRate()), new SetBBPLpfCoePacket(ProbeParams.LPF_COE), new SetBBPDCCoe(ProbeParams.BBP_DC_COE), new DockFMotorPacket()};
        int length = initParamPackets.length;
        while (i < length) {
            this.probeSocketThread.sendPacket(initParamPackets[i]);
            i++;
        }
        SetBBPSinTablePacket bbpSinTablePacket = new SetBBPSinTablePacket(ProbeParams.SIN_TABLE);
        TgcCurve tgcCurve = this.probeStatus.getFrequencyType().getTgcCurve();
        Set2DRxTgcProfilePacket rxTgcProfilePacket = new Set2DRxTgcProfilePacket(tgcCurve.getTgcCurveData(), tgcCurve.getOffset());
        SetBBPLogTablePacket bbpLogTablePacket = new SetBBPLogTablePacket(ProbeParams.LOG_TABLE);
        try {
            Thread.sleep(100);
            this.probeSocketThread.sendPacket(bbpSinTablePacket);
            Thread.sleep(100);
            this.probeSocketThread.sendPacket(rxTgcProfilePacket);
            Thread.sleep(100);
            this.probeSocketThread.sendPacket(bbpLogTablePacket);
            Thread.sleep(100);
            LogUtils.LOGI("packet", "init packets end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isScanning() {
        return this.packetHandler.isScanning();
    }

    public void changeTgc(short tgc) {
        this.probeStatus.setTgc(tgc);
        if (this.probeSocketThread != null && this.probeSocketThread.isAlive()) {
            TgcCurve tgcCurve = this.probeStatus.getFrequencyType().getTgcCurve();
            if (this.packetHandler.isScanning()) {
                LogUtils.LOGI("changeTgc", "send scanstop.");
                this.probeSocketThread.sendPacket(new ScanStopPacket());
                LogUtils.LOGI("changeTgc", "send tgc.");
                this.probeSocketThread.sendPacket(new Set2DRxTgcProfilePacket(tgcCurve.getTgcCurveData(), tgcCurve.getOffset()));
                LogUtils.LOGI("changeTgc", "send scanstart.");
                this.probeSocketThread.sendPacket(new ScanStartPacket((short) 0));
                return;
            }
            LogUtils.LOGI("changeTgc", "send tgc.");
            this.probeSocketThread.sendPacket(new Set2DRxTgcProfilePacket(tgcCurve.getTgcCurveData(), tgcCurve.getOffset()));
        }
    }

    public void changeFreq(FrequencyType freq) {
        this.probeStatus.setFrequencyType(freq);
        if (this.probeSocketThread != null && this.probeSocketThread.isAlive()) {
            if (this.packetHandler.isScanning()) {
                this.probeSocketThread.sendPacket(new ScanStopPacket());
                this.probeSocketThread.sendPacket(new Set2DTxHalfPeriodPacket(freq.getTxHalfPeriod()));
                this.probeSocketThread.sendPacket(new Set2DRxTgcProfilePacket(freq.getTgcCurve().getTgcCurveData(), freq.getTgcCurve().getOffset()));
                this.probeSocketThread.sendPacket(new ScanStartPacket((short) 0));
                return;
            }
            this.probeSocketThread.sendPacket(new Set2DTxHalfPeriodPacket(freq.getTxHalfPeriod()));
            this.probeSocketThread.sendPacket(new Set2DRxTgcProfilePacket(freq.getTgcCurve().getTgcCurveData(), freq.getTgcCurve().getOffset()));
        }
    }

    public void changeDepth(DepthType depth) {
        this.probeStatus.setDepthType(depth);
        if (this.probeSocketThread != null && this.probeSocketThread.isAlive()) {
            if (this.packetHandler.isScanning) {
                this.probeSocketThread.sendPacket(new ScanStopPacket());
                this.probeSocketThread.sendPacket(new Set2DRxGateSizePacket(depth.getGateSize()));
                this.probeSocketThread.sendPacket(new SetBBPDeciRatePacket(depth.getDeciRate()));
                this.probeSocketThread.sendPacket(new ScanStartPacket((short) 0));
            } else {
                this.probeSocketThread.sendPacket(new Set2DRxGateSizePacket(depth.getGateSize()));
                this.probeSocketThread.sendPacket(new SetBBPDeciRatePacket(depth.getDeciRate()));
            }
            this.skipFrame.set(true);
        }
    }

    public void startScanning() {
        if (this.probeSocketThread != null && this.probeSocketThread.isAlive()) {
            LogUtils.LOGI("packet", "start scanning.");
            this.packetHandler.setScanning(true);
        }
    }

    public void stopScanning() {
        if (this.probeSocketThread != null && this.probeSocketThread.isAlive()) {
            LogUtils.LOGI("packet", "stop scanning.");
            this.packetHandler.setScanning(false);
        }
    }

    public void sendSettingsParams() {
        if (this.probeSocketThread != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            short bbpControlWord = (short) 0;
            if (prefs.getBoolean(ProbeParams.BBP_CTL_CIC_BYPASS_KEY, false)) {
                bbpControlWord = (short) 32;
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DCONV_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 16);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DZONE_ZERO_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 2);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_ENV_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 512);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_ENV_BYPASS_BY_DI_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 1024);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_LOG_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 16384);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_LPF_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 256);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DECI0_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 64);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DECI0_INCREMENT_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 128);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_LOG_INCREMENT_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | -32768);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_ABS1_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 8192);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_ABS0_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 8);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DECI1_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 2048);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DECI1_INCREMENT_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 4096);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DIN_INCREMENT_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 1);
            }
            if (prefs.getBoolean(ProbeParams.BBP_CTL_DC_BYPASS_KEY, false)) {
                bbpControlWord = (short) (bbpControlWord | 4);
            }
            short power_off_seconds = (short) (prefs.getInt(ProbeParams.POWER_OFF_MINUTES_KEY, 8) * 60);
            byte alert_voltage_low = (byte) prefs.getInt(ProbeParams.ALERT_VOLTAGE_LOW_KEY, -96);
            byte alert_voltage_cancel = (byte) prefs.getInt(ProbeParams.ALERT_VOLTAGE_CANCEL_KEY, -88);
            short bbpDCCoe = (short) prefs.getInt(ProbeParams.BBP_DC_COE_KEY, 0);
            short fmotor_dock_ndir_phase = (short) prefs.getInt(ProbeParams.FMOTOR_DOCK_NDIR_PHASE_KEY, 2);
            LogUtils.LOGI("settings param", "send settings param:" + bbpControlWord);
            LogUtils.LOGI("settings param", "send bbpDCCoe param:" + bbpDCCoe);
            LogUtils.LOGI("settings param", "send fmotor_dock_ndir_phase param:" + fmotor_dock_ndir_phase);
            LogUtils.LOGI("settings param", "send power_off_seoncds param:" + power_off_seconds);
            LogUtils.LOGI("settings param", "send alert_voltage low:" + alert_voltage_low + " alert_voltage cancel:" + alert_voltage_cancel);
            this.probeSocketThread.sendPacket(new SetBBPControlWord(bbpControlWord));
            this.probeSocketThread.sendPacket(new SetBBPDCCoe(bbpDCCoe));
            this.probeSocketThread.sendPacket(new SetFMotorDockNDirPhasePacket(fmotor_dock_ndir_phase));
            this.probeSocketThread.sendPacket(new SetPowerOffSecondsPacket(power_off_seconds));
            this.probeSocketThread.sendPacket(new SetAlertVoltagePacket(alert_voltage_low, alert_voltage_cancel));
            short tx_half_period = (short) prefs.getInt(ProbeParams.TX_HALF_PERIOD_KEY, 12);
            float bbp_delta_phase = prefs.getFloat(ProbeParams.BBP_DELTA_PHASE_KEY, ProbeParams.BBP_DELTA_PHASE);
            short tx_cycles = (short) prefs.getInt(ProbeParams.TX_CYCLES_KEY, 2);
            short rx_line_per_frame = (short) prefs.getInt(ProbeParams.RX_LINE_PER_FRAME_KEY, 109);
            short rx_line_prt = (short) prefs.getInt(ProbeParams.RX_LINE_PRT_KEY, 7694);
            LogUtils.LOGI("settings param", "send tx_half_period param:" + tx_half_period);
            LogUtils.LOGI("settings param", "send bbp_delta_phase param:" + bbp_delta_phase);
            LogUtils.LOGI("settings param", "send tx_cycles param:" + tx_cycles);
            LogUtils.LOGI("settings param", "send rx_line_per_frame param:" + rx_line_per_frame);
            LogUtils.LOGI("settings param", "send rx_line_prt param:" + rx_line_prt);
            this.probeSocketThread.sendPacket(new Set2DTxHalfPeriodPacket(tx_half_period));
            this.probeSocketThread.sendPacket(new SetBBPDeltaPhasePacket(bbp_delta_phase));
            this.probeSocketThread.sendPacket(new Set2DTxCyclesPacket(tx_cycles));
            this.probeSocketThread.sendPacket(new Set2DRxLinePerFramePacket(rx_line_per_frame));
            this.probeSocketThread.sendPacket(new Set2DRxLinePtrPacket(rx_line_prt));
            this.probeStatus.updateScanConversionLines(rx_line_per_frame);
        }
    }
}
