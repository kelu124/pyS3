package org.apache.poi;

public abstract class UnsupportedFileFormatException extends IllegalArgumentException {
    private static final long serialVersionUID = -8281969197282030046L;

    public UnsupportedFileFormatException(String s) {
        super(s);
    }
}
