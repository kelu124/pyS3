package com.itextpdf.text.log;

public class CounterFactory {
    private static CounterFactory myself = new CounterFactory();
    private Counter counter = new NoOpCounter();

    private CounterFactory() {
    }

    public static CounterFactory getInstance() {
        return myself;
    }

    public static Counter getCounter(Class<?> klass) {
        return myself.counter.getCounter(klass);
    }

    public Counter getCounter() {
        return this.counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }
}
