package org.apache.poi.sl.usermodel;

import com.lee.ultrascan.interactor.FrameDataInteractor;
import java.awt.Dimension;
import java.io.IOException;

public interface PictureData {

    public enum PictureType {
        EMF(2, 2, "image/x-emf", ".emf"),
        WMF(3, 3, "image/x-wmf", ".wmf"),
        PICT(4, 4, "image/pict", ".pict"),
        JPEG(5, 5, "image/jpeg", FrameDataInteractor.frameImageExtension),
        PNG(6, 6, "image/png", ".png"),
        DIB(7, 7, "image/dib", ".dib"),
        GIF(-1, 8, "image/gif", ".gif"),
        TIFF(-1, 9, "image/tiff", ".tif"),
        EPS(-1, 10, "image/x-eps", ".eps"),
        BMP(-1, 11, "image/x-ms-bmp", ".bmp"),
        WPG(-1, 12, "image/x-wpg", ".wpg"),
        WDP(-1, 13, "image/vnd.ms-photo", ".wdp");
        
        public final String contentType;
        public final String extension;
        public final int nativeId;
        public final int ooxmlId;

        private PictureType(int nativeId, int ooxmlId, String contentType, String extension) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
            this.contentType = contentType;
            this.extension = extension;
        }

        public static PictureType forNativeID(int nativeId) {
            for (PictureType ans : values()) {
                if (ans.nativeId == nativeId) {
                    return ans;
                }
            }
            return null;
        }

        public static PictureType forOoxmlID(int ooxmlId) {
            for (PictureType ans : values()) {
                if (ans.ooxmlId == ooxmlId) {
                    return ans;
                }
            }
            return null;
        }
    }

    byte[] getChecksum();

    String getContentType();

    byte[] getData();

    Dimension getImageDimension();

    Dimension getImageDimensionInPixels();

    PictureType getType();

    void setData(byte[] bArr) throws IOException;
}
