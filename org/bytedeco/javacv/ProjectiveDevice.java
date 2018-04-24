package org.bytedeco.javacv;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.Arrays;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvAttrList;
import org.bytedeco.javacpp.opencv_core.CvFileNode;
import org.bytedeco.javacpp.opencv_core.CvFileStorage;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class ProjectiveDevice {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static ThreadLocal<CvMat> B4x3 = CvMat.createThreadLocal(4, 3);
    private static ThreadLocal<CvMat> P13x4 = CvMat.createThreadLocal(3, 4);
    private static ThreadLocal<CvMat> P23x4 = CvMat.createThreadLocal(3, 4);
    private static ThreadLocal<CvMat> R13x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> R23x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> a4x1 = CvMat.createThreadLocal(4, 1);
    private static ThreadLocal<CvMat> relativeR3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> relativeT3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> t3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> temp3x3 = CvMat.createThreadLocal(3, 3);
    public CvMat f180E;
    public CvMat f181F;
    public CvMat f182R;
    public CvMat f183T;
    public CvMat additiveLight;
    public double avgColorErr;
    public double avgEpipolarErr;
    public double avgReprojErr;
    public CvMat cameraMatrix;
    public CvMat colorMixingMatrix;
    public String colorOrder;
    public double colorR2;
    private IplImage[] distortMaps1;
    private IplImage[] distortMaps2;
    public CvMat distortionCoeffs;
    public CvMat extrParams;
    private boolean fixedPointMaps;
    public int imageHeight;
    public int imageWidth;
    private int mapsPyramidLevel;
    public double maxEpipolarErr;
    public double maxReprojErr;
    public CvMat reprojErrs;
    private Settings settings;
    private IplImage tempImage;
    private IplImage[] undistortMaps1;
    private IplImage[] undistortMaps2;

    public static class Settings extends BaseChildSettings {
        String name = "";
        double responseGamma = 0.0d;

        public Settings(Settings settings) {
            this.name = settings.name;
            this.responseGamma = settings.responseGamma;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            String str = this.name;
            this.name = name;
            firePropertyChange("name", str, name);
        }

        public double getResponseGamma() {
            return this.responseGamma;
        }

        public void setResponseGamma(double responseGamma) {
            this.responseGamma = responseGamma;
        }
    }

    public static class CalibratedSettings extends Settings {
        File parametersFile = new File("calibration.yaml");

        public CalibratedSettings(CalibratedSettings settings) {
            super(settings);
            this.parametersFile = settings.parametersFile;
        }

        public File getParametersFile() {
            return this.parametersFile;
        }

        public void setParametersFile(File parametersFile) {
            this.parametersFile = parametersFile;
        }

        public String getParametersFilename() {
            return this.parametersFile == null ? "" : this.parametersFile.getPath();
        }

        public void setParametersFilename(String parametersFilename) {
            File file = (parametersFilename == null || parametersFilename.length() == 0) ? null : new File(parametersFilename);
            this.parametersFile = file;
        }
    }

    public static class CalibrationSettings extends Settings {
        int flags = 14720;
        double initAspectRatio = 1.0d;

        public CalibrationSettings(CalibrationSettings settings) {
            super(settings);
            this.initAspectRatio = settings.initAspectRatio;
            this.flags = settings.flags;
        }

        public double getInitAspectRatio() {
            return this.initAspectRatio;
        }

        public void setInitAspectRatio(double initAspectRatio) {
            this.initAspectRatio = initAspectRatio;
        }

        public boolean isUseIntrinsicGuess() {
            return (this.flags & 1) != 0;
        }

        public void setUseIntrinsicGuess(boolean useIntrinsicGuess) {
            if (useIntrinsicGuess) {
                this.flags |= 1;
            } else {
                this.flags &= -2;
            }
        }

        public boolean isFixAspectRatio() {
            return (this.flags & 2) != 0;
        }

        public void setFixAspectRatio(boolean fixAspectRatio) {
            if (fixAspectRatio) {
                this.flags |= 2;
            } else {
                this.flags &= -3;
            }
        }

        public boolean isFixPrincipalPoint() {
            return (this.flags & 4) != 0;
        }

        public void setFixPrincipalPoint(boolean fixPrincipalPoint) {
            if (fixPrincipalPoint) {
                this.flags |= 4;
            } else {
                this.flags &= -5;
            }
        }

        public boolean isZeroTangentDist() {
            return (this.flags & 8) != 0;
        }

        public void setZeroTangentDist(boolean zeroTangentDist) {
            if (zeroTangentDist) {
                this.flags |= 8;
            } else {
                this.flags &= -9;
            }
        }

        public boolean isFixFocalLength() {
            return (this.flags & 16) != 0;
        }

        public void setFixFocalLength(boolean fixFocalLength) {
            if (fixFocalLength) {
                this.flags |= 16;
            } else {
                this.flags &= -17;
            }
        }

        public boolean isFixK1() {
            return (this.flags & 32) != 0;
        }

        public void setFixK1(boolean fixK1) {
            if (fixK1) {
                this.flags |= 32;
            } else {
                this.flags &= -33;
            }
        }

        public boolean isFixK2() {
            return (this.flags & 64) != 0;
        }

        public void setFixK2(boolean fixK2) {
            if (fixK2) {
                this.flags |= 64;
            } else {
                this.flags &= -65;
            }
        }

        public boolean isFixK3() {
            return (this.flags & 128) != 0;
        }

        public void setFixK3(boolean fixK3) {
            if (fixK3) {
                this.flags |= 128;
            } else {
                this.flags &= -129;
            }
        }

        public boolean isFixK4() {
            return (this.flags & 2048) != 0;
        }

        public void setFixK4(boolean fixK4) {
            if (fixK4) {
                this.flags |= 2048;
            } else {
                this.flags &= -2049;
            }
        }

        public boolean isFixK5() {
            return (this.flags & 4096) != 0;
        }

        public void setFixK5(boolean fixK5) {
            if (fixK5) {
                this.flags |= 4096;
            } else {
                this.flags &= -4097;
            }
        }

        public boolean isFixK6() {
            return (this.flags & 8192) != 0;
        }

        public void setFixK6(boolean fixK6) {
            if (fixK6) {
                this.flags |= 8192;
            } else {
                this.flags &= -8193;
            }
        }

        public boolean isRationalModel() {
            return (this.flags & 16384) != 0;
        }

        public void setRationalModel(boolean rationalModel) {
            if (rationalModel) {
                this.flags |= 16384;
            } else {
                this.flags &= -16385;
            }
        }

        public boolean isStereoFixIntrinsic() {
            return (this.flags & 256) != 0;
        }

        public void setStereoFixIntrinsic(boolean stereoFixIntrinsic) {
            if (stereoFixIntrinsic) {
                this.flags |= 256;
            } else {
                this.flags &= -257;
            }
        }

        public boolean isStereoSameFocalLength() {
            return (this.flags & 512) != 0;
        }

        public void setStereoSameFocalLength(boolean stereoSameFocalLength) {
            if (stereoSameFocalLength) {
                this.flags |= 512;
            } else {
                this.flags &= -513;
            }
        }
    }

    public static class Exception extends Exception {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static {
        boolean z;
        if (ProjectiveDevice.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProjectiveDevice(String name) {
        this.imageWidth = 0;
        this.imageHeight = 0;
        this.cameraMatrix = null;
        this.distortionCoeffs = null;
        this.extrParams = null;
        this.reprojErrs = null;
        this.f182R = null;
        this.f183T = null;
        this.f180E = null;
        this.f181F = null;
        this.colorOrder = "BGR";
        this.colorMixingMatrix = null;
        this.additiveLight = null;
        this.colorR2 = 1.0d;
        this.fixedPointMaps = false;
        this.mapsPyramidLevel = 0;
        this.undistortMaps1 = new IplImage[]{null};
        this.undistortMaps2 = new IplImage[]{null};
        this.distortMaps1 = new IplImage[]{null};
        this.distortMaps2 = new IplImage[]{null};
        this.tempImage = null;
        Settings s = new Settings();
        s.name = name;
        setSettings(s);
    }

    public ProjectiveDevice(String name, File file) throws Exception {
        this(name);
        readParameters(file);
    }

    public ProjectiveDevice(String name, String filename) throws Exception {
        this(name);
        readParameters(filename);
    }

    public ProjectiveDevice(String name, CvFileStorage fs) throws Exception {
        this(name);
        readParameters(fs);
    }

    public ProjectiveDevice(Settings settings) throws Exception {
        this.imageWidth = 0;
        this.imageHeight = 0;
        this.cameraMatrix = null;
        this.distortionCoeffs = null;
        this.extrParams = null;
        this.reprojErrs = null;
        this.f182R = null;
        this.f183T = null;
        this.f180E = null;
        this.f181F = null;
        this.colorOrder = "BGR";
        this.colorMixingMatrix = null;
        this.additiveLight = null;
        this.colorR2 = 1.0d;
        this.fixedPointMaps = false;
        this.mapsPyramidLevel = 0;
        this.undistortMaps1 = new IplImage[]{null};
        this.undistortMaps2 = new IplImage[]{null};
        this.distortMaps1 = new IplImage[]{null};
        this.distortMaps2 = new IplImage[]{null};
        this.tempImage = null;
        setSettings(settings);
        if (settings instanceof CalibratedSettings) {
            readParameters(((CalibratedSettings) settings).parametersFile);
        }
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void rescale(int imageWidth, int imageHeight) {
        if ((imageWidth != this.imageWidth || imageHeight != this.imageHeight) && this.cameraMatrix != null) {
            double sx = ((double) imageWidth) / ((double) this.imageWidth);
            double sy = ((double) imageHeight) / ((double) this.imageHeight);
            this.cameraMatrix.put(0, this.cameraMatrix.get(0) * sx);
            this.cameraMatrix.put(1, this.cameraMatrix.get(1) * sx);
            this.cameraMatrix.put(2, this.cameraMatrix.get(2) * sx);
            this.cameraMatrix.put(3, this.cameraMatrix.get(3) * sy);
            this.cameraMatrix.put(4, this.cameraMatrix.get(4) * sy);
            this.cameraMatrix.put(5, this.cameraMatrix.get(5) * sy);
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            int p = this.mapsPyramidLevel;
            IplImage[] iplImageArr = this.undistortMaps1;
            IplImage[] iplImageArr2 = this.undistortMaps2;
            IplImage[] iplImageArr3 = this.distortMaps1;
            this.distortMaps2[p] = null;
            iplImageArr3[p] = null;
            iplImageArr2[p] = null;
            iplImageArr[p] = null;
        }
    }

    public int[] getRGBColorOrder() {
        int[] order = new int[3];
        for (int i = 0; i < 3; i++) {
            switch (Character.toUpperCase(this.colorOrder.charAt(i))) {
                case 'B':
                    order[i] = 2;
                    break;
                case 'G':
                    order[i] = 1;
                    break;
                case 'R':
                    order[i] = 0;
                    break;
                default:
                    if ($assertionsDisabled) {
                        break;
                    }
                    throw new AssertionError();
            }
        }
        return order;
    }

    public static double[] undistort(double[] xd, double[] k) {
        double k1 = k[0];
        double k2 = k[1];
        double k3 = k.length > 4 ? k[4] : 0.0d;
        if (k.length > 5) {
            double k4 = k[5];
        }
        if (k.length > 6) {
            double k5 = k[6];
        }
        if (k.length > 7) {
            double k6 = k[7];
        }
        double p1 = k[2];
        double p2 = k[3];
        double[] xu = (double[]) xd.clone();
        for (int i = 0; i < xd.length / 2; i++) {
            double x = xu[i * 2];
            double y = xu[(i * 2) + 1];
            double xo = xd[i * 2];
            double yo = xd[(i * 2) + 1];
            for (int j = 0; j < 20; j++) {
                double r_2 = (x * x) + (y * y);
                double k_radial = ((1.0d + (k1 * r_2)) + ((k2 * r_2) * r_2)) + (((k3 * r_2) * r_2) * r_2);
                double delta_y = ((((2.0d * y) * y) + r_2) * p1) + (((2.0d * p2) * x) * y);
                x = (xo - ((((2.0d * p1) * x) * y) + ((((2.0d * x) * x) + r_2) * p2))) / k_radial;
                y = (yo - delta_y) / k_radial;
            }
            xu[i * 2] = x;
            xu[(i * 2) + 1] = y;
        }
        return xu;
    }

    public double[] undistort(double... x) {
        return unnormalize(undistort(normalize(x, this.cameraMatrix), this.distortionCoeffs.get()), this.cameraMatrix);
    }

    public static double[] distort(double[] xu, double[] k) {
        double k1 = k[0];
        double k2 = k[1];
        double k3 = k.length > 4 ? k[4] : 0.0d;
        if (k.length > 5) {
            double k4 = k[5];
        }
        if (k.length > 6) {
            double k5 = k[6];
        }
        if (k.length > 7) {
            double k6 = k[7];
        }
        double p1 = k[2];
        double p2 = k[3];
        double[] xd = (double[]) xu.clone();
        for (int i = 0; i < xu.length / 2; i++) {
            double x = xu[i * 2];
            double y = xu[(i * 2) + 1];
            double r_2 = (x * x) + (y * y);
            double k_radial = ((1.0d + (k1 * r_2)) + ((k2 * r_2) * r_2)) + (((k3 * r_2) * r_2) * r_2);
            double delta_y = ((((2.0d * y) * y) + r_2) * p1) + (((2.0d * p2) * x) * y);
            xd[i * 2] = (x * k_radial) + ((((2.0d * p1) * x) * y) + ((((2.0d * x) * x) + r_2) * p2));
            xd[(i * 2) + 1] = (y * k_radial) + delta_y;
        }
        return xd;
    }

    public double[] distort(double... x) {
        return unnormalize(distort(normalize(x, this.cameraMatrix), this.distortionCoeffs.get()), this.cameraMatrix);
    }

    public static double[] normalize(double[] xu, CvMat K) {
        double[] xn = (double[]) xu.clone();
        double fx = K.get(0) / K.get(8);
        double fy = K.get(4) / K.get(8);
        double dx = K.get(2) / K.get(8);
        double dy = K.get(5) / K.get(8);
        double s = K.get(1) / K.get(8);
        for (int i = 0; i < xu.length / 2; i++) {
            xn[i * 2] = ((xu[i * 2] - dx) / fx) - (((xu[(i * 2) + 1] + dy) * s) / (fx * fy));
            xn[(i * 2) + 1] = (xu[(i * 2) + 1] - dy) / fy;
        }
        return xn;
    }

    public static double[] unnormalize(double[] xn, CvMat K) {
        double[] xu = (double[]) xn.clone();
        double fx = K.get(0) / K.get(8);
        double fy = K.get(4) / K.get(8);
        double dx = K.get(2) / K.get(8);
        double dy = K.get(5) / K.get(8);
        double s = K.get(1) / K.get(8);
        for (int i = 0; i < xn.length / 2; i++) {
            xu[i * 2] = ((xn[i * 2] * fx) + dx) + (xn[(i * 2) + 1] * s);
            xu[(i * 2) + 1] = (xn[(i * 2) + 1] * fy) + dy;
        }
        return xu;
    }

    public boolean isFixedPointMaps() {
        return this.fixedPointMaps;
    }

    public void setFixedPointMaps(boolean fixedPointMaps) {
        if (this.fixedPointMaps != fixedPointMaps) {
            this.fixedPointMaps = fixedPointMaps;
            int p = this.mapsPyramidLevel;
            IplImage[] iplImageArr = this.undistortMaps1;
            IplImage[] iplImageArr2 = this.undistortMaps2;
            IplImage[] iplImageArr3 = this.distortMaps1;
            this.distortMaps2[p] = null;
            iplImageArr3[p] = null;
            iplImageArr2[p] = null;
            iplImageArr[p] = null;
        }
    }

    public int getMapsPyramidLevel() {
        return this.mapsPyramidLevel;
    }

    public void setMapsPyramidLevel(int mapsPyramidLevel) {
        if (this.mapsPyramidLevel != mapsPyramidLevel) {
            this.mapsPyramidLevel = mapsPyramidLevel;
            int p = mapsPyramidLevel;
            if (p >= this.undistortMaps1.length || p >= this.undistortMaps2.length || p >= this.distortMaps1.length || p >= this.distortMaps2.length) {
                this.undistortMaps1 = (IplImage[]) Arrays.copyOf(this.undistortMaps1, p + 1);
                this.undistortMaps2 = (IplImage[]) Arrays.copyOf(this.undistortMaps2, p + 1);
                this.distortMaps1 = (IplImage[]) Arrays.copyOf(this.distortMaps1, p + 1);
                this.distortMaps2 = (IplImage[]) Arrays.copyOf(this.distortMaps2, p + 1);
            }
        }
    }

    private void initUndistortMaps() {
        int p = this.mapsPyramidLevel;
        if (this.undistortMaps1[p] == null || this.undistortMaps2[p] == null) {
            if (this.fixedPointMaps) {
                this.undistortMaps1[p] = IplImage.create(this.imageWidth, this.imageHeight, opencv_core.IPL_DEPTH_16S, 2);
                this.undistortMaps2[p] = IplImage.create(this.imageWidth, this.imageHeight, 16, 1);
            } else {
                this.undistortMaps1[p] = IplImage.create(this.imageWidth, this.imageHeight, 32, 1);
                this.undistortMaps2[p] = IplImage.create(this.imageWidth, this.imageHeight, 32, 1);
            }
            opencv_imgproc.cvInitUndistortMap(this.cameraMatrix, this.distortionCoeffs, this.undistortMaps1[p], this.undistortMaps2[p]);
            if (this.mapsPyramidLevel > 0) {
                IplImage map1 = this.undistortMaps1[p];
                IplImage map2 = this.undistortMaps2[p];
                int w = this.imageWidth >> p;
                int h = this.imageHeight >> p;
                this.undistortMaps1[p] = IplImage.create(w, h, map1.depth(), map1.nChannels());
                this.undistortMaps2[p] = IplImage.create(w, h, map2.depth(), map2.nChannels());
                opencv_imgproc.cvResize(map1, this.undistortMaps1[p], 0);
                opencv_imgproc.cvResize(map2, this.undistortMaps2[p], 0);
            }
        }
    }

    public IplImage getUndistortMap1() {
        initUndistortMaps();
        return this.undistortMaps1[this.mapsPyramidLevel];
    }

    public IplImage getUndistortMap2() {
        initUndistortMaps();
        return this.undistortMaps2[this.mapsPyramidLevel];
    }

    public void undistort(IplImage src, IplImage dst) {
        if (src != null && dst != null) {
            initUndistortMaps();
            opencv_imgproc.cvRemap(src, dst, this.undistortMaps1[this.mapsPyramidLevel], this.undistortMaps2[this.mapsPyramidLevel], 9, CvScalar.ZERO);
        }
    }

    public IplImage undistort(IplImage image) {
        if (image == null) {
            return null;
        }
        initUndistortMaps();
        this.tempImage = IplImage.createIfNotCompatible(this.tempImage, image);
        opencv_core.cvResetImageROI(this.tempImage);
        opencv_imgproc.cvRemap(image, this.tempImage, this.undistortMaps1[this.mapsPyramidLevel], this.undistortMaps2[this.mapsPyramidLevel], 9, CvScalar.ZERO);
        return this.tempImage;
    }

    private void initDistortMaps() {
        int p = this.mapsPyramidLevel;
        if (this.distortMaps1[p] == null || this.distortMaps2[p] == null) {
            IplImage mapx = IplImage.create(this.imageWidth, this.imageHeight, 32, 1);
            IplImage mapy = IplImage.create(this.imageWidth, this.imageHeight, 32, 1);
            FloatBuffer bufx = mapx.getFloatBuffer();
            FloatBuffer bufy = mapy.getFloatBuffer();
            int width = mapx.width();
            int height = mapx.height();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double[] distxy = undistort((double) x, (double) y);
                    bufx.put((float) distxy[0]);
                    bufy.put((float) distxy[1]);
                }
            }
            if (this.fixedPointMaps) {
                this.distortMaps1[p] = IplImage.create(this.imageWidth, this.imageHeight, opencv_core.IPL_DEPTH_16S, 2);
                this.distortMaps2[p] = IplImage.create(this.imageWidth, this.imageHeight, 16, 1);
                opencv_imgproc.cvConvertMaps(mapx, mapy, this.distortMaps1[p], this.distortMaps2[p]);
                mapx.release();
                mapy.release();
            } else {
                this.distortMaps1[p] = mapx;
                this.distortMaps2[p] = mapy;
            }
            if (this.mapsPyramidLevel > 0) {
                IplImage map1 = this.distortMaps1[p];
                IplImage map2 = this.distortMaps2[p];
                int w = this.imageWidth >> p;
                int h = this.imageHeight >> p;
                this.distortMaps1[p] = IplImage.create(w, h, map1.depth(), map1.nChannels());
                this.distortMaps2[p] = IplImage.create(w, h, map2.depth(), map2.nChannels());
                opencv_imgproc.cvResize(map1, this.distortMaps1[p], 0);
                opencv_imgproc.cvResize(map2, this.distortMaps2[p], 0);
            }
        }
    }

    public IplImage getDistortMap1() {
        initDistortMaps();
        return this.distortMaps1[this.mapsPyramidLevel];
    }

    public IplImage getDistortMap2() {
        initDistortMaps();
        return this.distortMaps2[this.mapsPyramidLevel];
    }

    public void distort(IplImage src, IplImage dst) {
        if (src != null && dst != null) {
            initDistortMaps();
            opencv_imgproc.cvRemap(src, dst, this.distortMaps1[this.mapsPyramidLevel], this.distortMaps2[this.mapsPyramidLevel], 9, CvScalar.ZERO);
        }
    }

    public IplImage distort(IplImage image) {
        if (image == null) {
            return null;
        }
        initDistortMaps();
        this.tempImage = IplImage.createIfNotCompatible(this.tempImage, image);
        opencv_imgproc.cvRemap(image, this.tempImage, this.distortMaps1[this.mapsPyramidLevel], this.distortMaps2[this.mapsPyramidLevel], 9, CvScalar.ZERO);
        return this.tempImage;
    }

    public CvMat getBackProjectionMatrix(CvMat n, double d, CvMat B) {
        CvMat temp = (CvMat) temp3x3.get();
        temp.cols(1);
        temp.step(temp.step() / 3);
        B.rows(3);
        opencv_core.cvGEMM(this.f182R, this.f183T, -1.0d, null, 0.0d, temp, 1);
        opencv_core.cvGEMM(temp, n, 1.0d, null, 0.0d, B, 2);
        double a = opencv_core.cvDotProduct(n, temp) + d;
        B.put(0, B.get(0) - a);
        B.put(4, B.get(4) - a);
        B.put(8, B.get(8) - a);
        B.rows(4);
        temp.cols(3);
        temp.step(temp.step() * 3);
        B.put(9, n.get());
        opencv_core.cvMatMul(this.cameraMatrix, this.f182R, temp);
        opencv_core.cvInvert(temp, temp, 0);
        opencv_core.cvMatMul(B, temp, B);
        opencv_core.cvConvertScale(B, B, 1.0d / B.get(11), 0.0d);
        return B;
    }

    public CvMat getFrontoParallelH(double[] roipts, CvMat n, CvMat H) {
        CvMat B = (CvMat) B4x3.get();
        CvMat a = (CvMat) a4x1.get();
        CvMat t = (CvMat) t3x1.get();
        double s = Math.signum(n.get(2));
        double[] dir = JavaCV.unitize((-s) * n.get(1), n.get(0) * s);
        double theta = Math.acos((n.get(2) * s) / JavaCV.norm(n.get()));
        t.put(new double[]{dir[0] * theta, dir[1] * theta, 0.0d});
        opencv_calib3d.cvRodrigues2(t, H, null);
        opencv_core.cvMatMul(this.f182R, H, H);
        double x = 0.0d;
        double y = 0.0d;
        if (roipts != null) {
            double x1 = roipts[0];
            double y1 = roipts[1];
            double x2 = roipts[4];
            double y2 = roipts[5];
            double x3 = roipts[2];
            double y3 = roipts[3];
            double x4 = roipts[6];
            double y4 = roipts[7];
            double u = (((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3))) / (((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1)));
            x = x1 + ((x2 - x1) * u);
            y = y1 + ((y2 - y1) * u);
        }
        getBackProjectionMatrix(n, -1.0d, B);
        t.put(new double[]{x, y, 1.0d});
        opencv_core.cvMatMul(B, t, a);
        H.put(2, a.get(0) / a.get(3));
        H.put(5, a.get(1) / a.get(3));
        H.put(8, a.get(2) / a.get(3));
        return H;
    }

    public CvMat getRectifyingHomography(ProjectiveDevice peer, CvMat H) {
        CvMat relativeR = (CvMat) relativeR3x3.get();
        CvMat relativeT = (CvMat) relativeT3x1.get();
        opencv_core.cvGEMM(this.f182R, peer.f182R, 1.0d, null, 0.0d, relativeR, 2);
        opencv_core.cvGEMM(relativeR, peer.f183T, -1.0d, this.f183T, 1.0d, relativeT, 0);
        CvArr R1 = (CvMat) R13x3.get();
        CvMat P1 = (CvMat) P13x4.get();
        CvArr R2 = (CvMat) R23x3.get();
        CvMat P2 = (CvMat) P23x4.get();
        opencv_calib3d.cvStereoRectify(peer.cameraMatrix, this.cameraMatrix, peer.distortionCoeffs, this.distortionCoeffs, opencv_core.cvSize((peer.imageWidth + this.imageWidth) / 2, (peer.imageHeight + this.imageHeight) / 2), relativeR, relativeT, R1, R2, P1, P2, null, 0, -1.0d, CvSize.ZERO, null, null);
        opencv_core.cvMatMul(this.cameraMatrix, R2, R2);
        opencv_core.cvInvert(this.cameraMatrix, R1);
        opencv_core.cvMatMul(R2, R1, H);
        return H;
    }

    public static ProjectiveDevice[] read(String filename) throws Exception {
        int i = 0;
        CvFileStorage fs = CvFileStorage.open(filename, null, 0);
        CameraDevice[] cameraDevices = CameraDevice.read(fs);
        ProjectorDevice[] projectorDevices = ProjectorDevice.read(fs);
        ProjectiveDevice[] devices = new ProjectiveDevice[(cameraDevices.length + projectorDevices.length)];
        int length = cameraDevices.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            int i4 = i3 + 1;
            devices[i3] = cameraDevices[i2];
            i2++;
            i3 = i4;
        }
        i2 = projectorDevices.length;
        while (i < i2) {
            i4 = i3 + 1;
            devices[i3] = projectorDevices[i];
            i++;
            i3 = i4;
        }
        fs.release();
        return devices;
    }

    public static void write(String filename, ProjectiveDevice[]... devices) {
        int i;
        ProjectiveDevice[] ds;
        int totalLength = 0;
        for (ProjectiveDevice[] ds2 : devices) {
            totalLength += ds2.length;
        }
        ProjectiveDevice[] allDevices = new ProjectiveDevice[totalLength];
        int i2 = 0;
        int length = devices.length;
        int i3 = 0;
        while (i3 < length) {
            ds2 = devices[i3];
            int length2 = ds2.length;
            i = 0;
            int i4 = i2;
            while (i < length2) {
                i2 = i4 + 1;
                allDevices[i4] = ds2[i];
                i++;
                i4 = i2;
            }
            i3++;
            i2 = i4;
        }
        write(filename, allDevices);
    }

    public static void write(String filename, ProjectiveDevice... devices) {
        int i = 0;
        CvFileStorage fs = CvFileStorage.open(filename, null, 1);
        CvAttrList a = opencv_core.cvAttrList();
        opencv_core.cvStartWriteStruct(fs, "Cameras", 5, null, a);
        for (ProjectiveDevice d : devices) {
            if (d instanceof CameraDevice) {
                opencv_core.cvWriteString(fs, null, d.getSettings().getName(), 0);
            }
        }
        opencv_core.cvEndWriteStruct(fs);
        opencv_core.cvStartWriteStruct(fs, "Projectors", 5, null, a);
        for (ProjectiveDevice d2 : devices) {
            if (d2 instanceof ProjectorDevice) {
                opencv_core.cvWriteString(fs, null, d2.getSettings().getName(), 0);
            }
        }
        opencv_core.cvEndWriteStruct(fs);
        int length = devices.length;
        while (i < length) {
            devices[i].writeParameters(fs);
            i++;
        }
        fs.release();
    }

    public void writeParameters(File file) {
        writeParameters(file.getAbsolutePath());
    }

    public void writeParameters(String filename) {
        CvFileStorage fs = CvFileStorage.open(filename, null, 1);
        writeParameters(fs);
        fs.release();
    }

    public void writeParameters(CvFileStorage fs) {
        CvAttrList a = opencv_core.cvAttrList();
        opencv_core.cvStartWriteStruct(fs, getSettings().getName(), 6, null, a);
        opencv_core.cvWriteInt(fs, "imageWidth", this.imageWidth);
        opencv_core.cvWriteInt(fs, "imageHeight", this.imageHeight);
        opencv_core.cvWriteReal(fs, "responseGamma", getSettings().getResponseGamma());
        if (this.cameraMatrix != null) {
            opencv_core.cvWrite(fs, "cameraMatrix", this.cameraMatrix, a);
        }
        if (this.distortionCoeffs != null) {
            opencv_core.cvWrite(fs, "distortionCoeffs", this.distortionCoeffs, a);
        }
        if (this.extrParams != null) {
            opencv_core.cvWrite(fs, "extrParams", this.extrParams, a);
        }
        if (this.reprojErrs != null) {
            opencv_core.cvWrite(fs, "reprojErrs", this.reprojErrs, a);
        }
        opencv_core.cvWriteReal(fs, "avgReprojErr", this.avgReprojErr);
        opencv_core.cvWriteReal(fs, "maxReprojErr", this.maxReprojErr);
        if (this.f182R != null) {
            opencv_core.cvWrite(fs, "R", this.f182R, a);
        }
        if (this.f183T != null) {
            opencv_core.cvWrite(fs, "T", this.f183T, a);
        }
        if (this.f180E != null) {
            opencv_core.cvWrite(fs, "E", this.f180E, a);
        }
        if (this.f181F != null) {
            opencv_core.cvWrite(fs, "F", this.f181F, a);
        }
        opencv_core.cvWriteReal(fs, "avgEpipolarErr", this.avgEpipolarErr);
        opencv_core.cvWriteReal(fs, "maxEpipolarErr", this.maxEpipolarErr);
        opencv_core.cvWriteString(fs, "colorOrder", this.colorOrder, 0);
        if (this.colorMixingMatrix != null) {
            opencv_core.cvWrite(fs, "colorMixingMatrix", this.colorMixingMatrix, a);
        }
        if (this.additiveLight != null) {
            opencv_core.cvWrite(fs, "additiveLight", this.additiveLight, a);
        }
        opencv_core.cvWriteReal(fs, "avgColorErr", this.avgColorErr);
        opencv_core.cvWriteReal(fs, "colorR2", this.colorR2);
        opencv_core.cvEndWriteStruct(fs);
    }

    public void readParameters(File file) throws Exception {
        readParameters(file.getAbsolutePath());
    }

    public void readParameters(String filename) throws Exception {
        CvFileStorage fs = CvFileStorage.open(filename, null, 0);
        readParameters(fs);
        fs.release();
    }

    public void readParameters(CvFileStorage fs) throws Exception {
        CvMat cvMat = null;
        if (fs == null) {
            throw new Exception("Error: CvFileStorage is null, cannot read parameters for device " + getSettings().getName() + ". Is the parametersFile correct?");
        }
        CvAttrList a = opencv_core.cvAttrList();
        CvFileNode fn = opencv_core.cvGetFileNodeByName(fs, null, getSettings().getName());
        if (fn == null) {
            throw new Exception("Error: CvFileNode is null, cannot read parameters for device " + getSettings().getName() + ". Is the name correct?");
        }
        this.imageWidth = opencv_core.cvReadIntByName(fs, fn, "imageWidth", this.imageWidth);
        this.imageHeight = opencv_core.cvReadIntByName(fs, fn, "imageHeight", this.imageHeight);
        getSettings().setResponseGamma(opencv_core.cvReadRealByName(fs, fn, "gamma", getSettings().getResponseGamma()));
        Pointer p = opencv_core.cvReadByName(fs, fn, "cameraMatrix", a);
        this.cameraMatrix = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "distortionCoeffs", a);
        this.distortionCoeffs = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "extrParams", a);
        this.extrParams = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "reprojErrs", a);
        this.reprojErrs = p == null ? null : new CvMat(p);
        this.avgReprojErr = opencv_core.cvReadRealByName(fs, fn, "avgReprojErr", this.avgReprojErr);
        this.maxReprojErr = opencv_core.cvReadRealByName(fs, fn, "maxReprojErr", this.maxReprojErr);
        p = opencv_core.cvReadByName(fs, fn, "R", a);
        this.f182R = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "T", a);
        this.f183T = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "E", a);
        this.f180E = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "F", a);
        this.f181F = p == null ? null : new CvMat(p);
        this.avgEpipolarErr = opencv_core.cvReadRealByName(fs, fn, "avgEpipolarErr", this.avgEpipolarErr);
        this.maxEpipolarErr = opencv_core.cvReadRealByName(fs, fn, "maxEpipolarErr", this.maxEpipolarErr);
        this.colorOrder = opencv_core.cvReadStringByName(fs, fn, "colorOrder", this.colorOrder);
        p = opencv_core.cvReadByName(fs, fn, "colorMixingMatrix", a);
        this.colorMixingMatrix = p == null ? null : new CvMat(p);
        p = opencv_core.cvReadByName(fs, fn, "additiveLight", a);
        if (p != null) {
            cvMat = new CvMat(p);
        }
        this.additiveLight = cvMat;
        this.avgColorErr = opencv_core.cvReadRealByName(fs, fn, "avgColorErr", this.avgColorErr);
        this.colorR2 = opencv_core.cvReadRealByName(fs, fn, "colorR2", this.colorR2);
    }

    public String toString() {
        String str;
        String s = getSettings().getName() + " (" + this.imageWidth + " x " + this.imageHeight + ")\n";
        for (int i = 0; i < getSettings().getName().length(); i++) {
            s = s + "=";
        }
        StringBuilder append = new StringBuilder().append(s).append("\nIntrinsics\n----------\ncamera matrix = ");
        if (this.cameraMatrix == null) {
            str = "null";
        } else {
            str = this.cameraMatrix.toString(16);
        }
        append = append.append(str).append("\ndistortion coefficients = ").append(this.distortionCoeffs == null ? "null" : this.distortionCoeffs).append("\nreprojection RMS/max error (pixels) = ").append((float) this.avgReprojErr).append(" / ").append((float) this.maxReprojErr).append("\n\nExtrinsics\n----------\nrotation = ");
        if (this.f182R == null) {
            str = "null";
        } else {
            str = this.f182R.toString(11);
        }
        append = append.append(str).append("\ntranslation = ");
        if (this.f183T == null) {
            str = "null";
        } else {
            str = this.f183T.toString(14);
        }
        append = append.append(str).append("\nepipolar RMS/max error (pixels) = ").append((float) this.avgEpipolarErr).append(" / ").append((float) this.maxEpipolarErr).append("\n\nColor\n-----\norder = ").append(this.colorOrder).append("\nmixing matrix = ");
        if (this.colorMixingMatrix == null) {
            str = "null";
        } else {
            str = this.colorMixingMatrix.toString(16);
        }
        append = append.append(str).append("\nadditive light = ");
        if (this.additiveLight == null) {
            str = "null";
        } else {
            str = this.additiveLight.toString(17);
        }
        return append.append(str).append("\nnormalized RMSE (intensity) = ").append((float) this.avgColorErr).append("\nR^2 (intensity) = ").append((float) this.colorR2).toString();
    }
}
