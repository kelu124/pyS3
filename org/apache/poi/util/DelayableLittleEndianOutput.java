package org.apache.poi.util;

public interface DelayableLittleEndianOutput extends LittleEndianOutput {
    LittleEndianOutput createDelayedOutput(int i);
}
