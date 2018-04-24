package org.apache.poi.hpsf;

public class HPSFException extends Exception {
    private Throwable reason;

    public HPSFException(String msg) {
        super(msg);
    }

    public HPSFException(Throwable reason) {
        this.reason = reason;
    }

    public HPSFException(String msg, Throwable reason) {
        super(msg);
        this.reason = reason;
    }

    public Throwable getReason() {
        return this.reason;
    }
}
