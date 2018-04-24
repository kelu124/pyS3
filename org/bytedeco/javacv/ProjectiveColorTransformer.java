package org.bytedeco.javacv;

import java.util.Arrays;
import org.bytedeco.javacpp.cvkernels.KernelData;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.ImageTransformer.Data;

public class ProjectiveColorTransformer extends ProjectiveTransformer {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ThreadLocal<CvMat> X24x4 = CvMat.createThreadLocal(4, 4);
    protected static ThreadLocal<CvMat> temp3x1 = CvMat.createThreadLocal(3, 1);
    protected CvMat f199X = null;
    protected CvMat[] X2 = null;
    protected int numBiases = 0;
    protected int numGains = 0;

    public class Parameters extends org.bytedeco.javacv.ProjectiveTransformer.Parameters {
        static final /* synthetic */ boolean $assertionsDisabled = (!ProjectiveColorTransformer.class.desiredAssertionStatus());
        private CvMat f193A = null;
        private CvMat f194b = null;
        protected double[] colorParameters = null;
        protected double[] identityColorParameters = null;

        protected Parameters() {
            super();
            this.identityColorParameters = new double[(ProjectiveColorTransformer.this.numGains + ProjectiveColorTransformer.this.numBiases)];
            if (ProjectiveColorTransformer.this.numGains > 0) {
                this.f193A = CvMat.create(3, 3);
                opencv_core.cvSetIdentity(this.f193A);
            }
            if (ProjectiveColorTransformer.this.numBiases > 0) {
                this.f194b = CvMat.create(3, 1);
                opencv_core.cvSetZero(this.f194b);
            }
            switch (ProjectiveColorTransformer.this.numGains) {
                case 0:
                    if (!($assertionsDisabled || this.f193A == null)) {
                        throw new AssertionError();
                    }
                case 1:
                    this.identityColorParameters[0] = ((this.f193A.get(0) + this.f193A.get(4)) + this.f193A.get(8)) / 3.0d;
                    break;
                case 3:
                    this.identityColorParameters[0] = this.f193A.get(0);
                    this.identityColorParameters[1] = this.f193A.get(4);
                    this.identityColorParameters[2] = this.f193A.get(8);
                    break;
                case 9:
                    this.f193A.get(0, this.identityColorParameters, 0, 9);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            switch (ProjectiveColorTransformer.this.numBiases) {
                case 0:
                    if (!($assertionsDisabled || this.f194b == null)) {
                        throw new AssertionError();
                    }
                case 1:
                    this.identityColorParameters[ProjectiveColorTransformer.this.numGains] = ((this.f194b.get(0) + this.f194b.get(1)) + this.f194b.get(2)) / 3.0d;
                    break;
                case 3:
                    this.f194b.get(0, this.identityColorParameters, ProjectiveColorTransformer.this.numGains, 3);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            reset(false);
        }

        public double[] getColorParameters() {
            return this.colorParameters;
        }

        public double[] getIdentityColorParameters() {
            return this.identityColorParameters;
        }

        public int size() {
            return (super.size() + ProjectiveColorTransformer.this.numGains) + ProjectiveColorTransformer.this.numBiases;
        }

        public double get(int i) {
            int s = super.size();
            if (i < s) {
                return super.get(i);
            }
            return this.colorParameters[i - s];
        }

        public void set(int i, double p) {
            int s = super.size();
            if (i < s) {
                super.set(i, p);
            } else if (this.colorParameters[i - s] != p) {
                this.colorParameters[i - s] = p;
                setUpdateNeeded(true);
            }
        }

        public void reset(boolean asIdentity) {
            super.reset(asIdentity);
            resetColor(asIdentity);
        }

        public void resetColor(boolean asIdentity) {
            if (this.identityColorParameters == null) {
                return;
            }
            if (!Arrays.equals(this.colorParameters, this.identityColorParameters) || this.fakeIdentity != asIdentity) {
                this.fakeIdentity = asIdentity;
                this.colorParameters = (double[]) this.identityColorParameters.clone();
                setUpdateNeeded(true);
            }
        }

        public void compose(org.bytedeco.javacv.ImageTransformer.Parameters p1, boolean inverse1, org.bytedeco.javacv.ImageTransformer.Parameters p2, boolean inverse2) {
            super.compose(p1, inverse1, p2, inverse2);
            composeColor(p1, inverse1, p2, inverse2);
        }

        public void composeColor(org.bytedeco.javacv.ImageTransformer.Parameters p1, boolean inverse1, org.bytedeco.javacv.ImageTransformer.Parameters p2, boolean inverse2) {
            if ($assertionsDisabled || !(inverse1 || inverse2)) {
                Parameters pp1 = (Parameters) p1;
                Parameters pp2 = (Parameters) p2;
                CvMat A1 = pp1.getA();
                CvMat b1 = pp1.getB();
                CvMat A2 = pp2.getA();
                CvMat b2 = pp2.getB();
                if (this.f194b != null) {
                    if (pp1.fakeIdentity && ProjectiveColorTransformer.this.f199X != null) {
                        CvMat temp = (CvMat) ProjectiveColorTransformer.temp3x1.get();
                        opencv_core.cvMatMul(ProjectiveColorTransformer.this.f199X, b1, temp);
                        b1 = temp;
                    }
                    if (A2 == null && b2 == null) {
                        opencv_core.cvCopy(b1, this.f194b);
                    } else if (b1 == null) {
                        opencv_core.cvCopy(b2, this.f194b);
                    } else if (b2 == null) {
                        opencv_core.cvMatMul(A2, b1, this.f194b);
                    } else {
                        opencv_core.cvGEMM(A2, b1, 1.0d, b2, 1.0d, this.f194b, 0);
                    }
                }
                if (this.f193A != null) {
                    if (A1 == null) {
                        opencv_core.cvCopy(A2, this.f193A);
                    } else if (A2 == null) {
                        opencv_core.cvCopy(A1, this.f193A);
                    } else {
                        opencv_core.cvMatMul(A2, A1, this.f193A);
                    }
                }
                switch (ProjectiveColorTransformer.this.numGains) {
                    case 0:
                        if (!($assertionsDisabled || this.f193A == null)) {
                            throw new AssertionError();
                        }
                    case 1:
                        this.colorParameters[0] = ((this.f193A.get(0) + this.f193A.get(4)) + this.f193A.get(8)) / 3.0d;
                        break;
                    case 3:
                        this.colorParameters[0] = this.f193A.get(0);
                        this.colorParameters[1] = this.f193A.get(4);
                        this.colorParameters[2] = this.f193A.get(8);
                        break;
                    case 9:
                        this.f193A.get(0, this.colorParameters, 0, 9);
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
                switch (ProjectiveColorTransformer.this.numBiases) {
                    case 0:
                        if (!$assertionsDisabled && this.f194b != null) {
                            throw new AssertionError();
                        }
                        return;
                    case 1:
                        this.colorParameters[ProjectiveColorTransformer.this.numGains] = ((this.f194b.get(0) + this.f194b.get(1)) + this.f194b.get(2)) / 3.0d;
                        return;
                    case 3:
                        this.f194b.get(0, this.colorParameters, ProjectiveColorTransformer.this.numGains, 3);
                        return;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        return;
                }
            }
            throw new AssertionError();
        }

        public CvMat getA() {
            update();
            return this.f193A;
        }

        public CvMat getB() {
            update();
            return this.f194b;
        }

        protected void update() {
            if (isUpdateNeeded()) {
                switch (ProjectiveColorTransformer.this.numGains) {
                    case 0:
                        if (!($assertionsDisabled || this.f193A == null)) {
                            throw new AssertionError();
                        }
                    case 1:
                        this.f193A.put(0, this.colorParameters[0]);
                        this.f193A.put(4, this.colorParameters[0]);
                        this.f193A.put(8, this.colorParameters[0]);
                        break;
                    case 3:
                        this.f193A.put(0, this.colorParameters[0]);
                        this.f193A.put(4, this.colorParameters[1]);
                        this.f193A.put(8, this.colorParameters[2]);
                        break;
                    case 9:
                        this.f193A.put(0, this.colorParameters, 0, 9);
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
                switch (ProjectiveColorTransformer.this.numBiases) {
                    case 0:
                        if (!($assertionsDisabled || this.f194b == null)) {
                            throw new AssertionError();
                        }
                    case 1:
                        this.f194b.put(0, this.colorParameters[ProjectiveColorTransformer.this.numGains]);
                        this.f194b.put(1, this.colorParameters[ProjectiveColorTransformer.this.numGains]);
                        this.f194b.put(2, this.colorParameters[ProjectiveColorTransformer.this.numGains]);
                        break;
                    case 3:
                        this.f194b.put(0, this.colorParameters, ProjectiveColorTransformer.this.numGains, 3);
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
                super.update();
                setUpdateNeeded(false);
            }
        }

        public Parameters clone() {
            Parameters p = new Parameters();
            p.set((org.bytedeco.javacv.ImageTransformer.Parameters) this);
            return p;
        }
    }

    static {
        boolean z;
        if (ProjectiveColorTransformer.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProjectiveColorTransformer(CvMat K1, CvMat K2, CvMat R, CvMat t, CvMat n, double[] referencePoints1, double[] referencePoints2, CvMat X, int numGains, int numBiases) {
        CvMat cvMat = null;
        super(K1, K2, R, t, n, referencePoints1, referencePoints2);
        if (X != null) {
            cvMat = X.clone();
        }
        this.f199X = cvMat;
        this.numGains = numGains;
        this.numBiases = numBiases;
    }

    public CvMat getX() {
        return this.f199X;
    }

    public int getNumGains() {
        return this.numGains;
    }

    public int getNumBiases() {
        return this.numBiases;
    }

    public void transformColor(IplImage srcImage, IplImage dstImage, CvRect roi, int pyramidLevel, org.bytedeco.javacv.ImageTransformer.Parameters parameters, boolean inverse) {
        Parameters p = (Parameters) parameters;
        if ((!Arrays.equals(p.getColorParameters(), p.getIdentityColorParameters()) || (this.f199X != null && !p.fakeIdentity)) && (this.f199X != null || this.numGains != 0 || this.numBiases != 0)) {
            CvMat X2 = (CvMat) X24x4.get();
            prepareColorTransform(X2, pyramidLevel, p, inverse);
            X2.rows(3);
            if (roi == null) {
                opencv_core.cvResetImageROI(dstImage);
            } else {
                opencv_core.cvSetImageROI(dstImage, roi);
            }
            X2.put(0, 3, X2.get(0, 3) * dstImage.highValue());
            X2.put(1, 3, X2.get(1, 3) * dstImage.highValue());
            X2.put(2, 3, X2.get(2, 3) * dstImage.highValue());
            opencv_core.cvTransform(srcImage, dstImage, X2, null);
            X2.rows(4);
        } else if (srcImage != dstImage) {
            opencv_core.cvCopy(srcImage, dstImage);
        }
    }

    protected void prepareColorTransform(CvMat X2, int pyramidLevel, Parameters p, boolean inverse) {
        CvMat A = p.getA();
        CvMat b = p.getB();
        opencv_core.cvSetIdentity(X2);
        X2.rows(3);
        X2.cols(3);
        if (p.fakeIdentity && !inverse) {
            X2.put(A);
        } else if (A != null && this.f199X != null) {
            opencv_core.cvMatMul(this.f199X, A, X2);
        } else if (this.f199X == null) {
            X2.put(A);
        } else if (A == null) {
            X2.put(this.f199X);
        }
        X2.rows(4);
        X2.cols(4);
        if (b != null) {
            X2.put(0, 3, b.get(0));
            X2.put(1, 3, b.get(1));
            X2.put(2, 3, b.get(2));
        }
        if (inverse) {
            opencv_core.cvInvert(X2, X2, 1);
        }
    }

    public void transform(Data[] data, CvRect roi, org.bytedeco.javacv.ImageTransformer.Parameters[] parameters, boolean[] inverses) {
        if ($assertionsDisabled || data.length == parameters.length) {
            int i;
            if (this.kernelData == null || this.kernelData.capacity() < ((long) data.length)) {
                this.kernelData = new KernelData((long) data.length);
            }
            if (this.H == null || this.H.length < data.length) {
                this.H = new CvMat[data.length];
                for (i = 0; i < this.H.length; i++) {
                    this.H[i] = CvMat.create(3, 3);
                }
            }
            if (this.X2 == null || this.X2.length < data.length) {
                this.X2 = new CvMat[data.length];
                for (i = 0; i < this.X2.length; i++) {
                    this.X2[i] = CvMat.create(4, 4);
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
                boolean inverse = inverses == null ? false : inverses[i];
                prepareHomography(this.H[i], data[i].pyramidLevel, (Parameters) parameters[i], inverse);
                prepareColorTransform(this.X2[i], data[i].pyramidLevel, (Parameters) parameters[i], inverse);
                this.kernelData.H1(this.H[i]);
                this.kernelData.H2(null);
                this.kernelData.X(this.X2[i]);
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
