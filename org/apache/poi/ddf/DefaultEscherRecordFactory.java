package org.apache.poi.ddf;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.util.LittleEndian;

public class DefaultEscherRecordFactory implements EscherRecordFactory {
    private static Class<?>[] escherRecordClasses = new Class[]{EscherBSERecord.class, EscherOptRecord.class, EscherTertiaryOptRecord.class, EscherClientAnchorRecord.class, EscherDgRecord.class, EscherSpgrRecord.class, EscherSpRecord.class, EscherClientDataRecord.class, EscherDggRecord.class, EscherSplitMenuColorsRecord.class, EscherChildAnchorRecord.class, EscherTextboxRecord.class};
    private static Map<Short, Constructor<? extends EscherRecord>> recordsMap = recordsToMap(escherRecordClasses);

    public EscherRecord createRecord(byte[] data, int offset) {
        short options = LittleEndian.getShort(data, offset);
        short recordId = LittleEndian.getShort(data, offset + 2);
        EscherContainerRecord r;
        if (isContainer(options, recordId)) {
            r = new EscherContainerRecord();
            r.setRecordId(recordId);
            r.setOptions(options);
            return r;
        } else if (recordId < EscherBlipRecord.RECORD_ID_START || recordId > EscherBlipRecord.RECORD_ID_END) {
            Constructor<? extends EscherRecord> recordConstructor = (Constructor) recordsMap.get(Short.valueOf(recordId));
            if (recordConstructor == null) {
                return new UnknownEscherRecord();
            }
            try {
                EscherRecord escherRecord = (EscherRecord) recordConstructor.newInstance(new Object[0]);
                escherRecord.setRecordId(recordId);
                escherRecord.setOptions(options);
                return escherRecord;
            } catch (Exception e) {
                return new UnknownEscherRecord();
            }
        } else {
            if (recordId == EscherBitmapBlip.RECORD_ID_DIB || recordId == EscherBitmapBlip.RECORD_ID_JPEG || recordId == EscherBitmapBlip.RECORD_ID_PNG) {
                r = new EscherBitmapBlip();
            } else if (recordId == (short) -4070 || recordId == (short) -4069 || recordId == (short) -4068) {
                r = new EscherMetafileBlip();
            } else {
                r = new EscherBlipRecord();
            }
            r.setRecordId(recordId);
            r.setOptions(options);
            return r;
        }
    }

    protected static Map<Short, Constructor<? extends EscherRecord>> recordsToMap(Class<?>[] recClasses) {
        Map<Short, Constructor<? extends EscherRecord>> result = new HashMap();
        Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
        Class<?>[] arr$ = recClasses;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Class<? extends EscherRecord> recCls = arr$[i$];
            try {
                short sid = recCls.getField("RECORD_ID").getShort(null);
                try {
                    result.put(Short.valueOf(sid), recCls.getConstructor(EMPTY_CLASS_ARRAY));
                    i$++;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } catch (IllegalArgumentException e2) {
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException(e3);
            } catch (NoSuchFieldException e4) {
                throw new RuntimeException(e4);
            }
        }
        return result;
    }

    public static boolean isContainer(short options, short recordId) {
        if (recordId >= EscherContainerRecord.DGG_CONTAINER && recordId <= EscherContainerRecord.SOLVER_CONTAINER) {
            return true;
        }
        if (recordId == EscherTextboxRecord.RECORD_ID) {
            return false;
        }
        if ((options & 15) != 15) {
            return false;
        }
        return true;
    }
}
