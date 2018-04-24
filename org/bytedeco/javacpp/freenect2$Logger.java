package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdString;

@Namespace("libfreenect2")
public class freenect2$Logger extends Pointer {
    public static final int Debug = 4;
    public static final int Error = 1;
    public static final int Info = 3;
    public static final int None = 0;
    public static final int Warning = 2;

    @Cast({"libfreenect2::Logger::Level"})
    public static native int getDefaultLevel();

    @StdString
    public static native BytePointer level2str(@Cast({"libfreenect2::Logger::Level"}) int i);

    @Cast({"libfreenect2::Logger::Level"})
    public native int level();

    public native void log(@Cast({"libfreenect2::Logger::Level"}) int i, @StdString String str);

    public native void log(@Cast({"libfreenect2::Logger::Level"}) int i, @StdString BytePointer bytePointer);

    static {
        Loader.load();
    }

    public freenect2$Logger(Pointer p) {
        super(p);
    }
}
