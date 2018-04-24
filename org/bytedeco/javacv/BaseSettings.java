package org.bytedeco.javacv;

import java.beans.PropertyChangeListener;
import java.beans.beancontext.BeanContextSupport;
import java.util.Arrays;

public class BaseSettings extends BeanContextSupport implements Comparable<BaseSettings> {
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcSupport.addPropertyChangeListener(listener);
        for (Object s : toArray()) {
            if (s instanceof BaseChildSettings) {
                ((BaseChildSettings) s).addPropertyChangeListener(listener);
            } else if (s instanceof BaseSettings) {
                ((BaseSettings) s).addPropertyChangeListener(listener);
            }
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcSupport.removePropertyChangeListener(listener);
        for (Object s : toArray()) {
            if (s instanceof BaseChildSettings) {
                ((BaseChildSettings) s).removePropertyChangeListener(listener);
            } else if (s instanceof BaseSettings) {
                ((BaseSettings) s).addPropertyChangeListener(listener);
            }
        }
    }

    public int compareTo(BaseSettings o) {
        return getName().compareTo(o.getName());
    }

    protected String getName() {
        return "";
    }

    public Object[] toArray() {
        Object[] a = super.toArray();
        Arrays.sort(a);
        return a;
    }

    public Object[] toArray(Object[] a) {
        a = super.toArray(a);
        Arrays.sort(a);
        return a;
    }
}
