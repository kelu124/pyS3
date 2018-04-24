package org.apache.poi.hpsf;

public class IllegalPropertySetDataException extends HPSFRuntimeException {
    public IllegalPropertySetDataException(String msg) {
        super(msg);
    }

    public IllegalPropertySetDataException(Throwable reason) {
        super(reason);
    }

    public IllegalPropertySetDataException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
