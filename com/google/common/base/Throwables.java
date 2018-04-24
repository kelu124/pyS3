package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Throwables {
    @GwtIncompatible
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
    @GwtIncompatible
    @VisibleForTesting
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    @GwtIncompatible
    @Nullable
    private static final Method getStackTraceDepthMethod;
    @GwtIncompatible
    @Nullable
    private static final Method getStackTraceElementMethod = (jla == null ? null : getGetMethod());
    @GwtIncompatible
    @Nullable
    private static final Object jla = getJLA();

    private Throwables() {
    }

    @GwtIncompatible
    public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws Throwable {
        Preconditions.checkNotNull(throwable);
        if (declaredType.isInstance(throwable)) {
            throw ((Throwable) declaredType.cast(throwable));
        }
    }

    @GwtIncompatible
    @Deprecated
    public static <X extends Throwable> void propagateIfInstanceOf(@Nullable Throwable throwable, Class<X> declaredType) throws Throwable {
        if (throwable != null) {
            throwIfInstanceOf(throwable, declaredType);
        }
    }

    public static void throwIfUnchecked(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        if (throwable instanceof RuntimeException) {
            throw ((RuntimeException) throwable);
        } else if (throwable instanceof Error) {
            throw ((Error) throwable);
        }
    }

    @GwtIncompatible
    @Deprecated
    public static void propagateIfPossible(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwIfUnchecked(throwable);
        }
    }

    @GwtIncompatible
    public static <X extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X> declaredType) throws Throwable {
        propagateIfInstanceOf(throwable, declaredType);
        propagateIfPossible(throwable);
    }

    @GwtIncompatible
    public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws Throwable, Throwable {
        Preconditions.checkNotNull(declaredType2);
        propagateIfInstanceOf(throwable, declaredType1);
        propagateIfPossible(throwable, declaredType2);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Deprecated
    public static RuntimeException propagate(Throwable throwable) {
        throwIfUnchecked(throwable);
        throw new RuntimeException(throwable);
    }

    public static Throwable getRootCause(Throwable throwable) {
        while (true) {
            Throwable cause = throwable.getCause();
            if (cause == null) {
                return throwable;
            }
            throwable = cause;
        }
    }

    @Beta
    public static List<Throwable> getCausalChain(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        List<Throwable> causes = new ArrayList(4);
        while (throwable != null) {
            causes.add(throwable);
            throwable = throwable.getCause();
        }
        return Collections.unmodifiableList(causes);
    }

    @GwtIncompatible
    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    @GwtIncompatible
    @Beta
    public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
        return lazyStackTraceIsLazy() ? jlaStackTrace(throwable) : Collections.unmodifiableList(Arrays.asList(throwable.getStackTrace()));
    }

    @GwtIncompatible
    @Beta
    public static boolean lazyStackTraceIsLazy() {
        int i = 1;
        int i2 = getStackTraceElementMethod != null ? 1 : 0;
        if (getStackTraceDepthMethod == null) {
            i = 0;
        }
        return i2 & i;
    }

    @GwtIncompatible
    private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
        Preconditions.checkNotNull(t);
        return new AbstractList<StackTraceElement>() {
            public StackTraceElement get(int n) {
                return (StackTraceElement) Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, t, Integer.valueOf(n));
            }

            public int size() {
                return ((Integer) Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, t)).intValue();
            }
        };
    }

    @GwtIncompatible
    private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params) {
        try {
            return method.invoke(receiver, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw propagate(e2.getCause());
        }
    }

    static {
        Method method = null;
        if (jla != null) {
            method = getSizeMethod();
        }
        getStackTraceDepthMethod = method;
    }

    @GwtIncompatible
    @Nullable
    private static Object getJLA() {
        Object obj = null;
        try {
            obj = Class.forName(SHARED_SECRETS_CLASSNAME, false, null).getMethod("getJavaLangAccess", new Class[0]).invoke(null, new Object[0]);
        } catch (ThreadDeath death) {
            throw death;
        } catch (Throwable th) {
        }
        return obj;
    }

    @GwtIncompatible
    @Nullable
    private static Method getGetMethod() {
        return getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
    }

    @GwtIncompatible
    @Nullable
    private static Method getSizeMethod() {
        return getJlaMethod("getStackTraceDepth", Throwable.class);
    }

    @GwtIncompatible
    @Nullable
    private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
        Method method = null;
        try {
            method = Class.forName(JAVA_LANG_ACCESS_CLASSNAME, false, null).getMethod(name, parameterTypes);
        } catch (ThreadDeath death) {
            throw death;
        } catch (Throwable th) {
        }
        return method;
    }
}
