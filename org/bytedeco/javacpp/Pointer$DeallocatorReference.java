package org.bytedeco.javacpp;

import java.lang.ref.PhantomReference;

class Pointer$DeallocatorReference extends PhantomReference<Pointer> {
    static volatile Pointer$DeallocatorReference head = null;
    static volatile long totalBytes = 0;
    long bytes;
    Pointer$Deallocator deallocator;
    volatile Pointer$DeallocatorReference next = null;
    volatile Pointer$DeallocatorReference prev = null;

    Pointer$DeallocatorReference(Pointer p, Pointer$Deallocator deallocator) {
        super(p, Pointer.access$000());
        this.deallocator = deallocator;
        this.bytes = p.capacity * ((long) p.sizeof());
    }

    final void add() {
        synchronized (Pointer$DeallocatorReference.class) {
            if (head == null) {
                head = this;
            } else {
                this.next = head;
                Pointer$DeallocatorReference pointer$DeallocatorReference = this.next;
                head = this;
                pointer$DeallocatorReference.prev = this;
            }
            totalBytes += this.bytes;
        }
    }

    final void remove() {
        synchronized (Pointer$DeallocatorReference.class) {
            if (this.prev == this && this.next == this) {
                return;
            }
            if (this.prev == null) {
                head = this.next;
            } else {
                this.prev.next = this.next;
            }
            if (this.next != null) {
                this.next.prev = this.prev;
            }
            this.next = this;
            this.prev = this;
            totalBytes -= this.bytes;
        }
    }

    public void clear() {
        super.clear();
        if (this.deallocator != null) {
            if (Pointer.access$100().isDebugEnabled()) {
                Pointer.access$100().debug("Collecting " + this);
            }
            this.deallocator.deallocate();
            this.deallocator = null;
        }
    }

    public String toString() {
        return getClass().getName() + "[deallocator=" + this.deallocator + "]";
    }
}
