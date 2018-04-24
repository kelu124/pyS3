package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class Dispatcher {

    private static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                ((Subscriber) subscribers.next()).dispatchEvent(event);
            }
        }
    }

    private static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object event, Subscriber subscriber) {
                this.event = event;
                this.subscriber = subscriber;
            }
        }

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                this.queue.add(new EventWithSubscriber(event, (Subscriber) subscribers.next()));
            }
            while (true) {
                EventWithSubscriber e = (EventWithSubscriber) this.queue.poll();
                if (e != null) {
                    e.subscriber.dispatchEvent(e.event);
                } else {
                    return;
                }
            }
        }
    }

    private static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        class C06911 extends ThreadLocal<Queue<Event>> {
            C06911() {
            }

            protected Queue<Event> initialValue() {
                return Queues.newArrayDeque();
            }
        }

        class C06922 extends ThreadLocal<Boolean> {
            C06922() {
            }

            protected Boolean initialValue() {
                return Boolean.valueOf(false);
            }
        }

        private static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object event, Iterator<Subscriber> subscribers) {
                this.event = event;
                this.subscribers = subscribers;
            }
        }

        private PerThreadQueuedDispatcher() {
            this.queue = new C06911();
            this.dispatching = new C06922();
        }

        void dispatch(java.lang.Object r5, java.util.Iterator<com.google.common.eventbus.Subscriber> r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            com.google.common.base.Preconditions.checkNotNull(r5);
            com.google.common.base.Preconditions.checkNotNull(r6);
            r2 = r4.queue;
            r1 = r2.get();
            r1 = (java.util.Queue) r1;
            r2 = new com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event;
            r3 = 0;
            r2.<init>(r5, r6);
            r1.offer(r2);
            r2 = r4.dispatching;
            r2 = r2.get();
            r2 = (java.lang.Boolean) r2;
            r2 = r2.booleanValue();
            if (r2 != 0) goto L_0x0069;
        L_0x0025:
            r2 = r4.dispatching;
            r3 = 1;
            r3 = java.lang.Boolean.valueOf(r3);
            r2.set(r3);
        L_0x002f:
            r0 = r1.poll();	 Catch:{ all -> 0x0053 }
            r0 = (com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event) r0;	 Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x005f;	 Catch:{ all -> 0x0053 }
        L_0x0037:
            r2 = r0.subscribers;	 Catch:{ all -> 0x0053 }
            r2 = r2.hasNext();	 Catch:{ all -> 0x0053 }
            if (r2 == 0) goto L_0x002f;	 Catch:{ all -> 0x0053 }
        L_0x0041:
            r2 = r0.subscribers;	 Catch:{ all -> 0x0053 }
            r2 = r2.next();	 Catch:{ all -> 0x0053 }
            r2 = (com.google.common.eventbus.Subscriber) r2;	 Catch:{ all -> 0x0053 }
            r3 = r0.event;	 Catch:{ all -> 0x0053 }
            r2.dispatchEvent(r3);	 Catch:{ all -> 0x0053 }
            goto L_0x0037;
        L_0x0053:
            r2 = move-exception;
            r3 = r4.dispatching;
            r3.remove();
            r3 = r4.queue;
            r3.remove();
            throw r2;
        L_0x005f:
            r2 = r4.dispatching;
            r2.remove();
            r2 = r4.queue;
            r2.remove();
        L_0x0069:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.dispatch(java.lang.Object, java.util.Iterator):void");
        }
    }

    abstract void dispatch(Object obj, Iterator<Subscriber> it);

    Dispatcher() {
    }

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }
}
