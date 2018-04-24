package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.itextpdf.text.pdf.PdfBoolean;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import sun.misc.Unsafe;

@GwtCompatible(emulated = true)
public abstract class AbstractFuture<V> implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", PdfBoolean.FALSE));
    private static final Object NULL = new Object();
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
    private volatile Listener listeners;
    private volatile Object value;
    private volatile Waiter waiters;

    private static abstract class AtomicHelper {
        abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        abstract void putNext(Waiter waiter, Waiter waiter2);

        abstract void putThread(Waiter waiter, Thread thread);

        private AtomicHelper() {
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") {
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception) {
            this.exception = (Throwable) Preconditions.checkNotNull(exception);
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            super();
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }

        void putThread(Waiter waiter, Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }

        void putNext(Waiter waiter, Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }

        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return this.waitersUpdater.compareAndSet(future, expect, update);
        }

        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return this.listenersUpdater.compareAndSet(future, expect, update);
        }

        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return this.valueUpdater.compareAndSet(future, expect, update);
        }
    }

    private static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }

        public void run() {
            if (this.owner.value == this) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }

        void putNext(Waiter waiter, Waiter newValue) {
            waiter.next = newValue;
        }

        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            boolean z;
            synchronized (future) {
                if (future.waiters == expect) {
                    future.waiters = update;
                    z = true;
                } else {
                    z = false;
                }
            }
            return z;
        }

        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            boolean z;
            synchronized (future) {
                if (future.listeners == expect) {
                    future.listeners = update;
                    z = true;
                } else {
                    z = false;
                }
            }
            return z;
        }

        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            boolean z;
            synchronized (future) {
                if (future.value == expect) {
                    future.value = update;
                    z = true;
                } else {
                    z = false;
                }
            }
            return z;
        }
    }

    static abstract class TrustedFuture<V> extends AbstractFuture<V> {
        TrustedFuture() {
        }

        @CanIgnoreReturnValue
        public final V get() throws InterruptedException, ExecutionException {
            return super.get();
        }

        @CanIgnoreReturnValue
        public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(timeout, unit);
        }

        public final boolean isDone() {
            return super.isDone();
        }

        public final boolean isCancelled() {
            return super.isCancelled();
        }

        public final void addListener(Runnable listener, Executor executor) {
            super.addListener(listener, executor);
        }

        @CanIgnoreReturnValue
        public final boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }

    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        static class C08011 implements PrivilegedExceptionAction<Unsafe> {
            C08011() {
            }

            public Unsafe run() throws Exception {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object x = f.get(null);
                    if (k.isInstance(x)) {
                        return (Unsafe) k.cast(x);
                    }
                }
                throw new NoSuchFieldError("the Unsafe");
            }
        }

        private UnsafeAtomicHelper() {
            super();
        }

        static {
            Unsafe unsafe;
            try {
                unsafe = Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    unsafe = (Unsafe) AccessController.doPrivileged(new C08011());
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
            try {
                Class<?> abstractFuture = AbstractFuture.class;
                WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (Exception e3) {
                Throwables.throwIfUnchecked(e3);
                throw new RuntimeException(e3);
            }
        }

        void putThread(Waiter waiter, Thread newValue) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
        }

        void putNext(Waiter waiter, Waiter newValue) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
        }

        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
        }

        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
        }

        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
        }
    }

    static {
        AtomicHelper helper;
        try {
            helper = new UnsafeAtomicHelper();
        } catch (Throwable atomicReferenceFieldUpdaterFailure) {
            log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", unsafeFailure);
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", atomicReferenceFieldUpdaterFailure);
            helper = new SynchronizedHelper();
        }
        ATOMIC_HELPER = helper;
        Class cls = LockSupport.class;
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr != Waiter.TOMBSTONE) {
                while (curr != null) {
                    Waiter succ = curr.next;
                    if (curr.thread != null) {
                        pred = curr;
                    } else if (pred != null) {
                        pred.next = succ;
                        if (pred.thread == null) {
                        }
                    } else if (ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                    }
                    curr = succ;
                }
                return;
            }
            return;
        }
    }

    protected AbstractFuture() {
    }

    @CanIgnoreReturnValue
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long remainingNanos = unit.toNanos(timeout);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
            return getDoneValue(localValue);
        }
        long endNanos = remainingNanos > 0 ? System.nanoTime() + remainingNanos : 0;
        if (remainingNanos >= SPIN_THRESHOLD_NANOS) {
            Waiter oldHead = this.waiters;
            if (oldHead != Waiter.TOMBSTONE) {
                Waiter node = new Waiter();
                do {
                    node.setNext(oldHead);
                    if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                        do {
                            LockSupport.parkNanos(this, remainingNanos);
                            if (Thread.interrupted()) {
                                removeWaiter(node);
                                throw new InterruptedException();
                            }
                            localValue = this.value;
                            if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
                                return getDoneValue(localValue);
                            }
                            remainingNanos = endNanos - System.nanoTime();
                        } while (remainingNanos >= SPIN_THRESHOLD_NANOS);
                        removeWaiter(node);
                    } else {
                        oldHead = this.waiters;
                    }
                } while (oldHead != Waiter.TOMBSTONE);
            }
            return getDoneValue(this.value);
        }
        while (remainingNanos > 0) {
            localValue = this.value;
            if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
                return getDoneValue(localValue);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            remainingNanos = endNanos - System.nanoTime();
        }
        throw new TimeoutException();
    }

    @CanIgnoreReturnValue
    public V get() throws InterruptedException, ExecutionException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
            return getDoneValue(localValue);
        }
        Waiter oldHead = this.waiters;
        if (oldHead != Waiter.TOMBSTONE) {
            Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    int i;
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            removeWaiter(node);
                            throw new InterruptedException();
                        }
                        localValue = this.value;
                        if (localValue != null) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                    } while ((i & (!(localValue instanceof SetFuture) ? 1 : 0)) == 0);
                    return getDoneValue(localValue);
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
        }
        return getDoneValue(this.value);
    }

    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        } else if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        } else if (obj == NULL) {
            return null;
        } else {
            return obj;
        }
    }

    public boolean isDone() {
        int i = 1;
        Object localValue = this.value;
        int i2 = localValue != null ? 1 : 0;
        if (localValue instanceof SetFuture) {
            i = 0;
        }
        return i2 & i;
    }

    public boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @CanIgnoreReturnValue
    public boolean cancel(boolean mayInterruptIfRunning) {
        int i;
        Object localValue = this.value;
        boolean rValue = false;
        if (localValue == null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i | (localValue instanceof SetFuture)) != 0) {
            ListenableFuture<?> futureToPropagateTo;
            Cancellation valueToSet = new Cancellation(mayInterruptIfRunning, GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null);
            AbstractFuture<?> abstractFuture = this;
            while (true) {
                if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    futureToPropagateTo = ((SetFuture) localValue).future;
                    if (!(futureToPropagateTo instanceof TrustedFuture)) {
                        break;
                    }
                    AbstractFuture<?> trusted = (AbstractFuture) futureToPropagateTo;
                    localValue = trusted.value;
                    if (localValue == null) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    if ((i | (localValue instanceof SetFuture)) == 0) {
                        break;
                    }
                    abstractFuture = trusted;
                } else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                }
            }
            futureToPropagateTo.cancel(mayInterruptIfRunning);
        }
        return rValue;
    }

    protected void interruptTask() {
    }

    protected final boolean wasInterrupted() {
        Object localValue = this.value;
        return (localValue instanceof Cancellation) && ((Cancellation) localValue).wasInterrupted;
    }

    public void addListener(Runnable listener, Executor executor) {
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        Listener oldHead = this.listeners;
        if (oldHead != Listener.TOMBSTONE) {
            Listener newNode = new Listener(listener, executor);
            do {
                newNode.next = oldHead;
                if (!ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                    oldHead = this.listeners;
                } else {
                    return;
                }
            } while (oldHead != Listener.TOMBSTONE);
        }
        executeListener(listener, executor);
    }

    @CanIgnoreReturnValue
    protected boolean set(@Nullable V value) {
        if (value == null) {
            Object valueToSet = NULL;
        } else {
            V valueToSet2 = value;
        }
        if (!ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            return false;
        }
        complete(this);
        return true;
    }

    @CanIgnoreReturnValue
    protected boolean setException(Throwable throwable) {
        if (!ATOMIC_HELPER.casValue(this, null, new Failure((Throwable) Preconditions.checkNotNull(throwable)))) {
            return false;
        }
        complete(this);
        return true;
    }

    @CanIgnoreReturnValue
    @Beta
    protected boolean setFuture(ListenableFuture<? extends V> future) {
        SetFuture valueToSet;
        Failure failure;
        Preconditions.checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                if (!ATOMIC_HELPER.casValue(this, null, getFutureValue(future))) {
                    return false;
                }
                complete(this);
                return true;
            }
            valueToSet = new SetFuture(this, future);
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                try {
                    future.addListener(valueToSet, MoreExecutors.directExecutor());
                    return true;
                } catch (Throwable th) {
                    failure = Failure.FALLBACK_INSTANCE;
                }
            } else {
                localValue = this.value;
            }
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation) localValue).wasInterrupted);
        }
        return false;
        ATOMIC_HELPER.casValue(this, valueToSet, failure);
        return true;
    }

    private static Object getFutureValue(ListenableFuture<?> future) {
        if (future instanceof TrustedFuture) {
            return ((AbstractFuture) future).value;
        }
        try {
            Object v = Futures.getDone(future);
            return v == null ? NULL : v;
        } catch (ExecutionException exception) {
            return new Failure(exception.getCause());
        } catch (CancellationException cancellation) {
            return new Cancellation(false, cancellation);
        } catch (Throwable t) {
            return new Failure(t);
        }
    }

    private static void complete(AbstractFuture<?> future) {
        Listener next = null;
        while (true) {
            future.releaseWaiters();
            future.afterDone();
            next = future.clearListeners(next);
            while (next != null) {
                Listener curr = next;
                next = next.next;
                Runnable task = curr.task;
                if (task instanceof SetFuture) {
                    SetFuture<?> setFuture = (SetFuture) task;
                    future = setFuture.owner;
                    if (future.value == setFuture) {
                        if (ATOMIC_HELPER.casValue(future, setFuture, getFutureValue(setFuture.future))) {
                        }
                    } else {
                        continue;
                    }
                } else {
                    executeListener(task, curr.executor);
                }
            }
            return;
        }
    }

    @Beta
    protected void afterDone() {
    }

    final Throwable trustedGetException() {
        return ((Failure) this.value).exception;
    }

    final void maybePropagateCancellation(@Nullable Future<?> related) {
        if (((related != null ? 1 : 0) & isCancelled()) != 0) {
            related.cancel(wasInterrupted());
        }
    }

    private void releaseWaiters() {
        Waiter head;
        do {
            head = this.waiters;
        } while (!ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
        for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
            currentWaiter.unpark();
        }
    }

    private Listener clearListeners(Listener onto) {
        Listener head;
        do {
            head = this.listeners;
        } while (!ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
        Listener reversedList = onto;
        while (head != null) {
            Listener tmp = head;
            head = head.next;
            tmp.next = reversedList;
            reversedList = tmp;
        }
        return reversedList;
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
        }
    }

    private static CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
        CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
}
