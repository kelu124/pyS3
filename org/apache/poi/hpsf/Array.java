package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;

@Internal
class Array {
    private ArrayHeader _header;
    private TypedPropertyValue[] _values;

    Array() {
    }

    Array(byte[] data, int offset) {
        read(data, offset);
    }

    int read(byte[] data, int startOffset) {
        int offset = startOffset;
        this._header = new ArrayHeader(data, offset);
        offset += this._header.getSize();
        long numberOfScalarsLong = this._header.getNumberOfScalarValues();
        if (numberOfScalarsLong > 2147483647L) {
            throw new UnsupportedOperationException("Sorry, but POI can't store array of properties with size of " + numberOfScalarsLong + " in memory");
        }
        int numberOfScalars = (int) numberOfScalarsLong;
        this._values = new TypedPropertyValue[numberOfScalars];
        int type = ArrayHeader.access$100(this._header);
        int i;
        if (type == 12) {
            for (i = 0; i < numberOfScalars; i++) {
                offset += new TypedPropertyValue().read(data, offset);
            }
        } else {
            for (i = 0; i < numberOfScalars; i++) {
                offset += new TypedPropertyValue(type, null).readValuePadded(data, offset);
            }
        }
        return offset - startOffset;
    }
}
