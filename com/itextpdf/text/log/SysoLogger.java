package com.itextpdf.text.log;

public class SysoLogger implements Logger {
    private String name;
    private final int shorten;

    public SysoLogger() {
        this(1);
    }

    public SysoLogger(int packageReduce) {
        this.shorten = packageReduce;
    }

    protected SysoLogger(String klass, int shorten) {
        this.shorten = shorten;
        this.name = klass;
    }

    public Logger getLogger(Class<?> klass) {
        return new SysoLogger(klass.getName(), this.shorten);
    }

    public Logger getLogger(String name) {
        return new SysoLogger("[itext]", 0);
    }

    public boolean isLogging(Level level) {
        return true;
    }

    public void warn(String message) {
        System.out.println(String.format("%s WARN  %s", new Object[]{shorten(this.name), message}));
    }

    private String shorten(String className) {
        if (this.shorten == 0) {
            return className;
        }
        StringBuilder target = new StringBuilder();
        String name = className;
        int fromIndex = className.indexOf(46);
        while (fromIndex != -1) {
            target.append(name.substring(0, fromIndex < this.shorten ? fromIndex : this.shorten));
            target.append('.');
            name = name.substring(fromIndex + 1);
            fromIndex = name.indexOf(46);
        }
        target.append(className.substring(className.lastIndexOf(46) + 1));
        return target.toString();
    }

    public void trace(String message) {
        System.out.println(String.format("%s TRACE %s", new Object[]{shorten(this.name), message}));
    }

    public void debug(String message) {
        System.out.println(String.format("%s DEBUG %s", new Object[]{shorten(this.name), message}));
    }

    public void info(String message) {
        System.out.println(String.format("%s INFO  %s", new Object[]{shorten(this.name), message}));
    }

    public void error(String message) {
        System.out.println(String.format("%s ERROR %s", new Object[]{this.name, message}));
    }

    public void error(String message, Exception e) {
        System.out.println(String.format("%s ERROR %s", new Object[]{this.name, message}));
        e.printStackTrace(System.out);
    }
}
