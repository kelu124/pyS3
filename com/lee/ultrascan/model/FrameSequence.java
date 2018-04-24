package com.lee.ultrascan.model;

import android.support.v4.util.CircularArray;
import com.lee.ultrascan.library.GrayFrame;
import com.lee.ultrascan.utils.LogUtils;

public class FrameSequence {
    private final int capacity = 128;
    private CircularArray<GrayFrame> frames = new CircularArray(128);

    public void add(GrayFrame grayFrame) {
        if (this.frames.size() == 128) {
            ((GrayFrame) this.frames.popFirst()).release();
        }
        this.frames.addLast(grayFrame);
        LogUtils.LOGE("frameImages", "add cur size:" + this.frames.size());
    }

    public GrayFrame getFrame(int index) {
        try {
            return (GrayFrame) this.frames.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public GrayFrame getLast() {
        try {
            return (GrayFrame) this.frames.getLast();
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void clear() {
        while (!this.frames.isEmpty()) {
            ((GrayFrame) this.frames.popLast()).release();
        }
    }

    public int size() {
        return this.frames.size();
    }
}
