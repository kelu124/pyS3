package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;

@GwtCompatible
public abstract class Escaper {
    private final Function<String, String> asFunction = new C06851();

    class C06851 implements Function<String, String> {
        C06851() {
        }

        public String apply(String from) {
            return Escaper.this.escape(from);
        }
    }

    public abstract String escape(String str);

    protected Escaper() {
    }

    public final Function<String, String> asFunction() {
        return this.asFunction;
    }
}
