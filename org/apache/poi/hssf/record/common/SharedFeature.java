package org.apache.poi.hssf.record.common;

import org.apache.poi.util.LittleEndianOutput;

public interface SharedFeature {
    int getDataSize();

    void serialize(LittleEndianOutput littleEndianOutput);

    String toString();
}
