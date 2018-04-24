package org.apache.poi.hpsf;

public class NoPropertySetStreamException extends HPSFException {
    public NoPropertySetStreamException(String msg) {
        super(msg);
    }

    public NoPropertySetStreamException(Throwable reason) {
        super(reason);
    }

    public NoPropertySetStreamException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
