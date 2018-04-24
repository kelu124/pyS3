package com.itextpdf.text.log;

public class LoggerFactory {
    private static LoggerFactory myself = new LoggerFactory();
    private Logger logger = new NoOpLogger();

    public static Logger getLogger(Class<?> klass) {
        return myself.logger.getLogger((Class) klass);
    }

    public static Logger getLogger(String name) {
        return myself.logger.getLogger(name);
    }

    public static LoggerFactory getInstance() {
        return myself;
    }

    private LoggerFactory() {
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger logger() {
        return this.logger;
    }
}
