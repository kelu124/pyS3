package org.bytedeco.javacpp;

import android.support.v4.media.session.PlaybackStateCompat;
import com.itextpdf.text.pdf.PdfBoolean;
import java.lang.ref.ReferenceQueue;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.tools.Logger;

@Platform
public class Pointer implements AutoCloseable {
    static final Thread deallocatorThread;
    private static final Logger logger = Logger.create(Pointer.class);
    static final long maxBytes;
    static final long maxPhysicalBytes;
    static final int maxRetries;
    private static final ReferenceQueue<Pointer> referenceQueue;
    protected long address = 0;
    protected long capacity = 0;
    private Deallocator deallocator = null;
    protected long limit = 0;
    protected long position = 0;

    private native void allocate(Buffer buffer);

    private native ByteBuffer asDirectBuffer();

    public static native Pointer calloc(long j, long j2);

    public static native void free(Pointer pointer);

    public static native Pointer malloc(long j);

    public static native Pointer memchr(Pointer pointer, int i, long j);

    public static native int memcmp(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memcpy(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memmove(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memset(Pointer pointer, int i, long j);

    @Name({"JavaCPP_physicalBytes"})
    public static native synchronized long physicalBytes();

    public static native Pointer realloc(Pointer pointer, long j);

    public Pointer(Pointer p) {
        if (p != null) {
            this.address = p.address;
            this.position = p.position;
            this.limit = p.limit;
            this.capacity = p.capacity;
            if (p.deallocator != null) {
                this.deallocator = new 1(this, p);
            }
        }
    }

    public Pointer(Buffer b) {
        if (b != null) {
            allocate(b);
        }
        if (!isNull()) {
            this.position = (long) b.position();
            this.limit = (long) b.limit();
            this.capacity = (long) b.capacity();
            this.deallocator = new 2(this, b);
        }
    }

    void init(long allocatedAddress, long allocatedCapacity, long ownerAddress, long deallocatorAddress) {
        this.address = allocatedAddress;
        this.position = 0;
        this.limit = allocatedCapacity;
        this.capacity = allocatedCapacity;
        if (ownerAddress != 0 && deallocatorAddress != 0) {
            deallocator(new NativeDeallocator(this, ownerAddress, deallocatorAddress));
        }
    }

    protected static <P extends Pointer> P withDeallocator(P p) {
        return p.deallocator(new CustomDeallocator(p));
    }

    static {
        String s = System.getProperty("org.bytedeco.javacpp.nopointergc", PdfBoolean.FALSE).toLowerCase();
        if (s.equals(PdfBoolean.TRUE) || s.equals("t") || s.equals("")) {
            referenceQueue = null;
            deallocatorThread = null;
        } else {
            referenceQueue = new ReferenceQueue();
            deallocatorThread = new DeallocatorThread();
        }
        long m = Runtime.getRuntime().maxMemory();
        s = System.getProperty("org.bytedeco.javacpp.maxbytes");
        if (s != null && s.length() > 0) {
            try {
                m = parseBytes(s);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        maxBytes = m;
        m = 2 * Runtime.getRuntime().maxMemory();
        s = System.getProperty("org.bytedeco.javacpp.maxphysicalbytes");
        if (s != null && s.length() > 0) {
            try {
                m = parseBytes(s);
            } catch (NumberFormatException e2) {
                throw new RuntimeException(e2);
            }
        }
        maxPhysicalBytes = m;
        int n = 10;
        s = System.getProperty("org.bytedeco.javacpp.maxretries");
        if (s != null && s.length() > 0) {
            try {
                n = Integer.parseInt(s);
            } catch (NumberFormatException e22) {
                throw new RuntimeException(e22);
            }
        }
        maxRetries = n;
    }

    static String formatBytes(long bytes) {
        if (bytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return bytes + "";
        }
        bytes /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (bytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return bytes + "K";
        }
        bytes /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (bytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return bytes + "M";
        }
        bytes /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (bytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return bytes + "G";
        }
        return (bytes / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "T";
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static long parseBytes(java.lang.String r10) throws java.lang.NumberFormatException {
        /*
        r1 = 0;
        r8 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = 0;
    L_0x0004:
        r4 = r10.length();
        if (r0 >= r4) goto L_0x0014;
    L_0x000a:
        r4 = r10.charAt(r0);
        r4 = java.lang.Character.isDigit(r4);
        if (r4 != 0) goto L_0x004d;
    L_0x0014:
        r4 = r10.substring(r1, r0);
        r2 = java.lang.Long.parseLong(r4);
        r4 = r10.substring(r0);
        r4 = r4.trim();
        r5 = r4.toLowerCase();
        r4 = -1;
        r6 = r5.hashCode();
        switch(r6) {
            case 0: goto L_0x009f;
            case 103: goto L_0x0063;
            case 107: goto L_0x008b;
            case 109: goto L_0x0077;
            case 116: goto L_0x0050;
            case 3291: goto L_0x006d;
            case 3415: goto L_0x0095;
            case 3477: goto L_0x0081;
            case 3694: goto L_0x0059;
            default: goto L_0x0030;
        };
    L_0x0030:
        r1 = r4;
    L_0x0031:
        switch(r1) {
            case 0: goto L_0x00aa;
            case 1: goto L_0x00aa;
            case 2: goto L_0x00ab;
            case 3: goto L_0x00ab;
            case 4: goto L_0x00ac;
            case 5: goto L_0x00ac;
            case 6: goto L_0x00ad;
            case 7: goto L_0x00ad;
            case 8: goto L_0x00ae;
            default: goto L_0x0034;
        };
    L_0x0034:
        r1 = new java.lang.NumberFormatException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Cannot parse into bytes: ";
        r4 = r4.append(r5);
        r4 = r4.append(r10);
        r4 = r4.toString();
        r1.<init>(r4);
        throw r1;
    L_0x004d:
        r0 = r0 + 1;
        goto L_0x0004;
    L_0x0050:
        r6 = "t";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0030;
    L_0x0058:
        goto L_0x0031;
    L_0x0059:
        r1 = "tb";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x0061:
        r1 = 1;
        goto L_0x0031;
    L_0x0063:
        r1 = "g";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x006b:
        r1 = 2;
        goto L_0x0031;
    L_0x006d:
        r1 = "gb";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x0075:
        r1 = 3;
        goto L_0x0031;
    L_0x0077:
        r1 = "m";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x007f:
        r1 = 4;
        goto L_0x0031;
    L_0x0081:
        r1 = "mb";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x0089:
        r1 = 5;
        goto L_0x0031;
    L_0x008b:
        r1 = "k";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x0093:
        r1 = 6;
        goto L_0x0031;
    L_0x0095:
        r1 = "kb";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x009d:
        r1 = 7;
        goto L_0x0031;
    L_0x009f:
        r1 = "";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x00a7:
        r1 = 8;
        goto L_0x0031;
    L_0x00aa:
        r2 = r2 * r8;
    L_0x00ab:
        r2 = r2 * r8;
    L_0x00ac:
        r2 = r2 * r8;
    L_0x00ad:
        r2 = r2 * r8;
    L_0x00ae:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.Pointer.parseBytes(java.lang.String):long");
    }

    public static void deallocateReferences() {
        while (referenceQueue != null) {
            DeallocatorReference r = (DeallocatorReference) referenceQueue.poll();
            if (r != null) {
                r.clear();
                r.remove();
            } else {
                return;
            }
        }
    }

    public static long maxBytes() {
        return maxBytes;
    }

    public static long totalBytes() {
        return DeallocatorReference.totalBytes;
    }

    public static long maxPhysicalBytes() {
        return maxPhysicalBytes;
    }

    public boolean isNull() {
        return this.address == 0;
    }

    public void setNull() {
        this.address = 0;
    }

    public long address() {
        return this.address;
    }

    public long position() {
        return this.position;
    }

    public <P extends Pointer> P position(long position) {
        this.position = position;
        return this;
    }

    public long limit() {
        return this.limit;
    }

    public <P extends Pointer> P limit(long limit) {
        this.limit = limit;
        return this;
    }

    public long capacity() {
        return this.capacity;
    }

    public <P extends Pointer> P capacity(long capacity) {
        this.limit = capacity;
        this.capacity = capacity;
        return this;
    }

    protected Deallocator deallocator() {
        return this.deallocator;
    }

    protected <P extends Pointer> P deallocator(Deallocator deallocator) {
        if (this.deallocator != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Predeallocating " + this);
            }
            this.deallocator.deallocate();
            this.deallocator = null;
        }
        if (!(deallocator == null || deallocator.equals(null))) {
            DeallocatorReference r;
            this.deallocator = deallocator;
            if (deallocator instanceof DeallocatorReference) {
                r = (DeallocatorReference) deallocator;
            } else {
                r = new DeallocatorReference(this, deallocator);
            }
            synchronized (DeallocatorThread.class) {
                long lastPhysicalBytes = 0;
                int i = 0;
                while (true) {
                    int count = i + 1;
                    try {
                        if (i >= maxRetries) {
                            break;
                        }
                        if (maxBytes <= 0 || DeallocatorReference.totalBytes + r.bytes <= maxBytes) {
                            if (maxPhysicalBytes <= 0) {
                                break;
                            }
                            lastPhysicalBytes = physicalBytes();
                            if (lastPhysicalBytes <= maxPhysicalBytes) {
                                break;
                            }
                        }
                        System.gc();
                        Thread.sleep(100);
                        i = count;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (UnsatisfiedLinkError e2) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(e2.getMessage());
                        }
                    }
                }
                if (maxBytes > 0 && DeallocatorReference.totalBytes + r.bytes > maxBytes) {
                    deallocate();
                    throw new OutOfMemoryError("Failed to allocate memory within limits: totalBytes = " + formatBytes(DeallocatorReference.totalBytes) + " + " + formatBytes(r.bytes) + " > maxBytes = " + formatBytes(maxBytes));
                } else if (maxPhysicalBytes <= 0 || lastPhysicalBytes <= maxPhysicalBytes) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Registering " + this);
                    }
                    r.add();
                } else {
                    deallocate();
                    throw new OutOfMemoryError("Physical memory usage is too high: physicalBytes = " + formatBytes(lastPhysicalBytes) + " > maxPhysicalBytes = " + formatBytes(maxPhysicalBytes));
                }
            }
        }
        return this;
    }

    public void close() {
        deallocate();
    }

    public void deallocate() {
        deallocate(true);
    }

    public void deallocate(boolean deallocate) {
        if (!deallocate || this.deallocator == null) {
            synchronized (DeallocatorReference.class) {
                for (DeallocatorReference r = DeallocatorReference.head; r != null; r = r.next) {
                    if (r.deallocator == this.deallocator) {
                        r.deallocator = null;
                        r.clear();
                        r.remove();
                        break;
                    }
                }
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Deallocating " + this);
        }
        this.deallocator.deallocate();
        this.address = 0;
    }

    public int offsetof(String member) {
        int offset = -1;
        try {
            Class<? extends Pointer> c = getClass();
            if (c != Pointer.class) {
                offset = Loader.offsetof(c, member);
            }
            return offset;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int sizeof() {
        Class c = getClass();
        if (c == Pointer.class || c == BytePointer.class) {
            return 1;
        }
        return offsetof("sizeof");
    }

    public ByteBuffer asByteBuffer() {
        if (isNull()) {
            return null;
        }
        if (this.limit <= 0 || this.limit >= this.position) {
            long j;
            int valueSize = sizeof();
            long arrayPosition = this.position;
            long arrayLimit = this.limit;
            this.position = ((long) valueSize) * arrayPosition;
            long j2 = (long) valueSize;
            if (arrayLimit <= 0) {
                j = 1 + arrayPosition;
            } else {
                j = arrayLimit;
            }
            this.limit = j * j2;
            ByteBuffer b = asDirectBuffer().order(ByteOrder.nativeOrder());
            this.position = arrayPosition;
            this.limit = arrayLimit;
            return b;
        }
        throw new IllegalArgumentException("limit < position: (" + this.limit + " < " + this.position + ")");
    }

    public Buffer asBuffer() {
        return asByteBuffer();
    }

    public <P extends Pointer> P put(Pointer p) {
        if (p.limit <= 0 || p.limit >= p.position) {
            int size = sizeof();
            int psize = p.sizeof();
            long length = ((long) psize) * (p.limit <= 0 ? 1 : p.limit - p.position);
            this.position *= (long) size;
            p.position *= (long) psize;
            memcpy(this, p, length);
            this.position /= (long) size;
            p.position /= (long) psize;
            return this;
        }
        throw new IllegalArgumentException("limit < position: (" + p.limit + " < " + p.position + ")");
    }

    public <P extends Pointer> P fill(int b) {
        if (this.limit <= 0 || this.limit >= this.position) {
            int size = sizeof();
            long length = ((long) size) * (this.limit <= 0 ? 1 : this.limit - this.position);
            this.position *= (long) size;
            memset(this, b, length);
            this.position /= (long) size;
            return this;
        }
        throw new IllegalArgumentException("limit < position: (" + this.limit + " < " + this.position + ")");
    }

    public <P extends Pointer> P zero() {
        return fill(0);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return isNull();
        }
        if (obj.getClass() != getClass() && obj.getClass() != Pointer.class && getClass() != Pointer.class) {
            return false;
        }
        Pointer other = (Pointer) obj;
        if (this.address == other.address && this.position == other.position) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) this.address;
    }

    public String toString() {
        return getClass().getName() + "[address=0x" + Long.toHexString(this.address) + ",position=" + this.position + ",limit=" + this.limit + ",capacity=" + this.capacity + ",deallocator=" + this.deallocator + "]";
    }
}
