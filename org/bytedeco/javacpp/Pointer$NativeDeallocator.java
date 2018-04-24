package org.bytedeco.javacpp;

protected class Pointer$NativeDeallocator extends Pointer$DeallocatorReference implements Pointer$Deallocator {
    private long deallocatorAddress;
    private long ownerAddress;

    private native void deallocate(long j, long j2);

    Pointer$NativeDeallocator(Pointer p, long ownerAddress, long deallocatorAddress) {
        super(p, null);
        this.deallocator = this;
        this.ownerAddress = ownerAddress;
        this.deallocatorAddress = deallocatorAddress;
    }

    public void deallocate() {
        if (this.ownerAddress != 0 && this.deallocatorAddress != 0) {
            deallocate(this.ownerAddress, this.deallocatorAddress);
            this.deallocatorAddress = 0;
            this.ownerAddress = 0;
        }
    }

    public String toString() {
        return getClass().getName() + "[ownerAddress=0x" + Long.toHexString(this.ownerAddress) + ",deallocatorAddress=0x" + Long.toHexString(this.deallocatorAddress) + "]";
    }
}
