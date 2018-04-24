package org.bytedeco.javacv;

import com.itextpdf.text.pdf.BaseField;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.KeyPoint;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d.AKAZE;
import org.bytedeco.javacpp.opencv_flann.Index;
import org.bytedeco.javacpp.opencv_flann.IndexParams;
import org.bytedeco.javacpp.opencv_flann.LshIndexParams;
import org.bytedeco.javacpp.opencv_flann.SearchParams;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class ObjectFinder {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final int[] bits = new int[256];
    static final Logger logger = Logger.getLogger(ObjectFinder.class.getName());
    Mat f186H;
    Mat distsMat;
    Index flannIndex;
    Mat imageDescriptors;
    KeyPointVector imageKeypoints;
    IndexParams indexParams;
    Mat indicesMat;
    Mat mask;
    Mat objectDescriptors;
    KeyPointVector objectKeypoints;
    Mat pt1;
    Mat pt2;
    ArrayList<Integer> ptpairs;
    SearchParams searchParams;
    Settings settings;

    public static class Settings extends BaseChildSettings {
        AKAZE detector = AKAZE.create();
        double distanceThreshold = 0.75d;
        int matchesMin = 4;
        IplImage objectImage = null;
        double ransacReprojThreshold = 1.0d;
        boolean useFLANN = false;

        public IplImage getObjectImage() {
            return this.objectImage;
        }

        public void setObjectImage(IplImage objectImage) {
            this.objectImage = objectImage;
        }

        public int getDescriptorType() {
            return this.detector.getDescriptorType();
        }

        public void setDescriptorType(int dtype) {
            this.detector.setDescriptorType(dtype);
        }

        public int getDescriptorSize() {
            return this.detector.getDescriptorSize();
        }

        public void setDescriptorSize(int dsize) {
            this.detector.setDescriptorSize(dsize);
        }

        public int getDescriptorChannels() {
            return this.detector.getDescriptorChannels();
        }

        public void setDescriptorChannels(int dch) {
            this.detector.setDescriptorChannels(dch);
        }

        public double getThreshold() {
            return this.detector.getThreshold();
        }

        public void setThreshold(double threshold) {
            this.detector.setThreshold(threshold);
        }

        public int getNOctaves() {
            return this.detector.getNOctaves();
        }

        public void setNOctaves(int nOctaves) {
            this.detector.setNOctaves(nOctaves);
        }

        public int getNOctaveLayers() {
            return this.detector.getNOctaveLayers();
        }

        public void setNOctaveLayers(int nOctaveLayers) {
            this.detector.setNOctaveLayers(nOctaveLayers);
        }

        public double getDistanceThreshold() {
            return this.distanceThreshold;
        }

        public void setDistanceThreshold(double distanceThreshold) {
            this.distanceThreshold = distanceThreshold;
        }

        public int getMatchesMin() {
            return this.matchesMin;
        }

        public void setMatchesMin(int matchesMin) {
            this.matchesMin = matchesMin;
        }

        public double getRansacReprojThreshold() {
            return this.ransacReprojThreshold;
        }

        public void setRansacReprojThreshold(double ransacReprojThreshold) {
            this.ransacReprojThreshold = ransacReprojThreshold;
        }

        public boolean isUseFLANN() {
            return this.useFLANN;
        }

        public void setUseFLANN(boolean useFLANN) {
            this.useFLANN = useFLANN;
        }
    }

    static {
        boolean z;
        if (ObjectFinder.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        for (int i = 0; i < bits.length; i++) {
            for (int j = i; j != 0; j >>= 1) {
                int[] iArr = bits;
                iArr[i] = iArr[i] + (j & 1);
            }
        }
    }

    public ObjectFinder(IplImage objectImage) {
        this.objectKeypoints = null;
        this.imageKeypoints = null;
        this.objectDescriptors = null;
        this.imageDescriptors = null;
        this.flannIndex = null;
        this.indexParams = null;
        this.searchParams = null;
        this.pt1 = null;
        this.pt2 = null;
        this.mask = null;
        this.f186H = null;
        this.ptpairs = null;
        this.settings = new Settings();
        this.settings.objectImage = objectImage;
        setSettings(this.settings);
    }

    public ObjectFinder(Settings settings) {
        this.objectKeypoints = null;
        this.imageKeypoints = null;
        this.objectDescriptors = null;
        this.imageDescriptors = null;
        this.flannIndex = null;
        this.indexParams = null;
        this.searchParams = null;
        this.pt1 = null;
        this.pt2 = null;
        this.mask = null;
        this.f186H = null;
        this.ptpairs = null;
        setSettings(settings);
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.objectKeypoints = new KeyPointVector();
        this.objectDescriptors = new Mat();
        settings.detector.detectAndCompute(opencv_core.cvarrToMat(settings.objectImage), new Mat(), this.objectKeypoints, this.objectDescriptors, false);
        int total = (int) this.objectKeypoints.size();
        if (settings.useFLANN) {
            this.indicesMat = new Mat(total, 2, opencv_core.CV_32SC1);
            this.distsMat = new Mat(total, 2, opencv_core.CV_32FC1);
            this.flannIndex = new Index();
            this.indexParams = new LshIndexParams(12, 20, 2);
            this.searchParams = new SearchParams(64, 0.0f, true);
            this.searchParams.deallocate(false);
        }
        this.pt1 = new Mat(total, 1, opencv_core.CV_32FC2);
        this.pt2 = new Mat(total, 1, opencv_core.CV_32FC2);
        this.mask = new Mat(total, 1, opencv_core.CV_8UC1);
        this.f186H = new Mat(3, 3, opencv_core.CV_64FC1);
        this.ptpairs = new ArrayList(this.objectDescriptors.rows() * 2);
        logger.info(total + " object descriptors");
    }

    public double[] find(IplImage image) {
        if (this.objectDescriptors.rows() < this.settings.getMatchesMin()) {
            return null;
        }
        this.imageKeypoints = new KeyPointVector();
        this.imageDescriptors = new Mat();
        this.settings.detector.detectAndCompute(opencv_core.cvarrToMat(image), new Mat(), this.imageKeypoints, this.imageDescriptors, false);
        if (this.imageDescriptors.rows() < this.settings.getMatchesMin()) {
            return null;
        }
        logger.info(((int) this.imageKeypoints.size()) + " image descriptors");
        int w = this.settings.objectImage.width();
        int h = this.settings.objectImage.height();
        return locatePlanarObject(this.objectKeypoints, this.objectDescriptors, this.imageKeypoints, this.imageDescriptors, new double[]{0.0d, 0.0d, (double) w, 0.0d, (double) w, (double) h, 0.0d, (double) h});
    }

    int compareDescriptors(ByteBuffer d1, ByteBuffer d2, int best) {
        int totalCost = 0;
        if ($assertionsDisabled || d1.limit() - d1.position() == d2.limit() - d2.position()) {
            while (d1.position() < d1.limit()) {
                totalCost += bits[(d1.get() ^ d2.get()) & 255];
                if (totalCost > best) {
                    break;
                }
            }
            return totalCost;
        }
        throw new AssertionError();
    }

    int naiveNearestNeighbor(ByteBuffer vec, ByteBuffer modelDescriptors) {
        int neighbor = -1;
        int dist1 = Integer.MAX_VALUE;
        int dist2 = Integer.MAX_VALUE;
        int size = vec.limit() - vec.position();
        for (int i = 0; i * size < modelDescriptors.capacity(); i++) {
            int d = compareDescriptors((ByteBuffer) vec.reset(), (ByteBuffer) modelDescriptors.position(i * size).limit((i + 1) * size), dist2);
            if (d < dist1) {
                dist2 = dist1;
                dist1 = d;
                neighbor = i;
            } else if (d < dist2) {
                dist2 = d;
            }
        }
        return ((double) dist1) < this.settings.distanceThreshold * ((double) dist2) ? neighbor : -1;
    }

    void findPairs(Mat objectDescriptors, Mat imageDescriptors) {
        int size = imageDescriptors.cols();
        ByteBuffer objectBuf = (ByteBuffer) objectDescriptors.createBuffer();
        ByteBuffer imageBuf = (ByteBuffer) imageDescriptors.createBuffer();
        for (int i = 0; i * size < objectBuf.capacity(); i++) {
            int nearestNeighbor = naiveNearestNeighbor((ByteBuffer) objectBuf.position(i * size).limit((i + 1) * size).mark(), imageBuf);
            if (nearestNeighbor >= 0) {
                this.ptpairs.add(Integer.valueOf(i));
                this.ptpairs.add(Integer.valueOf(nearestNeighbor));
            }
        }
    }

    void flannFindPairs(Mat objectDescriptors, Mat imageDescriptors) {
        int length = objectDescriptors.rows();
        this.flannIndex.build(imageDescriptors, this.indexParams, 9);
        this.flannIndex.knnSearch(objectDescriptors, this.indicesMat, this.distsMat, 2, this.searchParams);
        IntBuffer indicesBuf = (IntBuffer) this.indicesMat.createBuffer();
        IntBuffer distsBuf = (IntBuffer) this.distsMat.createBuffer();
        for (int i = 0; i < length; i++) {
            if (((double) distsBuf.get(i * 2)) < this.settings.distanceThreshold * ((double) distsBuf.get((i * 2) + 1))) {
                this.ptpairs.add(Integer.valueOf(i));
                this.ptpairs.add(Integer.valueOf(indicesBuf.get(i * 2)));
            }
        }
    }

    double[] locatePlanarObject(KeyPointVector objectKeypoints, Mat objectDescriptors, KeyPointVector imageKeypoints, Mat imageDescriptors, double[] srcCorners) {
        this.ptpairs.clear();
        if (this.settings.useFLANN) {
            flannFindPairs(objectDescriptors, imageDescriptors);
        } else {
            findPairs(objectDescriptors, imageDescriptors);
        }
        int n = this.ptpairs.size() / 2;
        logger.info(n + " matching pairs found");
        if (n < this.settings.matchesMin) {
            return null;
        }
        int i;
        this.pt1.resize((long) n);
        this.pt2.resize((long) n);
        this.mask.resize((long) n);
        FloatBuffer pt1Idx = (FloatBuffer) this.pt1.createBuffer();
        FloatBuffer pt2Idx = (FloatBuffer) this.pt2.createBuffer();
        for (i = 0; i < n; i++) {
            Point2f p1 = objectKeypoints.get((long) ((Integer) this.ptpairs.get(i * 2)).intValue()).pt();
            pt1Idx.put(i * 2, p1.x());
            pt1Idx.put((i * 2) + 1, p1.y());
            Point2f p2 = imageKeypoints.get((long) ((Integer) this.ptpairs.get((i * 2) + 1)).intValue()).pt();
            pt2Idx.put(i * 2, p2.x());
            pt2Idx.put((i * 2) + 1, p2.y());
        }
        this.f186H = opencv_calib3d.findHomography(this.pt1, this.pt2, 8, this.settings.ransacReprojThreshold, this.mask, 2000, 0.995d);
        if (this.f186H.empty() || opencv_core.countNonZero(this.mask) < this.settings.matchesMin) {
            return null;
        }
        double[] h = (double[]) this.f186H.createIndexer(false).array();
        double[] dstCorners = new double[srcCorners.length];
        for (i = 0; i < srcCorners.length / 2; i++) {
            double x = srcCorners[i * 2];
            double y = srcCorners[(i * 2) + 1];
            double Z = 1.0d / (((h[6] * x) + (h[7] * y)) + h[8]);
            double Y = (((h[3] * x) + (h[4] * y)) + h[5]) * Z;
            dstCorners[i * 2] = (((h[0] * x) + (h[1] * y)) + h[2]) * Z;
            dstCorners[(i * 2) + 1] = Y;
        }
        return dstCorners;
    }

    public static void main(String[] args) throws Exception {
        int i;
        String objectFilename = args.length == 2 ? args[0] : "/usr/local/share/OpenCV/samples/c/box.png";
        String sceneFilename = args.length == 2 ? args[1] : "/usr/local/share/OpenCV/samples/c/box_in_scene.png";
        CvArr object = opencv_imgcodecs.cvLoadImage(objectFilename, 0);
        IplImage image = opencv_imgcodecs.cvLoadImage(sceneFilename, 0);
        if (object == null || image == null) {
            System.err.println("Can not load " + objectFilename + " and/or " + sceneFilename);
            System.exit(-1);
        }
        CvArr objectColor = IplImage.create(object.width(), object.height(), 8, 3);
        opencv_imgproc.cvCvtColor(object, objectColor, 8);
        IplImage correspond = IplImage.create(image.width(), object.height() + image.height(), 8, 1);
        opencv_core.cvSetImageROI(correspond, opencv_core.cvRect(0, 0, object.width(), object.height()));
        opencv_core.cvCopy(object, correspond);
        opencv_core.cvSetImageROI(correspond, opencv_core.cvRect(0, object.height(), correspond.width(), correspond.height()));
        opencv_core.cvCopy(image, correspond);
        opencv_core.cvResetImageROI(correspond);
        Settings settings = new Settings();
        settings.objectImage = object;
        settings.useFLANN = true;
        settings.ransacReprojThreshold = 5.0d;
        ObjectFinder finder = new ObjectFinder(settings);
        long start = System.currentTimeMillis();
        double[] dst_corners = finder.find(image);
        System.out.println("Finding time = " + (System.currentTimeMillis() - start) + " ms");
        if (dst_corners != null) {
            for (i = 0; i < 4; i++) {
                int j = (i + 1) % 4;
                opencv_imgproc.line(opencv_core.cvarrToMat(correspond), new Point((int) Math.round(dst_corners[i * 2]), object.height() + ((int) Math.round(dst_corners[(i * 2) + 1]))), new Point((int) Math.round(dst_corners[j * 2]), object.height() + ((int) Math.round(dst_corners[(j * 2) + 1]))), Scalar.WHITE, 1, 8, 0);
            }
        }
        for (i = 0; i < finder.ptpairs.size(); i += 2) {
            Point2f pt1 = finder.objectKeypoints.get((long) ((Integer) finder.ptpairs.get(i)).intValue()).pt();
            Point2f pt2 = finder.imageKeypoints.get((long) ((Integer) finder.ptpairs.get(i + 1)).intValue()).pt();
            opencv_imgproc.line(opencv_core.cvarrToMat(correspond), new Point(Math.round(pt1.x()), Math.round(pt1.y())), new Point(Math.round(pt2.x()), Math.round(pt2.y() + ((float) object.height()))), Scalar.WHITE, 1, 8, 0);
        }
        CanvasFrame canvasFrame = new CanvasFrame("Object");
        CanvasFrame correspondFrame = new CanvasFrame("Object Correspond");
        OpenCVFrameConverter converter = new ToIplImage();
        correspondFrame.showImage(converter.convert(correspond));
        for (i = 0; ((long) i) < finder.objectKeypoints.size(); i++) {
            KeyPoint r = finder.objectKeypoints.get((long) i);
            opencv_imgproc.circle(opencv_core.cvarrToMat(objectColor), new Point(Math.round(r.pt().x()), Math.round(r.pt().y())), Math.round(r.size() / BaseField.BORDER_WIDTH_MEDIUM), Scalar.RED, 1, 8, 0);
        }
        canvasFrame.showImage(converter.convert((IplImage) objectColor));
        canvasFrame.waitKey();
        canvasFrame.dispose();
        correspondFrame.dispose();
    }
}
