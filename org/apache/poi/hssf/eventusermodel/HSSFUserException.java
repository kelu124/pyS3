package org.apache.poi.hssf.eventusermodel;

public class HSSFUserException extends Exception {
    private Throwable reason;

    public HSSFUserException(String msg) {
        super(msg);
    }

    public HSSFUserException(Throwable reason) {
        this.reason = reason;
    }

    public HSSFUserException(String msg, Throwable reason) {
        super(msg);
        this.reason = reason;
    }

    public Throwable getReason() {
        return this.reason;
    }
}
