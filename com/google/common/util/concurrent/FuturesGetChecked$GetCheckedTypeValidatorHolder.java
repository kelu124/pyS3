package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

@VisibleForTesting
class FuturesGetChecked$GetCheckedTypeValidatorHolder {
    static final FuturesGetChecked$GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();
    static final String CLASS_VALUE_VALIDATOR_NAME = (FuturesGetChecked$GetCheckedTypeValidatorHolder.class.getName() + "$ClassValueValidator");

    @IgnoreJRERequirement
    enum ClassValueValidator implements FuturesGetChecked$GetCheckedTypeValidator {
        INSTANCE;
        
        private static final ClassValue<Boolean> isValidClass = null;

        static class C08291 extends ClassValue<Boolean> {
            C08291() {
            }

            protected Boolean computeValue(Class<?> type) {
                FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
                return Boolean.valueOf(true);
            }
        }

        static {
            isValidClass = new C08291();
        }

        public void validateClass(Class<? extends Exception> exceptionClass) {
            isValidClass.get(exceptionClass);
        }
    }

    enum WeakSetValidator implements FuturesGetChecked$GetCheckedTypeValidator {
        INSTANCE;
        
        private static final Set<WeakReference<Class<? extends Exception>>> validClasses = null;

        static {
            validClasses = new CopyOnWriteArraySet();
        }

        public void validateClass(Class<? extends Exception> exceptionClass) {
            for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
                if (exceptionClass.equals(knownGood.get())) {
                    return;
                }
            }
            FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
            if (validClasses.size() > 1000) {
                validClasses.clear();
            }
            validClasses.add(new WeakReference(exceptionClass));
        }
    }

    FuturesGetChecked$GetCheckedTypeValidatorHolder() {
    }

    static FuturesGetChecked$GetCheckedTypeValidator getBestValidator() {
        try {
            return (FuturesGetChecked$GetCheckedTypeValidator) Class.forName(CLASS_VALUE_VALIDATOR_NAME).getEnumConstants()[0];
        } catch (Throwable th) {
            return FuturesGetChecked.weakSetValidator();
        }
    }
}
