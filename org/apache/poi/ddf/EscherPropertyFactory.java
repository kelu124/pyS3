package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndian;

public final class EscherPropertyFactory {
    public List<EscherProperty> createProperties(byte[] data, int offset, short numProperties) {
        List<EscherProperty> results = new ArrayList();
        int pos = offset;
        for (short i = (short) 0; i < numProperties; i++) {
            short propId = LittleEndian.getShort(data, pos);
            int propData = LittleEndian.getInt(data, pos + 2);
            short propNumber = (short) (propId & 16383);
            boolean isComplex = (propId & -32768) != 0;
            byte propertyType = EscherProperties.getPropertyType(propNumber);
            if (propertyType == (byte) 1) {
                results.add(new EscherBoolProperty(propId, propData));
            } else if (propertyType == (byte) 2) {
                results.add(new EscherRGBProperty(propId, propData));
            } else if (propertyType == (byte) 3) {
                results.add(new EscherShapePathProperty(propId, propData));
            } else if (!isComplex) {
                results.add(new EscherSimpleProperty(propId, propData));
            } else if (propertyType == (byte) 5) {
                results.add(new EscherArrayProperty(propId, new byte[propData]));
            } else {
                results.add(new EscherComplexProperty(propId, new byte[propData]));
            }
            pos += 6;
        }
        for (EscherProperty p : results) {
            if (p instanceof EscherComplexProperty) {
                if (p instanceof EscherArrayProperty) {
                    pos += ((EscherArrayProperty) p).setArrayData(data, pos);
                } else {
                    byte[] complexData = ((EscherComplexProperty) p).getComplexData();
                    int leftover = data.length - pos;
                    if (leftover < complexData.length) {
                        throw new IllegalStateException("Could not read complex escher property, lenght was " + complexData.length + ", but had only " + leftover + " bytes left");
                    }
                    System.arraycopy(data, pos, complexData, 0, complexData.length);
                    pos += complexData.length;
                }
            }
        }
        return results;
    }
}
