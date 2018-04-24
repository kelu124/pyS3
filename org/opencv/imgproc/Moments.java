package org.opencv.imgproc;

public class Moments {
    protected final long nativeObj;

    private static native long Moments_0();

    private static native void delete(long j);

    private static native double get_m00_0(long j);

    private static native double get_m01_0(long j);

    private static native double get_m02_0(long j);

    private static native double get_m03_0(long j);

    private static native double get_m10_0(long j);

    private static native double get_m11_0(long j);

    private static native double get_m12_0(long j);

    private static native double get_m20_0(long j);

    private static native double get_m21_0(long j);

    private static native double get_m30_0(long j);

    private static native double get_mu02_0(long j);

    private static native double get_mu03_0(long j);

    private static native double get_mu11_0(long j);

    private static native double get_mu12_0(long j);

    private static native double get_mu20_0(long j);

    private static native double get_mu21_0(long j);

    private static native double get_mu30_0(long j);

    private static native double get_nu02_0(long j);

    private static native double get_nu03_0(long j);

    private static native double get_nu11_0(long j);

    private static native double get_nu12_0(long j);

    private static native double get_nu20_0(long j);

    private static native double get_nu21_0(long j);

    private static native double get_nu30_0(long j);

    private static native void set_m00_0(long j, double d);

    private static native void set_m01_0(long j, double d);

    private static native void set_m02_0(long j, double d);

    private static native void set_m03_0(long j, double d);

    private static native void set_m10_0(long j, double d);

    private static native void set_m11_0(long j, double d);

    private static native void set_m12_0(long j, double d);

    private static native void set_m20_0(long j, double d);

    private static native void set_m21_0(long j, double d);

    private static native void set_m30_0(long j, double d);

    private static native void set_mu02_0(long j, double d);

    private static native void set_mu03_0(long j, double d);

    private static native void set_mu11_0(long j, double d);

    private static native void set_mu12_0(long j, double d);

    private static native void set_mu20_0(long j, double d);

    private static native void set_mu21_0(long j, double d);

    private static native void set_mu30_0(long j, double d);

    private static native void set_nu02_0(long j, double d);

    private static native void set_nu03_0(long j, double d);

    private static native void set_nu11_0(long j, double d);

    private static native void set_nu12_0(long j, double d);

    private static native void set_nu20_0(long j, double d);

    private static native void set_nu21_0(long j, double d);

    private static native void set_nu30_0(long j, double d);

    protected Moments(long addr) {
        this.nativeObj = addr;
    }

    public Moments() {
        this.nativeObj = Moments_0();
    }

    public double get_m00() {
        return get_m00_0(this.nativeObj);
    }

    public void set_m00(double m00) {
        set_m00_0(this.nativeObj, m00);
    }

    public double get_m10() {
        return get_m10_0(this.nativeObj);
    }

    public void set_m10(double m10) {
        set_m10_0(this.nativeObj, m10);
    }

    public double get_m01() {
        return get_m01_0(this.nativeObj);
    }

    public void set_m01(double m01) {
        set_m01_0(this.nativeObj, m01);
    }

    public double get_m20() {
        return get_m20_0(this.nativeObj);
    }

    public void set_m20(double m20) {
        set_m20_0(this.nativeObj, m20);
    }

    public double get_m11() {
        return get_m11_0(this.nativeObj);
    }

    public void set_m11(double m11) {
        set_m11_0(this.nativeObj, m11);
    }

    public double get_m02() {
        return get_m02_0(this.nativeObj);
    }

    public void set_m02(double m02) {
        set_m02_0(this.nativeObj, m02);
    }

    public double get_m30() {
        return get_m30_0(this.nativeObj);
    }

    public void set_m30(double m30) {
        set_m30_0(this.nativeObj, m30);
    }

    public double get_m21() {
        return get_m21_0(this.nativeObj);
    }

    public void set_m21(double m21) {
        set_m21_0(this.nativeObj, m21);
    }

    public double get_m12() {
        return get_m12_0(this.nativeObj);
    }

    public void set_m12(double m12) {
        set_m12_0(this.nativeObj, m12);
    }

    public double get_m03() {
        return get_m03_0(this.nativeObj);
    }

    public void set_m03(double m03) {
        set_m03_0(this.nativeObj, m03);
    }

    public double get_mu20() {
        return get_mu20_0(this.nativeObj);
    }

    public void set_mu20(double mu20) {
        set_mu20_0(this.nativeObj, mu20);
    }

    public double get_mu11() {
        return get_mu11_0(this.nativeObj);
    }

    public void set_mu11(double mu11) {
        set_mu11_0(this.nativeObj, mu11);
    }

    public double get_mu02() {
        return get_mu02_0(this.nativeObj);
    }

    public void set_mu02(double mu02) {
        set_mu02_0(this.nativeObj, mu02);
    }

    public double get_mu30() {
        return get_mu30_0(this.nativeObj);
    }

    public void set_mu30(double mu30) {
        set_mu30_0(this.nativeObj, mu30);
    }

    public double get_mu21() {
        return get_mu21_0(this.nativeObj);
    }

    public void set_mu21(double mu21) {
        set_mu21_0(this.nativeObj, mu21);
    }

    public double get_mu12() {
        return get_mu12_0(this.nativeObj);
    }

    public void set_mu12(double mu12) {
        set_mu12_0(this.nativeObj, mu12);
    }

    public double get_mu03() {
        return get_mu03_0(this.nativeObj);
    }

    public void set_mu03(double mu03) {
        set_mu03_0(this.nativeObj, mu03);
    }

    public double get_nu20() {
        return get_nu20_0(this.nativeObj);
    }

    public void set_nu20(double nu20) {
        set_nu20_0(this.nativeObj, nu20);
    }

    public double get_nu11() {
        return get_nu11_0(this.nativeObj);
    }

    public void set_nu11(double nu11) {
        set_nu11_0(this.nativeObj, nu11);
    }

    public double get_nu02() {
        return get_nu02_0(this.nativeObj);
    }

    public void set_nu02(double nu02) {
        set_nu02_0(this.nativeObj, nu02);
    }

    public double get_nu30() {
        return get_nu30_0(this.nativeObj);
    }

    public void set_nu30(double nu30) {
        set_nu30_0(this.nativeObj, nu30);
    }

    public double get_nu21() {
        return get_nu21_0(this.nativeObj);
    }

    public void set_nu21(double nu21) {
        set_nu21_0(this.nativeObj, nu21);
    }

    public double get_nu12() {
        return get_nu12_0(this.nativeObj);
    }

    public void set_nu12(double nu12) {
        set_nu12_0(this.nativeObj, nu12);
    }

    public double get_nu03() {
        return get_nu03_0(this.nativeObj);
    }

    public void set_nu03(double nu03) {
        set_nu03_0(this.nativeObj, nu03);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
