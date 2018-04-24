package org.bytedeco.javacv;

import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class MarkedPlane {
    private static ThreadLocal<CvMat> tempWarp3x3 = CvMat.createThreadLocal(3, 3);
    private CvScalar backgroundColor;
    private CvScalar foregroundColor;
    private ThreadLocal<CvMat> localDstPts;
    private ThreadLocal<CvMat> localSrcPts;
    private Marker[] markers;
    private IplImage planeImage;
    private CvMat prewarp;
    private IplImage superPlaneImage;

    public MarkedPlane(int width, int height, Marker[] planeMarkers, double superScale) {
        this(width, height, planeMarkers, false, CvScalar.BLACK, CvScalar.WHITE, superScale);
    }

    public MarkedPlane(int width, int height, Marker[] markers, boolean initPrewarp, CvScalar foregroundColor, CvScalar backgroundColor, double superScale) {
        this.markers = null;
        this.planeImage = null;
        this.superPlaneImage = null;
        this.markers = markers;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.prewarp = null;
        if (initPrewarp) {
            this.prewarp = CvMat.create(3, 3);
            double minx = Double.MAX_VALUE;
            double miny = Double.MAX_VALUE;
            double maxx = Double.MIN_VALUE;
            double maxy = Double.MIN_VALUE;
            for (Marker m : markers) {
                double[] c = m.corners;
                minx = Math.min(Math.min(Math.min(Math.min(minx, c[0]), c[2]), c[4]), c[6]);
                miny = Math.min(Math.min(Math.min(Math.min(miny, c[1]), c[3]), c[5]), c[7]);
                maxx = Math.max(Math.max(Math.max(Math.max(maxx, c[0]), c[2]), c[4]), c[6]);
                maxy = Math.max(Math.max(Math.max(Math.max(maxy, c[1]), c[3]), c[5]), c[7]);
            }
            double aspect = (maxx - minx) / (maxy - miny);
            if (aspect > ((double) width) / ((double) height)) {
                double h = ((double) width) / aspect;
                JavaCV.getPerspectiveTransform(new double[]{minx, miny, maxx, miny, maxx, maxy, minx, maxy}, new double[]{0.0d, ((double) height) - h, (double) width, ((double) height) - h, (double) width, (double) height, 0.0d, (double) height}, this.prewarp);
            } else {
                double w = ((double) height) * aspect;
                JavaCV.getPerspectiveTransform(new double[]{minx, miny, maxx, miny, maxx, maxy, minx, maxy}, new double[]{0.0d, 0.0d, w, 0.0d, w, (double) height, 0.0d, (double) height}, this.prewarp);
            }
        }
        if (width > 0 && height > 0) {
            this.planeImage = IplImage.create(width, height, 8, 1);
            if (superScale == 1.0d) {
                this.superPlaneImage = null;
            } else {
                this.superPlaneImage = IplImage.create((int) Math.ceil(((double) width) * superScale), (int) Math.ceil(((double) height) * superScale), 8, 1);
            }
            setPrewarp(this.prewarp);
        }
        this.localSrcPts = CvMat.createThreadLocal(markers.length * 4, 2);
        this.localDstPts = CvMat.createThreadLocal(markers.length * 4, 2);
    }

    public CvScalar getForegroundColor() {
        return this.foregroundColor;
    }

    public void setForegroundColor(CvScalar foregroundColor) {
        this.foregroundColor = foregroundColor;
        setPrewarp(this.prewarp);
    }

    public CvScalar getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(CvScalar backgroundColor) {
        this.backgroundColor = backgroundColor;
        setPrewarp(this.prewarp);
    }

    public Marker[] getMarkers() {
        return this.markers;
    }

    public void setColors(CvScalar foregroundColor, CvScalar backgroundColor) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        setPrewarp(this.prewarp);
    }

    public CvMat getPrewarp() {
        return this.prewarp;
    }

    public void setPrewarp(CvMat prewarp) {
        this.prewarp = prewarp;
        if (this.superPlaneImage == null) {
            opencv_core.cvSet(this.planeImage, this.backgroundColor);
        } else {
            opencv_core.cvSet(this.superPlaneImage, this.backgroundColor);
        }
        for (int i = 0; i < this.markers.length; i++) {
            if (this.superPlaneImage == null) {
                this.markers[i].draw(this.planeImage, this.foregroundColor, 1.0d, prewarp);
            } else {
                this.markers[i].draw(this.superPlaneImage, this.foregroundColor, ((double) this.superPlaneImage.width()) / ((double) this.planeImage.width()), prewarp);
            }
        }
        if (this.superPlaneImage != null) {
            opencv_imgproc.cvResize(this.superPlaneImage, this.planeImage, 3);
        }
    }

    public IplImage getImage() {
        return this.planeImage;
    }

    public int getWidth() {
        return this.planeImage.width();
    }

    public int getHeight() {
        return this.planeImage.height();
    }

    public double getTotalWarp(Marker[] imagedMarkers, CvMat totalWarp) {
        return getTotalWarp(imagedMarkers, totalWarp, false);
    }

    public double getTotalWarp(Marker[] imagedMarkers, CvMat totalWarp, boolean useCenters) {
        double rmse = Double.POSITIVE_INFINITY;
        int pointsPerMarker = useCenters ? 1 : 4;
        CvArr srcPts = (CvMat) this.localSrcPts.get();
        srcPts.rows(this.markers.length * pointsPerMarker);
        CvMat dstPts = (CvMat) this.localDstPts.get();
        dstPts.rows(this.markers.length * pointsPerMarker);
        int numPoints = 0;
        for (Marker m1 : this.markers) {
            int length = imagedMarkers.length;
            int i = 0;
            while (i < length) {
                Marker m2 = imagedMarkers[i];
                if (m1.id == m2.id) {
                    if (useCenters) {
                        srcPts.put(numPoints * 2, m1.getCenter());
                        dstPts.put(numPoints * 2, m2.getCenter());
                    } else {
                        srcPts.put(numPoints * 2, m1.corners);
                        dstPts.put(numPoints * 2, m2.corners);
                    }
                    numPoints += pointsPerMarker;
                } else {
                    i++;
                }
            }
        }
        if (numPoints > 4 || (srcPts.rows() == 4 && numPoints == 4)) {
            srcPts.rows(numPoints);
            dstPts.rows(numPoints);
            if (numPoints == 4) {
                JavaCV.getPerspectiveTransform(srcPts.get(), dstPts.get(), totalWarp);
            } else {
                opencv_calib3d.cvFindHomography(srcPts, dstPts, totalWarp);
            }
            srcPts.cols(1);
            srcPts.type(6, 2);
            dstPts.cols(1);
            dstPts.type(6, 2);
            opencv_core.cvPerspectiveTransform(srcPts, srcPts, totalWarp);
            srcPts.cols(2);
            srcPts.type(6, 1);
            dstPts.cols(2);
            dstPts.type(6, 1);
            rmse = 0.0d;
            for (int i2 = 0; i2 < numPoints; i2++) {
                double dx = dstPts.get(i2 * 2) - srcPts.get(i2 * 2);
                double dy = dstPts.get((i2 * 2) + 1) - srcPts.get((i2 * 2) + 1);
                rmse += (dx * dx) + (dy * dy);
            }
            rmse = Math.sqrt(rmse / ((double) numPoints));
            if (this.prewarp != null) {
                CvArr tempWarp = (CvMat) tempWarp3x3.get();
                opencv_core.cvInvert(this.prewarp, tempWarp);
                opencv_core.cvMatMul(totalWarp, tempWarp, totalWarp);
            }
        }
        return rmse;
    }
}
