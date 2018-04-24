package org.apache.poi.ss.formula.udf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

public final class DefaultUDFFinder implements UDFFinder {
    private final Map<String, FreeRefFunction> _functionsByName;

    public DefaultUDFFinder(String[] functionNames, FreeRefFunction[] functionImpls) {
        int nFuncs = functionNames.length;
        if (functionImpls.length != nFuncs) {
            throw new IllegalArgumentException("Mismatch in number of function names and implementations");
        }
        HashMap<String, FreeRefFunction> m = new HashMap((nFuncs * 3) / 2);
        for (int i = 0; i < functionImpls.length; i++) {
            m.put(functionNames[i].toUpperCase(Locale.ROOT), functionImpls[i]);
        }
        this._functionsByName = m;
    }

    public FreeRefFunction findFunction(String name) {
        return (FreeRefFunction) this._functionsByName.get(name.toUpperCase(Locale.ROOT));
    }
}
