package org.apache.poi.hssf.usermodel;

import com.lee.ultrascan.library.ProbeParams;

class HSSFCellStyle$1 extends ThreadLocal<Short> {
    HSSFCellStyle$1() {
    }

    protected Short initialValue() {
        return Short.valueOf(ProbeParams.BBP_CTL_LOG_INCREMENT);
    }
}
