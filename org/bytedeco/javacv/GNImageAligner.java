package org.bytedeco.javacv;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core$IplROI;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.videoInputLib;
import org.bytedeco.javacv.ImageTransformer.Data;
import org.bytedeco.javacv.ImageTransformer.Parameters;
import org.bytedeco.javacv.Parallel.Looper;

public class GNImageAligner implements ImageAligner {
    protected double RMSE;
    protected double[] constraintGrad;
    protected CvMat dstRoiPts;
    protected CvPoint dstRoiPtsArray;
    protected CvMat gradient;
    protected CvMat hessian;
    protected Data[] hessianGradientTransformerData;
    protected IplImage[] images;
    protected int lastLinePosition;
    protected IplImage[] mask;
    protected final int f185n;
    protected Parameters parameters;
    protected Parameters[] parametersArray;
    protected CvMat prior;
    protected Parameters priorParameters;
    protected int pyramidLevel;
    protected IplImage[] residual;
    protected Data[] residualTransformerData;
    protected boolean residualUpdateNeeded;
    protected CvRect roi;
    protected Settings settings;
    protected CvMat srcRoiPts;
    protected boolean[] subspaceCorrelated;
    protected double[][] subspaceJacobian;
    protected double[] subspaceParameters;
    protected double[] subspaceResidual;
    protected IplImage[] target;
    protected Parameters[] tempParameters;
    protected double[][] tempSubspaceParameters;
    protected IplImage[] template;
    protected CvRect temproi;
    protected IplImage[] transformed;
    protected ImageTransformer transformer;
    protected int trials;
    protected CvMat update;
    protected double[] updateScale;

    public static class Settings extends org.bytedeco.javacv.ImageAligner.Settings implements Cloneable {
        double alphaSubspace = 0.1d;
        double alphaTikhonov = 0.0d;
        boolean constrained = false;
        double deltaMax = 300.0d;
        double deltaMin = 10.0d;
        double displacementMax = videoInputLib.VI_VERSION;
        CvMat gammaTgamma = null;
        double[] lineSearch = new double[]{1.0d, 0.25d};
        double stepSize = 0.1d;

        public Settings(Settings s) {
            super(s);
            this.stepSize = s.stepSize;
            this.lineSearch = s.lineSearch;
            this.deltaMin = s.deltaMin;
            this.deltaMax = s.deltaMax;
            this.displacementMax = s.displacementMax;
            this.alphaSubspace = s.alphaSubspace;
            this.alphaTikhonov = s.alphaTikhonov;
            this.gammaTgamma = s.gammaTgamma;
            this.constrained = s.constrained;
        }

        public double getStepSize() {
            return this.stepSize;
        }

        public void setStepSize(double stepSize) {
            this.stepSize = stepSize;
        }

        public double[] getLineSearch() {
            return this.lineSearch;
        }

        public void setLineSearch(double[] lineSearch) {
            this.lineSearch = lineSearch;
        }

        public double getDeltaMin() {
            return this.deltaMin;
        }

        public void setDeltaMin(double deltaMin) {
            this.deltaMin = deltaMin;
        }

        public double getDeltaMax() {
            return this.deltaMax;
        }

        public void setDeltaMax(double deltaMax) {
            this.deltaMax = deltaMax;
        }

        public double getDisplacementMax() {
            return this.displacementMax;
        }

        public void setDisplacementMax(double displacementMax) {
            this.displacementMax = displacementMax;
        }

        public double getAlphaSubspace() {
            return this.alphaSubspace;
        }

        public void setAlphaSubspace(double alphaSubspace) {
            this.alphaSubspace = alphaSubspace;
        }

        public double getAlphaTikhonov() {
            return this.alphaTikhonov;
        }

        public void setAlphaTikhonov(double alphaTikhonov) {
            this.alphaTikhonov = alphaTikhonov;
        }

        public CvMat getGammaTgamma() {
            return this.gammaTgamma;
        }

        public void setGammaTgamma(CvMat gammaTgamma) {
            this.gammaTgamma = gammaTgamma;
        }

        public Settings clone() {
            return new Settings(this);
        }
    }

    public GNImageAligner(ImageTransformer transformer, Parameters initialParameters, IplImage template0, double[] roiPts, IplImage target0) {
        this(transformer, initialParameters, template0, roiPts, target0, new Settings());
    }

    public GNImageAligner(ImageTransformer transformer, Parameters initialParameters, IplImage template0, double[] roiPts, IplImage target0, Settings settings) {
        int i;
        this(transformer, initialParameters);
        setSettings(settings);
        int minLevel = settings.pyramidLevelMin;
        int maxLevel = settings.pyramidLevelMax;
        this.template = new IplImage[(maxLevel + 1)];
        this.target = new IplImage[(maxLevel + 1)];
        this.transformed = new IplImage[(maxLevel + 1)];
        this.residual = new IplImage[(maxLevel + 1)];
        this.mask = new IplImage[(maxLevel + 1)];
        int w = template0 != null ? template0.width() : target0.width();
        int h = template0 != null ? template0.height() : target0.height();
        int c = template0 != null ? template0.nChannels() : target0.nChannels();
        int o = template0 != null ? template0.origin() : target0.origin();
        for (i = minLevel; i <= maxLevel; i++) {
            if (i == minLevel && template0 != null && template0.depth() == 32) {
                this.template[i] = template0;
            } else {
                this.template[i] = IplImage.create(w, h, 32, c, o);
            }
            if (i == minLevel && target0 != null && target0.depth() == 32) {
                this.target[i] = target0;
            } else {
                this.target[i] = IplImage.create(w, h, 32, c, o);
            }
            this.transformed[i] = IplImage.create(w, h, 32, c, o);
            this.residual[i] = IplImage.create(w, h, 32, c, o);
            this.mask[i] = IplImage.create(w, h, 8, 1, o);
            w /= 2;
            h /= 2;
        }
        this.hessianGradientTransformerData = new Data[this.f185n];
        for (i = 0; i < this.f185n; i++) {
            this.hessianGradientTransformerData[i] = new Data(this.template[this.pyramidLevel], this.transformed[this.pyramidLevel], this.residual[this.pyramidLevel], this.mask[this.pyramidLevel], 0.0d, 0.0d, this.pyramidLevel, null, null, this.f185n);
        }
        this.residualTransformerData = new Data[]{new Data(this.template[this.pyramidLevel], this.target[this.pyramidLevel], null, this.mask[this.pyramidLevel], 0.0d, 0.0d, this.pyramidLevel, this.transformed[this.pyramidLevel], this.residual[this.pyramidLevel], 1)};
        setConstrained(settings.constrained);
        setTemplateImage(template0, roiPts);
        setTargetImage(target0);
    }

    protected GNImageAligner(ImageTransformer transformer, Parameters initialParameters) {
        int i;
        this.images = new IplImage[5];
        this.residualUpdateNeeded = true;
        this.lastLinePosition = 0;
        this.trials = 0;
        this.f185n = initialParameters.size();
        this.srcRoiPts = CvMat.create(4, 1, 6, 2);
        this.dstRoiPts = CvMat.create(4, 1, 6, 2);
        this.dstRoiPtsArray = new CvPoint(4);
        this.roi = new CvRect();
        this.temproi = new CvRect();
        this.transformer = transformer;
        this.parameters = initialParameters.clone();
        this.parametersArray = new Parameters[]{this.parameters};
        this.tempParameters = new Parameters[this.f185n];
        for (i = 0; i < this.tempParameters.length; i++) {
            this.tempParameters[i] = initialParameters.clone();
        }
        this.subspaceParameters = this.parameters.getSubspace();
        if (this.subspaceParameters != null) {
            this.tempSubspaceParameters = new double[Parallel.getNumThreads()][];
            for (i = 0; i < this.tempSubspaceParameters.length; i++) {
                this.tempSubspaceParameters[i] = (double[]) this.subspaceParameters.clone();
            }
        }
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(org.bytedeco.javacv.ImageAligner.Settings settings) {
        this.settings = (Settings) settings;
    }

    public IplImage getTemplateImage() {
        return this.template[this.pyramidLevel];
    }

    public void setTemplateImage(IplImage template0, double[] roiPts) {
        int minLevel = this.settings.pyramidLevelMin;
        int maxLevel = this.settings.pyramidLevelMax;
        if (roiPts == null && template0 != null) {
            int w = template0.width() << minLevel;
            int h = template0.height() << minLevel;
            this.srcRoiPts.put(new double[]{0.0d, 0.0d, (double) w, 0.0d, (double) w, (double) h, 0.0d, (double) h});
        } else if (roiPts != null) {
            this.srcRoiPts.put(roiPts);
        }
        if (template0 != null) {
            if (template0.depth() == 32) {
                this.template[minLevel] = template0;
            } else {
                opencv_core.cvConvertScale(template0, this.template[minLevel], 1.0d / template0.highValue(), 0.0d);
            }
            for (int i = minLevel + 1; i <= maxLevel; i++) {
                opencv_imgproc.cvPyrDown(this.template[i - 1], this.template[i], 7);
            }
            setPyramidLevel(maxLevel);
        }
    }

    public IplImage getTargetImage() {
        return this.target[this.pyramidLevel];
    }

    public void setTargetImage(IplImage target0) {
        int minLevel = this.settings.pyramidLevelMin;
        int maxLevel = this.settings.pyramidLevelMax;
        if (target0 != null) {
            int i;
            if (target0.depth() == 32) {
                this.target[minLevel] = target0;
            }
            if (this.settings.displacementMax > 0.0d) {
                this.transformer.transform(this.srcRoiPts, this.dstRoiPts, this.parameters, false);
                double[] pts = this.dstRoiPts.get();
                for (i = 0; i < pts.length; i++) {
                    pts[i] = pts[i] / ((double) (1 << minLevel));
                }
                int width = this.target[minLevel].width();
                int height = this.target[minLevel].height();
                this.temproi.x(0).y(0).width(width).height(height);
                int align = 1 << (maxLevel + 1);
                JavaCV.boundingRect(pts, this.temproi, ((int) Math.round(this.settings.displacementMax * ((double) width))) + 3, ((int) Math.round(this.settings.displacementMax * ((double) height))) + 3, align, align);
                opencv_core.cvSetImageROI(target0, this.temproi);
                opencv_core.cvSetImageROI(this.target[minLevel], this.temproi);
            } else {
                opencv_core.cvResetImageROI(target0);
                opencv_core.cvResetImageROI(this.target[minLevel]);
            }
            if (target0.depth() != 32) {
                opencv_core.cvConvertScale(target0, this.target[minLevel], 1.0d / target0.highValue(), 0.0d);
                opencv_core.cvResetImageROI(target0);
            }
            for (i = minLevel + 1; i <= maxLevel; i++) {
                opencv_core$IplROI ir = this.target[i - 1].roi();
                if (ir != null) {
                    this.temproi.x(ir.xOffset() / 2);
                    this.temproi.width(ir.width() / 2);
                    this.temproi.y(ir.yOffset() / 2);
                    this.temproi.height(ir.height() / 2);
                    opencv_core.cvSetImageROI(this.target[i], this.temproi);
                } else {
                    opencv_core.cvResetImageROI(this.target[i]);
                }
                opencv_imgproc.cvPyrDown(this.target[i - 1], this.target[i], 7);
            }
            setPyramidLevel(maxLevel);
        }
    }

    public int getPyramidLevel() {
        return this.pyramidLevel;
    }

    public void setPyramidLevel(int pyramidLevel) {
        this.pyramidLevel = pyramidLevel;
        this.residualUpdateNeeded = true;
        this.trials = 0;
    }

    public boolean isConstrained() {
        return this.settings.constrained;
    }

    public void setConstrained(boolean constrained) {
        if (this.settings.constrained != constrained || this.hessian == null || this.gradient == null || this.update == null) {
            this.settings.constrained = constrained;
            int m = constrained ? this.f185n + 1 : this.f185n;
            if (!(this.subspaceParameters == null || this.settings.alphaSubspace == 0.0d)) {
                m += this.subspaceParameters.length;
            }
            this.hessian = CvMat.create(m, m);
            this.gradient = CvMat.create(m, 1);
            this.update = CvMat.create(m, 1);
            this.updateScale = new double[m];
            this.prior = CvMat.create(this.f185n, 1);
            this.constraintGrad = new double[this.f185n];
            this.subspaceResidual = new double[this.f185n];
            this.subspaceJacobian = (double[][]) Array.newInstance(Double.TYPE, new int[]{m, this.f185n});
            this.subspaceCorrelated = new boolean[this.f185n];
        }
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters.set(parameters);
        this.subspaceParameters = parameters.getSubspace();
        if (!(this.subspaceParameters == null || this.settings.alphaSubspace == 0.0d)) {
            for (int i = 0; i < this.tempSubspaceParameters.length; i++) {
                this.tempSubspaceParameters[i] = (double[]) this.subspaceParameters.clone();
            }
        }
        this.residualUpdateNeeded = true;
    }

    public Parameters getPriorParameters() {
        return this.priorParameters;
    }

    public void setPriorParameters(Parameters priorParameters) {
        this.priorParameters.set(priorParameters);
    }

    public double[] getTransformedRoiPts() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.dstRoiPts.get();
    }

    public IplImage getTransformedImage() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.transformed[this.pyramidLevel];
    }

    public IplImage getResidualImage() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.residual[this.pyramidLevel];
    }

    public IplImage getMaskImage() {
        return this.mask[this.pyramidLevel];
    }

    public double getRMSE() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.RMSE;
    }

    public int getPixelCount() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.residualTransformerData[0].dstCount;
    }

    public int getOutlierCount() {
        return this.hessianGradientTransformerData[0].dstCountOutlier;
    }

    public CvRect getRoi() {
        if (this.residualUpdateNeeded) {
            doRoi();
        }
        return this.roi;
    }

    public int getLastLinePosition() {
        return this.lastLinePosition;
    }

    public IplImage[] getImages() {
        this.images[0] = getTemplateImage();
        this.images[1] = getTargetImage();
        this.images[2] = getTransformedImage();
        this.images[3] = getResidualImage();
        this.images[4] = getMaskImage();
        return this.images;
    }

    public boolean iterate(double[] delta) {
        double[] resetSubspaceParameters;
        int i;
        boolean converged = false;
        double prevRMSE = getRMSE();
        double[] prevParameters = this.parameters.get();
        double[] prevSubspaceParameters = this.subspaceParameters == null ? null : (double[]) this.subspaceParameters.clone();
        if (this.trials == 0 && this.parameters.preoptimize()) {
            setParameters(this.parameters);
            doResidual();
        }
        double[] resetParameters = this.parameters.get();
        if (this.subspaceParameters == null) {
            resetSubspaceParameters = null;
        } else {
            resetSubspaceParameters = (double[]) this.subspaceParameters.clone();
        }
        doHessianGradient(this.updateScale);
        this.lastLinePosition = 0;
        opencv_core.cvSolve(this.hessian, this.gradient, this.update, 1);
        for (i = 0; i < this.f185n; i++) {
            this.parameters.set(i, this.parameters.get(i) + ((this.settings.lineSearch[0] * this.update.get(i)) * this.updateScale[i]));
        }
        for (i = this.f185n; i < this.update.length(); i++) {
            double[] dArr = this.subspaceParameters;
            int i2 = i - this.f185n;
            dArr[i2] = dArr[i2] + ((this.settings.lineSearch[0] * this.update.get(i)) * this.updateScale[i]);
        }
        this.residualUpdateNeeded = true;
        for (int j = 1; j < this.settings.lineSearch.length && getRMSE() > prevRMSE; j++) {
            this.RMSE = prevRMSE;
            this.parameters.set(resetParameters);
            if (this.subspaceParameters != null) {
                System.arraycopy(resetSubspaceParameters, 0, this.subspaceParameters, 0, this.subspaceParameters.length);
            }
            this.lastLinePosition = j;
            for (i = 0; i < this.f185n; i++) {
                this.parameters.set(i, this.parameters.get(i) + ((this.settings.lineSearch[j] * this.update.get(i)) * this.updateScale[i]));
            }
            for (i = this.f185n; i < this.update.length(); i++) {
                dArr = this.subspaceParameters;
                i2 = i - this.f185n;
                dArr[i2] = dArr[i2] + ((this.settings.lineSearch[j] * this.update.get(i)) * this.updateScale[i]);
            }
            this.residualUpdateNeeded = true;
        }
        double deltaNorm = 0.0d;
        if (delta != null) {
            i = 0;
            while (i < delta.length && i < this.updateScale.length) {
                delta[i] = (this.settings.lineSearch[this.lastLinePosition] * this.update.get(i)) * this.updateScale[i];
                i++;
            }
            deltaNorm = JavaCV.norm(Arrays.copyOf(delta, this.f185n));
        }
        boolean invalid = getRMSE() > prevRMSE || deltaNorm > this.settings.deltaMax || Double.isNaN(this.RMSE) || Double.isInfinite(this.RMSE);
        if (invalid) {
            this.RMSE = prevRMSE;
            this.parameters.set(prevParameters);
            if (this.subspaceParameters != null) {
                System.arraycopy(prevSubspaceParameters, 0, this.subspaceParameters, 0, this.subspaceParameters.length);
            }
            this.residualUpdateNeeded = true;
        }
        if (invalid && deltaNorm > this.settings.deltaMin) {
            int i3 = this.trials + 1;
            this.trials = i3;
            if (i3 < 2) {
                return false;
            }
        }
        if (invalid || deltaNorm < this.settings.deltaMin) {
            this.trials = 0;
            if (this.pyramidLevel > this.settings.pyramidLevelMin) {
                setPyramidLevel(this.pyramidLevel - 1);
            } else {
                converged = true;
            }
        } else {
            this.trials = 0;
        }
        return converged;
    }

    protected void doHessianGradient(double[] scale) {
        int i;
        final double constraintError = this.parameters.getConstraintError();
        final double stepSize = this.settings.stepSize;
        opencv_core.cvSetZero(this.gradient);
        opencv_core.cvSetZero(this.hessian);
        final double[] dArr = scale;
        Parallel.loop(0, this.f185n, new Looper() {
            public void loop(int from, int to, int looperID) {
                for (int i = from; i < to; i++) {
                    GNImageAligner.this.tempParameters[i].set(GNImageAligner.this.parameters);
                    GNImageAligner.this.tempParameters[i].set(i, GNImageAligner.this.tempParameters[i].get(i) + stepSize);
                    dArr[i] = GNImageAligner.this.tempParameters[i].get(i) - GNImageAligner.this.parameters.get(i);
                    GNImageAligner.this.constraintGrad[i] = GNImageAligner.this.tempParameters[i].getConstraintError() - constraintError;
                }
            }
        });
        for (i = 0; i < this.f185n; i++) {
            Data d = this.hessianGradientTransformerData[i];
            d.srcImg = this.template[this.pyramidLevel];
            d.subImg = this.transformed[this.pyramidLevel];
            d.srcDotImg = this.residual[this.pyramidLevel];
            d.transImg = null;
            d.dstImg = null;
            d.mask = this.mask[this.pyramidLevel];
            d.zeroThreshold = this.settings.thresholdsZero[Math.min(this.settings.thresholdsZero.length - 1, this.pyramidLevel)];
            d.outlierThreshold = this.settings.thresholdsOutlier[Math.min(this.settings.thresholdsOutlier.length - 1, this.pyramidLevel)];
            if (this.settings.thresholdsMulRMSE) {
                d.zeroThreshold *= this.RMSE;
                d.outlierThreshold *= this.RMSE;
            }
            d.pyramidLevel = this.pyramidLevel;
        }
        this.transformer.transform(this.hessianGradientTransformerData, this.roi, this.tempParameters, null);
        for (i = 0; i < this.f185n; i++) {
            d = this.hessianGradientTransformerData[i];
            this.gradient.put(i, this.gradient.get(i) - d.srcDstDot);
            for (int j = 0; j < this.f185n; j++) {
                this.hessian.put(i, j, this.hessian.get(i, j) + d.dstDstDot.get(j));
            }
        }
        doRegularization(this.updateScale);
    }

    protected void doRegularization(double[] scale) {
        int i;
        double constraintError = this.parameters.getConstraintError();
        final double stepSize = this.settings.stepSize;
        if (!((this.settings.gammaTgamma == null && this.settings.alphaTikhonov == 0.0d) || this.prior == null || this.priorParameters == null)) {
            for (i = 0; i < this.f185n; i++) {
                this.prior.put(i, this.parameters.get(i) - this.priorParameters.get(i));
            }
            opencv_core.cvMatMul(this.hessian, this.prior, this.prior);
            for (i = 0; i < this.f185n; i++) {
                this.gradient.put(i, this.gradient.get(i) + this.prior.get(i));
            }
        }
        if (this.settings.constrained) {
            double constraintGradSum = 0.0d;
            for (double d : this.constraintGrad) {
                constraintGradSum += d;
            }
            scale[this.f185n] = ((double) this.f185n) * constraintGradSum;
            for (i = 0; i < this.f185n; i++) {
                double c = this.constraintGrad[i] * scale[this.f185n];
                this.hessian.put(i, this.f185n, c);
                this.hessian.put(this.f185n, i, c);
            }
            this.gradient.put(this.f185n, (-constraintError) * scale[this.f185n]);
        }
        if (!(this.subspaceParameters == null || this.subspaceParameters.length <= 0 || this.settings.alphaSubspace == 0.0d)) {
            final int m = this.subspaceParameters.length;
            Arrays.fill(this.subspaceCorrelated, false);
            this.tempParameters[0].set(this.parameters);
            this.tempParameters[0].setSubspace(this.subspaceParameters);
            final double[] dArr = scale;
            Parallel.loop(0, this.f185n + m, this.tempSubspaceParameters.length, new Looper() {
                public void loop(int from, int to, int looperID) {
                    for (int i = from; i < to; i++) {
                        if (i < GNImageAligner.this.f185n) {
                            Arrays.fill(GNImageAligner.this.subspaceJacobian[i], 0.0d);
                            GNImageAligner.this.subspaceJacobian[i][i] = dArr[i];
                        } else {
                            System.arraycopy(GNImageAligner.this.subspaceParameters, 0, GNImageAligner.this.tempSubspaceParameters[looperID], 0, m);
                            double[] dArr = GNImageAligner.this.tempSubspaceParameters[looperID];
                            int i2 = i - GNImageAligner.this.f185n;
                            dArr[i2] = dArr[i2] + stepSize;
                            GNImageAligner.this.tempParameters[(i - GNImageAligner.this.f185n) + 1].set(GNImageAligner.this.parameters);
                            GNImageAligner.this.tempParameters[(i - GNImageAligner.this.f185n) + 1].setSubspace(GNImageAligner.this.tempSubspaceParameters[looperID]);
                            dArr[i] = GNImageAligner.this.tempSubspaceParameters[looperID][i - GNImageAligner.this.f185n] - GNImageAligner.this.subspaceParameters[i - GNImageAligner.this.f185n];
                            for (int j = 0; j < GNImageAligner.this.f185n; j++) {
                                int i3;
                                GNImageAligner.this.subspaceJacobian[i][j] = GNImageAligner.this.tempParameters[0].get(j) - GNImageAligner.this.tempParameters[(i - GNImageAligner.this.f185n) + 1].get(j);
                                boolean[] zArr = GNImageAligner.this.subspaceCorrelated;
                                boolean z = zArr[j];
                                if (GNImageAligner.this.subspaceJacobian[i][j] != 0.0d) {
                                    i3 = 1;
                                } else {
                                    i3 = 0;
                                }
                                zArr[j] = i3 | z;
                            }
                        }
                    }
                }
            });
            int subspaceCorrelatedCount = 0;
            for (i = 0; i < this.f185n; i++) {
                this.subspaceResidual[i] = this.parameters.get(i) - this.tempParameters[0].get(i);
                if (this.subspaceCorrelated[i]) {
                    subspaceCorrelatedCount++;
                }
            }
            final double K = (((this.settings.alphaSubspace * this.settings.alphaSubspace) * this.RMSE) * this.RMSE) / ((double) subspaceCorrelatedCount);
            Parallel.loop(0, this.f185n + m, new Looper() {
                public void loop(int from, int to, int looperID) {
                    int i = from;
                    while (i < to) {
                        if (i >= GNImageAligner.this.f185n || GNImageAligner.this.subspaceCorrelated[i]) {
                            int k;
                            int j = i;
                            while (j < GNImageAligner.this.f185n + m) {
                                if (j >= GNImageAligner.this.f185n || GNImageAligner.this.subspaceCorrelated[j]) {
                                    double h = 0.0d;
                                    for (k = 0; k < GNImageAligner.this.f185n; k++) {
                                        h += GNImageAligner.this.subspaceJacobian[i][k] * GNImageAligner.this.subspaceJacobian[j][k];
                                    }
                                    h = GNImageAligner.this.hessian.get(i, j) + (K * h);
                                    GNImageAligner.this.hessian.put(i, j, h);
                                    GNImageAligner.this.hessian.put(j, i, h);
                                }
                                j++;
                            }
                            double g = 0.0d;
                            for (k = 0; k < GNImageAligner.this.f185n; k++) {
                                g -= GNImageAligner.this.subspaceJacobian[i][k] * GNImageAligner.this.subspaceResidual[k];
                            }
                            GNImageAligner.this.gradient.put(i, GNImageAligner.this.gradient.get(i) + (K * g));
                        }
                        i++;
                    }
                }
            });
        }
        int rows = this.hessian.rows();
        int cols = this.hessian.cols();
        i = 0;
        while (i < rows) {
            int j = 0;
            while (j < cols) {
                double h = this.hessian.get(i, j);
                double g = 0.0d;
                if (this.settings.gammaTgamma != null && i < this.settings.gammaTgamma.rows() && j < this.settings.gammaTgamma.cols()) {
                    g = this.settings.gammaTgamma.get(i, j);
                }
                double a = 0.0d;
                if (i == j && i < this.f185n) {
                    a = this.settings.alphaTikhonov * this.settings.alphaTikhonov;
                }
                this.hessian.put(i, j, (h + g) + a);
                j++;
            }
            i++;
        }
    }

    protected void doRoi() {
        this.transformer.transform(this.srcRoiPts, this.dstRoiPts, this.parameters, false);
        double[] pts = this.dstRoiPts.get();
        for (int i = 0; i < pts.length; i++) {
            pts[i] = pts[i] / ((double) (1 << this.pyramidLevel));
        }
        this.roi.x(0).y(0).width(this.mask[this.pyramidLevel].width()).height(this.mask[this.pyramidLevel].height());
        JavaCV.boundingRect(pts, this.roi, 3, 3, 16, 1);
        opencv_core.cvSetZero(this.mask[this.pyramidLevel]);
        this.dstRoiPtsArray.put((byte) 16, pts);
        opencv_imgproc.cvFillConvexPoly(this.mask[this.pyramidLevel], this.dstRoiPtsArray, 4, CvScalar.WHITE, 8, 16);
    }

    protected void doResidual() {
        this.parameters.getConstraintError();
        Data d = this.residualTransformerData[0];
        d.srcImg = this.template[this.pyramidLevel];
        d.subImg = this.target[this.pyramidLevel];
        d.srcDotImg = null;
        d.transImg = this.transformed[this.pyramidLevel];
        d.dstImg = this.residual[this.pyramidLevel];
        d.mask = this.mask[this.pyramidLevel];
        d.zeroThreshold = this.settings.thresholdsZero[Math.min(this.settings.thresholdsZero.length - 1, this.pyramidLevel)];
        d.outlierThreshold = this.settings.thresholdsOutlier[Math.min(this.settings.thresholdsOutlier.length - 1, this.pyramidLevel)];
        if (this.settings.thresholdsMulRMSE) {
            d.zeroThreshold *= this.RMSE;
            d.outlierThreshold *= this.RMSE;
        }
        d.pyramidLevel = this.pyramidLevel;
        this.transformer.transform(this.residualTransformerData, this.roi, this.parametersArray, null);
        double dstDstDot = this.residualTransformerData[0].dstDstDot.get(0);
        int dstCount = this.residualTransformerData[0].dstCount;
        this.RMSE = dstCount < this.f185n ? Double.NaN : Math.sqrt(dstDstDot / ((double) dstCount));
        this.residualUpdateNeeded = false;
    }
}
