package org.apache.poi.hpsf;

public class MarkUnsupportedException extends HPSFException {
    public MarkUnsupportedException(String msg) {
        super(msg);
    }

    public MarkUnsupportedException(Throwable reason) {
        super(reason);
    }

    public MarkUnsupportedException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
