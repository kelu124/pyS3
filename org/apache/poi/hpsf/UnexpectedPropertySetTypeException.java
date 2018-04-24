package org.apache.poi.hpsf;

public class UnexpectedPropertySetTypeException extends HPSFException {
    public UnexpectedPropertySetTypeException(String msg) {
        super(msg);
    }

    public UnexpectedPropertySetTypeException(Throwable reason) {
        super(reason);
    }

    public UnexpectedPropertySetTypeException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
