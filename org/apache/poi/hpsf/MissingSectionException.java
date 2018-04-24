package org.apache.poi.hpsf;

public class MissingSectionException extends HPSFRuntimeException {
    public MissingSectionException(String msg) {
        super(msg);
    }

    public MissingSectionException(Throwable reason) {
        super(reason);
    }

    public MissingSectionException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
