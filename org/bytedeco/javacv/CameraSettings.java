package org.bytedeco.javacv;

import java.beans.PropertyVetoException;
import org.bytedeco.javacv.CameraDevice.CalibratedSettings;
import org.bytedeco.javacv.CameraDevice.CalibrationSettings;
import org.bytedeco.javacv.CameraDevice.Settings;

public class CameraSettings extends BaseSettings {
    boolean calibrated;
    Class<? extends FrameGrabber> frameGrabber;
    double monitorWindowsScale;

    public CameraSettings() {
        this(false);
    }

    public CameraSettings(boolean calibrated) {
        this.calibrated = false;
        this.monitorWindowsScale = 1.0d;
        this.frameGrabber = null;
        this.calibrated = calibrated;
    }

    public int getQuantity() {
        return size();
    }

    public void setQuantity(int quantity) throws PropertyVetoException {
        quantity = Math.max(1, quantity);
        Object[] a = toArray();
        int i = a.length;
        while (i > quantity) {
            remove(a[i - 1]);
            i--;
        }
        while (i < quantity) {
            Settings c = this.calibrated ? new CalibratedSettings() : new CalibrationSettings();
            c.setName("Camera " + String.format("%2d", new Object[]{Integer.valueOf(i)}));
            c.setDeviceNumber(Integer.valueOf(i));
            c.setFrameGrabber(this.frameGrabber);
            add(c);
            i++;
        }
        this.pcSupport.firePropertyChange("quantity", a.length, quantity);
    }

    public double getMonitorWindowsScale() {
        return this.monitorWindowsScale;
    }

    public void setMonitorWindowsScale(double monitorWindowsScale) {
        this.monitorWindowsScale = monitorWindowsScale;
    }

    public Class<? extends FrameGrabber> getFrameGrabber() {
        return this.frameGrabber;
    }

    public void setFrameGrabber(Class<? extends FrameGrabber> frameGrabber) {
        Class cls = this.frameGrabber;
        this.frameGrabber = frameGrabber;
        this.pcSupport.firePropertyChange("frameGrabber", cls, frameGrabber);
    }

    public Settings[] toArray() {
        return (Settings[]) toArray(new Settings[size()]);
    }
}
