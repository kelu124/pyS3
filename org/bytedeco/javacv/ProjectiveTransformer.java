package org.bytedeco.javacv;

import org.bytedeco.javacpp.cvkernels.KernelData;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.ImageTransformer.Data;

public class ProjectiveTransformer implements ImageTransformer {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ThreadLocal<CvMat> H3x3 = CvMat.createThreadLocal(3, 3);
    protected static ThreadLocal<CvMat> pts4x1 = CvMat.createThreadLocal(4, 1, 6, 2);
    protected CvMat[] f195H;
    protected CvMat K1;
    protected CvMat K2;
    protected CvMat f196R;
    protected CvScalar fillColor;
    protected CvMat invK1;
    protected CvMat invK2;
    protected KernelData kernelData;
    protected CvMat f197n;
    protected double[] referencePoints1;
    protected double[] referencePoints2;
    protected CvMat f198t;

    public class Parameters implements org.bytedeco.javacv.ImageTransformer.Parameters {
        private CvMat f192H = CvMat.create(3, 3);
        private CvMat R2 = null;
        private double constraintError = 0.0d;
        protected boolean fakeIdentity = false;
        private CvMat n2 = null;
        protected double[] projectiveParameters = null;
        private CvMat t2 = null;
        private boolean updateNeeded = true;

        protected Parameters() {
            reset(false);
        }

        public boolean isUpdateNeeded() {
            return this.updateNeeded;
        }

        public void setUpdateNeeded(boolean updateNeeded) {
            this.updateNeeded = updateNeeded;
        }

        public int size() {
            return this.projectiveParameters.length;
        }

        public double[] get() {
            double[] p = new double[size()];
            for (int i = 0; i < p.length; i++) {
                p[i] = get(i);
            }
            return p;
        }

        public double get(int i) {
            return this.projectiveParameters[i];
        }

        public void set(double... p) {
            for (int i = 0; i < p.length; i++) {
                set(i, p[i]);
            }
        }

        public void set(int i, double p) {
            if (this.projectiveParameters[i] != p) {
                this.projectiveParameters[i] = p;
                setUpdateNeeded(true);
            }
        }

        public void set(org.bytedeco.javacv.ImageTransformer.Parameters p) {
            set(p.get());
            this.fakeIdentity = ((Parameters) p).fakeIdentity;
        }

        public void reset(boolean asIdentity) {
            setUpdateNeeded(true);
            if (ProjectiveTransformer.this.referencePoints1 == null || !(ProjectiveTransformer.this.referencePoints1.length == 0 || ProjectiveTransformer.this.referencePoints1.length == 8)) {
                if (ProjectiveTransformer.this.K2 != null && ProjectiveTransformer.this.invK1 != null) {
                    if (ProjectiveTransformer.this.f196R != null && ProjectiveTransformer.this.f198t != null) {
                        this.projectiveParameters = new double[]{ProjectiveTransformer.this.referencePoints1[0], ProjectiveTransformer.this.referencePoints1[2], ProjectiveTransformer.this.referencePoints1[4]};
                    } else if (ProjectiveTransformer.this.f197n != null) {
                        this.projectiveParameters = new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
                    } else {
                        this.projectiveParameters = new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
                    }
                }
            } else if (ProjectiveTransformer.this.referencePoints1.length == 0) {
                this.projectiveParameters = new double[]{1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d};
            } else {
                this.projectiveParameters = (double[]) ProjectiveTransformer.this.referencePoints1.clone();
            }
        }

        public double getConstraintError() {
            update();
            return this.constraintError;
        }

        public void set(CvMat setH, boolean inverse) {
            if (this.projectiveParameters.length != 8 || ProjectiveTransformer.this.referencePoints1 == null) {
                throw new UnsupportedOperationException("Set homography operation not supported.");
            }
            if (inverse) {
                opencv_core.cvInvert(setH, this.f192H);
            } else if (setH != this.f192H) {
                opencv_core.cvCopy(setH, this.f192H);
            }
            if (ProjectiveTransformer.this.referencePoints1.length == 0) {
                for (int i = 0; i < 8; i++) {
                    this.projectiveParameters[i] = this.f192H.get(i) / this.f192H.get(8);
                }
            } else {
                CvMat pts = ((CvMat) ProjectiveTransformer.pts4x1.get()).put(ProjectiveTransformer.this.referencePoints1);
                opencv_core.cvPerspectiveTransform(pts, pts, this.f192H);
                pts.get(this.projectiveParameters);
            }
            setUpdateNeeded(true);
        }

        public void compose(org.bytedeco.javacv.ImageTransformer.Parameters p1, boolean inverse1, org.bytedeco.javacv.ImageTransformer.Parameters p2, boolean inverse2) {
            Parameters pp1 = (Parameters) p1;
            Parameters pp2 = (Parameters) p2;
            if (ProjectiveTransformer.this.K2 == null || ProjectiveTransformer.this.invK1 == null || ProjectiveTransformer.this.f196R == null || ProjectiveTransformer.this.f198t == null || !pp1.fakeIdentity) {
                compose(pp1.getH(), inverse1, pp2.getH(), inverse2);
            }
        }

        public void compose(CvMat H1, boolean inverse1, CvMat H2, boolean inverse2) {
            if (inverse1 && inverse2) {
                opencv_core.cvMatMul(H2, H1, this.f192H);
                opencv_core.cvInvert(this.f192H, this.f192H);
            } else if (inverse1) {
                opencv_core.cvInvert(H1, this.f192H);
                opencv_core.cvMatMul(this.f192H, H2, this.f192H);
            } else if (inverse2) {
                opencv_core.cvInvert(H2, this.f192H);
                opencv_core.cvMatMul(H1, this.f192H, this.f192H);
            } else {
                opencv_core.cvMatMul(H1, H2, this.f192H);
            }
            set(this.f192H, false);
        }

        public CvMat getH() {
            update();
            return this.f192H;
        }

        public CvMat getN() {
            update();
            return this.n2;
        }

        public CvMat getR() {
            update();
            return this.R2;
        }

        public CvMat getT() {
            update();
            return this.t2;
        }

        protected void update() {
            if (isUpdateNeeded()) {
                if (ProjectiveTransformer.this.referencePoints1 == null || !(ProjectiveTransformer.this.referencePoints1.length == 0 || ProjectiveTransformer.this.referencePoints1.length == 8)) {
                    if (!(ProjectiveTransformer.this.K2 == null || ProjectiveTransformer.this.invK1 == null)) {
                        if (ProjectiveTransformer.this.f196R == null || ProjectiveTransformer.this.f198t == null) {
                            if (ProjectiveTransformer.this.f197n != null) {
                                this.n2 = ProjectiveTransformer.this.f197n;
                            } else {
                                if (this.n2 == null) {
                                    this.n2 = CvMat.create(3, 1);
                                }
                                this.n2.put(0, this.projectiveParameters, 8, 3);
                            }
                            if (this.R2 == null) {
                                this.R2 = CvMat.create(3, 3);
                            }
                            if (this.t2 == null) {
                                this.t2 = CvMat.create(3, 1);
                            }
                            this.t2.put(0, this.projectiveParameters, 0, 3);
                            opencv_calib3d.cvRodrigues2(this.t2, this.R2, null);
                            this.t2.put(0, this.projectiveParameters, 3, 3);
                            opencv_core.cvGEMM(this.t2, this.n2, -1.0d, this.R2, 1.0d, this.f192H, 2);
                        } else {
                            double[] src = ProjectiveTransformer.this.referencePoints2;
                            double[] dst = new double[]{this.projectiveParameters[0], ProjectiveTransformer.this.referencePoints1[1], this.projectiveParameters[1], ProjectiveTransformer.this.referencePoints1[3], this.projectiveParameters[2], ProjectiveTransformer.this.referencePoints1[5]};
                            if (this.R2 == null) {
                                this.R2 = CvMat.create(3, 3);
                            }
                            if (this.t2 == null) {
                                this.t2 = CvMat.create(3, 1);
                            }
                            opencv_core.cvTranspose(ProjectiveTransformer.this.f196R, this.R2);
                            opencv_core.cvGEMM(this.R2, ProjectiveTransformer.this.f198t, -1.0d, null, 0.0d, this.t2, 0);
                            JavaCV.getPerspectiveTransform(src, dst, ProjectiveTransformer.this.invK2, ProjectiveTransformer.this.K1, this.R2, this.t2, this.f192H);
                        }
                    }
                } else if (ProjectiveTransformer.this.referencePoints1.length == 0) {
                    this.f192H.put(0, this.projectiveParameters, 0, 8);
                    this.f192H.put(8, 1.0d);
                } else {
                    JavaCV.getPerspectiveTransform(ProjectiveTransformer.this.referencePoints1, this.projectiveParameters, this.f192H);
                }
                setUpdateNeeded(false);
            }
        }

        public boolean preoptimize() {
            return false;
        }

        public double[] getSubspace() {
            return null;
        }

        public void setSubspace(double... p) {
        }

        public Parameters clone() {
            Parameters p = new Parameters();
            p.set((org.bytedeco.javacv.ImageTransformer.Parameters) this);
            return p;
        }

        public String toString() {
            String s = "[";
            double[] p = get();
            for (int i = 0; i < p.length; i++) {
                s = s + ((float) p[i]);
                if (i < p.length - 1) {
                    s = s + ", ";
                }
            }
            return s + "]";
        }
    }

    static {
        boolean z;
        if (ProjectiveTransformer.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProjectiveTransformer() {
        this(null, null, null, null, null, new double[0], null);
    }

    public ProjectiveTransformer(double[] referencePoints) {
        this(null, null, null, null, null, referencePoints, null);
    }

    public ProjectiveTransformer(ProjectiveDevice d1, ProjectiveDevice d2, CvMat n, double[] referencePoints1, double[] referencePoints2) {
        this(d1.cameraMatrix, d2.cameraMatrix, d2.f182R, d2.f183T, n, referencePoints1, referencePoints2);
    }

    public ProjectiveTransformer(CvMat K1, CvMat K2, CvMat R, CvMat t, CvMat n, double[] referencePoints1, double[] referencePoints2) {
        this.K1 = null;
        this.K2 = null;
        this.invK1 = null;
        this.invK2 = null;
        this.f196R = null;
        this.f198t = null;
        this.f197n = null;
        this.referencePoints1 = null;
        this.referencePoints2 = null;
        this.fillColor = opencv_core.cvScalar(0.0d, 0.0d, 0.0d, 1.0d);
        this.kernelData = null;
        this.f195H = null;
        this.K1 = K1 == null ? null : K1.clone();
        this.K2 = K2 == null ? null : K2.clone();
        this.invK1 = K1 == null ? null : K1.clone();
        this.invK2 = K2 == null ? null : K2.clone();
        if (K1 != null) {
            opencv_core.cvInvert(K1, this.invK1);
        }
        if (K2 != null) {
            opencv_core.cvInvert(K2, this.invK2);
        }
        this.f196R = R == null ? null : R.clone();
        this.f198t = t == null ? null : t.clone();
        this.f197n = n == null ? null : n.clone();
        this.referencePoints1 = referencePoints1 == null ? null : (double[]) referencePoints1.clone();
        this.referencePoints2 = referencePoints2 == null ? null : (double[]) referencePoints2.clone();
    }

    public CvScalar getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(CvScalar fillColor) {
        this.fillColor = fillColor;
    }

    public double[] getReferencePoints1() {
        return this.referencePoints1;
    }

    public double[] getReferencePoints2() {
        return this.referencePoints2;
    }

    public CvMat getK1() {
        return this.K1;
    }

    public CvMat getK2() {
        return this.K2;
    }

    public CvMat getInvK1() {
        return this.invK1;
    }

    public CvMat getInvK2() {
        return this.invK2;
    }

    public CvMat getR() {
        return this.f196R;
    }

    public CvMat getT() {
        return this.f198t;
    }

    public CvMat getN() {
        return this.f197n;
    }

    protected void prepareHomography(CvMat H, int pyramidLevel, Parameters p, boolean inverse) {
        if (this.K2 == null || this.invK1 == null || this.f196R == null || this.f198t == null || !p.fakeIdentity) {
            if (inverse) {
                H.put(p.getH());
            } else {
                opencv_core.cvInvert(p.getH(), H);
            }
            if (pyramidLevel > 0) {
                int scale = 1 << pyramidLevel;
                H.put(2, H.get(2) / ((double) scale));
                H.put(5, H.get(5) / ((double) scale));
                H.put(6, H.get(6) * ((double) scale));
                H.put(7, H.get(7) * ((double) scale));
                return;
            }
            return;
        }
        opencv_core.cvSetIdentity(H);
    }

    public void transform(IplImage srcImage, IplImage dstImage, CvRect roi, int pyramidLevel, org.bytedeco.javacv.ImageTransformer.Parameters parameters, boolean inverse) {
        Parameters p = (Parameters) parameters;
        if (this.K2 == null || this.invK1 == null || this.f196R == null || this.f198t == null || !p.fakeIdentity) {
            CvMat H = (CvMat) H3x3.get();
            prepareHomography(H, pyramidLevel, p, true);
            if (!(roi == null || (roi.x() == 0 && roi.y() == 0))) {
                int x = roi.x();
                int y = roi.y();
                if (inverse) {
                    H.put(2, ((H.get(0) * ((double) x)) + (H.get(1) * ((double) y))) + H.get(2));
                    H.put(5, ((H.get(3) * ((double) x)) + (H.get(4) * ((double) y))) + H.get(5));
                    H.put(8, ((H.get(6) * ((double) x)) + (H.get(7) * ((double) y))) + H.get(8));
                } else {
                    H.put(0, H.get(0) - (((double) x) * H.get(6)));
                    H.put(1, H.get(1) - (((double) x) * H.get(7)));
                    H.put(2, H.get(2) - (((double) x) * H.get(8)));
                    H.put(3, H.get(3) - (((double) y) * H.get(6)));
                    H.put(4, H.get(4) - (((double) y) * H.get(7)));
                    H.put(5, H.get(5) - (((double) y) * H.get(8)));
                }
            }
            dstImage.origin(srcImage.origin());
            if (roi == null) {
                opencv_core.cvResetImageROI(dstImage);
            } else {
                opencv_core.cvSetImageROI(dstImage, roi);
            }
            opencv_imgproc.cvWarpPerspective(srcImage, dstImage, H, (inverse ? 16 : 0) | 9, getFillColor());
        } else if (srcImage != dstImage) {
            opencv_core.cvCopy(srcImage, dstImage);
        }
    }

    public void transform(CvMat srcPts, CvMat dstPts, org.bytedeco.javacv.ImageTransformer.Parameters parameters, boolean inverse) {
        CvMat H;
        Parameters p = (Parameters) parameters;
        if (inverse) {
            H = (CvMat) H3x3.get();
            opencv_core.cvInvert(p.getH(), H);
        } else {
            H = p.getH();
        }
        opencv_core.cvPerspectiveTransform(srcPts, dstPts, H);
    }

    public void transform(Data[] data, CvRect roi, org.bytedeco.javacv.ImageTransformer.Parameters[] parameters, boolean[] inverses) {
        if ($assertionsDisabled || data.length == parameters.length) {
            int i;
            if (this.kernelData == null || this.kernelData.capacity() < ((long) data.length)) {
                this.kernelData = new KernelData((long) data.length);
            }
            if (this.f195H == null || this.f195H.length < data.length) {
                this.f195H = new CvMat[data.length];
                for (i = 0; i < this.f195H.length; i++) {
                    this.f195H[i] = CvMat.create(3, 3);
                }
            }
            i = 0;
            while (i < data.length) {
                this.kernelData.position((long) i);
                this.kernelData.srcImg(data[i].srcImg);
                this.kernelData.srcImg2(null);
                this.kernelData.subImg(data[i].subImg);
                this.kernelData.srcDotImg(data[i].srcDotImg);
                this.kernelData.mask(data[i].mask);
                this.kernelData.zeroThreshold(data[i].zeroThreshold);
                this.kernelData.outlierThreshold(data[i].outlierThreshold);
                prepareHomography(this.f195H[i], data[i].pyramidLevel, (Parameters) parameters[i], inverses == null ? false : inverses[i]);
                this.kernelData.H1(this.f195H[i]);
                this.kernelData.H2(null);
                this.kernelData.X(null);
                this.kernelData.transImg(data[i].transImg);
                this.kernelData.dstImg(data[i].dstImg);
                this.kernelData.dstDstDot(data[i].dstDstDot);
                i++;
            }
            long fullCapacity = this.kernelData.capacity();
            this.kernelData.capacity((long) data.length);
            cvkernels.multiWarpColorTransform(this.kernelData, roi, getFillColor());
            this.kernelData.capacity(fullCapacity);
            for (i = 0; i < data.length; i++) {
                this.kernelData.position((long) i);
                data[i].dstCount = this.kernelData.dstCount();
                data[i].dstCountZero = this.kernelData.dstCountZero();
                data[i].dstCountOutlier = this.kernelData.dstCountOutlier();
                data[i].srcDstDot = this.kernelData.srcDstDot();
            }
            return;
        }
        throw new AssertionError();
    }

    public Parameters createParameters() {
        return new Parameters();
    }
}
