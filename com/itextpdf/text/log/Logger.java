package com.itextpdf.text.log;

public interface Logger {
    void debug(String str);

    void error(String str);

    void error(String str, Exception exception);

    Logger getLogger(Class<?> cls);

    Logger getLogger(String str);

    void info(String str);

    boolean isLogging(Level level);

    void trace(String str);

    void warn(String str);
}
