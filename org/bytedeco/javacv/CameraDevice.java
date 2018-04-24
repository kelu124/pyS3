package org.bytedeco.javacv;

import com.itextpdf.text.xml.xmp.DublinCoreProperties;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvFileNode;
import org.bytedeco.javacpp.opencv_core.CvFileStorage;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacv.BaseChildSettings.PropertyVetoExceptionThatNetBeansLikes;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.ProjectiveDevice.Exception;

public class CameraDevice extends ProjectiveDevice {
    private Settings settings;

    public interface Settings {
        void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

        int getBitsPerPixel();

        String getDescription();

        File getDeviceFile();

        String getDeviceFilename();

        Integer getDeviceNumber();

        String getDevicePath();

        String getFormat();

        Class<? extends FrameGrabber> getFrameGrabber();

        double getFrameRate();

        int getImageHeight();

        ImageMode getImageMode();

        int getImageWidth();

        String getName();

        int getNumBuffers();

        double getResponseGamma();

        int getTimeout();

        boolean isDeinterlace();

        boolean isTriggerMode();

        void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

        void setBitsPerPixel(int i);

        void setDeinterlace(boolean z);

        void setDeviceFile(File file) throws PropertyVetoException;

        void setDeviceFilename(String str) throws PropertyVetoException;

        void setDeviceNumber(Integer num) throws PropertyVetoException;

        void setDevicePath(String str) throws PropertyVetoException;

        void setFormat(String str);

        void setFrameGrabber(Class<? extends FrameGrabber> cls);

        void setFrameRate(double d);

        void setImageHeight(int i);

        void setImageMode(ImageMode imageMode);

        void setImageWidth(int i);

        void setName(String str);

        void setNumBuffers(int i);

        void setResponseGamma(double d);

        void setTimeout(int i);

        void setTriggerMode(boolean z);
    }

    public static class SettingsImplementation extends org.bytedeco.javacv.ProjectiveDevice.Settings implements Settings {
        int bpp;
        boolean deinterlace;
        File deviceFile;
        Integer deviceNumber;
        String devicePath;
        String format;
        Class<? extends FrameGrabber> frameGrabber;
        double frameRate;
        int imageHeight;
        ImageMode imageMode;
        int imageWidth;
        int numBuffers;
        int timeout;
        boolean triggerMode;

        public SettingsImplementation() {
            this.deviceNumber = null;
            this.deviceFile = null;
            this.devicePath = null;
            this.frameGrabber = null;
            this.format = "";
            this.imageWidth = 0;
            this.imageHeight = 0;
            this.frameRate = 0.0d;
            this.triggerMode = false;
            this.bpp = 0;
            this.imageMode = ImageMode.COLOR;
            this.timeout = 10000;
            this.numBuffers = 4;
            this.deinterlace = false;
            this.name = "Camera  0";
        }

        public SettingsImplementation(org.bytedeco.javacv.ProjectiveDevice.Settings settings) {
            super(settings);
            this.deviceNumber = null;
            this.deviceFile = null;
            this.devicePath = null;
            this.frameGrabber = null;
            this.format = "";
            this.imageWidth = 0;
            this.imageHeight = 0;
            this.frameRate = 0.0d;
            this.triggerMode = false;
            this.bpp = 0;
            this.imageMode = ImageMode.COLOR;
            this.timeout = 10000;
            this.numBuffers = 4;
            this.deinterlace = false;
            if (settings instanceof SettingsImplementation) {
                SettingsImplementation s = (SettingsImplementation) settings;
                this.deviceNumber = s.deviceNumber;
                this.deviceFile = s.deviceFile;
                this.devicePath = s.devicePath;
                this.frameGrabber = s.frameGrabber;
                this.format = s.format;
                this.imageWidth = s.imageWidth;
                this.imageHeight = s.imageHeight;
                this.frameRate = s.frameRate;
                this.triggerMode = s.triggerMode;
                this.bpp = s.bpp;
                this.imageMode = s.imageMode;
                this.timeout = s.timeout;
                this.numBuffers = s.numBuffers;
                this.deinterlace = s.deinterlace;
            }
        }

        public Integer getDeviceNumber() {
            return this.deviceNumber;
        }

        public void setDeviceNumber(Integer deviceNumber) throws PropertyVetoException {
            if (deviceNumber != null) {
                try {
                    if (this.frameGrabber != null) {
                        try {
                            this.frameGrabber.getConstructor(new Class[]{Integer.TYPE});
                        } catch (NoSuchMethodException e) {
                            this.frameGrabber.getConstructor(new Class[]{Integer.class});
                        }
                    }
                    setDevicePath(null);
                    setDeviceFile(null);
                } catch (NoSuchMethodException e2) {
                    String str = this.frameGrabber.getSimpleName() + " does not accept a deviceNumber.";
                    Integer num = this.deviceNumber;
                    this.deviceNumber = null;
                    throw new PropertyVetoExceptionThatNetBeansLikes(str, new PropertyChangeEvent(this, "deviceNumber", num, null));
                }
            }
            String oldDescription = getDescription();
            Integer num2 = this.deviceNumber;
            this.deviceNumber = deviceNumber;
            firePropertyChange("deviceNumber", num2, deviceNumber);
            firePropertyChange(DublinCoreProperties.DESCRIPTION, oldDescription, getDescription());
        }

        public File getDeviceFile() {
            return this.deviceFile;
        }

        public void setDeviceFile(File deviceFile) throws PropertyVetoException {
            if (deviceFile != null) {
                try {
                    if (this.frameGrabber != null) {
                        this.frameGrabber.getConstructor(new Class[]{File.class});
                    }
                    setDeviceNumber(null);
                    setDevicePath(null);
                } catch (NoSuchMethodException e) {
                    String str = this.frameGrabber.getSimpleName() + " does not accept a deviceFile.";
                    File file = this.deviceFile;
                    this.deviceFile = null;
                    throw new PropertyVetoExceptionThatNetBeansLikes(str, new PropertyChangeEvent(this, "deviceFile", file, null));
                }
            }
            String oldDescription = getDescription();
            File file2 = this.deviceFile;
            this.deviceFile = deviceFile;
            firePropertyChange("deviceFile", file2, deviceFile);
            firePropertyChange(DublinCoreProperties.DESCRIPTION, oldDescription, getDescription());
        }

        public String getDeviceFilename() {
            return getDeviceFile() == null ? "" : getDeviceFile().getPath();
        }

        public void setDeviceFilename(String deviceFilename) throws PropertyVetoException {
            File file = (deviceFilename == null || deviceFilename.length() == 0) ? null : new File(deviceFilename);
            setDeviceFile(file);
        }

        public String getDevicePath() {
            return this.devicePath;
        }

        public void setDevicePath(String devicePath) throws PropertyVetoException {
            String str;
            if (devicePath != null) {
                try {
                    if (this.frameGrabber != null) {
                        this.frameGrabber.getConstructor(new Class[]{String.class});
                    }
                    setDeviceNumber(null);
                    setDeviceFile(null);
                } catch (NoSuchMethodException e) {
                    devicePath = "";
                    str = this.frameGrabber.getSimpleName() + " does not accept a devicePath.";
                    String str2 = this.devicePath;
                    this.devicePath = null;
                    throw new PropertyVetoExceptionThatNetBeansLikes(str, new PropertyChangeEvent(this, "devicePath", str2, null));
                }
            }
            String oldDescription = getDescription();
            str = this.devicePath;
            this.devicePath = devicePath;
            firePropertyChange("devicePath", str, devicePath);
            firePropertyChange(DublinCoreProperties.DESCRIPTION, oldDescription, getDescription());
        }

        public Class<? extends FrameGrabber> getFrameGrabber() {
            return this.frameGrabber;
        }

        public void setFrameGrabber(Class<? extends FrameGrabber> frameGrabber) {
            String oldDescription = getDescription();
            Class cls = this.frameGrabber;
            this.frameGrabber = frameGrabber;
            firePropertyChange("frameGrabber", cls, frameGrabber);
            firePropertyChange(DublinCoreProperties.DESCRIPTION, oldDescription, getDescription());
            if (frameGrabber == null) {
                Integer num = this.deviceNumber;
                this.deviceNumber = null;
                firePropertyChange("deviceNumber", num, null);
                File file = this.deviceFile;
                this.deviceFile = null;
                firePropertyChange("deviceFile", file, null);
                String str = this.devicePath;
                this.devicePath = null;
                firePropertyChange("devicePath", str, null);
                return;
            }
            boolean hasDeviceNumber = false;
            try {
                frameGrabber.getConstructor(new Class[]{Integer.TYPE});
                hasDeviceNumber = true;
            } catch (NoSuchMethodException e) {
                try {
                    frameGrabber.getConstructor(new Class[]{Integer.class});
                    hasDeviceNumber = true;
                } catch (NoSuchMethodException e2) {
                    num = this.deviceNumber;
                    this.deviceNumber = null;
                    firePropertyChange("deviceNumber", num, null);
                }
            }
            try {
                frameGrabber.getConstructor(new Class[]{File.class});
            } catch (NoSuchMethodException e3) {
                file = this.deviceFile;
                this.deviceFile = null;
                firePropertyChange("deviceFile", file, null);
            }
            try {
                frameGrabber.getConstructor(new Class[]{String.class});
            } catch (NoSuchMethodException e4) {
                str = this.devicePath;
                this.devicePath = null;
                firePropertyChange("devicePath", str, null);
            }
            if (hasDeviceNumber && this.deviceNumber == null && this.deviceFile == null && this.devicePath == null) {
                try {
                    setDeviceNumber(Integer.valueOf(0));
                } catch (PropertyVetoException e5) {
                }
            }
        }

        public String getDescription() {
            String[] descriptions = null;
            try {
                descriptions = (String[]) this.frameGrabber.getMethod("getDeviceDescriptions", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
            }
            if (descriptions == null || this.deviceNumber == null || this.deviceNumber.intValue() >= descriptions.length) {
                return "";
            }
            return descriptions[this.deviceNumber.intValue()];
        }

        public String getFormat() {
            return this.format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getImageWidth() {
            return this.imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public int getImageHeight() {
            return this.imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }

        public double getFrameRate() {
            return this.frameRate;
        }

        public void setFrameRate(double frameRate) {
            this.frameRate = frameRate;
        }

        public boolean isTriggerMode() {
            return this.triggerMode;
        }

        public void setTriggerMode(boolean triggerMode) {
            this.triggerMode = triggerMode;
        }

        public int getBitsPerPixel() {
            return this.bpp;
        }

        public void setBitsPerPixel(int bitsPerPixel) {
            this.bpp = bitsPerPixel;
        }

        public ImageMode getImageMode() {
            return this.imageMode;
        }

        public void setImageMode(ImageMode imageMode) {
            this.imageMode = imageMode;
        }

        public int getTimeout() {
            return this.timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getNumBuffers() {
            return this.numBuffers;
        }

        public void setNumBuffers(int numBuffers) {
            this.numBuffers = numBuffers;
        }

        public boolean isDeinterlace() {
            return this.deinterlace;
        }

        public void setDeinterlace(boolean deinterlace) {
            this.deinterlace = deinterlace;
        }
    }

    public static class CalibratedSettings extends org.bytedeco.javacv.ProjectiveDevice.CalibratedSettings implements Settings {
        SettingsImplementation si = new C12391();

        class C12391 extends SettingsImplementation {
            C12391() {
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

        public Integer getDeviceNumber() {
            return this.si.getDeviceNumber();
        }

        public void setDeviceNumber(Integer deviceNumber) throws PropertyVetoException {
            this.si.setDeviceNumber(deviceNumber);
        }

        public File getDeviceFile() {
            return this.si.getDeviceFile();
        }

        public void setDeviceFile(File deviceFile) throws PropertyVetoException {
            this.si.setDeviceFile(deviceFile);
        }

        public String getDeviceFilename() {
            return this.si.getDeviceFilename();
        }

        public void setDeviceFilename(String deviceFilename) throws PropertyVetoException {
            this.si.setDeviceFilename(deviceFilename);
        }

        public String getDevicePath() {
            return this.si.getDevicePath();
        }

        public void setDevicePath(String devicePath) throws PropertyVetoException {
            this.si.setDevicePath(devicePath);
        }

        public Class<? extends FrameGrabber> getFrameGrabber() {
            return this.si.getFrameGrabber();
        }

        public void setFrameGrabber(Class<? extends FrameGrabber> frameGrabber) {
            this.si.setFrameGrabber(frameGrabber);
        }

        public String getDescription() {
            return this.si.getDescription();
        }

        public String getFormat() {
            return this.si.getFormat();
        }

        public void setFormat(String format) {
            this.si.setFormat(format);
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

        public double getFrameRate() {
            return this.si.getFrameRate();
        }

        public void setFrameRate(double frameRate) {
            this.si.setFrameRate(frameRate);
        }

        public boolean isTriggerMode() {
            return this.si.isTriggerMode();
        }

        public void setTriggerMode(boolean triggerMode) {
            this.si.setTriggerMode(triggerMode);
        }

        public int getBitsPerPixel() {
            return this.si.getBitsPerPixel();
        }

        public void setBitsPerPixel(int bitsPerPixel) {
            this.si.setBitsPerPixel(bitsPerPixel);
        }

        public ImageMode getImageMode() {
            return this.si.getImageMode();
        }

        public void setImageMode(ImageMode imageMode) {
            this.si.setImageMode(imageMode);
        }

        public int getTimeout() {
            return this.si.getTimeout();
        }

        public void setTimeout(int timeout) {
            this.si.setTimeout(timeout);
        }

        public int getNumBuffers() {
            return this.si.getNumBuffers();
        }

        public void setNumBuffers(int numBuffers) {
            this.si.setNumBuffers(numBuffers);
        }

        public boolean isDeinterlace() {
            return this.si.isDeinterlace();
        }

        public void setDeinterlace(boolean deinterlace) {
            this.si.setDeinterlace(deinterlace);
        }
    }

    public static class CalibrationSettings extends org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings implements Settings {
        SettingsImplementation si = new C12401();

        class C12401 extends SettingsImplementation {
            C12401() {
            }

            public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
                CalibrationSettings.this.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public CalibrationSettings(org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings settings) {
            super(settings);
            if (settings instanceof CalibrationSettings) {
                this.si = new SettingsImplementation(((CalibrationSettings) settings).si);
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

        public Integer getDeviceNumber() {
            return this.si.getDeviceNumber();
        }

        public void setDeviceNumber(Integer deviceNumber) throws PropertyVetoException {
            this.si.setDeviceNumber(deviceNumber);
        }

        public File getDeviceFile() {
            return this.si.getDeviceFile();
        }

        public void setDeviceFile(File deviceFile) throws PropertyVetoException {
            this.si.setDeviceFile(deviceFile);
        }

        public String getDeviceFilename() {
            return this.si.getDeviceFilename();
        }

        public void setDeviceFilename(String deviceFilename) throws PropertyVetoException {
            this.si.setDeviceFilename(deviceFilename);
        }

        public String getDevicePath() {
            return this.si.getDevicePath();
        }

        public void setDevicePath(String devicePath) throws PropertyVetoException {
            this.si.setDevicePath(devicePath);
        }

        public Class<? extends FrameGrabber> getFrameGrabber() {
            return this.si.getFrameGrabber();
        }

        public void setFrameGrabber(Class<? extends FrameGrabber> frameGrabber) {
            this.si.setFrameGrabber(frameGrabber);
        }

        public String getDescription() {
            return this.si.getDescription();
        }

        public String getFormat() {
            return this.si.getFormat();
        }

        public void setFormat(String format) {
            this.si.setFormat(format);
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

        public double getFrameRate() {
            return this.si.getFrameRate();
        }

        public void setFrameRate(double frameRate) {
            this.si.setFrameRate(frameRate);
        }

        public boolean isTriggerMode() {
            return this.si.isTriggerMode();
        }

        public void setTriggerMode(boolean triggerMode) {
            this.si.setTriggerMode(triggerMode);
        }

        public int getBitsPerPixel() {
            return this.si.getBitsPerPixel();
        }

        public void setBitsPerPixel(int bitsPerPixel) {
            this.si.setBitsPerPixel(bitsPerPixel);
        }

        public ImageMode getImageMode() {
            return this.si.getImageMode();
        }

        public void setImageMode(ImageMode imageMode) {
            this.si.setImageMode(imageMode);
        }

        public int getTimeout() {
            return this.si.getTimeout();
        }

        public void setTimeout(int timeout) {
            this.si.setTimeout(timeout);
        }

        public int getNumBuffers() {
            return this.si.getNumBuffers();
        }

        public void setNumBuffers(int numBuffers) {
            this.si.setNumBuffers(numBuffers);
        }

        public boolean isDeinterlace() {
            return this.si.isDeinterlace();
        }

        public void setDeinterlace(boolean deinterlace) {
            this.si.setDeinterlace(deinterlace);
        }
    }

    public CameraDevice(String name) {
        super(name);
    }

    public CameraDevice(String name, String filename) throws Exception {
        super(name, filename);
        this.settings.setImageWidth(this.imageWidth);
        this.settings.setImageHeight(this.imageHeight);
    }

    public CameraDevice(String name, CvFileStorage fs) throws Exception {
        super(name, fs);
        this.settings.setImageWidth(this.imageWidth);
        this.settings.setImageHeight(this.imageHeight);
    }

    public CameraDevice(Settings settings) throws Exception {
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
            this.settings.setName("Camera " + String.format("%2d", new Object[]{this.settings.getDeviceNumber()}));
        }
    }

    public FrameGrabber createFrameGrabber() throws FrameGrabber.Exception {
        FrameGrabber f;
        Throwable t;
        int number = 0;
        try {
            this.settings.getFrameGrabber().getMethod("tryLoad", new Class[0]).invoke(null, new Object[0]);
            if (this.settings.getDeviceFile() != null) {
                f = (FrameGrabber) this.settings.getFrameGrabber().getConstructor(new Class[]{File.class}).newInstance(new Object[]{this.settings.getDeviceFile()});
            } else if (this.settings.getDevicePath() == null || this.settings.getDevicePath().length() <= 0) {
                if (this.settings.getDeviceNumber() != null) {
                    number = this.settings.getDeviceNumber().intValue();
                }
                f = (FrameGrabber) this.settings.getFrameGrabber().getConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(number)});
            } else {
                f = (FrameGrabber) this.settings.getFrameGrabber().getConstructor(new Class[]{String.class}).newInstance(new Object[]{this.settings.getDevicePath()});
            }
        } catch (NoSuchMethodException e) {
            f = (FrameGrabber) this.settings.getFrameGrabber().getConstructor(new Class[]{Integer.class}).newInstance(new Object[]{Integer.valueOf(number)});
        } catch (Throwable th) {
            t = th;
            if (t instanceof InvocationTargetException) {
                t = ((InvocationTargetException) t).getCause();
            }
            if (t instanceof FrameGrabber.Exception) {
                FrameGrabber.Exception t2 = (FrameGrabber.Exception) t;
            } else {
                FrameGrabber.Exception exception = new FrameGrabber.Exception("Failed to create " + this.settings.getFrameGrabber(), t);
            }
        }
        f.setFormat(this.settings.getFormat());
        f.setImageWidth(this.settings.getImageWidth());
        f.setImageHeight(this.settings.getImageHeight());
        f.setFrameRate(this.settings.getFrameRate());
        f.setTriggerMode(this.settings.isTriggerMode());
        f.setBitsPerPixel(this.settings.getBitsPerPixel());
        f.setImageMode(this.settings.getImageMode());
        f.setTimeout(this.settings.getTimeout());
        f.setNumBuffers(this.settings.getNumBuffers());
        f.setGamma(this.settings.getResponseGamma());
        f.setDeinterlace(this.settings.isDeinterlace());
        return f;
    }

    public static CameraDevice[] read(String filename) throws Exception {
        CvFileStorage fs = CvFileStorage.open(filename, null, 0);
        CameraDevice[] devices = read(fs);
        fs.release();
        return devices;
    }

    public static CameraDevice[] read(CvFileStorage fs) throws Exception {
        CvSeq seq = opencv_core.cvGetFileNodeByName(fs, null, "Cameras").data_seq();
        int count = seq.total();
        CameraDevice[] devices = new CameraDevice[count];
        for (int i = 0; i < count; i++) {
            Pointer p = opencv_core.cvGetSeqElem(seq, i);
            if (p != null) {
                devices[i] = new CameraDevice(opencv_core.cvReadString(new CvFileNode(p), (String) null), fs);
            }
        }
        return devices;
    }
}
