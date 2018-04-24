package org.bytedeco.javacv;

import com.jogamp.common.os.Platform;
import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLEventList;
import com.jogamp.opencl.CLImage2d;
import com.jogamp.opencl.CLImageFormat;
import com.jogamp.opencl.CLImageFormat.ChannelOrder;
import com.jogamp.opencl.CLImageFormat.ChannelType;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLMemory.Mem;
import java.nio.FloatBuffer;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacv.ImageTransformer.Parameters;
import org.bytedeco.javacv.ImageTransformerCL.InputData;
import org.bytedeco.javacv.ImageTransformerCL.OutputData;

public class ProCamTransformerCL extends ProCamTransformer implements ImageTransformerCL {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final ThreadLocal<CvMat> H13x3 = CvMat.createThreadLocal(3, 3);
    private static final ThreadLocal<CvMat> H23x3 = CvMat.createThreadLocal(3, 3);
    private static final ThreadLocal<CvMat> X4x4 = CvMat.createThreadLocal(4, 4);
    protected final CLBuffer<FloatBuffer> H1Buffer;
    protected final CLBuffer<FloatBuffer> H2Buffer;
    protected final CLBuffer<FloatBuffer> XBuffer;
    protected final JavaCVCL context;
    private CLKernel dotKernel;
    protected final int nullSize;
    private CLKernel oneKernel;
    protected CLImage2d[] projectorImageCL;
    private CLKernel reduceKernel;
    private CLKernel subKernel;
    protected CLImage2d[] surfaceImageCL;

    static {
        boolean z;
        if (ProCamTransformerCL.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProCamTransformerCL(JavaCVCL context, double[] referencePoints, CameraDevice camera, ProjectorDevice projector) {
        this(context, referencePoints, camera, projector, null);
    }

    public ProCamTransformerCL(JavaCVCL context, double[] referencePoints, CameraDevice camera, ProjectorDevice projector, CvMat n) {
        CLBuffer cLBuffer;
        super(referencePoints, camera, projector, n);
        this.projectorImageCL = null;
        this.surfaceImageCL = null;
        int dotSize = createParameters().size();
        this.context = context;
        this.nullSize = Platform.is32Bit() ? 4 : 8;
        if (this.surfaceTransformer == null) {
            cLBuffer = null;
        } else {
            cLBuffer = context.getCLContext().createFloatBuffer(dotSize * 9, new Mem[]{Mem.READ_ONLY});
        }
        this.H1Buffer = cLBuffer;
        this.H2Buffer = context.getCLContext().createFloatBuffer(dotSize * 9, new Mem[]{Mem.READ_ONLY});
        this.XBuffer = context.getCLContext().createFloatBuffer(dotSize * 16, new Mem[]{Mem.READ_ONLY});
        if (getClass() == ProCamTransformerCL.class) {
            CLKernel[] kernels = context.buildKernels("-cl-fast-relaxed-math -cl-mad-enable -cl-nv-maxrregcount=32 -DDOT_SIZE=" + dotSize, "ImageTransformer.cl:ProCamTransformer.cl", "transformOne", "transformSub", "transformDot", "reduceOutputData");
            this.oneKernel = kernels[0];
            this.subKernel = kernels[1];
            this.dotKernel = kernels[2];
            this.reduceKernel = kernels[3];
        }
    }

    public JavaCVCL getContext() {
        return this.context;
    }

    public ProjectiveColorTransformerCL getSurfaceTransformerCL() {
        return (ProjectiveColorTransformerCL) this.surfaceTransformer;
    }

    public ProjectiveColorTransformerCL getProjectorTransformerCL() {
        return (ProjectiveColorTransformerCL) this.projectorTransformer;
    }

    public CLImage2d getProjectorImageCL(int pyramidLevel) {
        return this.projectorImageCL[pyramidLevel];
    }

    public void setProjectorImageCL(CLImage2d projectorImage0, int minPyramidLevel, int maxPyramidLevel) {
        if (this.projectorImageCL == null || this.projectorImageCL.length != maxPyramidLevel + 1) {
            this.projectorImageCL = new CLImage2d[(maxPyramidLevel + 1)];
        }
        this.projectorImageCL[minPyramidLevel] = projectorImage0;
        for (int i = minPyramidLevel + 1; i <= maxPyramidLevel; i++) {
            if (this.projectorImageCL[i] == null) {
                this.projectorImageCL[i] = this.context.getCLContext().createImage2d(this.projectorImageCL[i - 1].width / 2, this.projectorImageCL[i - 1].height / 2, new CLImageFormat(ChannelOrder.RGBA, ChannelType.FLOAT), new Mem[0]);
            }
            this.context.pyrDown(this.projectorImageCL[i - 1], this.projectorImageCL[i]);
        }
    }

    public CLImage2d getSurfaceImageCL(int pyramidLevel) {
        return this.surfaceImageCL[pyramidLevel];
    }

    public void setSurfaceImageCL(CLImage2d surfaceImage0, int pyramidLevels) {
        if (this.surfaceImageCL == null || this.surfaceImageCL.length != pyramidLevels) {
            this.surfaceImageCL = new CLImage2d[pyramidLevels];
        }
        this.surfaceImageCL[0] = surfaceImage0;
        for (int i = 1; i < pyramidLevels; i++) {
            if (this.surfaceImageCL[i] == null) {
                this.surfaceImageCL[i] = this.context.getCLContext().createImage2d(this.surfaceImageCL[i - 1].width / 2, this.surfaceImageCL[i - 1].height / 2, new CLImageFormat(ChannelOrder.RGBA, ChannelType.FLOAT), new Mem[0]);
            }
            this.context.pyrDown(this.surfaceImageCL[i - 1], this.surfaceImageCL[i]);
        }
    }

    protected void prepareTransforms(CLBuffer H1Buffer, CLBuffer H2Buffer, CLBuffer XBuffer, int pyramidLevel, Parameters[] parameters) {
        FloatBuffer floatH1 = this.surfaceTransformer == null ? null : (FloatBuffer) H1Buffer.getBuffer().rewind();
        FloatBuffer floatH2 = (FloatBuffer) H2Buffer.getBuffer().rewind();
        FloatBuffer floatX = (FloatBuffer) XBuffer.getBuffer().rewind();
        CvMat H1 = (CvMat) H13x3.get();
        CvMat H2 = (CvMat) H23x3.get();
        CvMat X = (CvMat) X4x4.get();
        for (Parameters parameters2 : parameters) {
            CvMat cvMat;
            int j;
            if (this.surfaceTransformer == null) {
                cvMat = null;
            } else {
                cvMat = H1;
            }
            prepareTransforms(cvMat, H2, X, pyramidLevel, (ProCamTransformer.Parameters) parameters2);
            for (j = 0; j < 9; j++) {
                if (this.surfaceTransformer != null) {
                    floatH1.put((float) H1.get(j));
                }
                floatH2.put((float) H2.get(j));
            }
            for (j = 0; j < 16; j++) {
                floatX.put((float) X.get(j));
            }
        }
        if (this.surfaceTransformer != null) {
            floatH1.rewind();
        }
        floatH2.rewind();
        floatX.rewind();
    }

    public void transform(CLImage2d srcImg, CLImage2d subImg, CLImage2d srcDotImg, CLImage2d transImg, CLImage2d dstImg, CLImage2d maskImg, Parameters[] parameters, boolean[] inverses, InputData inputData, OutputData outputData) {
        CLKernel kernel;
        if (inverses != null) {
            for (boolean z : inverses) {
                if (z) {
                    throw new UnsupportedOperationException("Inverse transform not supported.");
                }
            }
        }
        prepareTransforms(this.H1Buffer, this.H2Buffer, this.XBuffer, inputData.pyramidLevel, parameters);
        int dotSize = parameters[0].size();
        int localSize = parameters.length > 1 ? parameters.length : inputData.roiWidth > 32 ? 64 : 32;
        int globalSize = JavaCVCL.alignCeil(inputData.roiWidth, localSize);
        int reduceSize = globalSize / localSize;
        CLBuffer inputBuffer = inputData.getBuffer(this.context);
        CLBuffer outputBuffer = outputData.getBuffer(this.context, dotSize, reduceSize);
        CLEventList cLEventList = new CLEventList(1);
        if (this.surfaceTransformer != null) {
            this.context.writeBuffer(this.H1Buffer, false);
        }
        this.context.writeBuffer(this.H2Buffer, false);
        this.context.writeBuffer(this.XBuffer, false);
        if (inputData.autoWrite) {
            inputData.writeBuffer(this.context);
        }
        CLImage2d srcImg2 = this.projectorImageCL[inputData.pyramidLevel];
        if (subImg == null) {
            if ($assertionsDisabled || parameters.length == 1) {
                CLKernel putArg = this.oneKernel.putArg(srcImg2).putArg(srcImg);
                if (dstImg != null) {
                    transImg = dstImg;
                }
                kernel = putArg.putArg(transImg).putArg(maskImg).putArg(this.H2Buffer);
            } else {
                throw new AssertionError();
            }
        } else if (srcDotImg == null) {
            if ($assertionsDisabled || parameters.length == 1) {
                kernel = this.subKernel.putArg(srcImg2).putArg(srcImg).putArg(subImg).putArg(transImg).putArg(dstImg).putArg(maskImg).putArg(this.H2Buffer);
            } else {
                throw new AssertionError();
            }
        } else if ($assertionsDisabled || parameters.length == dotSize) {
            kernel = this.dotKernel.putArg(srcImg2).putArg(srcImg).putArg(subImg).putArg(srcDotImg).putArg(maskImg).putArg(this.H2Buffer);
        } else {
            throw new AssertionError();
        }
        if (this.H1Buffer != null) {
            kernel.putArg(this.H1Buffer);
        } else {
            kernel.putNullArg(this.nullSize);
        }
        kernel.putArg(this.XBuffer).putArg(inputBuffer).putArg(outputBuffer).rewind();
        this.context.executeKernel(kernel, (long) inputData.roiX, 0, 0, (long) globalSize, 1, (long) parameters.length, (long) localSize, 1, (long) parameters.length, cLEventList);
        if (reduceSize > 1) {
            this.reduceKernel.putArg(outputBuffer).rewind();
            this.context.executeKernel(this.reduceKernel, 0, (long) reduceSize, (long) reduceSize);
        }
        if (outputData.autoRead) {
            outputData.readBuffer(this.context);
        }
    }
}
