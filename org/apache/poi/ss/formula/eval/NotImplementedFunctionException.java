package org.apache.poi.ss.formula.eval;

public final class NotImplementedFunctionException extends NotImplementedException {
    private static final long serialVersionUID = 1208119411557559057L;
    private String functionName;

    public NotImplementedFunctionException(String functionName) {
        super(functionName);
        this.functionName = functionName;
    }

    public NotImplementedFunctionException(String functionName, NotImplementedException cause) {
        super(functionName, cause);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return this.functionName;
    }
}
