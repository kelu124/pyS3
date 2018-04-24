package org.apache.poi.ss.usermodel;

import java.util.HashMap;
import java.util.Map;

public enum FormulaError {
    _NO_ERROR(-1, "(no error)"),
    NULL(0, "#NULL!"),
    DIV0(7, "#DIV/0!"),
    VALUE(15, "#VALUE!"),
    REF(23, "#REF!"),
    NAME(29, "#NAME?"),
    NUM(36, "#NUM!"),
    NA(42, "#N/A"),
    CIRCULAR_REF(-60, "~CIRCULAR~REF~"),
    FUNCTION_NOT_IMPLEMENTED(-30, "~FUNCTION~NOT~IMPLEMENTED~");
    
    private static final Map<Byte, FormulaError> bmap = null;
    private static final Map<Integer, FormulaError> imap = null;
    private static final Map<String, FormulaError> smap = null;
    private final int longType;
    private final String repr;
    private final byte type;

    static {
        smap = new HashMap();
        bmap = new HashMap();
        imap = new HashMap();
        for (FormulaError error : values()) {
            bmap.put(Byte.valueOf(error.getCode()), error);
            imap.put(Integer.valueOf(error.getLongCode()), error);
            smap.put(error.getString(), error);
        }
    }

    private FormulaError(int type, String repr) {
        this.type = (byte) type;
        this.longType = type;
        this.repr = repr;
    }

    public byte getCode() {
        return this.type;
    }

    public int getLongCode() {
        return this.longType;
    }

    public String getString() {
        return this.repr;
    }

    public static final boolean isValidCode(int errorCode) {
        for (FormulaError error : values()) {
            if (error.getCode() == errorCode || error.getLongCode() == errorCode) {
                return true;
            }
        }
        return false;
    }

    public static FormulaError forInt(byte type) throws IllegalArgumentException {
        FormulaError err = (FormulaError) bmap.get(Byte.valueOf(type));
        if (err != null) {
            return err;
        }
        throw new IllegalArgumentException("Unknown error type: " + type);
    }

    public static FormulaError forInt(int type) throws IllegalArgumentException {
        FormulaError err = (FormulaError) imap.get(Integer.valueOf(type));
        if (err == null) {
            err = (FormulaError) bmap.get(Byte.valueOf((byte) type));
        }
        if (err != null) {
            return err;
        }
        throw new IllegalArgumentException("Unknown error type: " + type);
    }

    public static FormulaError forString(String code) throws IllegalArgumentException {
        FormulaError err = (FormulaError) smap.get(code);
        if (err != null) {
            return err;
        }
        throw new IllegalArgumentException("Unknown error code: " + code);
    }
}
