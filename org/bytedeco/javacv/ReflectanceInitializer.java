package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Logger;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.GNImageAligner.Settings;
import org.bytedeco.javacv.ProCamTransformer.Parameters;

public class ReflectanceInitializer {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static ThreadLocal<CvMat> mat3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> mat3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> mat4x4 = CvMat.createThreadLocal(4, 4);
    private Settings alignerSettings;
    private CameraDevice cameraDevice;
    private ProjectorDevice projectorDevice;
    private IplImage[] projectorImages;
    private double reflectanceMin;
    private int smoothingSize;

    static {
        boolean z;
        if (ReflectanceInitializer.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ReflectanceInitializer(CameraDevice cameraDevice, ProjectorDevice projectorDevice, int channels, Settings alignerSettings) {
        this(cameraDevice, projectorDevice, channels, alignerSettings, 51, 0.01d);
    }

    public ReflectanceInitializer(CameraDevice cameraDevice, ProjectorDevice projectorDevice, int channels, Settings alignerSettings, int smoothingSize, double reflectanceMin) {
        this.alignerSettings = alignerSettings;
        this.smoothingSize = smoothingSize;
        this.reflectanceMin = reflectanceMin;
        this.cameraDevice = cameraDevice;
        this.projectorDevice = projectorDevice;
        this.projectorImages = new IplImage[3];
        for (int i = 0; i < this.projectorImages.length; i++) {
            this.projectorImages[i] = IplImage.create(projectorDevice.imageWidth, projectorDevice.imageHeight, 32, channels);
        }
        opencv_core.cvSetZero(this.projectorImages[0]);
        opencv_core.cvSet(this.projectorImages[1], CvScalar.ONE);
        CvMat H = (CvMat) mat3x3.get();
        projectorDevice.getRectifyingHomography(cameraDevice, H);
        JavaCV.fractalTriangleWave(this.projectorImages[2], H);
    }

    public IplImage[] getProjectorImages() {
        return this.projectorImages;
    }

    public IplImage initializeReflectance(IplImage[] cameraImages, IplImage reflectance, double[] roiPts, double[] gainAmbientLight) {
        CvMat invp;
        int w = cameraImages[0].width();
        int h = cameraImages[0].height();
        int channels = cameraImages[0].nChannels();
        IplImage mask = IplImage.create(w, h, 8, 1);
        opencv_core.cvSetZero(mask);
        opencv_imgproc.cvFillConvexPoly(mask, new CvPoint((long) (roiPts.length / 2)).put((byte) (16 - this.cameraDevice.getMapsPyramidLevel()), roiPts), 4, CvScalar.WHITE, 8, 16);
        CvArr float1 = cameraImages[0];
        CvArr float2 = cameraImages[1];
        opencv_core.cvCopy(float2, reflectance);
        opencv_imgproc.cvSmooth(float1, float1, 2, this.smoothingSize, 0, 0.0d, 0.0d);
        opencv_imgproc.cvSmooth(float2, float2, 2, this.smoothingSize, 0, 0.0d, 0.0d);
        opencv_core.cvSub(float2, float1, float2, null);
        CvArr p = (CvMat) mat3x1.get();
        p.put(new double[]{1.0d, 1.0d, 1.0d});
        opencv_core.cvMatMul(this.projectorDevice.colorMixingMatrix, p, p);
        if (float2.nChannels() == 4) {
            invp = (CvMat) mat4x4.get();
            invp.put(new double[]{1.0d / p.get(0), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d / p.get(1), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d / p.get(2), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d});
        } else {
            invp = (CvMat) mat3x3.get();
            invp.put(new double[]{1.0d / p.get(0), 0.0d, 0.0d, 0.0d, 1.0d / p.get(1), 0.0d, 0.0d, 0.0d, 1.0d / p.get(2)});
        }
        opencv_core.cvTransform(float2, float2, invp, null);
        FloatBuffer fb1 = float1.getFloatBuffer();
        FloatBuffer fb2 = float2.getFloatBuffer();
        ByteBuffer mb = mask.getByteBuffer();
        if (!$assertionsDisabled && fb1.capacity() != fb2.capacity() / 3) {
            throw new AssertionError();
        } else if ($assertionsDisabled || fb1.capacity() == mb.capacity() / 3) {
            int z;
            int[] nPixels = new int[channels];
            int i = 0;
            for (int j = 0; j < fb1.capacity(); j += channels) {
                for (z = 0; z < channels; z++) {
                    float ra = fb1.get(j + z);
                    float r = fb2.get(j + z);
                    float a = r == 0.0f ? 0.0f : ra / r;
                    fb1.put(j + z, a);
                    if (mb.get(i) != (byte) 0 && ((double) r) > this.reflectanceMin) {
                        nPixels[z] = nPixels[z] + 1;
                        int i2 = z + 1;
                        gainAmbientLight[i2] = gainAmbientLight[i2] + ((double) a);
                    }
                }
                i++;
            }
            gainAmbientLight[0] = 1.0d;
            for (z = 0; z < gainAmbientLight.length - 1; z++) {
                gainAmbientLight[z + 1] = nPixels[z] == 0 ? 0.0d : gainAmbientLight[z + 1] / ((double) nPixels[z]);
            }
            opencv_core.cvAddS(float1, opencv_core.cvScalar(p.get(0), p.get(1), p.get(2), 0.0d), float1, null);
            opencv_core.cvDiv(reflectance, float1, reflectance, 1.0d);
            opencv_core.cvNot(mask, mask);
            opencv_imgproc.cvErode(mask, mask, null, 15);
            opencv_core.cvSet(reflectance, CvScalar.ZERO, mask);
            return reflectance;
        } else {
            throw new AssertionError();
        }
    }

    public CvMat initializePlaneParameters(IplImage reflectance, IplImage cameraImage, double[] referencePoints, double[] roiPts, double[] gainAmbientLight) {
        ProCamTransformer transformer = new ProCamTransformer(referencePoints, this.cameraDevice, this.projectorDevice, null);
        transformer.setProjectorImage(this.projectorImages[2], 0, this.alignerSettings.pyramidLevelMax);
        Parameters parameters = transformer.createParameters();
        int gainAmbientLightStart = parameters.size() - gainAmbientLight.length;
        int gainAmbientLightEnd = parameters.size();
        for (int i = gainAmbientLightStart; i < gainAmbientLightEnd; i++) {
            parameters.set(i, gainAmbientLight[i - gainAmbientLightStart]);
        }
        ImageAligner aligner = new GNImageAligner(transformer, parameters, reflectance, roiPts, cameraImage, this.alignerSettings);
        double[] delta = new double[(parameters.size() + 1)];
        boolean converged = false;
        long iterationsStartTime = System.currentTimeMillis();
        int iterations = 0;
        while (!converged && iterations < 100) {
            converged = aligner.iterate(delta);
            iterations++;
        }
        parameters = (Parameters) aligner.getParameters();
        Logger.getLogger(ReflectanceInitializer.class.getName()).info("iteratingTime = " + (System.currentTimeMillis() - iterationsStartTime) + "  iterations = " + iterations + "  objectiveRMSE = " + ((float) aligner.getRMSE()));
        return parameters.getN0();
    }
}
