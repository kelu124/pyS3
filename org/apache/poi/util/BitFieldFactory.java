package org.apache.poi.util;

import java.util.HashMap;
import java.util.Map;

public class BitFieldFactory {
    private static Map<Integer, BitField> instances = new HashMap();

    public static BitField getInstance(int mask) {
        BitField f = (BitField) instances.get(Integer.valueOf(mask));
        if (f != null) {
            return f;
        }
        f = new BitField(mask);
        instances.put(Integer.valueOf(mask), f);
        return f;
    }
}
