package org.bytedeco.javacpp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import org.bytedeco.javacpp.annotation.Platform;

public class ClassProperties extends HashMap<String, List<String>> {
    String[] defaultNames;
    List<Class> effectiveClasses;
    List<Class> inheritedClasses;
    boolean loaded;
    String pathSeparator;
    String platform;
    String platformRoot;

    public ClassProperties() {
        this.defaultNames = new String[0];
        this.inheritedClasses = null;
        this.effectiveClasses = null;
        this.loaded = false;
    }

    public ClassProperties(Properties properties) {
        this.defaultNames = new String[0];
        this.inheritedClasses = null;
        this.effectiveClasses = null;
        this.loaded = false;
        this.platform = properties.getProperty("platform");
        this.platformRoot = properties.getProperty("platform.root");
        this.pathSeparator = properties.getProperty("platform.path.separator");
        if (this.platformRoot == null || this.platformRoot.length() == 0) {
            this.platformRoot = ".";
        }
        if (!this.platformRoot.endsWith(File.separator)) {
            this.platformRoot += File.separator;
        }
        for (Entry e : properties.entrySet()) {
            String k = (String) e.getKey();
            String v = (String) e.getValue();
            if (!(v == null || v.length() == 0)) {
                if (k.equals("platform.includepath") || k.equals("platform.include") || k.equals("platform.linkpath") || k.equals("platform.link") || k.equals("platform.preloadpath") || k.equals("platform.preload") || k.equals("platform.frameworkpath") || k.equals("platform.framework") || k.equals("platform.library.suffix")) {
                    addAll(k, v.split(this.pathSeparator));
                } else {
                    setProperty(k, v);
                }
            }
        }
    }

    public List<String> get(String key) {
        List<String> list = (List) super.get(key);
        if (list != null) {
            return list;
        }
        list = new ArrayList();
        put(key, list);
        return list;
    }

    public void addAll(String key, String... values) {
        if (values != null) {
            addAll(key, Arrays.asList(values));
        }
    }

    public void addAll(String key, Collection<String> values) {
        if (values != null) {
            String root = null;
            if (key.equals("platform.compiler") || key.equals("platform.sysroot") || key.equals("platform.includepath") || key.equals("platform.linkpath")) {
                root = this.platformRoot;
            }
            List<String> values2 = get(key);
            for (String value : values) {
                String value2;
                if (value2 != null) {
                    if (!(root == null || new File(value2).isAbsolute() || !new File(root + value2).exists())) {
                        value2 = root + value2;
                    }
                    if (!values2.contains(value2)) {
                        values2.add(value2);
                    }
                }
            }
        }
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        List<String> values = get(key);
        return values.isEmpty() ? defaultValue : (String) values.get(0);
    }

    public String setProperty(String key, String value) {
        List<String> values = get(key);
        String oldValue = values.isEmpty() ? null : (String) values.get(0);
        values.clear();
        addAll(key, value);
        return oldValue;
    }

    public void load(Class cls, boolean inherit) {
        String[][] names;
        Class<?> c = Loader.getEnclosingClass(cls);
        List<Class> classList = new ArrayList();
        classList.add(0, c);
        while (!c.isAnnotationPresent(org.bytedeco.javacpp.annotation.Properties.class) && !c.isAnnotationPresent(Platform.class) && c.getSuperclass() != null && c.getSuperclass() != Object.class) {
            c = c.getSuperclass();
            classList.add(0, c);
        }
        if (this.effectiveClasses == null) {
            this.effectiveClasses = classList;
        }
        org.bytedeco.javacpp.annotation.Properties classProperties = (org.bytedeco.javacpp.annotation.Properties) c.getAnnotation(org.bytedeco.javacpp.annotation.Properties.class);
        Platform[] platforms = null;
        if (classProperties == null) {
            if (((Platform) c.getAnnotation(Platform.class)) != null) {
                platforms = new Platform[]{(Platform) c.getAnnotation(Platform.class)};
            }
        } else {
            Class[] classes = classProperties.inherit();
            if (inherit && classes != null) {
                if (this.inheritedClasses == null) {
                    this.inheritedClasses = new ArrayList();
                }
                for (Class c2 : classes) {
                    load(c2, inherit);
                    if (!this.inheritedClasses.contains(c2)) {
                        this.inheritedClasses.add(c2);
                    }
                }
            }
            if (classProperties.target().length() > 0) {
                addAll("target", classProperties.target());
            }
            if (classProperties.helper().length() > 0) {
                addAll("helper", classProperties.helper());
            }
            names = classProperties.names();
            if (names.length > 0) {
                this.defaultNames = names;
            }
            platforms = classProperties.value();
        }
        String[] pragma = new String[0];
        String[] define = new String[0];
        String[] include = new String[0];
        String[] cinclude = new String[0];
        String[] includepath = new String[0];
        String[] compiler = new String[0];
        String[] linkpath = new String[0];
        String[] link = new String[0];
        String[] frameworkpath = new String[0];
        String[] framework = new String[0];
        String[] preloadpath = new String[0];
        String[] preload = new String[0];
        String library = "jni" + c.getSimpleName();
        for (Platform p : platforms != null ? platforms : new Platform[0]) {
            names = new String[2][];
            names[0] = p.value().length > 0 ? p.value() : this.defaultNames;
            names[1] = p.not();
            boolean[] zArr = new boolean[2];
            zArr = new boolean[]{false, false};
            for (int i = 0; i < names.length; i++) {
                for (String s : names[i]) {
                    if (this.platform.startsWith(s)) {
                        zArr[i] = true;
                        break;
                    }
                }
            }
            if ((names[0].length == 0 || zArr[0]) && (names[1].length == 0 || !zArr[1])) {
                if (p.pragma().length > 0) {
                    pragma = p.pragma();
                }
                if (p.define().length > 0) {
                    define = p.define();
                }
                if (p.include().length > 0) {
                    include = p.include();
                }
                if (p.cinclude().length > 0) {
                    cinclude = p.cinclude();
                }
                if (p.includepath().length > 0) {
                    includepath = p.includepath();
                }
                if (p.compiler().length > 0) {
                    compiler = p.compiler();
                }
                if (p.linkpath().length > 0) {
                    linkpath = p.linkpath();
                }
                if (p.link().length > 0) {
                    link = p.link();
                }
                if (p.frameworkpath().length > 0) {
                    frameworkpath = p.frameworkpath();
                }
                if (p.framework().length > 0) {
                    framework = p.framework();
                }
                if (p.preloadpath().length > 0) {
                    preloadpath = p.preloadpath();
                }
                if (p.preload().length > 0) {
                    preload = p.preload();
                }
                if (p.library().length() > 0) {
                    library = p.library();
                }
            }
        }
        addAll("platform.pragma", pragma);
        addAll("platform.define", define);
        addAll("platform.include", include);
        addAll("platform.cinclude", cinclude);
        addAll("platform.includepath", includepath);
        addAll("platform.compiler.*", compiler);
        addAll("platform.linkpath", linkpath);
        addAll("platform.link", link);
        addAll("platform.frameworkpath", frameworkpath);
        addAll("platform.framework", framework);
        addAll("platform.preloadpath", preloadpath);
        addAll("platform.preload", preload);
        setProperty("platform.library", library);
        if (platforms != null && platforms.length > 0) {
            this.loaded = true;
        }
    }

    public List<Class> getInheritedClasses() {
        return this.inheritedClasses;
    }

    public List<Class> getEffectiveClasses() {
        return this.effectiveClasses;
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}
