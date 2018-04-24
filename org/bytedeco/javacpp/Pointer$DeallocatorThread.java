package org.bytedeco.javacpp;

class Pointer$DeallocatorThread extends Thread {
    Pointer$DeallocatorThread() {
        super("JavaCPP Deallocator");
        setPriority(10);
        setDaemon(true);
        start();
    }

    public void run() {
        while (true) {
            try {
                Pointer$DeallocatorReference r = (Pointer$DeallocatorReference) Pointer.access$000().remove();
                r.clear();
                r.remove();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
