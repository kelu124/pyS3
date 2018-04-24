package org.apache.poi.hpsf;

public class NoFormatIDException extends HPSFRuntimeException {
    public NoFormatIDException(String msg) {
        super(msg);
    }

    public NoFormatIDException(Throwable reason) {
        super(reason);
    }

    public NoFormatIDException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
