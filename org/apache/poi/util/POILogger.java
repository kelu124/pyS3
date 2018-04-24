package org.apache.poi.util;

@Internal
public abstract class POILogger {
    public static final int DEBUG = 1;
    public static final int ERROR = 7;
    public static final int FATAL = 9;
    public static final int INFO = 3;
    protected static final String[] LEVEL_STRINGS = new String[]{"?0?", "DEBUG", "?2?", "INFO", "?4?", "WARN", "?6?", "ERROR", "?8?", "FATAL", "?10+?"};
    protected static final String[] LEVEL_STRINGS_SHORT = new String[]{"?", "D", "?", "I", "?", "W", "?", "E", "?", "F", "?"};
    public static final int WARN = 5;

    public abstract boolean check(int i);

    public abstract void initialize(String str);

    protected abstract void log(int i, Object obj);

    protected abstract void log(int i, Object obj, Throwable th);

    POILogger() {
    }

    public void log(int level, Object... objs) {
        if (check(level)) {
            StringBuilder sb = new StringBuilder(32);
            Throwable lastEx = null;
            int i = 0;
            while (i < objs.length) {
                if (i == objs.length - 1 && (objs[i] instanceof Throwable)) {
                    lastEx = objs[i];
                } else {
                    sb.append(objs[i]);
                }
                i++;
            }
            Object msg = sb.toString().replaceAll("[\r\n]+", " ");
            if (lastEx == null) {
                log(level, msg);
            } else {
                log(level, msg, lastEx);
            }
        }
    }
}
