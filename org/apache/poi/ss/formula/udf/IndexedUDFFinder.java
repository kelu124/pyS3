package org.apache.poi.ss.formula.udf;

import java.util.HashMap;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.util.Internal;

@Internal
public class IndexedUDFFinder extends AggregatingUDFFinder {
    private final HashMap<Integer, String> _funcMap = new HashMap();

    public IndexedUDFFinder(UDFFinder... usedToolPacks) {
        super(usedToolPacks);
    }

    public FreeRefFunction findFunction(String name) {
        FreeRefFunction func = super.findFunction(name);
        if (func != null) {
            this._funcMap.put(Integer.valueOf(getFunctionIndex(name)), name);
        }
        return func;
    }

    public String getFunctionName(int idx) {
        return (String) this._funcMap.get(Integer.valueOf(idx));
    }

    public int getFunctionIndex(String name) {
        return name.hashCode();
    }
}
