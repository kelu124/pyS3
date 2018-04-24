package org.apache.poi.hssf.record;

public interface BiffHeaderInput {
    int available();

    int readDataSize();

    int readRecordSID();
}
