package org.apache.poi.hpsf;

import org.apache.poi.util.LittleEndian;

class Array$ArrayHeader {
    private Array$ArrayDimension[] _dimensions;
    private int _type;

    Array$ArrayHeader(byte[] data, int startOffset) {
        int offset = startOffset;
        this._type = LittleEndian.getInt(data, offset);
        offset += 4;
        long numDimensionsUnsigned = LittleEndian.getUInt(data, offset);
        offset += 4;
        if (1 > numDimensionsUnsigned || numDimensionsUnsigned > 31) {
            throw new IllegalPropertySetDataException("Array dimension number " + numDimensionsUnsigned + " is not in [1; 31] range");
        }
        int numDimensions = (int) numDimensionsUnsigned;
        this._dimensions = new Array$ArrayDimension[numDimensions];
        for (int i = 0; i < numDimensions; i++) {
            this._dimensions[i] = new Array$ArrayDimension(data, offset);
            offset += 8;
        }
    }

    long getNumberOfScalarValues() {
        long result = 1;
        for (Array$ArrayDimension dimension : this._dimensions) {
            result *= dimension._size;
        }
        return result;
    }

    int getSize() {
        return (this._dimensions.length * 8) + 8;
    }

    int getType() {
        return this._type;
    }
}
