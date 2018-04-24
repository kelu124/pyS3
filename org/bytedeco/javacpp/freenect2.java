package org.bytedeco.javacpp;

public class freenect2 extends org.bytedeco.javacpp.presets.freenect2 {
    public static final int LIBFREENECT2_API_VERSION = 2;
    public static final String LIBFREENECT2_TEGRAJPEG_LIBRARY = "";
    public static final String LIBFREENECT2_VERSION = "0.2.0";

    static {
        Loader.load();
    }
}
