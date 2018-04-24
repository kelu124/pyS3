package com.itextpdf.text.log;

public class NoOpCounter implements Counter {
    public Counter getCounter(Class<?> cls) {
        return this;
    }

    public void read(long l) {
    }

    public void written(long l) {
    }
}
