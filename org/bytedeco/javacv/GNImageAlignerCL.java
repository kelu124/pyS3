package org.bytedeco.javacv;

import com.itextpdf.text.pdf.BaseField;
import com.jogamp.opencl.CLImage2d;
import com.jogamp.opencl.CLImageFormat;
import com.jogamp.opencl.CLImageFormat.ChannelOrder;
import com.jogamp.opencl.CLImageFormat.ChannelType;
import com.jogamp.opencl.CLMemory.Mem;
import com.jogamp.opencl.gl.CLGLContext;
import com.jogamp.opencl.gl.CLGLImage2d;
import com.jogamp.opengl.GL2;
import java.util.Arrays;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.GNImageAligner.Settings;
import org.bytedeco.javacv.ImageTransformer.Parameters;
import org.bytedeco.javacv.ImageTransformerCL.InputData;
import org.bytedeco.javacv.ImageTransformerCL.OutputData;
import org.bytedeco.javacv.Parallel.Looper;

public class GNImageAlignerCL extends GNImageAligner implements ImageAlignerCL {
    static final /* synthetic */ boolean $assertionsDisabled = (!GNImageAlignerCL.class.desiredAssertionStatus());
    private final JavaCVCL context;
    private CLImage2d[] imagesCL;
    private InputData inputData;
    private CLGLImage2d[] maskCL;
    private int[] maskfb;
    private int[] maskrb;
    private OutputData outputData;
    private CLImage2d[] residualCL;
    private CLImage2d[] targetCL;
    private CLImage2d[] templateCL;
    private boolean[] templateChanged;
    private CLImage2d[] transformedCL;

    public GNImageAlignerCL(ImageTransformerCL transformer, Parameters initialParameters, CLImage2d template0, double[] roiPts, CLImage2d target0) {
        this(transformer, initialParameters, template0, roiPts, target0, new Settings());
    }

    public GNImageAlignerCL(ImageTransformerCL transformer, Parameters initialParameters, CLImage2d template0, double[] roiPts, CLImage2d target0, Settings settings) {
        super(transformer, initialParameters);
        this.imagesCL = new CLImage2d[5];
        setSettings(settings);
        this.context = transformer.getContext();
        int minLevel = settings.pyramidLevelMin;
        int maxLevel = settings.pyramidLevelMax;
        this.template = new IplImage[(maxLevel + 1)];
        this.target = new IplImage[(maxLevel + 1)];
        this.transformed = new IplImage[(maxLevel + 1)];
        this.residual = new IplImage[(maxLevel + 1)];
        this.mask = new IplImage[(maxLevel + 1)];
        this.templateCL = new CLImage2d[(maxLevel + 1)];
        this.targetCL = new CLImage2d[(maxLevel + 1)];
        this.transformedCL = new CLImage2d[(maxLevel + 1)];
        this.residualCL = new CLImage2d[(maxLevel + 1)];
        this.maskCL = new CLGLImage2d[(maxLevel + 1)];
        this.maskrb = new int[(maxLevel + 1)];
        this.maskfb = new int[(maxLevel + 1)];
        int w = template0 != null ? template0.width : target0.width;
        int h = template0 != null ? template0.height : target0.height;
        CLGLContext c = this.context.getCLGLContext();
        GL2 gl = this.context.getGL2();
        gl.glGenRenderbuffers(maxLevel + 1, this.maskrb, 0);
        gl.glGenFramebuffers(maxLevel + 1, this.maskfb, 0);
        CLImageFormat f = new CLImageFormat(ChannelOrder.RGBA, ChannelType.FLOAT);
        int i = minLevel;
        while (i <= maxLevel) {
            CLImage2d[] cLImage2dArr = this.templateCL;
            CLImage2d createImage2d = (i != minLevel || template0 == null) ? c.createImage2d(w, h, f, new Mem[0]) : template0;
            cLImage2dArr[i] = createImage2d;
            cLImage2dArr = this.targetCL;
            createImage2d = (i != minLevel || target0 == null) ? c.createImage2d(w, h, f, new Mem[0]) : target0;
            cLImage2dArr[i] = createImage2d;
            this.transformedCL[i] = c.createImage2d(w, h, f, new Mem[0]);
            this.residualCL[i] = c.createImage2d(w, h, f, new Mem[0]);
            gl.glBindRenderbuffer(36161, this.maskrb[i]);
            gl.glBindFramebuffer(36160, this.maskfb[i]);
            gl.glRenderbufferStorage(36161, 32832, w, h);
            gl.glFramebufferRenderbuffer(36160, 36064, 36161, this.maskrb[i]);
            if ($assertionsDisabled || gl.glCheckFramebufferStatus(36160) == 36053) {
                this.maskCL[i] = c.createFromGLRenderbuffer(this.maskrb[i], new Mem[0]);
                System.out.println(this.maskCL[i] + " " + this.maskCL[i].getElementSize() + " " + this.maskCL[i].getFormat());
                w /= 2;
                h /= 2;
                i++;
            } else {
                throw new AssertionError();
            }
        }
        this.inputData = new InputData();
        this.outputData = new OutputData(false);
        this.templateChanged = new boolean[(maxLevel + 1)];
        Arrays.fill(this.templateChanged, true);
        setConstrained(settings.constrained);
        setTemplateImageCL(template0, roiPts);
        setTargetImageCL(target0);
    }

    public void release() {
        int minLevel = this.settings.pyramidLevelMin;
        int maxLevel = this.settings.pyramidLevelMax;
        if (!(this.templateCL == null || this.targetCL == null || this.transformedCL == null || this.residualCL == null || this.maskCL == null)) {
            for (int i = minLevel; i <= maxLevel; i++) {
                if (i > minLevel) {
                    this.templateCL[i].release();
                }
                if (i > minLevel) {
                    this.targetCL[i].release();
                }
                this.transformedCL[i].release();
                this.residualCL[i].release();
                this.maskCL[i].release();
            }
            this.maskCL = null;
            this.residualCL = null;
            this.transformedCL = null;
            this.targetCL = null;
            this.templateCL = null;
        }
        this.context.getGLContext().makeCurrent();
        GL2 gl = this.context.getGL2();
        if (this.maskfb != null) {
            gl.glDeleteFramebuffers(maxLevel + 1, this.maskfb, 0);
            this.maskfb = null;
        }
        if (this.maskrb != null) {
            gl.glDeleteRenderbuffers(maxLevel + 1, this.maskrb, 0);
            this.maskrb = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public IplImage getTemplateImage() {
        return getTemplateImage(true);
    }

    public IplImage getTemplateImage(boolean blocking) {
        if (!this.templateChanged[this.pyramidLevel]) {
            return this.template[this.pyramidLevel];
        }
        this.templateChanged[this.pyramidLevel] = false;
        IplImage[] iplImageArr = this.template;
        int i = this.pyramidLevel;
        IplImage readImage = this.context.readImage(getTemplateImageCL(), this.template[this.pyramidLevel], blocking);
        iplImageArr[i] = readImage;
        return readImage;
    }

    public void setTemplateImage(IplImage template0, double[] roiPts) {
        this.context.writeImage(this.templateCL[this.settings.pyramidLevelMin], template0, false);
        setTemplateImageCL(this.templateCL[this.settings.pyramidLevelMin], roiPts);
    }

    public IplImage getTargetImage() {
        return getTargetImage(true);
    }

    public IplImage getTargetImage(boolean blocking) {
        IplImage[] iplImageArr = this.target;
        int i = this.pyramidLevel;
        IplImage readImage = this.context.readImage(getTargetImageCL(), this.target[this.pyramidLevel], blocking);
        iplImageArr[i] = readImage;
        return readImage;
    }

    public void setTargetImage(IplImage target0) {
        this.context.writeImage(this.targetCL[this.settings.pyramidLevelMin], target0, false);
        setTargetImageCL(this.targetCL[this.settings.pyramidLevelMin]);
    }

    public IplImage getTransformedImage() {
        return getTransformedImage(true);
    }

    public IplImage getTransformedImage(boolean blocking) {
        IplImage[] iplImageArr = this.transformed;
        int i = this.pyramidLevel;
        IplImage readImage = this.context.readImage(getTransformedImageCL(), this.transformed[this.pyramidLevel], blocking);
        iplImageArr[i] = readImage;
        return readImage;
    }

    public IplImage getResidualImage() {
        return getResidualImage(true);
    }

    public IplImage getResidualImage(boolean blocking) {
        IplImage[] iplImageArr = this.residual;
        int i = this.pyramidLevel;
        IplImage readImage = this.context.readImage(getResidualImageCL(), this.residual[this.pyramidLevel], blocking);
        iplImageArr[i] = readImage;
        return readImage;
    }

    public IplImage getMaskImage() {
        return getMaskImage(true);
    }

    public IplImage getMaskImage(boolean blocking) {
        this.context.acquireGLObject(this.maskCL[this.pyramidLevel]);
        this.mask[this.pyramidLevel] = this.context.readImage(getMaskImageCL(), this.mask[this.pyramidLevel], blocking);
        this.context.releaseGLObject(this.maskCL[this.pyramidLevel]);
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
        return this.outputData.dstCount;
    }

    public int getOutlierCount() {
        return this.outputData.dstCountOutlier;
    }

    public CvRect getRoi() {
        if (this.residualUpdateNeeded) {
            doRoi();
        }
        return this.roi.x(this.inputData.roiX).y(this.inputData.roiY).width(this.inputData.roiWidth).height(this.inputData.roiHeight);
    }

    public IplImage[] getImages() {
        return getImages(true);
    }

    public IplImage[] getImages(boolean blocking) {
        this.images[0] = getTemplateImage(false);
        this.images[1] = getTargetImage(false);
        this.images[2] = getTransformedImage(false);
        this.images[3] = getResidualImage(false);
        this.images[4] = getMaskImage(blocking);
        return this.images;
    }

    public CLImage2d getTemplateImageCL() {
        return this.templateCL[this.pyramidLevel];
    }

    public void setTemplateImageCL(CLImage2d template0, double[] roiPts) {
        int minLevel = this.settings.pyramidLevelMin;
        int maxLevel = this.settings.pyramidLevelMax;
        if (roiPts != null || template0 == null) {
            this.srcRoiPts.put(roiPts);
        } else {
            int w = template0.width << minLevel;
            int h = template0.height << minLevel;
            this.srcRoiPts.put(new double[]{0.0d, 0.0d, (double) w, 0.0d, (double) w, (double) h, 0.0d, (double) h});
        }
        if (template0 != null) {
            this.templateCL[minLevel] = template0;
            for (int i = minLevel + 1; i <= maxLevel; i++) {
                this.context.pyrDown(this.templateCL[i - 1], this.templateCL[i]);
            }
            setPyramidLevel(maxLevel);
            Arrays.fill(this.templateChanged, true);
        }
    }

    public CLImage2d getTargetImageCL() {
        return this.targetCL[this.pyramidLevel];
    }

    public void setTargetImageCL(CLImage2d target0) {
        int minLevel = this.settings.pyramidLevelMin;
        int maxLevel = this.settings.pyramidLevelMax;
        this.targetCL[minLevel] = target0;
        for (int i = minLevel + 1; i <= maxLevel; i++) {
            this.context.pyrDown(this.targetCL[i - 1], this.targetCL[i]);
        }
        setPyramidLevel(maxLevel);
    }

    public CLImage2d getTransformedImageCL() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.transformedCL[this.pyramidLevel];
    }

    public CLImage2d getResidualImageCL() {
        if (this.residualUpdateNeeded) {
            doRoi();
            doResidual();
        }
        return this.residualCL[this.pyramidLevel];
    }

    public CLImage2d getMaskImageCL() {
        return this.maskCL[this.pyramidLevel];
    }

    public CLImage2d[] getImagesCL() {
        this.imagesCL[0] = this.templateCL[this.pyramidLevel];
        this.imagesCL[1] = this.targetCL[this.pyramidLevel];
        this.imagesCL[2] = this.transformedCL[this.pyramidLevel];
        this.imagesCL[3] = this.residualCL[this.pyramidLevel];
        this.imagesCL[4] = this.maskCL[this.pyramidLevel];
        return this.imagesCL;
    }

    protected void doHessianGradient(double[] scale) {
        final double constraintError = this.parameters.getConstraintError();
        final double stepSize = this.settings.stepSize;
        opencv_core.cvSetZero(this.gradient);
        opencv_core.cvSetZero(this.hessian);
        final double[] dArr = scale;
        Parallel.loop(0, this.n, new Looper() {
            public void loop(int from, int to, int looperID) {
                for (int i = from; i < to; i++) {
                    GNImageAlignerCL.this.tempParameters[i].set(GNImageAlignerCL.this.parameters);
                    GNImageAlignerCL.this.tempParameters[i].set(i, GNImageAlignerCL.this.tempParameters[i].get(i) + stepSize);
                    dArr[i] = GNImageAlignerCL.this.tempParameters[i].get(i) - GNImageAlignerCL.this.parameters.get(i);
                    GNImageAlignerCL.this.constraintGrad[i] = GNImageAlignerCL.this.tempParameters[i].getConstraintError() - constraintError;
                }
            }
        });
        this.inputData.zeroThreshold = this.settings.thresholdsZero[Math.min(this.settings.thresholdsZero.length - 1, this.pyramidLevel)];
        this.inputData.outlierThreshold = this.settings.thresholdsOutlier[Math.min(this.settings.thresholdsOutlier.length - 1, this.pyramidLevel)];
        if (this.settings.thresholdsMulRMSE) {
            InputData inputData = this.inputData;
            inputData.zeroThreshold *= this.RMSE;
            inputData = this.inputData;
            inputData.outlierThreshold *= this.RMSE;
        }
        this.inputData.pyramidLevel = this.pyramidLevel;
        this.context.acquireGLObject(this.maskCL[this.pyramidLevel]);
        ((ImageTransformerCL) this.transformer).transform(this.templateCL[this.pyramidLevel], this.transformedCL[this.pyramidLevel], this.residualCL[this.pyramidLevel], null, null, this.maskCL[this.pyramidLevel], this.tempParameters, null, this.inputData, this.outputData);
        this.context.releaseGLObject(this.maskCL[this.pyramidLevel]);
        doRegularization(this.updateScale);
        this.outputData.readBuffer(this.context);
        for (int i = 0; i < this.n; i++) {
            this.gradient.put(i, this.gradient.get(i) - ((double) this.outputData.srcDstDot.get(i)));
            for (int j = 0; j < this.n; j++) {
                this.hessian.put(i, j, this.hessian.get(i, j) + ((double) this.outputData.dstDstDot.get((this.n * i) + j)));
            }
        }
    }

    protected void doRoi() {
        this.transformer.transform(this.srcRoiPts, this.dstRoiPts, this.parameters, false);
        double[] pts = this.dstRoiPts.get();
        for (int i = 0; i < pts.length; i++) {
            pts[i] = pts[i] / ((double) (1 << this.pyramidLevel));
        }
        this.roi.x(0).y(0).width(this.maskCL[this.pyramidLevel].width).height(this.maskCL[this.pyramidLevel].height);
        JavaCV.boundingRect(pts, this.roi, 3, 3, 16, 1);
        this.inputData.roiX = this.roi.x();
        this.inputData.roiY = this.roi.y();
        this.inputData.roiWidth = this.roi.width();
        this.inputData.roiHeight = this.roi.height();
        GL2 gl = this.context.getGL2();
        gl.glBindFramebuffer(36160, this.maskfb[this.pyramidLevel]);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        this.context.getGLU().gluOrtho2D(0.0f, (float) this.maskCL[this.pyramidLevel].width, 0.0f, (float) this.maskCL[this.pyramidLevel].height);
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
        gl.glViewport(0, 0, this.maskCL[this.pyramidLevel].width, this.maskCL[this.pyramidLevel].height);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(16384);
        gl.glColor4f(BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN);
        gl.glBegin(9);
        gl.glVertex2d(pts[0], pts[1]);
        gl.glVertex2d(pts[2] + 1.0d, pts[3]);
        gl.glVertex2d(pts[4] + 1.0d, pts[5] + 1.0d);
        gl.glVertex2d(pts[6], pts[7] + 1.0d);
        gl.glEnd();
    }

    protected void doResidual() {
        this.parameters.getConstraintError();
        this.inputData.zeroThreshold = this.settings.thresholdsZero[Math.min(this.settings.thresholdsZero.length - 1, this.pyramidLevel)];
        this.inputData.outlierThreshold = this.settings.thresholdsOutlier[Math.min(this.settings.thresholdsOutlier.length - 1, this.pyramidLevel)];
        if (this.settings.thresholdsMulRMSE) {
            InputData inputData = this.inputData;
            inputData.zeroThreshold *= this.RMSE;
            inputData = this.inputData;
            inputData.outlierThreshold *= this.RMSE;
        }
        this.inputData.pyramidLevel = this.pyramidLevel;
        this.context.acquireGLObject(this.maskCL[this.pyramidLevel]);
        ((ImageTransformerCL) this.transformer).transform(this.templateCL[this.pyramidLevel], this.targetCL[this.pyramidLevel], null, this.transformedCL[this.pyramidLevel], this.residualCL[this.pyramidLevel], this.maskCL[this.pyramidLevel], this.parametersArray, null, this.inputData, this.outputData);
        this.context.releaseGLObject(this.maskCL[this.pyramidLevel]);
        this.outputData.readBuffer(this.context);
        double dstDstDot = (double) this.outputData.dstDstDot.get(0);
        int dstCount = this.outputData.dstCount;
        this.RMSE = dstCount < this.n ? Double.NaN : Math.sqrt(dstDstDot / ((double) dstCount));
        this.residualUpdateNeeded = false;
    }
}
