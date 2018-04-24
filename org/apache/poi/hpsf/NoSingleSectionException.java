package org.apache.poi.hpsf;

public class NoSingleSectionException extends HPSFRuntimeException {
    public NoSingleSectionException(String msg) {
        super(msg);
    }

    public NoSingleSectionException(Throwable reason) {
        super(reason);
    }

    public NoSingleSectionException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
