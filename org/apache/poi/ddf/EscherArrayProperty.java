package org.apache.poi.ddf;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public final class EscherArrayProperty extends EscherComplexProperty implements Iterable<byte[]> {
    private static final int FIXED_SIZE = 6;
    private boolean emptyComplexPart = false;
    private boolean sizeIncludesHeaderSize = true;

    class C10481 implements Iterator<byte[]> {
        int idx = 0;

        C10481() {
        }

        public boolean hasNext() {
            return this.idx < EscherArrayProperty.this.getNumberOfElementsInArray();
        }

        public byte[] next() {
            if (hasNext()) {
                EscherArrayProperty escherArrayProperty = EscherArrayProperty.this;
                int i = this.idx;
                this.idx = i + 1;
                return escherArrayProperty.getElement(i);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    public EscherArrayProperty(short id, byte[] complexData) {
        boolean z = true;
        super(id, checkComplexData(complexData));
        if (complexData.length != 0) {
            z = false;
        }
        this.emptyComplexPart = z;
    }

    public EscherArrayProperty(short propertyNumber, boolean isBlipId, byte[] complexData) {
        super(propertyNumber, isBlipId, checkComplexData(complexData));
    }

    private static byte[] checkComplexData(byte[] complexData) {
        if (complexData == null || complexData.length == 0) {
            return new byte[6];
        }
        return complexData;
    }

    public int getNumberOfElementsInArray() {
        return this.emptyComplexPart ? 0 : LittleEndian.getUShort(this._complexData, 0);
    }

    public void setNumberOfElementsInArray(int numberOfElements) {
        int expectedArraySize = (getActualSizeOfElements(getSizeOfElements()) * numberOfElements) + 6;
        if (expectedArraySize != this._complexData.length) {
            byte[] newArray = new byte[expectedArraySize];
            System.arraycopy(this._complexData, 0, newArray, 0, this._complexData.length);
            this._complexData = newArray;
        }
        LittleEndian.putShort(this._complexData, 0, (short) numberOfElements);
    }

    public int getNumberOfElementsInMemory() {
        return this.emptyComplexPart ? 0 : LittleEndian.getUShort(this._complexData, 2);
    }

    public void setNumberOfElementsInMemory(int numberOfElements) {
        int expectedArraySize = (getActualSizeOfElements(getSizeOfElements()) * numberOfElements) + 6;
        if (expectedArraySize != this._complexData.length) {
            byte[] newArray = new byte[expectedArraySize];
            System.arraycopy(this._complexData, 0, newArray, 0, expectedArraySize);
            this._complexData = newArray;
        }
        LittleEndian.putShort(this._complexData, 2, (short) numberOfElements);
    }

    public short getSizeOfElements() {
        return this.emptyComplexPart ? (short) 0 : LittleEndian.getShort(this._complexData, 4);
    }

    public void setSizeOfElements(int sizeOfElements) {
        LittleEndian.putShort(this._complexData, 4, (short) sizeOfElements);
        int expectedArraySize = (getNumberOfElementsInArray() * getActualSizeOfElements(getSizeOfElements())) + 6;
        if (expectedArraySize != this._complexData.length) {
            byte[] newArray = new byte[expectedArraySize];
            System.arraycopy(this._complexData, 0, newArray, 0, 6);
            this._complexData = newArray;
        }
    }

    public byte[] getElement(int index) {
        int actualSize = getActualSizeOfElements(getSizeOfElements());
        byte[] result = new byte[actualSize];
        System.arraycopy(this._complexData, (index * actualSize) + 6, result, 0, result.length);
        return result;
    }

    public void setElement(int index, byte[] element) {
        int actualSize = getActualSizeOfElements(getSizeOfElements());
        System.arraycopy(element, 0, this._complexData, (index * actualSize) + 6, actualSize);
    }

    public String toString() {
        StringBuffer results = new StringBuffer();
        results.append("    {EscherArrayProperty:\n");
        results.append("     Num Elements: " + getNumberOfElementsInArray() + '\n');
        results.append("     Num Elements In Memory: " + getNumberOfElementsInMemory() + '\n');
        results.append("     Size of elements: " + getSizeOfElements() + '\n');
        for (int i = 0; i < getNumberOfElementsInArray(); i++) {
            results.append("     Element " + i + ": " + HexDump.toHex(getElement(i)) + '\n');
        }
        results.append("}\n");
        return "propNum: " + getPropertyNumber() + ", propName: " + EscherProperties.getPropertyName(getPropertyNumber()) + ", complex: " + isComplex() + ", blipId: " + isBlipId() + ", data: " + '\n' + results.toString();
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\">\n");
        for (int i = 0; i < getNumberOfElementsInArray(); i++) {
            builder.append("\t").append(tab).append("<Element>").append(HexDump.toHex(getElement(i))).append("</Element>\n");
        }
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public int setArrayData(byte[] data, int offset) {
        if (this.emptyComplexPart) {
            this._complexData = new byte[0];
        } else {
            int arraySize = getActualSizeOfElements(LittleEndian.getShort(data, offset + 4)) * LittleEndian.getShort(data, offset);
            if (arraySize == this._complexData.length) {
                this._complexData = new byte[(arraySize + 6)];
                this.sizeIncludesHeaderSize = false;
            }
            System.arraycopy(data, offset, this._complexData, 0, this._complexData.length);
        }
        return this._complexData.length;
    }

    public int serializeSimplePart(byte[] data, int pos) {
        LittleEndian.putShort(data, pos, getId());
        int recordSize = this._complexData.length;
        if (!this.sizeIncludesHeaderSize) {
            recordSize -= 6;
        }
        LittleEndian.putInt(data, pos + 2, recordSize);
        return 6;
    }

    private static int getActualSizeOfElements(short sizeOfElements) {
        if (sizeOfElements < (short) 0) {
            return (short) ((-sizeOfElements) >> 2);
        }
        return sizeOfElements;
    }

    public Iterator<byte[]> iterator() {
        return new C10481();
    }
}
