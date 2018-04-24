package org.bytedeco.javacpp;

public class freenect$freenect_zero_plane_info extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native float dcmos_emitter_dist();

    public native freenect$freenect_zero_plane_info dcmos_emitter_dist(float f);

    public native float dcmos_rcmos_dist();

    public native freenect$freenect_zero_plane_info dcmos_rcmos_dist(float f);

    public native float reference_distance();

    public native freenect$freenect_zero_plane_info reference_distance(float f);

    public native float reference_pixel_size();

    public native freenect$freenect_zero_plane_info reference_pixel_size(float f);

    static {
        Loader.load();
    }

    public freenect$freenect_zero_plane_info() {
        super((Pointer) null);
        allocate();
    }

    public freenect$freenect_zero_plane_info(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect$freenect_zero_plane_info(Pointer p) {
        super(p);
    }

    public freenect$freenect_zero_plane_info position(long position) {
        return (freenect$freenect_zero_plane_info) super.position(position);
    }
}
