package org.bytedeco.javacpp.tools;

public class Info {
    String[] annotations = null;
    String base = null;
    boolean cast = false;
    String[] cppNames = null;
    String cppText = null;
    String[] cppTypes = null;
    boolean define = false;
    boolean flatten = false;
    String[] javaNames = null;
    String javaText = null;
    String[] linePatterns = null;
    String[] pointerTypes = null;
    boolean purify = false;
    boolean skip = false;
    boolean translate = false;
    String[] valueTypes = null;
    boolean virtualize = false;

    public Info(String... cppNames) {
        this.cppNames = cppNames;
    }

    public Info(Info i) {
        String[] strArr;
        this.cppNames = i.cppNames != null ? (String[]) i.cppNames.clone() : null;
        if (i.javaNames != null) {
            strArr = (String[]) i.javaNames.clone();
        } else {
            strArr = null;
        }
        this.javaNames = strArr;
        if (i.annotations != null) {
            strArr = (String[]) i.annotations.clone();
        } else {
            strArr = null;
        }
        this.annotations = strArr;
        if (i.cppTypes != null) {
            strArr = (String[]) i.cppTypes.clone();
        } else {
            strArr = null;
        }
        this.cppTypes = strArr;
        if (i.valueTypes != null) {
            strArr = (String[]) i.valueTypes.clone();
        } else {
            strArr = null;
        }
        this.valueTypes = strArr;
        if (i.pointerTypes != null) {
            strArr = (String[]) i.pointerTypes.clone();
        } else {
            strArr = null;
        }
        this.pointerTypes = strArr;
        this.cast = i.cast;
        this.define = i.define;
        this.flatten = i.flatten;
        this.translate = i.translate;
        this.skip = i.skip;
        this.purify = i.purify;
        this.virtualize = i.virtualize;
        this.base = i.base;
        this.cppText = i.cppText;
        this.javaText = i.javaText;
    }

    public Info cppNames(String... cppNames) {
        this.cppNames = cppNames;
        return this;
    }

    public Info javaNames(String... javaNames) {
        this.javaNames = javaNames;
        return this;
    }

    public Info annotations(String... annotations) {
        this.annotations = annotations;
        return this;
    }

    public Info cppTypes(String... cppTypes) {
        this.cppTypes = cppTypes;
        return this;
    }

    public Info valueTypes(String... valueTypes) {
        this.valueTypes = valueTypes;
        return this;
    }

    public Info pointerTypes(String... pointerTypes) {
        this.pointerTypes = pointerTypes;
        return this;
    }

    public Info linePatterns(String... linePatterns) {
        this.linePatterns = linePatterns;
        return this;
    }

    public Info cast() {
        this.cast = true;
        return this;
    }

    public Info cast(boolean cast) {
        this.cast = cast;
        return this;
    }

    public Info define() {
        this.define = true;
        return this;
    }

    public Info define(boolean define) {
        this.define = define;
        return this;
    }

    public Info flatten() {
        this.flatten = true;
        return this;
    }

    public Info flatten(boolean flatten) {
        this.flatten = flatten;
        return this;
    }

    public Info translate() {
        this.translate = true;
        return this;
    }

    public Info translate(boolean translate) {
        this.translate = translate;
        return this;
    }

    public Info skip() {
        this.skip = true;
        return this;
    }

    public Info skip(boolean skip) {
        this.skip = skip;
        return this;
    }

    public Info purify() {
        this.purify = true;
        return this;
    }

    public Info purify(boolean purify) {
        this.purify = purify;
        return this;
    }

    public Info virtualize() {
        this.virtualize = true;
        return this;
    }

    public Info virtualize(boolean virtualize) {
        this.virtualize = virtualize;
        return this;
    }

    public Info base(String base) {
        this.base = base;
        return this;
    }

    public Info cppText(String cppText) {
        this.cppText = cppText;
        return this;
    }

    public Info javaText(String javaText) {
        this.javaText = javaText;
        return this;
    }
}
