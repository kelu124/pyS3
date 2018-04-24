package com.lee.ultrascan.service;

import com.lee.ultrascan.service.packets.BatteryAckPacket;
import com.lee.ultrascan.service.packets.ButtonEventPacket;
import com.lee.ultrascan.service.packets.CmdRfidPacket;
import com.lee.ultrascan.service.packets.ConnectPacket;
import com.lee.ultrascan.service.packets.Packet;
import com.lee.ultrascan.service.packets.PacketHeader;
import com.lee.ultrascan.service.packets.ScanGetFrameAckPacket;
import com.lee.ultrascan.service.packets.SettingsAckPacket;
import com.lee.ultrascan.service.packets.UpgradeAckPacket;
import com.lee.ultrascan.service.packets.WakeupAckPacket;
import com.lee.ultrascan.utils.LogUtils;

public class ProbeMessageID {
    public static final int ID_BATTERY = 2;
    public static final int ID_BATTERY_ACK = 3;
    public static final int ID_BUTTON_EVENT = 13;
    public static final int ID_CMD_RFID = 14;
    public static final int ID_CONNECT = 0;
    public static final int ID_CONNECT_ACK = 1;
    public static final int ID_DISCONNECTED = -1;
    public static final int ID_DOCK_FMOTOR = 4108;
    public static final int ID_MOVE_FMOTOR = 4109;
    public static final int ID_RESET = 7;
    public static final int ID_SCAN_GET_FRAME = 8194;
    public static final int ID_SCAN_GET_FRAME_ACK = 8195;
    public static final int ID_SCAN_SET_RX_DEAD_ZONE = 8196;
    public static final int ID_SCAN_START = 8192;
    public static final int ID_SCAN_STOP = 8193;
    public static final int ID_SEND_DATA = 10;
    public static final int ID_SET2D_RX_BOTH_DIRECTIONS = 16392;
    public static final int ID_SET2D_RX_GATE_SIZE = 16387;
    public static final int ID_SET2D_RX_GATE_START = 16386;
    public static final int ID_SET2D_RX_LINE_PER_FRAME = 16389;
    public static final int ID_SET2D_RX_LINE_PRT = 16388;
    public static final int ID_SET2D_RX_TGC_BREAK_SIZE = 16391;
    public static final int ID_SET2D_RX_TGC_PROFILE = 16390;
    public static final int ID_SET2D_TX_CYCLES = 16385;
    public static final int ID_SET2D_TX_HALF_PERIOD = 16384;
    public static final int ID_SETTINGS = 28672;
    public static final int ID_SETTINGS_ACK = 28673;
    public static final int ID_SET_ALERT_VOLTAGE = 12;
    public static final int ID_SET_BBP_CONTROL_WORD = 24583;
    public static final int ID_SET_BBP_COS_PHASE0 = 24577;
    public static final int ID_SET_BBP_DC_COE = 24584;
    public static final int ID_SET_BBP_DECI_RATE = 24580;
    public static final int ID_SET_BBP_DELTA_PHASE = 24579;
    public static final int ID_SET_BBP_LOG_TABLE = 24582;
    public static final int ID_SET_BBP_LPF_COE = 24581;
    public static final int ID_SET_BBP_SIN_PHASE0 = 24578;
    public static final int ID_SET_BBP_SIN_TABLE = 24576;
    public static final int ID_SET_FMOTOR_DOCK_NDIR_PHASE = 4101;
    public static final int ID_SET_FMOTOR_DOCK_NDIR_SPEED = 4099;
    public static final int ID_SET_FMOTOR_DOCK_NDIR_STEPS = 4100;
    public static final int ID_SET_FMOTOR_DOCK_PDIR_PHASE = 4098;
    public static final int ID_SET_FMOTOR_DOCK_PDIR_SPEED = 4096;
    public static final int ID_SET_FMOTOR_DOCK_PDIR_STEPS = 4097;
    public static final int ID_SET_FMOTOR_MOVE_NDIR_SPEED = 4107;
    public static final int ID_SET_FMOTOR_MOVE_PDIR_SPEED = 4106;
    public static final int ID_SET_FMOTOR_SCAN_NDIR_SPEED = 4104;
    public static final int ID_SET_FMOTOR_SCAN_NDIR_STEPS = 4105;
    public static final int ID_SET_FMOTOR_SCAN_PDIR_SPEED = 4102;
    public static final int ID_SET_FMOTOR_SCAN_PDIR_STEPS = 4103;
    public static final int ID_SET_POWER_OFF_SECONDS = 11;
    public static final int ID_SLEEP = 4;
    public static final int ID_UPGRADE = 8;
    public static final int ID_UPGRADE_ACK = 9;
    public static final int ID_WAKEUP = 5;
    public static final int ID_WAKEUP_ACK = 6;

    public static Packet createReadPacketByHeader(PacketHeader packetHeader) {
        LogUtils.LOGI("packets", "id:" + packetHeader.id + " length:" + packetHeader.length);
        switch (packetHeader.id) {
            case 0:
                return new ConnectPacket(packetHeader);
            case 3:
                LogUtils.LOGI("packets", "received battery ack.");
                return new BatteryAckPacket(packetHeader);
            case 6:
                return new WakeupAckPacket(packetHeader);
            case 9:
                return new UpgradeAckPacket(packetHeader);
            case 13:
                return new ButtonEventPacket(packetHeader);
            case 14:
                LogUtils.LOGI("packets", "create RfidPacket.");
                return new CmdRfidPacket(packetHeader);
            case ID_SCAN_GET_FRAME_ACK /*8195*/:
                LogUtils.LOGI("packets", "received frame ack.");
                return new ScanGetFrameAckPacket(packetHeader);
            case ID_SETTINGS_ACK /*28673*/:
                return new SettingsAckPacket(packetHeader);
            default:
                return null;
        }
    }
}
