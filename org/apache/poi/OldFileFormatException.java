package org.apache.poi;

public abstract class OldFileFormatException extends UnsupportedFileFormatException {
    private static final long serialVersionUID = 7849681804154571175L;

    public OldFileFormatException(String s) {
        super(s);
    }
}
