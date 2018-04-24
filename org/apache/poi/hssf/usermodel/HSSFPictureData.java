package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherBlipRecord;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.util.PngUtils;

public class HSSFPictureData implements PictureData {
    public static final short FORMAT_MASK = (short) -16;
    public static final short MSOBI_DIB = (short) 31360;
    public static final short MSOBI_EMF = (short) 15680;
    public static final short MSOBI_JPEG = (short) 18080;
    public static final short MSOBI_PICT = (short) 21536;
    public static final short MSOBI_PNG = (short) 28160;
    public static final short MSOBI_WMF = (short) 8544;
    private EscherBlipRecord blip;

    public HSSFPictureData(EscherBlipRecord blip) {
        this.blip = blip;
    }

    public byte[] getData() {
        byte[] pictureData = this.blip.getPicturedata();
        if (!PngUtils.matchesPngHeader(pictureData, 16)) {
            return pictureData;
        }
        byte[] png = new byte[(pictureData.length - 16)];
        System.arraycopy(pictureData, 16, png, 0, png.length);
        return png;
    }

    public int getFormat() {
        return this.blip.getRecordId() + 4072;
    }

    public String suggestFileExtension() {
        switch (this.blip.getRecordId()) {
            case (short) -4070:
                return "emf";
            case (short) -4069:
                return "wmf";
            case (short) -4068:
                return "pict";
            case (short) -4067:
                return "jpeg";
            case (short) -4066:
                return "png";
            case (short) -4065:
                return "dib";
            default:
                return "";
        }
    }

    public String getMimeType() {
        switch (this.blip.getRecordId()) {
            case (short) -4070:
                return "image/x-emf";
            case (short) -4069:
                return "image/x-wmf";
            case (short) -4068:
                return "image/x-pict";
            case (short) -4067:
                return "image/jpeg";
            case (short) -4066:
                return "image/png";
            case (short) -4065:
                return "image/bmp";
            default:
                return "image/unknown";
        }
    }

    public int getPictureType() {
        switch (this.blip.getRecordId()) {
            case (short) -4070:
                return 2;
            case (short) -4069:
                return 3;
            case (short) -4068:
                return 4;
            case (short) -4067:
                return 5;
            case (short) -4066:
                return 6;
            case (short) -4065:
                return 7;
            default:
                return -1;
        }
    }
}
