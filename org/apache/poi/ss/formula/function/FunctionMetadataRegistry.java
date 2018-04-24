package org.apache.poi.ss.formula.function;

import java.util.Map;
import java.util.Set;

public final class FunctionMetadataRegistry {
    public static final int FUNCTION_INDEX_CHOOSE = 100;
    public static final short FUNCTION_INDEX_EXTERNAL = (short) 255;
    public static final int FUNCTION_INDEX_IF = 1;
    public static final short FUNCTION_INDEX_INDIRECT = (short) 148;
    public static final short FUNCTION_INDEX_SUM = (short) 4;
    public static final String FUNCTION_NAME_IF = "IF";
    private static FunctionMetadataRegistry _instance;
    private final FunctionMetadata[] _functionDataByIndex;
    private final Map<String, FunctionMetadata> _functionDataByName;

    private static FunctionMetadataRegistry getInstance() {
        if (_instance == null) {
            _instance = FunctionMetadataReader.createRegistry();
        }
        return _instance;
    }

    FunctionMetadataRegistry(FunctionMetadata[] functionDataByIndex, Map<String, FunctionMetadata> functionDataByName) {
        this._functionDataByIndex = functionDataByIndex;
        this._functionDataByName = functionDataByName;
    }

    Set<String> getAllFunctionNames() {
        return this._functionDataByName.keySet();
    }

    public static FunctionMetadata getFunctionByIndex(int index) {
        return getInstance().getFunctionByIndexInternal(index);
    }

    private FunctionMetadata getFunctionByIndexInternal(int index) {
        return this._functionDataByIndex[index];
    }

    public static short lookupIndexByName(String name) {
        FunctionMetadata fd = getInstance().getFunctionByNameInternal(name);
        if (fd == null) {
            return (short) -1;
        }
        return (short) fd.getIndex();
    }

    private FunctionMetadata getFunctionByNameInternal(String name) {
        return (FunctionMetadata) this._functionDataByName.get(name);
    }

    public static FunctionMetadata getFunctionByName(String name) {
        return getInstance().getFunctionByNameInternal(name);
    }
}
