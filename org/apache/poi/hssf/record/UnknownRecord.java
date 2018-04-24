package org.apache.poi.hssf.record;

import com.lee.ultrascan.service.ProbeMessageID;
import java.util.Locale;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

public final class UnknownRecord extends StandardRecord {
    public static final int BITMAP_00E9 = 233;
    public static final int CODENAME_1BA = 442;
    public static final int HEADER_FOOTER_089C = 2204;
    public static final int LABELRANGES_015F = 351;
    public static final int PHONETICPR_00EF = 239;
    public static final int PLS_004D = 77;
    public static final int PLV_MAC = 2248;
    public static final int PRINTSIZE_0033 = 51;
    public static final int QUICKTIP_0800 = 2048;
    public static final int SCL_00A0 = 160;
    public static final int SHEETEXT_0862 = 2146;
    public static final int SHEETPROTECTION_0867 = 2151;
    public static final int SHEETPR_0081 = 129;
    public static final int SORT_0090 = 144;
    public static final int STANDARDWIDTH_0099 = 153;
    private byte[] _rawData;
    private int _sid;

    public UnknownRecord(int id, byte[] data) {
        this._sid = 65535 & id;
        this._rawData = data;
    }

    public UnknownRecord(RecordInputStream in) {
        this._sid = in.getSid();
        this._rawData = in.readRemainder();
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this._rawData);
    }

    protected int getDataSize() {
        return this._rawData.length;
    }

    public String toString() {
        String biffName = getBiffName(this._sid);
        if (biffName == null) {
            biffName = "UNKNOWNRECORD";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(biffName).append("] (0x");
        sb.append(Integer.toHexString(this._sid).toUpperCase(Locale.ROOT) + ")\n");
        if (this._rawData.length > 0) {
            sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
        }
        sb.append("[/").append(biffName).append("]\n");
        return sb.toString();
    }

    public short getSid() {
        return (short) this._sid;
    }

    public static String getBiffName(int sid) {
        switch (sid) {
            case 51:
                return "PRINTSIZE";
            case 77:
                return "PLS";
            case 80:
                return "DCON";
            case 127:
                return "IMDATA";
            case 129:
                return "SHEETPR";
            case 144:
                return "SORT";
            case 148:
                return "LHRECORD";
            case 153:
                return "STANDARDWIDTH";
            case 160:
                return "SCL";
            case 174:
                return "SCENMAN";
            case 178:
                return "SXVI";
            case 180:
                return "SXIVD";
            case 181:
                return "SXLI";
            case 211:
                return "OBPROJ";
            case 220:
                return "PARAMQRY";
            case 222:
                return "OLESIZE";
            case BITMAP_00E9 /*233*/:
                return "BITMAP";
            case PHONETICPR_00EF /*239*/:
                return "PHONETICPR";
            case 241:
                return "SXEX";
            case LABELRANGES_015F /*351*/:
                return "LABELRANGES";
            case dc1394.DC1394_FEATURE_IRIS /*425*/:
                return "USERBVIEW";
            case 429:
                return "QSI";
            case 442:
                return "CODENAME";
            case opencv_videoio.CV_CAP_PROP_XI_WB_KR /*448*/:
                return "EXCEL9FILE";
            case 2048:
                return "QUICKTIP";
            case 2050:
                return "QSISXTAG";
            case 2051:
                return "DBQUERYEXT";
            case 2053:
                return "TXTQUERY";
            case 2064:
                return "SXVIEWEX9";
            case 2066:
                return "CONTINUEFRT";
            case SHEETEXT_0862 /*2146*/:
                return "SHEETEXT";
            case 2147:
                return "BOOKEXT";
            case 2148:
                return "SXADDL";
            case SHEETPROTECTION_0867 /*2151*/:
                return "SHEETPROTECTION";
            case 2155:
                return "DATALABEXTCONTENTS";
            case 2156:
                return "CELLWATCH";
            case 2162:
                return "SHARED FEATURE v11";
            case 2164:
                return "DROPDOWNOBJIDS";
            case 2166:
                return "DCONN";
            case 2168:
                return "SHARED FEATURE v12";
            case 2171:
                return "CFEX";
            case 2172:
                return "XFCRC";
            case 2173:
                return "XFEXT";
            case 2175:
                return "CONTINUEFRT12";
            case 2187:
                return "PLV";
            case 2188:
                return "COMPAT12";
            case 2189:
                return "DXF";
            case 2194:
                return "STYLEEXT";
            case 2198:
                return "THEME";
            case 2199:
                return "GUIDTYPELIB";
            case 2202:
                return "MTRSETTINGS";
            case 2203:
                return "COMPRESSPICTURES";
            case HEADER_FOOTER_089C /*2204*/:
                return "HEADERFOOTER";
            case 2205:
                return "CRTLAYOUT12";
            case 2206:
                return "CRTMLFRT";
            case 2207:
                return "CRTMLFRTCONTINUE";
            case 2209:
                return "SHAPEPROPSSTREAM";
            case 2211:
                return "FORCEFULLCALCULATION";
            case 2212:
                return "SHAPEPROPSSTREAM";
            case 2213:
                return "TEXTPROPSSTREAM";
            case 2214:
                return "RICHTEXTSTREAM";
            case 2215:
                return "CRTLAYOUT12A";
            case PLV_MAC /*2248*/:
                return "PLV{Mac Excel}";
            case 4097:
                return "UNITS";
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_SPEED /*4102*/:
                return "CHARTDATAFORMAT";
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_STEPS /*4103*/:
                return "CHARTLINEFORMAT";
            default:
                if (isObservedButUnknown(sid)) {
                    return "UNKNOWN-" + Integer.toHexString(sid).toUpperCase(Locale.ROOT);
                }
                return null;
        }
    }

    private static boolean isObservedButUnknown(int sid) {
        switch (sid) {
            case 51:
            case 52:
            case opencv_videoio.CV_CAP_PROP_XI_BPC /*445*/:
            case opencv_videoio.CV_CAP_PROP_XI_WB_KB /*450*/:
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_NDIR_STEPS /*4105*/:
            case ProbeMessageID.ID_SET_FMOTOR_MOVE_PDIR_SPEED /*4106*/:
            case ProbeMessageID.ID_SET_FMOTOR_MOVE_NDIR_SPEED /*4107*/:
            case ProbeMessageID.ID_DOCK_FMOTOR /*4108*/:
            case 4116:
            case 4119:
            case 4120:
            case 4121:
            case 4122:
            case 4123:
            case 4125:
            case 4126:
            case 4127:
            case 4128:
            case 4129:
            case 4130:
            case 4132:
            case 4133:
            case 4134:
            case 4135:
            case 4146:
            case 4147:
            case 4148:
            case 4149:
            case 4154:
            case 4161:
            case 4163:
            case 4164:
            case 4165:
            case 4166:
            case 4170:
            case 4171:
            case 4174:
            case 4175:
            case 4177:
            case 4188:
            case 4189:
            case 4191:
            case 4192:
            case 4194:
            case 4195:
            case 4196:
            case 4197:
            case 4198:
                return true;
            default:
                return false;
        }
    }

    public Object clone() {
        return this;
    }
}
