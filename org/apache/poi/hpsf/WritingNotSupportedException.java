package org.apache.poi.hpsf;

public class WritingNotSupportedException extends UnsupportedVariantTypeException {
    public WritingNotSupportedException(long variantType, Object value) {
        super(variantType, value);
    }
}
