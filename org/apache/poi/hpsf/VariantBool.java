package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
class VariantBool {
    static final int SIZE = 2;
    private static final POILogger logger = POILogFactory.getLogger(VariantBool.class);
    private boolean _value;

    VariantBool(byte[] data, int offset) {
        switch (LittleEndian.getShort(data, offset)) {
            case (short) -1:
                this._value = true;
                return;
            case (short) 0:
                this._value = false;
                return;
            default:
                logger.log(5, "VARIANT_BOOL value '" + value + "' is incorrect");
                this._value = true;
                return;
        }
    }

    boolean getValue() {
        return this._value;
    }

    void setValue(boolean value) {
        this._value = value;
    }
}
