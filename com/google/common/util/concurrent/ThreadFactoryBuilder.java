package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.CheckReturnValue;

@GwtIncompatible
@CanIgnoreReturnValue
public final class ThreadFactoryBuilder {
    private ThreadFactory backingThreadFactory = null;
    private Boolean daemon = null;
    private String nameFormat = null;
    private Integer priority = null;
    private UncaughtExceptionHandler uncaughtExceptionHandler = null;

    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        String unused = format(nameFormat, Integer.valueOf(0));
        this.nameFormat = nameFormat;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = Boolean.valueOf(daemon);
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        boolean z = true;
        Preconditions.checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", priority, 1);
        if (priority > 10) {
            z = false;
        }
        Preconditions.checkArgument(z, "Thread priority (%s) must be <= %s", priority, 10);
        this.priority = Integer.valueOf(priority);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = (UncaughtExceptionHandler) Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = (ThreadFactory) Preconditions.checkNotNull(backingThreadFactory);
        return this;
    }

    @CheckReturnValue
    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        String nameFormat = builder.nameFormat;
        return new 1(builder.backingThreadFactory != null ? builder.backingThreadFactory : Executors.defaultThreadFactory(), nameFormat, nameFormat != null ? new AtomicLong(0) : null, builder.daemon, builder.priority, builder.uncaughtExceptionHandler);
    }

    private static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
