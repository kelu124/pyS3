package com.itextpdf.text.pdf;

public interface ExtraEncoding {
    String byteToChar(byte[] bArr, String str);

    byte[] charToByte(char c, String str);

    byte[] charToByte(String str, String str2);
}
