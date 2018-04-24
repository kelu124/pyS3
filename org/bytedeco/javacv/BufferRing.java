package org.bytedeco.javacv;

public class BufferRing<B extends ReleasableBuffer> {
    private Object[] buffers;
    private int position;

    public interface BufferFactory<B extends ReleasableBuffer> {
        B create();
    }

    public interface ReleasableBuffer {
        void release();
    }

    public BufferRing(BufferFactory<B> factory, int size) {
        this.buffers = new Object[size];
        for (int i = 0; i < size; i++) {
            this.buffers[i] = factory.create();
        }
        this.position = 0;
    }

    public int capacity() {
        return this.buffers.length;
    }

    public int position() {
        return this.position;
    }

    public BufferRing position(int position) {
        this.position = ((position % this.buffers.length) + this.buffers.length) % this.buffers.length;
        return this;
    }

    public B get() {
        return (ReleasableBuffer) this.buffers[this.position];
    }

    public B get(int offset) {
        return (ReleasableBuffer) this.buffers[(((this.position + offset) % this.buffers.length) + this.buffers.length) % this.buffers.length];
    }

    public void release() {
        for (Object obj : this.buffers) {
            ((ReleasableBuffer) obj).release();
        }
        this.buffers = null;
    }
}
