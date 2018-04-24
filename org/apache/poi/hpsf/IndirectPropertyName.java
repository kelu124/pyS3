package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;

@Internal
class IndirectPropertyName {
    private CodePageString _value;

    IndirectPropertyName(byte[] data, int offset) {
        this._value = new CodePageString(data, offset);
    }

    int getSize() {
        return this._value.getSize();
    }
}
