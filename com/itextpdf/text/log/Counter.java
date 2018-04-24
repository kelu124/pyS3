package com.itextpdf.text.log;

public interface Counter {
    Counter getCounter(Class<?> cls);

    void read(long j);

    void written(long j);
}
