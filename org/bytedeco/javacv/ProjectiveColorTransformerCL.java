package org.bytedeco.javacv;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLEventList;
import com.jogamp.opencl.CLImage2d;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLMemory.Mem;
import java.nio.FloatBuffer;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacv.ImageTransformer.Parameters;
import org.bytedeco.javacv.ImageTransformerCL.InputData;
import org.bytedeco.javacv.ImageTransformerCL.OutputData;

public class ProjectiveColorTransformerCL extends ProjectiveColorTransformer implements ImageTransformerCL {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProjectiveColorTransformerCL.class.desiredAssertionStatus());
    protected final CLBuffer<FloatBuffer> HBuffer;
    protected final CLBuffer<FloatBuffer> XBuffer;
    protected final JavaCVCL context;
    private CLKernel dotKernel;
    private CLKernel oneKernel;
    private CLKernel reduceKernel;
    private CLKernel subKernel;

    public ProjectiveColorTransformerCL(JavaCVCL context, CvMat K1, CvMat K2, CvMat R, CvMat t, CvMat n, double[] referencePoints1, double[] referencePoints2, CvMat X, int numGains, int numBiases) {
        super(K1, K2, R, t, n, referencePoints1, referencePoints2, X, numGains, numBiases);
        int dotSize = createParameters().size();
        this.context = context;
        this.HBuffer = context.getCLContext().createFloatBuffer(dotSize * 9, new Mem[]{Mem.READ_ONLY});
        this.XBuffer = context.getCLContext().createFloatBuffer(dotSize * 16, new Mem[]{Mem.READ_ONLY});
        if (getClass() == ProjectiveColorTransformerCL.class) {
            CLKernel[] kernels = context.buildKernels("-cl-fast-relaxed-math -cl-mad-enable -DDOT_SIZE=" + dotSize, "ImageTransformer.cl:ProjectiveColorTransformer.cl", "transformOne", "transformSub", "transformDot", "reduceOutputData");
            this.oneKernel = kernels[0];
            this.subKernel = kernels[1];
            this.dotKernel = kernels[2];
            this.reduceKernel = kernels[3];
        }
    }

    public JavaCVCL getContext() {
        return this.context;
    }

    protected void prepareHomographies(CLBuffer HBuffer, int pyramidLevel, Parameters[] parameters, boolean[] inverses) {
        FloatBuffer floatH = (FloatBuffer) HBuffer.getBuffer().rewind();
        CvMat H = (CvMat) H3x3.get();
        int i = 0;
        while (i < parameters.length) {
            prepareHomography(H, pyramidLevel, (ProjectiveColorTransformer.Parameters) parameters[i], inverses == null ? false : inverses[i]);
            for (int j = 0; j < 9; j++) {
                floatH.put((float) H.get(j));
            }
            i++;
        }
        floatH.rewind();
    }

    protected void prepareColorTransforms(CLBuffer XBuffer, int pyramidLevel, Parameters[] parameters, boolean[] inverses) {
        FloatBuffer floatX = (FloatBuffer) XBuffer.getBuffer().rewind();
        CvMat X2 = (CvMat) X24x4.get();
        int i = 0;
        while (i < parameters.length) {
            prepareColorTransform(X2, pyramidLevel, (ProjectiveColorTransformer.Parameters) parameters[i], inverses == null ? false : inverses[i]);
            for (int j = 0; j < 16; j++) {
                floatX.put((float) X2.get(j));
            }
            i++;
        }
        floatX.rewind();
    }

    public void transform(CLImage2d srcImg, CLImage2d subImg, CLImage2d srcDotImg, CLImage2d transImg, CLImage2d dstImg, CLImage2d maskImg, Parameters[] parameters, boolean[] inverses, InputData inputData, OutputData outputData) {
        CLKernel kernel;
        prepareHomographies(this.HBuffer, inputData.pyramidLevel, parameters, inverses);
        prepareColorTransforms(this.XBuffer, inputData.pyramidLevel, parameters, inverses);
        int dotSize = parameters[0].size();
        int localSize = parameters.length > 1 ? parameters.length : inputData.roiWidth > 32 ? 64 : 32;
        int globalSize = JavaCVCL.alignCeil(inputData.roiWidth, localSize);
        int reduceSize = globalSize / localSize;
        CLBuffer inputBuffer = inputData.getBuffer(this.context);
        CLBuffer outputBuffer = outputData.getBuffer(this.context, dotSize, reduceSize);
        CLEventList cLEventList = new CLEventList(1);
        this.context.writeBuffer(this.HBuffer, false);
        this.context.writeBuffer(this.XBuffer, false);
        if (inputData.autoWrite) {
            inputData.writeBuffer(this.context);
        }
        if (subImg == null) {
            if ($assertionsDisabled || parameters.length == 1) {
                CLKernel putArg = this.oneKernel.putArg(srcImg);
                if (dstImg != null) {
                    transImg = dstImg;
                }
                kernel = putArg.putArg(transImg).putArg(maskImg).putArg(this.HBuffer).putArg(this.XBuffer).putArg(inputBuffer).putArg(outputBuffer).rewind();
            } else {
                throw new AssertionError();
            }
        } else if (srcDotImg == null) {
            if ($assertionsDisabled || parameters.length == 1) {
                kernel = this.subKernel.putArg(srcImg).putArg(subImg).putArg(transImg).putArg(dstImg).putArg(maskImg).putArg(this.HBuffer).putArg(this.XBuffer).putArg(inputBuffer).putArg(outputBuffer).rewind();
            } else {
                throw new AssertionError();
            }
        } else if ($assertionsDisabled || parameters.length == dotSize) {
            kernel = this.dotKernel.putArg(srcImg).putArg(subImg).putArg(srcDotImg).putArg(maskImg).putArg(this.HBuffer).putArg(this.XBuffer).putArg(inputBuffer).putArg(outputBuffer).rewind();
        } else {
            throw new AssertionError();
        }
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
