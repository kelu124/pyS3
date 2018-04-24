package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.bytedeco.javacpp.ARToolKitPlus;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class Marker implements Cloneable {
    private static ThreadLocal<CvMat> H3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> dstPts4x1 = CvMat.createThreadLocal(4, 1, 6, 2);
    private static IplImage[] imageCache = new IplImage[4096];
    private static final double[] src = new double[]{0.0d, 0.0d, 8.0d, 0.0d, 8.0d, 8.0d, 0.0d, 8.0d};
    private static ThreadLocal<CvMat> srcPts4x1 = CvMat.createThreadLocal(4, 1, 6, 2);
    public double confidence;
    public double[] corners;
    public int id;

    public static class ArraySettings extends BaseChildSettings {
        boolean checkered = true;
        int columns = 12;
        int rows = 8;
        double sizeX = 200.0d;
        double sizeY = 200.0d;
        double spacingX = 300.0d;
        double spacingY = 300.0d;

        public int getRows() {
            return this.rows;
        }

        public void setRows(int rows) {
            Integer valueOf = Integer.valueOf(this.rows);
            this.rows = rows;
            firePropertyChange("rows", valueOf, Integer.valueOf(rows));
        }

        public int getColumns() {
            return this.columns;
        }

        public void setColumns(int columns) {
            Integer valueOf = Integer.valueOf(this.columns);
            this.columns = columns;
            firePropertyChange("columns", valueOf, Integer.valueOf(columns));
        }

        public double getSizeX() {
            return this.sizeX;
        }

        public void setSizeX(double sizeX) {
            Double valueOf = Double.valueOf(this.sizeX);
            this.sizeX = sizeX;
            firePropertyChange("sizeX", valueOf, Double.valueOf(sizeX));
        }

        public double getSizeY() {
            return this.sizeY;
        }

        public void setSizeY(double sizeY) {
            Double valueOf = Double.valueOf(this.sizeY);
            this.sizeY = sizeY;
            firePropertyChange("sizeY", valueOf, Double.valueOf(sizeY));
        }

        public double getSpacingX() {
            return this.spacingX;
        }

        public void setSpacingX(double spacingX) {
            Double valueOf = Double.valueOf(this.spacingX);
            this.spacingX = spacingX;
            firePropertyChange("spacingX", valueOf, Double.valueOf(spacingX));
        }

        public double getSpacingY() {
            return this.spacingY;
        }

        public void setSpacingY(double spacingY) {
            Double valueOf = Double.valueOf(this.spacingY);
            this.spacingY = spacingY;
            firePropertyChange("spacingY", valueOf, Double.valueOf(spacingY));
        }

        public boolean isCheckered() {
            return this.checkered;
        }

        public void setCheckered(boolean checkered) {
            Boolean valueOf = Boolean.valueOf(this.checkered);
            this.checkered = checkered;
            firePropertyChange("checkered", valueOf, Boolean.valueOf(checkered));
        }
    }

    public Marker(int id, double[] corners, double confidence) {
        this.id = id;
        this.corners = corners;
        this.confidence = confidence;
    }

    public Marker(int id, double... corners) {
        this(id, corners, 1.0d);
    }

    public Marker clone() {
        return new Marker(this.id, (double[]) this.corners.clone(), this.confidence);
    }

    public int hashCode() {
        return ((this.id + 259) * 37) + (this.corners != null ? this.corners.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Marker)) {
            return false;
        }
        Marker m = (Marker) o;
        if (m.id == this.id && Arrays.equals(m.corners, this.corners)) {
            return true;
        }
        return false;
    }

    public double[] getCenter() {
        double x = 0.0d;
        double y = 0.0d;
        for (int i = 0; i < 4; i++) {
            x += this.corners[i * 2];
            y += this.corners[(i * 2) + 1];
        }
        y /= 4.0d;
        return new double[]{x / 4.0d, y};
    }

    public IplImage getImage() {
        return getImage(this.id);
    }

    public static IplImage getImage(int id) {
        if (imageCache[id] == null) {
            imageCache[id] = IplImage.create(8, 8, 8, 1);
            ARToolKitPlus.createImagePatternBCH(id, imageCache[id].getByteBuffer());
        }
        return imageCache[id];
    }

    public void draw(IplImage image) {
        draw(image, CvScalar.BLACK, 1.0d, null);
    }

    public void draw(IplImage image, CvScalar color, double scale, CvMat prewarp) {
        draw(image, color, scale, scale, prewarp);
    }

    public void draw(IplImage image, CvScalar color, double scaleX, double scaleY, CvMat prewarp) {
        CvArr H = (CvMat) H3x3.get();
        JavaCV.getPerspectiveTransform(src, this.corners, H);
        if (prewarp != null) {
            opencv_core.cvGEMM(prewarp, H, 1.0d, null, 0.0d, H, 0);
        }
        IplImage marker = getImage();
        ByteBuffer mbuf = marker.getByteBuffer();
        CvArr srcPts = (CvMat) srcPts4x1.get();
        CvMat dstPts = (CvMat) dstPts4x1.get();
        CvPoint cvPoint = new CvPoint(4);
        int h = marker.height();
        int w = marker.width();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (mbuf.get((y * w) + x) == (byte) 0) {
                    int i;
                    srcPts.put(new double[]{(double) x, (double) y, (double) (x + 1), (double) y, (double) (x + 1), (double) (y + 1), (double) x, (double) (y + 1)});
                    opencv_core.cvPerspectiveTransform(srcPts, dstPts, H);
                    double centerx = 0.0d;
                    double centery = 0.0d;
                    for (i = 0; i < 4; i++) {
                        centerx += dstPts.get(i * 2);
                        centery += dstPts.get((i * 2) + 1);
                    }
                    centerx /= 4.0d;
                    centery /= 4.0d;
                    for (i = 0; i < 4; i++) {
                        double a = dstPts.get(i * 2);
                        double b = dstPts.get((i * 2) + 1);
                        double dy = centery - b;
                        double dx = centerx - a < 0.0d ? -1.0d : 0.0d;
                        dy = dy < 0.0d ? -1.0d : 0.0d;
                        cvPoint.position((long) i).x((int) Math.round(((a * scaleX) + dx) * 65536.0d));
                        cvPoint.position((long) i).y((int) Math.round(((b * scaleY) + dy) * 65536.0d));
                    }
                    opencv_imgproc.cvFillConvexPoly(image, cvPoint.position(0), 4, color, 8, 16);
                }
            }
        }
    }

    public static Marker[][] createArray(ArraySettings settings) {
        return createArray(settings, 0.0d, 0.0d);
    }

    public static Marker[][] createArray(ArraySettings settings, double marginx, double marginy) {
        Marker[] markers = new Marker[(settings.rows * settings.columns)];
        int id = 0;
        for (int y = 0; y < settings.rows; y++) {
            for (int x = 0; x < settings.columns; x++) {
                double cx = ((((double) x) * settings.spacingX) + (settings.sizeX / 2.0d)) + marginx;
                double cy = ((((double) y) * settings.spacingY) + (settings.sizeY / 2.0d)) + marginy;
                markers[id] = new Marker(id, new double[]{cx - (settings.sizeX / 2.0d), cy - (settings.sizeY / 2.0d), cx + (settings.sizeX / 2.0d), cy - (settings.sizeY / 2.0d), cx + (settings.sizeX / 2.0d), cy + (settings.sizeY / 2.0d), cx - (settings.sizeX / 2.0d), cy + (settings.sizeY / 2.0d)}, 1.0d);
                id++;
            }
        }
        if (settings.checkered) {
            Marker[] markers1 = new Marker[(markers.length / 2)];
            Marker[] markers2 = new Marker[(markers.length / 2)];
            for (int i = 0; i < markers.length; i++) {
                if ((((i % settings.columns) % 2 == 0 ? 1 : 0) ^ ((i / settings.columns) % 2 == 0 ? 1 : 0)) != 0) {
                    markers2[i / 2] = markers[i];
                } else {
                    markers1[i / 2] = markers[i];
                }
            }
            return new Marker[][]{markers2, markers1};
        }
        return new Marker[][]{markers};
    }

    public static Marker[][] createArray(int rows, int columns, double sizeX, double sizeY, double spacingX, double spacingY, boolean checkered, double marginx, double marginy) {
        ArraySettings s = new ArraySettings();
        s.rows = rows;
        s.columns = columns;
        s.sizeX = sizeX;
        s.sizeY = sizeY;
        s.spacingX = spacingX;
        s.spacingY = spacingY;
        s.checkered = checkered;
        return createArray(s, marginx, marginy);
    }

    public static void applyWarp(Marker[] markers, CvMat warp) {
        CvMat pts = (CvMat) srcPts4x1.get();
        for (Marker m : markers) {
            opencv_core.cvPerspectiveTransform(pts.put(m.corners), pts, warp);
            pts.get(m.corners);
        }
    }

    public String toString() {
        return "[" + this.id + ": (" + this.corners[0] + ", " + this.corners[1] + ") (" + this.corners[2] + ", " + this.corners[3] + ") (" + this.corners[4] + ", " + this.corners[5] + ") (" + this.corners[6] + ", " + this.corners[7] + ")]";
    }
}
