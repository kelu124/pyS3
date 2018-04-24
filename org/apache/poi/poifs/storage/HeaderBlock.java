package org.apache.poi.poifs.storage;

import com.itextpdf.text.html.HtmlTags;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.IntegerField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LongField;
import org.apache.poi.util.ShortField;

public final class HeaderBlock implements HeaderBlockConstants {
    private static final byte[] MAGIC_BIFF2 = new byte[]{(byte) 9, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 112, (byte) 0};
    private static final byte[] MAGIC_BIFF3 = new byte[]{(byte) 9, (byte) 2, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 112, (byte) 0};
    private static final byte[] MAGIC_BIFF4a = new byte[]{(byte) 9, (byte) 4, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 112, (byte) 0};
    private static final byte[] MAGIC_BIFF4b = new byte[]{(byte) 9, (byte) 4, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1};
    private static final byte _default_value = (byte) -1;
    private int _bat_count;
    private final byte[] _data;
    private int _property_start;
    private int _sbat_count;
    private int _sbat_start;
    private int _xbat_count;
    private int _xbat_start;
    private final POIFSBigBlockSize bigBlockSize;

    public HeaderBlock(InputStream stream) throws IOException {
        this(readFirst512(stream));
        if (this.bigBlockSize.getBigBlockSize() != 512) {
            IOUtils.readFully(stream, new byte[(this.bigBlockSize.getBigBlockSize() - 512)]);
        }
    }

    public HeaderBlock(ByteBuffer buffer) throws IOException {
        this(IOUtils.toByteArray(buffer, 512));
    }

    private HeaderBlock(byte[] data) throws IOException {
        this._data = (byte[]) data.clone();
        long signature = LittleEndian.getLong(this._data, 0);
        if (signature == HeaderBlockConstants._signature) {
            if (this._data[30] == (byte) 12) {
                this.bigBlockSize = POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
            } else if (this._data[30] == (byte) 9) {
                this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
            } else {
                throw new IOException("Unsupported blocksize  (2^" + this._data[30] + "). Expected 2^9 or 2^12.");
            }
            this._bat_count = new IntegerField(44, data).get();
            this._property_start = new IntegerField(48, this._data).get();
            this._sbat_start = new IntegerField(60, this._data).get();
            this._sbat_count = new IntegerField(64, this._data).get();
            this._xbat_start = new IntegerField(68, this._data).get();
            this._xbat_count = new IntegerField(72, this._data).get();
        } else if (cmp(POIFSConstants.OOXML_FILE_HEADER, data)) {
            throw new OfficeXmlFileException("The supplied data appears to be in the Office 2007+ XML. You are calling the part of POI that deals with OLE2 Office Documents. You need to call a different part of POI to process this data (eg XSSF instead of HSSF)");
        } else if (cmp(POIFSConstants.RAW_XML_FILE_HEADER, data)) {
            throw new NotOLE2FileException("The supplied data appears to be a raw XML file. Formats such as Office 2003 XML are not supported");
        } else if (cmp(MAGIC_BIFF2, data)) {
            throw new OldExcelFormatException("The supplied data appears to be in BIFF2 format. HSSF only supports the BIFF8 format, try OldExcelExtractor");
        } else if (cmp(MAGIC_BIFF3, data)) {
            throw new OldExcelFormatException("The supplied data appears to be in BIFF3 format. HSSF only supports the BIFF8 format, try OldExcelExtractor");
        } else if (cmp(MAGIC_BIFF4a, data) || cmp(MAGIC_BIFF4b, data)) {
            throw new OldExcelFormatException("The supplied data appears to be in BIFF4 format. HSSF only supports the BIFF8 format, try OldExcelExtractor");
        } else {
            throw new NotOLE2FileException("Invalid header signature; read " + HexDump.longToHex(signature) + ", expected " + HexDump.longToHex(HeaderBlockConstants._signature) + " - Your file appears " + "not to be a valid OLE2 document");
        }
    }

    public HeaderBlock(POIFSBigBlockSize bigBlockSize) {
        this.bigBlockSize = bigBlockSize;
        this._data = new byte[512];
        Arrays.fill(this._data, (byte) -1);
        LongField longField = new LongField(0, HeaderBlockConstants._signature, this._data);
        IntegerField integerField = new IntegerField(8, 0, this._data);
        integerField = new IntegerField(12, 0, this._data);
        integerField = new IntegerField(16, 0, this._data);
        integerField = new IntegerField(20, 0, this._data);
        ShortField shortField = new ShortField(24, (short) 59, this._data);
        shortField = new ShortField(26, (short) 3, this._data);
        shortField = new ShortField(28, (short) -2, this._data);
        shortField = new ShortField(30, bigBlockSize.getHeaderValue(), this._data);
        integerField = new IntegerField(32, 6, this._data);
        integerField = new IntegerField(36, 0, this._data);
        integerField = new IntegerField(40, 0, this._data);
        integerField = new IntegerField(52, 0, this._data);
        integerField = new IntegerField(56, 4096, this._data);
        this._bat_count = 0;
        this._sbat_count = 0;
        this._xbat_count = 0;
        this._property_start = -2;
        this._sbat_start = -2;
        this._xbat_start = -2;
    }

    private static byte[] readFirst512(InputStream stream) throws IOException {
        byte[] data = new byte[512];
        int bsCount = IOUtils.readFully(stream, data);
        if (bsCount == 512) {
            return data;
        }
        throw alertShortRead(bsCount, 512);
    }

    private static IOException alertShortRead(int pRead, int expectedReadSize) {
        int read;
        if (pRead < 0) {
            read = 0;
        } else {
            read = pRead;
        }
        return new IOException("Unable to read entire header; " + read + (" byte" + (read == 1 ? "" : HtmlTags.f36S)) + " read; expected " + expectedReadSize + " bytes");
    }

    public int getPropertyStart() {
        return this._property_start;
    }

    public void setPropertyStart(int startBlock) {
        this._property_start = startBlock;
    }

    public int getSBATStart() {
        return this._sbat_start;
    }

    public int getSBATCount() {
        return this._sbat_count;
    }

    public void setSBATStart(int startBlock) {
        this._sbat_start = startBlock;
    }

    public void setSBATBlockCount(int count) {
        this._sbat_count = count;
    }

    public int getBATCount() {
        return this._bat_count;
    }

    public void setBATCount(int count) {
        this._bat_count = count;
    }

    public int[] getBATArray() {
        int[] result = new int[Math.min(this._bat_count, 109)];
        int offset = 76;
        for (int j = 0; j < result.length; j++) {
            result[j] = LittleEndian.getInt(this._data, offset);
            offset += 4;
        }
        return result;
    }

    public void setBATArray(int[] bat_array) {
        int i;
        int count = Math.min(bat_array.length, 109);
        int blank = 109 - count;
        int offset = 76;
        for (i = 0; i < count; i++) {
            LittleEndian.putInt(this._data, offset, bat_array[i]);
            offset += 4;
        }
        for (i = 0; i < blank; i++) {
            LittleEndian.putInt(this._data, offset, -1);
            offset += 4;
        }
    }

    public int getXBATCount() {
        return this._xbat_count;
    }

    public void setXBATCount(int count) {
        this._xbat_count = count;
    }

    public int getXBATIndex() {
        return this._xbat_start;
    }

    public void setXBATStart(int startBlock) {
        this._xbat_start = startBlock;
    }

    public POIFSBigBlockSize getBigBlockSize() {
        return this.bigBlockSize;
    }

    void writeData(OutputStream stream) throws IOException {
        IntegerField integerField = new IntegerField(44, this._bat_count, this._data);
        integerField = new IntegerField(48, this._property_start, this._data);
        integerField = new IntegerField(60, this._sbat_start, this._data);
        integerField = new IntegerField(64, this._sbat_count, this._data);
        integerField = new IntegerField(68, this._xbat_start, this._data);
        integerField = new IntegerField(72, this._xbat_count, this._data);
        stream.write(this._data, 0, 512);
        for (int i = 512; i < this.bigBlockSize.getBigBlockSize(); i++) {
            stream.write(0);
        }
    }

    private static boolean cmp(byte[] magic, byte[] data) {
        int i;
        byte[] arr$ = magic;
        int len$ = arr$.length;
        int i$ = 0;
        int i2 = 0;
        while (i$ < len$) {
            byte m = arr$[i$];
            i = i2 + 1;
            byte d = data[i2];
            if (d != m && (m != (byte) 112 || (d != (byte) 16 && d != (byte) 32 && d != (byte) 64))) {
                return false;
            }
            i$++;
            i2 = i;
        }
        i = i2;
        return true;
    }
}
