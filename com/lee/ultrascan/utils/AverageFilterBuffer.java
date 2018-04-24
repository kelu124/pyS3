package com.lee.ultrascan.utils;

public class AverageFilterBuffer {
    private final int capacity;
    private int currentPos = 0;
    private float[] data;

    public AverageFilterBuffer(int capacity) {
        this.capacity = capacity;
        this.data = new float[capacity];
    }

    public void append(float value) {
        this.data[this.currentPos] = value;
        this.currentPos = (this.currentPos + 1) % this.capacity;
    }

    public float getFilteredValue() {
        float sum = 0.0f;
        for (float n : this.data) {
            sum += n;
        }
        return sum / ((float) this.capacity);
    }
}
