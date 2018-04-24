package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public interface FixedField {
    void readFromBytes(byte[] bArr) throws ArrayIndexOutOfBoundsException;

    void readFromStream(InputStream inputStream) throws IOException;

    String toString();

    void writeToBytes(byte[] bArr) throws ArrayIndexOutOfBoundsException;
}
