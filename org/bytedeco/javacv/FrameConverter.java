package org.bytedeco.javacv;

public abstract class FrameConverter<F> {
    protected Frame frame;

    public abstract F convert(Frame frame);

    public abstract Frame convert(F f);
}
