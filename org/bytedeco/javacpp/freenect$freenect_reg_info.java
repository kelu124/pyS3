package org.bytedeco.javacpp;

public class freenect$freenect_reg_info extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int ax();

    public native freenect$freenect_reg_info ax(int i);

    public native int ay();

    public native freenect$freenect_reg_info ay(int i);

    public native int back_comp1();

    public native freenect$freenect_reg_info back_comp1(int i);

    public native int back_comp2();

    public native freenect$freenect_reg_info back_comp2(int i);

    public native int bx();

    public native freenect$freenect_reg_info bx(int i);

    public native int by();

    public native freenect$freenect_reg_info by(int i);

    public native int cx();

    public native freenect$freenect_reg_info cx(int i);

    public native int cy();

    public native freenect$freenect_reg_info cy(int i);

    public native int dx();

    public native freenect$freenect_reg_info dx(int i);

    public native int dx_beta_inc();

    public native freenect$freenect_reg_info dx_beta_inc(int i);

    public native int dx_beta_start();

    public native freenect$freenect_reg_info dx_beta_start(int i);

    public native int dx_center();

    public native freenect$freenect_reg_info dx_center(int i);

    public native int dx_start();

    public native freenect$freenect_reg_info dx_start(int i);

    public native int dxdx_start();

    public native freenect$freenect_reg_info dxdx_start(int i);

    public native int dxdxdx_start();

    public native freenect$freenect_reg_info dxdxdx_start(int i);

    public native int dxdxdy_start();

    public native freenect$freenect_reg_info dxdxdy_start(int i);

    public native int dxdy_start();

    public native freenect$freenect_reg_info dxdy_start(int i);

    public native int dy();

    public native freenect$freenect_reg_info dy(int i);

    public native int dy_beta_inc();

    public native freenect$freenect_reg_info dy_beta_inc(int i);

    public native int dy_beta_start();

    public native freenect$freenect_reg_info dy_beta_start(int i);

    public native int dy_start();

    public native freenect$freenect_reg_info dy_start(int i);

    public native int dydx_start();

    public native freenect$freenect_reg_info dydx_start(int i);

    public native int dydxdx_start();

    public native freenect$freenect_reg_info dydxdx_start(int i);

    public native int dydxdy_start();

    public native freenect$freenect_reg_info dydxdy_start(int i);

    public native int dydy_start();

    public native freenect$freenect_reg_info dydy_start(int i);

    public native int dydydx_start();

    public native freenect$freenect_reg_info dydydx_start(int i);

    public native int dydydy_start();

    public native freenect$freenect_reg_info dydydy_start(int i);

    public native int rollout_blank();

    public native freenect$freenect_reg_info rollout_blank(int i);

    public native int rollout_size();

    public native freenect$freenect_reg_info rollout_size(int i);

    static {
        Loader.load();
    }

    public freenect$freenect_reg_info() {
        super((Pointer) null);
        allocate();
    }

    public freenect$freenect_reg_info(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect$freenect_reg_info(Pointer p) {
        super(p);
    }

    public freenect$freenect_reg_info position(long position) {
        return (freenect$freenect_reg_info) super.position(position);
    }
}
