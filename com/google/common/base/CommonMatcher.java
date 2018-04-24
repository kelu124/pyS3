package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher {
    abstract int end();

    abstract boolean find();

    abstract boolean find(int i);

    abstract boolean matches();

    abstract String replaceAll(String str);

    abstract int start();

    CommonMatcher() {
    }
}
