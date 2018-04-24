package org.apache.poi.ddf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class EscherMetafileBlip extends EscherBlipRecord {
    private static final int HEADER_SIZE = 8;
    public static final short RECORD_ID_EMF = (short) -4070;
    public static final short RECORD_ID_PICT = (short) -4068;
    public static final short RECORD_ID_WMF = (short) -4069;
    private static final POILogger log = POILogFactory.getLogger(EscherMetafileBlip.class);
    private final byte[] field_1_UID = new byte[16];
    private final byte[] field_2_UID = new byte[16];
    private int field_2_cb;
    private int field_3_rcBounds_x1;
    private int field_3_rcBounds_x2;
    private int field_3_rcBounds_y1;
    private int field_3_rcBounds_y2;
    private int field_4_ptSize_h;
    private int field_4_ptSize_w;
    private int field_5_cbSave;
    private byte field_6_fCompression;
    private byte field_7_fFilter;
    private byte[] raw_pictureData;
    private byte[] remainingData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        System.arraycopy(data, pos, this.field_1_UID, 0, 16);
        pos += 16;
        if ((getOptions() ^ getSignature()) == 16) {
            System.arraycopy(data, pos, this.field_2_UID, 0, 16);
            pos += 16;
        }
        this.field_2_cb = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_3_rcBounds_x1 = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_3_rcBounds_y1 = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_3_rcBounds_x2 = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_3_rcBounds_y2 = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_4_ptSize_w = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_4_ptSize_h = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_5_cbSave = LittleEndian.getInt(data, pos);
        pos += 4;
        this.field_6_fCompression = data[pos];
        pos++;
        this.field_7_fFilter = data[pos];
        pos++;
        this.raw_pictureData = new byte[this.field_5_cbSave];
        System.arraycopy(data, pos, this.raw_pictureData, 0, this.field_5_cbSave);
        pos += this.field_5_cbSave;
        if (this.field_6_fCompression == (byte) 0) {
            this.field_pictureData = inflatePictureData(this.raw_pictureData);
        } else {
            this.field_pictureData = this.raw_pictureData;
        }
        int remaining = ((bytesAfterHeader - pos) + offset) + 8;
        if (remaining > 0) {
            this.remainingData = new byte[remaining];
            System.arraycopy(data, pos, this.remainingData, 0, remaining);
        }
        return bytesAfterHeader + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        pos += 2;
        LittleEndian.putShort(data, pos, getRecordId());
        pos += 2;
        LittleEndian.putInt(data, pos, getRecordSize() - 8);
        pos += 4;
        System.arraycopy(this.field_1_UID, 0, data, pos, this.field_1_UID.length);
        pos += this.field_1_UID.length;
        if ((getOptions() ^ getSignature()) == 16) {
            System.arraycopy(this.field_2_UID, 0, data, pos, this.field_2_UID.length);
            pos += this.field_2_UID.length;
        }
        LittleEndian.putInt(data, pos, this.field_2_cb);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_rcBounds_x1);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_rcBounds_y1);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_rcBounds_x2);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_rcBounds_y2);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_4_ptSize_w);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_4_ptSize_h);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_5_cbSave);
        pos += 4;
        data[pos] = this.field_6_fCompression;
        pos++;
        data[pos] = this.field_7_fFilter;
        pos++;
        System.arraycopy(this.raw_pictureData, 0, data, pos, this.raw_pictureData.length);
        pos += this.raw_pictureData.length;
        if (this.remainingData != null) {
            System.arraycopy(this.remainingData, 0, data, pos, this.remainingData.length);
            pos += this.remainingData.length;
        }
        listener.afterRecordSerialize(getRecordSize() + offset, getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    private static byte[] inflatePictureData(byte[] data) {
        try {
            InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(data));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            while (true) {
                int readBytes = in.read(buf);
                if (readBytes <= 0) {
                    break;
                }
                out.write(buf, 0, readBytes);
            }
            data = out.toByteArray();
        } catch (IOException e) {
            log.log(5, new Object[]{"Possibly corrupt compression or non-compressed data", e});
        }
        return data;
    }

    public int getRecordSize() {
        int size = this.raw_pictureData.length + 58;
        if (this.remainingData != null) {
            size += this.remainingData.length;
        }
        if ((getOptions() ^ getSignature()) == 16) {
            return size + this.field_2_UID.length;
        }
        return size;
    }

    public byte[] getUID() {
        return this.field_1_UID;
    }

    public void setUID(byte[] uid) {
        if (uid == null || uid.length != 16) {
            throw new IllegalArgumentException("uid must be byte[16]");
        }
        System.arraycopy(uid, 0, this.field_1_UID, 0, this.field_1_UID.length);
    }

    public byte[] getPrimaryUID() {
        return this.field_2_UID;
    }

    public void setPrimaryUID(byte[] primaryUID) {
        if (primaryUID == null || primaryUID.length != 16) {
            throw new IllegalArgumentException("primaryUID must be byte[16]");
        }
        System.arraycopy(primaryUID, 0, this.field_2_UID, 0, this.field_2_UID.length);
    }

    public int getUncompressedSize() {
        return this.field_2_cb;
    }

    public void setUncompressedSize(int uncompressedSize) {
        this.field_2_cb = uncompressedSize;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.field_3_rcBounds_x1, this.field_3_rcBounds_y1, this.field_3_rcBounds_x2 - this.field_3_rcBounds_x1, this.field_3_rcBounds_y2 - this.field_3_rcBounds_y1);
    }

    public void setBounds(Rectangle bounds) {
        this.field_3_rcBounds_x1 = bounds.x;
        this.field_3_rcBounds_y1 = bounds.y;
        this.field_3_rcBounds_x2 = bounds.x + bounds.width;
        this.field_3_rcBounds_y2 = bounds.y + bounds.height;
    }

    public Dimension getSizeEMU() {
        return new Dimension(this.field_4_ptSize_w, this.field_4_ptSize_h);
    }

    public void setSizeEMU(Dimension sizeEMU) {
        this.field_4_ptSize_w = sizeEMU.width;
        this.field_4_ptSize_h = sizeEMU.height;
    }

    public int getCompressedSize() {
        return this.field_5_cbSave;
    }

    public void setCompressedSize(int compressedSize) {
        this.field_5_cbSave = compressedSize;
    }

    public boolean isCompressed() {
        return this.field_6_fCompression == (byte) 0;
    }

    public void setCompressed(boolean compressed) {
        this.field_6_fCompression = compressed ? (byte) 0 : (byte) -2;
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(getRecordId()) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  UID: 0x" + HexDump.toHex(this.field_1_UID) + '\n' + (this.field_2_UID == null ? "" : "  UID2: 0x" + HexDump.toHex(this.field_2_UID) + '\n') + "  Uncompressed Size: " + HexDump.toHex(this.field_2_cb) + '\n' + "  Bounds: " + getBounds() + '\n' + "  Size in EMU: " + getSizeEMU() + '\n' + "  Compressed Size: " + HexDump.toHex(this.field_5_cbSave) + '\n' + "  Compression: " + HexDump.toHex(this.field_6_fCompression) + '\n' + "  Filter: " + HexDump.toHex(this.field_7_fFilter) + '\n' + "  Extra Data:" + '\n' + "" + (this.remainingData == null ? null : "\n Remaining Data: " + HexDump.toHex(this.remainingData, 32));
    }

    public String toXml(String tab) {
        String str;
        String extraData = "";
        StringBuilder builder = new StringBuilder();
        StringBuilder append = builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<UID>0x");
        StringBuilder append2 = new StringBuilder().append(HexDump.toHex(this.field_1_UID)).append('\n');
        if (this.field_2_UID == null) {
            str = "";
        } else {
            str = "  UID2: 0x" + HexDump.toHex(this.field_2_UID) + '\n';
        }
        append.append(append2.append(str).toString()).append("</UID>\n").append(tab).append("\t").append("<UncompressedSize>0x").append(HexDump.toHex(this.field_2_cb)).append("</UncompressedSize>\n").append(tab).append("\t").append("<Bounds>").append(getBounds()).append("</Bounds>\n").append(tab).append("\t").append("<SizeInEMU>").append(getSizeEMU()).append("</SizeInEMU>\n").append(tab).append("\t").append("<CompressedSize>0x").append(HexDump.toHex(this.field_5_cbSave)).append("</CompressedSize>\n").append(tab).append("\t").append("<Compression>0x").append(HexDump.toHex(this.field_6_fCompression)).append("</Compression>\n").append(tab).append("\t").append("<Filter>0x").append(HexDump.toHex(this.field_7_fFilter)).append("</Filter>\n").append(tab).append("\t").append("<ExtraData>").append(extraData).append("</ExtraData>\n").append(tab).append("\t").append("<RemainingData>0x").append(HexDump.toHex(this.remainingData, 32)).append("</RemainingData>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public short getSignature() {
        switch (getRecordId()) {
            case (short) -4070:
                return HSSFPictureData.MSOBI_EMF;
            case (short) -4069:
                return HSSFPictureData.MSOBI_WMF;
            case (short) -4068:
                return HSSFPictureData.MSOBI_PICT;
            default:
                if (!log.check(5)) {
                    return (short) 0;
                }
                log.log(5, new Object[]{"Unknown metafile: " + getRecordId()});
                return (short) 0;
        }
    }

    public void setPictureData(byte[] pictureData) {
        super.setPictureData(pictureData);
        setUncompressedSize(pictureData.length);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(bos);
            dos.write(pictureData);
            dos.close();
            this.raw_pictureData = bos.toByteArray();
            setCompressedSize(this.raw_pictureData.length);
            setCompressed(true);
        } catch (IOException e) {
            throw new RuntimeException("Can't compress metafile picture data", e);
        }
    }

    public void setFilter(byte filter) {
        this.field_7_fFilter = filter;
    }
}
