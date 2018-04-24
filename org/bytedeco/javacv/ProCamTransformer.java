package org.bytedeco.javacv;

import org.bytedeco.javacpp.cvkernels.KernelData;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core$IplROI;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.ImageTransformer.Data;

public class ProCamTransformer implements ImageTransformer {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProCamTransformer.class.desiredAssertionStatus());
    protected CvMat[] H1;
    protected CvMat[] H2;
    protected CvMat[] f191X;
    protected CameraDevice camera;
    protected CvScalar fillColor;
    protected CvMat frontoParallelH;
    protected CvMat invCameraMatrix;
    protected CvMat invFrontoParallelH;
    protected KernelData kernelData;
    protected ProjectorDevice projector;
    protected IplImage[] projectorImage;
    protected ProjectiveColorTransformer projectorTransformer;
    protected CvRect roi;
    protected IplImage[] surfaceImage;
    protected ProjectiveColorTransformer surfaceTransformer;

    public class Parameters implements org.bytedeco.javacv.ImageTransformer.Parameters {
        private CvMat f187H = CvMat.create(3, 3);
        private CvMat f188R = CvMat.create(3, 3);
        private CvMat f189n = CvMat.create(3, 1);
        private org.bytedeco.javacv.ProjectiveColorTransformer.Parameters projectorParameters = null;
        private org.bytedeco.javacv.ProjectiveColorTransformer.Parameters surfaceParameters = null;
        private CvMat f190t = CvMat.create(3, 1);
        private IplImage[] tempImage = null;

        protected Parameters() {
            reset(false);
        }

        protected Parameters(org.bytedeco.javacv.ProjectiveColorTransformer.Parameters surfaceParameters, org.bytedeco.javacv.ProjectiveColorTransformer.Parameters projectorParameters) {
            reset(surfaceParameters, projectorParameters);
        }

        public org.bytedeco.javacv.ProjectiveColorTransformer.Parameters getSurfaceParameters() {
            return this.surfaceParameters;
        }

        public org.bytedeco.javacv.ProjectiveColorTransformer.Parameters getProjectorParameters() {
            return this.projectorParameters;
        }

        private int getSizeForSurface() {
            if (ProCamTransformer.this.surfaceTransformer == null) {
                return 0;
            }
            return (this.surfaceParameters.size() - ProCamTransformer.this.surfaceTransformer.getNumGains()) - ProCamTransformer.this.surfaceTransformer.getNumBiases();
        }

        private int getSizeForProjector() {
            return this.projectorParameters.size();
        }

        public int size() {
            return getSizeForSurface() + getSizeForProjector();
        }

        public double[] get() {
            double[] p = new double[size()];
            for (int i = 0; i < p.length; i++) {
                p[i] = get(i);
            }
            return p;
        }

        public double get(int i) {
            if (i < getSizeForSurface()) {
                return this.surfaceParameters.get(i);
            }
            return this.projectorParameters.get(i - getSizeForSurface());
        }

        public void set(double... p) {
            for (int i = 0; i < p.length; i++) {
                set(i, p[i]);
            }
        }

        public void set(int i, double p) {
            if (i < getSizeForSurface()) {
                this.surfaceParameters.set(i, p);
            } else {
                this.projectorParameters.set(i - getSizeForSurface(), p);
            }
        }

        public void set(org.bytedeco.javacv.ImageTransformer.Parameters p) {
            Parameters pcp = (Parameters) p;
            if (ProCamTransformer.this.surfaceTransformer != null) {
                this.surfaceParameters.set(pcp.getSurfaceParameters());
                this.surfaceParameters.resetColor(false);
            }
            this.projectorParameters.set(pcp.getProjectorParameters());
        }

        public void reset(boolean asIdentity) {
            reset(null, null);
        }

        public void reset(org.bytedeco.javacv.ProjectiveColorTransformer.Parameters surfaceParameters, org.bytedeco.javacv.ProjectiveColorTransformer.Parameters projectorParameters) {
            if (surfaceParameters == null && ProCamTransformer.this.surfaceTransformer != null) {
                surfaceParameters = ProCamTransformer.this.surfaceTransformer.createParameters();
            }
            if (projectorParameters == null) {
                projectorParameters = ProCamTransformer.this.projectorTransformer.createParameters();
            }
            this.surfaceParameters = surfaceParameters;
            this.projectorParameters = projectorParameters;
            setSubspace(getSubspace());
        }

        public double getConstraintError() {
            double error = ProCamTransformer.this.surfaceTransformer == null ? 0.0d : this.surfaceParameters.getConstraintError();
            this.projectorParameters.update();
            return error;
        }

        public void compose(org.bytedeco.javacv.ImageTransformer.Parameters p1, boolean inverse1, org.bytedeco.javacv.ImageTransformer.Parameters p2, boolean inverse2) {
            throw new UnsupportedOperationException("Compose operation not supported.");
        }

        public boolean preoptimize() {
            double[] p = setSubspaceInternal(getSubspaceInternal());
            if (p == null) {
                return false;
            }
            set(8, p[8]);
            set(9, p[9]);
            set(10, p[10]);
            return true;
        }

        public void setSubspace(double... p) {
            double[] dst = setSubspaceInternal(p);
            if (dst != null) {
                set(dst);
            }
        }

        public double[] getSubspace() {
            return getSubspaceInternal();
        }

        private double[] setSubspaceInternal(double... p) {
            if (ProCamTransformer.this.invFrontoParallelH == null) {
                return null;
            }
            dst = new double[11];
            this.f190t.put(new double[]{p[0], p[1], p[2]});
            opencv_calib3d.cvRodrigues2(this.f190t, this.f188R, null);
            this.f190t.put(new double[]{p[3], p[4], p[5]});
            this.f187H.put(new double[]{this.f188R.get(0), this.f188R.get(1), this.f190t.get(0), this.f188R.get(3), this.f188R.get(4), this.f190t.get(1), this.f188R.get(6), this.f188R.get(7), this.f190t.get(2)});
            opencv_core.cvMatMul(this.f187H, ProCamTransformer.this.invFrontoParallelH, this.f187H);
            opencv_core.cvMatMul(ProCamTransformer.this.surfaceTransformer.getK2(), this.f187H, this.f187H);
            opencv_core.cvMatMul(this.f187H, ProCamTransformer.this.surfaceTransformer.getInvK1(), this.f187H);
            opencv_core.cvGEMM(this.f188R, this.f190t, 1.0d, null, 0.0d, this.f190t, 1);
            double scale = 1.0d / this.f190t.get(2);
            this.f189n.put(new double[]{0.0d, 0.0d, 1.0d});
            opencv_core.cvGEMM(this.f188R, this.f189n, scale, null, 0.0d, this.f189n, 0);
            JavaCV.perspectiveTransform(ProCamTransformer.this.projectorTransformer.getReferencePoints2(), dst, ProCamTransformer.this.projectorTransformer.getInvK1(), ProCamTransformer.this.projectorTransformer.getK2(), ProCamTransformer.this.projectorTransformer.getR(), ProCamTransformer.this.projectorTransformer.getT(), this.f189n, true);
            dst[8] = dst[0];
            dst[9] = dst[2];
            dst[10] = dst[4];
            JavaCV.perspectiveTransform(ProCamTransformer.this.surfaceTransformer.getReferencePoints1(), dst, this.f187H);
            return dst;
        }

        private double[] getSubspaceInternal() {
            if (ProCamTransformer.this.frontoParallelH == null) {
                return null;
            }
            opencv_core.cvMatMul(ProCamTransformer.this.surfaceTransformer.getK1(), ProCamTransformer.this.frontoParallelH, this.f187H);
            opencv_core.cvMatMul(this.surfaceParameters.getH(), this.f187H, this.f187H);
            opencv_core.cvMatMul(ProCamTransformer.this.surfaceTransformer.getInvK2(), this.f187H, this.f187H);
            JavaCV.HtoRt(this.f187H, this.f188R, this.f190t);
            opencv_calib3d.cvRodrigues2(this.f188R, this.f189n, null);
            return new double[]{this.f189n.get(0), this.f189n.get(1), this.f189n.get(2), this.f190t.get(0), this.f190t.get(1), this.f190t.get(2)};
        }

        public CvMat getN() {
            double[] src = ProCamTransformer.this.projectorTransformer.getReferencePoints2();
            double[] dst = (double[]) ProCamTransformer.this.projectorTransformer.getReferencePoints1().clone();
            dst[0] = this.projectorParameters.get(0);
            dst[2] = this.projectorParameters.get(1);
            dst[4] = this.projectorParameters.get(2);
            opencv_core.cvTranspose(ProCamTransformer.this.projectorTransformer.getR(), this.f188R);
            opencv_core.cvGEMM(this.f188R, ProCamTransformer.this.projectorTransformer.getT(), -1.0d, null, 0.0d, this.f190t, 0);
            JavaCV.getPlaneParameters(src, dst, ProCamTransformer.this.projectorTransformer.getInvK2(), ProCamTransformer.this.projectorTransformer.getK1(), this.f188R, this.f190t, this.f189n);
            opencv_core.cvGEMM(this.f188R, this.f189n, 1.0d / (1.0d + opencv_core.cvDotProduct(this.f189n, ProCamTransformer.this.projectorTransformer.getT())), null, 0.0d, this.f189n, 0);
            return this.f189n;
        }

        public CvMat getN0() {
            this.f189n = getN();
            if (ProCamTransformer.this.surfaceTransformer == null) {
                return this.f189n;
            }
            ProCamTransformer.this.camera.getFrontoParallelH(this.surfaceParameters.get(), this.f189n, this.f188R);
            opencv_core.cvInvert(this.surfaceParameters.getH(), this.f187H);
            opencv_core.cvMatMul(this.f187H, ProCamTransformer.this.surfaceTransformer.getK2(), this.f187H);
            opencv_core.cvMatMul(this.f187H, this.f188R, this.f187H);
            opencv_core.cvMatMul(ProCamTransformer.this.surfaceTransformer.getInvK1(), this.f187H, this.f187H);
            JavaCV.HtoRt(this.f187H, this.f188R, this.f190t);
            opencv_core.cvGEMM(this.f188R, this.f190t, 1.0d, null, 0.0d, this.f190t, 1);
            double scale = 1.0d / this.f190t.get(2);
            this.f189n.put(new double[]{0.0d, 0.0d, 1.0d});
            opencv_core.cvGEMM(this.f188R, this.f189n, scale, null, 0.0d, this.f189n, 0);
            return this.f189n;
        }

        public Parameters clone() {
            Parameters p = new Parameters();
            p.surfaceParameters = this.surfaceParameters == null ? null : this.surfaceParameters.clone();
            p.projectorParameters = this.projectorParameters.clone();
            return p;
        }

        public String toString() {
            if (this.surfaceParameters != null) {
                return this.surfaceParameters.toString() + this.projectorParameters.toString();
            }
            return this.projectorParameters.toString();
        }
    }

    public ProCamTransformer(double[] referencePoints, CameraDevice camera, ProjectorDevice projector) {
        this(referencePoints, camera, projector, null);
    }

    public ProCamTransformer(double[] referencePoints, CameraDevice camera, ProjectorDevice projector, CvMat n) {
        this.camera = null;
        this.projector = null;
        this.surfaceTransformer = null;
        this.projectorTransformer = null;
        this.projectorImage = null;
        this.surfaceImage = null;
        this.fillColor = opencv_core.cvScalar(0.0d, 0.0d, 0.0d, 1.0d);
        this.roi = new CvRect();
        this.frontoParallelH = null;
        this.invFrontoParallelH = null;
        this.invCameraMatrix = null;
        this.kernelData = null;
        this.H1 = null;
        this.H2 = null;
        this.f191X = null;
        this.camera = camera;
        this.projector = projector;
        if (referencePoints != null) {
            this.surfaceTransformer = new ProjectiveColorTransformer(camera.cameraMatrix, camera.cameraMatrix, null, null, n, referencePoints, null, null, 3, 0);
        }
        double[] referencePoints1 = new double[]{0.0d, 0.0d, (double) (camera.imageWidth / 2), (double) camera.imageHeight, (double) camera.imageWidth, 0.0d};
        double[] referencePoints2 = new double[]{0.0d, 0.0d, (double) (projector.imageWidth / 2), (double) projector.imageHeight, (double) projector.imageWidth, 0.0d};
        if (n != null) {
            this.invCameraMatrix = CvMat.create(3, 3);
            opencv_core.cvInvert(camera.cameraMatrix, this.invCameraMatrix);
            JavaCV.perspectiveTransform(referencePoints2, referencePoints1, this.invCameraMatrix, projector.cameraMatrix, projector.R, projector.T, n, true);
        }
        this.projectorTransformer = new ProjectiveColorTransformer(camera.cameraMatrix, projector.cameraMatrix, projector.R, projector.T, null, referencePoints1, referencePoints2, projector.colorMixingMatrix, 1, 3);
        if (referencePoints != null && n != null) {
            this.frontoParallelH = camera.getFrontoParallelH(referencePoints, n, CvMat.create(3, 3));
            this.invFrontoParallelH = this.frontoParallelH.clone();
            opencv_core.cvInvert(this.frontoParallelH, this.invFrontoParallelH);
        }
    }

    public int getNumGains() {
        return this.projectorTransformer.getNumGains();
    }

    public int getNumBiases() {
        return this.projectorTransformer.getNumBiases();
    }

    public CvScalar getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(CvScalar fillColor) {
        this.fillColor = fillColor;
    }

    public ProjectiveColorTransformer getSurfaceTransformer() {
        return this.surfaceTransformer;
    }

    public ProjectiveColorTransformer getProjectorTransformer() {
        return this.projectorTransformer;
    }

    public IplImage getProjectorImage(int pyramidLevel) {
        return this.projectorImage[pyramidLevel];
    }

    public void setProjectorImage(IplImage projectorImage0, int minLevel, int maxLevel) {
        setProjectorImage(projectorImage0, minLevel, maxLevel, true);
    }

    public void setProjectorImage(IplImage projectorImage0, int minLevel, int maxLevel, boolean convertToFloat) {
        opencv_core$IplROI ir;
        if (this.projectorImage == null || this.projectorImage.length != maxLevel + 1) {
            this.projectorImage = new IplImage[(maxLevel + 1)];
        }
        if (projectorImage0.depth() == 32 || !convertToFloat) {
            this.projectorImage[minLevel] = projectorImage0;
        } else {
            if (this.projectorImage[minLevel] == null) {
                this.projectorImage[minLevel] = IplImage.create(projectorImage0.width(), projectorImage0.height(), 32, projectorImage0.nChannels(), projectorImage0.origin());
            }
            ir = projectorImage0.roi();
            if (ir != null) {
                int align = 1 << (maxLevel + 1);
                this.roi.x(Math.max(0, ((int) Math.floor(((double) ir.xOffset()) / ((double) align))) * align));
                this.roi.y(Math.max(0, ((int) Math.floor(((double) ir.yOffset()) / ((double) align))) * align));
                this.roi.width(Math.min(projectorImage0.width(), ((int) Math.ceil(((double) ir.width()) / ((double) align))) * align));
                this.roi.height(Math.min(projectorImage0.height(), ((int) Math.ceil(((double) ir.height()) / ((double) align))) * align));
                opencv_core.cvSetImageROI(projectorImage0, this.roi);
                opencv_core.cvSetImageROI(this.projectorImage[minLevel], this.roi);
            } else {
                opencv_core.cvResetImageROI(projectorImage0);
                opencv_core.cvResetImageROI(this.projectorImage[minLevel]);
            }
            opencv_core.cvConvertScale(projectorImage0, this.projectorImage[minLevel], 0.00392156862745098d, 0.0d);
        }
        for (int i = minLevel + 1; i <= maxLevel; i++) {
            int w = this.projectorImage[i - 1].width() / 2;
            int h = this.projectorImage[i - 1].height() / 2;
            int d = this.projectorImage[i - 1].depth();
            int c = this.projectorImage[i - 1].nChannels();
            int o = this.projectorImage[i - 1].origin();
            if (this.projectorImage[i] == null) {
                this.projectorImage[i] = IplImage.create(w, h, d, c, o);
            }
            ir = this.projectorImage[i - 1].roi();
            if (ir != null) {
                this.roi.x(ir.xOffset() / 2);
                this.roi.width(ir.width() / 2);
                this.roi.y(ir.yOffset() / 2);
                this.roi.height(ir.height() / 2);
                opencv_core.cvSetImageROI(this.projectorImage[i], this.roi);
            } else {
                opencv_core.cvResetImageROI(this.projectorImage[i]);
            }
            opencv_imgproc.cvPyrDown(this.projectorImage[i - 1], this.projectorImage[i], 7);
            opencv_core.cvResetImageROI(this.projectorImage[i - 1]);
        }
    }

    public IplImage getSurfaceImage(int pyramidLevel) {
        return this.surfaceImage[pyramidLevel];
    }

    public void setSurfaceImage(IplImage surfaceImage0, int pyramidLevels) {
        if (this.surfaceImage == null || this.surfaceImage.length != pyramidLevels) {
            this.surfaceImage = new IplImage[pyramidLevels];
        }
        this.surfaceImage[0] = surfaceImage0;
        opencv_core.cvResetImageROI(surfaceImage0);
        for (int i = 1; i < pyramidLevels; i++) {
            int w = this.surfaceImage[i - 1].width() / 2;
            int h = this.surfaceImage[i - 1].height() / 2;
            int d = this.surfaceImage[i - 1].depth();
            int c = this.surfaceImage[i - 1].nChannels();
            int o = this.surfaceImage[i - 1].origin();
            if (this.surfaceImage[i] == null) {
                this.surfaceImage[i] = IplImage.create(w, h, d, c, o);
            } else {
                opencv_core.cvResetImageROI(this.surfaceImage[i]);
            }
            opencv_imgproc.cvPyrDown(this.surfaceImage[i - 1], this.surfaceImage[i], 7);
        }
    }

    protected void prepareTransforms(CvMat H1, CvMat H2, CvMat X, int pyramidLevel, Parameters p) {
        org.bytedeco.javacv.ProjectiveColorTransformer.Parameters cameraParameters = p.getSurfaceParameters();
        org.bytedeco.javacv.ProjectiveColorTransformer.Parameters projectorParameters = p.getProjectorParameters();
        if (this.surfaceTransformer != null) {
            opencv_core.cvInvert(cameraParameters.getH(), H1);
        }
        opencv_core.cvInvert(projectorParameters.getH(), H2);
        if (pyramidLevel > 0) {
            int scale = 1 << pyramidLevel;
            if (this.surfaceTransformer != null) {
                H1.put(2, H1.get(2) / ((double) scale));
                H1.put(5, H1.get(5) / ((double) scale));
                H1.put(6, H1.get(6) * ((double) scale));
                H1.put(7, H1.get(7) * ((double) scale));
            }
            H2.put(2, H2.get(2) / ((double) scale));
            H2.put(5, H2.get(5) / ((double) scale));
            H2.put(6, H2.get(6) * ((double) scale));
            H2.put(7, H2.get(7) * ((double) scale));
        }
        double[] x = this.projector.colorMixingMatrix.get();
        double a2 = projectorParameters.getColorParameters()[0];
        X.put(new double[]{x[0] * a2, x[1] * a2, x[2] * a2, a[1], x[3] * a2, x[4] * a2, x[5] * a2, a[2], x[6] * a2, x[7] * a2, x[8] * a2, a[3], 0.0d, 0.0d, 0.0d, 1.0d});
    }

    public void transform(IplImage srcImage, IplImage dstImage, CvRect roi, int pyramidLevel, org.bytedeco.javacv.ImageTransformer.Parameters parameters, boolean inverse) {
        if (inverse) {
            throw new UnsupportedOperationException("Inverse transform not supported.");
        }
        Parameters p = (Parameters) parameters;
        org.bytedeco.javacv.ProjectiveTransformer.Parameters cameraParameters = p.getSurfaceParameters();
        org.bytedeco.javacv.ProjectiveTransformer.Parameters projectorParameters = p.getProjectorParameters();
        if (p.tempImage == null || p.tempImage.length <= pyramidLevel) {
            p.tempImage = new IplImage[(pyramidLevel + 1)];
        }
        p.tempImage[pyramidLevel] = IplImage.createIfNotCompatible(p.tempImage[pyramidLevel], dstImage);
        if (roi == null) {
            opencv_core.cvResetImageROI(p.tempImage[pyramidLevel]);
        } else {
            opencv_core.cvSetImageROI(p.tempImage[pyramidLevel], roi);
        }
        if (this.surfaceTransformer != null) {
            this.surfaceTransformer.transform(srcImage, p.tempImage[pyramidLevel], roi, pyramidLevel, cameraParameters, false);
        }
        this.projectorTransformer.transform(this.projectorImage[pyramidLevel], dstImage, roi, pyramidLevel, projectorParameters, false);
        if (this.surfaceTransformer != null) {
            opencv_core.cvMul(dstImage, p.tempImage[pyramidLevel], dstImage, 1.0d / dstImage.highValue());
            return;
        }
        opencv_core.cvCopy(p.tempImage[pyramidLevel], dstImage);
    }

    public void transform(CvMat srcPts, CvMat dstPts, org.bytedeco.javacv.ImageTransformer.Parameters parameters, boolean inverse) {
        if (this.surfaceTransformer != null) {
            this.surfaceTransformer.transform(srcPts, dstPts, ((Parameters) parameters).surfaceParameters, inverse);
        } else if (dstPts != srcPts) {
            dstPts.put(srcPts);
        }
    }

    public void transform(Data[] data, CvRect roi, org.bytedeco.javacv.ImageTransformer.Parameters[] parameters, boolean[] inverses) {
        if ($assertionsDisabled || data.length == parameters.length) {
            int i;
            if (this.kernelData == null || this.kernelData.capacity() < ((long) data.length)) {
                this.kernelData = new KernelData((long) data.length);
            }
            if ((this.H1 == null || this.H1.length < data.length) && this.surfaceTransformer != null) {
                this.H1 = new CvMat[data.length];
                for (i = 0; i < this.H1.length; i++) {
                    this.H1[i] = CvMat.create(3, 3);
                }
            }
            if (this.H2 == null || this.H2.length < data.length) {
                this.H2 = new CvMat[data.length];
                for (i = 0; i < this.H2.length; i++) {
                    this.H2[i] = CvMat.create(3, 3);
                }
            }
            if (this.f191X == null || this.f191X.length < data.length) {
                this.f191X = new CvMat[data.length];
                for (i = 0; i < this.f191X.length; i++) {
                    this.f191X[i] = CvMat.create(4, 4);
                }
            }
            i = 0;
            while (i < data.length) {
                this.kernelData.position((long) i);
                this.kernelData.srcImg(this.projectorImage[data[i].pyramidLevel]);
                this.kernelData.srcImg2(this.surfaceTransformer == null ? null : data[i].srcImg);
                this.kernelData.subImg(data[i].subImg);
                this.kernelData.srcDotImg(data[i].srcDotImg);
                this.kernelData.mask(data[i].mask);
                this.kernelData.zeroThreshold(data[i].zeroThreshold);
                this.kernelData.outlierThreshold(data[i].outlierThreshold);
                if (inverses == null || !inverses[i]) {
                    prepareTransforms(this.surfaceTransformer == null ? null : this.H1[i], this.H2[i], this.f191X[i], data[i].pyramidLevel, (Parameters) parameters[i]);
                    this.kernelData.H1(this.H2[i]);
                    this.kernelData.H2(this.surfaceTransformer == null ? null : this.H1[i]);
                    this.kernelData.X(this.f191X[i]);
                    this.kernelData.transImg(data[i].transImg);
                    this.kernelData.dstImg(data[i].dstImg);
                    this.kernelData.dstDstDot(data[i].dstDstDot);
                    i++;
                } else {
                    throw new UnsupportedOperationException("Inverse transform not supported.");
                }
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
