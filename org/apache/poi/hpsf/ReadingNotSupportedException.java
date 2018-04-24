package org.apache.poi.hpsf;

public class ReadingNotSupportedException extends UnsupportedVariantTypeException {
    public ReadingNotSupportedException(long variantType, Object value) {
        super(variantType, value);
    }
}
