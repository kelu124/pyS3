package org.bytedeco.javacv;

import com.itextpdf.text.xml.xmp.DublinCoreProperties;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvFileNode;
import org.bytedeco.javacpp.opencv_core.CvFileStorage;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacv.ProjectiveDevice.Exception;

public class ProjectorDevice extends ProjectiveDevice {
    private static ThreadLocal<CvMat> B4x3 = CvMat.createThreadLocal(4, 3);
    private static ThreadLocal<CvMat> x23x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> x34x1 = CvMat.createThreadLocal(4, 1);
    private Settings settings;

    public interface Settings {
        void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

        int getBitDepth();

        String getDescription();

        int getImageHeight();

        int getImageWidth();

        long getLatency();

        String getName();

        int getRefreshRate();

        double getResponseGamma();

        int getScreenNumber();

        boolean isUseOpenGL();

        void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

        void setBitDepth(int i);

        void setImageHeight(int i);

        void setImageWidth(int i);

        void setLatency(long j);

        void setName(String str);

        void setRefreshRate(int i);

        void setResponseGamma(double d);

        void setScreenNumber(int i);

        void setUseOpenGL(boolean z);
    }

    public static class SettingsImplementation extends org.bytedeco.javacv.ProjectiveDevice.Settings implements Settings {
        int bitDepth;
        int imageHeight;
        int imageWidth;
        long latency;
        int refreshRate;
        int screenNumber;
        private boolean useOpenGL;

        public SettingsImplementation() {
            int i = 1;
            if (CanvasFrame.getScreenDevices().length <= 1) {
                i = 0;
            }
            this.screenNumber = i;
            this.latency = 200;
            this.imageWidth = 0;
            this.imageHeight = 0;
            this.bitDepth = 0;
            this.refreshRate = 0;
            this.useOpenGL = false;
            this.name = "Projector  0";
            setScreenNumber(this.screenNumber);
        }

        public SettingsImplementation(org.bytedeco.javacv.ProjectiveDevice.Settings settings) {
            int i = 1;
            super(settings);
            if (CanvasFrame.getScreenDevices().length <= 1) {
                i = 0;
            }
            this.screenNumber = i;
            this.latency = 200;
            this.imageWidth = 0;
            this.imageHeight = 0;
            this.bitDepth = 0;
            this.refreshRate = 0;
            this.useOpenGL = false;
            if (settings instanceof SettingsImplementation) {
                SettingsImplementation s = (SettingsImplementation) settings;
                this.screenNumber = s.screenNumber;
                this.latency = s.latency;
                this.imageWidth = s.imageWidth;
                this.imageHeight = s.imageHeight;
                this.bitDepth = s.bitDepth;
                this.refreshRate = s.refreshRate;
                this.useOpenGL = s.useOpenGL;
            }
        }

        public int getScreenNumber() {
            return this.screenNumber;
        }

        public void setScreenNumber(int screenNumber) {
            int i = 0;
            DisplayMode d = CanvasFrame.getDisplayMode(screenNumber);
            String oldDescription = getDescription();
            Integer valueOf = Integer.valueOf(this.screenNumber);
            this.screenNumber = screenNumber;
            firePropertyChange("screenNumber", valueOf, Integer.valueOf(screenNumber));
            firePropertyChange(DublinCoreProperties.DESCRIPTION, oldDescription, getDescription());
            String str = "imageWidth";
            Integer valueOf2 = Integer.valueOf(this.imageWidth);
            int width = d == null ? 0 : d.getWidth();
            this.imageWidth = width;
            firePropertyChange(str, valueOf2, Integer.valueOf(width));
            str = "imageHeight";
            valueOf2 = Integer.valueOf(this.imageHeight);
            width = d == null ? 0 : d.getHeight();
            this.imageHeight = width;
            firePropertyChange(str, valueOf2, Integer.valueOf(width));
            str = "bitDepth";
            valueOf2 = Integer.valueOf(this.bitDepth);
            width = d == null ? 0 : d.getBitDepth();
            this.bitDepth = width;
            firePropertyChange(str, valueOf2, Integer.valueOf(width));
            String str2 = "refreshRate";
            valueOf = Integer.valueOf(this.refreshRate);
            if (d != null) {
                i = d.getRefreshRate();
            }
            this.refreshRate = i;
            firePropertyChange(str2, valueOf, Integer.valueOf(i));
            Double valueOf3 = Double.valueOf(this.responseGamma);
            double gamma = CanvasFrame.getGamma(screenNumber);
            this.responseGamma = gamma;
            firePropertyChange("responseGamma", valueOf3, Double.valueOf(gamma));
        }

        public long getLatency() {
            return this.latency;
        }

        public void setLatency(long latency) {
            this.latency = latency;
        }

        public String getDescription() {
            String[] descriptions = CanvasFrame.getScreenDescriptions();
            if (descriptions == null || this.screenNumber < 0 || this.screenNumber >= descriptions.length) {
                return "";
            }
            return descriptions[this.screenNumber];
        }

        public int getImageWidth() {
            return this.imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            Integer valueOf = Integer.valueOf(this.imageWidth);
            this.imageWidth = imageWidth;
            firePropertyChange("imageWidth", valueOf, Integer.valueOf(imageWidth));
        }

        public int getImageHeight() {
            return this.imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            Integer valueOf = Integer.valueOf(this.imageHeight);
            this.imageHeight = imageHeight;
            firePropertyChange("imageHeight", valueOf, Integer.valueOf(imageHeight));
        }

        public int getBitDepth() {
            return this.bitDepth;
        }

        public void setBitDepth(int bitDepth) {
            this.bitDepth = bitDepth;
        }

        public int getRefreshRate() {
            return this.refreshRate;
        }

        public void setRefreshRate(int refreshRate) {
            this.refreshRate = refreshRate;
        }

        public boolean isUseOpenGL() {
            return this.useOpenGL;
        }

        public void setUseOpenGL(boolean useOpenGL) {
            this.useOpenGL = useOpenGL;
        }
    }

    public static class CalibratedSettings extends org.bytedeco.javacv.ProjectiveDevice.CalibratedSettings implements Settings {
        SettingsImplementation si = new C12651();

        class C12651 extends SettingsImplementation {
            C12651() {
            }

            public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
                CalibratedSettings.this.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public CalibratedSettings(org.bytedeco.javacv.ProjectiveDevice.CalibratedSettings settings) {
            super(settings);
            if (settings instanceof CalibratedSettings) {
                this.si = new SettingsImplementation(((CalibratedSettings) settings).si);
            }
        }

        public String getName() {
            return this.si.getName();
        }

        public void setName(String name) {
            this.si.setName(name);
        }

        public double getResponseGamma() {
            return this.si.getResponseGamma();
        }

        public void setResponseGamma(double responseGamma) {
            this.si.setResponseGamma(responseGamma);
        }

        public int getScreenNumber() {
            return this.si.getScreenNumber();
        }

        public void setScreenNumber(int screenNumber) {
            this.si.setScreenNumber(screenNumber);
        }

        public long getLatency() {
            return this.si.getLatency();
        }

        public void setLatency(long latency) {
            this.si.setLatency(latency);
        }

        public String getDescription() {
            return this.si.getDescription();
        }

        public int getImageWidth() {
            return this.si.getImageWidth();
        }

        public void setImageWidth(int imageWidth) {
            this.si.setImageWidth(imageWidth);
        }

        public int getImageHeight() {
            return this.si.getImageHeight();
        }

        public void setImageHeight(int imageHeight) {
            this.si.setImageHeight(imageHeight);
        }

        public int getBitDepth() {
            return this.si.getBitDepth();
        }

        public void setBitDepth(int bitDepth) {
            this.si.setBitDepth(bitDepth);
        }

        public int getRefreshRate() {
            return this.si.getRefreshRate();
        }

        public void setRefreshRate(int refreshRate) {
            this.si.setRefreshRate(refreshRate);
        }

        public boolean isUseOpenGL() {
            return this.si.isUseOpenGL();
        }

        public void setUseOpenGL(boolean useOpenGL) {
            this.si.setUseOpenGL(useOpenGL);
        }
    }

    public static class CalibrationSettings extends org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings implements Settings {
        double brightnessBackground = 0.0d;
        double brightnessForeground = 1.0d;
        SettingsImplementation si = new C12661();

        class C12661 extends SettingsImplementation {
            C12661() {
            }

            public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
                CalibrationSettings.this.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public CalibrationSettings(org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings settings) {
            super(settings);
            if (settings instanceof CalibrationSettings) {
                CalibrationSettings s = (CalibrationSettings) settings;
                this.si = new SettingsImplementation(s.si);
                this.brightnessBackground = s.brightnessBackground;
                this.brightnessForeground = s.brightnessForeground;
            }
        }

        public String getName() {
            return this.si.getName();
        }

        public void setName(String name) {
            this.si.setName(name);
        }

        public double getResponseGamma() {
            return this.si.getResponseGamma();
        }

        public void setResponseGamma(double responseGamma) {
            this.si.setResponseGamma(responseGamma);
        }

        public int getScreenNumber() {
            return this.si.getScreenNumber();
        }

        public void setScreenNumber(int screenNumber) {
            this.si.setScreenNumber(screenNumber);
        }

        public long getLatency() {
            return this.si.getLatency();
        }

        public void setLatency(long latency) {
            this.si.setLatency(latency);
        }

        public String getDescription() {
            return this.si.getDescription();
        }

        public int getImageWidth() {
            return this.si.getImageWidth();
        }

        public void setImageWidth(int imageWidth) {
            this.si.setImageWidth(imageWidth);
        }

        public int getImageHeight() {
            return this.si.getImageHeight();
        }

        public void setImageHeight(int imageHeight) {
            this.si.setImageHeight(imageHeight);
        }

        public int getBitDepth() {
            return this.si.getBitDepth();
        }

        public void setBitDepth(int bitDepth) {
            this.si.setBitDepth(bitDepth);
        }

        public int getRefreshRate() {
            return this.si.getRefreshRate();
        }

        public void setRefreshRate(int refreshRate) {
            this.si.setRefreshRate(refreshRate);
        }

        public boolean isUseOpenGL() {
            return this.si.isUseOpenGL();
        }

        public void setUseOpenGL(boolean useOpenGL) {
            this.si.setUseOpenGL(useOpenGL);
        }

        public double getBrightnessBackground() {
            return this.brightnessBackground;
        }

        public void setBrightnessBackground(double brightnessBackground) {
            Double valueOf = Double.valueOf(this.brightnessBackground);
            this.brightnessBackground = brightnessBackground;
            firePropertyChange("brightnessBackground", valueOf, Double.valueOf(brightnessBackground));
        }

        public double getBrightnessForeground() {
            return this.brightnessForeground;
        }

        public void setBrightnessForeground(double brightnessForeground) {
            Double valueOf = Double.valueOf(this.brightnessForeground);
            this.brightnessForeground = brightnessForeground;
            firePropertyChange("brightnessForeground", valueOf, Double.valueOf(brightnessForeground));
        }
    }

    public ProjectorDevice(String name) {
        super(name);
    }

    public ProjectorDevice(String name, String filename) throws Exception {
        super(name, filename);
        this.settings.setImageWidth(this.imageWidth);
        this.settings.setImageHeight(this.imageHeight);
    }

    public ProjectorDevice(String name, CvFileStorage fs) throws Exception {
        super(name, fs);
        this.settings.setImageWidth(this.imageWidth);
        this.settings.setImageHeight(this.imageHeight);
    }

    public ProjectorDevice(Settings settings) throws Exception {
        super((org.bytedeco.javacv.ProjectiveDevice.Settings) settings);
    }

    public org.bytedeco.javacv.ProjectiveDevice.Settings getSettings() {
        return (org.bytedeco.javacv.ProjectiveDevice.Settings) this.settings;
    }

    public void setSettings(Settings settings) {
        setSettings((org.bytedeco.javacv.ProjectiveDevice.Settings) settings);
    }

    public void setSettings(org.bytedeco.javacv.ProjectiveDevice.Settings settings) {
        super.setSettings(settings);
        if (settings instanceof org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings) {
            this.settings = new CalibrationSettings((org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings) settings);
        } else if (settings instanceof org.bytedeco.javacv.ProjectiveDevice.CalibratedSettings) {
            this.settings = new CalibratedSettings((org.bytedeco.javacv.ProjectiveDevice.CalibratedSettings) settings);
        } else {
            this.settings = new SettingsImplementation(settings);
        }
        if (this.settings.getName() == null || this.settings.getName().length() == 0) {
            this.settings.setName("Projector " + String.format("%2d", new Object[]{Integer.valueOf(this.settings.getScreenNumber())}));
        }
    }

    public CanvasFrame createCanvasFrame() throws CanvasFrame.Exception {
        if (this.settings.getScreenNumber() < 0) {
            return null;
        }
        DisplayMode d = new DisplayMode(this.settings.getImageWidth(), this.settings.getImageHeight(), this.settings.getBitDepth(), this.settings.getRefreshRate());
        CanvasFrame c = null;
        Throwable cause = null;
        try {
            c = (CanvasFrame) Class.forName(CanvasFrame.class.getPackage().getName() + (this.settings.isUseOpenGL() ? ".GLCanvasFrame" : ".CanvasFrame")).asSubclass(CanvasFrame.class).getConstructor(new Class[]{String.class, Integer.TYPE, DisplayMode.class, Double.TYPE}).newInstance(new Object[]{this.settings.getName(), Integer.valueOf(this.settings.getScreenNumber()), d, Double.valueOf(this.settings.getResponseGamma())});
        } catch (Throwable ex) {
            cause = ex;
        } catch (Throwable ex2) {
            cause = ex2;
        } catch (Throwable ex22) {
            cause = ex22;
        } catch (Throwable ex222) {
            cause = ex222;
        } catch (Throwable ex2222) {
            cause = ex2222;
        } catch (InvocationTargetException ex3) {
            cause = ex3.getCause();
        }
        if (cause == null) {
            c.setLatency(this.settings.getLatency());
            Dimension size = c.getCanvasSize();
            if (size.width == this.imageWidth && size.height == this.imageHeight) {
                return c;
            }
            rescale(size.width, size.height);
            return c;
        } else if (cause instanceof CanvasFrame.Exception) {
            throw ((CanvasFrame.Exception) cause);
        } else {
            throw new CanvasFrame.Exception("Failed to create CanvasFrame", cause);
        }
    }

    public double getAttenuation(double x, double y, CvMat n, double d) {
        CvArr B = (CvMat) B4x3.get();
        CvArr x2 = (CvMat) x23x1.get();
        CvArr x3 = (CvMat) x34x1.get();
        getBackProjectionMatrix(n, d, B);
        x2.put(new double[]{x, y, 1.0d});
        opencv_core.cvMatMul(B, x2, x3);
        opencv_core.cvGEMM(this.R, this.T, -1.0d, null, 0.0d, x2, 1);
        x3.rows(3);
        opencv_core.cvAddWeighted(x3, 1.0d / x3.get(3), x2, -1.0d, 0.0d, x2);
        double distance2 = opencv_core.cvDotProduct(x2, x2);
        double attenuation = (((-Math.signum(d)) * opencv_core.cvDotProduct(x2, n)) / (Math.sqrt(opencv_core.cvDotProduct(n, n)) * Math.sqrt(distance2))) / distance2;
        x3.rows(4);
        return attenuation;
    }

    public static ProjectorDevice[] read(String filename) throws Exception {
        CvFileStorage fs = CvFileStorage.open(filename, null, 0);
        ProjectorDevice[] devices = read(fs);
        fs.release();
        return devices;
    }

    public static ProjectorDevice[] read(CvFileStorage fs) throws Exception {
        CvSeq seq = opencv_core.cvGetFileNodeByName(fs, null, "Projectors").data_seq();
        int count = seq.total();
        ProjectorDevice[] devices = new ProjectorDevice[count];
        for (int i = 0; i < count; i++) {
            Pointer p = opencv_core.cvGetSeqElem(seq, i);
            if (p != null) {
                devices[i] = new ProjectorDevice(opencv_core.cvReadString(new CvFileNode(p), (String) null), fs);
            }
        }
        return devices;
    }
}
