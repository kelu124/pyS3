package org.bytedeco.javacv;

import java.beans.PropertyChangeListener;
import org.bytedeco.javacv.ProjectorDevice.CalibratedSettings;
import org.bytedeco.javacv.ProjectorDevice.CalibrationSettings;
import org.bytedeco.javacv.ProjectorDevice.Settings;

public class ProjectorSettings extends BaseSettings {
    boolean calibrated;

    public ProjectorSettings() {
        this(false);
    }

    public ProjectorSettings(boolean calibrated) {
        this.calibrated = false;
        this.calibrated = calibrated;
    }

    public int getQuantity() {
        return size();
    }

    public void setQuantity(int quantity) {
        Object[] a = toArray();
        int i = a.length;
        while (i > quantity) {
            remove(a[i - 1]);
            i--;
        }
        while (i < quantity) {
            Settings c = this.calibrated ? new CalibratedSettings() : new CalibrationSettings();
            c.setName("Projector " + String.format("%2d", new Object[]{Integer.valueOf(i)}));
            c.setScreenNumber(c.getScreenNumber() + i);
            add(c);
            for (PropertyChangeListener l : this.pcSupport.getPropertyChangeListeners()) {
                ((BaseChildSettings) c).addPropertyChangeListener(l);
            }
            i++;
        }
        this.pcSupport.firePropertyChange("quantity", a.length, quantity);
    }

    public Settings[] toArray() {
        return (Settings[]) toArray(new Settings[size()]);
    }
}
