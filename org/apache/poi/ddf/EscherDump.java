package org.apache.poi.ddf;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.itextpdf.text.Element;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.itextpdf.xmp.XMPError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.InflaterInputStream;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.HexRead;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndian.BufferUnderrunException;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avformat.AVStream;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

public final class EscherDump {

    final class AnonymousClass1PropName {
        final int _id;
        final String _name;

        public AnonymousClass1PropName(int id, String name) {
            this._id = id;
            this._name = name;
        }
    }

    public void dump(byte[] data, int offset, int size, PrintStream out) {
        EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
        int pos = offset;
        while (pos < offset + size) {
            EscherRecord r = recordFactory.createRecord(data, pos);
            int bytesRead = r.fillFields(data, pos, recordFactory);
            out.println(r.toString());
            pos += bytesRead;
        }
    }

    public void dumpOld(long maxLength, InputStream in, PrintStream out) throws IOException, BufferUnderrunException {
        long remainingBytes = maxLength;
        while (!false && remainingBytes > 0) {
            String recordName;
            short nDumpSize;
            short options = LittleEndian.readShort(in);
            short recordId = LittleEndian.readShort(in);
            int recordBytesRemaining = LittleEndian.readInt(in);
            remainingBytes -= 8;
            switch (recordId) {
                case (short) -4096:
                    recordName = "MsofbtDggContainer";
                    break;
                case (short) -4095:
                    recordName = "MsofbtBstoreContainer";
                    break;
                case (short) -4094:
                    recordName = "MsofbtDgContainer";
                    break;
                case (short) -4093:
                    recordName = "MsofbtSpgrContainer";
                    break;
                case (short) -4092:
                    recordName = "MsofbtSpContainer";
                    break;
                case (short) -4091:
                    recordName = "MsofbtSolverContainer";
                    break;
                case (short) -4090:
                    recordName = EscherDggRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4089:
                    recordName = EscherBSERecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4088:
                    recordName = EscherDgRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4087:
                    recordName = EscherSpgrRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4086:
                    recordName = EscherSpRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4085:
                    recordName = "MsofbtOPT";
                    break;
                case (short) -4084:
                    recordName = "MsofbtTextbox";
                    break;
                case (short) -4083:
                    recordName = "MsofbtClientTextbox";
                    break;
                case (short) -4082:
                    recordName = "MsofbtAnchor";
                    break;
                case (short) -4081:
                    recordName = EscherChildAnchorRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4080:
                    recordName = EscherClientAnchorRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4079:
                    recordName = EscherClientDataRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -4078:
                    recordName = "MsofbtConnectorRule";
                    break;
                case (short) -4077:
                    recordName = "MsofbtAlignRule";
                    break;
                case (short) -4076:
                    recordName = "MsofbtArcRule";
                    break;
                case (short) -4075:
                    recordName = "MsofbtClientRule";
                    break;
                case (short) -4074:
                    recordName = "MsofbtCLSID";
                    break;
                case (short) -4073:
                    recordName = "MsofbtCalloutRule";
                    break;
                case (short) -3816:
                    recordName = "MsofbtRegroupItem";
                    break;
                case (short) -3815:
                    recordName = "MsofbtSelection";
                    break;
                case (short) -3814:
                    recordName = "MsofbtColorMRU";
                    break;
                case (short) -3811:
                    recordName = "MsofbtDeletedPspl";
                    break;
                case (short) -3810:
                    recordName = EscherSplitMenuColorsRecord.RECORD_DESCRIPTION;
                    break;
                case (short) -3809:
                    recordName = "MsofbtOleObject";
                    break;
                case (short) -3808:
                    recordName = "MsofbtColorScheme";
                    break;
                case (short) -3806:
                    recordName = "MsofbtUDefProp";
                    break;
                default:
                    if (recordId < (short) -4072 || recordId > (short) -3817) {
                        if ((options & 15) != 15) {
                            recordName = "UNKNOWN ID";
                            break;
                        } else {
                            recordName = "UNKNOWN container";
                            break;
                        }
                    }
                    recordName = "MsofbtBLIP";
                    break;
                    break;
            }
            StringBuilder stringBuf = new StringBuilder();
            stringBuf.append("  ");
            stringBuf.append(HexDump.toHex(recordId));
            stringBuf.append("  ").append(recordName).append(" [");
            stringBuf.append(HexDump.toHex(options));
            stringBuf.append(',');
            stringBuf.append(HexDump.toHex(recordBytesRemaining));
            stringBuf.append("]  instance: ");
            stringBuf.append(HexDump.toHex((short) (options >> 4)));
            out.println(stringBuf.toString());
            stringBuf.setLength(0);
            if (recordId == (short) -4089 && 36 <= remainingBytes && 36 <= recordBytesRemaining) {
                stringBuf = stringBuf.append("    btWin32: ");
                byte n8 = (byte) in.read();
                stringBuf.append(HexDump.toHex(n8));
                stringBuf.append(getBlipType(n8));
                stringBuf.append("  btMacOS: ");
                n8 = (byte) in.read();
                stringBuf.append(HexDump.toHex(n8));
                stringBuf.append(getBlipType(n8));
                out.println(stringBuf.toString());
                out.println("    rgbUid:");
                HexDump.dump(in, out, 0, 16);
                out.print("    tag: ");
                outHex(2, in, out);
                out.println();
                out.print("    size: ");
                outHex(4, in, out);
                out.println();
                out.print("    cRef: ");
                outHex(4, in, out);
                out.println();
                out.print("    offs: ");
                outHex(4, in, out);
                out.println();
                out.print("    usage: ");
                outHex(1, in, out);
                out.println();
                out.print("    cbName: ");
                outHex(1, in, out);
                out.println();
                out.print("    unused2: ");
                outHex(1, in, out);
                out.println();
                out.print("    unused3: ");
                outHex(1, in, out);
                out.println();
                remainingBytes -= 36;
                recordBytesRemaining = 0;
            } else if (recordId == (short) -4080 && 18 <= remainingBytes && 18 <= recordBytesRemaining) {
                out.print("    Flag: ");
                outHex(2, in, out);
                out.println();
                out.print("    Col1: ");
                outHex(2, in, out);
                out.print("    dX1: ");
                outHex(2, in, out);
                out.print("    Row1: ");
                outHex(2, in, out);
                out.print("    dY1: ");
                outHex(2, in, out);
                out.println();
                out.print("    Col2: ");
                outHex(2, in, out);
                out.print("    dX2: ");
                outHex(2, in, out);
                out.print("    Row2: ");
                outHex(2, in, out);
                out.print("    dY2: ");
                outHex(2, in, out);
                out.println();
                remainingBytes -= 18;
                recordBytesRemaining -= 18;
            } else if (recordId == (short) -4085 || recordId == (short) -3806) {
                int nComplex = 0;
                out.println("    PROPID        VALUE");
                while (recordBytesRemaining >= nComplex + 6 && remainingBytes >= ((long) (nComplex + 6))) {
                    short n16 = LittleEndian.readShort(in);
                    int n32 = LittleEndian.readInt(in);
                    recordBytesRemaining -= 6;
                    remainingBytes -= 6;
                    out.print("    ");
                    out.print(HexDump.toHex(n16));
                    out.print(" (");
                    int propertyId = n16 & 16383;
                    out.print(" " + propertyId);
                    if ((n16 & -32768) == 0) {
                        if ((n16 & 16384) != 0) {
                            out.print(", fBlipID");
                        }
                        out.print(")  ");
                        out.print(HexDump.toHex(n32));
                        if ((n16 & 16384) == 0) {
                            out.print(" (");
                            out.print(dec1616(n32));
                            out.print(')');
                            out.print(" {" + propName((short) propertyId) + "}");
                        }
                        out.println();
                    } else {
                        out.print(", fComplex)  ");
                        out.print(HexDump.toHex(n32));
                        out.print(" - Complex prop len");
                        out.println(" {" + propName((short) propertyId) + "}");
                        nComplex += n32;
                    }
                }
                while ((((long) nComplex) & remainingBytes) > 0) {
                    nDumpSize = nComplex > ((int) remainingBytes) ? (short) ((int) remainingBytes) : (short) nComplex;
                    HexDump.dump(in, out, 0, nDumpSize);
                    nComplex -= nDumpSize;
                    recordBytesRemaining -= nDumpSize;
                    remainingBytes -= (long) nDumpSize;
                }
            } else if (recordId == (short) -4078) {
                out.print("    Connector rule: ");
                out.print(LittleEndian.readInt(in));
                out.print("    ShapeID A: ");
                out.print(LittleEndian.readInt(in));
                out.print("   ShapeID B: ");
                out.print(LittleEndian.readInt(in));
                out.print("    ShapeID connector: ");
                out.print(LittleEndian.readInt(in));
                out.print("   Connect pt A: ");
                out.print(LittleEndian.readInt(in));
                out.print("   Connect pt B: ");
                out.println(LittleEndian.readInt(in));
                recordBytesRemaining -= 24;
                remainingBytes -= 24;
            } else if (recordId >= (short) -4072 && recordId < (short) -3817) {
                out.println("    Secondary UID: ");
                HexDump.dump(in, out, 0, 16);
                out.println("    Cache of size: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Boundary top: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Boundary left: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Boundary width: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Boundary height: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    X: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Y: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Cache of saved size: " + HexDump.toHex(LittleEndian.readInt(in)));
                out.println("    Compression Flag: " + HexDump.toHex((byte) in.read()));
                out.println("    Filter: " + HexDump.toHex((byte) in.read()));
                out.println("    Data (after decompression): ");
                recordBytesRemaining -= 50;
                remainingBytes -= 50;
                nDumpSize = recordBytesRemaining > ((int) remainingBytes) ? (short) ((int) remainingBytes) : (short) recordBytesRemaining;
                byte[] buf = new byte[nDumpSize];
                short read = in.read(buf);
                while (read != (short) -1 && read < nDumpSize) {
                    read += in.read(buf, read, buf.length);
                }
                HexDump.dump(new InflaterInputStream(new ByteArrayInputStream(buf)), out, 0, -1);
                recordBytesRemaining -= nDumpSize;
                remainingBytes -= (long) nDumpSize;
            }
            if (((options & 15) == 15) && remainingBytes >= 0) {
                if (recordBytesRemaining <= ((int) remainingBytes)) {
                    out.println("            completed within");
                } else {
                    out.println("            continued elsewhere");
                }
            } else if (remainingBytes >= 0) {
                nDumpSize = recordBytesRemaining > ((int) remainingBytes) ? (short) ((int) remainingBytes) : (short) recordBytesRemaining;
                if (nDumpSize != (short) 0) {
                    HexDump.dump(in, out, 0, nDumpSize);
                    remainingBytes -= (long) nDumpSize;
                }
            } else {
                out.println(" >> OVERRUN <<");
            }
        }
    }

    private String propName(short propertyId) {
        AnonymousClass1PropName[] props = new AnonymousClass1PropName[TIFFConstants.TIFFTAG_ORIENTATION];
        props[0] = new AnonymousClass1PropName(4, "transform.rotation");
        props[1] = new AnonymousClass1PropName(119, "protection.lockrotation");
        props[2] = new AnonymousClass1PropName(120, "protection.lockaspectratio");
        props[3] = new AnonymousClass1PropName(121, "protection.lockposition");
        props[4] = new AnonymousClass1PropName(122, "protection.lockagainstselect");
        props[5] = new AnonymousClass1PropName(123, "protection.lockcropping");
        props[6] = new AnonymousClass1PropName(124, "protection.lockvertices");
        props[7] = new AnonymousClass1PropName(125, "protection.locktext");
        props[8] = new AnonymousClass1PropName(126, "protection.lockadjusthandles");
        props[9] = new AnonymousClass1PropName(127, "protection.lockagainstgrouping");
        props[10] = new AnonymousClass1PropName(128, "text.textid");
        props[11] = new AnonymousClass1PropName(129, "text.textleft");
        props[12] = new AnonymousClass1PropName(130, "text.texttop");
        props[13] = new AnonymousClass1PropName(131, "text.textright");
        props[14] = new AnonymousClass1PropName(132, "text.textbottom");
        props[15] = new AnonymousClass1PropName(133, "text.wraptext");
        props[16] = new AnonymousClass1PropName(134, "text.scaletext");
        props[17] = new AnonymousClass1PropName(135, "text.anchortext");
        props[18] = new AnonymousClass1PropName(136, "text.textflow");
        props[19] = new AnonymousClass1PropName(137, "text.fontrotation");
        props[20] = new AnonymousClass1PropName(138, "text.idofnextshape");
        props[21] = new AnonymousClass1PropName(139, "text.bidir");
        props[22] = new AnonymousClass1PropName(187, "text.singleclickselects");
        props[23] = new AnonymousClass1PropName(188, "text.usehostmargins");
        props[24] = new AnonymousClass1PropName(189, "text.rotatetextwithshape");
        props[25] = new AnonymousClass1PropName(190, "text.sizeshapetofittext");
        props[26] = new AnonymousClass1PropName(191, "text.sizetexttofitshape");
        props[27] = new AnonymousClass1PropName(192, "geotext.unicode");
        props[28] = new AnonymousClass1PropName(HSSFShapeTypes.ActionButtonForwardNext, "geotext.rtftext");
        props[29] = new AnonymousClass1PropName(HSSFShapeTypes.ActionButtonBackPrevious, "geotext.alignmentoncurve");
        props[30] = new AnonymousClass1PropName(HSSFShapeTypes.ActionButtonEnd, "geotext.defaultpointsize");
        props[31] = new AnonymousClass1PropName(HSSFShapeTypes.ActionButtonBeginning, "geotext.textspacing");
        props[32] = new AnonymousClass1PropName(HSSFShapeTypes.ActionButtonReturn, "geotext.fontfamilyname");
        props[33] = new AnonymousClass1PropName(240, "geotext.reverseroworder");
        props[34] = new AnonymousClass1PropName(241, "geotext.hastexteffect");
        props[35] = new AnonymousClass1PropName(242, "geotext.rotatecharacters");
        props[36] = new AnonymousClass1PropName(243, "geotext.kerncharacters");
        props[37] = new AnonymousClass1PropName(AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, "geotext.tightortrack");
        props[38] = new AnonymousClass1PropName(245, "geotext.stretchtofitshape");
        props[39] = new AnonymousClass1PropName(246, "geotext.charboundingbox");
        props[40] = new AnonymousClass1PropName(MetaDo.META_CREATEPALETTE, "geotext.scaletextonpath");
        props[41] = new AnonymousClass1PropName(248, "geotext.stretchcharheight");
        props[42] = new AnonymousClass1PropName(249, "geotext.nomeasurealongpath");
        props[43] = new AnonymousClass1PropName(Callback.DEFAULT_SWIPE_ANIMATION_DURATION, "geotext.boldfont");
        props[44] = new AnonymousClass1PropName(251, "geotext.italicfont");
        props[45] = new AnonymousClass1PropName(252, "geotext.underlinefont");
        props[46] = new AnonymousClass1PropName(253, "geotext.shadowfont");
        props[47] = new AnonymousClass1PropName(254, "geotext.smallcapsfont");
        props[48] = new AnonymousClass1PropName(255, "geotext.strikethroughfont");
        props[49] = new AnonymousClass1PropName(256, "blip.cropfromtop");
        props[50] = new AnonymousClass1PropName(257, "blip.cropfrombottom");
        props[51] = new AnonymousClass1PropName(258, "blip.cropfromleft");
        props[52] = new AnonymousClass1PropName(259, "blip.cropfromright");
        props[53] = new AnonymousClass1PropName(MetaDo.META_SETROP2, "blip.bliptodisplay");
        props[54] = new AnonymousClass1PropName(MetaDo.META_SETRELABS, "blip.blipfilename");
        props[55] = new AnonymousClass1PropName(262, "blip.blipflags");
        props[56] = new AnonymousClass1PropName(263, "blip.transparentcolor");
        props[57] = new AnonymousClass1PropName(264, "blip.contrastsetting");
        props[58] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_CELLLENGTH, "blip.brightnesssetting");
        props[59] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_FILLORDER, "blip.gamma");
        props[60] = new AnonymousClass1PropName(267, "blip.pictureid");
        props[61] = new AnonymousClass1PropName(268, "blip.doublemod");
        props[62] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_DOCUMENTNAME, "blip.picturefillmod");
        props[63] = new AnonymousClass1PropName(270, "blip.pictureline");
        props[64] = new AnonymousClass1PropName(271, "blip.printblip");
        props[65] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_MODEL, "blip.printblipfilename");
        props[66] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_STRIPOFFSETS, "blip.printflags");
        props[67] = new AnonymousClass1PropName(316, "blip.nohittestpicture");
        props[68] = new AnonymousClass1PropName(317, "blip.picturegray");
        props[69] = new AnonymousClass1PropName(318, "blip.picturebilevel");
        props[70] = new AnonymousClass1PropName(319, "blip.pictureactive");
        props[71] = new AnonymousClass1PropName(320, "geometry.left");
        props[72] = new AnonymousClass1PropName(321, "geometry.top");
        props[73] = new AnonymousClass1PropName(322, "geometry.right");
        props[74] = new AnonymousClass1PropName(323, "geometry.bottom");
        props[75] = new AnonymousClass1PropName(324, "geometry.shapepath");
        props[76] = new AnonymousClass1PropName(325, "geometry.vertices");
        props[77] = new AnonymousClass1PropName(326, "geometry.segmentinfo");
        props[78] = new AnonymousClass1PropName(327, "geometry.adjustvalue");
        props[79] = new AnonymousClass1PropName(328, "geometry.adjust2value");
        props[80] = new AnonymousClass1PropName(avutil.AV_PIX_FMT_YUV440P10BE, "geometry.adjust3value");
        props[81] = new AnonymousClass1PropName(330, "geometry.adjust4value");
        props[82] = new AnonymousClass1PropName(avutil.AV_PIX_FMT_YUV440P12BE, "geometry.adjust5value");
        props[83] = new AnonymousClass1PropName(332, "geometry.adjust6value");
        props[84] = new AnonymousClass1PropName(333, "geometry.adjust7value");
        props[85] = new AnonymousClass1PropName(334, "geometry.adjust8value");
        props[86] = new AnonymousClass1PropName(avutil.AV_PIX_FMT_P010LE, "geometry.adjust9value");
        props[87] = new AnonymousClass1PropName(336, "geometry.adjust10value");
        props[88] = new AnonymousClass1PropName(378, "geometry.shadowOK");
        props[89] = new AnonymousClass1PropName(379, "geometry.3dok");
        props[90] = new AnonymousClass1PropName(380, "geometry.lineok");
        props[91] = new AnonymousClass1PropName(381, "geometry.geotextok");
        props[92] = new AnonymousClass1PropName(382, "geometry.fillshadeshapeok");
        props[93] = new AnonymousClass1PropName(383, "geometry.fillok");
        props[94] = new AnonymousClass1PropName(384, "fill.filltype");
        props[95] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_1, "fill.fillcolor");
        props[96] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_2, "fill.fillopacity");
        props[97] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_3, "fill.fillbackcolor");
        props[98] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_4, "fill.backopacity");
        props[99] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_5, "fill.crmod");
        props[100] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_MODE_14, "fill.patterntexture");
        props[101] = new AnonymousClass1PropName(391, "fill.blipfilename");
        props[102] = new AnonymousClass1PropName(392, "fill.blipflags");
        props[103] = new AnonymousClass1PropName(393, "fill.width");
        props[104] = new AnonymousClass1PropName(394, "fill.height");
        props[105] = new AnonymousClass1PropName(395, "fill.angle");
        props[106] = new AnonymousClass1PropName(396, "fill.focus");
        props[107] = new AnonymousClass1PropName(397, "fill.toleft");
        props[108] = new AnonymousClass1PropName(398, "fill.totop");
        props[109] = new AnonymousClass1PropName(AVStream.MAX_STD_TIMEBASES, "fill.toright");
        props[110] = new AnonymousClass1PropName(400, "fill.tobottom");
        props[111] = new AnonymousClass1PropName(401, "fill.rectleft");
        props[112] = new AnonymousClass1PropName(402, "fill.recttop");
        props[113] = new AnonymousClass1PropName(403, "fill.rectright");
        props[114] = new AnonymousClass1PropName(404, "fill.rectbottom");
        props[115] = new AnonymousClass1PropName(405, "fill.dztype");
        props[116] = new AnonymousClass1PropName(406, "fill.shadepreset");
        props[117] = new AnonymousClass1PropName(407, "fill.shadecolors");
        props[118] = new AnonymousClass1PropName(408, "fill.originx");
        props[119] = new AnonymousClass1PropName(409, "fill.originy");
        props[120] = new AnonymousClass1PropName(410, "fill.shapeoriginx");
        props[121] = new AnonymousClass1PropName(411, "fill.shapeoriginy");
        props[122] = new AnonymousClass1PropName(412, "fill.shadetype");
        props[123] = new AnonymousClass1PropName(443, "fill.filled");
        props[124] = new AnonymousClass1PropName(444, "fill.hittestfill");
        props[125] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_BPC, "fill.shape");
        props[126] = new AnonymousClass1PropName(446, "fill.userect");
        props[127] = new AnonymousClass1PropName(447, "fill.nofillhittest");
        props[128] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_WB_KR, "linestyle.color");
        props[129] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_WB_KG, "linestyle.opacity");
        props[130] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_WB_KB, "linestyle.backcolor");
        props[131] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_WIDTH, "linestyle.crmod");
        props[132] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_HEIGHT, "linestyle.linetype");
        props[133] = new AnonymousClass1PropName(453, "linestyle.fillblip");
        props[134] = new AnonymousClass1PropName(454, "linestyle.fillblipname");
        props[135] = new AnonymousClass1PropName(455, "linestyle.fillblipflags");
        props[136] = new AnonymousClass1PropName(456, "linestyle.fillwidth");
        props[137] = new AnonymousClass1PropName(457, "linestyle.fillheight");
        props[138] = new AnonymousClass1PropName(458, "linestyle.filldztype");
        props[139] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_LIMIT_BANDWIDTH, "linestyle.linewidth");
        props[140] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_SENSOR_DATA_BIT_DEPTH, "linestyle.linemiterlimit");
        props[141] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_BIT_DEPTH, "linestyle.linestyle");
        props[142] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_IMAGE_DATA_BIT_DEPTH, "linestyle.linedashing");
        props[143] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_PACKING, "linestyle.linedashstyle");
        props[144] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_PACKING_TYPE, "linestyle.linestartarrowhead");
        props[145] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_IS_COOLED, "linestyle.lineendarrowhead");
        props[146] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_COOLING, "linestyle.linestartarrowwidth");
        props[147] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_TARGET_TEMP, "linestyle.lineestartarrowlength");
        props[148] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_CHIP_TEMP, "linestyle.lineendarrowwidth");
        props[149] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_HOUS_TEMP, "linestyle.lineendarrowlength");
        props[150] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_CMS, "linestyle.linejoinstyle");
        props[151] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_APPLY_CMS, "linestyle.lineendcapstyle");
        props[152] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_EN, "linestyle.arrowheadsok");
        props[153] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_T0, "linestyle.anyline");
        props[154] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_T1, "linestyle.hitlinetest");
        props[155] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_POL, "linestyle.linefillshape");
        props[156] = new AnonymousClass1PropName(511, "linestyle.nolinedrawdash");
        props[157] = new AnonymousClass1PropName(512, "shadowstyle.type");
        props[158] = new AnonymousClass1PropName(513, "shadowstyle.color");
        props[159] = new AnonymousClass1PropName(514, "shadowstyle.highlight");
        props[160] = new AnonymousClass1PropName(515, "shadowstyle.crmod");
        props[161] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH, "shadowstyle.opacity");
        props[162] = new AnonymousClass1PropName(517, "shadowstyle.offsetx");
        props[163] = new AnonymousClass1PropName(518, "shadowstyle.offsety");
        props[164] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_JPEGQTABLES, "shadowstyle.secondoffsetx");
        props[165] = new AnonymousClass1PropName(TIFFConstants.TIFFTAG_JPEGDCTABLES, "shadowstyle.secondoffsety");
        props[166] = new AnonymousClass1PropName(521, "shadowstyle.scalextox");
        props[167] = new AnonymousClass1PropName(522, "shadowstyle.scaleytox");
        props[168] = new AnonymousClass1PropName(MetaDo.META_SETWINDOWORG, "shadowstyle.scalextoy");
        props[169] = new AnonymousClass1PropName(MetaDo.META_SETWINDOWEXT, "shadowstyle.scaleytoy");
        props[170] = new AnonymousClass1PropName(MetaDo.META_SETVIEWPORTORG, "shadowstyle.perspectivex");
        props[171] = new AnonymousClass1PropName(MetaDo.META_SETVIEWPORTEXT, "shadowstyle.perspectivey");
        props[172] = new AnonymousClass1PropName(MetaDo.META_OFFSETWINDOWORG, "shadowstyle.weight");
        props[173] = new AnonymousClass1PropName(528, "shadowstyle.originx");
        props[174] = new AnonymousClass1PropName(529, "shadowstyle.originy");
        props[175] = new AnonymousClass1PropName(574, "shadowstyle.shadow");
        props[176] = new AnonymousClass1PropName(575, "shadowstyle.shadowobsured");
        props[177] = new AnonymousClass1PropName(576, "perspective.type");
        props[178] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_SOURCE_1, "perspective.offsetx");
        props[179] = new AnonymousClass1PropName(578, "perspective.offsety");
        props[180] = new AnonymousClass1PropName(dc1394.DC1394_TRIGGER_SOURCE_3, "perspective.scalextox");
        props[181] = new AnonymousClass1PropName(580, "perspective.scaleytox");
        props[182] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_FREE_FFS_SIZE, "perspective.scalextoy");
        props[183] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_USED_FFS_SIZE, "perspective.scaleytox");
        props[184] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_FFS_ACCESS_KEY, "perspective.perspectivex");
        props[185] = new AnonymousClass1PropName(584, "perspective.perspectivey");
        props[186] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_SENSOR_FEATURE_SELECTOR, "perspective.weight");
        props[187] = new AnonymousClass1PropName(opencv_videoio.CV_CAP_PROP_XI_SENSOR_FEATURE_VALUE, "perspective.originx");
        props[188] = new AnonymousClass1PropName(587, "perspective.originy");
        props[189] = new AnonymousClass1PropName(639, "perspective.perspectiveon");
        props[190] = new AnonymousClass1PropName(640, "3d.specularamount");
        props[191] = new AnonymousClass1PropName(661, "3d.diffuseamount");
        props[192] = new AnonymousClass1PropName(662, "3d.shininess");
        props[HSSFShapeTypes.ActionButtonForwardNext] = new AnonymousClass1PropName(663, "3d.edgethickness");
        props[HSSFShapeTypes.ActionButtonBackPrevious] = new AnonymousClass1PropName(664, "3d.extrudeforward");
        props[HSSFShapeTypes.ActionButtonEnd] = new AnonymousClass1PropName(665, "3d.extrudebackward");
        props[HSSFShapeTypes.ActionButtonBeginning] = new AnonymousClass1PropName(Element.WRITABLE_DIRECT, "3d.extrudeplane");
        props[HSSFShapeTypes.ActionButtonReturn] = new AnonymousClass1PropName(667, "3d.extrusioncolor");
        props[HSSFShapeTypes.ActionButtonDocument] = new AnonymousClass1PropName(648, "3d.crmod");
        props[HSSFShapeTypes.ActionButtonSound] = new AnonymousClass1PropName(700, "3d.3deffect");
        props[200] = new AnonymousClass1PropName(701, "3d.metallic");
        props[201] = new AnonymousClass1PropName(702, "3d.useextrusioncolor");
        props[202] = new AnonymousClass1PropName(703, "3d.lightface");
        props[XMPError.BADXMP] = new AnonymousClass1PropName(704, "3dstyle.yrotationangle");
        props[XMPError.BADSTREAM] = new AnonymousClass1PropName(705, "3dstyle.xrotationangle");
        props[205] = new AnonymousClass1PropName(706, "3dstyle.rotationaxisx");
        props[206] = new AnonymousClass1PropName(707, "3dstyle.rotationaxisy");
        props[207] = new AnonymousClass1PropName(708, "3dstyle.rotationaxisz");
        props[208] = new AnonymousClass1PropName(709, "3dstyle.rotationangle");
        props[209] = new AnonymousClass1PropName(710, "3dstyle.rotationcenterx");
        props[210] = new AnonymousClass1PropName(711, "3dstyle.rotationcentery");
        props[211] = new AnonymousClass1PropName(712, "3dstyle.rotationcenterz");
        props[212] = new AnonymousClass1PropName(713, "3dstyle.rendermode");
        props[213] = new AnonymousClass1PropName(714, "3dstyle.tolerance");
        props[214] = new AnonymousClass1PropName(715, "3dstyle.xviewpoint");
        props[215] = new AnonymousClass1PropName(716, "3dstyle.yviewpoint");
        props[216] = new AnonymousClass1PropName(717, "3dstyle.zviewpoint");
        props[217] = new AnonymousClass1PropName(718, "3dstyle.originx");
        props[218] = new AnonymousClass1PropName(719, "3dstyle.originy");
        props[219] = new AnonymousClass1PropName(720, "3dstyle.skewangle");
        props[220] = new AnonymousClass1PropName(721, "3dstyle.skewamount");
        props[221] = new AnonymousClass1PropName(722, "3dstyle.ambientintensity");
        props[222] = new AnonymousClass1PropName(723, "3dstyle.keyx");
        props[223] = new AnonymousClass1PropName(724, "3dstyle.keyy");
        props[224] = new AnonymousClass1PropName(725, "3dstyle.keyz");
        props[225] = new AnonymousClass1PropName(726, "3dstyle.keyintensity");
        props[Jpeg.M_APP2] = new AnonymousClass1PropName(727, "3dstyle.fillx");
        props[227] = new AnonymousClass1PropName(728, "3dstyle.filly");
        props[228] = new AnonymousClass1PropName(729, "3dstyle.fillz");
        props[229] = new AnonymousClass1PropName(730, "3dstyle.fillintensity");
        props[230] = new AnonymousClass1PropName(MetaDo.META_CREATEFONTINDIRECT, "3dstyle.constrainrotation");
        props[231] = new AnonymousClass1PropName(MetaDo.META_CREATEBRUSHINDIRECT, "3dstyle.rotationcenterauto");
        props[232] = new AnonymousClass1PropName(765, "3dstyle.parallel");
        props[UnknownRecord.BITMAP_00E9] = new AnonymousClass1PropName(766, "3dstyle.keyharsh");
        props[234] = new AnonymousClass1PropName(767, "3dstyle.fillharsh");
        props[235] = new AnonymousClass1PropName(dc1394.DC1394_LOG_WARNING, "shape.master");
        props[236] = new AnonymousClass1PropName(771, "shape.connectorstyle");
        props[Jpeg.M_APPD] = new AnonymousClass1PropName(772, "shape.blackandwhitesettings");
        props[Jpeg.M_APPE] = new AnonymousClass1PropName(773, "shape.wmodepurebw");
        props[UnknownRecord.PHONETICPR_00EF] = new AnonymousClass1PropName(774, "shape.wmodebw");
        props[240] = new AnonymousClass1PropName(826, "shape.oleicon");
        props[241] = new AnonymousClass1PropName(827, "shape.preferrelativeresize");
        props[242] = new AnonymousClass1PropName(828, "shape.lockshapetype");
        props[243] = new AnonymousClass1PropName(830, "shape.deleteattachedobject");
        props[AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE] = new AnonymousClass1PropName(831, "shape.backgroundshape");
        props[245] = new AnonymousClass1PropName(832, "callout.callouttype");
        props[246] = new AnonymousClass1PropName(833, "callout.xycalloutgap");
        props[MetaDo.META_CREATEPALETTE] = new AnonymousClass1PropName(834, "callout.calloutangle");
        props[248] = new AnonymousClass1PropName(835, "callout.calloutdroptype");
        props[249] = new AnonymousClass1PropName(836, "callout.calloutdropspecified");
        props[Callback.DEFAULT_SWIPE_ANIMATION_DURATION] = new AnonymousClass1PropName(837, "callout.calloutlengthspecified");
        props[251] = new AnonymousClass1PropName(889, "callout.iscallout");
        props[252] = new AnonymousClass1PropName(890, "callout.calloutaccentbar");
        props[253] = new AnonymousClass1PropName(891, "callout.callouttextborder");
        props[254] = new AnonymousClass1PropName(892, "callout.calloutminusx");
        props[255] = new AnonymousClass1PropName(893, "callout.calloutminusy");
        props[256] = new AnonymousClass1PropName(894, "callout.dropauto");
        props[257] = new AnonymousClass1PropName(895, "callout.lengthspecified");
        props[258] = new AnonymousClass1PropName(896, "groupshape.shapename");
        props[259] = new AnonymousClass1PropName(897, "groupshape.description");
        props[MetaDo.META_SETROP2] = new AnonymousClass1PropName(898, "groupshape.hyperlink");
        props[MetaDo.META_SETRELABS] = new AnonymousClass1PropName(899, "groupshape.wrappolygonvertices");
        props[262] = new AnonymousClass1PropName(900, "groupshape.wrapdistleft");
        props[263] = new AnonymousClass1PropName(901, "groupshape.wrapdisttop");
        props[264] = new AnonymousClass1PropName(902, "groupshape.wrapdistright");
        props[TIFFConstants.TIFFTAG_CELLLENGTH] = new AnonymousClass1PropName(903, "groupshape.wrapdistbottom");
        props[TIFFConstants.TIFFTAG_FILLORDER] = new AnonymousClass1PropName(904, "groupshape.regroupid");
        props[267] = new AnonymousClass1PropName(953, "groupshape.editedwrap");
        props[268] = new AnonymousClass1PropName(954, "groupshape.behinddocument");
        props[TIFFConstants.TIFFTAG_DOCUMENTNAME] = new AnonymousClass1PropName(955, "groupshape.ondblclicknotify");
        props[270] = new AnonymousClass1PropName(956, "groupshape.isbutton");
        props[271] = new AnonymousClass1PropName(957, "groupshape.1dadjustment");
        props[TIFFConstants.TIFFTAG_MODEL] = new AnonymousClass1PropName(958, "groupshape.hidden");
        props[TIFFConstants.TIFFTAG_STRIPOFFSETS] = new AnonymousClass1PropName(959, "groupshape.print");
        for (int i = 0; i < props.length; i++) {
            if (props[i]._id == propertyId) {
                return props[i]._name;
            }
        }
        return "unknown property";
    }

    private static String getBlipType(byte b) {
        return EscherBSERecord.getBlipType(b);
    }

    private String dec1616(int n32) {
        return (("" + ((short) (n32 >> 16))) + '.') + ((short) (65535 & n32));
    }

    private void outHex(int bytes, InputStream in, PrintStream out) throws IOException, BufferUnderrunException {
        switch (bytes) {
            case 1:
                out.print(HexDump.toHex((byte) in.read()));
                return;
            case 2:
                out.print(HexDump.toHex(LittleEndian.readShort(in)));
                return;
            case 4:
                out.print(HexDump.toHex(LittleEndian.readInt(in)));
                return;
            default:
                throw new IOException("Unable to output variable of that width");
        }
    }

    public static void main(String[] args) {
        main(args, System.out);
    }

    public static void main(String[] args, PrintStream out) {
        byte[] bytes = HexRead.readFromString("0F 00 00 F0 89 07 00 00 00 00 06 F0 18 00 00 00 05 04 00 00 02 00 00 00 05 00 00 00 01 00 00 00 01 00 00 00 05 00 00 00 4F 00 01 F0 2F 07 00 00 42 00 07 F0 B7 01 00 00 03 04 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 FF 00 93 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 8B 01 00 00 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 92 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 59 01 00 00 00 FE 78 9C E3 9B C4 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 D9 C8 58 CC E8 CF B0 80 61 3A 8A 7E 0D C6 23 AC 4F E0 E2 98 B6 12 2B 06 73 9D 12 E3 52 56 59 F6 08 8A CC 52 66 A3 50 FF 96 2B 94 E9 DF 4C A1 FE 2D 3A 03 AB 9F 81 C2 F0 A3 54 BF 0F 85 EE A7 54 FF 40 FB 7F A0 E3 9F D2 F4 4F 71 FE 19 58 FF 2B 31 7F 67 36 3B 25 4F 99 1B 4E 53 A6 5F 89 25 95 E9 C4 00 C7 83 12 F3 1F 26 35 4A D3 D2 47 0E 0A C3 41 8E C9 8A 52 37 DC 15 A1 D0 0D BC 4C 06 0C 2B 28 2C 13 28 D4 EF 43 61 5A A0 58 3F 85 71 E0 4B 69 9E 64 65 FE 39 C0 E5 22 30 1D 30 27 0E 74 3A 18 60 FD 4A CC B1 2C 13 7D 07 36 2D 2A 31 85 B2 6A 0D 74 1D 1D 22 4D 99 FE 60 0A F5 9B EC 1C 58 FD 67 06 56 3F 38 0D 84 3C A5 30 0E 28 D3 AF C4 A4 CA FA 44 7A 0D 65 6E 60 7F 4D A1 1B 24 58 F7 49 AF A5 CC 0D CC DF 19 FE 03 00 F0 B1 25 4D 42 00 07 F0 E1 01 00 00 03 04 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 FF 00 BD 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 B5 01 00 00 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 DA 03 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 83 01 00 00 00 FE 78 9C A5 52 BF 4B 42 51 14 3E F7 DC 77 7A 16 45 48 8B 3C 48 A8 16 15 0D 6C 88 D0 04 C3 40 A3 32 1C 84 96 08 21 04 A1 C5 5C A2 35 82 C0 35 6A AB 1C 6A 6B A8 24 5A 83 68 08 84 84 96 A2 86 A0 7F C2 86 5E E7 5E F5 41 E4 10 BC 03 1F E7 FB F1 CE B9 F7 F1 9E 7C 05 2E 7A 37 9B E0 45 7B 10 EC 6F 96 5F 1D 74 13 55 7E B0 6C 5D 20 60 C0 49 A2 9A BD 99 4F 50 83 1B 30 38 13 0E 33 60 A6 A7 6B B5 37 EB F4 10 FA 14 15 A0 B6 6B 37 0C 1E B3 49 73 5B A5 C2 26 48 3E C1 E0 6C 08 4A 30 C9 93 AA 02 B8 20 13 62 05 4E E1 E8 D7 7C C0 B8 14 95 5E BE B8 A7 CF 1E BE 55 2C 56 B9 78 DF 08 7E 88 4C 27 FF 7B DB FF 7A DD B7 1A 17 67 34 6A AE BA DA 35 D1 E7 72 BE FE EC 6E FE DA E5 7C 3D EC 7A DE 03 FD 50 06 0B 23 F2 0E F3 B2 A5 11 91 0D 4C B5 B5 F3 BF 94 C1 8F 24 F7 D9 6F 60 94 3B C9 9A F3 1C 6B E7 BB F0 2E 49 B2 25 2B C6 B1 EE 69 EE 15 63 4F 71 7D CE 85 CC C8 35 B9 C3 28 28 CE D0 5C 67 79 F2 4A A2 14 23 A4 38 43 73 9D 2D 69 2F C1 08 31 9F C5 5C 9B EB 7B C5 69 19 B3 B4 81 F3 DC E3 B4 8E 8B CC B3 94 53 5A E7 41 2A 63 9A AA 38 C5 3D 48 BB EC 57 59 6F 2B AD 73 1F 1D 60 92 AE 70 8C BB 8F CE 31 C1 3C 49 27 4A EB DC A4 5B 8C D1 0B 0E 73 37 E9 11 A7 99 C7 E8 41 69 B0 7F 00 96 F2 A7 E8 42 00 07 F0 B4 01 00 00 03 04 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 FF 00 90 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 88 01 00 00 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 12 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 56 01 00 00 00 FE 78 9C E3 13 62 00 02 D6 BB EC 17 19 04 99 FE F3 30 FC FF 07 E4 FE 82 62 98 0C 08 C8 31 48 FD 07 03 06 46 06 2E B8 8C 21 98 75 87 FD 02 C3 42 86 6D 0C 2C 40 39 46 38 8B 85 C1 02 AB A8 14 C4 64 B0 EC 34 06 36 10 93 91 01 64 3A 58 54 87 81 83 FD 22 AB 63 51 66 62 0E 03 33 D0 06 16 A0 1C 2F 43 26 83 1E 50 27 08 68 B0 6C 64 2C 66 F4 67 58 C0 30 1D 45 BF 06 E3 11 D6 27 70 71 4C 5B 89 15 83 B9 4E 89 71 29 AB 2C 7B 04 45 66 29 B3 51 A8 7F CB 15 CA F4 6F A6 50 FF 16 9D 81 D5 CF 40 61 F8 51 AA DF 87 42 F7 53 AA 7F A0 FD 3F D0 F1 4F 69 FA A7 38 FF 0C AC FF 95 98 BF 33 9B 9D 92 A7 CC 0D A7 29 D3 AF C4 92 CA 74 62 80 E3 41 89 F9 0F 93 1A A5 69 E9 23 07 85 E1 20 C7 64 45 A9 1B EE 8A 50 E8 06 5E 26 03 86 15 14 96 09 14 EA F7 A1 30 2D 50 AC 9F C2 38 F0 A5 34 4F B2 32 FF 1C E0 72 11 98 0E 98 13 07 38 1D 28 31 C7 B2 4C F4 1D D8 B4 A0 C4 14 CA AA 35 D0 75 64 88 34 65 FA 83 29 D4 6F B2 73 60 F5 9F A1 54 FF 0E CA D3 40 C8 53 0A E3 E0 09 85 6E 50 65 7D 22 BD 86 32 37 B0 BF A6 D0 0D 12 AC FB A4 D7 52 E6 06 E6 EF 0C FF 01 97 1D 12 C7 42 00 07 F0 C3 01 00 00 03 04 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 FF 00 9F 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 97 01 00 00 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 AE 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 65 01 00 00 00 FE 78 9C E3 5B C7 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 99 C6 B8 98 D1 9F 61 01 C3 74 14 FD 1A 8C 2B D8 84 B1 88 4B A5 A5 75 03 01 50 DF 59 46 77 46 0F A8 3C A6 AB 88 15 83 B9 5E 89 B1 8B D5 97 2D 82 22 B3 94 29 D5 BF E5 CA C0 EA DF AC 43 A1 FD 14 EA 67 A0 30 FC 28 D5 EF 43 A1 FB 7D 87 B8 FF 07 3A FE 07 3A FD 53 EA 7E 0A C3 4F 89 F9 0E 73 EA 69 79 CA DC 70 8A 32 FD 4A 2C 5E 4C DF 87 7A 3C BC E0 A5 30 1E 3E 31 C5 33 AC A0 30 2F 52 A8 DF 87 C2 30 A4 54 3F A5 65 19 85 65 A9 12 D3 2B 16 0D 8A CB 13 4A F3 E3 27 E6 09 03 9D 0E 06 58 BF 12 B3 13 CB C1 01 4E 8B 4A 4C 56 AC 91 03 5D 37 86 48 53 A6 3F 98 42 FD 26 3B 07 56 FF 99 1D 14 EA A7 CC 7E 70 1A 08 79 42 61 1C 3C A5 D0 0D 9C 6C C2 32 6B 29 73 03 DB 6B CA DC C0 F8 97 F5 AD CC 1A CA DC C0 F4 83 32 37 B0 A4 30 CE FC C7 48 99 1B FE 33 32 FC 07 00 6C CC 2E 23 33 00 0B F0 12 00 00 00 BF 00 08 00 08 00 81 01 09 00 00 08 C0 01 40 00 00 08 40 00 1E F1 10 00 00 00 0D 00 00 08 0C 00 00 08 17 00 00 08 F7 00 00 10                                              ");
        new EscherDump().dump(bytes, 0, bytes.length, out);
    }

    public void dump(int recordSize, byte[] data, PrintStream out) {
        dump(data, 0, recordSize, out);
    }
}
