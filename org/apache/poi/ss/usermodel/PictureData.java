package org.apache.poi.ss.usermodel;

public interface PictureData {
    byte[] getData();

    String getMimeType();

    int getPictureType();

    String suggestFileExtension();
}
