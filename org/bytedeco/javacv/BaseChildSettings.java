package org.bytedeco.javacv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.beancontext.BeanContextChildSupport;
import java.util.ListResourceBundle;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class BaseChildSettings extends BeanContextChildSupport implements Comparable<BaseChildSettings> {

    public static class PropertyVetoExceptionThatNetBeansLikes extends PropertyVetoException implements Callable {

        class C12381 extends ListResourceBundle {
            C12381() {
            }

            protected Object[][] getContents() {
                Object[][] objArr = new Object[1][];
                objArr[0] = new Object[]{PropertyVetoExceptionThatNetBeansLikes.this.getMessage(), PropertyVetoExceptionThatNetBeansLikes.this.getMessage()};
                return objArr;
            }
        }

        public PropertyVetoExceptionThatNetBeansLikes(String mess, PropertyChangeEvent evt) {
            super(mess, evt);
        }

        public Object call() throws Exception {
            new LogRecord(Level.ALL, getMessage()).setResourceBundle(new C12381());
            return new LogRecord[]{lg};
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcSupport.removePropertyChangeListener(listener);
    }

    public int compareTo(BaseChildSettings o) {
        return getName().compareTo(o.getName());
    }

    protected String getName() {
        return "";
    }
}
