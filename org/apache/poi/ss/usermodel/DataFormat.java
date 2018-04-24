package org.apache.poi.ss.usermodel;

public interface DataFormat {
    String getFormat(short s);

    short getFormat(String str);
}
