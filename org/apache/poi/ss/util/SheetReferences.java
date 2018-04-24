package org.apache.poi.ss.util;

import java.util.HashMap;
import java.util.Map;

public class SheetReferences {
    Map<Integer, String> map = new HashMap(5);

    public void addSheetReference(String sheetName, int number) {
        this.map.put(Integer.valueOf(number), sheetName);
    }

    public String getSheetName(int number) {
        return (String) this.map.get(Integer.valueOf(number));
    }
}
