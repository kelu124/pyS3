package org.bytedeco.javacpp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

protected class Pointer$CustomDeallocator extends Pointer$DeallocatorReference implements Pointer$Deallocator {
    Method method;
    Pointer pointer;

    public Pointer$CustomDeallocator(Pointer p) {
        super(p, null);
        this.pointer = null;
        this.method = null;
        this.deallocator = this;
        Class<? extends Pointer> cls = p.getClass();
        for (Method m : cls.getDeclaredMethods()) {
            Class[] parameters = m.getParameterTypes();
            if (Modifier.isStatic(m.getModifiers()) && m.getReturnType().equals(Void.TYPE) && m.getName().equals("deallocate") && parameters.length == 1 && Pointer.class.isAssignableFrom(parameters[0])) {
                m.setAccessible(true);
                this.method = m;
                break;
            }
        }
        if (this.method == null) {
            throw new RuntimeException(new NoSuchMethodException("static void " + cls.getCanonicalName() + ".deallocate(" + Pointer.class.getCanonicalName() + ")"));
        }
        try {
            Constructor<? extends Pointer> c = cls.getConstructor(new Class[]{Pointer.class});
            c.setAccessible(true);
            this.pointer = (Pointer) c.newInstance(new Object[]{p});
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deallocate() {
        try {
            this.method.invoke(null, new Object[]{this.pointer});
            this.pointer.setNull();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        return getClass().getName() + "[pointer=" + this.pointer + ",method=" + this.method + "]";
    }
}
