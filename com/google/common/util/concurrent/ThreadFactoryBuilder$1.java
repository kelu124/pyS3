package com.google.common.util.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class ThreadFactoryBuilder$1 implements ThreadFactory {
    final /* synthetic */ ThreadFactory val$backingThreadFactory;
    final /* synthetic */ AtomicLong val$count;
    final /* synthetic */ Boolean val$daemon;
    final /* synthetic */ String val$nameFormat;
    final /* synthetic */ Integer val$priority;
    final /* synthetic */ UncaughtExceptionHandler val$uncaughtExceptionHandler;

    ThreadFactoryBuilder$1(ThreadFactory threadFactory, String str, AtomicLong atomicLong, Boolean bool, Integer num, UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.val$backingThreadFactory = threadFactory;
        this.val$nameFormat = str;
        this.val$count = atomicLong;
        this.val$daemon = bool;
        this.val$priority = num;
        this.val$uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = this.val$backingThreadFactory.newThread(runnable);
        if (this.val$nameFormat != null) {
            thread.setName(ThreadFactoryBuilder.access$000(this.val$nameFormat, new Object[]{Long.valueOf(this.val$count.getAndIncrement())}));
        }
        if (this.val$daemon != null) {
            thread.setDaemon(this.val$daemon.booleanValue());
        }
        if (this.val$priority != null) {
            thread.setPriority(this.val$priority.intValue());
        }
        if (this.val$uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(this.val$uncaughtExceptionHandler);
        }
        return thread;
    }
}
